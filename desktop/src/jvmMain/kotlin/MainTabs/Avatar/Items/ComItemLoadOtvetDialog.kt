package MainTabs.Avatar.Items

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material.Text
import common.MyTextStyleParam
import extensions.RowVA
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemOtvetDialogQuest
import viewmodel.MainDB


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ComItemLoadOtvetDialog(item: ItemOtvetDialogQuest) {
        var activeElev by remember { mutableStateOf(0.5F) }
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
            )
            .clickable {
                MainDB.addQuest.completeDialogEvent(item)
            }
        ) {
            Column(
                Modifier.weight(1f)

            ) {
                Text(
                    item.text,
                    Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    style = MyTextStyleParam.style5.copy(fontSize = 17.sp, fontWeight = FontWeight.Normal)
                    )
//                RowVA {
//                    questDB.spisTrigger.getState().value?.let { listTrig ->
//                        listTrig.filter { it.parent_type == TypeParentTrig.OTVDIALOG.code && it.parent_id == item.id.toLong() }
//                            .let {
//                                it.forEach { trig ->
//                                    ComItemTrigger(trig, Modifier.padding(start = 5.dp, bottom = 5.dp))
//                                }
//                            }
//                    }
//                }
            }
//            val expandedDropMenuOtvet = mutableStateOf(false)
//            MyButtDropdownMenuStyle2(
//                Modifier.padding(end = 3.dp).padding(vertical = 0.dp),
//                expandedDropMenuOtvet
//            ) {
//                MyDropdownMenuItem(expandedDropMenuOtvet, "+ Триггер") {
//                    PanAddTrigger(dialPan, item.parentTrig())
//                }
//                MyDeleteDropdownMenuButton(expandedDropMenuOtvet) {
//                    questDB.addQuest.delOtvetDialog(item.id.toLong())
//                }
//            }
        }
}