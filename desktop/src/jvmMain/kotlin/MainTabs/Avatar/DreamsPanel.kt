package MainTabs.Avatar

import MainTabs.Avatar.Element.PanAddDream
import MainTabs.Avatar.Element.PanAddImageCommon
import MainTabs.Avatar.Items.ComItemDream
import MainTabs.imageFromFile
import MyDialog.MyDialogLayout
import MyList
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.models.data.ItemDream
import ru.ragefalcon.sharedcode.models.data.ItemYearGraf
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.StyleVMspis
import viewmodel.MainDB
import viewmodel.StateVM
import java.io.File
import kotlin.random.Random

class DreamsPanel {

    private val selection = SingleSelectionType<ItemDream>()
    val vypDream = mutableStateOf(false)
    val spisDreamView = mutableStateOf(false)

    val list = mutableStateOf<List<ItemYearGraf>>(listOf())

    var imageIB: MutableState<ImageBitmap?> = mutableStateOf<ImageBitmap?>(null).apply {
        selection.selected?.let { item ->
            val ff = File(StateVM.dirDreamsImages, "dream_${item.id_main}.jpg")
            if (ff.exists()) {
                value = imageFromFile(ff)
            }
        }
    }

    @Composable
    private fun dropMenuItemDream(item: ItemDream, expanded: MutableState<Boolean>, dialLay: MyDialogLayout) {
        DropdownMenuItem(onClick = {
            PanAddDream(dialLay, item)
            expanded.value = false
        }) {
            Text(text = "Изменить", color = Color.White)
        }
        MyCompleteDropdownMenuButton(
            expanded,
            item.stat == 10L,
            textToFalse = "Раздостигнута",
            textToTrue = "Достигнута"
        ) {
            MainDB.addAvatar.setOpenDream(item.id.toLong(), item.stat != 10L)
            if (!vypDream.value) selection.selected = null
        }
        MyDeleteDropdownMenuButton(expanded) {
            MainDB.addAvatar.delDream(item.id.toLong())
            selection.selected = null
        }
    }

    fun updatePrivsDream() {
    }

    @Composable
    private fun StyleVMspis.InterfaceState.DreamTabParam.topPanel(dialLay: MyDialogLayout) {
        RowVA(Modifier.padding(bottom = 0.dp)) {
            MainDB.avatarSpis.spisDreams.getState().value?.let { list ->
                if (list.count() > 1) MyTextButtStyle1(
                    "❮❮",
                    modifier = Modifier.padding(end = 5.dp),
                    myStyleTextButton = TextButtonStyleState(buttArrowDream)
                ) {
                    list.indexOf(selection.selected).let { indexSel ->
                        selection.selected =
                            if (indexSel > 0) list[indexSel - 1] else list.lastOrNull()
                        updatePrivsDream()
                    }
                }
                Box(Modifier.weight(1f)) {
                    selection.selected?.let {
                        ComItemDream(it, selection, selFun = {

                        }, selectable = false, itemDreamStyleState = ItemDreamStyleState(itemDream)) { item, expanded ->
                            dropMenuItemDream(item, expanded, dialLay)
                        }.getComposable()
                    }
                }
                if (list.count() > 1) MyTextButtStyle1(
                    "❯❯",
                    modifier = Modifier.padding(start = 5.dp),
                    myStyleTextButton = TextButtonStyleState(buttArrowDream)
                ) {
                    list.indexOf(selection.selected).let { indexSel ->
                        selection.selected =
                            if (indexSel < list.count() - 1) list[indexSel + 1] else list[0]
                        updatePrivsDream()
                    }
                }
            }
        }
    }

    @Composable
    private fun StyleVMspis.InterfaceState.DreamTabParam.buttPanel(dialLay: MyDialogLayout) {
        RowVA(Modifier.padding(top = 5.dp)) {
            MyToggleButtIconStyle1(
                "ic_round_check_circle_outline_24.xml",
                "ic_round_check_circle_24.xml",
                twoIcon = false,
                value = vypDream,
                sizeIcon = 35.dp,
                modifier = Modifier.height(35.dp).padding(start = 15.dp),
                width = 70.dp,
                myStyleToggleButton = ToggleButtonStyleState(buttVypDream)
            ) {

                MainDB.avatarFun.setOpenspisDreams(it)
                selection.selected = null
            }
            MyToggleButtIconStyle1(
                "ic_round_view_list_24.xml",
                twoIcon = false,
                value = spisDreamView,
                sizeIcon = 35.dp,
                modifier = Modifier.height(35.dp).padding(start = 15.dp),
                width = 70.dp,
                myStyleToggleButton = ToggleButtonStyleState(buttViewSpisDream)
            )

            Text(
                "Мечты: ${MainDB.avatarSpis.spisDreams.getState().value?.size ?: 0}",
                Modifier.weight(1f).padding(horizontal = 20.dp),
                style = textCountDream.getValue()
            )
            MyTextButtStyle1(
                "+",
                modifier = Modifier.padding(end = 15.dp),
                width = 70.dp, height = 35.dp,
                myStyleTextButton = TextButtonStyleState(buttAddDream)
            ) {

                PanAddDream(dialLay)
            }
        }
    }

