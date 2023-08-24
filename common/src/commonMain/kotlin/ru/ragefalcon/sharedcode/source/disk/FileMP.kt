package ru.ragefalcon.sharedcode.source.disk

import com.squareup.sqldelight.db.SqlDriver
import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineDispatcher

expect class FileMP() {

    companion object {
        fun getFileSeparator(): String
        fun getSystemDir(): String
        fun deleteFile(path: String)
        fun deleteDirectory(path: String)
    }

    fun openFileOutput(path: String)
    fun openFileInput(path: String)

    fun openFileOutput(dbArgs: DbArgs, path: String)
    fun openFileInput(dbArgs: DbArgs, path: String)


    fun writeFile(byteArray: ByteArray)

    fun closeFile()

    fun getFileStream(): ByteArray?
}

fun getPathWithSeparator(elem: List<String>): String {
    var str = ""
    if (elem.size>1) {
        str = elem[0]
        for (i in 1 until elem.size) {
            str = "$str${FileMP.getFileSeparator()}${elem[i]}"
        }
    }   else    {
        if (elem.size == 1) str = elem[0]
    }
    return str
}
