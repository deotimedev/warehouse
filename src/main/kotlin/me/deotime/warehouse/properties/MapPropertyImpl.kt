package me.deotime.warehouse.properties

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.zip
import kotlinx.serialization.KSerializer
import me.deotime.warehouse.Storage
import java.io.File

internal class MapPropertyImpl<K, V>(
    override val name: String,
    override val storage: Storage,
    private val keySerializer: KSerializer<K>,
    private val valueKSerializer: KSerializer<V>
) : Storage.Property.Map<K, V>, AbstractProperty() {

    override suspend fun get(key: K) = sync {
        File(location, "${key.hashCode()}").takeIf { it.exists() }
            ?.let { valueKSerializer.deserialize(File(it, "value")) }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun set(key: K, value: V?): Unit = sync {
        val hash = key.hashCode()
        value?.let {
            val entry = File(location, "$hash").apply { mkdirs() }
            File(entry, "key").writeText(keySerializer.serialize(key))
            File(entry, "value").writeText(valueKSerializer.serialize(it))
        } ?: File(location, "$hash").takeIf { it.exists() }?.delete()
    }

    override suspend fun keys() = sync {
        flow {
            for (item in files()) emit(keySerializer.deserialize(File(item, "key")))
        }
    }

    override suspend fun values() = sync {
        flow {
            for (item in files()) emit(valueKSerializer.deserialize(File(item, "value")))
        }
    }

    override suspend fun collect(collector: FlowCollector<Pair<K, V>>) {
        flow { emitAll(keys().zip(values()) { a, b -> a to b }) }.collect(collector)
    }

}