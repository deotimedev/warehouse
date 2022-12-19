package me.deotime.warehouse.properties

import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import me.deotime.warehouse.Storage
import kotlin.reflect.KProperty
import kotlin.reflect.KType

internal object PropertyFactory {
    fun <T> createProperty(type: KType, default: () -> T): Storage.Property.Delegate<Storage.Property.Single<T>> =
        PropertyInitializer.Single(type, default)

    fun <T> createList(type: KType): Storage.Property.Delegate<Storage.Property.List<T>> =
        PropertyInitializer.List(type)

    fun <K, V> createMap(keyType: KType, valueKType: KType): Storage.Property.Delegate<Storage.Property.Map<K, V>> =
        PropertyInitializer.Map(keyType, valueKType)

    internal sealed interface PropertyInitializer<T, P : Storage.Property<*>> : Storage.Property.Delegate<P> {
        override fun provideDelegate(ref: Storage, prop: KProperty<*>) = construct(prop.name, ref)

        fun construct(name: String, storage: Storage): P

        @Suppress("UNCHECKED_CAST")
        val KType.serializer
            get() = serializer(this) as KSerializer<T>


        class Single<T>(private val type: KType, private val default: () -> T) :
            PropertyInitializer<T, Storage.Property.Single<T>> {
            override fun construct(name: String, storage: Storage) =
                SinglePropertyImpl(name, storage, type.serializer, default)
        }

        class List<T>(private val type: KType) : PropertyInitializer<T, Storage.Property.List<T>> {
            override fun construct(name: String, storage: Storage) = ListPropertyImpl(name, storage, type.serializer)
        }


        class Map<K, V>(private val keyType: KType, private val valueKType: KType) :
            PropertyInitializer<Nothing, Storage.Property.Map<K, V>> {

            @Suppress("UNCHECKED_CAST")
            override fun construct(name: String, storage: Storage) =
                MapPropertyImpl(
                    name,
                    storage,
                    keyType.serializer as KSerializer<K>,
                    valueKType.serializer as KSerializer<V>
                )
        }
    }
}