package tests

//import org.jetbrains.skia.Font
//import org.jetbrains.skia.FontStyle
//import org.jetbrains.skia.Paint
//import org.jetbrains.skia.Typeface
//import org.jetbrains.skija.*
//import org.jetbrains.skiko.*
//
//class SkiaTest {
//}
//public class CounterRenderer : SkiaRenderer {
//    private var counter = 0
//    private val typeface = Typeface.makeFromName("Roboto", FontStyle.NORMAL)
//    private val fontSize = 30F
//    private val font = Font(typeface, fontSize)
//    private val paint = Paint().setColor(0XFF000000.toInt())
//
//    override fun onInit() {
//    }
//
//    override fun onDispose() {
//    }
//
//    override fun onReshape(width: Int, height: Int) {
//    }
//
//    override fun onRender(canvas: Canvas, width: Int, height: Int) {
//        canvas.drawString("Counter: ${counter++}", 10F, 50F, font, paint)
//    }
//}