package com.mudrichenkoevgeny.core.network.datastore

import androidx.datastore.core.Serializer
import com.mudrichenkoevgeny.core.storage.extensions.deserializeFromStream
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object NetworkSessionDataSerializer : Serializer<NetworkSessionData> {

    override val defaultValue: NetworkSessionData = NetworkSessionData()

    override suspend fun readFrom(input: InputStream): NetworkSessionData = runCatching {
        input.deserializeFromStream(NetworkSessionData::class)
    }.getOrElse { NetworkSessionData() }

    override suspend fun writeTo(t: NetworkSessionData, output: OutputStream) =
        output.write(
            Json.encodeToString(serializer = NetworkSessionData.serializer(), value = t).toByteArray()
        )
}