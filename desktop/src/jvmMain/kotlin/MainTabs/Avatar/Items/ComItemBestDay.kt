package MainTabs.Avatar.Items

import MainTabs.imageFromFile
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.*
import org.jetbrains.skia.*
import org.jetbrains.skiko.toBufferedImage
import org.jetbrains.skiko.toImage
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemBestDays
import viewmodel.StateVM
import java.io.File
import java.util.*
class ComItemBestDay(
    val item: ItemBestDays,
    val selection: SingleSelectionType<ItemBestDays>,
    val openDay: (ItemBestDays) -> Unit = {},
    val itemBestDayStyleState: ItemBestDayStyleState,
    val dropMenu: @Composable ColumnScope.(ItemBestDays, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    var expanded = mutableStateOf(false)

//    val maxHeightOpis = mutableStateOf((-1).dp)

    var imageIB: MutableState<ImageBitmap?> = mutableStateOf<ImageBitmap?>(null).apply {
        if (item.enableIcon) {
            val ff = File(StateVM.dirBestDaysImages, "bestDay_${Date(item.data).format("yyyy_MM_dd")}.jpg")
            if (ff.exists()) {
                value = imageFromFile(ff)
            }
        }
    }

/*
    val matrix = ColorMatrix().apply {
//        this.setToScale(0.8f,0.8f,0.8f,1f)
        setToSaturation(0F)
        this.setToScale(1.8f,0.8f,0.8f,1f)
    }
*/

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun getComposable() {
        with(itemBestDayStyleState) {
            MyCardStyle1(
                selection.isActive(item), 0, {
                    selection.selected = item
                }, onDoubleClick = { openDay(item) },
                styleSettings = itemBestDayStyleState,
                dropMenu = { exp -> dropMenu(item, exp) }
            ) {
//        MyCardStyle1(selection.isActive(item), onClick = {
//            selection.selected = item
//        }, onDoubleClick = {openDay(item)},
//            dropMenu = { exp -> dropMenu(item, exp) }
//        )
//        {
//            Row(Modifier.padding(5.dp), verticalAlignment = Alignment.CenterVertically) {

                Column(modifier = Modifier.padding(8.dp)) {
                    imageIB.value?.let { imgBtm ->
//                        val size = 350.dp
                        MyShadowBox(image_shadow){
                            Image(
                                bitmap = imgBtm, //BitmapPainter(
                                "defaultAvatar",
                                Modifier
                                    .padding(bottom = 5.dp)
                                    .wrapContentSize()
//                                .heightIn(0.dp, size)
//                                .widthIn(0.dp, size)
                                    .clip(image_shape)
                                    .border(image_border_width, image_border_brush, image_shape)
//                                    .shadow(image_elevation, image_shape.shadow)
                                    .alpha(0.8f)
                                ,
                                contentScale = ContentScale.Fit,
                                colorFilter = if(BLACKandWHITE) ColorFilter.tint(TINT, BlendMode.Color) else null// .colorMatrix(matrix)
                            )
                        }
/*
                        Box(
                            Modifier
                                .height(size/4)
                                .width(size)
                                .graphicsLayer(
                                    renderEffect = ImageFilter.makeImage(image = imgBtm.asSkiaBitmap().toBufferedImage().toImage(),
                                        src = org.jetbrains.skia.Rect(0f,0f, imgBtm.width.toFloat(),imgBtm.height.toFloat()),
                                        dst = org.jetbrains.skia.Rect(0f,0f, size.value,(size/4).value),
                                        mode = SamplingMode.DEFAULT //FilterMipmap(FilterMode.NEAREST, MipmapMode.LINEAR)
                                    )
                                        .asComposeRenderEffect() // .makeBlur(elevation.toPx(), elevation.toPx(), FilterTileMode.REPEAT).asComposeRenderEffect()
                                )

                        ) {
                        }
*/
/*
                        Canvas(Modifier
                            .padding(bottom = 5.dp)
                            .wrapContentSize()
                            .height(size/4)
                            .width(size)
                            .clip(shape)
                            .border(2.dp, Color.White, shape)
//                                .padding(2.dp)
//                                .border(3.dp, Color(0x7FFFF7D9), shape2)
                            .shadow(2.dp, shape),
                        ){
                            var aa = org.jetbrains.skia.Paint().apply {
                                this.isAntiAlias = true
//                                imageFilter = ImageFilter.makeBlur(0.5f,0.5f,FilterTileMode.DECAL)
                            }
                            drawRect(Color.Red, Offset(0f,0f), Size(this.size.width,this.size.height))
                            drawIntoCanvas {
                                it.nativeCanvas.drawImageRect(
                                    image = imgBtm.asSkiaBitmap().toBufferedImage().toImage(),
                                    src = org.jetbrains.skia.Rect(0f,0f, imgBtm.width.toFloat(),imgBtm.height.toFloat()),
                                    dst = org.jetbrains.skia.Rect(0f,0f, this.size.width,this.size.height),
                                    samplingMode = FilterMipmap(FilterMode.NEAREST, MipmapMode.LINEAR) , // CubicResampler(0.05f, 0.05f) , // SamplingMode.LINEAR,
                                    paint = aa,
                                    false
                                )
                            }
*/
/*
                            drawImage(
                                imgBtm,
                                srcSize = IntSize((imgBtm.width*1.3).toInt(),imgBtm.height),
                                dstSize = IntSize(size.value.toInt(),size.value.toInt()/4),
//                                colorFilter = ColorFilter.colorMatrix(),
                                filterQuality = FilterQuality.High
                            )
*//*

                        }
*/
                    }
                    RowVA {

                        Column(Modifier.padding(0.dp).weight(1f)) {
                            Text(
                                modifier = Modifier.padding(0.dp),
                                text = Date(item.data).format("dd.MM.yyyy (EEE)"),
                                style = dateText
                            )
                            Text(
                                modifier = Modifier.padding(0.dp),
                                text = item.name,
                                style = mainTextStyle
                            )
                        }
                        if (selection.isActive(item)) MyButtDropdownMenuStyle2(
                            Modifier.padding(end = 0.dp).padding(vertical = 0.dp), expanded, buttMenu
                        ) {
                            dropMenu(item, expanded)
                        }
                        if (selection.isActive(item)) MyTextButtSimpleStyle(
                            text = "\uD83D\uDD6E",
                            modifier = Modifier
                                .padding(horizontal = 10.dp),
                            fontSize = 20.sp,
                            color = open_butt_color
                        ) {
                            openDay(item)
                        }
                    }
                }
            }
        }
    }
}



