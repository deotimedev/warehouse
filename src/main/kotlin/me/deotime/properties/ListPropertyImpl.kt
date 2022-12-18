package me.deotime.properties

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.fold
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import me.deotime.Storage
import java.io.File
import java.util.UUID
import kotlin.math.max

internal open class ListPropertyImpl<T>(
    override val name: String,
    override val storage: Storage,
    private val serializer: KSerializer<T>
) : Storage.Property.List<T>, MapPropertyImpl<Int, T>(name, storage, serializer(), serializer) {

    // this might go really badly
    private suspend fun index() = sync { location.listFiles()?.getOrNull((size() - 1).coerceAtMost(0))?.name?.toIntOrNull() ?: 0 }

    override suspend fun add(element: T) {
        set(index(), element)
    }

    override suspend fun get(key: Int) = sync { location.listFiles()?.getOrNull(0)?.let { serializer.deserialize(it) } }

}