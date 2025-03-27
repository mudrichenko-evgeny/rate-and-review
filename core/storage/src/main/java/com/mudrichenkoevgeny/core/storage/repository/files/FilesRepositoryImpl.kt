package com.mudrichenkoevgeny.core.storage.repository.files

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class FilesRepositoryImpl @Inject constructor(
    private val context: Context
): FilesRepository {

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override suspend fun getFileFromUri(uri: Uri?): File? {
        uri ?: return null
        return runCatching { this.cacheFileFromUri(context, uri) }.getOrNull()
    }

    override suspend fun deleteCachedImageFile(file: File) {
        val file = File(context.cacheDir, file.name)
        if (file.exists()) {
            file.delete()
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun cacheFileFromUri(context: Context, uri: Uri): File? {
        val contentResolver = context.contentResolver

        val fileName = getFileName(context, uri) ?: TEMP_FILE_NAME

        val inputStream = contentResolver.openInputStream(uri) ?: throw IOException("Cannot open input stream")
        val file = File(context.cacheDir, fileName)

        FileOutputStream(file).use { outputStream ->
            inputStream.copyTo(outputStream)
        }

        return file
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun getFileName(context: Context, uri: Uri): String? {
        var fileName: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    fileName = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        return fileName ?: uri.path?.substringAfterLast('/')
    }

    companion object {
        private const val TEMP_FILE_NAME = "temp_file"
    }
}