package MainTabs

import MainTabs.Journal.Element.PanAddBloknot
import MainTabs.Journal.IdeaPanel
import MainTabs.Journal.Items.ComItemBloknot
import MyDialog.MyDialogLayout
import MyList
import MyShowMessage
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.MyDeleteDropdownMenuButton
import common.MyTextButtStyle1
import extensions.*
import viewmodel.MainDB
import viewmodel.StateVM

class MainJournalTabs(val dialLay: MyDialogLayout) {

    init {
        println("init MainJournalTabs: $this")
    }

    //    val dialLay = MyDialogLayout()
    val listStateIdea = MyLazyListState() //: LazyListState = LazyListState(0)
    var lastSelectBloknot by mutableStateOf(-1L)
    val ideaPanel = IdeaPanel(listStateIdea)

    @Composable
    fun show() {
        LaunchedEffect(StateVM.selectBloknot.value) {
            StateVM.selectBloknot.value?.let { bloknot ->
                if (lastSelectBloknot != bloknot.id.toLong()) listStateIdea.scrollToZero()
                lastSelectBloknot = bloknot.id.toLong()
                MainDB.journalFun.setBloknotForSpisIdea(bloknot.id.toLong(), StateVM.filterIdea.value)
            }
            if (StateVM.selectionIdea.selected?.bloknot.toString() != StateVM.selectBloknot.value?.id) {
                StateVM.selectionIdea.selected = null
            }
        }
        LaunchedEffect(StateVM.selectionIdea.selected) {
            if (StateVM.selectionIdeaStap.selected?.idea_id.toString() != StateVM.selectionIdea.selected?.id) {
                StateVM.openIdeaOpis.value = false
                StateVM.selectionIdeaStap.selected = null
            }
        }
        Box {
            if (StateVM.selectBloknot.value == null) {
                bloknotPage(dialLay)
            } else {
                ideaPage(dialLay, ideaPanel)
            }
//            dialLay.getLay()
        }
    }
}

@Composable
fun ideaPage(dialLay: MyDialogLayout, ideaPanel: IdeaPanel) {
    StateVM.selectBloknot.value?.let {
        MainDB.styleParam.journalParam.itemBloknot.getComposable(::ItemBloknotStyleState) { itemBloknotStyle ->
            Column {
                ComItemBloknot(it, StateVM.selectionBloknot, edit = false, {
                    StateVM.selectBloknot.value = null
                }, itemBloknotStyleState =  itemBloknotStyle)
                ideaPanel.show(dialLay)
            }
        }
    }
}

@Composable
fun bloknotPage(dialLay: MyDialogLayout) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        MainDB.styleParam.journalParam.itemBloknot.getComposable(::ItemBloknotStyleState) { itemBloknotStyle ->
            MyList(MainDB.journalSpis.spisBloknot, Modifier.padding(10.dp).weight(1f)) { ind, itemBloknot ->
                ComItemBloknot(
                    itemBloknot, StateVM.selectionBloknot, openBloknot = { bloknot ->
                        StateVM.selectBloknot.value = bloknot
                    }, itemBloknotStyleState =  itemBloknotStyle, dialLay = dialLay
                ) { item, expanded ->
                    DropdownMenuItem(onClick = {
                        PanAddBloknot(dialLay, item)
                        expanded.value = false
                    }) {
                        Text(text = "Изменить", color = Color.White)
                    }
                    MyDeleteDropdownMenuButton(expanded){
                        if (item.countidea == 0L) {
                            MainDB.addJournal.delBloknot(item.id.toLong()){
                                MainDB.complexOpisSpis.spisComplexOpisForBloknot.delAllImageForItem(it)
                            }
                        } else {
                            MyShowMessage(dialLay, "Удалите вначале все записи из блокнота")
                        }
                    }
/*
                DropdownMenuItem(onClick = {
                    if (item.countidea == 0L) {
                        MainDB.addJournal.delBloknot(item.id.toLong())
                    } else {
                        MyShowMessage(dialLay, "Удалите вначале все записи из блокнота")
                    }
                    expanded.value = false
                }) {
                    Text(text = "Удалить", color = Color.White)
                }
*/

                }
            }
        }
        Row {
            MyTextButtStyle1("+",
                modifier = Modifier.padding(0.dp),
//                modifierText = Modifier.padding(10.dp),
                width = 70.dp,
                height = 50.dp,
                myStyleTextButton = TextButtonStyleState(MainDB.styleParam.journalParam.addBlokButt)) {
                PanAddBloknot(dialLay)
            }
        }
    }
}
