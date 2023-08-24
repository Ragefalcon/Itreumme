package common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import extensions.drawSectorWhiter
import extensions.toColor
import org.jetbrains.skia.Point
import ru.ragefalcon.sharedcode.extensions.myColorRaduga
import ru.ragefalcon.sharedcode.models.data.ItemSectorDiag
import kotlin.math.min

class DrawSektorDiagram {


    fun drawRasxodByType(canvas: DrawScope, sectorDiag: List<ItemSectorDiag>, density: Density) {
        canvas.apply {
            val centr = Point(canvas.size.width / 2.toFloat(), canvas.size.height / 2.toFloat())
            val radOut = min(canvas.size.width, canvas.size.height) / 2.toFloat()
            val radIn = radOut / 3
            var angle = -90F
            var delta = 0F
            val dd =
                1024 / (if (sectorDiag.filter { it.procent > 0.02 }
                        .count() != 0) sectorDiag.filter { it.procent > 0.02 }
                    .count() + 1 else 1)
            var color = 0

            with(density) {
                for (type in sectorDiag.filter { it.procent > 0.02 }) {
                    delta = (type.procent * 360F).toFloat()
                    drawSectorWhiter(
                        type.color.toMyIntCol().toColor(),
                        centr,
                        radOut,
                        radIn,
                        angle,
                        delta,
                        2.dp.toPx()
                    )
                    angle += delta
                    color += dd
                }
                drawSectorWhiter(myColorRaduga(color).toColor(), centr, radOut, radIn, angle, 270F - angle,2.dp.toPx())
            }
        }
    }

    @Composable
    fun drawDiagram(modifier: Modifier = Modifier, sectorDiag: List<ItemSectorDiag>) {
        val density  = LocalDensity.current
        Canvas(
            modifier = modifier.padding(horizontal = 13.dp)
                .padding(bottom = 10.dp)
        ) {
            drawRasxodByType(this, sectorDiag, density)
        }
    }
}