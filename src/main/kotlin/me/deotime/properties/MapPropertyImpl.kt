package me.deotime.properties

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import me.deotime.Storage

internal class MapPropertyImpl<K, V>(
    override val name: String,
    override val storage: Storage,
    private val keySerializer: KSerializer<K>,
    private val valueKSerializer: KSerializer<V>
) : Storage.Property.Map<K, V>, Storage.Property.Collection<Pair<K, V>> by CollectionPropertyImpl(name, storage, mapPropertySerializer()) {
    override suspend fun get(key: K) {
        TODO("Not yet implemented")
    }

    override suspend fun set(key: K, value: V?) {
        TODO("Not yet implemented")
    }

    companion object {

        // THIS WILL NEVER BE USED
        @Suppress("UNCHECKED_CAST")
        private fun <K, V> mapPropertySerializer() = object : KSerializer<Nothing> {
            override val descriptor: SerialDescriptor get() = error("Map cannot be serialized normally.")
            override fun serialize(encoder: Encoder, value: Nothing) = error("Map can not be serialized normally.")
            override fun deserialize(decoder: Decoder) = error("Map can not be deserialized normally.")
        } as KSerializer<Pair<K, V>>

    }
}