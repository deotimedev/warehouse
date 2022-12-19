package me.deotime.warehouse.properties

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import me.deotime.warehouse.Storage

internal class ListPropertyImpl<T>(
    override val name: String,
    override val storage: Storage,
    private val serializer: KSerializer<T>
) : Storage.Property.List<T>, AbstractProperty() {

    private val delegate = MapPropertyImpl<Int, T>(name, storage, serializer(), serializer)

    // this might go really badly
    private suspend fun index() =
        sync { location.listFiles()?.getOrNull((delegate.files().size - 1).coerceAtMost(0))?.name?.toIntOrNull() ?: 0 }

    override suspend fun add(element: T) {
        delegate.set(index(), element)
    }

    override suspend fun get(index: Int) =
        sync { files().getOrNull(0)?.let { serializer.deserialize(it) } }

    override suspend fun collect(collector: FlowCollector<T>) = sync {
        flow {
            for (item in files()) emit(serializer.deserialize(item))
        }.collect(collector)
    }
}