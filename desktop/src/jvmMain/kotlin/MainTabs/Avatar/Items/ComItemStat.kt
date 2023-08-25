package MainTabs.Avatar.Items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.MyShadowBox
import extensions.ItemStatusStyleState
import extensions.RowVA
import ru.ragefalcon.sharedcode.models.data.ItemStat

class ComItemStat(
    val item: ItemStat,
    val style: ItemStatusStyleState
) {

    @Composable
    fun getComposable() {
        MyShadowBox(style.shadow) {
            Box(
                modifier = style.outer_padding
                    .fillMaxWidth()
                    .background(style.BACKGROUND, style.shape)
                    .border(
                        width = style.BORDER_WIDTH,
                        brush = style.BORDER,
                        shape = style.shape
                    ),
            ) {
                RowVA(style.inner_padding) {
                    Text(
                        modifier = Modifier.padding(start = 20.dp).weight(1f),
                        text = item.name,
                        style = style.mainText
                    )
                    Text(
                        modifier = Modifier.padding(end = 20.dp),
                        text = item.value,
                        style = style.valueText
                    )
                }
            }
        }
    }
}
