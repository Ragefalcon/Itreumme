package MainTabs.Time.Elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.MyTextButtStyle2
import common.MyTextStyleParam
import common.MyTextToggleButtStyle1
import extensions.format
import extensions.minusOffset
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import java.util.*

class TimeSelectSlider(dtStart: Date, dtEnd: Date) {

    private fun DateToFloat(date: Date): Float {

        return date.format("HH").toFloat() * 12f + date.format("mm").toFloat() / 5f
    }

    private fun FloatToDate(value: Float): Date {
        return Date().apply {
            time = value.toLong() * 5 * 60 * 1000

        }.minusOffset()
    }

    private val progressStart = mutableStateOf(DateToFloat(dtStart))
    private val progressEnd = mutableStateOf(DateToFloat(dtEnd))

    fun getTimeStart(): Date {
        return FloatToDate(progressStart.value)
    }

    fun getTimeEnd(): Date {
        return FloatToDate(progressEnd.value)
    }

    fun setTimeStart(date: Date) {
        progressStart.value = DateToFloat(date)
    }

    fun setTimeEnd(date: Date) {
        progressEnd.value = DateToFloat(date)
    }

    private val fixDiap = mutableStateOf(false)
    private var widthDiap = 20f

    private val maxValSlider = 24f * 12f

    @Composable
    fun getComposable(modifier: Modifier = Modifier) {
        Card(
            modifier = modifier
                .padding(horizontal = 8.dp, vertical = 2.dp)

                .border(
                    width = 0.5.dp,
                    brush = Brush.horizontalGradient(
                        listOf(Color(0x7FFFF7D9), Color(0x7FFFF7D9))
                    ),
                    shape = RoundedCornerShape(10.dp)
                ),
            elevation = 8.dp,
            backgroundColor = MyColorARGB.colorMyMainTheme.toColor(),
            shape = RoundedCornerShape(corner = CornerSize(10.dp))
        ) {
            Column(
                Modifier.padding(top = 10.dp).padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val razryv = progressStart.value <= progressEnd.value

                Row(verticalAlignment = Alignment.CenterVertically) {
                    MyTextButtStyle2(
                        "❮ ",
                        modifier = Modifier.width(30.dp).height(30.dp),
                        fontSize = 12.sp
                    ) {
                        progressStart.value -= 1
                    }
                    MyTextButtStyle2(
                        "❯",
                        modifier = Modifier.width(30.dp).height(30.dp),
                        fontSize = 12.sp
                    ) {
                        progressStart.value += 1
                    }
                    Spacer(Modifier.weight(1f))
                    MyTextToggleButtStyle1(
                        "ФП",
                        modifier = Modifier.width(40.dp).height(30.dp),
                        value = fixDiap
                    ) {
                        widthDiap = (progressEnd.value.toInt() - progressStart.value.toInt()).toFloat()
                    }
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "${(progressStart.value.toInt() * 5 / 60F).toInt()} ч. ${(progressStart.value.toInt() * 5 % 60).toInt()} мин. - " +
                                "${(progressEnd.value.toInt() * 5 / 60F).toInt()} ч. ${(progressEnd.value.toInt() * 5 % 60).toInt()} мин.",
                        style = MyTextStyleParam.style1.copy(fontSize = 18.sp)
                    )
                    Spacer(Modifier.weight(1f))
                    MyTextButtStyle2(
                        "❮",
                        modifier = Modifier.width(30.dp).height(30.dp),
                        fontSize = 12.sp
                    ) {
                        progressEnd.value -= 1
                    }
                    MyTextButtStyle2(
                        "❯",
                        modifier = Modifier.width(30.dp).height(30.dp),
                        fontSize = 12.sp
                    ) {
                        progressEnd.value += 1
                    }
                }

                Box(Modifier.height(60.dp).fillMaxWidth()) {
                    BoxWithConstraints(Modifier.fillMaxWidth().padding(10.dp)) {
                        val minV = minOf(progressStart.value, progressEnd.value)
                        val maxV = maxOf(progressStart.value, progressEnd.value)
                        val width1 = maxWidth * minV / maxValSlider
                        val width2 = maxWidth * (maxV - minV) / maxValSlider
                        val width3 = maxWidth * (1 - maxV / maxValSlider)
                        Row {
                            Box(
                                Modifier.height(30.dp)
                                    .background(if (razryv) Color.Black.copy(alpha = 0.3f) else Color.Cyan.copy(alpha = 0.3f))
                                    .width(width1)
                            )
                            Box(
                                Modifier.height(30.dp)
                                    .background(if (razryv) Color.Green.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.3f))
                                    .width(width2)
                            )
                            Box(
                                Modifier.height(30.dp)
                                    .background(if (razryv) Color.Black.copy(alpha = 0.3f) else Color.Green.copy(alpha = 0.3f))
                                    .width(width3)
                            )
                        }
                    }
                    Row(Modifier.fillMaxWidth().padding(10.dp)) {
                        for (i in 0..23 step 3) {
                            Text(
                                " ${i.toString()}",
                                Modifier.border(1.dp, Color.White).weight(1f).height(30.dp),
                                style = MyTextStyleParam.style2.copy(textAlign = TextAlign.Start)
                            )
                        }
                    }
                    Slider(
                        value = progressStart.value,
                        modifier = Modifier.height(10.dp).fillMaxWidth().padding(top = 5.dp),
                        onValueChange = {
                            progressStart.value = it
                            if (fixDiap.value) {
                                var secondProg = it + widthDiap
                                if (secondProg > maxValSlider) secondProg -= maxValSlider
                                if (secondProg < 0) secondProg += maxValSlider
                                progressEnd.value = secondProg
                            }
                        },
                        onValueChangeFinished = {
                        },
                        valueRange = 0f..maxValSlider,
                        steps = maxValSlider.toInt(),
                        colors = SliderDefaults.colors(
                            thumbColor = MyColorARGB.colorStatTint_03.toColor(),
                            disabledThumbColor = Color.Transparent,
                            activeTrackColor = Color.Transparent,
                            inactiveTrackColor = Color.Transparent,
                            disabledActiveTrackColor = Color.Transparent,
                            disabledInactiveTrackColor = Color.Transparent,
                            activeTickColor = Color.Transparent,
                            inactiveTickColor = Color.Transparent,
                            disabledActiveTickColor = Color.Transparent,
                            disabledInactiveTickColor = Color.Transparent,
                        )
                    )
                    Slider(
                        value = progressEnd.value,
                        modifier = Modifier.height(10.dp).fillMaxWidth().padding(top = 40.dp),
                        onValueChange = {
                            progressEnd.value = it
                            if (fixDiap.value) {
                                var secondProg = it - widthDiap
                                if (secondProg > maxValSlider) secondProg -= maxValSlider
                                if (secondProg < 0) secondProg += maxValSlider
                                progressStart.value = secondProg
                            }
                        },
                        onValueChangeFinished = {
                        },
                        valueRange = 0f..maxValSlider,
                        steps = maxValSlider.toInt(),
                        colors = SliderDefaults.colors(
                            thumbColor = MyColorARGB.colorStatTint_04.toColor(),
                            disabledThumbColor = Color.Transparent,
                            activeTrackColor = Color.Transparent,
                            inactiveTrackColor = Color.Transparent,
                            disabledActiveTrackColor = Color.Transparent,
                            disabledInactiveTrackColor = Color.Transparent,
                            activeTickColor = Color.Transparent,
                            inactiveTickColor = Color.Transparent,
                            disabledActiveTickColor = Color.Transparent,
                            disabledInactiveTickColor = Color.Transparent,
                        )
                    )
                }
            }
        }
    }
}