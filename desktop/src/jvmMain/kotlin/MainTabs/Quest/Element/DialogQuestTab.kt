package MainTabs.Quest.Element

import MainTabs.Quest.Items.ComItemDialog
import MainTabs.Quest.Items.ComItemGovorun
import MyDialog.MyDialogLayout
import MyList
import MyListRow
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import common.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemDialog
import viewmodel.QuestVM
import java.io.File


class DialogQuestTab(val dialLay: MyDialogLayout) {

    private val selection = SingleSelectionType<ItemDialog>()
    private val selectionGovorun = SingleSelection()

    @Composable
    fun show() {
        QuestVM.openQuestDB?.let { questDB ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
                    MyTextStyle1(
                        "Действующие лица (${questDB.spisQuest.spisGovorun.getState().value?.size ?: 0}):",
                        Modifier.weight(1f).padding(horizontal = 10.dp),
                        textAlign = TextAlign.Start
                    )
                    MyTextButtStyle1("+", modifier = Modifier.padding(start = 15.dp)) {
                        PanAddGovorun(dialLay)
                    }
                }
                MyListRow(questDB.spisQuest.spisGovorun, Modifier.heightIn(0.dp, 150.dp)) { ind, govorun ->
                    ComItemGovorun(govorun, selectionGovorun, questDB.dirQuest) { item, exp ->
                        MyDropdownMenuItem(exp, "Изменить") {
                            PanAddGovorun(dialLay, item)
                        }
                        MyDeleteDropdownMenuButton(exp) {
                            val tmp = File(questDB.dirQuest, "${item.image_file}.jpg")
                            if (tmp.exists()) tmp.delete()
                            questDB.addQuest.delGovorun(item.id.toLong())
                        }
                    }.getComposable()
                }
                Row(modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
                    MyTextStyle1(
                        "Количесто диалогов: ${questDB.spisQuest.spisDialog.getState().value?.size ?: 0}",
                        Modifier.weight(1f).padding(horizontal = 10.dp),
                        textAlign = TextAlign.Start
                    )
                    selection.selected?.let { dialog ->
                        MyTextButtStyle1("setStart", modifier = Modifier.padding(start = 15.dp)) {
                            QuestVM.openQuestDB?.addQuest?.setStartQuestDialog(dialog)
                        }
                    }
                    MyTextButtStyle1("+", modifier = Modifier.padding(start = 15.dp)) {
                        PanAddDialog(dialLay)
                    }
                }
                val coroutineScope = rememberCoroutineScope()
                val stateList: LazyListState = rememberLazyListState()
                MyList(questDB.spisQuest.spisDialog, Modifier.weight(1f), state = stateList) { ind, itemDialog ->
                    ComItemDialog(itemDialog, selection, dialLay, { delayStart ->
                        coroutineScope.launch {
                            delay(delayStart)
                            var key = true
                            stateList.layoutInfo.visibleItemsInfo.find { it.index == ind }?.let { itemInfo ->
                                if (itemInfo.offset >= 0 && (itemInfo.offset + itemInfo.size) <= stateList.layoutInfo.viewportEndOffset) key =
                                    false
                            }
                            if (key) stateList.animateScrollToItem(ind)
                        }
                    }) { item, expanded ->
                        DropdownMenuItem(onClick = {
                            PanAddDialog(dialLay, item)
                            expanded.value = false
                        }) {
                            Text(text = "Изменить", color = Color.White)
                        }
                        MyDeleteDropdownMenuButton(expanded) {
                            questDB.addQuest.delDialog(item.id.toLong())
                        }
                    }.getComposable()
                }
            }
        }
    }
}
