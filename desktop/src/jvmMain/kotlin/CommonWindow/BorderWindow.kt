package CommonWindow

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import common.cursorForHorizontalResize
import common.cursorForTopLeftResize
import common.cursorForTopRightResize
import common.cursorForVerticalResize

@Composable
fun BorderWindow(widthRamk: Dp) {
    Box(
        Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource("nineRamk_left.png"),
            contentDescription = "Sample",
            modifier = Modifier.fillMaxHeight().width(widthRamk).align(Alignment.CenterStart),
            contentScale = ContentScale.FillBounds// .Fit
        )
        Image(
            painter = painterResource("nineRamk_right.png"),
            contentDescription = "Sample",
            modifier = Modifier.fillMaxHeight().width(widthRamk).align(Alignment.CenterEnd),
            contentScale = ContentScale.FillBounds// .Fit
        )
        Image(
            painter = painterResource("nineRamk_top.png"),
            contentDescription = "Sample",
            modifier = Modifier.fillMaxWidth().height(widthRamk).align(Alignment.TopCenter),
            contentScale = ContentScale.FillBounds// .Fit
        )
        Image(
            painter = painterResource("nineRamk_bottom.png"),
            contentDescription = "Sample",
            modifier = Modifier.fillMaxWidth().height(widthRamk).align(Alignment.BottomCenter),
            contentScale = ContentScale.FillBounds// .Fit
        )
        Image(
            painter = painterResource("nineRamk_bottomRight.png"),
            contentDescription = "Sample",
            modifier = androidx.compose.ui.Modifier.height(widthRamk).width(widthRamk).align(Alignment.BottomEnd),
            contentScale = ContentScale.FillBounds// .Fit
        )
        Image(
            painter = painterResource("nineRamk_bottomLeft.png"),
            contentDescription = "Sample",
            modifier = androidx.compose.ui.Modifier.height(widthRamk).width(widthRamk).align(Alignment.BottomStart),
            contentScale = ContentScale.FillBounds// .Fit
        )
        Image(
            painter = painterResource("nineRamk_topRight.png"),
            contentDescription = "Sample",
            modifier = androidx.compose.ui.Modifier.height(widthRamk).width(widthRamk).align(Alignment.TopEnd),
            contentScale = ContentScale.FillBounds// .Fit
        )
        Image(
            painter = painterResource("nineRamk_topLeft.png"),
            contentDescription = "Sample",
            modifier = androidx.compose.ui.Modifier.height(widthRamk).width(widthRamk).align(Alignment.TopStart),
            contentScale = ContentScale.FillBounds// .Fit
        )
    }
    Box(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxHeight().width(7.dp).cursorForHorizontalResize().align(Alignment.TopStart))
        Box(Modifier.fillMaxHeight().width(7.dp).cursorForHorizontalResize().align(Alignment.TopEnd))
        Box(Modifier.fillMaxWidth().height(7.dp).cursorForVerticalResize().align(Alignment.TopStart))
        Box(Modifier.fillMaxWidth().height(7.dp).cursorForVerticalResize().align(Alignment.BottomStart))
        Box(Modifier.width(7.dp).height(7.dp).cursorForTopLeftResize().align(Alignment.TopStart))
        Box(Modifier.width(7.dp).height(7.dp).cursorForTopLeftResize().align(Alignment.BottomEnd))
        Box(Modifier.width(7.dp).height(7.dp).cursorForTopRightResize().align(Alignment.TopEnd))
        Box(Modifier.width(7.dp).height(7.dp).cursorForTopRightResize().align(Alignment.BottomStart))
    }
}