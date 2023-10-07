package ru.ragefalcon.sharedcode.source.disk

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

actual class FileMP {
    private var myfile: File? = null

    actual companion object {
        actual fun getFileSeparator(): String = File.separator
        actual fun getSystemDir(): String = System.getProperty("user.dir")
        actual fun deleteFile(path: String) {
            val tmpfile = File(path)
            if (tmpfile.exists()) tmpfile.delete()
        }

        actual fun deleteDirectory(path: String) {
            val tmpfile = File(path)
            if (tmpfile.exists()) tmpfile.deleteRecursively()
        }
    }

    actual fun length(): Long? = myfile?.length()
    actual fun openFileOutput(path: String) {
        val fileName = path
        myfile = File(fileName)
        myfile?.let { file ->
            if (!file.parentFile.exists()) Files.createDirectory(Paths.get(file.parentFile.path))
        }
        myfile?.createNewFile()
        myfile?.writeBytes(ByteArray(0))

    }

    actual fun openFileInput(path: String) {
        val fileName = path
        myfile = File(fileName)
        myfile?.let { file ->
            if (!file.parentFile.exists()) Files.createDirectory(Paths.get(file.parentFile.path))
        }
    }

    actual fun openFileOutput(dbArgs: DbArgs, path: String) {
        val fileName = "${dbArgs.path}${File.separator}$path"
        myfile = File(fileName)
        myfile?.let { file ->
            if (!file.parentFile.exists()) Files.createDirectory(Paths.get(file.parentFile.path))
        }
        myfile?.createNewFile()
        myfile?.writeBytes(ByteArray(0))

    }

    actual fun openFileInput(dbArgs: DbArgs, path: String) {
        val fileName = "${dbArgs.path}${File.separator}$path"
        myfile = File(fileName)
        myfile?.let { file ->
            if (!file.parentFile.exists()) Files.createDirectory(Paths.get(file.parentFile.path))
        }
    }


    actual fun writeFile(byteArray: ByteArray) {
        myfile?.appendBytes(byteArray)

    }

    actual fun closeFile() {
    }

    actual fun getFileStream(): ByteArray? {
        if (myfile?.exists() == true) return myfile?.readBytes()
        return null
    }
}