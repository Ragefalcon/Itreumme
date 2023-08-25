package MainTabs

import MainTabs.Avatar.Element.PanViewAvatar
import MainTabs.Avatar.Items.ComItemStat
import MainTabs.Time.Elements.VajnHourDiagram
import MyDialog.InnerFinishBirthdayAction
import MyDialog.MyDialogLayout
import MyDialog.MyInfoShow
import MyList
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.AwtWindow
import common.MyShadowBox
import common.MyTextStyleParam
import extensions.*
import kotlinx.coroutines.delay
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.commonfun.unOffsetCorrFromBase
import ru.ragefalcon.sharedcode.models.commonfun.vozrast
import ru.ragefalcon.sharedcode.source.disk.CommonName
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.InnerDialogs.InnerStartTriggerEnum
import viewmodel.MainDB
import viewmodel.StateVM
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.util.*
import javax.swing.filechooser.FileFilter

class OpenFileFilter() : FileFilter() {
    var description1 = ""
    var fileExt = listOf<String>()

    constructor(extension: List<String>) : this() {
        fileExt = extension
    }

    constructor(extension: List<String>, typeDescription: String) : this() {
        fileExt = extension
        description1 = typeDescription
    }

    override fun accept(f: File): Boolean {
        return if (f.isDirectory) true else {
            var key = false
            for (ext in fileExt) {
                if (f.name.lowercase(Locale.getDefault()).endsWith(ext)) key = true
            }
            key
        }
    }

    override fun getDescription(): String {
        return description1
    }
}


class MainStatusPanel(val dialogLayout: MyDialogLayout) {


    /**
     * https://www.reddit.com/r/Kotlin/comments/n16u8z/desktop_compose_file_picker/
     *
     * fileFilter в этой реализации совсем не работает причем это прописано уже описании класса FileDialog
     * */
    @Composable
    private fun FileDialog(
        parent: Frame? = null, onCloseRequest: (result: String?) -> Unit
    ) = AwtWindow(
        create = {
            object : FileDialog(parent, "Choose a file", FileDialog.LOAD) {
                override fun setVisible(value: Boolean) {
                    super.setVisible(value)
                    if (value) {
                        onCloseRequest(file)
                    }
                }
            }.apply {
                file = "*.jpg;*.zip"
            }
        }, dispose = FileDialog::dispose
    )


    var isFileChooserOpen = mutableStateOf(false)

