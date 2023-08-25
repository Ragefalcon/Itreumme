package MainTabs.Finance.Items

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import com.soywiz.korio.lang.substr
import common.MyShadowBox
import extensions.ItemSchetGrafState
import extensions.withSimplePlate
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemSumOnSchet
import viewmodel.MainDB

@Composable
fun ComItemSumOnSchet(
    item: ItemSumOnSchet,
    style: ItemSchetGrafState,
    onClick: (ItemSumOnSchet) -> Unit = {}
) {

    with(style) {
        MyShadowBox(plateMain.shadow) {
            Column(outer_padding
                .run {
                    if (item.id == MainDB.CB_spisSchet.getSelected()?.id?.toLong()) this.border(
                        border_width_active,
                        borderActive,
                        plateMain.shape
                    ) else this
                }
                .withSimplePlate(plateMain)
                .clickable { onClick(item) }
                .then(inner_padding)
                .fillMaxWidth(),
                horizontalAlignment = Alignment.Start) {
                Row(Modifier.padding(bottom = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        item.name,
                        style = textName,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = AnnotatedString(
                            text = item.summa.roundToStringProb(2).let { it.substr(0, it.length - 2) },
                            spanStyle = SpanStyle(fontSize = textSumm.fontSize)
                        ).plus(
                            AnnotatedString(
                                text = item.summa.roundToStringProb(2).let { it.substr(it.length - 2, 2) },
                                spanStyle = SpanStyle(fontSize = textSumm.fontSize * 0.7f)
                            )
                        ),
                        style = textSumm
                    )
                    Text(
                        modifier = Modifier.padding(start = 3.dp),
                        text = item.valut,
                        style = textValut
                    )
                }
                val minus = item.summa < 0
                val width = if (minus) -item.procent else item.procent
                MyShadowBox(if (minus) platePolosMinus.shadow else platePolos.shadow) {
                    Box(
                        Modifier
                            .withSimplePlate(if (minus) platePolosMinus else platePolos)
                            .fillMaxWidth(width.toFloat()).height(10.dp)
                    )
                }
            }
        }
    }
}