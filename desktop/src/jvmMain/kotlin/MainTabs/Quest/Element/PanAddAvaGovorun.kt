package MainTabs.Quest.Element

import MainTabs.Avatar.Element.ImageCropSelector
import MyDialog.MyDialogLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import common.BackgroungPanelStyle1
import common.CropBoxForImageFile
import common.MyTextButtStyle1
import extensions.RowVA

fun PanAddAvaGovorun(
    dialLay: MyDialogLayout,
    avatar: MutableState<ImageBitmap>,
    fileForCrop: CropBoxForImageFile,
    changeAvatar: MutableState<Boolean>,
    checkAvaAvatar: MutableState<Boolean>,
) {

    dialLay.dial = @Composable {

        val size = 250.dp
        val shape = RoundedCornerShape(125.dp)
        val shape2 = RoundedCornerShape(123.dp)

        BackgroungPanelStyle1() {
            Column(
                Modifier.padding(15.dp).widthIn(0.dp, this@BackgroungPanelStyle1.maxWidth * 0.8f)
                    .heightIn(0.dp, this@BackgroungPanelStyle1.maxHeight * 0.8f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(Modifier.weight(1f, false)) {
                    RowVA {
                        Box(
                            Modifier.padding(20.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Image(
                                bitmap = fileForCrop.outImage.cropImageBitmap.value ?: useResource(
                                    fileForCrop.defaultResource,
                                    ::loadImageBitmap
                                ),
                                "defaultAvatar",
                                Modifier.wrapContentSize()
                                    .height(size)
                                    .width(size)
                                    .clip(shape)
                                    .border(2.dp, Color.White, shape)
                                    .padding(2.dp)
                                    .border(3.dp, Color(0x7FFFF7D9), shape2)
                                    .shadow(2.dp, shape),
                                contentScale = ContentScale.Crop,
                            )
                        }
                        Box(
                            Modifier
                                .width(this@BackgroungPanelStyle1.maxWidth * 0.5f)
                                .height(this@BackgroungPanelStyle1.maxHeight * 0.6f)
                                .padding(10.dp),
                            Alignment.Center
                        ) {
                            ImageCropSelector(fileForCrop, 600, true)
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
                        fileForCrop.outImage.cropImageBitmap.value?.let {
                            avatar.value = it
                            changeAvatar.value = true
                            checkAvaAvatar.value = true
                        }
                        dialLay.close()
                    }
                }
            }
        }
    }

    dialLay.show()
}

