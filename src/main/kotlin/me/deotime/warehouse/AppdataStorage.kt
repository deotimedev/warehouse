package me.deotime.warehouse

import java.io.File

interface AppdataStorage : Storage {

    override val root: String
        get() = AppdataDirectory ?: error("Appdata directory could not be found.")

    companion object {
        internal val AppdataDirectory = runCatching {
            System.getenv("APPDATA") + File.separator
        }.getOrNull()
    }

}