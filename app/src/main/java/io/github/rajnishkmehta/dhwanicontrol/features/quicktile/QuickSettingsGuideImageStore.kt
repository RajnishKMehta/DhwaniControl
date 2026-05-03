package io.github.rajnishkmehta.dhwanicontrol.features.quicktile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URI
import java.security.MessageDigest

class QuickSettingsGuideImageStore(context: Context) {

    private val imageDirectory = File(context.filesDir, DIRECTORY_NAME)

    fun loadBitmap(imageUrl: String): Result<Bitmap> = runCatching {
        checkInterrupted()
        val imageFile = ensureImageFile(imageUrl)
        checkInterrupted()

        val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
        if (Thread.interrupted()) throw InterruptedException("Image load interrupted.")

        if (bitmap == null && imageFile.length() > 0) {
            // Corrupted cached file detected; delete and retry once
            imageFile.delete()
            val redownloadedFile = ensureImageFile(imageUrl)
            checkInterrupted()
            BitmapFactory.decodeFile(redownloadedFile.absolutePath)
                ?: error("Saved guide image could not be decoded.")
        } else {
            bitmap ?: error("Saved guide image could not be decoded.")
        }
    }

    private fun ensureImageFile(imageUrl: String): File {
        checkInterrupted()

        val imageFile = File(imageDirectory, imageUrl.toStableFileName())
        if (imageFile.isFile && imageFile.length() > 0L) {
            return imageFile
        }

        imageDirectory.mkdirs()
        val tempFile = File.createTempFile("${imageFile.name}.download.", ".tmp", imageDirectory)

        val connection = (URI(imageUrl).toURL().openConnection() as HttpURLConnection).apply {
            connectTimeout = CONNECT_TIMEOUT_MS
            readTimeout = READ_TIMEOUT_MS
            instanceFollowRedirects = true
            requestMethod = "GET"
        }

        try {
            if (connection.responseCode !in 200..299) {
                tempFile.delete()
                error("Guide image download failed.")
            }

            connection.inputStream.use { input ->
                tempFile.outputStream().use { output ->
                    copyInterruptibly(input, output)
                }
            }

            if (Thread.interrupted()) throw InterruptedException("Image download interrupted.")
            if (tempFile.length() == 0L) {
                tempFile.delete()
                error("Guide image download failed.")
            }

            if (!tempFile.renameTo(imageFile)) {
                tempFile.copyTo(imageFile, overwrite = true)
                tempFile.delete()
            }
            return imageFile
        } catch (exception: Exception) {
            tempFile.delete()
            throw exception
        } finally {
            connection.disconnect()
        }
    }

    private fun copyInterruptibly(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        while (true) {
            if (Thread.interrupted()) throw InterruptedException("Image download interrupted.")
            val bytesRead = input.read(buffer)
            if (bytesRead < 0) break
            output.write(buffer, 0, bytesRead)
        }
    }

    private fun checkInterrupted() {
        if (Thread.interrupted()) throw InterruptedException("Image load interrupted.")
    }

    private fun String.toStableFileName(): String {
        val digest = MessageDigest.getInstance("SHA-256")
            .digest(toByteArray(Charsets.UTF_8))
            .joinToString(separator = "") { byte -> "%02x".format(byte) }
        val extension = substringAfterLast('/', missingDelimiterValue = "")
            .substringBefore('?')
            .substringAfterLast('.', missingDelimiterValue = WEBP_EXTENSION)
            .filter { character -> character.isLetterOrDigit() }
            .takeIf { it.isNotBlank() && it.length <= MAX_EXTENSION_LENGTH }
            ?: WEBP_EXTENSION

        return "$digest.$extension"
    }

    private companion object {
        const val DIRECTORY_NAME = "quick_tile_guide"
        const val WEBP_EXTENSION = "webp"
        const val MAX_EXTENSION_LENGTH = 5
        const val CONNECT_TIMEOUT_MS = 10_000
        const val READ_TIMEOUT_MS = 15_000
    }
}
