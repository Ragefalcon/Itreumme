package MainTabs.Avatar.Items

import MainTabs.imageFromFile
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.ItemBestDayStyleState
import extensions.RowVA
import extensions.format
import ru.ragefalcon.sharedcode.models.data.ItemBestDays
import viewmodel.StateVM
import java.io.File
import java.util.*

class ComItemBestDay(
    val item: ItemBestDays,
    val selection: SingleSelectionType<ItemBestDays>,
    val openDay: (ItemBestDays) -> Unit = {},
    val itemBestDayStyleState: ItemBestDayStyleState,
    val dropMenu: @Composable ColumnScope.(ItemBestDays, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    var expanded = mutableStateOf(false)

    var imageIB: MutableState<ImageBitmap?> = mutableStateOf<ImageBitmap?>(null).apply {
        if (item.enableIcon) {
            val ff = File(StateVM.dirBestDaysImages, "bestDay_${Date(item.data).format("yyyy_MM_dd")}.jpg")
            if (ff.exists()) {
                value = imageFromFile(ff)
            }
        }
    }

    @Composable
    fun getComposable() {
        with(itemBestDayStyleState) {
            MyCardStyle1(
                selection.isActive(item), 0, {
                    selection.selected = item
                }, onDoubleClick = { openDay(item) },
                styleSettings = itemBestDayStyleState,
                dropMenu = { exp -> dropMenu(item, exp) }
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    imageIB.value?.let { imgBtm ->
                        MyShadowBox(image_shadow) {
                            Image(
                                bitmap = imgBtm,
                                "defaultAvatar",
                                Modifier
                                    .padding(bottom = 5.dp)
                                    .wrapContentSize()
                                    .clip(image_shape)
                                    .border(image_border_width, image_border_brush, image_shape)
                                    .alpha(0.8f),
                                contentScale = ContentScale.Fit,
                                colorFilter = if (BLACKandWHITE) ColorFilter.tint(TINT, BlendMode.Color) else null
                            )
                        }
                    }
                    RowVA {
                        Column(Modifier.padding(0.dp).weight(1f)) {
                            Text(
                                modifier = Modifier.padding(0.dp),
                                text = Date(item.data).format("dd.MM.yyyy (EEE)"),
                                style = dateText
                            )
                            Text(
                                modifier = Modifier.padding(0.dp),
                                text = item.name,
                                style = mainTextStyle
                            )
                        }
                        if (selection.isActive(item)) MyButtDropdownMenuStyle2(
                            Modifier.padding(end = 0.dp).padding(vertical = 0.dp), expanded, buttMenu
                        ) {
                            dropMenu(item, expanded)
                        }
                        if (selection.isActive(item)) MyTextButtSimpleStyle(
                            text = "\uD83D\uDD6E",
                            modifier = Modifier
                                .padding(horizontal = 10.dp),
                            fontSize = 20.sp,
                            color = open_butt_color
                        ) {
                            openDay(item)
                        }
                    }
                }
            }
        }
    }
}



