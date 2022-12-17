package me.deotime.properties

import me.deotime.Storage

internal class SinglePropertyImpl<T>(
    override val name: String,
    override val storage: Storage
) : Storage.Property.Single<T> {
    override suspend fun get(): T {
        TODO("Not yet implemented")
    }

    override suspend fun set(value: T) {
        TODO("Not yet implemented")
    }
}