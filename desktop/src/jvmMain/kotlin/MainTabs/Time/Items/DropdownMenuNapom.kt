package MainTabs.Time.Items

import MainTabs.Time.Elements.PanAddNapom
import MyDialog.MyDialogLayout
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.MyDeleteDropdownMenuButton
import common.MyDropdownMenuItem
import extensions.ItemNapomStyleState
import extensions.delAllImageForItem
import ru.ragefalcon.sharedcode.models.data.ItemNapom
import viewmodel.MainDB

@Composable
fun DropdownMenuNapom(
    item: ItemNapom,
    expanded: MutableState<Boolean>,
    dialLay: MyDialogLayout,
    calendar: Boolean = false
) {
    ItemNapomStyleState(MainDB.styleParam.timeParam.denPlanTab.itemNapom).let { itemNapomStyle ->
        Text(
            modifier = Modifier.padding(0.dp),
            text = item.name,
            style = itemNapomStyle.mainTextStyle
        )
        MyDropdownMenuItem(expanded, itemNapomStyle.dropdown, "Изменить") {
            PanAddNapom(dialLay, item, calendar)
        }
        MyDeleteDropdownMenuButton(expanded) {
            MainDB.addTime.delNapom(item.id.toLong()) {
                MainDB.complexOpisSpis.spisComplexOpisForNapom.delAllImageForItem(it)
            }
        }
    }
}