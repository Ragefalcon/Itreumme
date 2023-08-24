package extensions


import java.awt.Image
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.IOException


fun setClipboard(s: String) {
    val selection = StringSelection(s)
    val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(selection, selection)
//    var aa = ImageBitmap()
//    clipboard.getContents(aa)
//    clipboard.se
}

/**
 * Get an image off the system clipboard.
 *
 * @return Returns an Image if successful; otherwise returns null.
 */
fun getImageFromClipboard(): Image? {
    val transferable = Toolkit.getDefaultToolkit().systemClipboard.getContents(null)
    if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
        try {
            return transferable.getTransferData(DataFlavor.imageFlavor) as Image
        } catch (e: UnsupportedFlavorException) {
            // handle this as desired
            e.printStackTrace()
        } catch (e: IOException) {
            // handle this as desired
            e.printStackTrace()
        }
    } else {
        System.err.println("getImageFromClipboard: That wasn't an image!")
    }
    return null
}