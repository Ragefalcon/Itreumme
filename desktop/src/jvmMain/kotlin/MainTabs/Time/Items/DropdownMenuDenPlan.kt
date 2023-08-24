package MainTabs.Time.Items

import MainTabs.Time.Elements.PanAddDenPlan
import MyDialog.MyDialogLayout
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import common.MyDeleteDropdownMenuButton
import common.MyDropdownMenuItem
import extensions.ItemDenPlanStyleState
import extensions.delAllImageForItem
import ru.ragefalcon.sharedcode.models.data.ItemDenPlan
import viewmodel.MainDB

@Composable
fun DropdownMenuDenPlan(
    item: ItemDenPlan,
    expanded: MutableState<Boolean>,
    dialLay: MyDialogLayout,
    style: ItemDenPlanStyleState,
    calendar: Boolean = false
) {
    Text(
        text = item.name,
        style = style.mainTextStyle
    )
    MyDropdownMenuItem(expanded, style.dropdown, "Изменить") {
        PanAddDenPlan(dialLay, item, calendar = calendar)
    }
    MyDropdownMenuItem(expanded, style.dropdown, "Повторить") {
        PanAddDenPlan(
            dialLay,
            item.copy(
                id = (item.id.toLong() * (-1)).toString(),
                data = MainDB.denPlanDate.value.time
            ),
            calendar = calendar
        )
    }
    MyDeleteDropdownMenuButton(expanded) {
        MainDB.addTime.delDenPlan(item.id.toLong()) {
            MainDB.complexOpisSpis.spisComplexOpisForDenPlan.delAllImageForItem(it)
        }
    }
}
