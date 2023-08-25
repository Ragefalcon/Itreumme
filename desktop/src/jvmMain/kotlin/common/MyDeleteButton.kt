package common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun MyDeleteButton(
    modifier: Modifier = Modifier,
    width: Dp = 180.dp,
    finishListener: () -> Unit = {}
) {
    val progressGotov = remember { mutableStateOf(1.0f) }
    val animProg = remember { mutableStateOf(false) }
    val fin = remember { mutableStateOf(false) }

    LaunchedEffect(animProg.value) {
        while (animProg.value && progressGotov.value + 0.03 <= 1) {
            progressGotov.value = progressGotov.value + 0.03f
            delay(2)
        }
        if (animProg.value) progressGotov.value = 1f
    }
    Column(
        modifier
            .background(Color.Red.copy(alpha = 0.3f), RoundedCornerShape(5.dp))
            .border(1.dp, Color.Red, RoundedCornerShape(5.dp)).padding(5.dp)
    ) {
        Text(text = "Удалить", color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(start = 10.dp))
        Box {
            Slider(
                value = progressGotov.value,
                modifier = Modifier.height(30.dp).width(width).padding(start = 10.dp),
                onValueChange = {
                    animProg.value = false
                    progressGotov.value = it
                    if (progressGotov.value == 0f) {
                        if (!fin.value) {
                            fin.value = true
                            finishListener()
                        }
                    }
                },
                onValueChangeFinished = {
                    if (progressGotov.value == 0f) {
                        progressGotov.value = 1f
                        if (!fin.value) {
                            fin.value = true
                            finishListener()
                        }
                    } else {
                        animProg.value = true
                    }
                },
                colors = SliderDefaults.colors(
                    thumbColor = Color(0x6FFF8888),
                    disabledThumbColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
                        .compositeOver(MaterialTheme.colors.surface),
                    activeTrackColor = Color.Green,
                    inactiveTrackColor = Color(0x6FFF8888),
                    disabledActiveTrackColor = MaterialTheme.colors.onSurface.copy(alpha = SliderDefaults.DisabledActiveTrackAlpha),
                    disabledInactiveTrackColor = MaterialTheme.colors.onSurface.copy(alpha = SliderDefaults.DisabledInactiveTrackAlpha),
                    activeTickColor = contentColorFor(Color.Blue).copy(alpha = SliderDefaults.TickAlpha),
                    inactiveTickColor = Color.Magenta.copy(alpha = SliderDefaults.TickAlpha),
                    disabledActiveTickColor = contentColorFor(Color.Green).copy(alpha = SliderDefaults.TickAlpha)
                        .copy(alpha = SliderDefaults.DisabledTickAlpha),
                    disabledInactiveTickColor = MaterialTheme.colors.onSurface.copy(alpha = SliderDefaults.DisabledInactiveTrackAlpha)
                        .copy(alpha = SliderDefaults.DisabledTickAlpha)
                )
            )
            Box(
                Modifier.height(30.dp).width(width - 30.dp).padding(start = 10.dp)
                    .clickable { progressGotov.value = 1f })
        }
    }

}