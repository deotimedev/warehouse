package me.deotime.properties

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.KSerializer
import me.deotime.Storage
import java.io.File
import java.util.UUID

internal open class CollectionPropertyImpl<T>(
    override val name: String,
    override val storage: Storage,
    private val serializer: KSerializer<T>
) : Storage.Property.Collection<T>, AbstractProperty<T>() {


    init {
        location.mkdirs()
    }

    override suspend fun size() = sync { location.list()?.size ?: 0 }

    override suspend fun add(element: T) {
        putItem(UUID.randomUUID().toString(), serializer.serialize(element))
    }

    override suspend fun collect(collector: FlowCollector<T>) = sync {
        flow {
            for(item in location.listFiles().orEmpty()) emit(serializer.deserialize(item))
        }.collect(collector)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun putItem(name: String, value: String) = sync {
        File(location, name)
            .apply { createNewFile() }
            .writeText(value)
    }
}