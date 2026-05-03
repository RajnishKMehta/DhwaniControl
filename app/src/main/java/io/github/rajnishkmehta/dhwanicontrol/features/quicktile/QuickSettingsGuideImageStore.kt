package io.github.rajnishkmehta.dhwanicontrol.features.quicktile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.net.HttpURLConnection
import java.net.URI
import java.security.MessageDigest

class QuickSettingsGuideImageStore(context: Context) {

    private val imageDirectory = File(context.filesDir, DIRECTORY_NAME)

    fun loadBitmap(imageUrl: String): Result<Bitmap> = runCatching {
        val imageFile = ensureImageFile(imageUrl)
        BitmapFactory.decodeFile(imageFile.absolutePath)
            ?: error("Saved guide image could not be decoded.")
    }

    private fun ensureImageFile(imageUrl: String): File {
        val imageFile = File(imageDirectory, imageUrl.toStableFileName())
        if (imageFile.isFile && imageFile.length() > 0L) {
            return imageFile
        }

        imageDirectory.mkdirs()
        val tempFile = File(imageDirectory, "${imageFile.name}.download")

        val connection = (URI(imageUrl).toURL().openConnection() as HttpURLConnection).apply {
            connectTimeout = CONNECT_TIMEOUT_MS
            readTimeout = READ_TIMEOUT_MS
            instanceFollowRedirects = true
            requestMethod = "GET"
        }

        try {
            if (connection.responseCode !in 200..299) {
                error("Guide image download failed.")
            }

            connection.inputStream.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            if (tempFile.length() == 0L) {
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
