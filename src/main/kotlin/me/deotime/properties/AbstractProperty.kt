package me.deotime.properties

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import me.deotime.Storage
import java.io.File

internal abstract class AbstractProperty<T> {

    abstract val name: String
    abstract val storage: Storage

    private val mutex = Mutex()
    protected val location by lazy { File(storage.root, storage.name) }

    protected suspend fun <R> sync(closure: suspend () -> R) =
        withContext(Dispatchers.IO) { mutex.withLock { closure() } }

    protected fun <A> KSerializer<A>.serialize(value: A) = Json.encodeToString(this, value)
    fun <A> KSerializer<A>.deserialize(data: File) = Json.decodeFromString(this, data.readText())
}