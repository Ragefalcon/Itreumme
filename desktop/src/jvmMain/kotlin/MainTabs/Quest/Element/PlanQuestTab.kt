package MainTabs.Quest.Element

import MainTabs.Quest.Items.ComItemPlanQuest
import MainTabs.Quest.Items.ComItemPlanStapQuest
import MainTabs.Quest.Items.ItemPlanQuestPlate
import MyDialog.MyDialogLayout
import MyList
import MyShowMessage
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemPlanQuest
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemPlanStapQuest
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.parentOfTrig
import viewmodel.QuestVM


class PlanQuestTab(val dialLay: MyDialogLayout) {

    private val selection = SingleSelectionType<ItemPlanQuest>()
    private val selectionStap = SingleSelectionType<ItemPlanStapQuest>()

    val vypPlan = mutableStateOf(false)
    val vypPlanStap = mutableStateOf(false)
    val openStapPan = mutableStateOf(false)
    var openStapPan1 by mutableStateOf(false)


    @Composable
    private fun spisPlan(scope: ColumnScope) {
        QuestVM.openQuestDB?.let { questDB ->
            scope.apply {
                MyList(questDB.spisQuest.spisPlan, modifier = Modifier.padding(10.dp).weight(1f)) { ind,itemPlanQuest ->
                    ComItemPlanQuest(
                        itemPlanQuest, selection,
                        selFun = {
                        },
                        openStaps = {
                            openStapPan.value = true
                        }
                    ) { item, expanded ->
                        MyDropdownMenuItem(expanded, "+ Триггер") {
                            PanAddTrigger(dialLay, item.parentOfTrig())
                        }
                        MyDropdownMenuItem(expanded = expanded, name = "Изменить") {
                            PanAddPlanQuest(dialLay, item)
                        }
                        MyDeleteDropdownMenuButton(expanded) {
                            if (item.countstap > 0) {
                                MyShowMessage(dialLay, "Удалите вначале все этапы этого проекта")
                            } else {
                                questDB.addQuest.delPlan(item.id.toLong())
                            }
                        }

                    }.getComposable()
                }
            }
        }
    }

    @Composable
    fun show(modifier: Modifier = Modifier) {
        QuestVM.withDBC { questDB ->
            LaunchedEffect(selection.selected) {
                selection.selected?.let {
//                QuestVM.withDB { questDB ->
                    questDB.questFun.setPlanForSpisStapPlan(it.id.toLong())
                    questDB.questFun.setPlanForCountStapPlan(it.id.toLong())
                    questDB.questFun.setOpenSpisStapPlan(vypPlanStap.value)
//                }
                }
            }

            val scrollStateStap = rememberLazyListState(0)
            Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                if (openStapPan.value) {
                    selection.selected?.let {
                        ItemPlanQuestPlate(it, Modifier.padding(bottom = 5.dp)) {
                            openStapPan.value = false
                            openStapPan1 = false
                            GlobalScope.launch {
                                scrollStateStap.scrollToItem(0, 0)
                            }
                        }
                    }
                } else {
                    Row(modifier = Modifier.padding(bottom = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                        MyTextToggleButtStyle1("Вып", vypPlan, modifier = Modifier.padding(start = 15.dp)) {
//                    MainDB.timeFun.setOpenSpisPlan(it)
                            selection.selected?.let {
//                        if (it.stat == 10L) selection.selected = null
                            }
                        }
                        MyTextStyle1(
                            "Количесто проектов: ${questDB.spisQuest.spisPlan.getState().value?.size ?: 0}",
                            Modifier.weight(1f).padding(horizontal = 10.dp)
                        )
                        MyTextButtStyle1("+", modifier = Modifier.padding(start = 15.dp)) {
                            PanAddPlanQuest(dialLay)
                        }
                    }
                    spisPlan(this)
                }
                selection.selected?.let {
                    Column(
                        modifier = Modifier.padding(bottom = 5.dp).animateContentSize().border(
                            width = 1.dp,
                            brush = Brush.horizontalGradient(listOf(Color(0xFFFFF7D9), Color(0xFFFFF7D9))),
                            shape = RoundedCornerShape(10.dp)
                        ).background(
                            shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                            color = Color(0xFFE4E0C7),
                        )
                    ) {
                        Row(modifier = Modifier.clickable {

                            openStapPan.value = true
                        }, verticalAlignment = Alignment.CenterVertically) {
/*
                            MyTextToggleButtStyle1("Вып", vypPlanStap, modifier = Modifier.padding(start = 15.dp)) {
                                MainDB.timeFun.setOpenSpisStapPlan(it)
                                selectionStap.selected?.let {
//                                if (it.stat == 10L) selectionStap.selected = null
                                }
                            }
*/
                            MyTextStyle1(
                                "Количесто этапов: ${questDB.spisQuest.countStapPlan.getState().value ?: 0L}",
                                Modifier.weight(1f)
                                    .padding(horizontal = 10.dp),
                                color = Color(0xFF464D45)
                            )
                            MyTextButtStyle1("+", modifier = Modifier.padding(start = 15.dp)) {
                                selection.selected?.let {
                                    PanAddPlanStapQuest(dialLay, null, it)
                                }
                            }
                        }
                        if (openStapPan.value)
                            MyList(
                                questDB.spisQuest.spisOpenStapPlan,
                                Modifier.padding(10.dp).weight(1f).padding(bottom = 10.dp),
                                scrollStateStap
                            ) { ind, itemPlanStap ->
                                ComItemPlanStapQuest(itemPlanStap, selectionStap, sverFun = {
                                    questDB.questFun.setExpandStapPlan(it.id.toLong(), it.svernut.not())
                                }) { item, expanded ->
                                    MyDropdownMenuItem(expanded, "+ Триггер") {
                                        PanAddTrigger(dialLay, item.parentOfTrig())
                                    }
                                    MyDropdownMenuItem(name = "Изменить", expanded = expanded) {
                                        selection.selected?.let { itemPlan ->
                                            PanAddPlanStapQuest(dialLay, null, itemPlan, item)
                                        }
                                    }
                                    MyDeleteDropdownMenuButton(expanded) {
                                        if (item.podstapcount > 0) {
                                            MyShowMessage(dialLay, "Удалите вначале все подъэтапы этого этапа")
                                        } else {
                                            questDB.addQuest.delStapPlan(item.id.toLong())
                                        }
                                    }

                                }.getComposable()
                            }

                    }
                }
            }
        }
    }
}
