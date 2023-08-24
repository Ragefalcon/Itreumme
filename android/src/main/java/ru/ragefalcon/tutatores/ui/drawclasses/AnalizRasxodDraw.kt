package ru.ragefalcon.tutatores.ui.drawclasses

import android.graphics.*
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.extensions.myColorRaduga
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.*
import ru.ragefalcon.tutatores.extensions.*
import java.util.*
import kotlin.math.min

class AnalizRasxodDraw {
    var sectorDiag: List<ItemSectorDiag> = listOf()
    var rectDiag: List<ItemRectDiag> = listOf()
    var twoRectDiag: List<ItemTwoRectDiag> = listOf()
    var sumOnSchet: List<ItemSumOnSchet> = listOf()

    var sumOperWeek: List<ItemOperWeek> = listOf()
    var minSumOperWeek: Float = 0F
    var maxSumOperWeek: Float = 0F
    var cursor: Float = 1F
    var shirVisGraf = 100
    var scrollValue = 0


    private var sumItogo = "0 руб."

    /** Если true, то при перерисовке произойдет обновление/перерасчет круговой диаграммы */
    private var updateSectorDiag = true
    private var updateRectDiag = true
    private var updateSpis = true
    private var updateSumOnSchet = true

    fun drawRasxodByType(canvas: Canvas) {
//        println("drawRasxodByType")
        val centr = PointF(canvas.width / 2.toFloat(), canvas.height * 9 / 20.toFloat())
        val radOut = min(canvas.width, canvas.height) * 4 / 5 / 2.toFloat()
        val radIn = radOut / 3
        var angle = -90F
        var delta = 0F
        var dd =
            1024 / (if (sectorDiag.filter { it.procent > 0.02 }.count() != 0) sectorDiag.filter { it.procent > 0.02 }
                .count() + 1 else 1)
        var color = 0

        for (type in sectorDiag.filter { it.procent > 0.02 }) {
            delta = (type.procent * 360F).toFloat()
//            if (delta == 360F) delta = 359F
            canvas.drawSectorWhiter(
                type.color.toMyIntCol().toIntColor(),
                centr,
                radOut,
                radIn,
                angle,
                delta
            )  // myColorRaduga(color).toIntColor()
            angle += delta
            color += dd
        }
        canvas.drawSectorWhiter(myColorRaduga(color).toIntColor(), centr, radOut, radIn, angle, 270F - angle)

        var pT = Paint(Paint.ANTI_ALIAS_FLAG)
        pT.style = Paint.Style.FILL
        pT.color = Color.WHITE
        pT.textSize = 15.pxF
        canvas.drawText("Итого: $sumItogo", 16.pxF, canvas.height - pT.textSize, pT)
    }

    fun setSumItogo(zn: String) {
        sumItogo = zn
        updateSectorDiag = true
    }

    fun getHeightSpis(): Int {
        return sectorDiag.count() * 30.px + 10.px
    }

    private var sumOnSchetHeight = 60
    fun getHeightSumOnSchet(): Int {
        return sumOnSchet.count() * sumOnSchetHeight.px + 20.px
    }

    fun getWidthRectDiag(): Int {
        return (rectDiag.count() * 15.pxF + 20.pxF).toInt()
    }

    fun getWidthTwoRectDiag(): Int {
        return (twoRectDiag.count() * 22.pxF + 20.pxF).toInt()
    }

    fun getWidthGraf(): Int {
        var month = 0
//        if (sumOperWeek.count() > 0) {
        sumOperWeek.lastOrNull()?.data?.let {
            month = 12 - Date(it).format("MM").toInt()
        }
        return ((sumOperWeek.count() + month*4) * 2.pxF + 20.pxF).toInt()
    }

