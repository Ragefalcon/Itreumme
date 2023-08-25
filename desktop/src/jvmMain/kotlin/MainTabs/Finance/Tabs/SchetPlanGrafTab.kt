package MainTabs.Finance.Tabs


import MainTabs.Finance.Items.ComItemSchetPlanWithSum
import MyDialog.MyDialogLayout
import MyList
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import extensions.ItemSchetPlanGrafState
import extensions.getComposable
import viewmodel.MainDB

class SchetPlanGrafTab(val dialLay: MyDialogLayout) {
    @Composable
    fun show(modifier: Modifier = Modifier) {
        Column(
            modifier.padding(vertical = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            (MainDB.finSpis.spisBindForSchetPlanWithName.getState().value ?: listOf()).let { listBind ->
                MainDB.styleParam.finParam.schetParam.itemSchetPlanGraf.getComposable(::ItemSchetPlanGrafState) { itemStyle ->
                    MyList(MainDB.finSpis.spisSchetPlanWithSumm, Modifier.weight(1f)) { ind, item ->
                        ComItemSchetPlanWithSum(
                            item,
                            listBind.filter { it.schet_plan_id == item.id },
                            itemStyle
                        ) { itemSum ->
                            MainDB.finSpis.spisSchetPlan.getState().value?.find { it.id == itemSum.id.toString() }
                                ?.let {
                                    MainDB.CB_spisSchetPlan.select(it)
                                }
                        }
                    }
                }
            }
        }
    }
}