package MainTabs.Quest.Element

import MainTabs.OpenFileFilter
import MainTabs.imageFromFile
import MyDialog.MyDialogLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import common.BackgroungPanelStyle1
import common.CropBoxForImageFile
import common.MyOutlinedTextField
import common.MyTextButtStyle1
import extensions.RowVA
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemGovorun
import viewmodel.QuestVM
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.JFileChooser

fun PanAddGovorun(
    dialPan: MyDialogLayout,
    item: ItemGovorun? = null
) {
    QuestVM.openQuestDB?.let { questDB ->

        val dialLayInner = MyDialogLayout()
        val fileForCrop = CropBoxForImageFile(null, defaultResource = "iv_avatar.png")
        val avatarFile = mutableStateOf(File(questDB.dirQuest, "${item?.image_file}.jpg"))
        val avatarF =
            mutableStateOf(item?.let { if (avatarFile.value.exists()) imageFromFile(avatarFile.value) else null }
                ?: fileForCrop.sourceImage.value)
        val changeAvatar = mutableStateOf(false)
        val checkAvaAvtor = mutableStateOf(avatarFile.value.exists())
        val shape = RoundedCornerShape(75.dp)

        dialPan.dial = @Composable {
            val text_name = remember { mutableStateOf(TextFieldValue(item?.name ?: "")) }
            val text_opis = remember { mutableStateOf(TextFieldValue(item?.opis ?: "")) }
            BackgroungPanelStyle1 {
                Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    RowVA {
                        Checkbox(checkAvaAvtor.value, onCheckedChange = {
                            checkAvaAvtor.value = it
                        })
                        Image(
                            bitmap = avatarF.value,
                            "defaultAvatar",
                            Modifier.wrapContentSize()
                                .padding(20.dp)
                                .height(150.dp)
                                .width(150.dp)
                                .clickable {
                                    PanAddAvaGovorun(dialLayInner, avatarF, fileForCrop, changeAvatar, checkAvaAvtor)
                                }.clip(shape)
                                .border(1.dp, Color.White, shape)
                                .padding(1.dp)
                                .border(3.dp, Color(0x7FFFF7D9), RoundedCornerShape(74.dp))
                                .shadow(2.dp, shape),
                            contentScale = ContentScale.Crop,
                        )
                    }
                    MyOutlinedTextField("Имя действующего лица", text_name, Modifier.padding(bottom = 10.dp))
                    MyOutlinedTextField("Немного о действующем лице", text_opis, Modifier.padding(bottom = 10.dp))
                    Row {
                        MyTextButtStyle1("Отмена") {
                            dialPan.close()
                        }
                        MyTextButtStyle1(item?.let { "Изменить" } ?: "Добавить", Modifier.padding(start = 5.dp)) {
                            if (text_name.value.text != "")
                                item?.let {
                                    val ava = File(questDB.dirQuest, "${questDB.nameQuest}_${item.id}.jpg")
                                    if (checkAvaAvtor.value) {
                                        if (changeAvatar.value) {
                                            fileForCrop.outImage.saveIconFile(ava.path)
                                        }
                                    } else {
                                        val tmp = File(questDB.dirQuest, "${item.image_file}.jpg")
                                        if (tmp.exists()) tmp.delete()
                                    }
                                    questDB.addQuest.updGovorun(
                                        id = it.id.toLong(),
                                        name = text_name.value.text,
                                        opis = text_opis.value.text,
                                        image_file = if (checkAvaAvtor.value) ava.nameWithoutExtension else ""
                                    )
                                    dialPan.close()
                                } ?: run {
                                    val idGov = questDB.addQuest.addGovorun(
                                        name = text_name.value.text,
                                        opis = text_opis.value.text,
                                        image_file = ""
                                    )
                                    if (idGov > 0 && checkAvaAvtor.value && changeAvatar.value) {
                                        val ava = File(questDB.dirQuest, "${questDB.nameQuest}_${idGov}.jpg")
                                        fileForCrop.outImage.saveIconFile(ava.path)
                                        questDB.addQuest.updGovorun(
                                            id = idGov,
                                            name = text_name.value.text,
                                            opis = text_opis.value.text,
                                            image_file = ava.nameWithoutExtension
                                        )
                                    }
                                    dialPan.close()
                                }
                        }
                    }
                }
            }
            dialLayInner.getLay()
        }
        dialPan.show()
    }
}

@Composable
private fun avatar(
    avatarF: MutableState<ImageBitmap>,
    avatarFile: MutableState<File>,
    changeAvatar: MutableState<Boolean>
) {

    val shape = RoundedCornerShape(75.dp)
    Box(
        Modifier
            .height(170.dp)
            .width(170.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            bitmap = avatarF.value,
            "defaultAvatar",
            Modifier.wrapContentSize()
                .height(150.dp)
                .width(150.dp)
                .clickable {
                    val myFilename: File
                    var chooser = JFileChooser()
                    chooser.removeChoosableFileFilter(chooser.choosableFileFilters[0])
                    chooser.addChoosableFileFilter(
                        OpenFileFilter(
                            listOf("jpeg", "jpg"),
                            "Photo in JPEG format"
                        )
                    )
                    val returnVal: Int =
                        chooser.showDialog(null, "Открыть")
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        if (!avatarFile.value.parentFile.exists()) Files.createDirectory(Paths.get(avatarFile.value.parentFile.path)) else avatarFile.value.delete()
                        avatarFile.value = chooser.selectedFile

                        avatarF.value = imageFromFile(chooser.selectedFile)
                    }
                }.clip(shape)
                .border(1.dp, Color.White, shape)
                .padding(1.dp)
                .border(3.dp, Color(0x7FFFF7D9), RoundedCornerShape(74.dp))
                .shadow(2.dp, shape),
            contentScale = ContentScale.Crop,
        )
    }
}

