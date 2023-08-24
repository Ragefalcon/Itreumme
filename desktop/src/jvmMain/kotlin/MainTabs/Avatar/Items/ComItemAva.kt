package MainTabs.Avatar.Items


import MainTabs.imageFromFile
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import common.MyButtDropdownMenuStyle2
import common.MyCardStyle2
import common.SingleSelectionType
import extensions.RowVA
import java.io.File

class ComItemAva(
    val item: File,
    val selection: SingleSelectionType<File>,
    val doubleClick: (File) -> Unit = {},
    val dropMenu: @Composable ColumnScope.(File, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    var expandedDropMenu = mutableStateOf(false)

    val size = 300.dp
    val shape = RoundedCornerShape(15.dp) //CircleShape

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun getComposable() {
        MyCardStyle2(selection.isActive(item), 0, {
            selection.selected = item
        }, onDoubleClick = {
            doubleClick(item)
        },
            widthBorder = 1.5.dp,
//            backColor = MyColorARGB.colorMyMainTheme.toColor(),
            dropMenu = { exp -> dropMenu(item, exp) }
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                RowVA {
                    if (item.exists()) imageFromFile(item).let { bitmapAva ->
                        Image(
                            bitmap = bitmapAva,
                            "defaultAvatar",
                            Modifier.wrapContentSize()
                                .padding(20.dp)
                                .height(size)
                                .width(size)
                                .clip(shape)
                                .border(1.dp, Color.White, shape)
                                .padding(1.dp)
                                .border(3.dp, Color(0x7FFFF7D9), RoundedCornerShape(14.dp))
                                .shadow(2.dp, shape),
                            contentScale = ContentScale.Crop,// Fit,
                        )
                    }
                    if (selection.isActive(item)) MyButtDropdownMenuStyle2(
                        Modifier.padding(end = 10.dp),
                        expandedDropMenu
                    ) { //setDissFun ->
                        dropMenu(item, expandedDropMenu)
                    }
                }
            }
        }
    }
}