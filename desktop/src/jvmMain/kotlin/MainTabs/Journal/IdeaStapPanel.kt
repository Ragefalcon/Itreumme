package MainTabs.Journal

import MainTabs.Journal.Element.PanAddIdeaStap
import MainTabs.Journal.Items.ComItemIdeaStap
import MyDialog.MyDialogLayout
import MyList
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.MyComplexOpisView
import common.MyShadowBox
import common.MyTextButtStyle1
import extensions.*
import viewmodel.MainDB
import viewmodel.StateVM

class IdeaStapPanel() {

    @Composable
    fun show(dialLay: MyDialogLayout, columnScope: ColumnScope) {
        val scrollState = rememberLazyListState()
        columnScope.apply {
            if (!StateVM.openIdeaOpis.value) {
                MainDB.styleParam.journalParam.itemIdeaStap.getComposable(::ItemIdeaStapStyleState) { itemStyle ->
                    MyList(
                        MainDB.journalSpis.spisStapIdea, Modifier.padding(10.dp).weight(1f),
                        scrollState,
                        darkScroll = true
                    ) { ind, itemIdea ->
                        ComItemIdeaStap(itemIdea, StateVM.selectionIdeaStap, openFun = {
                            StateVM.openIdeaOpis.value = true
                        }, itemIdeaStapStyleState = itemStyle, dialLay = dialLay) { item, expanded ->
                            DropdownMenuItem(onClick = {
                                PanAddIdeaStap(dialLay, item)
                                expanded.value = false
                            }) {
                                Text(
                                    text = "Изменить",
                                    color = Color.White
                                )
                            }
                            DropdownMenuItem(onClick = {
                                MainDB.addJournal.delStapIdea(item.id.toLong()) {
                                    MainDB.complexOpisSpis.spisComplexOpisForIdeaStap.delAllImageForItem(it)
                                }
                                expanded.value = false
                            }) {
                                Text(
                                    text = "Удалить",
                                    color = Color.White
                                )
                            }

                        }
                    }
                }
                Row(Modifier.padding(bottom = 10.dp).fillMaxWidth(), Arrangement.Center) {
                    MyTextButtStyle1(
                        "+",
                        modifier = Modifier.padding(start = 0.dp),

                        width = 70.dp,
                        height = 35.dp,
                        myStyleTextButton = TextButtonStyleState(MainDB.styleParam.journalParam.addIdeaStapButt)
                    ) {
                        PanAddIdeaStap(dialLay)
                    }
                    MyTextButtStyle1(
                        StateVM.filterIdeaStap.value,
                        modifier = Modifier.padding(start = 15.dp),
                        height = 35.dp,
                        myStyleTextButton = TextButtonStyleState(MainDB.styleParam.journalParam.sortIdeaStapButt)
                    ) {
                        if (StateVM.filterIdeaStap.value == "stat") {
                            StateVM.filterIdeaStap.value = "name"
                        } else {
                            StateVM.filterIdeaStap.value = "stat"
                        }
                        StateVM.selectionIdea.selected?.id?.toLong()?.let {
                            MainDB.journalFun.setIdeaForSpisStapIdea(it, sortField = StateVM.filterIdeaStap.value)
                        }

                    }
                }
            } else {
                StateVM.selectionIdeaStap.selected?.let { itemIdeaStap ->
                    val scroll = rememberScrollState(0)
                    Row(
                        Modifier.padding(horizontal = 10.dp).padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MainDB.journalSpis.spisStapIdea.getState().value?.let { list ->
                            if (list.count() > 1) MyTextButtStyle1(
                                "❮❮", modifier = Modifier.padding(end = 10.dp),
                                height = 35.dp,
                                myStyleTextButton = TextButtonStyleState(MainDB.styleParam.journalParam.nextIdeaStapButt)
                            ) {
                                list.indexOf(StateVM.selectionIdeaStap.selected).let { indexSel ->
                                    StateVM.selectionIdeaStap.selected =
                                        if (indexSel > 0) list[indexSel - 1] else list.lastOrNull()
                                }
                            }
                            Box(Modifier.weight(1f)) {
                                ComItemIdeaStap(
                                    itemIdeaStap,
                                    StateVM.selectionIdeaStap,
                                    openFun = {
                                        StateVM.openIdeaOpis.value = false
                                    },
                                    editable = false,
                                    itemIdeaStapStyleState = ItemIdeaStapStyleState(MainDB.styleParam.journalParam.itemIdeaStap)
                                )
                            }
                            if (list.count() > 1) MyTextButtStyle1(
                                "❯❯",
                                modifier = Modifier.padding(start = 10.dp),
                                height = 35.dp,
                                myStyleTextButton = TextButtonStyleState(MainDB.styleParam.journalParam.nextIdeaStapButt)
                            ) {
                                list.indexOf(StateVM.selectionIdeaStap.selected).let { indexSel ->
                                    StateVM.selectionIdeaStap.selected =
                                        if (indexSel < list.count() - 1) list[indexSel + 1] else list[0]
                                }
                            }
                        }
                    }
                    with(ItemIdeaStapStyleState(MainDB.styleParam.journalParam.itemIdeaStap).opisOpenText) {
                        with(ComplexOpisStyleState(MainDB.styleParam.journalParam.complexOpisForIdeaStap)) {
                            MyShadowBox(shadow = plateView.shadow) {
                                if (StateVM.openIdeaOpis.value)
                                    BoxWithVScrollBar(modifier = (if (StateVM.openIdeaOpis.value) padingOuter else Modifier)
                                        .fillMaxSize()
                                        .background(plateView.BACKGROUND, plateView.shape)
                                        .clip(plateView.shape)
                                        .border(
                                            width = plateView.BORDER_WIDTH,
                                            brush = plateView.BORDER,
                                            shape = plateView.shape
                                        )
                                        .then(inner_padding)
                                        /*
                                                                            .background(background, shapeCard)
                                                                            .border(
                                                                                width = borderWidth,
                                                                                brush = border,
                                                                                shape = shapeCard
                                                                            )
                                        */
                                        .run {
                                            if (StateVM.openIdeaOpis.value) this.then(padingInner)
                                            else this
                                        }) { scrollStateBox ->
                                        MainDB.complexOpisSpis.spisComplexOpisForIdeaStap.getState().value?.let { mapOpis ->
                                            mapOpis[itemIdeaStap.id.toLong()]?.let { listOpis ->
                                                SelectionContainer {
                                                    MyComplexOpisView(
                                                        outer_padding
                                                            .fillMaxWidth()
                                                            .verticalScroll(scrollStateBox, enabled = true),
                                                        listOpis,
                                                        dialLay,
                                                        ComplexOpisStyleState(MainDB.styleParam.timeParam.planTab.complexOpisForPlan)
                                                    )
                                                }
                                            }
                                        }
                                        /*
                                                                            SelectionContainer {
                                                                                Text(
                                                                                    modifier = Modifier.verticalScroll(scrollStateBox, enabled = true),
                                                                                    text = itemIdeaStap.opis,
                                                                                    style = textStyle
                                                                                )
                                                                            }
                                        */
                                    }
                            }
                        }
                    }
                }
            }
        }
    }
}
