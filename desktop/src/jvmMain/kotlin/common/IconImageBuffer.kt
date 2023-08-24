package common

import MainTabs.imageByteToBufferImage
import MainTabs.imageFromByteArray
import MainTabs.saveImage
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.soywiz.korim.format.ImageOrientation
import java.io.File

class IconImageBuffer(){

    private val cropImage: MutableState<ByteArray?> = mutableStateOf(null)
    val cropImageBitmap: MutableState<ImageBitmap?> = mutableStateOf(null)

    private var extension: String =  ""
    private val imageOrientation: MutableState<ImageOrientation?> = mutableStateOf(null)

    fun setBuffer(byteArray: ByteArray, ext: String, imgOrientation: ImageOrientation){
        cropImage.value = byteArray
        extension = ext
        imageOrientation.value = imgOrientation
        getCropImageBitmap()?.let {
            cropImageBitmap.value = it
        }
    }

    fun copy(outIcon: IconImageBuffer){
        cropImage.value?.let { byteA ->
            imageOrientation.value?.let { orien ->
                if (extension != ""){
                    outIcon.setBuffer(byteA,extension,orien)
                }
            }
        }
    }

    fun saveIconFile(pathname: String, quality: Float = 0.95f): Boolean{
        val ext = extension.uppercase()
        if (ext == "PNG"){
            cropImage.value?.let {
                File(pathname).writeBytes(it)
                return true
            }
        }
        if (ext == "JPG" || ext == "JPEG"){
            cropImage.value?.let { barray ->
                imageOrientation.value?.let { orient ->
                    imageByteToBufferImage(barray to orient)?.let { buffImg ->
                        saveImage(buffImg, File(pathname),quality)
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun getCropImageBitmap(): ImageBitmap? {
        val ext = extension.uppercase()
        if (ext == "PNG"){
            cropImage.value?.let {
                return imageFromByteArray(it)
            }
        }
        if (ext == "JPG" || ext == "JPEG"){
            cropImage.value?.let { barray ->
                imageOrientation.value?.let { orient ->
                    return imageByteToBufferImage(barray to orient)?.toComposeImageBitmap()
                }
            }
        }
        return null
    }


}