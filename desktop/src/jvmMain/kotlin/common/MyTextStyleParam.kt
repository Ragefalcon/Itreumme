package common

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB

class MyTextStyleParam() {
    companion object {
        val style1 = TextStyle(
            color = Color(0xFFFFF7D9),
            fontSize = 20.sp,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Center,
            shadow = Shadow(
                color = Color.Black,
                offset = Offset(4f, 4f),
                blurRadius = 4f
            ),
        )
        val style2 = TextStyle(
            color = Color(0xFFFFF7D9),
            fontSize = 20.sp,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Start,
            shadow = Shadow(
                color = Color.Black.copy(alpha = 0.7f),
                offset = Offset(2f, 2f),
                blurRadius = 4f
            ),
        )
        val style3 = TextStyle(
            color = Color(0xFFFFF7D9),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Start,
        )
        val style4 = TextStyle(
            color = Color(0xFFFFF7D9),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Start,
            shadow = Shadow(
                color = Color.White.copy(alpha = 0.4f),
                offset = Offset(1f, 1f),
                blurRadius = 2f
            ),
        )
        val style5 = TextStyle(
            color = MyColorARGB.colorBackRamk.plusWhite().toColor(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Start,
            shadow = Shadow(
                color = Color.White.copy(alpha = 0.3f),
                offset = Offset(1f, 1f),
                blurRadius = 1f
            ),
        )
    }
}