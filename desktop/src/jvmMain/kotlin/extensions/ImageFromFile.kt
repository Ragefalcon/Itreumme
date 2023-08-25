package MainTabs

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
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
}


/** https://www.geeksforgeeks.org/java-program-to-rotate-an-image/ */
fun rotateBufferedImage(img: BufferedImage, orientation: ImageOrientation): BufferedImage? {

    var width = img.width
    var height = img.height
    var angle = 0.0

    when (orientation.rotation) {
        ImageOrientation.Rotation.R0 -> Unit
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


    val newImage = BufferedImage(
        width,
        height,
        img.type
    )


    val g2 = newImage.createGraphics()

    g2.rotate(
        Math.toRadians(angle),


        (img.width / 2).toDouble(),
        (img.height / 2).toDouble(),


        )
    g2.translate(-(width - height) / 2.0, -(width - height) / 2.0)
    g2.drawImage(img, null, 0, 0)


    return newImage
}

fun scaleBufferedImage(img: BufferedImage): BufferedImage? {
    val koef = 0.1

    val newImage = BufferedImage(
        (img.width * koef).toInt(), (img.height * koef).toInt(), img.type
    )

    val g2 = newImage.createGraphics()
    g2.scale(koef, koef)
    g2.drawImage(img, null, 0, 0)
    return newImage
}

fun imageByteToBufferImage(imageData: Pair<ByteArray, ImageOrientation>): BufferedImage? {
    val bais = ByteArrayInputStream(imageData.first)
    val img1 = ImageIO.read(bais)
    val size = Dimension(img1.width, img1.height)
    val img = BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB)
    img.createGraphics().drawImage(img1, 0, 0, null)

    return if (imageData.second != ImageOrientation.ORIGINAL) rotateBufferedImage(img, imageData.second) else img
}


fun saveImage(image: BufferedImage?, jpegFiletoSave: File?, quality: Float) {

    val jpgWriter: ImageWriter = ImageIO.getImageWritersByFormatName("jpeg").next()
    val jpgWriteParam: ImageWriteParam = jpgWriter.getDefaultWriteParam()
    jpgWriteParam.compressionMode = ImageWriteParam.MODE_EXPLICIT
    jpgWriteParam.compressionQuality = quality

    val ios = FileImageOutputStream(jpegFiletoSave)
    jpgWriter.setOutput(ios)

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

    g.fillRect(0, 0, img.width, img.height)
    g.drawImage(img, 0, 0, null)
    g.dispose()
    return target
}

private fun createImage(width: Int, height: Int, hasAlpha: Boolean): BufferedImage {
    return BufferedImage(width, height, if (hasAlpha) BufferedImage.TYPE_INT_ARGB else BufferedImage.TYPE_INT_RGB)
}