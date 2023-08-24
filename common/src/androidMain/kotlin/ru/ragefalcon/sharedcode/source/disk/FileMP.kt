package ru.ragefalcon.sharedcode.source.disk

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.squareup.sqldelight.db.SqlDriver
import io.ktor.utils.io.core.*
import io.ktor.utils.io.streams.*
import kotlinx.coroutines.CoroutineDispatcher
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths

actual class FileMP {
    var outputFile: File? = null
    var outputStream: FileOutputStream? = null
    var inputStream: FileInputStream? = null
    actual companion object {
        actual fun getFileSeparator(): String = File.separator
        actual fun getSystemDir(): String = System.getProperty("user.dir") ?: ""
        actual fun deleteFile(path: String){
            val tmpfile = File( path)
            if (tmpfile.exists()) tmpfile.delete()
        }
        actual fun deleteDirectory(path: String){
            val tmpfile = File(path)
            if (tmpfile.exists()) tmpfile.deleteRecursively()
        }
    }

    actual fun openFileOutput(path: String){
        try {
            outputFile = File(path)
            outputFile?.let { fileOut ->
                fileOut.parentFile?.let{ parentFile ->
                    if (!parentFile.exists()) File(parentFile.path).mkdir() // Files.createDirectory(Paths.get(file.parentFile.path))
                }
            }
            outputStream = FileOutputStream(outputFile)
        } catch (exception: Throwable) {
        }

    }

    actual fun openFileInput(path: String){
        try {
            outputFile = File(path)
            inputStream = FileInputStream(outputFile)
        } catch (exception: Throwable) {
        }

    }


    actual fun openFileOutput(dbArgs: DbArgs, path: String){
//        val inputStream = dbArgs.context.assets.open("$ASSETS_PATH/$DATABASE_NAME.db")
        Log.d("LoadBD", "Поток открыт")

        try {

            outputFile = File( dbArgs.context.getDatabasePath(path).path)
            outputFile?.let { fileOut ->
                fileOut.parentFile?.let{ parentFile ->
                    if (!parentFile.exists()) File(parentFile.path).mkdir() // Files.createDirectory(Paths.get(file.parentFile.path))
                }
            }
            Log.d("LoadBD", "Открыт выходной файл")
            outputStream = FileOutputStream(outputFile)
            Log.d("LoadBD", "Открыт поток в выходной файл")
//            inputStream.copyTo(outputStream)
//            Log.d("LoadBD", "База скопирована в выходной поток")
//            inputStream.close()

        } catch (exception: Throwable) {
//            throw RuntimeException("The $DATABASE_NAME database couldn't be installed.", exception)
        }

    }

    actual fun openFileInput(dbArgs: DbArgs, path: String){
//        val inputStream = dbArgs.context.assets.open("$ASSETS_PATH/$DATABASE_NAME.db")
        Log.d("LoadBD", "Поток открыт")

        try {

            outputFile = File( dbArgs.context.getDatabasePath(path).path)
            Log.d("LoadBD", "Открыт выходной файл")
            inputStream = FileInputStream(outputFile)
            Log.d("LoadBD", "Открыт поток в выходной файл")
//            inputStream.copyTo(outputStream)
//            Log.d("LoadBD", "База скопирована в выходной поток")
//            inputStream.close()

        } catch (exception: Throwable) {
//            throw RuntimeException("The $DATABASE_NAME database couldn't be installed.", exception)
        }

    }

    actual fun writeFile(byteArray: ByteArray) {
//        outputFile?.writeBytes(byteArray)
        outputStream?.write(byteArray)

    }
    actual fun closeFile() {
        outputStream?.flush()
        outputStream?.close()
        inputStream?.close()
    }

    actual fun getFileStream(): ByteArray?{
        outputFile?.let {
            println("Ktor testFile: ${it.name}")
            println("Ktor testFile: ${it.readBytes().size}")
//            println("Ktor testFile: ${inputStream?.readBytes()?.size}")
            return inputStream?.readBytes()
        } ?: return null
    }

}