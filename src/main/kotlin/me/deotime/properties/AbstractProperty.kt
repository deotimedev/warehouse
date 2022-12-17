package me.deotime.properties

import kotlinx.coroutines.sync.Mutex
import kotlinx.serialization.KSerializer
import me.deotime.Storage
import java.io.File

internal abstract class AbstractProperty<T> {

    abstract val name: String
    abstract val storage: Storage

    protected val mutex = Mutex()
    protected val location by lazy { File(storage.root, storage.name) }

}