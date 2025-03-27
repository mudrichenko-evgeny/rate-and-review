package com.mudrichenkoevgeny.core.storage.datastore

import androidx.datastore.core.Serializer
import com.mudrichenkoevgeny.core.storage.extensions.deserializeFromStream
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object AppPreferencesSerializer : Serializer<AppPreferences> {

    override val defaultValue: AppPreferences = AppPreferences()

    override suspend fun readFrom(input: InputStream): AppPreferences = runCatching {
        input.deserializeFromStream(AppPreferences::class)
    }.getOrElse { AppPreferences() }

    override suspend fun writeTo(t: AppPreferences, output: OutputStream) =
        output.write(
            Json.encodeToString(serializer = AppPreferences.serializer(), value = t).toByteArray()
        )
}