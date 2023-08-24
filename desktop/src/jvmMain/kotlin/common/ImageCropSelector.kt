package MainTabs.Avatar.Element

import MainTabs.OpenFileFilter
import MainTabs.saveImage
import MyDialog.MyDialogLayout
import MyShowMessage
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.soywiz.korim.awt.toBufferedImage
import common.CropBoxForImageFile
import common.EnumDiskretSeekBar
import common.MyTextButtStyle1
import extensions.*
import org.jetbrains.skiko.toBufferedImage
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.tabElement
import viewmodel.StateVM
import java.awt.Image
import java.io.File
import javax.swing.JFileChooser
import kotlin.math.roundToInt
import java.awt.image.BufferedImage
import java.util.*

enum class RatioTabsEnum(override val nameTab: String, val ratio: Double) : tabElement {
    rat16_9("16:9",16.0 / 9.0),
    rat3_2("3:2",3.0 / 2.0),
    rat3_1("3:1",3.0 / 1.0),
    rat1_1("1:1",1.0 / 1.0),
    Any("Любое",0.0);
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ImageCropSelector(//cropImage: MutableState<ImageBitmap?>,
    fileForCrop: CropBoxForImageFile, maxSize: Int? = 250, onlyJpg: Boolean = false, ratioWdivH: Double = 1.0
) { //Pair<ByteArray, ImageOrientation>?

    val dialogLayout = MyDialogLayout()
    val updateImage: MutableState<Boolean> = remember { mutableStateOf(true) }

//    var first by remember { mutableStateOf(false) }
    var ratio_WdivH by remember { mutableStateOf(ratioWdivH) }
//    if (first && ratioWdivH != 0.0) {
//        println("first && ratioWdivH")
//        ratio_WdivH = ratioWdivH
//        first =false
//    }

    val ratioSeekBar = remember {
        EnumDiskretSeekBar(RatioTabsEnum::class, RatioTabsEnum.Any) {
            ratio_WdivH = it.ratio
        }
    }

    val sufSize = 20.dp
    val offsetX = remember { mutableStateOf(10f) }
    val offsetY = remember { mutableStateOf(10f) }
    val offsetX2 = remember { mutableStateOf(10f) }
    val offsetY2 = remember { mutableStateOf(10f) }
//        val offsetX = remember { Animatable(30f) }
//        val offsetY = remember { Animatable(30f) }
//        val offsetX2 = remember { Animatable(100f) }
//        val offsetY2 = remember { Animatable(100f) }
    val imgWidth = remember { mutableStateOf(300) }
    val imgHeight = remember { mutableStateOf(300) }
    val imgPadd = 18.dp

    val start = remember { mutableStateOf(minOf(offsetX.value, offsetX2.value)) }
    val end = remember { mutableStateOf(maxOf(offsetX.value, offsetX2.value)) }
    val top = remember { mutableStateOf(minOf(offsetY.value, offsetY2.value)) }
    val bottom = remember { mutableStateOf(maxOf(offsetY.value, offsetY2.value)) }
    val boxSize = remember { mutableStateOf(minOf(end.value - start.value, bottom.value - top.value)) }
    var boxHeight by remember { mutableStateOf(bottom.value - top.value) }
    var boxWidth by remember { mutableStateOf(end.value - start.value) }


    with(LocalDensity.current) {

        fun height(): Float {
            val rez = if (ratio_WdivH == 0.0) {
                if (boxHeight >= 5f) boxHeight else 5f
            } else if (boxHeight * ratio_WdivH <= boxWidth) boxHeight else boxWidth / ratio_WdivH.toFloat()
            return if (ratio_WdivH == 0.0) rez else if (ratio_WdivH >= 1.0) {
                if (rez >= 5f) rez else 5f
            } else {
                if (rez >= 5f && rez * ratio_WdivH >= 5f) rez else 5f / ratio_WdivH.toFloat()
            }
        }

        fun width(): Float {
            val rez = if (ratio_WdivH == 0.0) {
                if (boxWidth >= 5f) boxWidth else 5f
            } else if (boxHeight * ratio_WdivH <= boxWidth) boxHeight * ratio_WdivH.toFloat() else boxWidth
            return if (ratio_WdivH == 0.0) rez else if (ratio_WdivH <= 1.0) {
                if (rez >= 5f) rez else 5f
            } else {
                if (rez >= 5f && rez / ratio_WdivH >= 5f) rez else 5f * ratio_WdivH.toFloat()
            }
        }

        fun checkAddValue(new: Float, max: Float, min: Float): Float {
            return maxOf(minOf(new, max), min)
        }

        fun setValueX(offset: MutableState<Float>, newValue: Float, box: Float = 0f, topOffset: Boolean = true) {

            offset.value = checkAddValue(
                newValue,
                imgWidth.value + imgPadd.toPx() - sufSize.toPx() / 2 - (if (topOffset) box else 0f),
                imgPadd.toPx() - sufSize.toPx() / 2 + (if (!topOffset) box else 0f)
            )
        }

        fun setValueY(offset: MutableState<Float>, newValue: Float, box: Float = 0f, topOffset: Boolean = true) {
            offset.value = checkAddValue(
                newValue,
                imgHeight.value + imgPadd.toPx() - sufSize.toPx() / 2 - (if (topOffset) box else 0f),
                imgPadd.toPx() - sufSize.toPx() / 2 + (if (!topOffset) box else 0f)
            )
        }

        fun addValueX(offset: MutableState<Float>, delta: Float, box: Float = 0f, topOffset: Boolean = true) {
            setValueX(offset, offset.value + delta, box, topOffset)
/*
            offset.value =
                checkAddValue(
                offset.value + delta,
                imgWidth.value + imgPadd.toPx() - sufSize.toPx() / 2 - (if (topOffset) box else 0f),
                imgPadd.toPx() - sufSize.toPx() / 2 + (if (!topOffset) box else 0f)
            )
*/
        }

        fun addValueY(offset: MutableState<Float>, delta: Float, box: Float = 0f, topOffset: Boolean = true) {
            setValueY(offset, offset.value + delta, box, topOffset)
/*
            offset.value = checkAddValue(
                offset.value + delta,
                imgHeight.value + imgPadd.toPx() - sufSize.toPx() / 2 - (if (topOffset) box else 0f),
                imgPadd.toPx() - sufSize.toPx() / 2 + (if (!topOffset) box else 0f)
            )
*/
        }

        LaunchedEffect(
            offsetX.value,
            offsetX2.value,
            offsetY.value,
            offsetY2.value,
            updateImage.value,
            imgWidth.value,
            imgHeight.value,
            ratio_WdivH
        ) {
            start.value = minOf(offsetX.value, offsetX2.value) + sufSize.toPx() / 2
            end.value = maxOf(offsetX.value, offsetX2.value) + sufSize.toPx() / 2
            top.value = minOf(offsetY.value, offsetY2.value) + sufSize.toPx() / 2
            bottom.value = maxOf(offsetY.value, offsetY2.value) + sufSize.toPx() / 2
            boxWidth = end.value - start.value
            boxHeight = bottom.value - top.value
            if (ratio_WdivH != 0.0) {
                if (boxHeight * ratio_WdivH <= boxWidth) {
                    start.value += (boxWidth - boxHeight * ratio_WdivH.toFloat()) / 2
                } else {
                    top.value += (boxHeight - boxWidth / ratio_WdivH.toFloat()) / 2
                }
/*
                if (boxWidth > boxHeight) {
                    start.value += (boxWidth - boxHeight) / 2
                } else {
                    top.value += (boxHeight - boxWidth) / 2
                }
*/
            }

            boxSize.value = if (minOf(boxWidth, boxHeight) > 10f) minOf(boxWidth, boxHeight) else 10f


            if (updateImage.value) {
                println("boxHeight.sliceForIcon = ${height()}")
                println("boxHeight.sliceForIcon = ${height() / imgHeight.value.toFloat()}")
                fileForCrop.sliceForIcon(
                    (start.value - imgPadd.toPx()) / imgWidth.value.toFloat(),
                    (top.value - imgPadd.toPx()) / imgHeight.value.toFloat(),
                    width() / imgWidth.value.toFloat(),
                    height() / imgHeight.value.toFloat(),
//                    boxSize.value / imgWidth.value.toFloat(),
//                    boxSize.value / imgHeight.value.toFloat(),
                    maxSize,
                    ratio_WdivH == 1.0
                )
            }
        }

        fun loadImageFromBuffer(): Boolean = getImageFromClipboard()?.let {
            val jpegFiletoSave = File(StateVM.dirTemp, "buffer.jpg")
            val convertedImage =
                BufferedImage(
                    it.toBufferedImage().width,
                    it.toBufferedImage().height,
                    BufferedImage.TYPE_INT_RGB
                )
            convertedImage.createGraphics().drawImage(it.toBufferedImage(), 0, 0, null)
            saveImage(convertedImage, jpegFiletoSave, 0.95f)
//                        saveImage(it.toBufferedImage().toComposeImageBitmap().asSkiaBitmap().toBufferedImage(), jpegFiletoSave,0.95f)
            fileForCrop.setFile(jpegFiletoSave)
            true
        } ?: false

        val focusRequester = remember { FocusRequester() }
        ColumnVA {
            RowVA(Modifier
                .onKeyEvent {
                    when (it.key.keyCode) {
                        Key.V.keyCode -> {
                            if (it.isCtrlPressed) {
                                loadImageFromBuffer()
                            } else false
                        }
                        else -> false
                    }
                }
                .focusRequester(focusRequester)
                .focusable()
            ) {

                MyTextButtStyle1("Открыть изображение", Modifier.padding(bottom = 10.dp)) {
                    val chooser = JFileChooser()

                    chooser.currentDirectory = File(StateVM.lastOpenDir)
                    chooser.removeChoosableFileFilter(chooser.choosableFileFilters[0])
                    chooser.addChoosableFileFilter(
                        if (onlyJpg) OpenFileFilter(
                            listOf("jpeg", "jpg"),
                            "Photo in JPEG format"
                        ) else OpenFileFilter(
                            listOf("jpeg", "jpg", "png"),
                            "Photo in JPEG and png format"
                        )
                    )
//                            chooser.addChoosableFileFilter(OpenFileFilter("jpg", "Photo in JPEG format"))
//                            chooser.addChoosableFileFilter(OpenFileFilter("png", "PNG image"))
//                            chooser.addChoosableFileFilter(OpenFileFilter("svg", "Scalable Vector Graphic"))
//                            val composeWindow = ComposeWindow()
                    val returnVal: Int =
                        chooser.showDialog(null, "Открыть")// .showSaveDialog(ComposeWindow())
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        StateVM.lastOpenDir = chooser.selectedFile.parent
                        fileForCrop.setFile(chooser.selectedFile)

//                                        if (!iconFile.value.parentFile.exists()) Files.createDirectory(Paths.get(iconFile.value.parentFile.path)) else iconFile.value.delete()
//                                    iconFile.value.createNewFile()
//                                    iconFile.value.writeBytes(chooser.selectedFile.readBytes())
                    }
                }
                MyTextButtStyle1("из буфера", Modifier.padding(start = 10.dp, bottom = 10.dp)) {
                    if (loadImageFromBuffer().not()) MyShowMessage(dialogLayout, "В буфере нет изображения.")
                }
            }
            if (ratioWdivH == 0.0) ratioSeekBar.show(Modifier.padding(bottom = 10.dp))
            Box(
                Modifier.wrapContentSize()
                    .dashedBorder(
                        2.dp, MyColorARGB.MYBEG.toColor().copy(0.85f), on = 25.dp, off = 15.dp,
                        shape = RoundedCornerShape(15.dp)
                    )
            ) {
                Image(
                    bitmap = fileForCrop.sourceImage.value,
                    "defaultAvatar",
                    Modifier
                        .padding(imgPadd)
                        .onGloballyPositioned {
                            if (imgHeight.value != it.size.height || imgWidth.value != it.size.width) {
                                imgHeight.value = it.size.height
                                imgWidth.value = it.size.width
                                setValueX(offsetX, 0f)
                                setValueY(offsetY, 0f)
                                setValueX(offsetX2, imgPadd.toPx()/2f + imgWidth.value.toFloat())
                                setValueY(offsetY2, imgPadd.toPx()/2f + imgHeight.value.toFloat())
                            }
                        }
                        .wrapContentSize(),
                    contentScale = ContentScale.Fit, //Crop,// Fit,
                )
                if (fileForCrop.openFile.value) {
                    Box(Modifier.offset {
                        IntOffset(start.value.roundToInt(), top.value.roundToInt())
                    }
                        .width(width().toDp())
                        .height(height().toDp())
                        .dashedBorder(2.dp, Color.Green.copy(0.89f), on = 15.dp, off = 15.dp)
                        .clickable {
                            offsetX.value = start.value - sufSize.toPx() / 2
                            offsetY.value = top.value - sufSize.toPx() / 2
                            offsetX2.value = width() + start.value - sufSize.toPx() / 2
                            offsetY2.value = height() + top.value - sufSize.toPx() / 2
                        }
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragEnd = {
                                    updateImage.value = true
                                }
                            ) { change, dragAmount ->
                                updateImage.value = false
                                addValueX(offsetX, dragAmount.x, width(), true)
                                addValueY(offsetY, dragAmount.y, height(), true)
                                addValueX(offsetX2, dragAmount.x, width(), false)
                                addValueY(offsetY2, dragAmount.y, height(), false)
                            }
                        }
                    ) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .border(2.dp, Color.Red)
                        )
                    }
                    Surface(
                        color = Color(0xFF34AB52),
                        modifier = Modifier
                            .width(sufSize)
                            .height(sufSize)
                            .offset {
                                IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt())
                            }
                            .clip(RoundedCornerShape(7.dp))
                            .border(Dp.Companion.Hairline, MyColorARGB.MYBEG.toColor(), RoundedCornerShape(7.dp))
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragEnd = {
                                        updateImage.value = true
                                    }
                                ) { change, dragAmount ->
                                    updateImage.value = false
                                    addValueX(offsetX, dragAmount.x)
                                    addValueY(offsetY, dragAmount.y)
                                }
                            }
                    ) {
                    }
                    Surface(
                        color = Color(0xFF34AB52),
                        modifier = Modifier
                            .width(sufSize)
                            .height(sufSize)
                            .offset {
                                IntOffset(offsetX2.value.roundToInt(), offsetY2.value.roundToInt())
                            }
                            .clip(RoundedCornerShape(7.dp))
                            .border(Dp.Companion.Hairline, MyColorARGB.MYBEG.toColor(), RoundedCornerShape(7.dp))
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragEnd = {
                                        updateImage.value = true
                                    }
                                ) { change, dragAmount ->
                                    updateImage.value = false
                                    addValueX(offsetX2, dragAmount.x)
                                    addValueY(offsetY2, dragAmount.y)
                                }
                            }
                    ) {
                    }
                }
            }
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        dialogLayout.getLay()
    }
}