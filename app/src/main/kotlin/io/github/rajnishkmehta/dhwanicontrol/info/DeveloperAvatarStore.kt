package io.github.rajnishkmehta.dhwanicontrol.info

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.net.HttpURLConnection
import java.net.URI

class DeveloperAvatarStore(context: Context) {

    private val avatarDirectory = File(context.filesDir, DIRECTORY_NAME)
    private val avatarFile = File(avatarDirectory, AVATAR_FILE_NAME)

    fun loadSavedBitmap(): Bitmap? {
        if (!avatarFile.isFile || avatarFile.length() == 0L) {
            return null
        }

        val bitmap = BitmapFactory.decodeFile(avatarFile.absolutePath)
        if (bitmap == null) {
            avatarFile.delete()
            return null
        }

        return bitmap
    }

    fun refreshAvatar(avatarUrl: String): Result<Bitmap> = runCatching {
        val tempFile = downloadToTemp(avatarUrl)

        val bitmap = BitmapFactory.decodeFile(tempFile.absolutePath)
            ?: error("Downloaded avatar could not be decoded.")

        avatarDirectory.mkdirs()
        if (!tempFile.renameTo(avatarFile)) {
            tempFile.copyTo(avatarFile, overwrite = true)
            tempFile.delete()
        }

        bitmap
    }

    private fun downloadToTemp(avatarUrl: String): File {
        avatarDirectory.mkdirs()
        val tempFile = File.createTempFile("avatar.", ".tmp", avatarDirectory)

        val connection = (URI(avatarUrl).toURL().openConnection() as HttpURLConnection).apply {
            connectTimeout = CONNECT_TIMEOUT_MS
            readTimeout = READ_TIMEOUT_MS
            instanceFollowRedirects = true
            requestMethod = "GET"
        }

        try {
            if (connection.responseCode !in 200..299) {
                tempFile.delete()
                error("Avatar download failed.")
            }

            connection.inputStream.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            if (tempFile.length() == 0L) {
                tempFile.delete()
                error("Avatar download returned empty content.")
            }

            return tempFile
        } catch (exception: Exception) {
            tempFile.delete()
            throw exception
        } finally {
            connection.disconnect()
        }
    }

    private companion object {
        const val DIRECTORY_NAME = "developer_profile"
        const val AVATAR_FILE_NAME = "avatar.webp"
        const val CONNECT_TIMEOUT_MS = 10_000
        const val READ_TIMEOUT_MS = 15_000
    }
}
