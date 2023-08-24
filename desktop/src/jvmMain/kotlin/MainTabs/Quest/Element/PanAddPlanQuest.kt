package MainTabs.Quest.Element

import MyDialog.MyDialogLayout
import adapters.MyComboBox
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import common.BackgroungPanelStyle1
import common.MyOutlinedTextField
import common.MySelectStat
import common.MySelectStat.Companion.statNaborPlan
import common.MyTextButtStyle1
import extensions.RowVA
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemPlanQuest
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlan
import viewmodel.QuestVM


fun PanAddPlanQuest(
    dialPan: MyDialogLayout,
//    MainDB: MainMainDB,
    item: ItemPlanQuest? = null
) {

    val dialLayInner = MyDialogLayout()
//    val dateStart = mutableStateOf(item?.let { Date(it.data1) } ?: Date())
//    val dateEnd = mutableStateOf(item?.let { Date(it.data2) } ?: Date().add(14, TimeUnits.DAY))
//    val expandedDate = mutableStateOf((item?.data1?.withOffset() ?: 0L) != 0L)
    val expandedStat = mutableStateOf(false)
    val vajn = MySelectStat(item?.vajn ?: 1L, statNabor = statNaborPlan)
    val CB_spisStartVisible = MyComboBox(TypeStatPlan.getSelectList(), nameItem = { it.nameType }).apply {
        item?.let {  itemPlanQ ->
            TypeStatPlan.getType(itemPlanQ.commstat).let {
                if (!TypeStatPlan.getIsklSelectList().contains(it)) select(it)
            }
        }
    }

    dialPan.dial = @Composable {
        QuestVM.openQuestDB?.let { questDB ->

            val text_name = remember { mutableStateOf(TextFieldValue(item?.let { it.name } ?: "")) }
            val text_opis = remember { mutableStateOf(TextFieldValue(item?.let { it.opis } ?: "")) }
            BackgroungPanelStyle1 { //modif ->
                Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    MyOutlinedTextField("Название проекта", text_name)
                    MyOutlinedTextField("Описание проекта", text_opis, Modifier.heightIn(200.dp, 500.dp), TextAlign.Start)
                    RowVA {
                        vajn.show()
//                        MyCheckbox(boolVal = expandedDate.value,
//                        modifier = Modifier.padding(start = 20.dp),
//                            label = "Указать сроки")
                        CB_spisStartVisible.show()
                    }
                    Column(Modifier.animateContentSize()) {
//                    if (expandedDate.value) {
//                        buttDatePicker(dialLayInner, dateStart)
//                        buttDatePicker(dialLayInner, dateEnd)
//                    }
                    }
                    Row {
                        MyTextButtStyle1("Отмена") {
                            dialPan.close()
                        }
                        MyTextButtStyle1(item?.let { "Изменить" } ?: "Добавить", Modifier.padding(start = 5.dp)) {
                        if (text_name.value.text != "")
                            item?.let {
                                questDB.addQuest.updPlan(
                                    id = it.id.toLong(),
                                    vajn = vajn.value,
                                    name = text_name.value.text,
                                    hour = 0.0,
                                    srok = -1,
                                    statsrok = -1,
                                    opis = text_opis.value.text,
                                    commstat = CB_spisStartVisible.getSelected()?.run{
                                        if (this == TypeStatPlan.VISIB ) TypeStatPlan.UNBLOCKNOW.codValue else this.codValue
                                    } ?: 0
                                )
                                dialPan.close()
                            } ?: run {
                                questDB.addQuest.addPlan(
                                    vajn = vajn.value,
                                    name = text_name.value.text,
                                    hour = 0.0,
                                    srok = -1,
                                    statsrok = -1,
                                    opis = text_opis.value.text,
                                    commstat = CB_spisStartVisible.getSelected()?.run{
                                        if (this == TypeStatPlan.VISIB ) TypeStatPlan.UNBLOCKNOW.codValue else this.codValue
                                    } ?: 0
                                )
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
