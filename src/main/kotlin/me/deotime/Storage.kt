package me.deotime

import co.q64.raindrop.optics.Getter
import co.q64.raindrop.optics.Lens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow


interface Storage {

    val root: String
    val name: String

    interface Property<T> {
        suspend fun get(): T
        suspend infix fun <U> get(getter: Getter<T, U>) = getter.view(get())

        suspend fun <U> set(setter: Lens.Simple<T, U>, value: U) = update(setter) { value }
        suspend infix fun set(value: T)
    }


    interface Collection<T> {

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

fun test() {
    listOf<String>().asFlow()
}

suspend inline fun <T, U> Storage.Property<T>.update(lens: Lens.Simple<T, U>, closure: (U) -> U): Unit =
    get().let { Storage.Update(it, lens.set(it, lens.view(it).let(closure)).also { set(it) }) }

suspend inline infix fun <T> Storage.Property<T>.update(closure: (T) -> T) = update(Lens.id(), closure)