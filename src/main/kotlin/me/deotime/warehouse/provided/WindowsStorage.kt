package me.deotime.warehouse.provided

import me.deotime.warehouse.Storage
import java.io.File

internal object WindowsEnvironment {
    const val Appdata = "APPDATA"
    const val ProgramData = "ALLUSERSPROFILE"
    const val ProgramFiles = "PROGRAMFILES"
    const val ProgramFilesx86 = "PROGRAMFILES(x86)"
    const val User = "HOMEPATH"
}

sealed interface WindowsStorage : Storage {
    val env: String
    override val root get() = runCatching {
        System.getenv(env) + File.separator
    }.getOrElse { error("No environment variable found for directory $env. Note that this storage type only works on Windows operating systems.") }

    interface Appdata : WindowsStorage {
        override val env get() = WindowsEnvironment.Appdata
    }

    interface ProgramData : WindowsStorage {
        override val env get() = WindowsEnvironment.ProgramData
    }

    interface ProgramFiles : WindowsStorage {
        override val env: String get() = WindowsEnvironment.ProgramFiles
    }

    interface ProgramFilesx86 : WindowsStorage {
        override val env: String get() = WindowsEnvironment.ProgramFilesx86
    }

    interface User : WindowsStorage {
        override val env: String get() = WindowsEnvironment.User
    }
}

