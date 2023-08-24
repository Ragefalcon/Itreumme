package MainTabs

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.soywiz.korim.awt.toBufferedImage
import com.soywiz.korim.format.ImageOrientation
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.ImageWriter
import javax.imageio.stream.FileImageOutputStream


fun imageFromFile(file: File): ImageBitmap {
    return imageFromByteArray(file.readBytes())
//    return org.jetbrains.skia.Image.makeFromEncoded(file.readBytes()).toComposeImageBitmap()
}

fun imageFromByteArray(byteArray: ByteArray): ImageBitmap {
    return org.jetbrains.skia.Image.makeFromEncoded(byteArray).toComposeImageBitmap()
}

fun imageFromFileScaleTest(file: File): ImageBitmap {
    val buff = ImageIO.read(file)
    scaleBufferedImage(buff)?.let {
        return it.toComposeImageBitmap()
    }
    return imageFromByteArray(file.readBytes())
//    return org.jetbrains.skia.Image.makeFromEncoded(file.readBytes()).toComposeImageBitmap()
}

/*

Похоже конвертация в ByteArray проходит не очень гладко,
да и в представленной реализации она выглядит слишком ресурсозатратной...

fun imageFromBufferedImage(bufferedImage: BufferedImage): ImageBitmap {
    return org.jetbrains.skia.Image.makeFromEncoded(BufferedImageToBytes(bufferedImage)).toComposeImageBitmap()
}

// creating byte array from BufferedImage
fun BufferedImageToBytes(image: BufferedImage): ByteArray {
    val width = image.width
    val height = image.height

    val buffer = IntArray(width * height)
    image.getRGB(0, 0, width, height, buffer, 0, width)

    val pixels = ByteArray(width * height * 4)

    var index = 0
    for (y in 0 until height) {
        for (x in 0 until width) {
            val pixel = buffer[y * width + x]
            pixels[index++] = ((pixel and 0xFF)).toByte() // Blue component
            pixels[index++] = (((pixel shr 8) and 0xFF)).toByte() // Green component
            pixels[index++] = (((pixel shr 16) and 0xFF)).toByte() // Red component
            pixels[index++] = (((pixel shr 24) and 0xFF)).toByte() // Alpha component
        }
    }

    return pixels
}
*/

/** https://www.geeksforgeeks.org/java-program-to-rotate-an-image/ */
fun rotateBufferedImage(img: BufferedImage, orientation: ImageOrientation): BufferedImage? {

    // Getting Dimensions of image
    var width = img.width
    var height = img.height



    // Rotating image by degrees using toradians()
    // method
    // and setting new dimension t it
    var angle = 0.0
    when (orientation.rotation) {
        ImageOrientation.Rotation.R0 -> {
        }
        ImageOrientation.Rotation.R90 -> {
            height = img.width
            width = img.height
            angle = 90.0
        }
        ImageOrientation.Rotation.R180 -> {
            angle = 180.0
        }
        ImageOrientation.Rotation.R270 -> {
            height = img.width
            width = img.height
            angle = 270.0
        }
    }

    // Creating a new buffered image
    val newImage = BufferedImage(
        width,
        height,
        img.type
    )

    // creating Graphics in buffered image
    val g2 = newImage.createGraphics()

    println("width: $width")
    println("height: $height")
//    angle = 0.0
    g2.rotate(
        Math.toRadians(angle),
//        0.0,
//        width.toDouble(),
        (img.width / 2).toDouble(),
        (img.height / 2).toDouble(),
//        height.toDouble()
//        (width - height / 2).toDouble(),
//        (width / 2).toDouble(),
    )
    g2.translate(-(width - height)/2.0,-(width - height)/2.0)
    g2.drawImage(img, null, 0, 0)

    // Return rotated buffer image
    return newImage
}

fun scaleBufferedImage(img: BufferedImage): BufferedImage? {

    // Getting Dimensions of image
    val width = img.width
    val height = img.height

    val koef = 0.1
    // Creating a new buffered image
    val newImage = BufferedImage(
        (img.width * koef).toInt(), (img.height * koef).toInt(), img.type
    )

    // creating Graphics in buffered image
    val g2 = newImage.createGraphics()

    // Rotating image by degrees using toradians()
    // method
    // and setting new dimension t it

    g2.scale(koef, koef)
    g2.drawImage(img, null, 0, 0)

    // Return rotated buffer image
    return newImage
}

fun imageByteToBufferImage(imageData: Pair<ByteArray, ImageOrientation>): BufferedImage? {
    val bais = ByteArrayInputStream(imageData.first)
    val img1 = ImageIO.read(bais)
    val size = Dimension(img1.width, img1.height)

    // imageFromFile(iconFile.value).asSkiaBitmap().toBufferedImage()

    val img = BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB)
    img.createGraphics().drawImage(img1, 0, 0, null)
//    for (x in 0 until size.width) {
//        for (y in 0 until size.height) {
//            img.setRGB(x, y, img1.getRGB(x, y))
//        }
//    }
    return if (imageData.second != ImageOrientation.ORIGINAL) rotateBufferedImage(img, imageData.second) else img
}


fun saveImage(image: BufferedImage?, jpegFiletoSave: File?, quality: Float) {
    // save jpeg image with specific quality. "1f" corresponds to 100% , "0.7f" corresponds to 70%
    val jpgWriter: ImageWriter = ImageIO.getImageWritersByFormatName("jpeg").next()
    val jpgWriteParam: ImageWriteParam = jpgWriter.getDefaultWriteParam() //JPEGImageWriteParam(null) //
    jpgWriteParam.compressionMode = ImageWriteParam.MODE_EXPLICIT
    jpgWriteParam.compressionQuality = quality

//    jpgWriter.setOutput(ImageIO.createImageOutputStream(jpegFiletoSave))
    val ios = FileImageOutputStream(jpegFiletoSave)
    jpgWriter.setOutput(ios)
//    jpgWriter.write(null, IIOImage(image, null, null), jpgWriteParam)
//
//    jpgWriter.d
    val outputImage = IIOImage(image, null, null)
    jpgWriter.write(null, outputImage, jpgWriteParam)
    jpgWriter.dispose()
    ios.flush()
    ios.close()

}
private fun removeAlphaChannel(img: BufferedImage): BufferedImage {
    if (!img.colorModel.hasAlpha()) {
        return img
    }
    val target = createImage(img.width, img.height, false)
    val g = target.createGraphics()
    // g.setColor(new Color(color, false));
    g.fillRect(0, 0, img.width, img.height)
    g.drawImage(img, 0, 0, null)
    g.dispose()
    return target
}

private fun createImage(width: Int, height: Int, hasAlpha: Boolean): BufferedImage {
    return BufferedImage(width, height, if (hasAlpha) BufferedImage.TYPE_INT_ARGB else BufferedImage.TYPE_INT_RGB)
}