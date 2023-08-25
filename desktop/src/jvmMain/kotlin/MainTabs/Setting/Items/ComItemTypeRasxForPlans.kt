package MainTabs.Setting.Items


import adapters.MyComboBox
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.MyCardStyle1
import androidx.compose.material.Text
import common.MyTextStyleParam
import extensions.RowVA
import extensions.toColor
import extensions.toMyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemTyperasxod
import viewmodel.MainDB

class ComItemTypeRasxForPlans(
    val item: ItemTyperasxod,
) {

    val CB_SchetPlan = MyComboBox(
        MainDB.finSpis.spisSchetPlan, nameItem = { it.name }, openButtAtLeft = false, listenerInStart = false
    ) {
        if (item.schpl_id != it.id.toLong()) MainDB.addFinFun.updTyperasxodSchetPlan(
            item.id.toLong(), it.name, it.id.toLong()
        )
    }.apply {
        MainDB.finSpis.spisSchetPlan.getState().value?.find { it.id == item.schpl_id.toString() }?.let {
            select(it)
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun getComposable() {
        MyCardStyle1(
            false,
            0,
            {},
            backColor = if (!item.open) Color.Red.toMyColorARGB().plusWhite().plusWhite().toColor().copy(alpha = 0.7f)
            else Color(0xFF464D45)
        ) {
            RowVA(
                modifier = Modifier.padding(5.dp).padding(horizontal = 10.dp),
            ) {
                Text(
                    modifier = Modifier.weight(1f), text = item.typer, style = MyTextStyleParam.style2
                )
                CB_SchetPlan.show()
            }
        }
    }
}