package ru.ragefalcon.tutatores.ui.drawclasses;

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import ru.ragefalcon.sharedcode.models.data.ItemEffekt
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.extensions.pxF
import java.lang.Math.min

class BackgroundDial @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DrawViewHelper(context, attrs, defStyleAttr) {

    private var updateNeed: Boolean = false

//    fun setItemEffekt(itemm: ItemEffekt) {
//        item = itemm
//        updateNeed = true
//        invalidate()
//    }

    override fun calculateFun(): Boolean {
        if (updateNeed) {
            updateNeed = false
            return true
        } else {
            return false
        }
    }

    private val otstupOut = 2.pxF
    private var p1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.colorEffektShkal_Year)
//        color = Color.GREEN.toMyColorARGB().plusWhite().plusWhite().toIntColor()// myColorRaduga(color).toIntColor()
        setShadowLayer(3.0F, 2.0F, 2.0F, Color.BLACK)
    }
    private var pBackgb = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.colorEffektShkal_Back)
//        color = Color.RED.toMyColorARGB().plusWhite().plusWhite().toIntColor()// myColorRaduga(color).toIntColor()
//        setShadowLayer(3.0F, 2.0F, 2.0F, Color.BLACK)
    }
    private var pRamk = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 3.pxF
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        color = ContextCompat.getColor(context, R.color.colorBackRamk)//Color.WHITE
        setShadowLayer(6.0F, 4.0F, 4.0F, Color.BLACK)
    }
    private var pRamk2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 3.pxF
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        color = ContextCompat.getColor(context, R.color.colorBackRamk)//Color.WHITE
        setShadowLayer(6.0F, -2.0F, -2.0F, Color.BLACK)
    }

    override fun drawFun(canvas: Canvas) {
        val bitmap = Bitmap.createBitmap(
            if (canvas.width != 0) canvas.width else 100,
            if (canvas.height != 0) canvas.height else 100,
            Bitmap.Config.ARGB_8888
        )
        val bmpCnvs = Canvas(bitmap)
        val bitmap2 = Bitmap.createBitmap(
            canvas.height,
            canvas.height,
            Bitmap.Config.ARGB_8888
        )
        val bmpCnvs2 = Canvas(bitmap2)
        val rectRamk = RectF(0F, 0F, canvas.width.toFloat(), canvas.height.toFloat()).apply {
            inset(otstupOut+3.pxF, otstupOut+3.pxF)
        }
//        bmpCnvs.drawRect(
//            rectRamk,  pBackgb
//        )

        // canvas for drawing
//        val canvas = Canvas(bitmap).apply {
//            drawColor(Color.parseColor("#FEFEFA"))
//        }

        // radius of circle and gradient
        val radius = min(canvas.width, canvas.height) / 2F - 25F

        // paint to draw radial gradient circle
        val paint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL

            // radial gradient shader
            shader = RadialGradient(
                bmpCnvs2.width / 2F, // center x
                bmpCnvs2.height / 2F, // center y
                bmpCnvs2.width/1F, // radius
                intArrayOf( // colors
                    ContextCompat.getColor(context, R.color.colorBackGr1),
//                    ContextCompat.getColor(context, R.color.colorBackGr2),
                    ContextCompat.getColor(context, R.color.colorBackGr3)
//                    Color.parseColor("#E30022"),
//                    Color.parseColor("#F7E7CE"),
//                    Color.parseColor("#FFF600")
                ),
                // stops - may be null, valid values are between 0.0f and 1.0f
                null,
                Shader.TileMode.CLAMP // shader titling mode
            )
        }

        // finally, draw gradient circle on canvas
        bmpCnvs2.drawRoundRect(
            RectF(0f,0f,bmpCnvs2.width*1f,bmpCnvs2.height*1f),
            10f,10f,
            paint)
//        drawCircle(
//            bmpCnvs2.width / 2F, // cx
//            bmpCnvs2.height / 2F, // cy
//            radius, // radius
//            paint // paint to draw
//        )

        val paintDst = Paint(Paint.ANTI_ALIAS_FLAG)
        bmpCnvs.drawBitmap(bitmap2, Rect(0,0,bmpCnvs2.width,bmpCnvs2.height), rectRamk,paintDst)
        bmpCnvs.drawRoundRect(
            rectRamk, 15f,15f, pRamk
        )
//        bmpCnvs.drawRoundRect(
//            rectRamk, 15f,15f, pRamk2
//        )
        canvas.drawBitmap(bitmap, 0F, 0F, paintDst)
    }
}
