package common


import MainTabs.imageFromFile
import MyListRow
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import extensions.RowVA
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.source.disk.getValue
import viewmodel.MainDB
import viewmodel.StateVM
import java.io.File

fun PanViewImageList(
    fileList: List<File>,
    selection: File? = null
) {
    val selectFile = SingleSelectionType<File>().apply {
        this.selected = selection ?: fileList.firstOrNull()
    }

    StateVM.dialLayForViewPicture.dial = @Composable {
        Box(Modifier.fillMaxSize().background(Color.DarkGray.copy(0.99f)), contentAlignment = Alignment.Center) {
            if (MainDB.styleParam.commonParam.OLD_PAPER.getValue()) Image(
                BitmapPainter(useResource("desk_paper_back.png", ::loadImageBitmap)),
                "paperback",
                Modifier.fillMaxSize(),
                alpha = 0.6F,
                contentScale = ContentScale.FillBounds
            )
            Column(
                Modifier.fillMaxWidth(0.95f).fillMaxHeight(0.95f).padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier
                        .weight(1f)
                        .padding(bottom = 15.dp)
                        .clickable(remember { MutableInteractionSource() }, null) {
                            fileList.indexOf(selectFile.selected).let { indexSel ->
                                selectFile.selected =
                                    if (indexSel < fileList.count() - 1) fileList[indexSel + 1] else fileList[0]
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    selectFile.selected?.let { selFile ->

                        val imageIB: MutableState<ImageBitmap?> = mutableStateOf<ImageBitmap?>(null).apply {

                            if (selFile.exists()) {
                                value = imageFromFile(selFile)
                            }
                        }

                        imageIB.value?.let { imgBtm ->
                            Image(
                                bitmap = imgBtm,
                                "defaultAvatar",
                                Modifier
                                    .padding(5.dp)
                                    .wrapContentSize()
                                    .border(
                                        1.dp,
                                        Color.White
                                    )
                                    .border(
                                        2.dp,
                                        Color.Black.copy(0.9f)
                                    ),
                                contentScale = ContentScale.Fit,
                            )
                        }
                    }
                }
                RowVA(Modifier.height(100.dp).fillMaxWidth()) {
                    MyTextButtWithoutBorder(
                        "\uD83E\uDC44",
                        Modifier.padding(end = 10.dp),
                        fontSize = 24.sp,
                        textColor = MyColorARGB.colorMyBorderStroke.toColor()
                    ) {
                        fileList.indexOf(selectFile.selected).let { indexSel ->
                            selectFile.selected =
                                if (indexSel > 0) fileList[indexSel - 1] else fileList.lastOrNull()
                        }
                    }
                    MyListRow(fileList, Modifier.weight(1f)) { ind, item ->
                        val imageIB: MutableState<ImageBitmap?> = mutableStateOf<ImageBitmap?>(null).apply {

                            if (item.exists()) {
                                value = imageFromFile(item)
                            }
                        }
                        imageIB.value?.let { imgBtm ->
                            Image(
                                bitmap = imgBtm,
                                "defaultAvatar",
                                Modifier
                                    .padding(5.dp)
                                    .wrapContentSize()
                                    .border(
                                        1.dp,
                                        if (item == selectFile.selected) Color.White.copy(0.5f) else Color.Gray.copy(
                                            0.5f
                                        )
                                    )
                                    .clickable {
                                        selectFile.selected = item
                                    },
                                contentScale = ContentScale.Fit,
                            )
                        }
                    }
                    MyTextButtWithoutBorder(
                        "\uD83E\uDC46",
                        Modifier.padding(start = 10.dp),
                        fontSize = 24.sp,
                        textColor = MyColorARGB.colorMyBorderStroke.toColor()
                    ) {
                        fileList.indexOf(selectFile.selected).let { indexSel ->
                            selectFile.selected =
                                if (indexSel < fileList.count() - 1) fileList[indexSel + 1] else fileList[0]
                        }
                    }
                    MyTextButtWithoutBorder(
                        "❌",
                        Modifier.padding(start = 10.dp),
                        fontSize = 24.sp,
                        textColor = MyColorARGB.colorMyBorderStroke.toColor()
                    ) {
                        StateVM.dialLayForViewPicture.close()
                    }
                }
            }
        }
    }
    if (fileList.isNotEmpty()) StateVM.dialLayForViewPicture.show()
}
