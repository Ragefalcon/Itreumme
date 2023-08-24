package MainTabs.Avatar.Element


import MainTabs.Avatar.Items.ComItemAva
import MainTabs.imageFromFile
import MyDialog.MyDialogLayout
import MyListRow
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import common.*
import extensions.RowVA
import ru.ragefalcon.sharedcode.source.disk.CommonName
import viewmodel.StateVM
import java.io.File
import java.util.*

fun PanSelectAvatar(
    dialLay: MyDialogLayout,
    avatar: MutableState<ImageBitmap>
) {

    val filesList = mutableStateListOf<File>().apply {
        val flist = File(StateVM.dirAvatar).listFiles()
        if (flist.isNotEmpty()) for (ff in flist) {
            if (ff.isFile) if (ff.extension.lowercase() == "jpg" && ff.name != CommonName.nameAvatarFile) {
                this.add(ff)
            }
        }
    }


    dialLay.dial = @Composable {

        val innerIconBuffer = IconImageBuffer()
        val fileForCrop = remember { CropBoxForImageFile(null, innerIconBuffer, "icon_skill_color_lamp.png") }
        val seekBar = remember { DiskretSeekBar(listOf("Новая" to "new", "Выбрать из списка" to "spis"), "new") }

        val selectionImageFromSpis = remember { SingleSelectionType<File>() }

        val size = 350.dp
        val shape = RoundedCornerShape(15.dp) //CircleShape

        BackgroungPanelStyle1() {
            Column(
                Modifier.padding(15.dp).widthIn(0.dp, this@BackgroungPanelStyle1.maxWidth * 0.8f)
                    .heightIn(0.dp, this@BackgroungPanelStyle1.maxHeight * 0.8f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                seekBar.show()
                Box(Modifier.weight(1f, false)) {
                    when (seekBar.active?.cod) {
                        "new" -> RowVA {
                            Box(
                                Modifier.padding(20.dp)
//                        .height(size)
//                        .width(size)
                                ,
                                contentAlignment = Alignment.TopCenter
                            ) {
                                Image(
                                    bitmap = fileForCrop.outImage.cropImageBitmap.value ?: useResource(
                                        "iv_barash.jpg",
                                        ::loadImageBitmap
                                    ), //BitmapPainter(
                                    "defaultAvatar",
                                    Modifier.wrapContentSize()
                                        .height(size)
                                        .width(size)
                                        .clip(shape)
                                        .border(1.dp, Color.White, shape)
                                        .padding(1.dp)
                                        .border(3.dp, Color(0x7FFFF7D9), RoundedCornerShape(14.dp))
                                        .shadow(2.dp, shape),
                                    contentScale = ContentScale.Crop,// Fit,
                                )
                            }
                            Box(
                                Modifier
                                    .width(this@BackgroungPanelStyle1.maxWidth * 0.5f)
                                    .height(this@BackgroungPanelStyle1.maxHeight * 0.6f)
                                    .padding(10.dp),
                                Alignment.Center
                            ) {
                                ImageCropSelector(fileForCrop, 1000, true)
                            }
                        }
                        "spis" -> RowVA {
                            MyListRow(
                                filesList,
                                Modifier.width(this@BackgroungPanelStyle1.maxWidth * 0.8f).padding(vertical = 20.dp)
                            ) { ind, avaFile ->
                                if (avaFile.exists()) imageFromFile(avaFile).let { bitmapAva ->
                                    ComItemAva(avaFile, selectionImageFromSpis, doubleClick = { itemIB ->
                                        itemIB.copyTo(
                                            File(
                                                StateVM.dirAvatar,
                                                "avatarMain.jpg"
                                            ), overwrite = true
                                        )
                                        avatar.value = bitmapAva
                                        dialLay.close()
                                    }) { item, exp ->
                                        MyDeleteDropdownMenuButton(exp) {
                                            filesList.remove(avaFile)
                                            if (avaFile.exists()) avaFile.delete()
                                        }
                                    }.getComposable()
                                }
                            }
                        }
                        else -> {}
                    }
                }
                RowVA {
                    MyTextButtStyle1("Отмена") {
                        dialLay.close()
                    }
                    when (seekBar.active?.cod) {
                        "new" -> if (fileForCrop.outImage.cropImageBitmap.value != null) MyTextButtStyle1(
                            "Сохранить и выбрать",
                            Modifier.padding(start = 5.dp)
                        ) {
                            fileForCrop.outImage.saveIconFile(
                                File(
                                    StateVM.dirAvatar,
                                    "avatar_${Date().time}.${fileForCrop.extension()}"
                                ).path
                            )
                            fileForCrop.outImage.saveIconFile(
                                File(
                                    StateVM.dirAvatar,
                                    "avatarMain.${fileForCrop.extension()}"
                                ).path
                            )
                            fileForCrop.outImage.cropImageBitmap.value?.let {
                                avatar.value = it
                            }
                            dialLay.close()
                        }
                        "spis" -> {
                            selectionImageFromSpis.selected?.let { avaFile ->
                                MyDeleteButton(Modifier.padding(start = 5.dp)) {
                                    if (File(
                                            StateVM.dirAvatar,
                                            CommonName.nameAvatarFile
                                        ).exists()
                                    ) File(
                                        StateVM.dirAvatar,
                                        CommonName.nameAvatarFile
                                    ).delete()
                                    avatar.value = useResource(CommonName.nameDefaultAvatarResource, ::loadImageBitmap)
                                    dialLay.close()
                                }
                                MyTextButtStyle1(
                                    "Выбрать",
                                    Modifier.padding(start = 5.dp)
                                ) {
                                    if (avaFile.exists()) imageFromFile(avaFile).let { bitmapAva ->
                                        avaFile.copyTo(
                                            File(
                                                StateVM.dirAvatar,
                                                CommonName.nameAvatarFile
                                            ), overwrite = true
                                        )
                                        avatar.value = bitmapAva
                                        dialLay.close()
                                    }
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    dialLay.show()
}

