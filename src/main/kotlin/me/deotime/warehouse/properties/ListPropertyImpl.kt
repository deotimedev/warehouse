package me.deotime.warehouse.properties

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import me.deotime.warehouse.Warehouse
import java.io.File

internal class ListPropertyImpl<T>(
    override val name: String,
    override val warehouse: Warehouse,
    private val serializer: KSerializer<T>
) : Warehouse.Property.List<T>, AbstractCollectionProperty<T>() {

    override suspend fun add(element: T) {
        File(location, "${size()}").writeText(serializer.serialize(element))
    }

    override suspend fun get(index: Int) =
        sync { files().getOrNull(0)?.let { serializer.deserialize(it) } }

    override suspend fun collect(collector: FlowCollector<T>) = sync {
        flow {
            for (item in files()) emit(serializer.deserialize(item))
        }.collect(collector)
    }
}