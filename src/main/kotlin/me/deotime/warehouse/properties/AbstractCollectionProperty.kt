package me.deotime.warehouse.properties

import me.deotime.warehouse.Warehouse

internal abstract class AbstractCollectionProperty<I> : AbstractProperty(), Warehouse.Property.Collection<I> {
    override suspend fun size() = sync { files().size }
}