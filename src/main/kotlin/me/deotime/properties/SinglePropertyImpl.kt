package me.deotime.properties

import kotlinx.serialization.KSerializer
import me.deotime.Storage
import java.io.File

internal class SinglePropertyImpl<T>(
    override val name: String,
    override val storage: Storage,
    private val serializer: KSerializer<T>,
    private val default: T
) : Storage.Property.Single<T>, AbstractProperty<T>() {

    private val file = File(location, name).createNewFile()

    override suspend fun get(): T {
        TODO("Not yet implemented")
    }

    override suspend fun set(value: T) {
        TODO("Not yet implemented")
    }
}