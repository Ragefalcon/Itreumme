package common

import MainTabs.imageFromFile
import MainTabs.imageFromFileScaleTest
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import ru.ragefalcon.sharedcode.tests.imageSliceForIcon
import java.io.File

class CropBoxForImageFile(file: File?, val outImage: IconImageBuffer = IconImageBuffer(), val defaultResource: String) {

    private val sourceFile =  mutableStateOf(file)

    var openFile: MutableState<Boolean> = mutableStateOf(sourceFile.value?.exists() ?: false)

    val sourceImage: MutableState<ImageBitmap>  = mutableStateOf(
            sourceFile.value?.let { if (it.exists()) imageFromFile(it) else useResource(
                defaultResource,
                ::loadImageBitmap
            )} ?: useResource(
                defaultResource,
                ::loadImageBitmap
            )
        )

    fun extension(): String = sourceFile.value?.extension ?: ""

    fun setFile(file: File) {
        if (file.exists()) {
            sourceFile.value = file
            sourceImage.value = imageFromFile(file) //imageFromFileScaleTest(file) //
            openFile.value = true
        }
    }

    suspend fun sliceForIcon(
        start: Float,
        top: Float,
        width: Float,
        height: Float,
        maxSize: Int?,
        square: Boolean
    ) {
        sourceFile.value?.let { icon ->
            val aa = imageSliceForIcon(
                icon.path,
                start,
                top,
                width,
                height,
                maxSize,
                square
            )
            outImage.setBuffer(aa.first,extension(),aa.second)
        }
    }

}

