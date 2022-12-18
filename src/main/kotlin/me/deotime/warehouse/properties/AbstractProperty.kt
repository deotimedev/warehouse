package me.deotime.warehouse.properties

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import me.deotime.warehouse.Storage
import java.io.File

internal abstract class AbstractProperty<T> {

    abstract val name: String
    abstract val storage: Storage

    private val mutex = Mutex()
    protected val location by lazy {
        File(File(storage.root, storage.name), name)
    }

    protected suspend fun <R> sync(closure: suspend () -> R) =
        withContext(Dispatchers.IO) {
            mutex.withLock { closure() }
        }

    companion object {
        fun <A> KSerializer<A>.serialize(value: A) = Json.encodeToString(this, value)
        fun <A> KSerializer<A>.deserialize(data: File) = deserialize(data.readText())
        private fun <A> KSerializer<A>.deserialize(data: String) = Json.decodeFromString(this, data)
    }
}