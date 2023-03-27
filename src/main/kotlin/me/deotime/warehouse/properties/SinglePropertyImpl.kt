package me.deotime.warehouse.properties

import kotlinx.serialization.KSerializer
import me.deotime.warehouse.Warehouse
import java.io.File

internal class SinglePropertyImpl<T>(
    override val name: String,
    override val warehouse: Warehouse,
    private val serializer: KSerializer<T>,
    private val default: () -> T
) : Warehouse.Property.Single<T>, AbstractProperty() {

    init {
        location.apply {
            if (!exists()) {
                if (!parentFile.exists()) parentFile.mkdirs()
                createNewFile()
                write(default())
            }
        }
    }

    override suspend fun get() = sync { location.read() }

    override suspend fun set(value: T) = sync { location.write(value) }

    private fun File.read() = serializer.deserialize(this)

    private fun File.write(value: T) {
        writeText(serializer.serialize(value))
    }
}