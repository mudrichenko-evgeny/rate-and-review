package com.mudrichenkoevgeny.feature.user.datastore

import androidx.datastore.core.Serializer
import com.mudrichenkoevgeny.core.storage.extensions.deserializeFromStream
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object UserPreferencesSerializer : Serializer<UserPreferences> {

    override val defaultValue: UserPreferences = UserPreferences()

    override suspend fun readFrom(input: InputStream): UserPreferences = runCatching {
        input.deserializeFromStream(UserPreferences::class)
    }.getOrElse { UserPreferences() }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) =
        output.write(
            Json.encodeToString(serializer = UserPreferences.serializer(), value = t).toByteArray()
        )
}