    fun drawRasxodSpisByType(canvas: Canvas) {
        val bitmap1 = Bitmap.createBitmap(
            canvas.width, canvas.height,
            Bitmap.Config.ARGB_8888
        )
        val bitmapCanvas1 = Canvas(bitmap1)

        var p1 = Paint(Paint.ANTI_ALIAS_FLAG)
        var p2 = Paint(Paint.ANTI_ALIAS_FLAG)
        p1.style = Paint.Style.FILL
        p2.style = Paint.Style.STROKE
        p2.strokeWidth = 2.pxF
        p2.color = Color.WHITE
        p2.setShadowLayer(3.0F, 2.0F, 2.0F, Color.BLACK)

        var pT = Paint(Paint.ANTI_ALIAS_FLAG)
        pT.style = Paint.Style.FILL
        pT.color = Color.WHITE
        pT.textSize = 15.pxF
//        p2.textAlign = Paint.Align.CENTER
        var delta = 0F
        var dd =
            1024 / (if (sectorDiag.filter { it.procent > 0.02 }.count() != 0) sectorDiag.filter { it.procent > 0.02 }
                .count() + 1 else 1)
        var color = 0
        var i = 0
        for (type in sectorDiag.filter { it.procent > 0.02 }) {
            p1.color = type.color.toMyIntCol().plusWhite().toIntColor() //myColorRaduga(color).toIntColor()
            bitmapCanvas1.drawRoundRect(RectF(16.pxF, 5.pxF + i * 30.pxF, 36.pxF, 25.pxF + i * 30.pxF), 0F, 0F, p1)
            bitmapCanvas1.drawRoundRect(RectF(16.pxF, 5.pxF + i * 30.pxF, 36.pxF, 25.pxF + i * 30.pxF), 0F, 0F, p2)
            for (txt in type.name.split("\n")) {
                bitmapCanvas1.drawText(
                    txt, 46.pxF, 15.pxF + i * 30.pxF + pT.textSize / 3, pT
                )//+pT.textSize/2 //"${txt} ( ${type.summa.roundToStringProb(2)} - ${(type.procent * 100).roundToString(1)}% )"
                i++
            }
            color += dd
        }
//        var  mTextPaint:TextPaint = TextPaint();
//        var  mTextLayout = StaticLayout(sectorDiag.filter{ it.procent>0.02 }.lastOrNull().name, mTextPaint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
//
//        canvas.save();
//// calculate x and y position where your text will be placed
//
//        var textX = 46.pxF
//        var textY = 15.pxF + i*30.pxF+pT.textSize/3
//
//        canvas.translate(textX, textY)
//        mTextLayout.draw(canvas)
//        canvas.restore()

        var paintDst = Paint(Paint.ANTI_ALIAS_FLAG)
        canvas.drawBitmap(bitmap1, 0F, 0F, paintDst)
    }

