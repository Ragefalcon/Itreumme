package MainTabs.Journal

import MainTabs.Journal.Element.PanAddIdea
import MainTabs.Journal.Items.ComItemIdea
import MyDialog.MyDialogLayout
import MyList
import MyShowMessage
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.source.disk.getValue
import viewmodel.MainDB
import viewmodel.StateVM

class IdeaPanel(val listStateIdea: MyLazyListState) {


    private val ideaStapPanel = IdeaStapPanel()
    private var startValueProg = 0.35f
    val progressGotov = mutableStateOf(startValueProg)
    val searchOpen = mutableStateOf(false)
    val text_search = mutableStateOf(TextFieldValue(""))
    var keyScroll = false


    @Composable
    fun show(dialLay: MyDialogLayout) {
        listStateIdea.launchEffect()

        val panelState = remember { PanelState().apply { expandedSize = 350.dp } }
        val animatedSize =
            if (panelState.splitter.isResizeEnabled) {
                if (panelState.isExpanded) panelState.expandedSize else panelState.collapsedSize
            } else {
                animateDpAsState(
                    if (panelState.isExpanded) panelState.expandedSize else panelState.collapsedSize,
                    SpringSpec(stiffness = Spring.StiffnessLow)
                ).value
            }

        Column {
            VerticalSplittable(
                Modifier.fillMaxSize(),
                panelState.splitter,
                onResize = {
                    panelState.expandedSize =
                        (panelState.expandedSize + it).coerceAtLeast(panelState.expandedSizeMin)
                },
                color = Color.Transparent,
                splitStyle = {
                    Box(
                        Modifier.padding(vertical = 50.dp).fillMaxHeight().width(2.dp).background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    MyColorARGB.colorMyBorderStroke.toColor(),
                                    Color.Transparent
                                )
                            )
                        )
                    )
                }
            ) {
                ResizablePanel(Modifier.width(animatedSize).fillMaxHeight(), panelState) {
                    Column(
                        Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if ((MainDB.journalSpis.spisIdea.getState().value?.size ?: 0) > 0) {
                            MainDB.styleParam.journalParam.itemIdea.getComposable(::ItemIdeaStyleState) { itemStyle ->
                                MyList(
                                    MainDB.journalSpis.spisIdea,
                                    Modifier.padding(start = 10.dp, bottom = 10.dp).weight(1f),
                                    listStateIdea.listState
                                ) { ind, itemIdea ->
                                    ComItemIdea(itemIdea, StateVM.selectionIdea, selFun = {
                                        MainDB.journalFun.setIdeaForSpisStapIdea(
                                            it.id.toLong(),
                                            searchStr = if (searchOpen.value) text_search.value.text else "",
                                            sortField = StateVM.filterIdeaStap.value
                                        )
                                    }, itemIdeaStyleState = itemStyle) { item, expanded ->
                                        DropdownMenuItem(onClick = {
                                            PanAddIdea(dialLay, item)
                                            expanded.value = false
                                        }) {
                                            Text(
                                                text = "Изменить",
                                                color = Color.White
                                            )
                                        }
                                        DropdownMenuItem(onClick = {
                                            if (item.podstapcount == 0L) {
                                                MainDB.addJournal.delIdea(item.id.toLong()) {
                                                    MainDB.complexOpisSpis.spisComplexOpisForIdea.delAllImageForItem(it)
                                                }
                                            } else {
                                                MyShowMessage(dialLay, "Удалите вначале все записи из раздела")
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
                        } else {
                            Spacer(Modifier.weight(1f))
                        }
                        Box(Modifier.wrapContentSize().animateContentSize()) {
                            if (searchOpen.value) MyOutlinedTextField(
                                "Искать",
                                text_search,
                                modifier = Modifier.padding(bottom = 10.dp)
                            ) {
                                StateVM.selectionIdea.selected?.id?.toLong()?.let { id ->
                                    MainDB.journalFun.setIdeaForSpisStapIdea(
                                        id,
                                        searchStr = if (searchOpen.value) text_search.value.text else "",
                                        sortField = StateVM.filterIdeaStap.value
                                    )
                                }
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            MyToggleButtIconStyle1(
                                "ic_baseline_search_24.xml",
                                twoIcon = false,
                                value = searchOpen,
                                sizeIcon = 35.dp,
                                modifier = Modifier.padding(start = 15.dp),
                                width = 70.dp,
                                height = 35.dp,
                                myStyleToggleButton = ToggleButtonStyleState(MainDB.styleParam.journalParam.findIdeaStapButt)
                            ) {
                                StateVM.selectionIdea.selected?.id?.toLong()?.let { id ->
                                    MainDB.journalFun.setIdeaForSpisStapIdea(
                                        id,
                                        searchStr = if (searchOpen.value) text_search.value.text else "",
                                        sortField = StateVM.filterIdeaStap.value
                                    )
                                }
                            }
                            MyTextButtStyle1(
                                "+",
                                modifier = Modifier.padding(start = 15.dp),
                                width = 70.dp,
                                height = 35.dp,
                                myStyleTextButton = TextButtonStyleState(MainDB.styleParam.journalParam.addIdeaButt)
                            ) {
                                PanAddIdea(dialLay)
                            }
                            MyTextButtStyle1(
                                StateVM.filterIdea.value,
                                modifier = Modifier.padding(start = 15.dp),
                                height = 35.dp,
                                myStyleTextButton = TextButtonStyleState(MainDB.styleParam.journalParam.sortIdeaButt)
                            ) {
                                if (StateVM.filterIdea.value == "stat") {
                                    StateVM.filterIdea.value = "name"
                                } else {
                                    StateVM.filterIdea.value = "stat"
                                }
                                StateVM.selectBloknot.value?.id?.toLong()?.let {
                                    MainDB.journalFun.setBloknotForSpisIdea(it, StateVM.filterIdea.value)
                                }

                            }
                        }
                    }
                }
                MyShadowBox(MainDB.styleParam.journalParam.idea_stap_plate.shadow.getValue()) {
                    Column(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .withSimplePlate(SimplePlateWithShadowStyleState(MainDB.styleParam.journalParam.idea_stap_plate)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        StateVM.selectionIdea.selected?.let {
                            ideaStapPanel.show(dialLay, this)
                        }
                    }
                }
            }
        }
    }
}