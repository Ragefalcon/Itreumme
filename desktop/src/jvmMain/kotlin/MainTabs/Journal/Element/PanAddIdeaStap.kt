package MainTabs.Journal.Element

import MyDialog.MyDialogLayout
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import common.*
import extensions.addUpdList
import ru.ragefalcon.sharedcode.models.data.ItemBloknot
import ru.ragefalcon.sharedcode.models.data.ItemComplexOpis
import ru.ragefalcon.sharedcode.models.data.ItemIdeaStap
import ru.ragefalcon.sharedcode.models.data.ItemVxod
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TableNameForComplexOpis
import viewmodel.MainDB
import viewmodel.StateVM
import java.util.*

fun PanAddIdeaStap(
    dialPan: MyDialogLayout,
    item: ItemIdeaStap? = null
) {
    val itemParentIdea = item?.let {
        MainDB.journalSpis.spisIdeaForSelect.getState().value?.find {
            it.id.toLong() == item.idea_id
        }
    } ?: StateVM.selectionIdea.selected
    val boxSelectParent = BoxSelectParentIdea(
        itemBloknotParent = itemParentIdea?.let { itemPIdea ->
            MainDB.journalSpis.spisBloknot.getState().value?.find {
                it.id.toLong() == itemPIdea.bloknot
            }
        } ?: StateVM.selectBloknot.value,
        itemIdeaParent = itemParentIdea,
        onlyIdea = true
    )

    val dialLayInner = MyDialogLayout()
    val stat = MySelectStat(item?.stat ?: 0L)

    val text_name = mutableStateOf(TextFieldValue(item?.let { it.name } ?: ""))
    val complexOpis =
        MyComplexOpisWithNameBox(
            "Название заметки",
            text_name,
            "Описание заметки",
            item?.id?.toLong() ?: -1,
            TableNameForComplexOpis.spisIdeaStap,
            MainDB.styleParam.journalParam.complexOpisForIdeaStap,
            item?.let {
                MainDB.complexOpisSpis.spisComplexOpisForIdeaStap.getState().value?.get(it.id.toLong())
                    ?.toMutableStateList()
            },
        )

    dialPan.dial = @Composable {
        BackgroungPanelStyle1 { //modif ->
            Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                boxSelectParent.getComposable(Modifier.fillMaxWidth(0.7F))
                if (!boxSelectParent.isExpanded()) {
                    complexOpis.show(this, dialLayInner)
                    stat.show()
                    Row {
                        MyTextButtStyle1("Отмена") {
                            dialPan.close()
                        }
                        MyTextButtStyle1(item?.let { "Изменить" } ?: "Добавить", Modifier.padding(start = 5.dp)) {
                            if (text_name.value.text != "")
                                boxSelectParent.selectionBloknotParent.selected?.id?.let {
                                    complexOpis.listOpis.addUpdList { opis ->
                                        item?.let {
                                            MainDB.addJournal.updStapIdea(
                                                id = it.id.toLong(),
                                                name = text_name.value.text,
                                                opis = opis,
                                                data = it.data,
                                                stat = stat.value,
                                                idea_id = boxSelectParent.selectionIdeaParent.selected?.id?.toLong()
                                                    ?: -1L,
                                            )
                                        } ?: run {
                                            MainDB.addJournal.addStapIdea(
                                                name = text_name.value.text,
                                                opis = opis,
                                                data = Date().time,
                                                stat = stat.value,
                                                idea_id = boxSelectParent.selectionIdeaParent.selected?.id?.toLong()
                                                    ?: -1L,
                                            )
                                        }
                                    }
                                    dialPan.close()
                                }
                        }
                    }
                }
            }
        }
        dialLayInner.getLay()
    }

    dialPan.show()
}

fun PanAddIdeaStapFromVxod(
    dialPan: MyDialogLayout,
    itemVxod: ItemVxod,
    itemBloknot: ItemBloknot,
    cancelListener: () -> Unit = {},
    listOp: List<ItemComplexOpis>? = null,
    finishListener: () -> Unit = {}
) {
    val boxSelectParent = BoxSelectParentIdea(
        itemBloknotParent = itemBloknot,
    )
    val dialLayInner = MyDialogLayout()
    val stat = MySelectStat(itemVxod.stat)

    boxSelectParent.expandSpisIdea()

    val text_name = mutableStateOf(TextFieldValue(itemVxod.name))
    val complexOpis =
        MyComplexOpisWithNameBox(
            "Название заметки",
            text_name,
            "Описание заметки",
            -1,
            TableNameForComplexOpis.spisIdeaStap,
            MainDB.styleParam.journalParam.complexOpisForIdeaStap,
            listOp?.toMutableStateList() ?: mutableStateListOf(),
        )

    dialPan.dial = @Composable {
        BackgroungPanelStyle1 { //modif ->
            Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
//                Row(verticalAlignment = Alignment.CenterVertically){
                boxSelectParent.getComposable(Modifier.fillMaxWidth(0.7F))
                if (!boxSelectParent.isExpanded()) {
                    complexOpis.show(this, dialLayInner)
                    stat.show()
                    Row {
                        MyTextButtStyle1("Отмена") {
                            cancelListener()
                            dialPan.close()
                        }
                        MyTextButtStyle1("Добавить", Modifier.padding(start = 5.dp)) {
                            if (text_name.value.text != "")
                                boxSelectParent.selectionBloknotParent.selected?.id?.let {
                                    complexOpis.listOpis.addUpdList { opis ->
                                        MainDB.addJournal.addStapIdea(
                                            name = text_name.value.text,
                                            opis = opis,
                                            data = Date().time,
                                            stat = stat.value,
                                            idea_id = boxSelectParent.selectionIdeaParent.selected?.id?.toLong()
                                                ?: -1L,
                                        )
                                    }
                                    finishListener()
                                    dialPan.close()
                                }
                        }
                    }
                }
            }
        }
        dialLayInner.getLay()
    }
    dialPan.show()
}


