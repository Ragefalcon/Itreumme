package MainTabs.Avatar.Element

import MainTabs.Avatar.Items.ComItemDenPlanOfChronicle
import MainTabs.imageFromFile
import MyDialog.MyDialogLayout
import MyDialog.buttDatePickerWithButton
import MyList
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
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
import androidx.compose.ui.unit.sp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemBestDays
import ru.ragefalcon.sharedcode.models.data.ItemComplexOpisImageGroup
import viewmodel.MainDB
import viewmodel.StateVM
import java.io.File
import java.util.*

fun PanViewBestDay(
    dialPan: MyDialogLayout,
    item: ItemBestDays
) {

    val dialLayInner = MyDialogLayout()
    val dateInner = mutableStateOf(item.data.let { Date(it) })

    var imageIB: MutableState<ImageBitmap?> = mutableStateOf<ImageBitmap?>(null).apply {
        if (item.enableIcon) {
            val ff = File(StateVM.dirBestDaysImages, "bestDay_${Date(item.data).format("yyyy_MM_dd")}.jpg")
            if (ff.exists()) {
                value = imageFromFile(ff)
            }
        }
    }

    dialPan.dial = @Composable {
        val text_addDay = remember { mutableStateOf(TextFieldValue("")) }

        BackgroungPanelStyle1 {
            Column(
                Modifier.padding(15.dp)
                    .fillMaxHeight(0.85f)
                    .fillMaxWidth(0.7f), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                imageIB.value?.let {
                    val shape = RoundedCornerShape(15.dp)
                    Image(
                        bitmap = it,
                        "defaultAvatar",
                        Modifier.padding(bottom = 5.dp)
                            .widthIn(0.dp, this@BackgroungPanelStyle1.maxWidth * 0.8f)
                            .heightIn(0.dp, this@BackgroungPanelStyle1.maxHeight * 0.2f)
                            .clip(shape)
                            .border(2.dp, Color.White, shape)
                            .shadow(2.dp, shape)
                            .clickable() {
                                PanViewImageList(
                                    mutableListOf(
                                        File(
                                            StateVM.dirBestDaysImages,
                                            "bestDay_${Date(item.data).format("yyyy_MM_dd")}.jpg"
                                        )
                                    ).apply {
                                        MainDB.complexOpisSpis.spisComplexOpisForDenPlanInBestDays.getState().value?.let { mapOpis ->
                                            mapOpis.forEach {
                                                it.value.forEach {
                                                    if (it is ItemComplexOpisImageGroup) {
                                                        it.spisImages.forEach {
                                                            add(
                                                                File(
                                                                    StateVM.dirComplexOpisImages,
                                                                    "complexOpisImage_${it.id}.jpg"
                                                                )
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    File(
                                        StateVM.dirBestDaysImages,
                                        "bestDay_${Date(item.data).format("yyyy_MM_dd")}.jpg"
                                    )
                                )
                            },
                        contentScale = ContentScale.Fit,
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier.padding(vertical = 10.dp),
                        text = item.name,
                        style = MyTextStyleParam.style1.copy(
                            color = MyColorARGB.colorRasxodTheme.toColor()
                        )
                    )
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = text_addDay.value.text,
                        style = MyTextStyleParam.style2.copy(
                            color = Color.Black.toMyColorARGB().plusWhite().toColor().copy(alpha = 0.8f),
                            fontSize = 20.sp
                        )
                    )
                }
                buttDatePickerWithButton(dialLayInner, dateInner) {
                    val deltaDay = Date(item.data).daysBetween(Date(it.time)).toInt()
                    val str = if (deltaDay == 0) "" else "${if (deltaDay > 0) "+" else ""}$deltaDay"
                    text_addDay.value = TextFieldValue(str)
                    MainDB.avatarFun.setPlanBestDay(it.time)
                }
                MainDB.avatarSpis.spisDenPlanInBestDays.getState().value?.let {
                    MyList(
                        it,
                        Modifier.weight(1f).padding(vertical = 10.dp)
                    ) { ind, item ->
                        ComItemDenPlanOfChronicle(item, ind == 0, dialLayInner)
                    }
                }
                Row {
                    MyTextButtStyle1("Скрыть") {
                        dialPan.close()
                    }
                }
            }
        }
        dialLayInner.getLay()
    }
    dialPan.show()
}

