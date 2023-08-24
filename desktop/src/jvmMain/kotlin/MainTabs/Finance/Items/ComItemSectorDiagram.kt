package MainTabs.Finance.Items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.MyTextStyleParam
import extensions.toColor
import ru.ragefalcon.sharedcode.models.data.ItemSectorDiag

@Composable
fun ComItemSectorDiagram(
    item: ItemSectorDiag,
    textTire: TextStyle,
    textSt: TextStyle
) {
    Row(Modifier.padding(horizontal = 10.dp, vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.background(
                color = item.color.toMyIntCol().plusWhite().toColor(),
//                shape = RoundedCornerShape(5.dp)
            ).width(15.dp).height(15.dp)
//                .border(2.dp,item.color.toMyIntCol().toColor(),RoundedCornerShape(5.dp))
        )
        Text(
            " - ",
            Modifier.padding(horizontal = 10.dp),
            style = textTire
        )// MyTextStyleParam.style2.copy(fontSize = 13.sp ))
        Text(item.name, style = textSt)//  MyTextStyleParam.style2.copy( fontSize = 13.sp))
    }
}