package me.deotime.properties

import me.deotime.Storage
import kotlin.reflect.KType

internal object PropertyFactory {
    fun <T> createProperty(type: KType, default: T): Storage.Property.Delegate<Storage.Property.Single<T>> = TODO()

    fun <T> createCollection(type: KType): Storage.Property.Delegate<Storage.Property.Collection<T>> = TODO()

    fun <K, V> createMap(keyType: KType, valueKType: KType): Storage.Property.Delegate<Storage.Property.Map<K, V>> = TODO()
}