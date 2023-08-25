package MainTabs.Journal.Element

import MainTabs.Journal.Items.ComItemBloknot
import MainTabs.Journal.Items.ComItemIdea
import MainTabs.Journal.Items.ItemBloknotPlate
import MainTabs.Journal.Items.ItemIdeaPlate
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
import extensions.ItemBloknotStyleState
import extensions.ItemIdeaStyleState
import extensions.getComposable
import ru.ragefalcon.sharedcode.models.data.ItemBloknot
import ru.ragefalcon.sharedcode.models.data.ItemIdea
import viewmodel.MainDB
import viewmodel.StateVM

class BoxSelectParentIdea(
    private val emptyBloknot: Boolean = false,
    private var arrayIskl: List<Long> = listOf(),
    itemBloknotParent: ItemBloknot? = null,
    itemIdeaParent: ItemIdea? = null,
    private val onlyIdea: Boolean = false
) {
    val selectionBloknotParent = SingleSelectionType<ItemBloknot>().apply { selected = itemBloknotParent }
    val selectionIdeaParent = SingleSelectionType<ItemIdea>().apply { selected = itemIdeaParent }
    private val expandedSelBloknot = mutableStateOf(false)
    private val expandedSelIdea = mutableStateOf(false)

    fun isExpanded() = expandedSelBloknot.value || expandedSelIdea.value

    fun expandSpisIdea() {
        selectionBloknotParent.selected?.let {
            expandedSelIdea.value = true
        }
    }

    @Composable
    fun getComposable(modifier: Modifier = Modifier) {
        selectionBloknotParent.launchedEffect {
            it?.let {
                MainDB.journalFun.setBloknotForSpisIdeaForSelect(
                    it.id.toLong(),
                    arrayIskl,
                    sortField = StateVM.filterIdea.value
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
            if (!expandedSelBloknot.value) {
                selectionBloknotParent.selected?.let {
                    if (!onlyIdea) ItemBloknotPlate(
                        it,
                    ) {
                        expandedSelBloknot.value = true
                    }.getComposable()
                    if (!expandedSelIdea.value) {
                        if (selectionIdeaParent.selected == null) MyTextButtStyle1("Выбрать раздел") {
                            expandedSelIdea.value = true
                        } else {
                            selectionIdeaParent.selected?.let {
                                ItemIdeaPlate(
                                    it, itemIdeaStyleState = ItemIdeaStyleState(MainDB.styleParam.journalParam.itemIdea)

                                ) {
                                    expandedSelIdea.value = true
                                }
                            }
                        }
                    }
                    if (expandedSelIdea.value) {
                        MainDB.styleParam.journalParam.itemIdea.getComposable(::ItemIdeaStyleState) { itemStyle ->
                            MyList(
                                MainDB.journalSpis.spisIdeaForSelect,
                                Modifier.weight(1f).padding(bottom = 10.dp).padding(horizontal = 20.dp),
                                darkScroll = true
                            ) { ind, itemIdea ->
                                ComItemIdea(
                                    itemIdea, selectionIdeaParent,
                                    selFun = {
                                        expandedSelIdea.value = false
                                    }, editable = false,
                                    itemIdeaStyleState = itemStyle
                                )
                            }
                        }
                        MyTextButtStyle1("Отменить выбор") {
                            if (!onlyIdea) selectionIdeaParent.selected = null
                            expandedSelIdea.value = false
                        }
                    }
                }
                if (selectionBloknotParent.selected == null) MyTextButtStyle1("Выбрать блокнот") {
                    expandedSelBloknot.value = true
                }
            } else {
                MainDB.styleParam.journalParam.itemBloknot.getComposable(::ItemBloknotStyleState) { itemBloknotStyle ->
                    MyList(
                        MainDB.journalSpis.spisBloknot,
                        Modifier.weight(1f).padding(bottom = 10.dp),
                        darkScroll = true
                    ) { ind, itemBloknot ->
                        ComItemBloknot(itemBloknot, selectionBloknotParent, openBloknot = {
                            selectionIdeaParent.selected = null
                            expandedSelBloknot.value = false
                        }, edit = false, itemBloknotStyleState = itemBloknotStyle)
                    }
                }
                MyTextButtStyle1("Отменить выбор") {
                    if (emptyBloknot) selectionBloknotParent.selected = null
                    expandedSelBloknot.value = false
                }
            }
        }

    }

}