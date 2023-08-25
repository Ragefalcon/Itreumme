package MainTabs.Quest.Element

import MyDialog.MyDialogLayout
import adapters.MyComboBox
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import common.*
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemPlanQuest
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemPlanStapQuest
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlan
import viewmodel.QuestVM

fun PanAddPlanStapQuest(
    dialPan: MyDialogLayout,
    itemPlanStapParent: ItemPlanStapQuest? = null,
    itemPlanParent: ItemPlanQuest,
    item: ItemPlanStapQuest? = null
) {
    val dialLayInner = MyDialogLayout()

    val selParents = BoxSelectParentPlanQuest(false).apply {
        selectionPlanParent.selected = itemPlanParent
        selectionPlanStapParent.selected = itemPlanStapParent ?: item?.let { currItem ->
            QuestVM.openQuestDB?.spisQuest?.spisOpenStapPlan?.getState()?.value?.find {
                it.id == currItem.parent_id
            }
        }
    }

    QuestVM.withDB { questDB ->
        questDB.questFun.setPlanForSpisStapPlanForSelect(
            itemPlanParent.id.toLong(),
            item?.id?.toLong()?.let { listOf(it) } ?: listOf())
    }

    val CB_spisStartVisible = MyComboBox(TypeStatPlan.getSelectList(), nameItem = { it.nameType }).apply {
        item?.let { itemPlanQ ->
            TypeStatPlan.getType(itemPlanQ.commstat).let {
                if (!TypeStatPlan.getIsklSelectList().contains(it)) select(it)
            }

        }
    }

    dialPan.dial = @Composable {
        QuestVM.withDBC { questDB ->
            val text_name = remember { mutableStateOf(TextFieldValue(item?.let { it.name } ?: "")) }
            val text_opis = remember { mutableStateOf(TextFieldValue(item?.let { it.opis } ?: "")) }
            BackgroungPanelStyle1 {
                Column(
                    Modifier.padding(15.dp).fillMaxWidth(0.8F).heightIn(0.dp, dialPan.layHeight.value * 0.8F),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    selParents.getComposable()
                    if (!selParents.isExpanded()) {
                        MyOutlinedTextField("Название проекта", text_name)
                        MyOutlinedTextField(
                            "Описание проекта", text_opis, Modifier
                                .height(400.dp), TextAlign.Start
                        )
                    }
                    CB_spisStartVisible.show()
                    Column(Modifier.animateContentSize()) {
                    }
                    Row {
                        MyTextButtStyle1("Отмена") {
                            dialPan.close()
                        }
                        MyTextButtStyle1(item?.let { "Изменить" } ?: "Добавить", Modifier.padding(start = 5.dp)) {
                            if (text_name.value.text != "")
                                item?.let {
                                    questDB.addQuest.updStapPlan(
                                        id = it.id.toLong(),
                                        name = text_name.value.text,
                                        srok = -1,
                                        statsrok = -1,
                                        opis = text_opis.value.text,
                                        parent_id = selParents.selectionPlanStapParent.selected?.id?.toLong() ?: -1L,
                                        idplan = selParents.selectionPlanParent.selected?.id?.toLong()
                                            ?: itemPlanParent.id.toLong(),
                                        hour = 0.0,
                                        commstat = CB_spisStartVisible.getSelected()?.codValue ?: 0
                                    )
                                    dialPan.close()
                                } ?: run {
                                    questDB.addQuest.addStapPlan(
                                        name = text_name.value.text,
                                        hour = 0.0,
                                        srok = -1,
                                        statsrok = -1,
                                        opis = text_opis.value.text,
                                        parent_id = selParents.selectionPlanStapParent.selected?.id?.toLong() ?: -1L,
                                        idplan = selParents.selectionPlanParent.selected?.id?.toLong()
                                            ?: itemPlanParent.id.toLong(),
                                        commstat = CB_spisStartVisible.getSelected()?.codValue ?: 0
                                    )
                                    dialPan.close()
                                }
                        }
                    }
                }
            }
        }
        dialLayInner.getLay()
    }
    dialPan.show()
}
