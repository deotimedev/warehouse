package me.deotime.properties

import me.deotime.Storage

internal class MapPropertyImpl<K, V>(
    override val name: String,
    override val storage: Storage
) : Storage.Property.Map<K, V>, Storage.Property.Collection<Pair<K, V>> by CollectionPropertyImpl(name, storage) {
    override suspend fun get(key: K) {
        TODO("Not yet implemented")
    }

    override suspend fun set(key: K, value: V?) {
        TODO("Not yet implemented")
    }
}