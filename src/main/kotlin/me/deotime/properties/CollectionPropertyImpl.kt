package me.deotime.properties

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.KSerializer
import me.deotime.Storage

internal class CollectionPropertyImpl<T>(
    override val name: String,
    override val storage: Storage,
    private val serializer: KSerializer<T>
) : Storage.Property.Collection<T>, AbstractProperty<T>() {


    override val size: Int
        get() = TODO("Not yet implemented")

    override suspend fun add(element: T) {
        TODO("Not yet implemented")
    }

    override suspend fun collect(collector: FlowCollector<T>) {
        TODO("Not yet implemented")
    }
}