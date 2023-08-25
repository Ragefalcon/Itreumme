package MainTabs.Quest.Element

import MainTabs.Quest.Items.ComItemPlanQuest
import MainTabs.Quest.Items.ComItemPlanStapQuest
import MainTabs.Quest.Items.ItemPlanQuestPlate
import MainTabs.Quest.Items.ItemPlanStapQuestPlate
import MyList
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.MyTextButtStyle1
import common.SingleSelectionType
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemPlanQuest
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemPlanStapQuest
import viewmodel.QuestVM

class BoxSelectParentPlanQuest(
    val emptyPlan: Boolean = true,
    var arrayIskl: List<Long> = listOf(),
    itemPlanParent: ItemPlanQuest? = null,
    itemPlanStapParent: ItemPlanStapQuest? = null,
    val selectForGoalOrDream: Boolean = false,
    startWithOpenPlan: Boolean = false,
    val dream: Boolean = false,
    var idIsklPlan: String = "",
    var onlyPlans: Boolean = false
) {
    val selectionPlanParent = SingleSelectionType<ItemPlanQuest>().apply { selected = itemPlanParent }
    val selectionPlanStapParent = SingleSelectionType<ItemPlanStapQuest>().apply { selected = itemPlanStapParent }
    val expandedSelPlan = mutableStateOf(startWithOpenPlan)
    val expandedSelPlanStap = mutableStateOf(false)

    fun isExpanded() = expandedSelPlan.value || expandedSelPlanStap.value

    @Composable
    fun getComposable(modifier: Modifier = Modifier) {
        QuestVM.withDBC { questDB ->
            selectionPlanParent.launchedEffect {
                it?.let {
                    questDB.questFun.setPlanForSpisStapPlanForSelect(
                        it.id.toLong(),
                        arrayIskl
                    )
                }
            }
            Column(
                modifier = modifier.padding(bottom = 5.dp).animateContentSize().border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(listOf(Color(0xFFFFF7D9), Color(0xFFFFF7D9))),
                    shape = RoundedCornerShape(10.dp)
                ).background(
                    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                    color = Color(0xFFE4E0C7),
                ), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!expandedSelPlan.value) {
                    selectionPlanParent.selected?.let {
                        ItemPlanQuestPlate(
                            it,
                            modifier = Modifier.padding(bottom = 5.dp)
                        ) {
                            expandedSelPlan.value = true
                        }
                        if (!expandedSelPlanStap.value && !onlyPlans) {
                            if (selectionPlanStapParent.selected == null) MyTextButtStyle1("Сделать подъэтапом") {
                                expandedSelPlanStap.value = true
                            } else {
                                selectionPlanStapParent.selected?.let {
                                    ItemPlanStapQuestPlate(
                                        it,
                                        Modifier.padding(horizontal = 20.dp)
                                    ) {
                                        expandedSelPlanStap.value = true
                                    }
                                }
                            }
                        }
                        if (expandedSelPlanStap.value && !onlyPlans) {
                            MyList(
                                questDB.spisQuest.spisStapPlanForSelect,
                                Modifier.weight(1f).padding(bottom = 10.dp).padding(horizontal = 20.dp),
                                darkScroll = true
                            ) { _, itemPlanStap ->

                                ComItemPlanStapQuest(itemPlanStap, selectionPlanStapParent, sverFun = {
                                    questDB.questFun.setExpandStapPlan(it.id.toLong(), it.svernut.not())
                                }, selFun = {
                                    expandedSelPlanStap.value = false
                                }, editable = false).getComposable()
                            }
                            MyTextButtStyle1("Отменить выбор") {
                                selectionPlanStapParent.selected = null
                                expandedSelPlanStap.value = false
                            }
                        }
                    }
                    if (selectionPlanParent.selected == null) MyTextButtStyle1("Выбрать проект") {
                        expandedSelPlan.value = true
                    }
                } else {
                    MyList(
                        questDB.spisQuest.spisPlan,
                        Modifier.weight(1f).padding(bottom = 10.dp),
                        darkScroll = true
                    ) { _, itemPlan ->
                        var check = itemPlan.id != idIsklPlan
                        if (check) ComItemPlanQuest(itemPlan, selectionPlanParent, selFun = {
                            selectionPlanStapParent.selected = null
                            questDB.questFun.setPlanForSpisStapPlanForSelect(
                                it.id.toLong(),
                                arrayIskl
                            )
                            expandedSelPlan.value = false
                        }, editable = false).getComposable()
                    }
                    if (emptyPlan) MyTextButtStyle1("Отменить выбор") {
                        selectionPlanParent.selected = null
                        expandedSelPlan.value = false
                    }
                }
            }
        }
    }
}