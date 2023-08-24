package MainTabs.Time.Elements

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp.Companion.Hairline
import androidx.compose.ui.unit.dp
import extensions.format
import extensions.timeFromHHmmss
import extensions.toColor
import ru.ragefalcon.sharedcode.models.data.ItemDenPlan
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.StyleVMspis
import viewmodel.MainDB
import java.util.*

@Composable
fun ShkalTime(denPlan: ItemDenPlan, modifier: Modifier = Modifier.width(150.dp).height(15.dp), density: Density,
              colors: StyleVMspis.InterfaceState.ColorShkal = MainDB.styleParam.timeParam.planTab.panHistory.itemHistory.colorShkal) {
    val time1 = denPlan.let { Date().timeFromHHmmss(it.time1) }
    val time2 = denPlan.let { Date().timeFromHHmmss(it.time2) }
    ShkalTime(time1, time2, modifier, density,colors)
}

@Composable
fun ShkalTime(time1: Date, time2: Date, modifier: Modifier = Modifier.width(150.dp).height(15.dp), density: Density,
              colors: StyleVMspis.InterfaceState.ColorShkal = MainDB.styleParam.timeParam.planTab.panHistory.itemHistory.colorShkal) {


    val colorRamk = colors.colorRamk.getValue().toColor()
    val colorBack = colors.colorBack.getValue().toColor()
    val colorTime = colors.colorTime.getValue().toColor()
    val colorTommorow = colors.colorTommorow.getValue().toColor()

    fun DateToFloat(date: Date): Float {
//        return ((date.withOffset().time/60f/1000f).toInt()%(24*60+1))/5f
        return date.format("HH").toFloat() * 12f + date.format("mm").toFloat() / 5f
    }

    val oneDay = 24f * 12f //1000*60*60*24
    val startTime: Float = DateToFloat(time1)
    val endTime: Float = DateToFloat(time2)
//   if (startTime< 0f) startTime +=oneDay
//    if (endTime< 0f) endTime +=oneDay
    Canvas(
        modifier = modifier
    ) {


        val canvasWidth = size.width
        val canvasHeight = size.height

        val razryv = startTime <= endTime

        val minV = minOf(startTime, endTime)
        val maxV = maxOf(startTime, endTime)
        val width1 = canvasWidth * minV / oneDay.toFloat()
        val width2 = canvasWidth * (maxV - minV) / oneDay.toFloat()
        val width3 = canvasWidth * (1 - maxV / oneDay.toFloat())

        drawRect(
            if (razryv) colorBack else colorTime,
            Offset(0f, 0f),
            Size(width1, canvasHeight)
        )
        if (!razryv) drawRect(
            colorTommorow,
            Offset(0f, 0f),
            Size(width1, canvasHeight)
        )
        drawRect(
            if (razryv) colorTime else colorBack,
            Offset(width1, 0f),
            Size(width2, canvasHeight)
        )
        drawRect(
            if (razryv) colorBack else colorTime,
            Offset(width1 + width2, 0f),
            Size(width3, canvasHeight)
        )

        val ramk = Path()
        for (i in 1..7) {
            ramk.moveTo(canvasWidth / 8f * i, 0f)
            ramk.lineTo(canvasWidth / 8f * i, canvasHeight)
        }
        ramk.addRect(Rect(Offset(0f, 0f), Size(canvasWidth, canvasHeight)))
        with(density) {
            drawPath(ramk, colorRamk, style = Stroke(Hairline.toPx()))
        }
    }

}