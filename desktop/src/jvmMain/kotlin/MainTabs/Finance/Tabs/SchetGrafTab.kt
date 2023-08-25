package MainTabs.Finance.Tabs

import MainTabs.Finance.Items.ComItemSumOnSchet
import MyDialog.MyDialogLayout
import MyList
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import extensions.ItemSchetGrafState
import extensions.getComposable
import viewmodel.MainDB

class SchetGrafTab(val dialLay: MyDialogLayout) {


    @Composable
    fun show(modifier: Modifier = Modifier) {
        Column(
            modifier.padding(vertical = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainDB.styleParam.finParam.schetParam.itemSchetGraf.getComposable(::ItemSchetGrafState) { itemStyle ->
                MyList(MainDB.finSpis.spisSchetWithSumm, Modifier.weight(1f)) { ind, item ->
                    ComItemSumOnSchet(item, itemStyle) { itemSum ->
                        MainDB.finSpis.spisSchet.getState().value?.find { it.id == itemSum.id.toString() }?.let {
                            MainDB.CB_spisSchet.select(it)
                        }
                    }
                }
            }
        }
    }
}