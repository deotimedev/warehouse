package me.deotime.warehouse

import java.io.File

interface AppdataStorage : Storage {

    override val root: String
        get() = AppdataDirectory

    companion object {
        val AppdataDirectory = System.getenv("APPDATA") + File.separator
    }

}