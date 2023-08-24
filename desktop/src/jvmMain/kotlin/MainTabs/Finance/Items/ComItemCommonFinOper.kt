package MainTabs.Finance.Items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import ru.ragefalcon.sharedcode.models.data.ItemCommonFinOper
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComItemCommonFinOper(
    item: ItemCommonFinOper,
    selection: SingleSelectionType<ItemCommonFinOper>,
    itemRasxDoxStyleState: ItemRasxDoxOperStyleState,
    dropMenu: @Composable ColumnScope.(ItemCommonFinOper, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    val expandedDropMenu = remember { mutableStateOf(false) }
    with(itemRasxDoxStyleState) {
        MyCardStyle1(selection.isActive(item), 0, {
            selection.selected = item
        },
            dropMenu = { exp -> dropMenu(item, exp) },
            styleSettings = itemRasxDoxStyleState
        ) {
            RowVA(modifier = androidx.compose.ui.Modifier.padding(2.dp).padding(start = 15.dp, end = 10.dp)) {
                Column(androidx.compose.ui.Modifier.padding(0.dp)) {
                    RowVA {

                        Text(
                            modifier = androidx.compose.ui.Modifier.padding(end = 0.dp),
                            text = item.type,
                            style = textType
                        )
                    }
                    Text(
                        modifier = androidx.compose.ui.Modifier.padding(0.dp),
                        text = item.schet,
                        style = textSchet
                    )
                }
                Text(
                    modifier = androidx.compose.ui.Modifier.padding(start = 10.dp).weight(1f),
                    text = item.name,
                    style = mainTextStyle
                )
                if (selection.isActive(item)) MyButtDropdownMenuStyle2(
                    androidx.compose.ui.Modifier.padding(2.dp), expandedDropMenu,buttMenu,dropdown
                ) {
                    dropMenu(item, expandedDropMenu)
                }
                Column(androidx.compose.ui.Modifier.padding(start = 10.dp), horizontalAlignment = androidx.compose.ui.Alignment.End) {
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
                        modifier = androidx.compose.ui.Modifier.padding(top = 0.dp),
                        text = Date(item.data).format("dd MMM yyyy"),
                        style = textDate
                    )
                }
            }
        }
    }
}



/*
                            color = when (item.schet) {
                                "Расход" -> Color.Red.toMyColorARGB().plusWhite(1.3f)
                                    .toColor() // MyColorARGB.colorRasxodItem0.toColor()
                                "Доход" -> Color.Green.toMyColorARGB().plusWhite(1.3f)
                                    .toColor() //MyColorARGB.colorDoxodItem0.toColor()
                                "Перевод" -> if (item.summa > 0)
                                    Color.Yellow.toMyColorARGB().plusWhite(1.3f).toColor()
                                else MyColorARGB.colorRasxodTheme.toColor()
                                else -> Color(0xFFFFF7F9)
                            }
*/
