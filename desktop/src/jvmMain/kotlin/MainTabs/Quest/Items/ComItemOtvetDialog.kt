package MainTabs.Quest.Items

import MainTabs.Quest.Element.PanAddTrigger
import MyDialog.MyDialogLayout
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.BoxWithMyRightClick
import extensions.RowVA
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemOtvetDialog
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeParentOfTrig
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.parentOfTrig
import viewmodel.QuestVM

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ComItemOtvetDialog(item: ItemOtvetDialog, dialPan: MyDialogLayout) {

    @Composable
    fun ColumnScope.dropMenu(expandedDropMenuOtvet: MutableState<Boolean>){
        MyDropdownMenuItem(expandedDropMenuOtvet, "+ Триггер") {
            PanAddTrigger(dialPan, item.parentOfTrig())
        }
        MyDeleteDropdownMenuButton(expandedDropMenuOtvet) {
            QuestVM.openQuestDB?.addQuest?.delOtvetDialog(item.id.toLong())
        }
    }

    QuestVM.openQuestDB?.let { questDB ->
        var activeElev by remember { mutableStateOf(0.5F) }
//        BoxWithRightClickContextMenu(dropMenu = { expandedDropMenuOtvet -> dropMenu(expandedDropMenuOtvet) }) {
        BoxWithMyRightClick (dropMenu = { expandedDropMenuOtvet -> dropMenu(expandedDropMenuOtvet) }) {
            RowVA(Modifier.fillMaxWidth().padding(2.dp).animateContentSize()
                .pointerMoveFilter(
                    onEnter = {
                        activeElev = 1F
                        false
                    },
                    onExit = {
                        activeElev = 0.5F
                        false
                    }
                )
                .border(
                    1.dp,
                    Brush.horizontalGradient(
                        listOf(
                            Color(0x9FFFF7D9).copy(alpha = 0.6f * activeElev),
                            Color(0x6FFFF7D9).copy(alpha = 0.3f * activeElev),
                            Color(0x4FFFF7D9).copy(alpha = 0.2f * activeElev),
                            Color(0x2FFFF7D9).copy(alpha = 0.1f * activeElev),
                            Color(0x4FFFF7D9).copy(alpha = 0.2f * activeElev),
                            Color(0x6FFFF7D9).copy(alpha = 0.3f * activeElev),
                            Color(0x9FFFF7D9).copy(alpha = 0.6f * activeElev)
                        )
                    ),
                    RoundedCornerShape(8.dp)
                )) {
                Column(
                    Modifier.weight(1f)

                ) {
                    MyTextToggleEdit3(

                        item.text,
                        Modifier.padding(horizontal = 5.dp),
                        paramOutline = MyTextStyleParam.style5.copy(fontSize = 17.sp, fontWeight = FontWeight.Normal)
                    ) { newText ->
                        questDB.addQuest.updOtvetDialog(item.id.toLong(), newText)
                    }
//                    RowVA {
                    PlateOrderLayout(){
                        QuestVM.getComItemTriggers(TypeParentOfTrig.OTVDIALOG.code, item.id.toLong(), Modifier.padding(start = 5.dp, bottom = 5.dp),true)
                    }
                }
                val expandedDropMenuOtvet = remember { mutableStateOf(false) }
                MyButtDropdownMenuStyle2(
                    Modifier.padding(end = 3.dp).padding(vertical = 0.dp),
                    expandedDropMenuOtvet,
                ) {
                    dropMenu(expandedDropMenuOtvet)
//                    MyDropdownMenuItem(expandedDropMenuOtvet, "+ Триггер") {
//                        PanAddTrigger(dialPan, item.parentTrig())
//                    }
//                    MyDeleteDropdownMenuButton(expandedDropMenuOtvet) {
//                        questDB.addQuest.delOtvetDialog(item.id.toLong())
//                    }
                }
            }
        }
    }
}