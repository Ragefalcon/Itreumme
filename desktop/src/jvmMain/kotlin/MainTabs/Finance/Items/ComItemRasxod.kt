package MainTabs.Finance.Items


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import com.soywiz.korio.lang.substr
import common.MyButtDropdownMenuStyle2
import common.MyCardStyle1
import common.SingleSelectionType
import extensions.ItemRasxDoxOperStyleState
import extensions.RowVA
import extensions.format
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemRasxod
import java.util.*

@Composable
fun ComItemRasxod(
    item: ItemRasxod,
    selection: SingleSelectionType<ItemRasxod>,
    itemRasxDoxStyleState: ItemRasxDoxOperStyleState,
    dropMenu: @Composable ColumnScope.(ItemRasxod, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    val expandedDropMenu = remember { mutableStateOf(false) }
    with(itemRasxDoxStyleState) {
        MyCardStyle1(
            selection.isActive(item), 0, {
                selection.selected = item
            },
            dropMenu = { exp -> dropMenu(item, exp) },
            styleSettings = itemRasxDoxStyleState
        ) {
            RowVA(modifier = Modifier.padding(2.dp).padding(start = 15.dp, end = 10.dp)) {
                Column(Modifier.padding(0.dp)) {
                    RowVA {

                        Text(
                            modifier = Modifier.padding(end = 0.dp),
                            text = item.type,
                            style = textType
                        )
                    }
                    Text(
                        modifier = Modifier.padding(0.dp),
                        text = item.schet,
                        style = textSchet
                    )
                }
                Text(
                    modifier = Modifier.padding(start = 10.dp).weight(1f),
                    text = item.name,
                    style = mainTextStyle
                )
                if (selection.isActive(item)) MyButtDropdownMenuStyle2(
                    Modifier.padding(2.dp), expandedDropMenu, buttMenu, dropdown
                ) {
                    dropMenu(item, expandedDropMenu)
                }
                Column(Modifier.padding(start = 10.dp), horizontalAlignment = Alignment.End) {
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
                        modifier = Modifier.padding(top = 0.dp),
                        text = Date(item.data).format("dd MMM yyyy"),
                        style = textDate
                    )
                }
            }
        }
    }
}

