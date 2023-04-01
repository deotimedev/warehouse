package me.deotime.warehouse.util

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

@JvmInline
internal value class ReentrantMutex(
    private val mutex: Mutex = Mutex()
) {

    suspend fun <T> withLock(closure: suspend () -> T): T {
        val key = Key(this)
        return if (coroutineContext[key] != null) closure()
        else withContext(Value(key)) {
            mutex.withLock { closure() }
        }
    }

    @JvmInline
    private value class Key(val mutex: ReentrantMutex) : CoroutineContext.Key<Value>
    @JvmInline
    private value class Value(override val key: Key) : CoroutineContext.Element
}