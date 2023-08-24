package MainTabs.Time.Tabs

import MainTabs.Time.Elements.PanAddVxod
import MyDialog.MyDialogLayout
import MyList
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import MainTabs.Time.Items.ComItemVxod
import MyDialog.InnerFinishVxodAction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.text.style.TextAlign
import common.*
import extensions.ItemVxodStyleState
import extensions.TextButtonStyleState
import extensions.delAllImageForItem
import extensions.getComposable
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.InnerDialogs.InnerStartTriggerEnum
import viewmodel.MainDB
import viewmodel.StateVM


class VxodTab(val dialLay: MyDialogLayout) {

    private val selection = SingleSelection()
    val listState: LazyListState = LazyListState(0)

    @Composable
    fun show() {
//        val listState: LazyListState = rememberLazyListState(0)
        Column(Modifier.padding(top = 8.dp),horizontalAlignment = Alignment.CenterHorizontally) {
            MainDB.styleParam.timeParam.vxodTab.itemVxod.getComposable(::ItemVxodStyleState){ vxodStyle ->
                MyList(MainDB.timeSpis.spisVxod, Modifier.padding(top = 5.dp).weight(1f),listState) { ind, itemVxod -> //,listState
                    ComItemVxod(itemVxod, selection, vxodStyle,dialLay) { item, expanded ->
                        MyDropdownMenuItem(expanded,"Разобрать"){
                            StateVM.innerFinishAction.value = InnerFinishVxodAction(item)
                            MainDB.addTime.startInnerTrigger(InnerStartTriggerEnum.VxodKonvert)
                        }
                        MyDropdownMenuItem(expanded,"Изменить") {
                            PanAddVxod(dialLay, item)
                        }
                        MyDeleteDropdownMenuButton(expanded){
                            MainDB.addTime.delVxod(item.id.toLong()){
                                MainDB.complexOpisSpis.spisComplexOpisForVxod.delAllImageForItem(it)
                            }
                        }
                    }.getComposable()
                }
            }
            Row(modifier = Modifier.padding(bottom = 5.dp,top = 15.dp).padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Количесто входящих: ${MainDB.timeSpis.spisVxod.getState().value?.size ?: 0}",
                    Modifier.weight(1f).padding(horizontal = 10.dp),
                    style = MainDB.styleParam.timeParam.vxodTab.textCountVxod.getValue()
                )
                MyTextButtStyle1("+", modifier = Modifier.padding(start = 15.dp),width = 60.dp, height = 60.dp, myStyleTextButton = TextButtonStyleState(MainDB.styleParam.timeParam.vxodTab.buttAddVxod)) {
                    PanAddVxod(dialLay)
                }
            }
        }
    }
}
