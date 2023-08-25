package MainTabs.Quest.Items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.mouseClickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.MyDeleteDropdownMenuButton
import common.MyDropdownMenuStyle1
import common.MyTextStyleParam
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemTrigger
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStartObjOfTrigger
import viewmodel.QuestVM


@OptIn(
    androidx.compose.ui.ExperimentalComposeUiApi::class,
    androidx.compose.foundation.ExperimentalFoundationApi::class
)
@Composable
fun ComItemTrigger(item: ItemTrigger, modifier: Modifier = Modifier, editable: Boolean = true) {
    var activeElev by remember { mutableStateOf(false) }
    val expandedDropMenu = remember { mutableStateOf(false) }

    Row(
        modifier
            .background(MyColorARGB.colorEffektShkal_Nedel.toColor().copy(alpha = 0.3f), RoundedCornerShape(5.dp))
            .border(
                width = 0.5.dp,
                brush = if (activeElev) Brush.horizontalGradient(
                    listOf(Color(0x9FFFF7D9), Color(0x9FFFF7D9))
                ) else Brush.horizontalGradient(
                    listOf(Color(0x4FFFF7D9), Color(0x4FFFF7D9))
                ),
                shape = RoundedCornerShape(5.dp)
            )
            .pointerMoveFilter(
                onEnter = {
                    activeElev = true
                    false
                },
                onExit = {
                    activeElev = false
                    false
                }
            ).mouseClickable {
                expandedDropMenu.value = true
            }
    ) {
        TypeStartObjOfTrigger.values().toList().find { it.id == item.type_id }?.let {
            Text(
                when (it) {
                    TypeStartObjOfTrigger.STARTPLAN -> "СП:"
                    TypeStartObjOfTrigger.STARTDIALOG -> "СД:"
                    TypeStartObjOfTrigger.SUMTRIGGER -> "СумТ:"
                    TypeStartObjOfTrigger.STARTSTAP -> "СЭ:"
                    TypeStartObjOfTrigger.INNERFINISH -> "InF:"
                    TypeStartObjOfTrigger.STARTTREE -> "СА:"
                    TypeStartObjOfTrigger.STARTNODETREE -> "СУА:"
                    TypeStartObjOfTrigger.STARTLEVELTREE -> "СЛ:"
                },
                Modifier.padding(start = 8.dp).padding(vertical = 3.dp, horizontal = 2.dp),
                style = MyTextStyleParam.style2.copy(fontSize = 12.sp)
            )
        }
        Text(
            item.child_name,
            Modifier.padding(horizontal = 2.dp, vertical = 3.dp).padding(end = 8.dp),
            style = MyTextStyleParam.style5
        )
        if (editable) MyDropdownMenuStyle1(expandedDropMenu) { setDissFun ->
            MyDeleteDropdownMenuButton(expandedDropMenu) {
                QuestVM.openQuestDB?.let { questDB ->
                    questDB.addQuest.delTrigger(item.id.toLong())
                }
            }
        }
    }
}
