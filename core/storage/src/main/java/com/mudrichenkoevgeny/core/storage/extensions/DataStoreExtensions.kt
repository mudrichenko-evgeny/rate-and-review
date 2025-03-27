package com.mudrichenkoevgeny.core.storage.extensions

import androidx.datastore.core.CorruptionException
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.io.InputStream
import kotlin.reflect.KClass

@OptIn(InternalSerializationApi::class)
fun <T : Any> InputStream.deserializeFromStream(classWithSerializer: KClass<T>): T = try {
    val serializer: KSerializer<T> = classWithSerializer.serializer()
    Json.decodeFromString(serializer, this.readBytes().decodeToString())
} catch (e: SerializationException) {
    throw CorruptionException("Unable to read ${classWithSerializer.simpleName}", e)
} catch (e: Exception) {
    throw CorruptionException("No serializer found for ${classWithSerializer.simpleName}", e)
}