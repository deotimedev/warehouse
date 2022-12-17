package me.deotime.properties

import me.deotime.Storage
import kotlin.reflect.KProperty
import kotlin.reflect.KType

internal object PropertyFactory {
    fun <T> createProperty(type: KType, default: T): Storage.Property.Delegate<Storage.Property.Single<T>> = PropertyInitializer(::SinglePropertyImpl)
    fun <T> createCollection(type: KType): Storage.Property.Delegate<Storage.Property.Collection<T>> = PropertyInitializer(::CollectionPropertyImpl)
    fun <K, V> createMap(keyType: KType, valueKType: KType): Storage.Property.Delegate<Storage.Property.Map<K, V>> = PropertyInitializer(::MapPropertyImpl)

    internal class PropertyInitializer<T : Storage.Property>(private val construct: (String, Storage) -> T) : Storage.Property.Delegate<T> {
        override fun provideDelegate(ref: Storage, prop: KProperty<*>) = construct(prop.name, ref)
    }
}