    @Composable
    private fun StyleVMspis.InterfaceState.DreamTabParam.spisok(dialLay: MyDialogLayout, modifier: Modifier) {
        itemDream.getComposable(::ItemDreamStyleState) { itemStyle ->
            MyList(MainDB.avatarSpis.spisDreams, modifier) { ind, itemDr ->
                ComItemDream(itemDr, selection, selFun = {
                    updatePrivsDream()
                }, itemDreamStyleState = itemStyle) { item, expanded ->
                    dropMenuItemDream(item, expanded, dialLay)
                }.getComposable()
            }
        }
    }

    @Composable
    private fun StyleVMspis.InterfaceState.DreamTabParam.rectOpis(dialLay: MyDialogLayout, selItem: ItemDream) {
        BoxWithConstraints(Modifier.wrapContentSize()) {
            Column(
                Modifier.padding(15.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (imageIB.value != null) imageIB.value?.let {
                    MyShadowBox(panelImage.shadow.getValue()) {
                        Image(
                            bitmap = it,
                            "defaultAvatar",
                            Modifier.padding(bottom = 5.dp)

                                .heightIn(0.dp, this@BoxWithConstraints.maxHeight * 0.5f)
                                .widthIn(0.dp, this@BoxWithConstraints.maxWidth * 0.8f)
                                .withSimplePlate(SimplePlateWithShadowStyleState(panelImage))
                                .clip(panelImage.SHAPE.getValue())


                                .clickable {
                                    PanAddImageCommon(
                                        dialLay,
                                        maxSizePx = 1200,
                                        ratio_W_H = 3.0 to 0.0
                                    ) { outImage ->
                                        outImage.saveIconFile(
                                            File(
                                                StateVM.dirDreamsImages,
                                                "dream_${selItem.id_main}.jpg"
                                            ).path
                                        )
                                        val ff =
                                            File(StateVM.dirDreamsImages, "dream_${selItem.id_main}.jpg")
                                        if (ff.exists()) {
                                            imageIB.value = imageFromFile(ff)
                                        }

                                    }
                                },
                            contentScale = ContentScale.Fit,
                        )
                    }
                } else {
                    MyShadowBox(panelImage.shadow.getValue()) {
                        Box(
                            Modifier.padding(bottom = 5.dp)
                                .height(100.dp)
                                .width(200.dp)
                                .withSimplePlate(SimplePlateWithShadowStyleState(panelImage))


                                .clickable {
                                    PanAddImageCommon(
                                        dialLay,
                                        maxSizePx = 1200,
                                        ratio_W_H = 3.0 to 0.0
                                    ) { outImage ->
                                        outImage.saveIconFile(
                                            File(
                                                StateVM.dirDreamsImages,
                                                "dream_${selItem.id_main}.jpg"
                                            ).path
                                        )
                                        val ff =
                                            File(StateVM.dirDreamsImages, "dream_${selItem.id_main}.jpg")
                                        if (ff.exists()) {
                                            imageIB.value = imageFromFile(ff)
                                        }

                                    }
                                }, contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.padding(vertical = 10.dp),
                                text = "Добавить фото",
                                style = textAdd.getValue()
                            )
                        }
                    }

                }
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    BoxWithVScrollBar(Modifier.padding(10.dp)) { scrollStateBox ->
                        SelectionContainer {
                            Text(
                                modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth()
                                    .verticalScroll(scrollStateBox, enabled = true),
                                text = selItem.opis,
                                style = textOpis.getValue().copy(
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun show(
        dialLay: MyDialogLayout,
        modifier: Modifier = Modifier
    ) {
        LaunchedEffect(selection.selected) {
            selection.selected?.let { item ->
                val ff = File(StateVM.dirDreamsImages, "dream_${item.id_main}.jpg")
                if (ff.exists()) {
                    imageIB.value = imageFromFile(ff)
                } else {
                    imageIB.value = null
                }
            }
        }
        LaunchedEffect(MainDB.avatarSpis.spisDreams.getState().value) {
            MainDB.avatarSpis.spisDreams.getState().value?.let {
                if (!it.contains(selection.selected)) {
                    if (it.isNotEmpty()) {
                        if (selection.selected != null) {
                            it.find { it.id == (selection.selected?.id ?: -1L) }?.let {
                                selection.selected = it
                            } ?: run {
                                selection.selected =
                                    if (spisDreamView.value) it.firstOrNull() else it[Random.nextInt(it.indices.first,it.indices.last)]
                            }
                        } else {
                            selection.selected =
                                if (spisDreamView.value) it.firstOrNull() else it[Random.nextInt(it.indices.first,it.indices.last)]
                        }
                        updatePrivsDream()
                    }
                }
            }
        }
        with(MainDB.styleParam.avatarParam.dreamTab) {
            Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                if (spisDreamView.value) {
                    spisok(dialLay, Modifier.padding(vertical = 10.dp).weight(1f))
                } else {
                    topPanel(dialLay)
                    selection.selected?.let { selItem ->
                        MyShadowBox(panelOpis.shadow.getValue(), Modifier.weight(1f)) {
                            Box(
                                Modifier
                                    .withSimplePlate(SimplePlateWithShadowStyleState(panelOpis))
                                    .fillMaxWidth().animateContentSize()
                            ) {
                                if (VIGNETTE.getValue()) BackBoxEllipsGradient(
                                    modifier = Modifier.matchParentSize().clip(panelOpis.SHAPE.getValue()),
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0x3F000000),
                                        Color(0x8F000000),
                                        Color(0xFF000000)
                                    )
                                )
                                rectOpis(dialLay, selItem)
                            }
                        }

                    }
                }
                buttPanel(dialLay)
            }
        }
    }
}