package MainTabs.Time.Elements


import MyDialog.MyDialogLayout
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import common.BackgroungPanelStyle1
import common.MyComplexOpisWithNameBox
import common.MySelectStat
import common.MyTextButtStyle1
import extensions.addUpdList
import ru.ragefalcon.sharedcode.models.data.ItemComplexOpis
import ru.ragefalcon.sharedcode.models.data.ItemNextActionCommon
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TableNameForComplexOpis
import viewmodel.MainDB

fun PanAddNextAction(
    dialPan: MyDialogLayout,
    item: ItemNextActionCommon? = null,
    cancelListener: () -> Unit = {},
    listOp: List<ItemComplexOpis>? = null,
    finishListener: () -> Unit = {}
) {

    val text_name = mutableStateOf(TextFieldValue(item?.let { it.name } ?: ""))
    val vajn = MySelectStat(item?.vajn ?: 1L, statNabor = MySelectStat.statNaborPlan, iconRes = "bookmark_01.svg")
    val complexOpis =
        MyComplexOpisWithNameBox(
            "Название следующего действия",
            text_name,
            "Описание следующего действия",
            item?.id ?: -1L,
            TableNameForComplexOpis.spisNextAction,
            MainDB.styleParam.timeParam.denPlanTab.complexOpisForDenPlan,
            listOp?.toMutableStateList() ?: item?.let {
                MainDB.complexOpisSpis.spisComplexOpisForNextActionCommon.getState().value?.get(it.common_id)
                    ?.toMutableStateList()
            },
            leftNameComposable = {
                vajn.show()
            }
        )

    val dialLayInner = MyDialogLayout()


    val selParents = BoxSelectParentPlan(label = "Выберете проект/этап для привязки").apply {
        item?.let { itemDP ->

            selectionPlanParent.selected = MainDB.timeSpis.spisAllPlan.getState().value?.find { itemPlan ->
                itemPlan.id.toLong() == itemDP.privplan
            }
            selectionPlanStapParent.selected = MainDB.timeSpis.spisAllPlanStap.getState().value?.find { itemPlanStap ->
                itemPlanStap.id.toLong() == itemDP.stap_prpl
            }
        }
    }

    dialPan.dial = @Composable {
        BackgroungPanelStyle1 { //modif ->
            Column(
                Modifier.fillMaxWidth(0.8F).fillMaxHeight(0.95f).padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                selParents.getComposable(Modifier.fillMaxWidth(0.8F).padding(bottom = 5.dp))
                if (!selParents.isExpanded()) {
                    complexOpis.show(this, dialLayInner)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(Modifier.weight(1f))
                        MyTextButtStyle1("Отмена") {
                            dialPan.close()
                            cancelListener()
                        }
                        Spacer(Modifier.weight(1f))
                        if (text_name.value.text != "") {
                            MyTextButtStyle1(item?.let { "Изменить" } ?: "Добавить", Modifier.padding(start = 5.dp)) {
                                complexOpis.listOpis.addUpdList { opis ->
                                    MainDB.addTime.updOrAddNextAction(
                                        id = item?.let {  if (it.common_id > 0) it.common_id else null },
                                        name = text_name.value.text,
                                        vajn = vajn.value,
                                        opis = opis,
                                        privplan = selParents.selectionPlanParent.selected?.id?.toLong() ?: -1L,
                                        stap_prpl = selParents.selectionPlanStapParent.selected?.id?.toLong() ?: -1L,
                                    )
                                }
                                dialPan.close()
                                finishListener()
                            }
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
        dialLayInner.getLay()
    }
    dialPan.show()
}

