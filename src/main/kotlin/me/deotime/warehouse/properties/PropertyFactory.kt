package me.deotime.warehouse.properties

import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import me.deotime.warehouse.Warehouse
import kotlin.reflect.KProperty
import kotlin.reflect.KType

internal object PropertyFactory {
    fun <T> createProperty(type: KType, default: () -> T): Warehouse.Property.Delegate<Warehouse.Property.Single<T>> =
        PropertyInitializer.Single(type, default)

    fun <T> createList(type: KType): Warehouse.Property.Delegate<Warehouse.Property.List<T>> =
        PropertyInitializer.List(type)

    fun <K, V> createMap(keyType: KType, valueKType: KType): Warehouse.Property.Delegate<Warehouse.Property.Map<K, V>> =
        PropertyInitializer.Map(keyType, valueKType)

    internal sealed interface PropertyInitializer<T, P : Warehouse.Property<*>> : Warehouse.Property.Delegate<P> {
        override fun provideDelegate(ref: Warehouse, prop: KProperty<*>) = construct(prop.name, ref)

        fun construct(name: String, warehouse: Warehouse): P

        @Suppress("UNCHECKED_CAST")
        val KType.serializer
            get() = serializer(this) as KSerializer<T>


        class Single<T>(private val type: KType, private val default: () -> T) :
            PropertyInitializer<T, Warehouse.Property.Single<T>> {
            override fun construct(name: String, warehouse: Warehouse) =
                SinglePropertyImpl(name, warehouse, type.serializer, default)
        }

        class List<T>(private val type: KType) : PropertyInitializer<T, Warehouse.Property.List<T>> {
            override fun construct(name: String, warehouse: Warehouse) = ListPropertyImpl(name, warehouse, type.serializer)
        }


        class Map<K, V>(private val keyType: KType, private val valueKType: KType) :
            PropertyInitializer<Nothing, Warehouse.Property.Map<K, V>> {

            @Suppress("UNCHECKED_CAST")
            override fun construct(name: String, warehouse: Warehouse) =
                MapPropertyImpl(
                    name,
                    warehouse,
                    keyType.serializer as KSerializer<K>,
                    valueKType.serializer as KSerializer<V>
                )
        }
    }
}