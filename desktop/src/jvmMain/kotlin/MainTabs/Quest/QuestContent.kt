package MainTabs.Quest

import MainTabs.Avatar.SkillsPanel
import MainTabs.Quest.Element.DevelopQuestTab
import MainTabs.Quest.Element.DialogQuestTab
import MainTabs.Quest.Element.PlanQuestTab
import MyDialog.MyDialogLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.DiskretSeekBarManyRows
import common.SingleSelectionType
import ru.ragefalcon.sharedcode.models.data.ItemEffekt
import viewmodel.MainDB
import viewmodel.QuestVM

class QuestContent(val dialLay: MyDialogLayout) {

    val timeSeekBar =
        DiskretSeekBarManyRows(
            listOf(listOf(
                "Цели" to "Goal",
                "Навыки" to "Skills",
                "Проекты" to "Plans",
            ),
            listOf(
                "Диалоги" to "Dialogs",
                "Ежедневник" to "DenPlans",
                "для разработчика" to "Develop"
            ),
            ),
//            "Plans",
            "Skills",
        )
//    val denPlans = DenPlanTab(dialLay)
//    val vxodTab = VxodTab(dialLay)
    val developQuestTab = DevelopQuestTab(dialLay)
    val skillQuestTab = SkillsPanel(true) //QuestVM.openQuestDB) // SkillQuestTab(dialLay)
    val planQuestTab = PlanQuestTab(dialLay)
    val dialogQuestTab = DialogQuestTab(dialLay)

    private val selection = SingleSelectionType<ItemEffekt>()

    @Composable
    fun show() {
        LaunchedEffect(MainDB.denPlanDate.value) {
            if (MainDB.timeFun.getDay() != MainDB.denPlanDate.value.time) MainDB.timeFun.setDay(MainDB.denPlanDate.value.time)
        }
        Box() {
            Row {
                Column(
                    Modifier.background(color = Color(0xFF576350)).weight(1f).padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    timeSeekBar.show(Modifier.fillMaxWidth().padding(bottom = 10.dp))//weight(1f))
                    timeSeekBar.active?.let {
                        when (it.cod) {
                            "Plans" -> {
//                                DreamsPanel().show(dialLay, Modifier.fillMaxWidth())
                                planQuestTab.show()
                            }
                            "Skills" -> {
                                skillQuestTab.show(dialLay)
                            }
                            "Dialogs" -> {
                                dialogQuestTab.show()
                            }
                            "Develop" -> {
                                developQuestTab.show()
                            }
                            "DenPlans" -> {
//                                denPlans.show()
                            }
                            "Goal" -> {
//                                vxodTab.show()
                            }
                        }
                    }
                }
            }
        }
    }
}