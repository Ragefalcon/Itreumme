package MainTabs.Quest.Items

import MainTabs.imageFromFile
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.BoxWithVScrollBar
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemGovorun
import java.io.File

class ComItemGovorun(
    val item: ItemGovorun,
    val selection: SingleSelection,
    val dirIcon: String,
    val dropMenu: @Composable ColumnScope.(ItemGovorun, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    var expandedDropMenu = mutableStateOf(false)
    val avatarFile = File(dirIcon, "${item.image_file}.jpg")
    val avatarF =
        if (avatarFile.exists()) imageFromFile(avatarFile) else useResource("iv_avatar.png", ::loadImageBitmap)

    val shape = RoundedCornerShape(75.dp)

    @Composable
    fun getComposable() {
        val expandedOpis = remember { mutableStateOf(!item.sver) }
        MyCardStyle1(selection.isActive(item), 0, {
            selection.selected = item
        }, {
            item.sver = item.sver.not()
            expandedOpis.value = !expandedOpis.value
        }, dropMenu = { exp -> dropMenu(item, exp) }
        ) {
            Row(
                Modifier.padding(horizontal = 10.dp).padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box {
                        Image(
                            bitmap = avatarF,
                            "defaultAvatar",
                            Modifier.wrapContentSize().padding(horizontal = 20.dp)
                                .height(100.dp)
                                .width(100.dp)
                                .clip(shape)
                                .border(1.dp, Color.White, shape)
                                .padding(1.dp)
                                .border(3.dp, Color(0x7FFFF7D9), RoundedCornerShape(74.dp))
                                .shadow(2.dp, shape),
                            contentScale = ContentScale.Crop,
                        )
                        if (selection.isActive(item)) MyButtDropdownMenuStyle2(
                            Modifier.padding(top = 3.dp).padding(vertical = 0.dp).align(Alignment.TopStart),
                            expandedDropMenu
                        ) {
                            dropMenu(item, expandedDropMenu)
                        }
                        if (item.opis != "") RotationButtStyle1(
                            expandedOpis,
                            Modifier.padding(start = 10.dp, end = 10.dp).align(Alignment.TopEnd)
                        ) {
                            item.sver = item.sver.not()
                        }
                    }
                    Text(
                        text = item.name,
                        modifier = Modifier.padding(5.dp),
                        style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start)
                    )
                }
                if ((item.opis != "")) {
                    val scroll = rememberScrollState(0)
                    BoxExpand(
                        expandedOpis,
                        Modifier.widthIn(0.dp, 200.dp).myModWithBound1(),
                        Modifier.fillMaxHeight()
                    ) {
                        BoxWithVScrollBar(Modifier.padding(10.dp), scroll) { scrollStateBox ->
                            MyTextStyle2(
                                item.opis,
                                Modifier.verticalScroll(scrollStateBox, enabled = true),
                                textAlign = TextAlign.Start,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ComItemGovorunPlate(
    item: ItemGovorun,
    dirIcon: String
) {
    val avatarFile = File(dirIcon, "${item.image_file}.jpg")
    val avatarF =
        if (avatarFile.exists()) imageFromFile(avatarFile) else useResource("iv_avatar.png", ::loadImageBitmap)

    val shape = RoundedCornerShape(75.dp)
    Row(
        Modifier.padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            bitmap = avatarF,
            "defaultAvatar",
            Modifier.wrapContentSize().padding(horizontal = 5.dp)
                .height(50.dp)
                .width(50.dp)
                .clip(shape)
                .border(1.dp, Color.White, shape)
                .padding(1.dp)
                .border(3.dp, Color(0x7FFFF7D9), RoundedCornerShape(74.dp))
                .shadow(2.dp, shape),
            contentScale = ContentScale.Crop,
        )
        Text(
            text = item.name,
            modifier = Modifier,
            style = MyTextStyleParam.style2.copy(fontSize = 15.sp, textAlign = TextAlign.Start)
        )
    }
}