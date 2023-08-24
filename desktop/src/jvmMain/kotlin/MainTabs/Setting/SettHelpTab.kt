package MainTabs.Setting

import GooglePack.sess
import MainTabs.imageFromByteArray
import MainTabs.rotateBufferedImage
import MainTabs.saveImage
import MyDialog.MyDialogLayout
import MyShowMessage
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import common.MyTextButtStyle1
import extensions.BoxWithVScrollBar
import extensions.MyRectF
import extensions.getAbsolutRect
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.ImageInfo
import ru.ragefalcon.sharedcode.tests.testImageResize
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.InnerDialogs.InnerStartTriggerEnum
import tests.TestComposeCount
import viewmodel.MainDB
import viewmodel.StateVM
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.ImageWriter
import javax.imageio.stream.FileImageOutputStream


class SettHelpTab(val dialLay: MyDialogLayout) {

    val testComposeCount = TestComposeCount()

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun show() {
        Row{
            Column(Modifier
                .weight(1f)
//                .fillMaxSize()
            ) {
                MyTextButtStyle1("DirHome", modifier = Modifier.padding(start = 15.dp)) {
                    MyShowMessage(dialLay, StateVM.dirMain)
                }
                MyTextButtStyle1("testN", modifier = Modifier.padding(start = 15.dp)) {
                    MyShowMessage(
                        dialLay, "testMessage", hole = MyRectF(
                            StateVM.tmpVajnLayCoor?.getAbsolutRect() ?: Rect(
                                Offset(50f, 50f),
                                Size(100f, 100f)
                            )
                        )
                    )
                }
                MyTextButtStyle1("Стартовый диалог", modifier = Modifier.padding(start = 15.dp)) {
                    MainDB.addTime.startInnerTrigger(InnerStartTriggerEnum.StartTrigger)
                }

                MyTextButtStyle1("Очистить ReplicateRecord", modifier = Modifier.padding(start = 15.dp)) {
                    MainDB.sincFun.cleanReplicateRecord()
                }

                MyTextButtStyle1("Помощь", modifier = Modifier.padding(start = 15.dp)) {
                    MainDB.addTime.startInnerTrigger(InnerStartTriggerEnum.HelpOpis)
                }

                MyTextButtStyle1("Тестируемый диалог", modifier = Modifier.padding(start = 15.dp)) {
                    MainDB.addTime.startInnerTrigger(InnerStartTriggerEnum.TestTrigger)
                }

                MyTextButtStyle1("Редактор стилей", modifier = Modifier.padding(start = 15.dp)){
                    StateVM.openEditStyle.value = true
                }
            }
            BoxWithVScrollBar(Modifier.weight(1f).alpha(0.5f)) {
                testComposeCount.getComposable()
            }
        }
    }

}

//fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? { // File name like "image.png"
//    //create a file to write bitmap data
//    var file: File? = null
//    return try {
//        file = File(Environment.getExternalStorageDirectory().toString() + File.separator + fileNameToSave)
//        file.createNewFile()
//
//        //Convert bitmap to byte array
//        val bos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
//        val bitmapdata = bos.toByteArray()
//
//        //write the bytes in file
//        val fos = FileOutputStream(file)
//        fos.write(bitmapdata)
//        fos.flush()
//        fos.close()
//        file
//    } catch (e: Exception) {
//        e.printStackTrace()
//        file // it will return null
//    }
//}

fun ffff(imageData: ByteArray) {
    val bais = ByteArrayInputStream(imageData)
    val img1 = ImageIO.read(bais)
    val size = Dimension(img1.width, img1.height)
    // imageFromFile(iconFile.value).asSkiaBitmap().toBufferedImage()
    val img2 = imageFromByteArray(imageData).toAwtImage()
    val img = BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB)
    for (x in 0 until size.width){
        for (y in 0 until size.height) {
            img.setRGB(x, y, img1.getRGB(x,y))
        }
    }
    File("testffff0.png").writeBytes(imageData)
//    ImageIO.write(bais, "png", File("testffff0.png"))
//    img. img2.toBitmap()
//    rotateBufferedImage(img)?.let {
        saveImage(img, File("testffff0.jpg"),0.95f)
//    }
//    println(img1)
//    println("img1: ${img1.width}")
//    ImageIO.write(img1, "png", File("testffff1.png"))
//    ImageIO.write(img1, "jpg", File("testffff2.jpg"))
}

