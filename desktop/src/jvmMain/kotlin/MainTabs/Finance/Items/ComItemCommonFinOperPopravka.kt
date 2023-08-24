package MainTabs.Finance.Items


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
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
import ru.ragefalcon.sharedcode.models.data.ItemCommonFinOper
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComItemCommonFinOperPopravka(
    item: ItemCommonFinOper,
    itemRasxDoxStyleState: ItemRasxDoxOperStyleState,
) {
    with(itemRasxDoxStyleState) {
        MyCardStyle1(
            false, 0,
            styleSettings = itemRasxDoxStyleState
        ) {
            RowVA(modifier = Modifier.padding(2.dp).padding(start = 15.dp, end = 10.dp)) {
                Text(
                    text = item.name,
                    style = mainTextStyle
                )
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = Date(item.data).format("dd MMM yyyy"),
                    style = textDate
                )
                Spacer(Modifier.weight(1f))
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
            }
        }
    }
}
