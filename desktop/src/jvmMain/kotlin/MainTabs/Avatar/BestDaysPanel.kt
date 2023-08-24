package MainTabs.Avatar

import MainTabs.Avatar.Element.PanAddImageCommon
import MainTabs.Avatar.Element.PanViewBestDay
import MyList
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import MainTabs.Avatar.Items.ComItemBestDay
import MyDialog.MyDialogLayout
import MyDialog.myDatePicker
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.longMinusTimeLocal
import ru.ragefalcon.sharedcode.models.data.ItemBestDays
import ru.ragefalcon.sharedcode.source.disk.getValue
import viewmodel.MainDB
import viewmodel.StateVM
import java.io.File
import java.util.*

class BestDaysPanel {

    private val selection = SingleSelectionType<ItemBestDays>()
    private val changeDate = mutableStateOf(Date())

    @Composable
    fun show(
        dialLay: MyDialogLayout,
        modifier: Modifier = Modifier
    ) {
        with(MainDB.styleParam.avatarParam.chronicleTab) {
            MyShadowBox(plateChronicle.shadow.getValue()) {
                Column(
                    modifier
                        .withSimplePlate(SimplePlateWithShadowStyleState(plateChronicle))
                        .paddingStyle(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Хроника времен",
                        Modifier.padding(bottom = 7.dp).padding(vertical = 5.dp),
                        style = textChronicle.getValue()
                    )
                    itemBestDay.getComposable(::ItemBestDayStyleState) { itemBestDayStyleState ->
                        MyList(MainDB.avatarSpis.spisBestDays, Modifier.weight(1f)) { ind, itemBestDays ->
                            var imageIB: MutableState<ImageBitmap?> = mutableStateOf(null)
                            ComItemBestDay(itemBestDays, selection, openDay = {
                                PanViewBestDay(dialLay, it)
                                MainDB.avatarFun.setPlanBestDay(it.data)
                            }, itemBestDayStyleState = itemBestDayStyleState) { item, expanded ->
                                MyDropdownMenuItem(expanded, "+ Изображение") {
                                    PanAddImageCommon(dialLay, ratio_W_H = 0.0 to 0.0, maxSizePx = 1500) { outImage ->
                                        outImage.saveIconFile(
                                            File(
                                                StateVM.dirBestDaysImages,
                                                "bestDay_${Date(item.data).format("yyyy_MM_dd")}.jpg"
                                            ).path
                                        )
                                        MainDB.addAvatar.enableIconBestDay(item.id.toLong(), true)
                                    }
                                }
                                MyDropdownMenuItem(expanded, "- Изображение") {
                                    MainDB.addAvatar.enableIconBestDay(item.id.toLong(), false)
                                    File(
                                        StateVM.dirBestDaysImages,
                                        "bestDay_${Date(item.data).format("yyyy_MM_dd")}.jpg"
                                    ).let {
                                        if (it.exists()) it.delete()
                                    }
                                }
                                MyDeleteDropdownMenuButton(expanded) {
                                    MainDB.addAvatar.delBestDay(item.id.toLong())
                                    File(
                                        StateVM.dirBestDaysImages,
                                        "bestDay_${Date(item.data).format("yyyy_MM_dd")}.jpg"
                                    ).let {
                                        if (it.exists()) it.delete()
                                    }
                                }
                            }.getComposable()
                        }
                    }
                    MyTextButtStyle1(
                        "\uD83D\uDD0E",
                        modifier = Modifier.padding(start = 15.dp),
                        myStyleTextButton = TextButtonStyleState(buttSelectDate)
                    ) {
                        myDatePicker(dialLay, changeDate) {
                            MainDB.avatarFun.setPlanBestDay(it.time)
                            PanViewBestDay(
                                dialLay,
                                ItemBestDays("-1", it.format("dd MMMM yyyy (EEE)"), it.time.longMinusTimeLocal(), false)
                            )
                        }
                    }
                }
            }
        }
    }
}