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
            MyList(
                MainDB.finSpis.spisTyperasxodForPlan,
                Modifier.weight(1f).padding(bottom = 10.dp)
            ) { _, itemSettSchet ->
                ComItemTypeRasxForPlans(itemSettSchet).getComposable()
            }
        }
    }
}
