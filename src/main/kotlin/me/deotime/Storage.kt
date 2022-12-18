package me.deotime

import co.q64.raindrop.optics.Getter
import co.q64.raindrop.optics.Lens
import kotlinx.coroutines.flow.Flow
import me.deotime.properties.PropertyFactory
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.typeOf

interface Storage {

    val root: String
    val name: String

    sealed interface Property<Self : Property<Self>> {

        val name: String
        val storage: Storage

        @Suppress("UNCHECKED_CAST")
        operator fun getValue(ref: Any?, prop: KProperty<*>) = this as Self

        interface Delegate<T : Property<*>> {
            operator fun provideDelegate(ref: Storage, prop: KProperty<*>): T
        }

        interface Single<T> : Property<Single<T>> {

            suspend fun get(): T
            suspend infix fun <U> get(getter: Getter<T, U>) = getter.view(get())

            suspend fun <U> set(setter: Lens.Simple<T, U>, value: U) = update(setter) { value }
            suspend infix fun set(value: T)

        }

        interface Collection<T> : Flow<T> {

            suspend fun size(): Int

        }

        interface Map<K, V> : Property<Map<K, V>>, Collection<Pair<K, V>> {

            suspend infix fun get(key: K): V?
            suspend fun set(key: K, value: V?)

            suspend fun keys(): Flow<K>
            suspend fun values(): Flow<V>

        }

        interface List<T> : Property<List<T>> {

            suspend fun add(element: T)
            suspend fun get(index: Int): T?

        }


    }


    data class Update<T>(
        val before: T,
        val after: T
    )


}


fun <T> Storage.property(type: KType, default: T) = PropertyFactory.createProperty(type, default)
fun <T> Storage.list(type: KType) = PropertyFactory.createList<T>(type)
fun <K, V> Storage.map(keyType: KType, valueType: KType) = PropertyFactory.createMap<K, V>(keyType, valueType)

inline fun <reified T> Storage.property() = property<T?>(null)
inline fun <reified T> Storage.property(default: T) = property(typeOf<T>(), default)
inline fun <reified T> Storage.list() = list<T>(typeOf<T>())
inline fun <reified K, reified V> Storage.map() = map<K, V>(typeOf<K>(), typeOf<V>())


suspend inline fun <T, U> Storage.Property.Single<T>.update(lens: Lens.Simple<T, U>, closure: (U) -> U): Storage.Update<T> =
    get().let { Storage.Update(it, lens.set(it, lens.view(it).let(closure)).also { set(it) }) }

suspend inline infix fun <T> Storage.Property.Single<T>.update(closure: (T) -> T) = update(Lens.id(), closure)