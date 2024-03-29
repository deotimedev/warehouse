package me.deotime.warehouse.properties

import kotlinx.serialization.KSerializer
import me.deotime.warehouse.Warehouse
import me.deotime.warehouse.util.createIfNotExists
import java.io.File

internal class ListPropertyImpl<T>(
    override val name: String,
    override val warehouse: Warehouse,
    private val serializer: KSerializer<T>
) : Warehouse.Property.List<T>, AbstractCollectionProperty<Int, T>() {

    override suspend fun add(element: T) {
        File(location, "${size()}")
            .apply { createIfNotExists(directory = false) }
            .writeText(serializer.serialize(element))
    }

    override suspend fun get(index: Int) =
        sync { File(location, "$index").takeIf { it.exists() }?.let { serializer.deserialize(it) } }

    override suspend fun items() =
        files().map { serializer.deserialize(it) }

    override suspend fun remove(at: Int) = super.remove(at).also {
        if (it) reorder()
    }

    private fun reorder() {
        files().sorted().forEachIndexed { i, file ->
            file.renameTo(File(file.parent, "$i"))
        }
    }
}