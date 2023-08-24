package common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.Stroke
import extensions.MyRectF
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB

@Composable
fun BackCanvasWithHole(rectHole: MyRectF, modifier: Modifier = Modifier.fillMaxSize()) {
    Canvas(
        modifier = modifier
    ) {

        val cornerRadius = 20f
        val testPath = Path()
        val rectRamk = MyRectF(this.size, Offset.Zero)
        testPath.addRect(rectRamk.getRect())
        val rectH = MyRectF(rectHole)
        rectH.inset(-cornerRadius, -cornerRadius)
//        rectRamk.offsetX = -10f
//        rectRamk.offsetY = -10f
//            testPath.addRect(rectRamk.getRect())
        testPath.addRoundRect(RoundRect(rectH.getRect(), CornerRadius(cornerRadius)))
//        testPath.addOval(rectRamk.getRect())
        testPath.fillType = PathFillType.EvenOdd
        drawPath(testPath, color = Color.Black.copy(alpha = 0.9f))
        drawPath(
            testPath,
            color = MyColorARGB.colorMyBorderStroke.toColor(),
            style = Stroke(width = 2f),
            blendMode = BlendMode.SrcIn
        )
    }

}