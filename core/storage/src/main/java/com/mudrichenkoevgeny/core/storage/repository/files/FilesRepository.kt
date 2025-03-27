package com.mudrichenkoevgeny.core.storage.repository.files

import android.net.Uri
import java.io.File

interface FilesRepository {
    suspend fun getFileFromUri(uri: Uri?): File?
    suspend fun deleteCachedImageFile(file: File)
}