package common

import MainTabs.Avatar.Element.PanAddImageCommon
import MainTabs.Setting.openInBrowser
import MainTabs.imageFromFile
import MainTabs.saveImage
import MyDialog.MyDialogLayout
import MyDialog.MyOneVopros
import adapters.MyComboBox
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soywiz.korim.awt.toBufferedImage
import com.soywiz.korio.async.async
import extensions.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.*
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.tests.imageSliceForIcon
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TableNameForComplexOpis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeOpisBlock
import viewmodel.MainDB
import viewmodel.StateVM
import java.awt.image.BufferedImage
import java.io.File
import java.net.URI
import java.util.*


class MyComplexOpisEditBox(
    val label: String,
    val item_id: Long,
    val tableName: TableNameForComplexOpis,
    val listOpis: SnapshotStateList<ItemComplexOpis> = mutableStateListOf<ItemComplexOpis>(
        ItemComplexOpisText(
            -1L,
            tableName.nameTable,
            item_id,
            TypeOpisBlock.simpleText,
            1L,
            text = "",
            color = 1,
            fontSize = 3,
            cursiv = false,
            bold = 4
        )
    ),
    val focusName: FocusRequester? = null,
    val timeKeyEvent: MutableState<Long> = mutableStateOf(0L)
) {

    val maxSizeImagePx = 1500
    fun getImageForIG(dialLay: MyDialogLayout, rezFun: (Long) -> Unit) {
        PanAddImageCommon(dialLay, ratio_W_H = 0.0 to 0.0, maxSizePx = maxSizeImagePx) { outImage ->
            val tempIdImage = Date().format("yyyyMMddHHssSSS").toLong()
            outImage.saveIconFile(
                File(
                    StateVM.dirTemp,
                    "complexOpisImage_tmp_$tempIdImage.jpg"
                ).path
            )
            rezFun(tempIdImage)
        }

    }

    fun loadNewListOpis(newList: List<ItemComplexOpis>) {
        selectionItemOpis.selected = null
        listOpis.map { it.myCommonCopy() }.forEach { itemOpis ->
            if (itemOpis.id > 0) {
                changeNotSelectedItem(itemOpis, itemOpis.myCommonCopy(sort = -1))
            } else {
                listOpis.remove(itemOpis)
                saveTextField.remove(itemOpis)
            }
        }
        newList
            .clearSourceList(tableName)
            .forEach { newItem ->
                listOpis.add(newItem)
            }
    }


    var selectMarker by mutableStateOf(1L)
    var updateSelMarker by mutableStateOf(1L)
    fun updSelectMarker(new: Long) {
        selectMarker = new
        updateSelMarker++
    }

    var newItemMarker by mutableStateOf(-1)

    val focusRequesters: MutableList<Pair<Long, FocusRequester>> = mutableListOf()

    private fun setFocusRequesters(item: ItemComplexOpis) {
        focusRequesters.find { it.first == item.sort }
            ?.let {
                it.second.requestFocus()
            }
    }

    fun setFocus() {
        selectionItemOpis.selected?.let { itemOpis ->
            listOpis.find { it.sort == itemOpis.sort && (it !is ItemComplexOpisImageGroup || it.enableText) }
                ?.let { fItem ->
                    setFocusRequesters(fItem)
                } ?: listOpis.find { it.sort > 0 && (it !is ItemComplexOpisImageGroup || it.enableText) }
                ?.let { setFocusRequesters(it) }
        } ?: listOpis.find { it.sort > 0 && (it !is ItemComplexOpisImageGroup || it.enableText) }
            ?.let { setFocusRequesters(it) }
    }

    fun setFocusFirst() {
        listOpis.find { it.sort > 0 && (it !is ItemComplexOpisImageGroup || it.enableText) }
            ?.let { setFocusRequesters(it) }
    }

    fun setFocusLast() {
        listOpis.findLast { it.sort > 0 && (it !is ItemComplexOpisImageGroup || it.enableText) }
            ?.let { setFocusRequesters(it) }
    }

    fun setNextFocus() {
        selectionItemOpis.selected?.let { itemOpis ->
            listOpis.find { it.sort > itemOpis.sort && (it !is ItemComplexOpisImageGroup || it.enableText) }
                ?.let { fItem ->
                    setFocusRequesters(fItem)
                } ?: focusName?.requestFocus() ?: setFocusFirst()
        } ?: setFocus()
    }

    fun setPrevFocus() {
        selectionItemOpis.selected?.let { itemOpis ->
            listOpis.findLast { it.sort < itemOpis.sort && (it !is ItemComplexOpisImageGroup || it.enableText) }
                ?.let { fItem ->
                    setFocusRequesters(fItem)
                } ?: focusName?.requestFocus() ?: setFocusLast()
        } ?: setFocus()
    }

    var saveTextField: MutableMap<ItemComplexOpis, MutableState<TextFieldValue>> =
        mutableMapOf<ItemComplexOpis, MutableState<TextFieldValue>>().apply {
            listOpis.forEach {
                if (it is ItemComplexOpisTextCommon) put(it, mutableStateOf(TextFieldValue(it.text)))
            }
        }

    val selectionItemOpis = SingleSelectionType<ItemComplexOpis>()
    val selectionImage = SingleSelectionType<ItemComplexOpisImage>()

    fun changeSelectedItem(item: ItemComplexOpis, newItem: ItemComplexOpis) {
        if (changeNotSelectedItem(item, newItem)) selectionItemOpis.selected = newItem
    }

    fun changeNotSelectedItem(item: ItemComplexOpis, newItem: ItemComplexOpis): Boolean {
        listOpis.indexOf(item).let {
            if (it >= 0) {
                if (newItem is ItemComplexOpisTextCommon) {
                    val tmpTextField = saveTextField[item]
                    saveTextField.remove(item)
                    saveTextField.put(newItem, tmpTextField ?: mutableStateOf(TextFieldValue(newItem.text)))
                } else saveTextField.remove(item)
                listOpis.set(it, newItem)
                return true
            } else return false
        }
    }

    fun changeSelectedImage(
        listImg: MutableList<ItemComplexOpisImage>,
        item: ItemComplexOpisImage,
        newItem: ItemComplexOpisImage
    ): MutableList<ItemComplexOpisImage>? {
        changeNotSelectedImage(listImg, item, newItem)?.let {
            selectionImage.selected = newItem
            return it
        }
        return null
    }

    fun changeNotSelectedImage(
        listImg: MutableList<ItemComplexOpisImage>,
        item: ItemComplexOpisImage,
        newItem: ItemComplexOpisImage
    ): MutableList<ItemComplexOpisImage>? {
        listImg.indexOf(item).let {
            if (it >= 0) {
                listImg.set(it, newItem)
                return listImg
            } else return null
        }
    }

    fun addNewItem(item: ItemComplexOpis) {
        listOpis.add(item)
        if (item is ItemComplexOpisTextCommon) saveTextField.put(
            item,
            mutableStateOf(TextFieldValue(item.text))
        )

        val selSort = selectionItemOpis.selected?.sort
        selSort?.let {
            if (item.sort - selSort > 1) {
                selectionItemOpis.selected = item
                listOpis.filter {
                    it.sort > selSort && it.sort != item.sort
                }.map { it.myCommonCopy() }.forEach { findItem ->
                    changeNotSelectedItem(
                        findItem,
                        findItem.myCommonCopy(sort = findItem.sort + 1)
                    )
                }
                changeSelectedItem(item, item.myCommonCopy(sort = selSort + 1))
                listOpis.sortBy { it.sort }
                updSelectMarker(selSort + 1)
            } else {
                if (selSort + 1 == listOpis.maxOf { it.sort }) {
                    newItemMarker++
                }
            }
        }

    }

    fun loadImageFromBuffer(): Boolean {
        getImageFromClipboard()?.let {
            CoroutineScope(Dispatchers.Default).async {
                val tempIdImage = Date().format("yyyyMMddHHssSSS").toLong()
                val jpegFiletoSave = File(StateVM.dirTemp, "complexOpisImage_tmp_$tempIdImage.jpg")

                val bufferFile = File(StateVM.dirTemp, "bufferFile_$tempIdImage.jpg")
                val convertedImage =
                    BufferedImage(
                        it.toBufferedImage().width,
                        it.toBufferedImage().height,
                        BufferedImage.TYPE_INT_RGB
                    )
                convertedImage.createGraphics().drawImage(it.toBufferedImage(), 0, 0, null)
                saveImage(convertedImage, bufferFile, 0.95f)

                if (it.toBufferedImage().width <= maxSizeImagePx && it.toBufferedImage().height <= maxSizeImagePx) {
                    bufferFile.renameTo(jpegFiletoSave)
                } else {
                    val outImage: IconImageBuffer = IconImageBuffer()
                    val aa = imageSliceForIcon(
                        bufferFile.path,
                        0f,
                        0f,
                        it.toBufferedImage().width.toFloat(),
                        it.toBufferedImage().height.toFloat(),
                        maxSizeImagePx,
                        false
                    )
                    outImage.setBuffer(aa.first, "jpg", aa.second)
                    outImage.saveIconFile(jpegFiletoSave.path)
                    bufferFile.delete()
                }

                selectionItemOpis.selected.let {
                    if (it is ItemComplexOpisImageGroup) {
                        changeSelectedItem(
                            it,
                            it.copy(
                                spisImages = it.spisImages.toMutableList().apply {
                                    add(
                                        ItemComplexOpisImage(
                                            tempIdImage,
                                            -1L,
                                            this.filter { it.sort > 0 }.size + 1L,
                                            true
                                        )
                                    )
                                })
                        )
                    } else {
                        val item = ItemComplexOpisImageGroup(
                            -1L,
                            tableName.nameTable,
                            item_id,
                            TypeOpisBlock.simpleText,
                            listOpis.filter { it.sort > 0 }.size + 1L,
                            text = "",
                            color = 1,
                            fontSize = 3,
                            cursiv = false,
                            bold = 4,
                            sizePreview = 2,
                            widthLimit = true,

                            enableText = false,
                            textBefore = false,
                            spisImages = listOf(ItemComplexOpisImage(tempIdImage, -1L, 1L, true))
                        )
                        addNewItem(item)
                    }
                }
            }
            return true
        } ?: return false
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun risItem(
        itemOpis: ItemComplexOpis,
        scroll: ScrollState,
        styleState: ComplexOpisStyleState,
        layoutCoordinatesInScrollBox: MutableState<LayoutCoordinates?>,
        scrollBoxLC: MutableState<LayoutCoordinates?>,
        heightScrollbar: () -> Int,
        dialLay: MyDialogLayout,
        focusRequesterLast: MutableState<FocusRequester?>
    ) {
        with(styleState) {
            with(LocalDensity.current) {
                when (itemOpis) {
                    is ItemComplexOpisTextCommon -> {
                        val textField = saveTextField[itemOpis] ?: mutableStateOf(TextFieldValue(itemOpis.text))
                        val interactionSource = remember { MutableInteractionSource() }
                        val focus by interactionSource.collectIsFocusedAsState()
                        val focusRequester = remember {
                            FocusRequester().apply {
                                focusRequesters.find { it.first == itemOpis.sort }?.let {
                                    focusRequesters.remove(it)
                                }
                                focusRequesters.add(itemOpis.sort to this)
                            }
                        }

                        val cursorPosition = remember { mutableStateOf(0f) }
                        LaunchedEffect(cursorPosition.value) {
                            if (selectionItemOpis.selected == itemOpis) {

                                if (scroll.maxValue != Int.MAX_VALUE && scroll.maxValue > 0) {


                                    if (cursorPosition.value > heightScrollbar() + scroll.value - 10.dp.toPx()
                                            .toInt()
                                    ) {
                                        scroll.scrollTo(
                                            cursorPosition.value.toInt() - heightScrollbar() + 10.dp.toPx().toInt()
                                        )
                                    }
                                    if (cursorPosition.value < scroll.value + fontSet.getFontSize(itemOpis.fontSize)
                                            .toPx().toInt() + 10.dp.toPx().toInt()
                                    ) {
                                        scroll.scrollTo(
                                            cursorPosition.value.toInt() - fontSet.getFontSize(itemOpis.fontSize)
                                                .toPx()
                                                .toInt() - 10.dp.toPx().toInt()
                                        )
                                    }
                                }
                            }
                        }

                        LaunchedEffect(focus) {
                            if (focus) {
                                selectionItemOpis.selected = itemOpis
                                if (itemOpis !is ItemComplexOpisImageGroup) selectionImage.selected = null
                            }
                        }
                        var layoutCoordinatesItems by remember { mutableStateOf<LayoutCoordinates?>(null) }
                        MyShadowBox(
                            if (itemOpis is ItemComplexOpisCheckbox) {
                                when (itemOpis.checked) {
                                    0L -> plateCheckboxFalse.shadow
                                    1L -> plateCheckboxTrue.shadow
                                    2L -> plateCheckboxFail.shadow
                                    3L -> plateCheckboxQuestion.shadow
                                    4L -> plateCheckboxPriority.shadow
                                    else -> plateCheckboxFalse.shadow
                                }
                            } else Shadow(Color.Transparent)
                        ) {
                            RowVA(Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned {
                                    layoutCoordinatesItems = it
                                }
                                .clickable(remember { MutableInteractionSource() }, null) {
                                    if (focus.not() && (itemOpis !is ItemComplexOpisImageGroup || itemOpis.enableText)) focusRequester.requestFocus()
                                    if (itemOpis is ItemComplexOpisImageGroup) {
                                        selectionItemOpis.selected = itemOpis
                                        selectionImage.selected?.let {
                                            if (itemOpis.spisImages.contains(it).not()) selectionImage.selected =
                                                itemOpis.spisImages.sortedBy { it.sort }.firstOrNull()
                                        }
                                        if (selectionImage.selected == null) selectionImage.selected =
                                            itemOpis.spisImages.sortedBy { it.sort }.firstOrNull()
                                    } else {
                                        selectionItemOpis.selected = itemOpis
                                        selectionImage.selected = null
                                    }
                                }
                                .run {
                                    if (selectionItemOpis.selected == itemOpis && itemOpis is ItemComplexOpisImageGroup) {
                                        this
                                            .border(
                                                BorderStroke(
                                                    textFieldOpis.panelFocus.BORDER_WIDTH,
                                                    textFieldOpis.panelFocus.BORDER
                                                ), textFieldOpis.panelFocus.shape
                                            )
                                            .background(
                                                textFieldOpis.panelFocus.BACKGROUND,
                                                textFieldOpis.panelFocus.shape
                                            )
                                    } else this
                                }
                                .run {
                                    if (itemOpis is ItemComplexOpisCheckbox) {
                                        this.withSimplePlate(
                                            when (itemOpis.checked) {
                                                0L -> plateCheckboxFalse
                                                1L -> plateCheckboxTrue
                                                2L -> plateCheckboxFail
                                                3L -> plateCheckboxQuestion
                                                4L -> plateCheckboxPriority
                                                else -> plateCheckboxFalse
                                            }
                                        )
                                    } else this
                                }
                                .then(outer_padding_text)
                            ) {
                                Text(
                                    itemOpis.sort.toString(),
                                    Modifier.alpha(0.3f).padding(end = 3.dp),
                                    color = if (selectionItemOpis.selected == itemOpis) Color.Red else Color.White,
                                    fontSize = 11.sp
                                )
                                @Composable
                                fun mainText(fill: Boolean = true) {
                                    MyTextFieldForCompexOpis(
                                        saveTextField[itemOpis] ?: textField,
                                        hint = "... Поле для текста ...",
                                        modifier = Modifier
                                            .run {
                                                if (fill) this.fillMaxWidth() else this.weight(
                                                    1f,
                                                    false
                                                )
                                            }
                                            .focusRequester(focusRequester),
                                        onValueChange = {
                                            (saveTextField[itemOpis] ?: textField).value = it
                                            changeSelectedItem(
                                                itemOpis,
                                                when (itemOpis) {
                                                    is ItemComplexOpisText -> itemOpis.copy(text = it.text)
                                                    is ItemComplexOpisCheckbox -> itemOpis.copy(text = it.text)
                                                    is ItemComplexOpisImageGroup -> itemOpis.copy(text = it.text)
                                                    is ItemComplexOpisLink -> itemOpis.copy(text = it.text)
                                                }
                                            )
                                        },
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),


                                        textColor = colorSet.getColor(itemOpis.color),
                                        fontSize = fontSet.getFontSize(itemOpis.fontSize),
                                        cursive = itemOpis.cursiv,
                                        thin = itemOpis.bold,
                                        interactionSource = interactionSource,
                                        cursorPosition = cursorPosition,
                                        parentScrollbarLC = layoutCoordinatesInScrollBox.value,
                                        style = textFieldOpis,
                                        fill = fill
                                    )
                                }

                                if (itemOpis is ItemComplexOpisCheckbox) Image(
                                    painterResource(
                                        when (itemOpis.checked) {
                                            0L -> "ic_round_check_box_outline_blank_24.xml"
                                            1L -> "ic_baseline_check_box_24.xml"
                                            2L -> "ic_baseline_close_24.xml"
                                            3L -> "ic_baseline_question_mark_24.xml"
                                            4L -> "ic_baseline_priority_high_24.xml"
                                            else -> "ic_round_check_box_outline_blank_24.xml"
                                        }
                                    ),
                                    "MyCheckBox",
                                    Modifier.padding(start = MainDB.styleParam.timeParam.planTab.complexOpisForPlan.textFieldOpis.inner_padding.run { if (PADDING_INDIVIDUALLY.getValue()) START else PADDING_HORIZONTAL }
                                        .getValue().dp)
                                        .height(25.dp)
                                        .width(25.dp)
                                        .clickable {
                                            changeSelectedItem(
                                                itemOpis,
                                                itemOpis.nextCheck()
                                            )
                                        },
                                    contentScale = ContentScale.Fit,
                                    colorFilter = ColorFilter.tint(
                                        when (itemOpis.checked) {
                                            0L -> colorCheckboxFalse
                                            1L -> colorCheckboxTrue
                                            2L -> colorCheckboxFail
                                            3L -> colorCheckboxQuestion
                                            4L -> colorCheckboxPriority
                                            else -> colorCheckboxFalse
                                        }
                                    )
                                )
                                if (itemOpis is ItemComplexOpisImageGroup) {
                                    if (itemOpis.enableText && itemOpis.textBefore) mainText(false)
                                    MyShadowBox(shadowImage) {
                                        BoxWithConstraints {
                                            PlateOrderLayout(
                                                modifier = Modifier.padding(vertical = 5.dp)
                                                    .padding(
                                                        start = if (itemOpis.enableText && itemOpis.textBefore) 10.dp else 0.dp,
                                                        end = if (itemOpis.enableText && itemOpis.textBefore.not()) 10.dp else 0.dp
                                                    )
                                                    .run {
                                                        if (itemOpis.enableText) this.widthIn(
                                                            0.dp,
                                                            maxWidth * 2f / 3f - 10.dp
                                                        ) else this
                                                    },

                                                fillWidth = false,
                                                spaceBetween = 10.dp.toPx().toInt()
                                            ) {
                                                itemOpis.spisImages.filter { it.sort > 0 }.sortedBy { it.sort }
                                                    .forEach { itImg ->
                                                        val interactionSource1 =
                                                            remember { MutableInteractionSource() }
                                                        val isHovered by interactionSource1.collectIsHoveredAsState()
                                                        val imageIB: MutableState<ImageBitmap?> =
                                                            mutableStateOf<ImageBitmap?>(null).apply {
                                                                val ff = if (itImg.fileTmp.not()) File(
                                                                    StateVM.dirComplexOpisImages,
                                                                    "complexOpisImage_${itImg.id}.jpg"
                                                                ) else File(
                                                                    StateVM.dirTemp,
                                                                    "complexOpisImage_tmp_${itImg.id}.jpg"
                                                                )
                                                                if (ff.exists()) {
                                                                    value = imageFromFile(ff)
                                                                }
                                                            }

                                                        Box(
                                                            Modifier.padding(2.dp)
                                                                .offset(
                                                                    2.dp - (if (isHovered) 4f else 2f).dp,
                                                                    2.dp - (if (isHovered) 4f else 2f).dp
                                                                )
                                                        ) {
                                                            imageIB.value?.let { imgBtm ->
                                                                Image(
                                                                    bitmap = imgBtm,
                                                                    "defaultAvatar",
                                                                    Modifier


                                                                        .wrapContentSize()
                                                                        .heightIn(
                                                                            0.dp, when (itemOpis.sizePreview) {
                                                                                1L -> 50.dp
                                                                                2L -> 100.dp
                                                                                3L -> 170.dp
                                                                                else -> 100.dp
                                                                            }
                                                                        )
                                                                        .run {
                                                                            if (itemOpis.widthLimit) this.widthIn(
                                                                                0.dp, when (itemOpis.sizePreview) {
                                                                                    1L -> 50.dp
                                                                                    2L -> 100.dp
                                                                                    3L -> 170.dp
                                                                                    else -> 100.dp
                                                                                }
                                                                            ) else this
                                                                        }
                                                                        .border(
                                                                            BorderStroke(
                                                                                1.dp,
                                                                                if (itImg == selectionImage.selected) brushBoundImageActive else brushBoundImage
                                                                            )
                                                                        )
                                                                        .hoverable(interactionSource1)
                                                                        .mouseDoubleClick(onClick = {
                                                                            if (focus.not() && itemOpis.enableText) focusRequester.requestFocus()
                                                                            selectionItemOpis.selected = itemOpis
                                                                            selectionImage.selected = itImg
                                                                        }, onDoubleClick = {
                                                                            PanViewImageList(
                                                                                itemOpis.spisImages.filter { it.sort > 0 }
                                                                                    .sortedBy { it.sort }
                                                                                    .map {
                                                                                        File(
                                                                                            StateVM.run { if (it.opis_id > 0) dirComplexOpisImages else dirTemp },
                                                                                            "complexOpisImage_${if (it.opis_id > 0) "" else "tmp_"}${it.id}.jpg"
                                                                                        )
                                                                                    }, File(
                                                                                    StateVM.run { if (itImg.fileTmp.not()) dirComplexOpisImages else dirTemp },
                                                                                    "complexOpisImage_${if (itImg.fileTmp.not()) "" else "tmp_"}${itImg.id}.jpg"
                                                                                )
                                                                            )
                                                                        })
                                                                        .alpha(0.8f),
                                                                    contentScale = ContentScale.Fit,
                                                                )
                                                            }
                                                            if (imageIB.value == null) Image(
                                                                painterResource("ic_round_delete_forever_24.xml"),
                                                                "not_exist_img",
                                                                Modifier
                                                                    .height(
                                                                        when (itemOpis.sizePreview) {
                                                                            1L -> 50.dp
                                                                            2L -> 100.dp
                                                                            3L -> 170.dp
                                                                            else -> 100.dp
                                                                        }
                                                                    )
                                                                    .width(
                                                                        when (itemOpis.sizePreview) {
                                                                            1L -> 50.dp
                                                                            2L -> 100.dp
                                                                            3L -> 170.dp
                                                                            else -> 100.dp
                                                                        }
                                                                    ),
                                                                contentScale = ContentScale.Fit,
                                                                colorFilter = ColorFilter.tint(Color.Red.copy(0.5f))
                                                            )
                                                            Text(
                                                                itImg.sort.toString(),
                                                                modifier = Modifier.padding(start = 4.dp, top = 2.dp),
                                                                color = Color.Yellow,
                                                                fontSize = 12.sp
                                                            )
                                                        }
                                                    }
                                            }
                                        }
                                    }
                                    if (itemOpis.enableText && itemOpis.textBefore.not()) mainText()
                                }

                                if (itemOpis !is ItemComplexOpisImageGroup && itemOpis !is ItemComplexOpisLink) mainText()
                                if (itemOpis is ItemComplexOpisLink) {
                                    MyShadowBox(plateLink.shadow) {
                                        RowVA(Modifier.withSimplePlate(plateLink).then(inner_padding_link)) {
                                            MyIconButtWithoutBorder(
                                                "ic_round_keyboard_double_arrow_up_24.xml",
                                                sizeIcon = 30.dp,
                                                myStyleButton = buttIconLink
                                            ) {
                                                if ("^https://|^http://|^www\\.".toRegex()
                                                        .containsMatchIn(itemOpis.link)
                                                )
                                                    openInBrowser(URI(itemOpis.link))
                                                else openInBrowser(URI("www.${itemOpis.link}"))
                                            }
                                            Column() {
                                                mainText()
                                                val textFLink =
                                                    remember { mutableStateOf(TextFieldValue(itemOpis.link)) }
                                                LaunchedEffect(selectMarker, updateSelMarker) {
                                                    textFLink.value = TextFieldValue(itemOpis.link)
                                                }
                                                MyTextField(
                                                    textFLink,
                                                    label = "Ссылка",
                                                    hint = "... Поле для ссылки ...",
                                                    modifier = Modifier,
                                                    onValueChange = {
                                                        textFLink.value = it
                                                        changeSelectedItem(
                                                            itemOpis,
                                                            itemOpis.copy(link = it.text)
                                                        )
                                                    },
                                                    interactionSource = interactionSource,
                                                    style = textFieldLink
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        LaunchedEffect(selectMarker, updateSelMarker) {

                            if (selectMarker == itemOpis.sort) {
                                if (itemOpis !is ItemComplexOpisImageGroup || itemOpis.enableText) focusRequester.requestFocus()
                            }
                        }
                        LaunchedEffect(selectionItemOpis.selected) {
                            if (selectionItemOpis.selected == itemOpis) {
                                layoutCoordinatesItems?.let {
                                    layoutCoordinatesInScrollBox.value?.localBoundingBoxOf(it)?.let { rectItem ->
                                        scrollBoxLC.value?.let { scrollBox ->
                                            if (scroll.maxValue != Int.MAX_VALUE && scroll.maxValue > 0) {

                                                if (rectItem.size.height < scrollBox.size.height - 10.dp.toPx()) {


                                                    if (rectItem.bottom > heightScrollbar() + scroll.value - 10.dp.toPx()
                                                            .toInt()
                                                    ) {

                                                        scroll.scrollTo(
                                                            rectItem.bottom.toInt() - heightScrollbar() + 10.dp.toPx()
                                                                .toInt()
                                                        )
                                                    }
                                                    if (rectItem.top < scroll.value + 10.dp.toPx().toInt()) {

                                                        scroll.scrollTo((rectItem.top - 10.dp.toPx()).toInt())
                                                    }
                                                } else {

                                                    if (rectItem.top > heightScrollbar() + scroll.value - 10.dp.toPx()
                                                            .toInt()
                                                    ) {

                                                        scroll.scrollTo(
                                                            rectItem.top.toInt() - heightScrollbar() + 10.dp.toPx()
                                                                .toInt()
                                                        )
                                                    }
                                                    if (rectItem.bottom < scroll.value + 10.dp.toPx().toInt()) {

                                                        scroll.scrollTo((rectItem.bottom - 10.dp.toPx()).toInt())
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        LaunchedEffect(Unit) {
                            if (listOpis.filter { it.sort > 0 }.lastOrNull() == itemOpis) {
                                focusRequesterLast.value = focusRequester
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun commonItemButtPanel(
        styleState: ComplexOpisStyleState,
        dialLay: MyDialogLayout,
        itemOpis: ItemComplexOpisTextCommon
    ) {
        with(styleState) {
            MyTextButtWithoutBorder(
                "\uD83E\uDC45",
                Modifier.padding(start = 20.dp),
                fontSize = 24.sp,
                textColor = colorArrow
            ) {
                val secondElem = listOpis.findLast {
                    it.sort < itemOpis.sort
                }
                val old = itemOpis.sort
                var new = itemOpis.sort
                secondElem?.let { second ->
                    new = second.sort
                    changeSelectedItem(itemOpis, itemOpis.myCommonCopy(sort = new))
                    changeNotSelectedItem(second, second.myCommonCopy(sort = old))
                }
                listOpis.sortBy { it.sort }
                updSelectMarker(new)
            }
            MyTextButtWithoutBorder(
                "\uD83E\uDC47", Modifier.padding(horizontal = 10.dp),
                fontSize = 24.sp,
                textColor = colorArrow
            ) {
                val secondElem = listOpis.find {
                    it.sort > itemOpis.sort
                }
                val old = itemOpis.sort
                var new = itemOpis.sort
                secondElem?.let { second ->
                    new = second.sort
                    changeNotSelectedItem(second, second.myCommonCopy(sort = old))
                    changeSelectedItem(itemOpis, itemOpis.myCommonCopy(sort = new))
                }
                listOpis.sortBy { it.sort }
                updSelectMarker(new)
            }

            MyIconButtWithoutBorder(
                "ic_baseline_save_24.xml",
                sizeIcon = 30.dp,
                myStyleButton = styleState.buttIconSaveShablon
            ) {
                MyOneVopros(dialLay, "Введите имя нового шаблона", "Добавить", "Имя шаблона") {
                    if (it != "") {
                        MainDB.addComplexOpis.addComplexOpisShablon(ItemComplexOpisShablon(it, itemOpis))
                    }
                }
            }




            MyIconButtWithoutBorder(
                "ic_baseline_close_24.xml",
                sizeIcon = 30.dp,
                onDoubleClick = {
                    val delSort = itemOpis.sort
                    listOpis.filter {
                        it.sort > itemOpis.sort
                    }.map { it.myCommonCopy() }.forEach { findItem ->
                        changeNotSelectedItem(
                            findItem,
                            findItem.myCommonCopy(sort = findItem.sort - 1)
                        )
                    }
                    if (itemOpis.id > 0)
                        changeSelectedItem(itemOpis, itemOpis.myCommonCopy(sort = -1))
                    else {
                        listOpis.remove(itemOpis)
                        saveTextField.remove(itemOpis)
                    }
                    selectionItemOpis.selected =
                        listOpis.find { it.sort == delSort } ?: listOpis.filter { it.sort > 0 }.lastOrNull()
                    listOpis.sortBy { it.sort }
                    updSelectMarker(selectionItemOpis.selected?.sort ?: -1)
                }, myStyleButton = styleState.buttIconDelItem
            ) {}
        }
    }

    @Composable
    fun RowScope.imageGroupItemButtPanel(
        styleState: ComplexOpisStyleState,
        dialLay: MyDialogLayout,
        itemOpis: ItemComplexOpisImageGroup,
        myPreviewSizeComboBox: MyComboBox<Pair<Long, String>>
    ) {
        with(styleState) {
            MyTextToggleButtWithoutBorder(
                "T",
                mutableStateOf(itemOpis.enableText),
                Modifier.padding(start = 10.dp),
                fontSize = 24.sp,
                textStyle = MyTextStyleParam.style1.copy(fontStyle = FontStyle.Italic),
                textColor = colorToggleTextFalse,
                textColorTrue = colorToggleTextTrue
            ) {
                changeSelectedItem(
                    itemOpis,
                    itemOpis.copy(enableText = it)
                )
            }
            MyTextToggleButtWithoutBorder(
                "↹",
                mutableStateOf(itemOpis.textBefore),
                Modifier.padding(start = 10.dp),
                fontSize = 24.sp,
                textStyle = MyTextStyleParam.style1,
                textColor = colorToggleTextFalse,
                textColorTrue = colorToggleTextTrue
            ) {
                changeSelectedItem(
                    itemOpis,
                    itemOpis.copy(textBefore = it)
                )
            }
            MyTextToggleButtWithoutBorder(
                "W",
                mutableStateOf(itemOpis.widthLimit),
                Modifier.padding(start = 10.dp),
                fontSize = 24.sp,
                textStyle = MyTextStyleParam.style1.copy(fontStyle = FontStyle.Italic),
                textColor = colorToggleTextFalse,
                textColorTrue = colorToggleTextTrue
            ) {
                changeSelectedItem(
                    itemOpis,
                    itemOpis.copy(widthLimit = it)
                )
            }
            myPreviewSizeComboBox.show(Modifier.padding(start = 10.dp), comboBoxSizeImage)
            MyTextButtWithoutBorder(
                "+", Modifier.padding(horizontal = 10.dp),
                fontSize = 24.sp,
                textColor = colorArrow
            ) {
                getImageForIG(dialLay) { tempIdImage ->
                    changeSelectedItem(
                        itemOpis,
                        itemOpis.copy(
                            spisImages = itemOpis.spisImages.toMutableList().apply {
                                add(
                                    ItemComplexOpisImage(
                                        tempIdImage,
                                        -1L,
                                        this.filter { it.sort > 0 }.size + 1L,
                                        true
                                    )
                                )
                            })
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            if (itemOpis.spisImages.filter { it.sort > 0 }.size > 1 && selectionImage.selected != null) {
                Box(Modifier.padding(start = 0.dp).defaultMinSize(minHeight = 10.dp).width(30.dp)) {
                    if (itemOpis.spisImages.firstOrNull() != selectionImage.selected) MyTextButtWithoutBorder(
                        "\uD83E\uDC44",
                        Modifier,
                        fontSize = 24.sp,
                        textColor = colorArrow
                    ) {
                        selectionImage.selected?.let { img ->
                            val tempListImg = itemOpis.spisImages.toMutableList()
                            val secondElem = tempListImg.findLast {
                                it.sort < img.sort
                            }
                            secondElem?.let { second ->
                                val old = img.sort
                                val new = second.sort
                                changeSelectedImage(tempListImg, img, img.copy(sort = new))
                                changeNotSelectedImage(tempListImg, second, second.copy(sort = old))
                            }
                            tempListImg.sortBy { it.sort }
                            changeSelectedItem(itemOpis, itemOpis.copy(spisImages = tempListImg))
                        }
                    }
                }
                Box(Modifier.padding(start = 0.dp).defaultMinSize(minHeight = 10.dp).width(30.dp)) {
                    if (itemOpis.spisImages.lastOrNull() != selectionImage.selected) MyTextButtWithoutBorder(
                        "\uD83E\uDC46",
                        fontSize = 24.sp,
                        textColor = colorArrow
                    ) {
                        selectionImage.selected?.let { img ->
                            val tempListImg = itemOpis.spisImages.toMutableList()
                            val secondElem = tempListImg.find {
                                it.sort > img.sort
                            }
                            secondElem?.let { second ->
                                val old = img.sort
                                val new = second.sort
                                changeSelectedImage(tempListImg, img, img.copy(sort = new))
                                changeNotSelectedImage(tempListImg, second, second.copy(sort = old))
                            }
                            tempListImg.sortBy { it.sort }
                            changeSelectedItem(itemOpis, itemOpis.copy(spisImages = tempListImg))
                        }
                    }
                }




                MyIconButtWithoutBorder(
                    "ic_baseline_close_24.xml",
                    sizeIcon = 30.dp,
                    onDoubleClick = {
                        selectionImage.selected?.let {
                            val delSort = it.sort
                            var tmpList = itemOpis.spisImages.toMutableList()
                            itemOpis.spisImages.filter {
                                it.sort > delSort
                            }.map { it.copy() }.forEach { findItem ->
                                tmpList = changeSelectedImage(
                                    tmpList,
                                    findItem,
                                    findItem.copy(sort = findItem.sort - 1)
                                )?.toMutableList() ?: itemOpis.spisImages.toMutableList()
                            }
                            if (it.opis_id > 0) {
                                tmpList = changeSelectedImage(
                                    tmpList,
                                    it,
                                    it.copy(sort = -1L)
                                )?.toMutableList() ?: itemOpis.spisImages.toMutableList()
                            } else {
                                tmpList.remove(it)
                            }
                            tmpList.sortBy { it.sort }
                            changeSelectedItem(
                                itemOpis,
                                itemOpis.copy(spisImages = tmpList)
                            )
                            if (it.opis_id > 0) selectionImage.selected =
                                itemOpis.spisImages.filter { it.sort >= delSort }.firstOrNull()
                        }
                    }, myStyleButton = buttIconDelImage
                ) {}
            }
            MyTextButtWithoutBorder(
                "\uD83D\uDC40", Modifier.padding(start = 10.dp),
                fontSize = 24.sp,
                textColor = colorArrow
            ) {
                PanViewImageList(
                    itemOpis.spisImages.filter { it.sort > 0 }.sortedBy { it.sort }
                        .map {
                            File(
                                StateVM.run { if (it.opis_id > 0) dirComplexOpisImages else dirTemp },
                                "complexOpisImage_${if (it.opis_id > 0) "" else "tmp_"}${it.id}.jpg"
                            )
                        })
            }
        }
    }

    @Composable
    fun textStylePanel(
        styleState: ComplexOpisStyleState,
        itemOpis: ItemComplexOpisTextCommon,
        myFontComboBox: MyComboBox<Pair<Long, TextUnit>>,
        myColorComboBox: MyComboBox<Pair<Long, Color>>
    ) {
        with(styleState) {
            RowVA(Modifier, horizontalArrangement = Arrangement.Center) {
                MyTextToggleButtWithoutBorder(
                    "К",
                    mutableStateOf(itemOpis.cursiv),
                    fontSize = 24.sp,
                    textStyle = MyTextStyleParam.style1.copy(fontStyle = FontStyle.Italic),
                    textColor = colorToggleTextFalse,
                    textColorTrue = colorToggleTextTrue
                ) {
                    changeSelectedItem(
                        itemOpis,
                        itemOpis.myCopy(cursiv = it)
                    )
                }
                MyTextToggleButtWithoutBorder(
                    "Ж",
                    mutableStateOf(itemOpis.bold == 1),
                    Modifier.padding(start = 10.dp),
                    fontSize = 24.sp,
                    textStyle = MyTextStyleParam.style1.copy(fontWeight = FontWeight.Bold),
                    textColor = colorToggleTextFalse,
                    textColorTrue = colorToggleTextTrue
                ) {
                    changeSelectedItem(
                        itemOpis,
                        itemOpis.myCopy(bold = if (it) 1 else 0)
                    )
                }
                myColorComboBox.show(
                    Modifier.padding(start = 10.dp),
                    comboBoxColor
                )
                myFontComboBox.show(
                    Modifier.padding(start = 10.dp),
                    comboBoxFont
                )
                if (itemOpis is ItemComplexOpisCheckbox) MyTextToggleButtWithoutBorder(
                    "М",
                    mutableStateOf(itemOpis.many_type),
                    Modifier.padding(start = 10.dp),
                    fontSize = 24.sp,
                    textStyle = MyTextStyleParam.style1.copy(fontStyle = FontStyle.Italic),
                    textColor = colorToggleTextFalse,
                    textColorTrue = colorToggleTextTrue
                ) {
                    changeSelectedItem(
                        itemOpis,
                        itemOpis.copy(
                            many_type = it, checked = if (it) itemOpis.checked else {
                                if (itemOpis.checked > 1L) 0L else itemOpis.checked
                            }
                        )
                    )
                }

            }
        }
    }


    @Composable
    fun addButton(dialLay: MyDialogLayout, styleState: ComplexOpisStyleState) {
        with(styleState) {

            val myShablonComboBox = remember {
                MyComboBox(
                    MainDB.complexOpisSpis.spisComplexOpisShablon, comItem = { _, _ -> "Ш" },
                    comItemExp = { item, hover, exp, selfun ->
                        RowVA {
                            RowVA(Modifier.weight(1f).clickable {
                                selfun.invoke()
                                exp.value = false
                            }) {
                                Box(
                                    Modifier.padding(2.dp)
                                        .offset(
                                            2.dp - (if (hover) 4f else 2f).dp,
                                            2.dp - (if (hover) 4f else 2f).dp
                                        )
                                ) {
                                    Image(
                                        painterResource(
                                            when (item.typeOpisBlock) {
                                                TypeOpisBlock.simpleText -> "ic_baseline_text_fields_24.xml"
                                                TypeOpisBlock.checkbox -> "ic_baseline_check_box_24.xml"
                                                TypeOpisBlock.link -> "ic_round_keyboard_double_arrow_up_24.xml"
                                                TypeOpisBlock.image -> "ic_baseline_wallpaper_24.xml"
                                            }
                                        ),
                                        "buttIconStyle1",
                                        Modifier.height(25.dp).width(25.dp),
                                        contentScale = ContentScale.Fit,
                                        colorFilter = ColorFilter.tint(colorIconShablon, BlendMode.Modulate),
                                    )
                                }
                                Text(
                                    item.name,
                                    modifier = Modifier.padding(start = 10.dp).weight(1f),
                                    style = baseText.copy(
                                        textAlign = TextAlign.Start,
                                        color = colorSet.getColor(item.color),
                                        fontSize = fontSet.getFontSize(item.fontSize),
                                        fontStyle = if (item.cursiv) FontStyle.Italic else FontStyle.Normal,
                                        fontWeight = FontWeight(if (item.bold == 1) 600 else 400)
                                    )
                                )
                            }
                            MyIconButtWithoutBorder(
                                "ic_baseline_close_24.xml",
                                sizeIcon = 30.dp,
                                onDoubleClick = {
                                    MainDB.addComplexOpis.deleteShablon(item)
                                }, myStyleButton = buttIconDelShablon
                            ) {}

                        }
                    },
                    listenerInStart = false,
                    nameItem = { "" },
                    plateOrder = true,
                    width = 250.dp,
                    emptyListMessage = "Нет шаблонов"
                ) { itemShablon ->
                    itemShablon.getItemOpis(tableName.nameTable, item_id, listOpis.filter { it.sort > 0 }.size + 1L)
                        .let {
                            when (it) {
                                is ItemComplexOpisImageGroup -> {
                                    getImageForIG(dialLay) { tempIdImage ->
                                        addNewItem(
                                            it.copy(
                                                spisImages = listOf(
                                                    ItemComplexOpisImage(
                                                        tempIdImage,
                                                        -1L,
                                                        1L,
                                                        true
                                                    )
                                                )
                                            )
                                        )
                                    }
                                }

                                else -> addNewItem(it)
                            }
                        }
                }
            }
            RowVA(
                Modifier.fillMaxWidth().padding(top = 5.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                MyIconButtStyle(
                    "ic_baseline_text_fields_24.xml",
                    width = 50.dp,
                    height = 35.dp,
                    myStyleButton = buttIconAddFieldOpis
                ) {
                    val item = ItemComplexOpisText(
                        -1L,
                        tableName.nameTable,
                        item_id,
                        TypeOpisBlock.simpleText,
                        listOpis.filter { it.sort > 0 }.size + 1L,
                        text = "",
                        color = 1,
                        fontSize = 3,
                        cursiv = false,
                        bold = 4
                    )
                    addNewItem(item)
                }
                MyIconButtStyle(
                    "ic_baseline_check_box_24.xml",
                    Modifier.padding(start = 10.dp),
                    sizeIcon = 30.dp,
                    width = 50.dp,
                    height = 35.dp,
                    myStyleButton = buttIconAddFieldOpis
                ) {
                    val item = ItemComplexOpisCheckbox(
                        -1L,
                        tableName.nameTable,
                        item_id,
                        TypeOpisBlock.simpleText,
                        listOpis.filter { it.sort > 0 }.size + 1L,
                        text = "",
                        color = 1,
                        fontSize = 3,
                        cursiv = false,
                        bold = 4,
                        checked = 0L,
                        many_type = false
                    )
                    addNewItem(item)
                }
                MyIconButtStyle(
                    "ic_round_keyboard_double_arrow_up_24.xml",
                    Modifier.padding(start = 10.dp),
                    sizeIcon = 30.dp,
                    width = 50.dp,
                    height = 35.dp,
                    myStyleButton = buttIconAddFieldOpis
                ) {
                    val item = ItemComplexOpisLink(
                        -1L,
                        tableName.nameTable,
                        item_id,
                        TypeOpisBlock.link,
                        listOpis.filter { it.sort > 0 }.size + 1L,
                        text = "",
                        color = 1,
                        fontSize = 3,
                        cursiv = false,
                        bold = 4,
                        link = ""
                    )
                    addNewItem(item)
                }
                MyIconButtStyle(
                    "ic_baseline_wallpaper_24.xml",
                    Modifier.padding(start = 10.dp),
                    sizeIcon = 30.dp,
                    width = 50.dp,
                    height = 35.dp,
                    myStyleButton = buttIconAddFieldOpis
                ) {
                    getImageForIG(dialLay) { tempIdImage ->
                        val item = ItemComplexOpisImageGroup(
                            -1L,
                            tableName.nameTable,
                            item_id,
                            TypeOpisBlock.simpleText,
                            listOpis.filter { it.sort > 0 }.size + 1L,
                            text = "",
                            color = 1,
                            fontSize = 3,
                            cursiv = false,
                            bold = 4,
                            sizePreview = 2,
                            widthLimit = true,

                            enableText = false,
                            textBefore = false,
                            spisImages = listOf(ItemComplexOpisImage(tempIdImage, -1L, 1L, true))
                        )
                        addNewItem(item)
                    }
                }
                myShablonComboBox.show(Modifier.padding(start = 10.dp), comboBoxShablon)
                selectionItemOpis.selected?.let { selIt ->
                    if (selIt is ItemComplexOpisTextCommon) selIt.myCopy(
                        id = -1L,
                        text = "",
                        sort = listOpis.filter { it.sort > 0 }.size + 1L
                    ).let { selItem ->
                        MyIconButtStyle(
                            "ic_baseline_content_copy_24.xml",
                            Modifier.padding(start = 10.dp),
                            sizeIcon = 30.dp,
                            width = 50.dp,
                            height = 35.dp,
                            myStyleButton = buttIconAddFieldOpis
                        ) {
                            when (selItem) {
                                is ItemComplexOpisImageGroup -> {
                                    getImageForIG(dialLay) { tempIdImage ->
                                        addNewItem(
                                            selItem.copy(
                                                spisImages = listOf(
                                                    ItemComplexOpisImage(
                                                        tempIdImage,
                                                        -1L,
                                                        1L,
                                                        true
                                                    )
                                                )
                                            )
                                        )
                                    }
                                }

                                is ItemComplexOpisCheckbox -> addNewItem(selItem.copy(checked = 0L))
                                is ItemComplexOpisLink -> addNewItem(selItem.copy(link = ""))
                                is ItemComplexOpisText -> addNewItem(selItem)
                            }
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun ColumnScope.listItemsView(styleState: ComplexOpisStyleState, dialLay: MyDialogLayout) {

        var layoutCoordinatesInScrollBox: MutableState<LayoutCoordinates?> = remember { mutableStateOf(null) }
        var scrollBoxLC: MutableState<LayoutCoordinates?> = remember { mutableStateOf(null) }
        var heightBox: MutableState<Dp> = remember { mutableStateOf(0.dp) }
        fun heightScrollbar() = scrollBoxLC.value?.size?.height ?: 230
        val focusRequesterLast: MutableState<FocusRequester?> = remember { mutableStateOf(null) }

        with(LocalDensity.current) {
            with(styleState) {
                Box(Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
                    BoxWithVScrollBar(Modifier.fillMaxSize()
                        .onGloballyPositioned {
                            scrollBoxLC.value = it
                        }
                    ) { scroll ->
                        LaunchedEffect(newItemMarker) {
                            if (newItemMarker != -1) {
                                awaitAll(async { scroll.scrollTo(scroll.maxValue) })

                                updSelectMarker(listOpis.maxOf { it.sort })
                            }
                        }
                        Column(
                            Modifier

                                .verticalScroll(scroll, enabled = true)

                                .onGloballyPositioned {
                                    layoutCoordinatesInScrollBox.value = it
                                    scrollBoxLC.value?.let { visObl ->


                                        if (it.size.height < visObl.size.height) {
                                            heightBox.value =
                                                (visObl.size.height - it.size.height).toDp()
                                        } else if (heightBox.value != 0.dp) heightBox.value = 0.dp
                                    }
                                }
                        ) {
                            val focusManager = LocalFocusManager.current
                            listOpis.filter { it.sort > 0 }.forEach { itemOpis ->
                                risItem(
                                    itemOpis, scroll, styleState,

                                    layoutCoordinatesInScrollBox,
                                    scrollBoxLC,

                                    ::heightScrollbar,
                                    dialLay = dialLay,
                                    focusRequesterLast = focusRequesterLast
                                )
                            }
                        }
                    }
                    Box(Modifier
                        .fillMaxWidth()
                        .height(heightBox.value)

                        .clickable(remember { MutableInteractionSource() }, null) {
                            listOpis.filter { it !is ItemComplexOpisImageGroup || it.enableText }.lastOrNull()
                                ?.let { lastItem ->
                                    focusRequesters.find { it.first == lastItem.sort }?.let {
                                        it.second.requestFocus()
                                    }
                                }

                        }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun commonAllPanel(styleState: ComplexOpisStyleState, content: @Composable ColumnScope.() -> Unit) {
        val interactionSourceHover: MutableInteractionSource = remember { MutableInteractionSource() }
        val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
        val focus by interactionSource.collectIsFocusedAsState()
        val hover by interactionSourceHover.collectIsHoveredAsState()
        with(styleState) {
            with(LocalDensity.current) {
                (if (focus || hover) textFieldOpis.textNamePoleFocus else textFieldOpis.textNamePole).let { textName ->
                    MeasureUnconstrainedViewWidth(
                        {
                            Text(
                                label,
                                style = textName
                            )
                        }
                    ) { widthText ->
                        MyShadowBox(plateEdit.shadow) {
                            Box(Modifier.hoverable(interactionSourceHover).focusable(true, interactionSource)) {
                                Column(
                                    Modifier
                                        .padding(top = (textName.fontSize / 2).toDp())
                                        .background(
                                            plateEdit.BACKGROUND,
                                            plateEdit.shape
                                        )
                                        .onKeyEvent {
                                            when (it.key.keyCode) {
                                                Key.PageDown.keyCode -> {
                                                    setNextFocus()
                                                    true
                                                }

                                                Key.PageUp.keyCode -> {
                                                    setPrevFocus()
                                                    true
                                                }

                                                Key.Tab.keyCode -> {
                                                    if (it.isCtrlPressed) {
                                                        val time = Date().time
                                                        if (time - timeKeyEvent.value > 200) {
                                                            timeKeyEvent.value = time
                                                            focusName?.requestFocus()
                                                        }

                                                    }
                                                    true
                                                }

                                                Key.V.keyCode -> {
                                                    if (it.isCtrlPressed) {
                                                        loadImageFromBuffer()
                                                    } else false
                                                }

                                                else -> false
                                            }
                                        }

                                        .then(inner_padding_edit)
                                ) {
                                    content()
                                }
                                Box(Modifier.matchParentSize().padding(top = (textName.fontSize / 2).toDp())
                                    .drawWithCache {
                                        onDrawWithContent {
                                            clipRect(
                                                (textFieldOpis.START_NAME - 5.dp).toPx(),
                                                0f,
                                                (textFieldOpis.START_NAME + 5.dp + widthText).toPx(),
                                                textName.fontSize.toPx(),
                                                ClipOp.Difference
                                            ) {
                                                this@onDrawWithContent.drawContent()
                                            }
                                        }
                                    }
                                    .border(plateEdit.BORDER_WIDTH, plateEdit.BORDER, plateEdit.shape)
                                )
                                Text(
                                    label,
                                    Modifier.padding(start = textFieldOpis.START_NAME),
                                    style = textName
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun show(modifier: Modifier, dialLay: MyDialogLayout, styleState: ComplexOpisStyleState) {
        with(styleState) {
            val myColorComboBox = remember {
                MyComboBox(list = colorSet.getList(), comItem = { item, hover ->
                    Box(
                        modifier = Modifier
                            .padding(end = 2.dp)
                            .padding(vertical = 0.dp)
                            .width(25.dp)
                            .height(25.dp)
                            .border(1.dp,
                                MyColorARGB
                                    .run { if (hover) colorMyBorderStroke else colorMyBorderStrokeCommon }
                                    .toColor(),
                                RoundedCornerShape(5.dp)
                            )
                            .padding(3.dp)
                            .background(
                                item.second,
                                RoundedCornerShape(5.dp)
                            )
                    )
                }, nameItem = { "" }, plateOrder = true, width = 100.dp, listenerInStart = false) { itemColor ->
                    selectionItemOpis.selected?.let { itemOp ->
                        when (itemOp) {
                            is ItemComplexOpisTextCommon -> {
                                changeSelectedItem(itemOp, itemOp.myCopy(color = itemColor.first))
                            }
                        }
                    }
                }
            }
            val myFontComboBox = remember {
                MyComboBox(list = fontSet.getList(), { item, hover ->
                    (baseText.shadow?.offset ?: Offset(2f, 2f)).let { offset ->
                        Box(
                            Modifier
                                .height(25.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Аа",
                                modifier = Modifier
                                    .padding(end = 2.dp)
                                    .offset(
                                        offset.x.dp - (if (hover) offset.x * 2 else offset.x).dp,
                                        offset.y.dp - (if (hover) offset.y * 2 else offset.y).dp
                                    ),
                                style = baseText.copy(fontSize = item.second)
                            )
                        }
                    }
                }, { "" }, width = 40.dp, listenerInStart = false) { itemFont ->
                    selectionItemOpis.selected?.let { itemOp ->
                        when (itemOp) {
                            is ItemComplexOpisTextCommon -> {
                                changeSelectedItem(itemOp, itemOp.myCopy(fontSize = itemFont.first))
                            }
                        }
                    }
                }
            }
            val listSizePreview = listOf(1L to "S", 2L to "M", 3L to "L")
            val myPreviewSizeComboBox = remember {
                MyComboBox(
                    list = listSizePreview,
                    nameItem = { it.second },
                    width = 40.dp, listenerInStart = false
                ) { sizeNew ->
                    selectionItemOpis.selected?.let { itemOp ->
                        if (itemOp is ItemComplexOpisImageGroup) changeSelectedItem(
                            itemOp,
                            itemOp.copy(sizePreview = sizeNew.first)
                        )
                    }
                }
            }

            LaunchedEffect(selectionItemOpis.selected) {
                selectionItemOpis.selected?.let { itemOp ->
                    when (itemOp) {
                        is ItemComplexOpisTextCommon -> {
                            myFontComboBox.select(fontSet.getPairFontSize(itemOp.fontSize))
                            myColorComboBox.select(colorSet.getPairColor(itemOp.color))
                            if (itemOp is ItemComplexOpisImageGroup) myPreviewSizeComboBox.select(listSizePreview.find { it.first == itemOp.sizePreview }
                                ?: (1L to "S"))
                        }
                    }
                }
            }
            commonAllPanel(styleState) {
                listItemsView(styleState, dialLay)
                selectionItemOpis.selected?.let { itemOpis ->
                    MyShadowBox(plateEditButt.shadow) {
                        RowVA(
                            Modifier.padding(top = 5.dp).fillMaxWidth().withSimplePlate(plateEditButt)
                                .padding(vertical = 2.dp, horizontal = 10.dp)
                        ) {
                            when (itemOpis) {
                                is ItemComplexOpisTextCommon -> {
                                    if (itemOpis !is ItemComplexOpisImageGroup || itemOpis.enableText) textStylePanel(
                                        styleState,
                                        itemOpis,
                                        myFontComboBox,
                                        myColorComboBox
                                    )
                                    if (itemOpis is ItemComplexOpisImageGroup) imageGroupItemButtPanel(
                                        styleState,
                                        dialLay,
                                        itemOpis,
                                        myPreviewSizeComboBox
                                    ) else Spacer(Modifier.weight(1f))
                                }
                            }

                            commonItemButtPanel(styleState, dialLay, itemOpis)
                        }
                    }
                }
                addButton(dialLay, styleState)
            }
        }
    }
}

