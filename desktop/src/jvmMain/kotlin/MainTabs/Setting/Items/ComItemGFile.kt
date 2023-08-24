package uiItems

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.material.*
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeDownChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.MyTextButtStyle1
import extensions.format
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.myGoogleLib.ItemGDriveFile
import java.awt.SystemColor.text
import java.awt.event.MouseEvent
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ComItemGFile(
    item: ItemGDriveFile,
    dropMenu: @Composable ColumnScope.(ItemGDriveFile, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    var expanded = mutableStateOf(false)
    var lastEvent = mutableStateOf<MouseEvent?>(null)
    Card(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp).fillMaxWidth()
//            .mouseClickable {
//                println(this.buttons.isSecondaryPressed)
//                expanded.value = this.buttons.isSecondaryPressed
//            }
//            .pointerInput(Unit) {
//                forEachGesture {
//                    awaitPointerEventScope {
//                        lastEvent.value = awaitEventFirstDown().also { point ->
//                            point.changes.forEach { it.consumeDownChange() }
//                        }.mouseEvent
//                        println(lastEvent.value)
//                    }
//                }
//            }
            .border(
                width = 0.5.dp,
                brush = Brush.horizontalGradient(listOf(Color(0x7FFFF7D9), Color(0x7FFFF7D9))),
                shape = RoundedCornerShape(15.dp)
            ),
        elevation = 8.dp,
        backgroundColor = Color(0xFF464D45),
        shape = RoundedCornerShape(corner = CornerSize(15.dp))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MyTextButtStyle1("ᐁ", modifier = Modifier.padding(start = 10.dp).width(50.dp).height(50.dp), 20.dp) {
                expanded.value = true
            }
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                modifier = Modifier.background(Color(0xFF4F534F)).heightIn(0.dp, 400.dp)
                    .wrapContentHeight()//.fillMaxHeight(0.5F)
                    .graphicsLayer {
//            shadowElevation = 8.dp.toPx()
                        shape = RoundedCornerShape(10.dp)
                        clip = true
                    }
            ) {

                dropMenu(item, expanded)
            }
            Column(modifier = Modifier.padding(5.dp).padding(end = 10.dp).weight(1f)) {
                Row {

                    Column(Modifier.padding(0.dp).weight(1f)) {
                        Text(
                            modifier = Modifier.padding(0.dp),
                            text = item.kind,
                            style = TextStyle(color = Color(0xFFFFF7D9)),
                            fontSize = 15.sp
                        )
                        Text(
                            modifier = Modifier.padding(0.dp),
                            text = item.name,
                            style = TextStyle(color = Color(0xFFFFF7D9)),
                            fontSize = 15.sp
                        )
                    }
//                Spacer(modifier = Modifier.weight(1f))// .fillMaxWidth())
                    Text(
                        modifier = Modifier.padding(start = 10.dp).align(Alignment.CenterVertically),
                        text = "-",
                        style = TextStyle(color = Color(0xFFFFF7F9)),
                        fontSize = 25.sp
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {

                    Text(
                        modifier = Modifier.padding(0.dp),
                        text = item.mimeType,
                        style = TextStyle(color = Color(0xFFFFF7D9)),
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))// .fillMaxWidth())
                    Text(
                        modifier = Modifier.padding(0.dp),
                        text = item.id,
                        style = TextStyle(color = Color(0xFFFFF7D9)),
                        fontSize = 15.sp
                    )
                }
            }
        }
    }

}


@Preview
@Composable
fun PrevComItemRasxod() {
    ComItemGFile(ItemGDriveFile("Gfile", "11", "MainBD", "text|plain"))
}