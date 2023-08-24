package MainTabs.Setting

import Button
import CommonWindow.BorderWindow
import GooglePack.sess
import MainTabs.Setting.Items.*
import MyDialog.MyDialogLayout
import MyDialog.MyOneVopros
import MyDialog.MyOneVoprosTransit
import MyList
import MyListRow
import MyShowMessage
import adapters.MyComboBox
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import common.*
import common.color.ColorPicker
import extensions.*
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemSaveSetStyle
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.CommonInterfaceSetting
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.TypeSaveStyleSet
import viewmodel.MainDB
import viewmodel.StateVM
import viewmodel.StyleDB
import java.io.File
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WindowEditStyle() {
    val state = rememberWindowState(
        WindowPlacement.Floating,
        position = WindowPosition(Alignment.TopEnd),
        width = 1060.dp,
        height = 870.dp
    )

    val colorPicker = remember { ColorPicker() }
    val selIndGradColor = remember { mutableStateOf(-1) }

    @Composable
    fun listColorOfGradient(gradient: MutableState<List<MyColorARGB>>, dialLay: MyDialogLayout) {
        val mutColor = remember { mutableStateOf(MyColorARGB.colorBackGr2) }
        LaunchedEffect(mutColor.value) {
            if (selIndGradColor.value >= 0 && selIndGradColor.value < gradient.value.size) {
                gradient.value = gradient.value.toMutableList().apply {
                    removeAt(selIndGradColor.value)
                    add(selIndGradColor.value, mutColor.value)
                }
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            RowVA(Modifier.padding(horizontal = 10.dp).padding(top = 10.dp)) {
                if (selIndGradColor.value >= 0 && selIndGradColor.value < gradient.value.size && gradient.value.size > 2) MyTextButtStyle1(
                    "-"
                ) {
                    gradient.value = gradient.value.toMutableList().apply {
                        removeAt(selIndGradColor.value)
                    }
                }
                Box(Modifier.weight(1f, false)) {
                    MyListRow(gradient.value, maxWidth = 500) { ind, item ->
                        Box(
                            Modifier.padding(5.dp).height(20.dp).width(60.dp).background(item.toColor())
                                .border(1.dp, if (selIndGradColor.value == ind) Color.Red else Color.Black).clickable {
                                    selIndGradColor.value = ind
                                    mutColor.value = item
                                    colorPicker.setColorEditObj(mutColor)
                                }
                        )
                    }
                }
                MyTextButtStyle1("+") {
                    gradient.value = gradient.value.toMutableList().apply {
                        this.lastOrNull()?.let {
                            add(it)
                        }
                    }
                }
            }
            if (selIndGradColor.value >= 0 && selIndGradColor.value < gradient.value.size) {
                colorPicker.show(dialLay)
            } else Spacer(Modifier.weight(1f))
        }
    }

//    val editColor: MutableState<ItrCommObserveMutableObj<MyColorARGB>?> = remember { mutableStateOf(null) }
    val dialogLayout = remember { MyDialogLayout() }
    val selectionInterSett = remember { SingleSelectionType<CommonInterfaceSetting.InterfaceSettingsType>() }
    val selectionRazdelSetting = remember { SingleSelectionType<CommonInterfaceSetting.RazdelSetting>() }
    val summa = remember { mutableStateOf(TextFieldValue("0.0")) }
    val angle = remember { mutableStateOf(TextFieldValue("0")) }
    val angleLong = remember { mutableStateOf(0.0) }
    val labelDouble = remember { mutableStateOf("Значение") }

    val CB_spisSaveSetStyle = MyComboBox(MainDB.editStyleSpis.spisSaveSetStyleFull, nameItem = { it.name }) {
//        MainDB.editStyleFun.setSelectSetColorLibrary(it.id)
    }


    fun loadStyleShablon(item: ItemSaveSetStyle, razdel: CommonInterfaceSetting.RazdelSetting) {
        if (razdel.type_razdel != TypeSaveStyleSet.NOTSAVE) {
            if (razdel.type_razdel != TypeSaveStyleSet.FULL) {
                MainDB.addEditStyle.loadSaveSetStyleCommon(
                    set_id = item.id,
                    razdel.code_name_razdel
                )
            } else {
                MainDB.addEditStyle.loadSaveSetStyleCommon(
                    set_id = item.id,
                    ""
                )
//                MainDB.addEditStyle.loadSaveSetStyleFull(
//                    id = item.id
//                )
            }
        }
    }

    @Composable
    fun Donut() {
        val shape = object : Shape {
            override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
                val thickness = size.height / 4
                val p1 = Path().apply {
                    addOval(Rect(0f, 0f, size.width - 1, size.height - 1))
                }
                val p2 = Path().apply {
                    addOval(Rect(size.width / 2, size.width / 2, size.width / 2 + 0.00001f, size.width / 2 + 0.00001f))
//                    addOval(Rect(thickness,
//                        thickness,
//                        size.width - 1 - thickness,
//                        size.height - 1 - thickness))
                }
                val p3 = Path()
                p3.op(p1, p2, PathOperation.difference)
                p1.close()
//                return Outline.Generic(p1)
                return Outline.Generic(p3)
            }

        }
        Column {
            Surface(
                modifier = Modifier.width(200.dp).height(200.dp)
                    .clip(shape),
//                .innerShadow(
//                    blur = 20.dp,
//                    color = Color(0xffff0000),
//                    cornersRadius = 20.dp,
//                    offsetX = 0.5.dp,
//                    offsetY = 0.5.dp
//                )
//                .innerShadow(
//                blur = 1.dp,
//                color = Color(0xff00FFFF),
//                cornersRadius = 20.dp,
//                offsetX = (-40.5).dp,
//                offsetY = (-10.5).dp
//            )
                //.shadow(8.dp,shape),
                color = Color.White.copy(0.5f),
//                shape = shape
            ) {
            }
            Surface(
                modifier = Modifier.width(200.dp).height(200.dp).shadow(8.dp, shape, true)
                    .border(1.dp, Color.Red.copy(0.2f), shape),
                color = Color.White.copy(0.3f),
//                shape = shape,
//                elevation = 8.dp
            ) {
            }
            Surface(
                modifier = Modifier.width(200.dp).height(200.dp).shadow(8.dp, shape, false),
                color = Color.White.copy(0.5f),
//                shape = shape
            ) {
            }
            Surface(
                modifier = Modifier.width(200.dp).height(200.dp).shadow(8.dp, shape),
                color = Color.White.copy(0.5f),
                shape = shape,
                elevation = 8.dp
            ) {
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun selectAngle(
        modifier: Modifier = Modifier,
        refresh: Boolean = true
    ) { //intSett: CommonInterfaceSetting.InterfaceSettingsAngle,
        val cursorPositionH = remember { mutableStateOf(Offset(0f, 0f)) }
        fun setAngle(x: Double) {
            selectionInterSett.selected?.let { intSett ->
                if (intSett is CommonInterfaceSetting.InterfaceSettingsAngle) {
                    angleLong.value = if (x < 0.0 || x >= 360.0) 0.0 else x
                    intSett.itrObj.value = angleLong.value.toLong()
                    angle.value = TextFieldValue((angleLong.value.toLong()).toString())
                }
            }
        }
        with(LocalDensity.current) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Canvas(modifier = modifier.padding(5.dp).width(255.dp).height(255.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        angleLong.value = 360f - (getAngle(
                            cursorPositionH.value.x - 127.dp.toPx(),
                            cursorPositionH.value.y - 127.dp.toPx()
                        ) / 2 / Math.PI * 360f + 90f)
                        setAngle(angleLong.value)
//                                intSett.itrObj.value = angleLong.value.toLong()
//                                angle.value = TextFieldValue(angleLong.value.toLong().toString())
                    }
                    .pointerMoveFilter(onMove = {
                        cursorPositionH.value = it
                        true
                    })
                    .pointerInput(Unit) {
                        detectDragGestures() { change, dragAmount ->
                            angleLong.value = 360f - (getAngle(
                                cursorPositionH.value.x - 127.dp.toPx(),
                                cursorPositionH.value.y - 127.dp.toPx()
                            ) / 2 / Math.PI * 360f + 90f)
                            setAngle(angleLong.value)
//                                    intSett.itrObj.value = angleLong.value.toLong()
//                                    angle.value = TextFieldValue(angleLong.value.toLong().toString())
                        }
                    }
                ) {
                    val thickness = size.height / 4
                    val p1 = Path().apply {
                        addOval(Rect(0f, 0f, size.width, size.height))
                    }
                    val p2 = Path().apply {
                        addOval(
                            Rect(
                                thickness,
                                thickness,
                                size.width - thickness,
                                size.height - thickness
                            )
                        )
                    }
                    val p3 = Path()
                    p3.op(p1, p2, PathOperation.Difference)

                    drawPath(p3, color = Color.DarkGray)
//                val shir = 60.dp.toPx()
                    rotate(360f - angleLong.value.toFloat(), Offset(128.dp.toPx(), 128.dp.toPx())) {
                        drawRect(
                            Color.White, //.copy(0.5f),
                            Offset(128.dp.toPx(), -5.dp.toPx()), Size(3.dp.toPx(), thickness + 10.dp.toPx()),
                            style = Stroke(1.5.dp.toPx())
                        )
                    }
                    rotate(180f - angleLong.value.toFloat(), Offset(128.dp.toPx(), 128.dp.toPx())) {
                        drawRect(
                            Color.White.copy(0.5f),
                            Offset(128.dp.toPx(), -5.dp.toPx()), Size(3.dp.toPx(), thickness + 10.dp.toPx()),
                            style = Stroke(1.5.dp.toPx())
                        )
                    }
                }
                PlateOrderLayout(alignmentCenter = true) {
                    for (i in 0..23) {
                        MyTextButtStyle1((i * 15).toString(), Modifier.padding(5.dp)) {
                            setAngle((i * 15L).toDouble())
//                            intSett.itrObj.value = i * 15L
//                            angle.value = TextFieldValue((i * 15L).toString())
                        }
                    }
                }
                RowVA(Modifier.padding(top = 20.dp)) {
                    MyTextButtStyle1("<<", Modifier.padding(5.dp)) {
                        setAngle(angleLong.value - 5)
                    }
                    MyTextButtStyle1("<", Modifier.padding(5.dp)) {
                        setAngle(angleLong.value - 1)
                    }
                    MyOutlinedTextFieldInt(selectionInterSett.selected?.nameSett ?: "", angle)
                    MyTextButtStyle1(">", Modifier.padding(5.dp)) {
                        setAngle(angleLong.value + 1)
                    }
                    MyTextButtStyle1(">>", Modifier.padding(5.dp)) {
                        setAngle(angleLong.value + 5)
                    }
                }
                MyTextButtStyle1("Применить", Modifier.padding(5.dp)) {
                    angle.value.text.toLongOrNull()?.let {
                        setAngle(it.toDouble())
//                        intSett.itrObj.value = it
//                        angleLong.value = it.toDouble()
                    }
                }

            }
        }
    }

    val selectionShablon = remember { SingleSelectionType<ItemSaveSetStyle>() }

    @Composable
    fun editBoxRazdel() = selectionRazdelSetting.selected?.let { intRazd ->
        LaunchedEffect(selectionRazdelSetting.selected) {
            selectionShablon.selected = null
        }
        Column(
            Modifier
                .padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MyTextStyle1(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                text = intRazd.type_razdel.nameSpis,
                textAlign = TextAlign.Center
            )
            Column(
                modifier = Modifier.padding(bottom = 5.dp).weight(1f).animateContentSize().border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(listOf(Color(0xFFFFF7D9), Color(0xFFFFF7D9))),
                    shape = RoundedCornerShape(10.dp)
                ).background(
                    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                    color = Color(0xFFE4E0C7),
                ), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MainDB.editStyleSpis.getSpisSaveSetStyle(intRazd.type_razdel).let { spis ->
                    if (spis?.getState()?.value?.isNotEmpty() == true) {
                        MyList(
                            spis,
                            Modifier.weight(1f).padding(bottom = 10.dp).padding(horizontal = 20.dp),
                            darkScroll = true
                        ) { ind, itemShablonStyle ->
                            ComItemShablonStyle(itemShablonStyle, selectionShablon, doubleClick = {
                                loadStyleShablon(it, intRazd)
                            }) { item, expanded ->
                                MyDropdownMenuItem(expanded, "Загрузить") {
                                    loadStyleShablon(item, intRazd)
                                }
                                MyDropdownMenuItem(expanded, "Изменить") {
                                    MyOneVopros(
                                        dialPan = dialogLayout,
                                        "Введите новое название для шаблона стиля.",
                                        "Изменить",
                                        otvetDefault = item.name
                                    ) {
                                        if (it != "") MainDB.addEditStyle.updSaveSetStyle(item.id, it)
                                    }
                                }
                                MyDeleteDropdownMenuButton(expanded) {
                                    MainDB.addEditStyle.delSaveSetStyle(item.id)
                                }

                            }.getComposable()
                        }
                    } else {
                        Spacer(Modifier.weight(1f))
                        MyTextStyle1(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            text = "Здесь пока нет ни одного шаблона",
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
            if (intRazd.type_razdel == TypeSaveStyleSet.FULL) MyTextButtStyle1(
                "+",
                modifier = Modifier.padding(start = 15.dp).width(80.dp)
            ) {
                MyOneVoprosTransit(
                    dialogLayout,
                    "Введите имя файла для сохраняемого стиля:",
                    "Создать",
                    "Название файла",
                    "NewStyle_${Date().time}"
                ) { nameNewStyle ->
                    val dirS = File(StateVM.dirStyle, "$nameNewStyle.db")
                    if (!dirS.exists()) {
//                        File(dirS).mkdirs()
                        StyleDB(dirS).ObserFM.saveStyle(path = File(StateVM.dirMain,"mainDB.db").path)
//                        openQuest(File(dirQ, "$nameNewQuest.db"))
//                        updateFileList()
                        return@MyOneVoprosTransit false
                    } else {
                        MyShowMessage(dialogLayout, "Стиль с таким именем уже существует")
                        return@MyOneVoprosTransit true
                    }
                }
            }

        }
    }

    val refreshSelAngle = remember { mutableStateOf(false) }

    @Composable
    fun editBoxSett() = selectionInterSett.selected?.let { intSett ->
        when (intSett) {
            is CommonInterfaceSetting.InterfaceSettingsBoolean -> {

            }

            is CommonInterfaceSetting.InterfaceSettingsTypeCorner -> {

            }

            is CommonInterfaceSetting.InterfaceSettingsDoublePozitive -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    PlateOrderLayout(alignmentCenter = true) {
                        for (i in 0..10) {
                            MyTextButtStyle1((i * 0.5).roundToString(1), Modifier.padding(5.dp)) {
                                intSett.itrObj.value = i * 0.5
                            }
                        }
                        for (i in 6..20) {
                            MyTextButtStyle1((i * 1.0).roundToString(1), Modifier.padding(5.dp)) {
                                intSett.itrObj.value = i * 1.0
                            }
                        }
                    }
                    fun setSumm(x: Double) {
                        val tmp = if (x >= 0.0) x else 0.0
                        intSett.itrObj.value = tmp
                        summa.value = TextFieldValue(tmp.roundToString(1))

                    }
                    RowVA(Modifier.padding(top = 20.dp)) {
                        MyTextButtStyle1("<<", Modifier.padding(5.dp)) {
                            setSumm(intSett.itrObj.value - 2)
                        }
                        MyTextButtStyle1("<", Modifier.padding(5.dp)) {
                            setSumm(intSett.itrObj.value - 0.5)
                        }
                        MyOutlinedTextFieldDouble(intSett.nameSett, summa)
                        MyTextButtStyle1(">", Modifier.padding(5.dp)) {
                            setSumm(intSett.itrObj.value + 0.5)
                        }
                        MyTextButtStyle1(">>", Modifier.padding(5.dp)) {
                            setSumm(intSett.itrObj.value + 2)
                        }
                    }
                    MyTextButtStyle1("Применить", Modifier.padding(5.dp)) {
                        summa.value.text.toDoubleOrNull()?.let {
                            setSumm(it)
//                            intSett.itrObj.value = it
                        }
                    }

                }
            }

            is CommonInterfaceSetting.InterfaceSettingsDouble -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    PlateOrderLayout(alignmentCenter = true) {
                        for (i in 0..10) {
                            MyTextButtStyle1((i * 0.5).roundToString(1), Modifier.padding(5.dp)) {
                                intSett.itrObj.value = i * 0.5
                            }
                        }
                        for (i in 6..20) {
                            MyTextButtStyle1((i * 1.0).roundToString(1), Modifier.padding(5.dp)) {
                                intSett.itrObj.value = i * 1.0
                            }
                        }
                    }
                    fun setSumm(x: Double) {
                        intSett.itrObj.value = x
                        summa.value = TextFieldValue(x.roundToString(1))

                    }
                    RowVA(Modifier.padding(top = 20.dp)) {
                        MyTextButtStyle1("<<", Modifier.padding(5.dp)) {
                            setSumm(intSett.itrObj.value - 2)
                        }
                        MyTextButtStyle1("<", Modifier.padding(5.dp)) {
                            setSumm(intSett.itrObj.value - 0.5)
                        }
                        MyOutlinedTextFieldDouble(intSett.nameSett, summa)
                        MyTextButtStyle1(">", Modifier.padding(5.dp)) {
                            setSumm(intSett.itrObj.value + 0.5)
                        }
                        MyTextButtStyle1(">>", Modifier.padding(5.dp)) {
                            setSumm(intSett.itrObj.value + 2)
                        }
                    }
                    MyTextButtStyle1("Применить", Modifier.padding(5.dp)) {
                        summa.value.text.toDoubleOrNull()?.let {
                            setSumm(it)
//                            intSett.itrObj.value = it
                        }
                    }

                }
            }

            is CommonInterfaceSetting.InterfaceSettingsFontWeight -> {
                Column {
                    PlateOrderLayout(alignmentCenter = true) {
                        for (i in 1..9) {
                            MyTextButtStyle1((i).toString(), Modifier.padding(5.dp)) {
                                intSett.itrObj.value = i.toLong()
                            }
                        }
                    }

                }
            }

            is CommonInterfaceSetting.InterfaceSettingsAngle -> {
                selectAngle()
                /*
                                Column {
                                    PlateOrderLayout {
                                        for (i in 0..71) {
                                            MyTextButtStyle1((i * 5).toString(), Modifier.padding(5.dp)) {
                                                intSett.itrObj.value = i * 5L
                                            }
                                        }
                                    }
                                    MyOutlinedTextFieldInt(intSett.nameSett, angle)
                                    MyTextButtStyle1("Применить", Modifier.padding(5.dp)) {
                                        angle.value.text.toLongOrNull()?.let {
                                            intSett.itrObj.value = it
                                        }
                                    }

                                }
                */
            }

            is CommonInterfaceSetting.InterfaceSettingsLong -> {

            }

            is CommonInterfaceSetting.InterfaceSettingsMyColor -> {
                colorPicker.show(dialogLayout)
            }

            is CommonInterfaceSetting.InterfaceSettingsMyColorGradient -> {
                listColorOfGradient(intSett.itrObj, dialogLayout)
            }

            is CommonInterfaceSetting.InterfaceSettingsString -> {

            }

            else -> {}
        }
    }

    @Composable
    fun editBox() =
        if (selectionRazdelSetting.selected != null) editBoxRazdel() else if (selectionInterSett.selected != null) editBoxSett() else Donut()

    Window(
        onCloseRequest = {
            StateVM.openEditStyle.value = false
            /**
             * Код отсюда выполняется при нажатии комбинации клавиш Alt + F4
             * */
            sess?.stop(10, 10)
            println("закрыт редактор стилей!!!!")
        },

        onKeyEvent = { saf ->
            /**
             * Здесь можно делать проверку на то какая клавиша была нажата и по итогу выполнять какой-нибудь код.
             * После нужно вернуть true или false, если true - то больше ничего выполняться не будет, видимо
             * подразумевается, что клавиша перехвачена и выполнен собственный код. Если false - то после будут
             * еще выполнены действия по умолчанию, которые идут я так понимаю от системы и т.д. Такие как вызов контекстного
             * меню окна из которого его можно закрыть, свернуть и т.д. Фокус на это меню переходит после нажатия
             * на клавишу Alt, которая похоже иногда дает дополнительное ложное срабатывание после комбинации Alt + Shift
             * для смены раскладки.
             * */
            saf.key == Key.AltLeft || saf.key == Key.AltRight
        },
//        title = "Tutatores",
        state = state
        /*WindowState(
    WindowPlacement.Floating,
    position = WindowPosition(Alignment.Center),
    width = 800.dp,
    height = 800.dp
)*/,
        transparent = true,
        undecorated = true
    ) {
//        LaunchedEffect(selectionInterSett.selected) {
//            refreshSelAngle.value = !refreshSelAngle.value
//        }
        Box(
            Modifier.clip(RoundedCornerShape(14.dp))
                .border(BorderStroke(0.25.dp, Color(0x2FFFF7D9)), RoundedCornerShape(15.dp))
        ) {
//            editColor.value?.getComposeble {
//                colorPicker.setColorEditObj(it)
//            }
            Column(
                Modifier

                    .background(color = MyColorARGB.colorBackGr2.toColor()) //Color(0xFF576350))
                    .padding(15.dp)
                    .fillMaxSize()
            ) {
                AppWindowTitleBar(StateVM.openEditStyle, state)
                Row {
                    Column(
                        Modifier.weight(1f).padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
//                        spisSaveSetStyle(dialLay = dialogLayout)
                        BoxWithVScrollBar(Modifier.weight(1f)) { scrollState ->
                            Box(Modifier.verticalScroll(scrollState)) {
                                ComItemRazdelSettings(
                                    MainDB.styleParam,
                                    dialogLayout,
                                    selectionInterSett,
                                    selectionRazdelSetting
                                ) {
                                    when (it) {
                                        is CommonInterfaceSetting.InterfaceSettingsBoolean -> {

                                        }

                                        is CommonInterfaceSetting.InterfaceSettingsDoublePozitive -> {
                                            summa.value = TextFieldValue(it.getValue().roundToString(1))
                                        }

                                        is CommonInterfaceSetting.InterfaceSettingsDouble -> {
                                            summa.value = TextFieldValue(it.getValue().roundToString(1))
                                        }

                                        is CommonInterfaceSetting.InterfaceSettingsLong -> {

                                        }

                                        is CommonInterfaceSetting.InterfaceSettingsMyColor -> {
                                            colorPicker.setColorEditObj(it.itrObj)
//                                    editColor.value = it.itrObj
                                        }

                                        is CommonInterfaceSetting.InterfaceSettingsMyColorGradient -> {
                                            selIndGradColor.value = -1
//                                    editColor.value = it.itrObj
                                        }

                                        is CommonInterfaceSetting.InterfaceSettingsString -> {

                                        }

                                        is CommonInterfaceSetting.InterfaceSettingsAngle -> {
                                            angleLong.value = it.itrObj.value.toDouble()
                                            angle.value = TextFieldValue(it.itrObj.value.toString())
                                        }

                                        else -> {}
                                    }
                                }.getComposable()
                            }
                        }
                        MyCommonSliderButt(
                            "Сбросить все по умолчанию",
                            modifier = Modifier.padding(horizontal = 20.dp).padding(vertical = 10.dp),
                            reverse = true
                        ) {
//                        MyTextButtStyle1("Сбросить все по умолчанию") {
                            MainDB.addEditStyle.clearAllToDefault()
                        }
                    }
                    Box(Modifier.padding(10.dp).width(640.dp).fillMaxHeight(), contentAlignment = Alignment.Center) {
                        editBox()
                    }
                }
            }
            Image(
                BitmapPainter(useResource("desk_paper_back.png", ::loadImageBitmap)),
                "paperback",
                Modifier.fillMaxSize(),
                alpha = 0.6F,
                contentScale = ContentScale.FillBounds// .Fit
            )
            dialogLayout.getLay()
            BorderWindow((20 * 1.5).dp)
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun WindowScope.AppWindowTitleBar(open: MutableState<Boolean>, state: WindowState) =
    WindowDraggableArea(Modifier) { //.cursorForMove()
        Row(
            modifier = Modifier.background(color = MyColorARGB.colorMyAppBarDesktop.toColor())
                .border(0.5.dp, Color.Black)
                .fillMaxWidth()
                .height(25.dp)
                .padding(start = 20.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                Modifier.weight(1f).height(48.dp).background(MyColorARGB.colorMyAppBarDesktop.toColor()),
                contentAlignment = Alignment.Center
            ) {
                MyTextStyle2(
                    "Редактор стилей",
                    Modifier,
                    textAlign = TextAlign.Start,
                    fontSize = 15.sp
                )
            }
            Row {
                Button(
                    onClick = {
                        state.isMinimized = !state.isMinimized
//                    isDialogOpen = true
//                    val current = AppManager.focusedWindow
//                    if (current != null) {
//                        current.window.setExtendedState(JFrame.ICONIFIED)
//                    }
                    }
                )
                Spacer(modifier = Modifier.width(5.dp))
                Button(
                    onClick = {
                        state.placement = if (state.placement == WindowPlacement.Floating) {
                            WindowPlacement.Maximized
                        } else {
                            WindowPlacement.Floating
                        }
//                    val current = AppManager.focusedWindow
//                    if (current != null) {
//                        if (current.window.extendedState == JFrame.MAXIMIZED_BOTH) {
//                            current.window.setExtendedState(JFrame.NORMAL)
//                        } else {
//                            current.window.setExtendedState(JFrame.MAXIMIZED_BOTH)
//                        }
//                    }
                    }
                )
                Spacer(modifier = Modifier.width(5.dp))
                Button(
                    onClick = {
//                        sess?.stop(10, 10)
//                        val tempFiles = File(StateVM.dirTemp)
//                        if (tempFiles.exists()) tempFiles.deleteRecursively()
                        open.value = false
//                        appScope.exitApplication()
//                    AppManager.focusedWindow?.close()
                    }
                )
            }
        }

    }
