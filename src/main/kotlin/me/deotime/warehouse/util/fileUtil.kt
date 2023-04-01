package me.deotime.warehouse.util

import java.io.File

fun File.createIfNotExists(directory: Boolean = false) {
    if (!exists()) {
        if (!parentFile.exists()) parentFile.mkdirs()
        if (directory) mkdir() else createNewFile()
    }
}