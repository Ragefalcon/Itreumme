package MainTabs.Finance.Items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
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
            ).width(15.dp).height(15.dp)
        )
        Text(
            " - ",
            Modifier.padding(horizontal = 10.dp),
            style = textTire
        )
        Text(item.name, style = textSt)
    }
}