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

    private val file = File(location, name).apply {
        createNewFile()
        write(default)
    }

    override suspend fun get() = sync { read() }

    override suspend fun set(value: T) = sync { write(value) }

    private fun read() = serializer.deserialize(file)

    private fun write(value: T) {
        file.writeText(serializer.serialize(value))
    }
}