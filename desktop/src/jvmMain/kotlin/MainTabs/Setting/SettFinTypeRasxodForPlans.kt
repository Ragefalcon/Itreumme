package MainTabs.Setting


import MainTabs.Setting.Items.ComItemTypeRasxForPlans
import MyDialog.MyDialogLayout
import MyList
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.SingleSelectionType
import ru.ragefalcon.sharedcode.models.data.ItemSettSchetPlan
import viewmodel.MainDB

class SettFinTypeRasxodForPlans(val dialLay: MyDialogLayout) {

    private val selection = SingleSelectionType<ItemSettSchetPlan>()

    val visClose = mutableStateOf(false)

    @Composable
    fun show(modifier: Modifier = Modifier) {
        Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
/*
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
*/
            MyList(
                MainDB.finSpis.spisTyperasxodForPlan,
                Modifier.weight(1f).padding(bottom = 10.dp)
            ) { ind, itemSettSchet ->
                ComItemTypeRasxForPlans(itemSettSchet).getComposable()
            }
        }
    }
}
