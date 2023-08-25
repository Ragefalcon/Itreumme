package MainTabs.Quest.Items

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.mouseClickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import java.io.File

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun ComItemFileQuest(
    item: File,
    selection: SingleSelectionType<File>,
    edit: Boolean = true,
    onClick: (File) -> Unit = {},
    openFile: (File) -> Unit = {},
    dropMenu: @Composable ColumnScope.(File, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    val expandedDropMenu = remember { mutableStateOf(false) }
    val expandedDropMenuRightClick = mutableStateOf(false)

    MyCardStyle1(edit && selection.isActive(item), onClick = {
        selection.selected = item
        onClick(item)
    }, onDoubleClick = {
        selection.selected = item
        openFile(item)
    }, dropMenu = { exp -> dropMenu(item, exp) })
    {
        Row(Modifier.height(45.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.padding(horizontal = 5.dp).weight(1f),
                text = item.name,
                style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start, fontSize = 15.sp)

            )
            if (edit && selection.isActive(item)) MyButtDropdownMenuStyle1(
                Modifier.padding(start = 10.dp).padding(vertical = 5.dp), expandedDropMenu
            ) {
                dropMenu(item, expandedDropMenu)
            }
            MyTextStyle1(
                "\uD83D\uDD6E",
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .mouseClickable {
                        selection.selected = item
                        openFile(item)
                    }
            )
        }
    }
}
