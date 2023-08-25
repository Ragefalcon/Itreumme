package ru.ragefalcon.sharedcode.source.disk

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

actual class FileMP {
    var outputFile: File? = null
    var outputStream: FileOutputStream? = null
    var inputStream: FileInputStream? = null

    actual companion object {
        actual fun getFileSeparator(): String = File.separator
        actual fun getSystemDir(): String = System.getProperty("user.dir") ?: ""
        actual fun deleteFile(path: String) {
            val tmpfile = File(path)
            if (tmpfile.exists()) tmpfile.delete()
        }

        actual fun deleteDirectory(path: String) {
            val tmpfile = File(path)
            if (tmpfile.exists()) tmpfile.deleteRecursively()
        }
    }

    actual fun openFileOutput(path: String) {
        try {
            outputFile = File(path)
            outputFile?.let { fileOut ->
                fileOut.parentFile?.let { parentFile ->
                    if (!parentFile.exists()) File(parentFile.path).mkdir()
                }
            }
            outputStream = FileOutputStream(outputFile)
        } catch (exception: Throwable) {
        }

    }

    actual fun openFileInput(path: String) {
        try {
            outputFile = File(path)
            inputStream = FileInputStream(outputFile)
        } catch (exception: Throwable) {
        }

    }


    actual fun openFileOutput(dbArgs: DbArgs, path: String) {
        try {
            outputFile = File(dbArgs.context.getDatabasePath(path).path)
            outputFile?.let { fileOut ->
                fileOut.parentFile?.let { parentFile ->
                    if (!parentFile.exists()) File(parentFile.path).mkdir()
                }
            }
            outputStream = FileOutputStream(outputFile)
        } catch (exception: Throwable) {
        }

    }

    actual fun openFileInput(dbArgs: DbArgs, path: String) {
        try {

            outputFile = File(dbArgs.context.getDatabasePath(path).path)
            inputStream = FileInputStream(outputFile)
        } catch (exception: Throwable) {
        }

    }

    actual fun writeFile(byteArray: ByteArray) {
        outputStream?.write(byteArray)

    }

    actual fun closeFile() {
        outputStream?.flush()
        outputStream?.close()
        inputStream?.close()
    }

    actual fun getFileStream(): ByteArray? {
        outputFile?.let {
            return inputStream?.readBytes()
        } ?: return null
    }

}