    fun drawGraf(canvas: Canvas) {
        val bitmap1 = Bitmap.createBitmap(
            if (canvas.width != 0) canvas.width else 100,
            if (canvas.height != 0) canvas.height else 100,
            Bitmap.Config.ARGB_8888
        )
        val bitmapCanvas1 = Canvas(bitmap1)
        val maxVal = canvas.height * 2 / 3
        val shir = 15
        var graf = Path()
        var xx = 0.0
        val shag = 2.pxF
        val otstupVerx = 80.pxF
        val otstupNiz = 20.pxF
        val hh = canvas.height.toFloat()
        val hhGraph = hh - otstupVerx - otstupNiz
        val diap = if (minSumOperWeek < 0) maxSumOperWeek - minSumOperWeek else maxSumOperWeek
        val minusMin = if (minSumOperWeek < 0) minSumOperWeek else 0.0F

        scrollValue/shag + shirVisGraf/shag*cursor
        var cur: Int = (scrollValue/shag + shirVisGraf/shag*cursor + 1).toInt()
        if (cur>=sumOperWeek.count()) cur = sumOperWeek.count() - 1
//        println("progress cur: $cur")
//        println("progress scrollValue: $scrollValue")
//        println("progress shirVisGraf: $shirVisGraf")



        var pY = Paint(Paint.ANTI_ALIAS_FLAG)
        pY.style = Paint.Style.FILL
        pY.color = Color.YELLOW
        pY.textSize = 20.pxF
        pY.textAlign = Paint.Align.LEFT
        var pY2 = Paint(Paint.ANTI_ALIAS_FLAG)
        pY2.style = Paint.Style.FILL
        pY2.color = Color.GREEN
        pY2.textSize = 12.pxF
        pY2.textAlign = Paint.Align.LEFT

        var p1 = Paint(Paint.ANTI_ALIAS_FLAG)
        var p2 = Paint(Paint.ANTI_ALIAS_FLAG)
        var p3 = Paint(Paint.ANTI_ALIAS_FLAG)
        var pT = Paint(Paint.ANTI_ALIAS_FLAG)
        p1.style = Paint.Style.FILL_AND_STROKE
        p2.style = Paint.Style.STROKE
        p2.strokeWidth = 1.pxF
        p3.style = Paint.Style.STROKE
        p3.strokeWidth = 3.pxF

        pT.style = Paint.Style.FILL
        pT.color = Color.WHITE
        pT.textSize = 10.pxF
        pT.textAlign = Paint.Align.RIGHT
        var i = 0
        p1.color = Color.RED.toMyColorARGB().plusWhite().plusWhite().toIntColor()// myColorRaduga(color).toIntColor()
        p3.color = Color.WHITE.toMyColorARGB().plusWhite().plusWhite().toIntColor()// myColorRaduga(color).toIntColor()
        p2.color = Color.GREEN.toMyColorARGB().toIntColor()
        var textPath = Path()

        /**
         * Отрисовка полосок между годами
         * */
        var date = sumOperWeek.firstOrNull()?.data?.let{ Date(sumOperWeek.first().data).format("yyyy")} ?: "0000"
        var num = 0
        var key = false
        for (item in sumOperWeek) {
            if (date != Date(item.data).format("yyyy")) {
                bitmapCanvas1.drawLine(num * shag - 1.0F, otstupVerx / 2 - otstupNiz * 3 / 4, num * shag - 1.0F, hh, p1)
                textPath.reset()
                textPath.moveTo(10.pxF + num * shag, 24.pxF)
                textPath.lineTo(100.pxF + num * shag, 24.pxF)
                bitmapCanvas1.drawTextOnPath(
                    "${Date(item.data).format("yyyy")}",
                    textPath,
                    0F,
                    0F,
                    pY
                )

                textPath.reset()
                textPath.moveTo(10.pxF + num * shag, 40.pxF)
                textPath.lineTo(100.pxF + num * shag, 40.pxF)
                bitmapCanvas1.drawTextOnPath(
                    "${item.sumCap.roundToStringProb(2)}",
                    textPath,
                    0F,
                    0F,
                    pY2
                )
            }
            if (num==cur) {
                bitmapCanvas1.drawLine(num * shag - 1.0F, 20.pxF + otstupVerx / 2 - otstupNiz * 3 / 4, num * shag - 1.0F, hh, p2)
                bitmapCanvas1.drawCircle(num * shag, hh - otstupNiz - (item.sumCap.toFloat() - minusMin) / diap * hhGraph,4.pxF,p1)
                textPath.reset()
                textPath.moveTo(10.pxF + num * shag, 56.pxF)
                textPath.lineTo(100.pxF + num * shag, 56.pxF)
                bitmapCanvas1.drawTextOnPath(
                    "${item.sumCap.roundToStringProb(2)}",
                    textPath,
                    0F,
                    0F,
                    pY2
                )
                textPath.reset()
                textPath.moveTo(10.pxF + num * shag, 72.pxF)
                textPath.lineTo(100.pxF + num * shag, 72.pxF)
                bitmapCanvas1.drawTextOnPath(
                    "${Date(item.data).format("dd MMM yyyy")}",
                    textPath,
                    0F,
                    0F,
                    pY2
                )
            }
            num++
            date = Date(item.data).format("yyyy")
        }

        /**
         * Отрисовка горизонтальной оси нулевого баланса
         * */
        bitmapCanvas1.drawLine(
            0.0F,
            hh - otstupNiz + minusMin / diap * hhGraph,
            sumOperWeek.count() * shag,
            hh - otstupNiz + minusMin / diap * hhGraph,
            p1
        )



        graf.moveTo(0.0F, hh - otstupNiz + minusMin / diap * hhGraph)
        for (item in sumOperWeek) {
            graf.lineTo(i * shag, hh - otstupNiz - (item.sumCap.toFloat() - minusMin) / diap * hhGraph)
//            Log.d("MyPath", "yy week: ${hh - otstupNiz - (item.sumCap.toFloat() - minusMin) / diap * hhGraph}")

//            textPath.reset()
//            textPath.moveTo(22.pxF+i*shir.pxF,24.pxF + maxVal*2)
//            textPath.lineTo(22.pxF+i*shir.pxF,24.pxF + maxVal)
//            bitmapCanvas1.drawTextOnPath("( ${type.summa.roundToStringProb(2)} ) ${type.month}",textPath,0F,0F,pT)//16.pxF,9.pxF + i*30.pxF,pT)//+pT.textSize/3
            i++
        }

//        Log.d("MyPath", "count week: ${sumOperWeek.count()}")
//        graf.close()
        bitmapCanvas1.drawPath(graf, p3)
        bitmapCanvas1.drawPath(graf, p2)
        var paintDst = Paint(Paint.ANTI_ALIAS_FLAG)
        canvas.drawBitmap(bitmap1, 0F, 0F, paintDst)
    }

