package MainTabs.Journal.Element

import MyDialog.MyDialogLayout
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import common.*
import extensions.addUpdList
import ru.ragefalcon.sharedcode.models.data.ItemIdea
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TableNameForComplexOpis
import viewmodel.MainDB
import viewmodel.StateVM
import java.util.*

fun PanAddIdea(
    dialPan: MyDialogLayout,
    item: ItemIdea? = null
) {
    val boxSelectParent = BoxSelectParentIdea(
        arrayIskl = item?.let { listOf(it.id.toLong()) } ?: listOf<Long>(),
        itemBloknotParent = item?.let {
            MainDB.journalSpis.spisBloknot.getState().value?.find {
                it.id.toLong() == item.bloknot
            }
        } ?: StateVM.selectBloknot.value,
        itemIdeaParent = item?.let {
            MainDB.journalSpis.spisIdeaForSelect.getState().value?.find {
                it.id.toLong() == item.parent_id
            }
        }
    )

    val dialLayInner = MyDialogLayout()
    val stat = MySelectStat(item?.stat ?: 0L)

    val text_name = mutableStateOf(TextFieldValue(item?.let { it.name } ?: ""))
    val complexOpis =
        MyComplexOpisWithNameBox(
            "Название раздела",
            text_name,
            "Описание раздела",
            item?.id?.toLong() ?: -1,
            TableNameForComplexOpis.spisIdea,
            MainDB.styleParam.journalParam.complexOpisForIdea,
            item?.let {
                MainDB.complexOpisSpis.spisComplexOpisForIdea.getState().value?.let { itMap ->
                    itMap.get(it.id.toLong())
                        ?.toMutableStateList()
                }
            },
        )

    dialPan.dial = @Composable {
        BackgroungPanelStyle1 {
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
                                            MainDB.addJournal.updIdea(
                                                id = it.id.toLong(),
                                                name = text_name.value.text,
                                                opis = opis,
                                                data = it.data,
                                                stat = stat.value,
                                                parent_id = boxSelectParent.selectionIdeaParent.selected?.id?.toLong()
                                                    ?: -1L,
                                                bloknot = boxSelectParent.selectionBloknotParent.selected?.id?.toLong()
                                                    ?: 0L,
                                            )
                                        } ?: run {
                                            MainDB.addJournal.addIdea(
                                                name = text_name.value.text,
                                                opis = opis,
                                                data = Date().time,
                                                stat = stat.value,
                                                parent_id = boxSelectParent.selectionIdeaParent.selected?.id?.toLong()
                                                    ?: -1L,
                                                bloknot = boxSelectParent.selectionBloknotParent.selected?.id?.toLong()
                                                    ?: 0L,
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

