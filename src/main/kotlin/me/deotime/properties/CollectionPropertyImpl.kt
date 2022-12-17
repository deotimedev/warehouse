package me.deotime.properties

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.deotime.Storage

internal class CollectionPropertyImpl<T>(
    override val name: String,
    override val storage: Storage
) : Storage.Property.Collection<T> {

    private val mutex = Mutex() // todo abstract mutex to superclass

    override val size: Int
        get() = TODO("Not yet implemented")

    override suspend fun add(element: T) {
        mutex.withLock {  }
        TODO("Not yet implemented")
    }

    override suspend fun collect(collector: FlowCollector<T>) {
        mutex.withLock {  }
        TODO("Not yet implemented")
    }
}