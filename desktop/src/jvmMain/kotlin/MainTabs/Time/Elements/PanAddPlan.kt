package MainTabs.Time.Elements

import MyDialog.MyDialogLayout
import MyDialog.buttDatePickerWithButton
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.TimeUnits
import ru.ragefalcon.sharedcode.extensions.withOffset
import ru.ragefalcon.sharedcode.models.data.ItemComplexOpis
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TableNameForComplexOpis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlan
import viewmodel.MainDB
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
fun PanAddPlan(
    dialPan: MyDialogLayout,
    item: ItemPlan? = null,
    cancelListener: () -> Unit = {},
    listOp: List<ItemComplexOpis>? = null,
    finishListener: () -> Unit = {}
) {
    with(MainDB.styleParam.timeParam.planTab.panAddPlan) {
        val change = item?.let { item.id.toLong() > 0 } ?: false
        val dialLayInner = MyDialogLayout()
        val dateStart =
            mutableStateOf(item?.let { if (it.data1 > 1L && it.data2 > 1L) Date(it.data1) else null } ?: Date())
        val dateEnd =
            mutableStateOf(item?.let { if (it.data1 > 1L && it.data2 > 1L) Date(it.data2) else null } ?: Date().add(
                14,
                TimeUnits.DAY
            ))
        val expandedDate = mutableStateOf((item?.data1?.withOffset() ?: 0L) != 0L)
        val vajn = MySelectStat(
            item?.vajn ?: 1L, statNabor = MySelectStat.statNaborPlan, iconRes = "bookmark_01.svg", dropdownMenuStyle
        )

        val enabledDirection = mutableStateOf(item?.direction == true)
        val enabledGotov =
            mutableStateOf(item?.gotov?.let { it >= 0.0 } ?: MainDB.interfaceSpis.DefaultPercentForPlan.getValue())

        val text_name = mutableStateOf(TextFieldValue(item?.let { it.name } ?: ""))
        val complexOpis =
            MyComplexOpisWithNameBox(
                "Название проекта",
                text_name,
                "Описание проекта",
                item?.id?.toLong() ?: -1,
                TableNameForComplexOpis.spisPlan,
                MainDB.styleParam.timeParam.planTab.complexOpisForPlan,
                listOp?.toMutableStateList() ?: item?.let {
                    MainDB.complexOpisSpis.spisComplexOpisForPlan.getState().value?.get(it.id.toLong())
                        ?.toMutableStateList()
                },
                styleName = textName,
                leftNameComposable = { vajn.show() }
            )

        dialPan.dial = @Composable {
            BackgroungPanelStyle1(
                style = SimplePlateStyleState(platePanel),
                vignette = VIGNETTE.getValue()
            ) {
                Column(
                    Modifier.fillMaxWidth(0.75f).fillMaxHeight(0.8f).padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    complexOpis.show(this, dialLayInner)
                    RowVA(Modifier.padding(bottom = 5.dp)) {
                        Row(Modifier.weight(1f)) {
                            Spacer(Modifier.weight(1f))
                            MyToggleButtIconStyle1(
                                "ic_round_model_training_24.xml", value = enabledDirection, sizeIcon = 25.dp,
                                modifier = Modifier.padding(horizontal = 5.dp),
                                myStyleToggleButton = ToggleButtonStyleState(buttNaprav)
                            )
                        }
                        Row(Modifier.weight(1f)) {
                            if (enabledDirection.value.not()) {
                                MyToggleButtIconStyle1(
                                    "ic_round_percent_24.xml", value = enabledGotov, sizeIcon = 25.dp,
                                    modifier = Modifier.padding(horizontal = 5.dp),
                                    myStyleToggleButton = ToggleButtonStyleState(buttGotov)
                                )
                                MyToggleButtIconStyle1(
                                    "ic_round_access_time_24.xml", value = expandedDate, sizeIcon = 25.dp,
                                    modifier = Modifier.padding(horizontal = 5.dp),
                                    myStyleToggleButton = ToggleButtonStyleState(buttSrok)
                                )
                            }
                            Spacer(Modifier.weight(1f))
                        }
                    }
                    Column(Modifier.animateContentSize()) {
                        if (expandedDate.value && !enabledDirection.value) {
                            buttDatePickerWithButton(
                                dialLayInner, dateStart,
                                modifier = Modifier.padding(horizontal = 10.dp),
                                myStyleTextDate = TextButtonStyleState(buttDateStart),
                                myStyleTextArrow = TextButtonStyleState(buttDateArrowStart)
                            ) {
                                if (it > dateEnd.value) dateEnd.value = it
                            }
                            buttDatePickerWithButton(
                                dialLayInner, dateEnd,
                                modifier = Modifier.padding(horizontal = 10.dp).padding(bottom = 5.dp),
                                myStyleTextDate = TextButtonStyleState(buttDateEnd),
                                myStyleTextArrow = TextButtonStyleState(buttArrowDateEnd)
                            ) {
                                if (it < dateStart.value) dateStart.value = it
                            }
                        }
                    }
                    Row {
                        MyTextButtStyle1(
                            "Отмена",
                            myStyleTextButton = TextButtonStyleState(buttCancel)
                        ) {
                            cancelListener()
                            dialPan.close()
                        }
                        MyTextButtStyle1(
                            if (change) "Изменить" else "Добавить", Modifier.padding(start = 5.dp),
                            myStyleTextButton = TextButtonStyleState(buttAdd)
                        ) {
                            if (text_name.value.text != "") {
                                complexOpis.listOpis.addUpdList { opis ->
                                    return@addUpdList if (change) {
                                        item?.let {
                                            if (enabledGotov.value && !enabledDirection.value) {
                                                if (item.gotov < 0) MainDB.addTime.updGotovPlan(it.id.toLong(), 0.0)
                                            } else {
                                                if (item.gotov >= 0) MainDB.addTime.updGotovPlan(it.id.toLong(), -1.0)
                                            }
                                            MainDB.addTime.updPlan(
                                                id = it.id.toLong(),
                                                vajn = vajn.value,
                                                name = text_name.value.text,
                                                data1 = if (expandedDate.value && !enabledDirection.value) dateStart.value.time else 0,
                                                data2 = if (expandedDate.value && !enabledDirection.value) dateEnd.value.time else 1,
                                                opis = opis,
                                                direction = enabledDirection.value
                                            )
                                        } ?: listOf()
                                    } else {
                                        MainDB.addTime.addPlan(
                                            vajn = vajn.value,
                                            name = text_name.value.text,
                                            gotov = if (enabledGotov.value && !enabledDirection.value) 0.0 else -1.0,
                                            data1 = if (expandedDate.value && !enabledDirection.value) dateStart.value.time else 0,
                                            data2 = if (expandedDate.value && !enabledDirection.value) dateEnd.value.time else 1,
                                            opis = opis,
                                            stat = TypeStatPlan.VISIB,
                                            direction = enabledDirection.value
                                        )
                                    }
                                }
                                finishListener()
                                dialPan.close()
                            }
                        }
                    }
                }
            }
            dialLayInner.getLay()
        }
    }
    dialPan.show()
}