    fun drawRectDiag(canvas: Canvas) {
        val bitmap1 = Bitmap.createBitmap(
            if (canvas.width != 0) canvas.width else 100,
            if (canvas.height != 0) canvas.height else 100,
            Bitmap.Config.ARGB_8888
        )
        val bitmapCanvas1 = Canvas(bitmap1)
        val maxVal = canvas.height * 2 / 3
        val shir = 15
        var p1 = Paint(Paint.ANTI_ALIAS_FLAG)
        var p2 = Paint(Paint.ANTI_ALIAS_FLAG)
        var pT = Paint(Paint.ANTI_ALIAS_FLAG)
        p1.style = Paint.Style.FILL
        p2.style = Paint.Style.STROKE
        p2.strokeWidth = 1.pxF
        p2.color = Color.WHITE

        pT.style = Paint.Style.FILL
        pT.color = Color.WHITE
        pT.textSize = 10.pxF
        pT.textAlign = Paint.Align.RIGHT
//        p2.textAlign = Paint.Align.CENTER
        var delta = 0F
        var dd =
            1024 / (if (sectorDiag.filter { it.procent > 0.02 }.count() != 0) sectorDiag.filter { it.procent > 0.02 }
                .count() + 1 else 1)
        var color = 0
        var i = 0
        p1.color = Color.RED.toMyColorARGB().plusWhite().plusWhite().toIntColor()// myColorRaduga(color).toIntColor()
        p2.color = Color.RED.toMyColorARGB().toIntColor()
        var textPath = Path()
        for (type in rectDiag.asReversed()) {
            bitmapCanvas1.drawRoundRect(
                RectF(
                    12.pxF + i * shir.pxF,
                    16.pxF + maxVal,
                    24.pxF + i * shir.pxF,
                    16.pxF + maxVal * (1 - type.procent.toFloat())
                ), 0F, 0F, p1
            )
            bitmapCanvas1.drawRoundRect(
                RectF(
                    12.pxF + i * shir.pxF,
                    16.pxF + maxVal,
                    24.pxF + i * shir.pxF,
                    16.pxF + maxVal * (1 - type.procent.toFloat())
                ), 0F, 0F, p2
            )
//            bitmapCanvas1.drawRoundRect(RectF(16.pxF,12.pxF+i*30.pxF, 16.pxF + (canvas.width - 16.pxF)*type.procent.toFloat(), 25.pxF + i*30.pxF),0F,0F,p2)

            textPath.reset()
            textPath.moveTo(22.pxF + i * shir.pxF, 24.pxF + maxVal * 2)
            textPath.lineTo(22.pxF + i * shir.pxF, 24.pxF + maxVal)
//            textPath.moveTo(16.pxF+i*shir.pxF,16.pxF + maxVal)
//            textPath.lineTo(16.pxF+i*shir.pxF,16.pxF + maxVal*2)
            bitmapCanvas1.drawTextOnPath(
                "( ${type.summa.roundToStringProb(2)} ) ${type.month}",
                textPath,
                0F,
                0F,
                pT
            )//16.pxF,9.pxF + i*30.pxF,pT)//+pT.textSize/3
            i++
            color += dd
        }
        var paintDst = Paint(Paint.ANTI_ALIAS_FLAG)
        canvas.drawBitmap(bitmap1, 0F, 0F, paintDst)
    }

