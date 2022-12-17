package me.deotime

import co.q64.raindrop.Empty
import co.q64.raindrop.optics.Getter
import co.q64.raindrop.optics.Lens
import co.q64.raindrop.standard.algebra.orEmpty
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KProperty

interface Storage {

    val root: String
    val name: String

    interface Member {

        val name: String
        val storage: Storage

        operator fun getValue(ref: Any?, prop: KProperty<*>) = this

        interface Delegate<T : Member> {
            operator fun provideDelegate(ref: Storage, prop: KProperty<*>): T
        }

    }

    interface Property<T> : Member {

        suspend fun get(): T
        suspend infix fun <U> get(getter: Getter<T, U>) = getter.view(get())

        suspend fun <U> set(setter: Lens.Simple<T, U>, value: U) = update(setter) { value }
        suspend infix fun set(value: T)

    }


    interface Collection<T> : Member {

        val size: Int
        fun toFlow(): Flow<T>

    }


    interface Map<K, V> : Collection<Pair<K, V>> {

        suspend infix fun get(key: K)
        suspend fun set(key: K, value: V)

    }

    data class Update<T>(
        val before: T,
        val after: T
    )


}

inline fun <reified T> Storage.property(): Storage.Member.Delegate<Storage.Property<T?>> = property(null)
inline fun <reified T> Storage.property(default: T): Storage.Member.Delegate<Storage.Property<T>> = TODO()

suspend inline fun <T, U> Storage.Property<T>.update(lens: Lens.Simple<T, U>, closure: (U) -> U): Unit =
    get().let { Storage.Update(it, lens.set(it, lens.view(it).let(closure)).also { set(it) }) }

suspend inline infix fun <T> Storage.Property<T>.update(closure: (T) -> T) = update(Lens.id(), closure)