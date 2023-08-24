package common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun PlateOrderLayout(
    modifier: Modifier = Modifier,
    alignmentCenter: Boolean = false,
    alignmentEnd: Boolean = false,
    alignmentVertRowCenter: Boolean = true,
    koefMaxWidth: Float = 1f,
    fillWidth: Boolean = true,
    spaceBetween: Int = 0,
    children: @Composable () -> Unit
) = Layout({
    children()
}, modifier, measurePolicy = { measurables, constraints ->

    var rowWidth = 0
    var rowWidthMax = 0
    var rowHeight = 0
    var placeableHeightMax = 0
    var colHeight = 0
    var colHeightLayout = 0
    val placeableList: MutableList<Placeable> = mutableListOf()
    val placeableListPlaceFun: MutableList<Placeable.PlacementScope.() -> Unit> = mutableListOf()

    fun placeOneRow(widthRow: Int, heightRow: Int) {
        val tmpPlace: MutableList<Placeable> = mutableListOf()
        tmpPlace.addAll(placeableList)
        for (pl in tmpPlace) {
            rowHeight = maxOf(rowHeight, pl.height)
        }
        colHeightLayout += rowHeight + (if (colHeightLayout != 0) spaceBetween else 0)
        rowHeight = 0
        placeableListPlaceFun.add {
            var curX =
                if (alignmentCenter) ((if (fillWidth) (constraints.maxWidth * koefMaxWidth).toInt() else rowWidthMax) - widthRow) / 2 else if (alignmentEnd) (if (fillWidth) (constraints.maxWidth * koefMaxWidth).toInt() else rowWidthMax) - widthRow else 0
            for (pl in tmpPlace) {
                rowHeight = maxOf(rowHeight, pl.height)
                pl.place(
                    curX,
                    (if (alignmentVertRowCenter) colHeight + (heightRow - pl.height) / 2 else colHeight) + (if (colHeight != 0) spaceBetween else 0)
                )
//                if (alignmentEnd) curX -= pl.width else
                curX += pl.width + spaceBetween
            }
            colHeight += rowHeight + (if (colHeight != 0) spaceBetween else 0)
            rowHeight = 0
        }
        if (rowWidthMax < rowWidth) rowWidthMax = rowWidth
        rowWidth = 0
        placeableList.clear()
    }

    for (i in measurables.indices) {
        val curPlaceble = measurables[i].measure(constraints.copy(minWidth = 0, minHeight = 0))
        if (rowWidth == 0) {
            rowWidth += curPlaceble.width
            if (placeableHeightMax < curPlaceble.height) placeableHeightMax = curPlaceble.height
            placeableList.add(curPlaceble)
        } else {
            if (rowWidth + curPlaceble.width + spaceBetween > constraints.maxWidth * koefMaxWidth) {
                placeOneRow(rowWidth, placeableHeightMax)
                placeableHeightMax = 0
            }
            rowWidth += curPlaceble.width + spaceBetween
            if (placeableHeightMax < curPlaceble.height) placeableHeightMax = curPlaceble.height
            placeableList.add(curPlaceble)
        }
    }
    if (rowWidth > 0) {
        placeOneRow(rowWidth, placeableHeightMax)
    }

    layout(if (fillWidth) (constraints.maxWidth * koefMaxWidth).toInt() else rowWidthMax, colHeightLayout) {
        for (f in placeableListPlaceFun) {
            f.invoke(this)
        }
    }
}
)
