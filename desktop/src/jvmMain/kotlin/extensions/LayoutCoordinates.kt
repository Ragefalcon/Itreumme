package extensions

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates

fun LayoutCoordinates.getAbsolutRect(): Rect {
    var layCoor = this.parentLayoutCoordinates ?: this
    while (layCoor.parentLayoutCoordinates != null) {
        layCoor.parentLayoutCoordinates?.let {
            layCoor = it
        }
    }
    return layCoor.localBoundingBoxOf(this)
}