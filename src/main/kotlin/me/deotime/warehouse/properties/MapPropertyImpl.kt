package me.deotime.warehouse.properties

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.KSerializer
import me.deotime.warehouse.Warehouse
import java.io.File

internal class MapPropertyImpl<K, V>(
    override val name: String,
    override val warehouse: Warehouse,
    private val keySerializer: KSerializer<K>,
    private val valueKSerializer: KSerializer<V>
) : Warehouse.Property.Map<K, V>, AbstractCollectionProperty<K, Pair<K, V>>() {

    override suspend fun get(key: K) = sync {
        File(location, "${key.hashCode()}").takeIf { it.exists() }
            ?.let { valueKSerializer.deserialize(File(it, "value")) }
    }

    override suspend fun set(key: K, value: V): Unit = sync {
        val hash = key.hashCode()
        val file = File(location, "$hash")

        val entry = file.apply { mkdirs() }
        File(entry, "key").writeText(keySerializer.serialize(key))
        File(entry, "value").writeText(valueKSerializer.serialize(value))
    }

    override suspend fun keys() = sync {
        files().map { keySerializer.deserialize(File(it, "key")) }
    }

    override suspend fun values() = sync {
        files().map { valueKSerializer.deserialize(File(it, "value")) }
    }

    override suspend fun items() = sync {
        coroutineScope {
            val keys = async { keys() }
            val values = async { values() }
            keys.await().zip(values.await())
        }
    }

}