    @Composable
    fun show(avatarWidth: Dp, topPadding: Dp) {

        if (isFileChooserOpen.value) {
            FileDialog(

                onCloseRequest = {
                    isFileChooserOpen.value = false
                })
        }
        MyShadowBox(
            MainDB.styleParam.statusParam.shadow_panel.getValue(),
        ) {
            Row(
                Modifier.fillMaxWidth().padding(bottom = 0.dp)
                    .paddingStyle(MainDB.styleParam.statusParam.outerPadding)
                    .withSimplePlate(SimplePlateWithShadowStyleState(MainDB.styleParam.statusParam.plateStatus))
                    .paddingStyle(MainDB.styleParam.statusParam.inner_padding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MainDB.timeSpis.spisVajnHour.getState().value?.let {
                    VajnHourDiagram(it,
                        Modifier.width(200.dp).height(80.dp).onGloballyPositioned {
                            StateVM.tmpVajnLayCoor = it
                        }.clickable {
                            MyInfoShow(dialogLayout) {
                                MainDB.timeSpis.spisVajnHour.getState().value?.let {
                                    MyList(
                                        it.sortedBy { it.vajn }, maxHeight = 600
                                    ) { ind, itemHV ->
                                        Row(
                                            Modifier.padding(horizontal = 15.dp).padding(vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            if (itemHV.vajn == -1L) {
                                                Image(
                                                    painterResource("ic_baseline_nights_stay_24.xml"),
                                                    "statDenPlan",
                                                    Modifier.height(40.dp).width(40.dp),
                                                    colorFilter = ColorFilter.tint(
                                                        MyColorARGB.colorSleep.toColor(), BlendMode.Modulate
                                                    ),
                                                    contentScale = ContentScale.FillBounds,
                                                )
                                            } else {
                                                Image(
                                                    bitmap = useResource(
                                                        "ic_stat_00.png", ::loadImageBitmap
                                                    ),
                                                    "statDenPlan",
                                                    Modifier.height(40.dp).width(40.dp),
                                                    colorFilter = ColorFilter.tint(
                                                        when (itemHV.vajn.toInt()) {
                                                            0 -> Color(0xFFFFF42B)
                                                            1 -> Color(0xFFFFFFFF)
                                                            2 -> Color(0xFF7FFAF6)
                                                            3 -> Color(0xFFFF5858)
                                                            else -> Color(0xFFFFF42B)
                                                        }, BlendMode.Modulate
                                                    ),
                                                    contentScale = ContentScale.FillBounds,
                                                    filterQuality = FilterQuality.High
                                                )
                                            }
                                            Column(
                                                Modifier.padding(start = 10.dp), horizontalAlignment = Alignment.End
                                            ) {
                                                Text(
                                                    text = "  За неделю:",
                                                    style = MyTextStyleParam.style2.copy(fontSize = 15.sp)
                                                )
                                                Row {
                                                    Text(
                                                        text = itemHV.sumNedel.roundToString(1),
                                                        style = MyTextStyleParam.style2.copy(
                                                            color = Color.Cyan.toMyColorARGB().plusDark().plusDark()
                                                                .toColor().copy(alpha = 0.6f), fontSize = 13.sp
                                                        )
                                                    )
                                                    Text(
                                                        text = " / ", style = MyTextStyleParam.style2.copy(
                                                            fontSize = 13.sp
                                                        )
                                                    )
                                                    Text(
                                                        text = (itemHV.sumNedel / 7f).roundToString(1),
                                                        style = MyTextStyleParam.style2.copy(
                                                            color = Color.Yellow.toMyColorARGB().plusDark().plusDark()
                                                                .toColor().copy(alpha = 0.8f), fontSize = 13.sp
                                                        )
                                                    )
                                                }
                                            }
                                            Column(
                                                Modifier.padding(start = 10.dp), horizontalAlignment = Alignment.End
                                            ) {
                                                Text(
                                                    text = "     За месяц:",
                                                    style = MyTextStyleParam.style2.copy(fontSize = 15.sp)
                                                )
                                                Row {
                                                    Text(
                                                        modifier = Modifier.padding(0.dp),
                                                        text = itemHV.sumMonth.roundToString(1),
                                                        style = MyTextStyleParam.style2.copy(
                                                            color = Color.Cyan.toMyColorARGB().plusDark().plusDark()
                                                                .toColor().copy(alpha = 0.6f), fontSize = 13.sp
                                                        )
                                                    )
                                                    Text(
                                                        text = " / ", style = MyTextStyleParam.style2.copy(
                                                            fontSize = 13.sp
                                                        )
                                                    )
                                                    Text(
                                                        text = (itemHV.sumMonth / 30f).roundToString(1),
                                                        style = MyTextStyleParam.style2.copy(
                                                            color = Color.Yellow.toMyColorARGB().plusDark().plusDark()
                                                                .toColor().copy(alpha = 0.8f), fontSize = 13.sp
                                                        )
                                                    )
                                                }
                                            }
                                            Column(
                                                Modifier.padding(start = 10.dp), horizontalAlignment = Alignment.End
                                            ) {
                                                Text(
                                                    text = "        За год:",
                                                    style = MyTextStyleParam.style2.copy(fontSize = 15.sp)
                                                )
                                                Row {
                                                    Text(
                                                        modifier = Modifier.padding(0.dp),
                                                        text = itemHV.sumYear.roundToString(1),
                                                        style = MyTextStyleParam.style2.copy(
                                                            color = Color.Cyan.toMyColorARGB().plusDark().plusDark()
                                                                .toColor().copy(alpha = 0.6f), fontSize = 13.sp
                                                        )
                                                    )
                                                    Text(
                                                        text = " / ", style = MyTextStyleParam.style2.copy(
                                                            fontSize = 13.sp
                                                        )
                                                    )
                                                    Text(
                                                        text = (itemHV.sumYear / 365f).roundToString(1),
                                                        style = MyTextStyleParam.style2.copy(
                                                            color = Color.Yellow.toMyColorARGB().plusDark().plusDark()
                                                                .toColor().copy(alpha = 0.8f), fontSize = 13.sp
                                                        )
                                                    )
                                                }
                                            }
                                        }

                                    }
                                }
                            }

                        })
                }
                MainDB.styleParam.statusParam.itemStatus.getComposable(::ItemStatusStyleState) { itemS ->
                    MainDB.styleParam.statusParam.itemTimeLife.getComposable(::ItemStatusStyleState) { itemTL ->
                        MainDB.avatarSpis.spisAvatarStat.getState().value?.let { listStat ->
                            Column(Modifier.padding(start = 3.dp).weight(1f)) {
                                MyShadowBox(itemTL.shadow) {
                                    Box(
                                        modifier = itemTL.outer_padding
                                            .fillMaxWidth()
                                            .background(itemTL.BACKGROUND, itemTL.shape)
                                            .border(
                                                width = itemTL.BORDER_WIDTH,
                                                brush = itemTL.BORDER,
                                                shape = itemTL.shape
                                            )
                                            .clickable {
                                                StateVM.innerFinishAction.value = InnerFinishBirthdayAction()
                                                MainDB.addTime.startInnerTrigger(InnerStartTriggerEnum.ChangeBirthday)
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        timeLife(itemTL)
                                    }
                                }
                                if (listStat.size > 2) {
                                    ComItemStat(listStat[1], itemS).getComposable()
                                    ComItemStat(listStat[2], itemS).getComposable()
                                }
                            }
                            Column(Modifier.weight(1f)) {
                                if (listStat.size > 5) {
                                    ComItemStat(listStat[0], itemS).getComposable()
                                    ComItemStat(listStat[3], itemS).getComposable()
                                    ComItemStat(listStat[4], itemS).getComposable()
                                    ComItemStat(listStat[5], itemS).getComposable()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun avatar(dialPan: MyDialogLayout, modifier: Modifier = Modifier, size: Dp = 180.dp) {
    val shape2 = RoundedCornerShape(0.dp, 0.dp, 14.dp, 14.dp)
    val avatarFile = File(StateVM.dirAvatar, CommonName.nameAvatarFile)

    MainDB.styleParam.statusParam.let { style ->
        val shape = style.shape_avatar.getValue()

        val avatarF = remember {
            mutableStateOf(
                if (avatarFile.exists()) imageFromFile(avatarFile) else useResource(
                    CommonName.nameDefaultAvatarResource,
                    ::loadImageBitmap
                )
            )
        }
        MyShadowBox(
            style.shadow_avatar.getValue(),
            style.outerPaddingAvatar.getValue(modifier)
                .height(size)
                .width(size), contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                bitmap = avatarF.value,
                "defaultAvatar",
                Modifier
                    .wrapContentSize()

                    .height(size)
                    .width(size)
                    .clickable {
                        PanViewAvatar(dialLay = dialPan, avatarF)
                    }
                    .clip(shape)
                    .border(style.BORDER_WIDTH_AVATAR.getValue().dp, style.BORDER_BRUSH_AVATAR.getValue(), shape)
                    .padding(1.dp),
                contentScale = ContentScale.Crop,
            )
        }
    }

}

@Composable
fun timeLife(
    style: ItemStatusStyleState

) {
    val birthday = mutableStateOf(MainDB.avatarSpis.spisMainParam.getState().value?.find { it.name == "Birthday" }
        ?.let { unOffsetCorrFromBase(it.stringparam.toLong()) })

    val textLifetime = remember { mutableStateOf("") }

    LaunchedEffect(key1 = textLifetime.value, key2 = birthday) {
        birthday.value?.let {
            delay(1000L)
            MainDB.timeFun.checkChangeCurrentDate()
            textLifetime.value = vozrast(it)
        } ?: run { delay(1000L) }
    }
    Column(
        style.inner_padding, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Время жизни:", style = style.mainText.copy(textAlign = TextAlign.Center)
        )
        Text(
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
            text = textLifetime.value,
            style = style.valueText.copy(textAlign = TextAlign.Center)
        )
    }

}

@Composable
fun timeString(birthday: Long): State<String> = produceState(vozrast(birthday)) {
    while (true) {
        delay(1000L)
        value = vozrast(birthday)
    }
}