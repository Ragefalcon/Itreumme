package ru.ragefalcon.sharedcode.tests

import com.soywiz.korim.bitmap.BitmapSlice
import com.soywiz.korim.bitmap.resized
import com.soywiz.korim.format.*
import com.soywiz.korio.file.extension
import com.soywiz.korio.file.std.localVfs
import com.soywiz.korma.geom.Anchor
import com.soywiz.korma.geom.RectangleInt
import com.soywiz.korma.geom.ScaleMode

suspend fun imageSliceForIcon(
    file: String,
    start: Float,
    top: Float,
    width: Float,
    height: Float,
    maxSize: Int?,
    square: Boolean
): Pair<ByteArray, ImageOrientation> {
    var exif = ImageOrientation.ORIGINAL
    try {
        if (localVfs(file).extension == "jpg" || localVfs(file).extension == "jpeg")
            exif = EXIF.readExifFromJpeg(localVfs(file)).orientation ?: ImageOrientation.ORIGINAL
    } catch (_: Exception) {

    }
    val bb =
        localVfs(file).readBitmap()

    var startOrien = start
    var topOrien = top
    var widthOrien = width
    var heightOrien = height


    when (exif.rotation) {
        ImageOrientation.Rotation.R0 -> Unit
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

    val st = (bb.width * startOrien).toInt()
    val t = (bb.height * topOrien).toInt()
    var widthSl: Int = (bb.width * widthOrien).toInt()
    if (widthSl + st > bb.width) widthSl = bb.width - st
    var heightSl: Int = if (square) widthSl else (bb.height * heightOrien).toInt()
    if (heightSl + t > bb.height) heightSl = bb.height - t

    val rect = RectangleInt(st, t, widthSl, heightSl)
    var sliceImg = BitmapSlice(bb, rect).extract()

    maxSize?.let {
        if (sliceImg.width > sliceImg.height) {
            if (sliceImg.width > maxSize) sliceImg =
                sliceImg.resized(maxSize, maxSize * sliceImg.height / sliceImg.width, ScaleMode.FIT, Anchor.CENTER)
        } else {
            if (sliceImg.height > maxSize) sliceImg =
                sliceImg.resized(maxSize * sliceImg.width / sliceImg.height, maxSize, ScaleMode.FIT, Anchor.CENTER)
        }
    }
    return sliceImg.encode(PNG) to exif
}