package MainTabs.Avatar.Items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.Text
import androidx.compose.ui.draw.shadow
import common.MyShadowBox
import common.MyTextStyleParam
import extensions.ItemStatusStyleState
import extensions.RowVA
import extensions.paddingStyle
import ru.ragefalcon.sharedcode.models.data.ItemStat

class ComItemStat(
    val item: ItemStat,
    val style: ItemStatusStyleState
) {

    @Composable
    fun getComposable() {
        MyShadowBox(style.shadow){
            Box(
                modifier = style.outer_padding // Modifier
//                .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .background(style.BACKGROUND,style.shape)
                    .border(
                        width = style.BORDER_WIDTH, // 0.5.dp
                        brush = style.BORDER,
//                            Brush.horizontalGradient(
//                        listOf(Color(0x7FFFF7D9), Color(0x7FFFF7D9))
//                    ),
                        shape = style.shape//  RoundedCornerShape(15.dp)
                    )

                ,
//            elevation = style.ELEVATION_CARD,// 8.dp,
//            backgroundColor = Color(0xFF464D45),
//            shape = style.shape.shadow// RoundedCornerShape(corner = CornerSize(15.dp))
            ) {
                RowVA(style.inner_padding){
//                Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
//                verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        modifier = Modifier.padding(start = 20.dp).weight(1f),
                        text = item.name,
                        style = style.mainText //  MyTextStyleParam.style1.copy( textAlign = TextAlign.Start, fontSize = 14.sp )
                    )
                    Text(
                        modifier = Modifier.padding(end = 20.dp),
                        text = item.value,
                        style = style.valueText //MyTextStyleParam.style1.copy( textAlign = TextAlign.End, fontSize = 14.sp )
                    )
                }
            }
        }
    }
}