    fun drawTwoRectDiag(canvas: Canvas) {
        val bitmap1 = Bitmap.createBitmap(
            if (canvas.width != 0) canvas.width else 100,
            if (canvas.height != 0) canvas.height else 100,
            Bitmap.Config.ARGB_8888
        )
        val bitmapCanvas1 = Canvas(bitmap1)
        val maxVal = canvas.height * 2 / 3
        val shir = 22
        var p1 = Paint(Paint.ANTI_ALIAS_FLAG)
        var p2 = Paint(Paint.ANTI_ALIAS_FLAG)
        var p3 = Paint(Paint.ANTI_ALIAS_FLAG)
        var p4 = Paint(Paint.ANTI_ALIAS_FLAG)

        var pT = Paint(Paint.ANTI_ALIAS_FLAG)
        pT.style = Paint.Style.FILL
        pT.color = Color.WHITE
        pT.textSize = 10.pxF
        pT.textAlign = Paint.Align.RIGHT

        var pY = Paint(Paint.ANTI_ALIAS_FLAG)
        pY.style = Paint.Style.FILL
        pY.color = Color.YELLOW
        pY.textSize = 20.pxF
        pY.textAlign = Paint.Align.LEFT
        var pY2 = Paint(Paint.ANTI_ALIAS_FLAG)
        pY2.style = Paint.Style.FILL
        pY2.color = Color.GREEN
        pY2.textSize = 12.pxF
        pY2.textAlign = Paint.Align.LEFT
        var pY3 = Paint(Paint.ANTI_ALIAS_FLAG)
        pY3.style = Paint.Style.FILL
        pY3.color = Color.RED.toMyColorARGB().plusWhite().plusWhite().toIntColor()
        pY3.textSize = 12.pxF
        pY3.textAlign = Paint.Align.LEFT

        p1.style = Paint.Style.FILL
        p2.style = Paint.Style.STROKE
        p2.strokeWidth = 1.pxF
        p3.style = Paint.Style.FILL
        p4.style = Paint.Style.STROKE
        p4.strokeWidth = 1.pxF

        var i = 0
        p1.color = Color.RED.toMyColorARGB().plusWhite().plusWhite().toIntColor()// myColorRaduga(color).toIntColor()
        p2.color = Color.RED.toMyColorARGB().toIntColor()
        p3.color = Color.GREEN.toMyColorARGB().plusWhite().plusWhite().toIntColor()// myColorRaduga(color).toIntColor()
        p4.color = MyColorARGB.DOXODDARKGREEN.toIntColor()
        var textPath = Path()
        for (type in twoRectDiag.asReversed()) {
            bitmapCanvas1.drawRoundRect(
                RectF(
                    12.pxF + i * shir.pxF,
                    16.pxF + maxVal,
                    20.pxF + i * shir.pxF,
                    16.pxF + maxVal * (1 - 0.7F * type.procentrasx.toFloat())
                ), 0F, 0F, p1
            )
            bitmapCanvas1.drawRoundRect(
                RectF(
                    12.pxF + i * shir.pxF,
                    16.pxF + maxVal,
                    20.pxF + i * shir.pxF,
                    16.pxF + maxVal * (1 - 0.7F * type.procentrasx.toFloat())
                ), 0F, 0F, p2
            )
            bitmapCanvas1.drawRoundRect(
                RectF(
                    22.pxF + i * shir.pxF,
                    16.pxF + maxVal,
                    30.pxF + i * shir.pxF,
                    16.pxF + maxVal * (1 - 0.7F * type.procentdox.toFloat())
                ), 0F, 0F, p3
            )
            bitmapCanvas1.drawRoundRect(
                RectF(
                    22.pxF + i * shir.pxF,
                    16.pxF + maxVal,
                    30.pxF + i * shir.pxF,
                    16.pxF + maxVal * (1 - 0.7F * type.procentdox.toFloat())
                ), 0F, 0F, p4
            )
            if (type.month == "01") {
                bitmapCanvas1.drawLine(10.pxF + i * shir.pxF, 16.pxF + maxVal, 10.pxF + i * shir.pxF, 6.pxF, p2)
                textPath.reset()
                textPath.moveTo(20.pxF + i * shir.pxF, 24.pxF)
                textPath.lineTo(70.pxF + i * shir.pxF, 24.pxF)
//            textPath.moveTo(16.pxF+i*shir.pxF,16.pxF + maxVal)
//            textPath.lineTo(16.pxF+i*shir.pxF,16.pxF + maxVal*2)
                bitmapCanvas1.drawTextOnPath(
                    "${type.year}",
                    textPath,
                    0F,
                    0F,
                    pY
                )//16.pxF,9.pxF + i*30.pxF,pT)//+pT.textSize/3
                textPath.reset()
                textPath.moveTo(20.pxF + i * shir.pxF, 40.pxF)
                textPath.lineTo(100.pxF + i * shir.pxF, 40.pxF)
                bitmapCanvas1.drawTextOnPath(
                    "${type.sumyearrasx.roundToStringProb(2)}",
                    textPath,
                    0F,
                    0F,
                    pY3
                )//16.pxF,9.pxF + i*30.pxF,pT)//+pT.textSize/3
                textPath.reset()
                textPath.moveTo(20.pxF + i * shir.pxF, 56.pxF)
                textPath.lineTo(100.pxF + i * shir.pxF, 56.pxF)
                bitmapCanvas1.drawTextOnPath(
                    "${type.sumyeardox.roundToStringProb(2)}",
                    textPath,
                    0F,
                    0F,
                    pY2
                )//16.pxF,9.pxF + i*30.pxF,pT)//+pT.textSize/3
            }
//            bitmapCanvas1.drawRoundRect(RectF(16.pxF,12.pxF+i*30.pxF, 16.pxF + (canvas.width - 16.pxF)*type.procent.toFloat(), 25.pxF + i*30.pxF),0F,0F,p2)

            textPath.reset()
            textPath.moveTo(24.pxF + i * shir.pxF, 24.pxF + maxVal * 2.5F)
            textPath.lineTo(24.pxF + i * shir.pxF, 24.pxF + maxVal)
//            textPath.moveTo(16.pxF+i*shir.pxF,16.pxF + maxVal)
//            textPath.lineTo(16.pxF+i*shir.pxF,16.pxF + maxVal*2)
            bitmapCanvas1.drawTextOnPath(
                "( ${type.summarasx.roundToStringProb(2)}/${type.summadox.roundToStringProb(2)} ) ${type.monthyear}",
                textPath,
                0F,
                0F,
                pT
            )//16.pxF,9.pxF + i*30.pxF,pT)//+pT.textSize/3
            i++
        }
        var paintDst = Paint(Paint.ANTI_ALIAS_FLAG)
        canvas.drawBitmap(bitmap1, 0F, 0F, paintDst)
    }

