package MainTabs.Setting


import MainTabs.Setting.Element.PanAddSchetPlan
import MainTabs.Setting.Items.ComItemSchetPlanSett
import MyDialog.MyDialogLayout
import MyList
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.*
import ru.ragefalcon.sharedcode.models.data.ItemSettSchetPlan
import viewmodel.MainDB

class SettFinSchetPlanTab(val dialLay: MyDialogLayout) {

    private val selection = SingleSelectionType<ItemSettSchetPlan>()

    val visClose = mutableStateOf(false)

    @Composable
    fun show(modifier: Modifier = Modifier) {
        Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.padding(bottom = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                MyTextToggleButtStyle1("Закрытые", visClose, modifier = Modifier.padding(start = 15.dp)) {
                    MainDB.finFun.setVisibleOpenSettSchetPlan(!it)
                    selection.selected?.let {
                        if (it.open_ != 1L) selection.selected = null
                    }
                }
                MyTextStyle1(
                    "Количесто: ${MainDB.finSpis.spisSchetPlanForSett.getState().value?.size ?: 0}",
                    Modifier.weight(1f).padding(horizontal = 10.dp)
                )
                MyTextButtStyle1("+", modifier = Modifier.padding(start = 15.dp)) {
                    PanAddSchetPlan(dialLay)
                }
            }
            (MainDB.finSpis.spisBindForSchetPlanWithName.getState().value ?: listOf()).let { listBind ->
                MainDB.finSpis.spisTyperasxodForPlan.getState().value?.let { typeList ->
                    MyList(
                        MainDB.finSpis.spisSchetPlanForSett,
                        Modifier.weight(1f).padding(bottom = 10.dp)
                    ) { _, itemSettSchet ->
                        ComItemSchetPlanSett(
                            itemSettSchet,
                            typeList.filter { it.schpl_id == itemSettSchet.id.toLong() }.map { it.typer },
                            selection,
                            listBind.filter { it.schet_plan_id == itemSettSchet.id.toLong() }
                        ) { item, expanded ->
                            if (item.id.toLong() != 1L) {
                                DropdownMenuItem(onClick = {
                                    PanAddSchetPlan(dialLay, item)
                                    expanded.value = false
                                }) {
                                    Text(text = "Изменить", color = Color.White)
                                }
                                DropdownMenuItem(onClick = {
                                    if (item.open_ == 1L) MainDB.addFinFun.closeSchetPlan(item.id.toLong())
                                    else MainDB.addFinFun.updSchetPlanOpen(item.id.toLong(), 1L)
                                    expanded.value = false
                                }) {
                                    Text(text = if (item.open_ == 1L) "Закрыть" else "Открыть", color = Color.White)
                                }
                                if (item.countoper == 0L) {
                                    MyDeleteDropdownMenuButton(expanded) {
                                        MainDB.addFinFun.delSchetPlan(item.id.toLong())
                                    }
                                }
                            }
                        }.getComposable()
                    }
                }
            }
        }
    }
}
