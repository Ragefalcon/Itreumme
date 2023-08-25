package MainTabs.Quest

import MainTabs.Avatar.Element.PanOpenQuest
import MainTabs.Avatar.Items.ComItemLoadQuest
import MyDialog.MyDialogLayout
import MyDialog.MyInfoShow
import MyList
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import ru.ragefalcon.sharedcode.models.data.ItemLoadQuest
import viewmodel.MainDB
import viewmodel.StateVM

class QuestsPanel {

    private val selection = SingleSelectionType<ItemLoadQuest>()
    val vypQuest = mutableStateOf(false)

    @Composable
    fun show(
        dialLay: MyDialogLayout,
        modifier: Modifier = Modifier
    ) {
        val scrollState = rememberScrollState(0)
        Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                MyTextToggleButtStyle1("Вып", vypQuest, modifier = Modifier.padding(start = 15.dp)) {
                    selection.selected = null
                }
                MyTextStyle1(
                    "Взятые квесты: ${MainDB.loadQuestSpis.spisQuest.getState().value?.size ?: 0}",
                    Modifier.weight(1f).padding(horizontal = 10.dp)
                )
                MyTextButtStyle1("+", modifier = Modifier.padding(end = 15.dp)) {
                    PanOpenQuest(dialLay) {}
                }
            }
            fun showQuestInfo(item: ItemLoadQuest) {
                MyInfoShow(dialLay) {
                    MainDB.addQuest.getQuestMainParam(item.id.toLong()).let { list ->
                        Column(Modifier.padding(20.dp).heightIn(0.dp, 500.dp)) {
                            Text(item.name, Modifier.padding(bottom = 20.dp), style = MyTextStyleParam.style1)
                            MyList(list, Modifier.weight(1f, false)) { ind, item ->
                                if (item.name != "name") {
                                    Text(
                                        if (item.name == "opis") "Описание" else item.name,
                                        style = MyTextStyleParam.style2.copy(fontSize = 18.sp)
                                    )
                                    Text(
                                        item.stringparam,
                                        Modifier.padding(bottom = 15.dp),
                                        style = MyTextStyleParam.style5.copy(fontSize = 15.sp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            MyList(MainDB.loadQuestSpis.spisQuest, Modifier.weight(1f)) { ind, itemLoadQuest ->
                ComItemLoadQuest(itemLoadQuest, selection, doubleClick = {
                    showQuestInfo(it)
                }) { item, expanded ->
                    MyDropdownMenuItem(expanded, "Показать описание") {
                        showQuestInfo(item)
                    }
                    MyDeleteDropdownMenuButton(expanded) {
                        MainDB.addQuest.deleteFullQuest(item.id, StateVM.dirLoadedQuestFiles, StateVM.dirIconNodeTree)
                        selection.selected = null
                    }

                }.getComposable()
            }
        }
    }
}