package me.deotime.warehouse.properties

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import me.deotime.warehouse.Warehouse
import me.deotime.warehouse.util.ReentrantMutex
import me.deotime.warehouse.util.createIfNotExists
import java.io.File

internal abstract class AbstractProperty {

    abstract val name: String
    abstract val warehouse: Warehouse
    open val directory = false

    private val mutex = ReentrantMutex()
    protected val location by lazy {
        val warehouse = File(warehouse.root, warehouse.name)
        warehouse.createIfNotExists(directory = true)
        File(warehouse, name).apply { createIfNotExists(directory = this@AbstractProperty.directory) }
    }

    fun files() = location.listFiles()?.toList().orEmpty()

    protected suspend fun <R> sync(dispatcher: CoroutineDispatcher = Dispatchers.IO, closure: suspend () -> R) =
        mutex.withLock {
            withContext(dispatcher) {
                closure()
            }
        }

    protected fun <A> KSerializer<A>.serialize(value: A) = Json.encodeToString(this, value)
    protected fun <A> KSerializer<A>.deserialize(data: File) = deserialize(data.readText())
    private fun <A> KSerializer<A>.deserialize(data: String) = Json.decodeFromString(this, data)

}