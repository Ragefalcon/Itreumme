package MainTabs.Quest.Element

import MainTabs.Quest.Items.ComInnerStartTrigger
import MyDialog.MyDialogLayout
import MyList
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.MyDropdownMenuItem
import common.MyTextButtStyle1
import common.SingleSelection
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.InnerDialogs.InnerStartTriggerEnum
import viewmodel.MainDB
import viewmodel.QuestVM

class DevelopQuestTab(val dialLay: MyDialogLayout) {

    private val selection = SingleSelection()

    @Composable
    fun show() {
        QuestVM.openQuestDB?.let { questDB ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                MyTextButtStyle1("Обновить внутренние диалоги", Modifier.padding(20.dp)) {
                    MainDB.updateInnerDialog(true)
                }

                MyList(list = InnerStartTriggerEnum.values().toList()) { _, item ->
                    ComInnerStartTrigger(item, selection) { item, exp ->
                        MyDropdownMenuItem(exp, "+ Триггер") {
                            PanAddTrigger(dialLay, item.parentTrig())
                        }
                    }.getComposable()
                }
            }
        }
    }
}