package ru.ragefalcon.sharedcode.tests

import com.soywiz.korim.bitmap.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.extension
import com.soywiz.korio.file.std.localVfs
import com.soywiz.korma.geom.Anchor
import com.soywiz.korma.geom.RectangleInt
import com.soywiz.korma.geom.ScaleMode

suspend fun testImageResize(fff: (ByteArray) -> Unit) {
//    val bitmap1 = Bitmap8(width, height)
//    resourcesVfs["test.png"]
//    val aa = localVfs("C:/Kotlin/Tutatores_KMP/desktop/test.png").readBitmap()
//        .resized(256, 256, ScaleMode.COVER, Anchor.CENTER)
//
//    aa.writeTo(localVfs("C:/Kotlin/Tutatores_KMP/desktop/demo.png"), PNG)
//    val bb = localVfs("C:/Kotlin/Tutatores_KMP/desktop/test.png").readBitmap()
    val bb = localVfs("C:/Kotlin/Tutatores_KMP/desktop/avatarMain.jpg").readBitmap()
        .resized(156, 156, ScaleMode.COVER, Anchor.CENTER)
//    Bitmap.writeTo() .CompressFormat.PNG
//    bb.writeTo(localVfs("C:/Kotlin/Tutatores_KMP/desktop/demo2.png"), PNG)
//    bb.writeTo(localVfs("C:/Kotlin/Tutatores_KMP/desktop/demo2.png"), JPG)
//    bb.writeTo(localVfs("C:/Kotlin/Tutatores_KMP/desktop/demoRRRR.jpg"), BMP,ImageEncodingProps(extra = localVfs("C:/Kotlin/Tutatores_KMP/desktop/avatarMain.jpg").extra))
/*
    val fileJpg = FileMP()
    fileJpg.openFileOutput("C:/Kotlin/Tutatores_KMP/desktop/demo222.jpg")
    fileJpg.writeFile(
        bb.encode(PNG, ImageEncodingProps("C:/Kotlin/Tutatores_KMP/desktop/demo222.jpg", 0.51))
    )
    fileJpg.closeFile()
*/
    fff(bb.encode(PNG, ImageEncodingProps()))
}

suspend fun imageSliceForIcon(
    file: String,
    start: Float,
    top: Float,
    width: Float,
    height: Float,
    maxSize: Int?,
    square: Boolean
): Pair<ByteArray, ImageOrientation> { //NativeImage {
//    val filell = localVfs(file).extra
//    filell
    var exif = ImageOrientation.ORIGINAL
    try {
        if (localVfs(file).extension == "jpg" || localVfs(file).extension == "jpeg")
            exif = EXIF.readExifFromJpeg(localVfs(file)).orientation ?: ImageOrientation.ORIGINAL
    } catch (_: Exception) {

    }
    println("exif?.rotation: ${exif?.rotation}")
    println("exif?.flipX = ${exif?.flipX}")
    println("exif?.flipY = ${exif?.flipY}")
//    val hh = localVfs(file).readBitmap().flipX()// Slice()
//    hh.rotatedRight().
//        .withImageOrientation(exif!!)
    var bb =
        localVfs(file).readBitmap() //SliceWithOrientation()// .get().bmpBase // bmpBase // .base // .withImageOrientation().base
//    var rr = localVfs(file).readBitmapSlice()// .get().bmpBase // bmpBase // .base // .withImageOrientation().base


//    var bb = localVfs(file).readBitmapSliceWithOrientation().get().bmpBase // bmpBase // .base // .withImageOrientation().base
//    bb.slice().tl_x = 1f // .transformed(Matrix()).extract()
//    bb.slice().set
//    println("file = ${file.size}")
    var startOrien = start
    var topOrien = top
    var widthOrien = width
    var heightOrien = height


    when (exif.rotation) {
        ImageOrientation.Rotation.R0 -> {

        }
        ImageOrientation.Rotation.R90 -> {
            startOrien = top
            topOrien = 1 - start - width
            widthOrien = height
            heightOrien = width
        }
        ImageOrientation.Rotation.R180 -> {
            startOrien = 1 - start - width
            topOrien = 1 - top - height
            widthOrien = width
            heightOrien = height
        }
        ImageOrientation.Rotation.R270 -> {
            startOrien = 1 - top - height
            topOrien = start
            widthOrien = height
            heightOrien = width
        }
    }

    println("bb.size = ${bb.size}")
    val st = (bb.width * startOrien).toInt()
    val t = (bb.height * topOrien).toInt()
    var widthSl: Int = (bb.width * widthOrien).toInt()
    if (widthSl + st > bb.width) widthSl = bb.width - st
    var heightSl: Int = if (square) widthSl else (bb.height * heightOrien).toInt()
    if (heightSl + t > bb.height) heightSl = bb.height - t

//    if (exif.flipX) bb = bb.flippedX()
//    if (exif.flipY) bb = bb.flippedY()
//    if (exif.rotation == ImageOrientation.Rotation.R90 || exif.rotation == ImageOrientation.Rotation.R270){
//        val tmp = widthSl
//        widthSl = heightSl
//        heightSl = tmp
//    }

    val rect = RectangleInt(st, t, widthSl, heightSl)
    println("widthSl = ${widthSl}")
    println("heightSl = ${heightSl}")
//    ff.copy(ff.base,1f,2f,1f,2f,1f,2f,1f,2f)
//    val asdf = BitmapSlice(ff.base,rect, rotated = true).extract()
    var sliceImg = BitmapSlice(bb, rect).extract() // = bb.slice(rect).extract()

    maxSize?.let {
        if (sliceImg.width > sliceImg.height) {
            if (sliceImg.width > maxSize) sliceImg = sliceImg.resized(maxSize, maxSize*sliceImg.height/sliceImg.width, ScaleMode.FIT, Anchor.CENTER)
        } else {
            if (sliceImg.height > maxSize) sliceImg = sliceImg.resized(maxSize*sliceImg.width/sliceImg.height, maxSize, ScaleMode.FIT, Anchor.CENTER)
        }
    }
    return sliceImg.encode(PNG) to exif
//    return sliceImg.ensureNative() //.encode(PNG)
}