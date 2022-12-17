package me.deotime

import co.q64.raindrop.optics.Getter
import co.q64.raindrop.optics.Lens


interface Storage {

    val root: String
    val name: String

    interface Property<T> {
        suspend fun get(): T
        suspend infix fun <U> get(getter: Getter<T, U>) = getter.view(get())

        suspend fun <U> set(setter: Lens.Simple<T, U>, value: U) = update(setter) { value }
        suspend infix fun set(value: T)
    }


    interface Map<K, V> {

        suspend infix fun get(key: K)
        suspend fun set(key: K, value: V)

        interface List<T> : Map<Int, T>
    }

    data class Update<T>(
        val before: T,
        val after: T
    )


}

suspend inline fun <T, U> Storage.Property<T>.update(lens: Lens.Simple<T, U>, closure: (U) -> U): Unit =
    get().let { Storage.Update(it, lens.set(it, lens.view(it).let(closure)).also { set(it) }) }

suspend inline infix fun <T> Storage.Property<T>.update(closure: (T) -> T) = update(Lens.id(), closure)