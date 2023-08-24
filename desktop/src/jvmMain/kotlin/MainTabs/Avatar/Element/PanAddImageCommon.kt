package MainTabs.Avatar.Element


import MainTabs.MainAvatarTabs
import MyDialog.MyDialogLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import common.*
import extensions.RowVA
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.tabElement


fun PanAddImageCommon(
    dialLay: MyDialogLayout,
    defaultResource: String = "default_image_blank.jpg",
    maxSizePx: Int = 900,
    ratio_W_H: Pair<Double,Double> = 1.0 to 1.0,
    rezFun: (IconImageBuffer)->Unit

//    item: ItemBestDays,
//    image: MutableState<ImageBitmap?>
) {
    val fileForCrop = CropBoxForImageFile(null, defaultResource = defaultResource)

    dialLay.dial = @Composable {


        val size = 250.dp
        val shape = RoundedCornerShape(5.dp) //CircleShape
        val shape2 = RoundedCornerShape(3.dp) //CircleShape

        BackgroungPanelStyle1() {
            Column(
                Modifier.padding(15.dp).widthIn(0.dp, this@BackgroungPanelStyle1.maxWidth * 0.8f)
                    .heightIn(0.dp, this@BackgroungPanelStyle1.maxHeight * 0.8f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(Modifier.weight(1f, false)) {
                    RowVA {
                        Box(
                            Modifier.padding(20.dp)
                                .width(this@BackgroungPanelStyle1.maxWidth * 0.3f)
                                .height(this@BackgroungPanelStyle1.maxHeight * 0.6f)
                            ,
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                bitmap = fileForCrop.outImage.cropImageBitmap.value ?: useResource(
                                    fileForCrop.defaultResource,
                                    ::loadImageBitmap
                                ), //BitmapPainter(  ?: painterResource("ic_baseline_wallpaper_24.xml")
                                "defaultAvatar",
                                Modifier.wrapContentSize()
                                    .heightIn(0.dp,size)
                                    .widthIn(0.dp,size)
                                    .clip(shape)
                                    .border(2.dp, Color.White, shape)
                                    .padding(2.dp)
                                    .border(3.dp, Color(0x7FFFF7D9), shape2)
                                    .shadow(2.dp, shape),
                                contentScale = ContentScale.Fit,
                            )
                        }
                        Box(
                            Modifier
                                .width(this@BackgroungPanelStyle1.maxWidth * 0.5f)
                                .height(this@BackgroungPanelStyle1.maxHeight * 0.6f)
                                .padding(10.dp),
                            Alignment.Center
                        ) {
                            ImageCropSelector(fileForCrop, maxSizePx, true, if (ratio_W_H.second != 0.0) ratio_W_H.first/ratio_W_H.second else 0.0)
                        }
                    }
                }
                RowVA {
                    MyTextButtStyle1("Отмена") {
                        dialLay.close()
                    }
                    if (fileForCrop.outImage.cropImageBitmap.value != null) MyTextButtStyle1(
                        "Выбрать",
                        Modifier.padding(start = 5.dp)
                    ) {
//                        fileForCrop.outImage.cropImageBitmap.value?.let {
//                            image.value = it
//                        }
                        rezFun(fileForCrop.outImage)
                        dialLay.close()
                    }
                }
            }
        }
    }

    dialLay.show()
}

