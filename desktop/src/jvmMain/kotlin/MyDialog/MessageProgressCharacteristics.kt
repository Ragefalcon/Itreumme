package MyDialog

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.BackgroungPanelStyle1
import common.MyTextButtStyle1
import common.MyTextStyle1
import ru.ragefalcon.sharedcode.models.data.ItemCharacteristic
import viewmodel.MainDB


@Composable
fun MessageProgressCharacteristics() {
    val keyDial = remember { mutableStateOf(false) }
    val dialLay = remember { MyDialogLayout() }

    LaunchedEffect(MainDB.avatarSpis.spisProgressCharacteristicForMessage.getState().value){
        if (MainDB.avatarSpis.spisProgressCharacteristicForMessage.getState().value != null) keyDial.value = true
    }
    val alpha: Float by animateFloatAsState(
        targetValue = if (MainDB.avatarSpis.spisProgressCharacteristicForMessage.getState().value != null) 1f else 0.0f,
        animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing)
    ) {
        if (MainDB.avatarSpis.spisProgressCharacteristicForMessage.getState().value == null) keyDial.value = false
    }
    BoxWithConstraints(Modifier.fillMaxSize().alpha(alpha), Alignment.Center) {
        if (keyDial.value)
            MainDB.avatarSpis.spisProgressCharacteristicForMessage.getState().value?.let {
                tmp(it.first, it.second)
            }
    }
    dialLay.getLay()
}

@Composable
fun tmp(item: ItemCharacteristic, delta: Long) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0x88000000)
    ) {
        BackgroungPanelStyle1 {
            Column(
                Modifier.padding(15.dp).fillMaxWidth(0.6F),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MyTextStyle1("${if (delta == 1L || delta == 2L) "\uD83C\uDF89    " else ""}${item.name}    ${if (delta > 0) "+ $delta" else delta} (${item.stat})${if (delta == 1L || delta == 2L) "    \uD83C\uDF89" else ""}")
                MyTextButtStyle1("Хорошо", Modifier.padding(start = 5.dp)) {
                    MainDB.avatarFun.removeFromProgressCharacteristicsMessage(item to delta)
                }
            }
        }
    }

}