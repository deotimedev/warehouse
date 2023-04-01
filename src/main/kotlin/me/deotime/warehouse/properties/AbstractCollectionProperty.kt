package me.deotime.warehouse.properties

import me.deotime.warehouse.Warehouse
import java.io.File

internal abstract class AbstractCollectionProperty<K, I> : AbstractProperty(), Warehouse.Property.Collection<K, I> {
    override val directory = true
    override suspend fun size() = sync { files().size }
    override suspend fun remove(at: K) = sync { File(location, "${at.hashCode()}").delete() }
    override suspend fun clear() {
        sync { location.listFiles()?.forEach { it.delete() } }
    }
}