    fun drawSumOnSchet(canvas: Canvas) {
        val bitmap1 = Bitmap.createBitmap(
            canvas.width, canvas.height,
            Bitmap.Config.ARGB_8888
        )
        val bitmapCanvas1 = Canvas(bitmap1)

        var p1 = Paint(Paint.ANTI_ALIAS_FLAG)
        p1.style = Paint.Style.FILL
        p1.color = Color.GREEN.toMyColorARGB().plusWhite().plusWhite().toIntColor()// myColorRaduga(color).toIntColor()
        p1.setShadowLayer(3.0F, 2.0F, 2.0F, Color.BLACK)

        var p2 = Paint(Paint.ANTI_ALIAS_FLAG)
        p2.style = Paint.Style.STROKE
        p2.strokeWidth = 1.pxF
        p2.color = Color.WHITE
        p2.setShadowLayer(3.0F, 2.0F, 2.0F, Color.BLACK)


        var pT = Paint(Paint.ANTI_ALIAS_FLAG)
        pT.style = Paint.Style.FILL
        pT.color = Color.WHITE
        pT.textSize = 12.pxF
        pT.setShadowLayer(3.0F, 2.0F, 2.0F, Color.BLACK)
//        p2.textAlign = Paint.Align.CENTER

        var i = 0
        var koef = 0F
//        p2.color = Color.RED.toMyColorARGB().plusWhite().toIntColor()
        for (schet in sumOnSchet) {
//            bitmapCanvas1.drawRoundRect(RectF(16.pxF,12.pxF+i*30.pxF, 16.pxF + (canvas.width - 16.pxF)*schet.procent.toFloat(), 25.pxF + i*30.pxF),0F,0F,p1)
            if (schet.procent > 0) {
                koef = schet.procent.toFloat()
                p1.color =
                    Color.GREEN.toMyColorARGB().plusWhite().plusWhite().toIntColor()// myColorRaduga(color).toIntColor()
            } else {
                koef = -schet.procent.toFloat()
                p1.color =
                    Color.RED.toMyColorARGB().plusWhite().plusWhite().toIntColor()// myColorRaduga(color).toIntColor()
            }
            bitmapCanvas1.drawRoundRect(
                RectF(
                    24.pxF,
                    45.pxF + i * sumOnSchetHeight.pxF,
                    24.pxF + (canvas.width - 48.pxF) * koef,
                    60.pxF + i * sumOnSchetHeight.pxF
                ), 0F, 0F, p1
            )
//            bitmapCanvas1.drawRoundRect(RectF(16.pxF,12.pxF+i*30.pxF, 16.pxF + (canvas.width - 16.pxF)*schet.procent.toFloat(), 25.pxF + i*30.pxF),0F,0F,p2)
            bitmapCanvas1.drawRoundRect(
                RectF(
                    16.pxF,
                    10.pxF + i * sumOnSchetHeight.pxF,
                    canvas.width - 16.pxF,
                    65.pxF + i * sumOnSchetHeight.pxF
                ), 0F, 0F, p2
            )
            bitmapCanvas1.drawText(
                schet.name,
                24.pxF,
                12.pxF + i * sumOnSchetHeight.pxF + pT.textSize,
                pT
            )//+pT.textSize/3
            bitmapCanvas1.drawText(
                schet.summaStr,
                24.pxF,
                16.pxF + i * sumOnSchetHeight.pxF + pT.textSize * 2,
                pT
            )//+pT.textSize/3
            i++
        }
        var paintDst = Paint(Paint.ANTI_ALIAS_FLAG)
        canvas.drawBitmap(bitmap1, 0F, 0F, paintDst)
    }

    fun setUpdate() {
        updateSectorDiag = true
        updateSpis = true
    }

    fun setUpdateRect() {
        updateRectDiag = true
    }

    fun setUpdateSumOnSchet() {
        updateSumOnSchet = true
    }

    fun calculate(): Boolean {
        if (updateSectorDiag) {
            updateSectorDiag = false
            return true
        } else {
            return false
        }
    }

    fun calculateRect(): Boolean {
        if (updateRectDiag) {
            updateRectDiag = false
            return true
        } else {
            return false
        }
    }

    fun calculateSpis(): Boolean {
        if (updateSpis) {
            updateSpis = false
            return true
        } else {
            return false
        }
    }

    fun calculateSumOnSchet(): Boolean {
        if (updateSumOnSchet) {
            updateSumOnSchet = false
            return true
        } else {
            return false
        }
    }
}