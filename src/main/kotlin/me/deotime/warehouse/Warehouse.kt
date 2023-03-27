package me.deotime.warehouse

import co.q64.raindrop.optics.Getter
import co.q64.raindrop.optics.Setter
import kotlinx.coroutines.flow.Flow
import me.deotime.warehouse.properties.PropertyFactory
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.typeOf

interface Warehouse {

    val root: String
    val name: String

    sealed interface Property<Self : Property<Self>> {

        val name: String
        val warehouse: Warehouse

        @Suppress("UNCHECKED_CAST")
        operator fun getValue(ref: Any?, prop: KProperty<*>) = this as Self

        interface Delegate<T : Property<*>> {
            operator fun provideDelegate(ref: Warehouse, prop: KProperty<*>): T
        }

        interface Single<T> : Property<Single<T>> {

            suspend fun get(): T
            suspend operator fun invoke() = get()
            suspend infix fun set(value: T)
            suspend infix fun update(closure: (T) -> T) = get().let {
                val update = closure(it)
                set(update)
                Update(it, update)
            }


            suspend infix fun <U> get(getter: Getter<T, U>) = getter.view(get())
            suspend fun <U> set(setter: Setter.Simple<T, U>, value: U) = update(setter) { value }
            suspend fun <U> update(setter: Setter.Simple<T, U>, closure: (U) -> U) = update { setter.over(it, closure) }

        }

        interface Collection {
            suspend fun size(): Int
        }

        interface Map<K, V> : Property<Map<K, V>>, Flow<Pair<K, V>>, Collection {

            suspend infix fun get(key: K): V?
            suspend fun get(key: K, default: () -> V) = get(key) ?: default().also { set(key, it) }
            suspend fun set(key: K, value: V?)
            suspend fun update(key: K, closure: (V?) -> V?) = get(key).let {
                val update = closure(it)
                set(key, update)
                Update(it, update)
            }

            suspend fun update(key: K, default: () -> V, closure: (V) -> V) = update(key) { closure((it ?: default())) }

            suspend fun <U> get(key: K, getter: Getter.With.Empty<V, U>) =
                get(key) { getter.empty().empty() }.let { getter.view(it) }

            suspend fun <U> update(key: K, setter: Setter.With.Empty.Simple<V, U>, closure: (U) -> U) =
                update(key, { setter.empty().empty() }) { setter.over(it, closure) }

            suspend fun keys(): Flow<K>
            suspend fun values(): Flow<V>

        }

        interface List<T> : Property<List<T>>, Flow<T>, Collection {

            suspend fun add(element: T)
            suspend fun get(index: Int): T?

            suspend operator fun plusAssign(element: T) = add(element)

        }


    }


    data class Update<T>(
        val before: T,
        val after: T
    )


}


fun <T> Warehouse.property(type: KType, default: () -> T) = PropertyFactory.createProperty(type, default)
fun <T> Warehouse.list(type: KType) = PropertyFactory.createList<T>(type)
fun <K, V> Warehouse.map(keyType: KType, valueType: KType) = PropertyFactory.createMap<K, V>(keyType, valueType)

inline fun <reified T> Warehouse.property() = property<T?>(null)
inline fun <reified T> Warehouse.property(default: T) = property(typeOf<T>()) { default }
inline fun <reified T> Warehouse.property(noinline default: () -> T) = property(typeOf<T>(), default)
inline fun <reified T> Warehouse.list() = list<T>(typeOf<T>())
inline fun <reified K, reified V> Warehouse.map() = map<K, V>(typeOf<K>(), typeOf<V>())