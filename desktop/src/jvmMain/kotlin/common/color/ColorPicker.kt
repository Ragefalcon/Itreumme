package common.color

import MyDialog.MyDialogLayout
import MyDialog.MyOneVopros
import MyListRow
import adapters.MyComboBox
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.*
import common.*
import extensions.RowVA
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.tabElement
import viewmodel.MainDB
import kotlin.math.atan2
import kotlin.math.roundToInt

class ColorPicker() {


//    val selectColor: MutableState<MyColorARGB> = mutableStateOf(MyColorARGB(255, 128, 128, 128))

    var colorEdit: MutableState<MyColorARGB> = mutableStateOf(MyColorARGB(255, 128, 128, 128))

    private val offsetX = mutableStateOf(127.dp)
    private val offsetY = mutableStateOf(127.dp)

    private val cursorPosition = mutableStateOf(Offset(0f, 0f))
    private val cursorPositionH = mutableStateOf(Offset(0f, 0f))
    private val cursorPositionS = mutableStateOf(Offset(0f, 0f))
    private val cursorPositionL = mutableStateOf(Offset(0f, 0f))

    private val aColor = mutableStateOf(255f)
    private val rColor = mutableStateOf(128f)
    private val gColor = mutableStateOf(128f)
    private val bColor = mutableStateOf(128f)

    private val hColor = mutableStateOf(128f)
    private val sColor = mutableStateOf(128f)
    private val lColor = mutableStateOf(128f)

    private val radiusMarker = 5.dp

    private enum class MainChanel(override val nameTab: String) : tabElement {
        R("R(GB)"),
        G("G(RB)"),
        B("B(RG)");
    }

    private val typeBox = EnumDiskretSeekBar(MainChanel.R::class) {
        changeFromRGB(it)
        var aa = MainChanel.R::class
    }

    fun getColor() = MyColorARGB(
        aColor.value.toInt(),
        rColor.value.toInt(),
        gColor.value.toInt(),
        bColor.value.toInt(),
    ).toColor()

    fun getColorMyARGB() = MyColorARGB(
        aColor.value.toInt(),
        rColor.value.toInt(),
        gColor.value.toInt(),
        bColor.value.toInt(),
    )

    fun getColorMyRGB() = MyColorARGB(
        255,
        rColor.value.toInt(),
        gColor.value.toInt(),
        bColor.value.toInt(),
    )

    fun getColorRGB() = MyColorARGB(
        255,
        rColor.value.toInt(),
        gColor.value.toInt(),
        bColor.value.toInt(),
    ).toColor()

    fun getColorInverseRGB() = MyColorARGB(
        255,
        255 - rColor.value.toInt(),
        255 - gColor.value.toInt(),
        255 - bColor.value.toInt(),
    ).toColor()


    private val CB_spisSetColorLibrary = MyComboBox(MainDB.editStyleSpis.spisSetColorLibrary, nameItem = { it.name }) {
        MainDB.editStyleFun.setSelectSetColorLibrary(it.id)
    }

    @Composable
    private fun spisColorLibrary() {
        MainDB.editStyleSpis.spisColorLibrary.getState().value?.let {
            println("testttt ColorLibraryList")
            MyListRow(it, Modifier.padding(horizontal = 20.dp).padding(bottom = 10.dp)) { ind, item -> //ind,
                ComItemColorLibrary(item, doubleClick = {
                    aColor.value = it.color.A.toFloat()
                    rColor.value = it.color.R.toFloat()
                    gColor.value = it.color.G.toFloat()
                    bColor.value = it.color.B.toFloat()
                    changeFromRGB()
                }) { itemDM, exp ->
                    MyDropdownMenuItem(exp, "Загрузить") {
                        aColor.value = itemDM.color.A.toFloat()
                        rColor.value = itemDM.color.R.toFloat()
                        gColor.value = itemDM.color.G.toFloat()
                        bColor.value = itemDM.color.B.toFloat()
                        changeFromRGB()
                    }
                    MyDeleteDropdownMenuButton(exp) {
                        MainDB.addEditStyle.delColorFromLibrary(itemDM.id)
                    }
                }.getComposable()
            }
        }
    }

    @Composable
    private fun spisSetColorLibrary(dialLay: MyDialogLayout) {
        val expandedDropMenu = remember { mutableStateOf(false) }
        RowVA(Modifier.fillMaxWidth().padding(horizontal = 32.dp)) {
            MyTextButtStyle1("Добавить набор") {
                MyOneVopros(dialPan = dialLay, "Введите название нового набора цветов.", "Добавить") {
                    if (it != "") MainDB.addEditStyle.addSetColorLibrary(it)
                }
            }
            MainDB.editStyleSpis.spisSetColorLibrary.getState().value?.let { list ->
                if (list.isNotEmpty()) {
//                    CB_spisSetColorLibrary.getSelected()?.let {
//                        MyDeleteButton {
//                            println("delete button test")
//                            CB_spisSetColorLibrary.getSelected()?.let { selSet ->
//                                MainDB.addEditStyle.delSetColorLibrary(selSet.id)
//                            }
//                        }
//                    }
                    CB_spisSetColorLibrary.show(Modifier.padding(horizontal = 15.dp))
                    MyButtDropdownMenuStyle1(
                        Modifier.padding(start = 0.dp), expandedDropMenu
                    ) {
                        CB_spisSetColorLibrary.getSelected()?.let { setColor ->

                            MyDropdownMenuItem(expandedDropMenu, "Изменить") {
                                MyOneVopros(
                                    dialPan = dialLay,
                                    "Введите новое название для набора цветов.",
                                    "Изменить",
                                    otvetDefault = setColor.name
                                ) {
                                    if (it != "") MainDB.addEditStyle.updSetColorLibrary(setColor.id, it)
                                }
                            }
                            MyDeleteDropdownMenuButton(expandedDropMenu) {
                                CB_spisSetColorLibrary.getSelected()?.let { selSet ->
                                    MainDB.addEditStyle.delSetColorLibrary(selSet.id)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun colorToHexString(color: Color): String =
        java.lang.String.format("%08X", 0xFFFFFFFF and color.toArgb().toLong()) ?: "FF000000"

//    private enum class MainChanel {
//        R, G, B
//    }

/*
    private val typeBox = DiskretSeekBar(
        listOf(
            "R(GB)" to "R",
            "G(RB)" to "G",
            "B(RG)" to "B",
        ), "R"
    ) {
        println("test listener diskretSeekBar")
        changeFromRGB()
    }
*/

    @Composable
    private fun currentColor() {
        RowVA(Modifier.padding(vertical = 10.dp)) {
            Box(
                Modifier
                    .padding(start = 32.dp)
                    .height(50.dp)
                    .width(256.dp)
                    .background(getColor()).border(1.dp, Color.Black)
            )
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                SelectionContainer {
                    Text(
                        (getColor().toArgb()).toString(),
                        style = MyTextStyleParam.style2.copy(fontSize = 14.sp)
                    )
                }
//                SelectionContainer {
//                    MyTextStyle(
//                        "#${colorToHexString(getColor())}",
//                        param = MyTextStyleParam.style2.copy(fontSize = 14.sp)
//                    )
//                }
                SelectionContainer {
                    Text(
                        "#${getColorMyARGB().toHexString()}",
                        style = MyTextStyleParam.style2.copy(fontSize = 14.sp)
                    )
                }
            }
            MyTextButtStyle1("Добавить в набор", Modifier.padding(end = 32.dp)) {
                CB_spisSetColorLibrary.getSelected()?.let {
                    MainDB.addEditStyle.addColorToLibrary(it.id, colorToHexString(getColor()))
                }
            }
        }
    }

    private val editText = mutableStateOf(TextFieldValue("FFFFFFFF"))

    @Composable
    private fun boxsAndSliders() {
        Row {
            Column(
                Modifier.width(320.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                colorBox()
                typeBox.show(Modifier.padding(10.dp))
                colorSlider(rColor, Color.Red, Modifier.padding(5.dp), { vv -> vv.toInt().toString() }) {
                    changeFromRGB()
                }
                colorSlider(gColor, Color.Green, Modifier.padding(5.dp), { vv -> vv.toInt().toString() }) {
                    changeFromRGB()
                }
                colorSlider(bColor, Color.Blue, Modifier.padding(5.dp), { vv -> vv.toInt().toString() }) {
                    changeFromRGB()
                }
                colorSlider(
                    aColor,
                    Color.White,
                    Modifier.padding(5.dp),
                    { vv -> "${(vv / 255f * 100f).toInt()}%" }){
                    colorEdit.value = getColorMyARGB()
//                    changeFromRGB()
                }
                RowVA {
                    MyOutlinedTextField(
                        "hex (000000 to FFFFFFFF)",
                        editText,
                        Modifier.padding(bottom = 5.dp).weight(1f),
                        textStyle = MyTextStyleParam.style2
                    )
                    MyTextButtStyle1("Ok", Modifier.padding(start = 8.dp), fontSize = 17.sp) {
                        val aa = MyColorARGB(editText.value.text)
                        aColor.value = aa.A.toFloat()
                        rColor.value = aa.R.toFloat()
                        gColor.value = aa.G.toFloat()
                        bColor.value = aa.B.toFloat()
                        changeFromRGB()
//                                    rezEdit(editText.value.text)
                    }
                }
            }
            Column(
                Modifier.width(320.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                colorCircle()
                colorSaturationBox(Modifier.padding(top = 10.dp))
                colorLightBox(Modifier.padding(vertical = 10.dp))
                colorSlider(
                    hColor,
                    Color.Yellow,
                    Modifier.padding(5.dp),
                    { vv -> "${(vv / 255f * 360f).toInt()}°" }) {
                    changeFromHSL()
                }
                colorSlider(
                    sColor,
                    Color.White,
                    Modifier.padding(5.dp),
                    { vv -> "${(vv / 255f * 100f).toInt()}%" }) {
                    changeFromHSL()
                }
                colorSlider(
                    lColor,
                    Color.Black,
                    Modifier.padding(5.dp),
                    { vv -> "${(vv / 255f * 100f).toInt()}%" }) {
                    changeFromHSL()
                }
            }
        }
    }

    fun setColorEditObj(colEdit: MutableState<MyColorARGB>){
        colorEdit = colEdit
        if (colEdit.value != getColorMyARGB()) {
            aColor.value = colorEdit.value.A.toFloat()
            rColor.value = colorEdit.value.R.toFloat()
            gColor.value = colorEdit.value.G.toFloat()
            bColor.value = colorEdit.value.B.toFloat()
            println("setColorEditObj")
            changeFromRGB(updColorEdit = false)
        }
    }

    @Composable
    fun show(dialLay: MyDialogLayout) {
        Column(Modifier.padding(10.dp).width(640.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            spisSetColorLibrary(dialLay)
            currentColor()
            spisColorLibrary()
            boxsAndSliders()
        }
    }


    private fun checkAddValue(new: Dp, max: Dp, min: Dp): Dp {
        return maxOf(minOf(new, max), min)
    }


    private fun colorValueFromOffset(value: MutableState<Dp>): Dp =
        checkAddValue(value.value, 255.dp, 0.dp)

    private fun changeFromRGB(ch: MainChanel? = null, updColorEdit: Boolean = true) {
        val nowColor = getColorMyRGB()
        val hsl = rgbToHsl(nowColor.R, nowColor.G, nowColor.B)
        hColor.value = hsl[0] * 255f
        sColor.value = hsl[1] * 255f
        lColor.value = hsl[2] * 255f
        offsetX.value = when (ch ?: typeBox.active) {
            MainChanel.R -> gColor.value.toInt().dp
            MainChanel.G -> rColor.value.toInt().dp
            MainChanel.B -> rColor.value.toInt().dp
        }
        offsetY.value = when (ch ?: typeBox.active) {
            MainChanel.R -> bColor.value.toInt().dp
            MainChanel.G -> bColor.value.toInt().dp
            MainChanel.B -> gColor.value.toInt().dp
        }
        if (updColorEdit) colorEdit.value = getColorMyARGB()
    }

    private fun changeFromHSL() {
        val rgb = hslToRgb(hColor.value / 255f, sColor.value / 255f, lColor.value / 255f)
        rColor.value = (rgb?.get(0) ?: 255).toFloat()
        gColor.value = (rgb?.get(1) ?: 255).toFloat()
        bColor.value = (rgb?.get(2) ?: 255).toFloat()
        offsetX.value = when (typeBox.active) {
            MainChanel.R -> gColor.value.toInt().dp
            MainChanel.G -> rColor.value.toInt().dp
            MainChanel.B -> rColor.value.toInt().dp
        }
        offsetY.value = when (typeBox.active) {
            MainChanel.R -> bColor.value.toInt().dp
            MainChanel.G -> bColor.value.toInt().dp
            MainChanel.B -> gColor.value.toInt().dp
        }
        colorEdit.value = getColorMyARGB()
    }

    private fun updateFromOffset() {
        when (typeBox.active) {
            MainChanel.R -> {
                gColor.value = colorValueFromOffset(offsetX) / 1.dp
                bColor.value = colorValueFromOffset(offsetY) / 1.dp
            }
            MainChanel.G -> {
                rColor.value = colorValueFromOffset(offsetX) / 1.dp
                bColor.value = colorValueFromOffset(offsetY) / 1.dp
            }
            MainChanel.B -> {
                rColor.value = colorValueFromOffset(offsetX) / 1.dp
                gColor.value = colorValueFromOffset(offsetY) / 1.dp
            }
        }
        val nowColor = getColorMyRGB()
        val hsl = rgbToHsl(nowColor.R, nowColor.G, nowColor.B)
        hColor.value = hsl[0] * 255f
        sColor.value = hsl[1] * 255f
        lColor.value = hsl[2] * 255f
        colorEdit.value = getColorMyARGB()
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun colorBox(alpha: Int = 255) {
        with(LocalDensity.current) {
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState() // .collectIsHoveredAsState()
            var r = 0
            var g = 0
            var b = 0
            fun rr(w: Int, h: Int) {
                when (typeBox.active) {
                    MainChanel.R -> {
                        r = rColor.value.toInt()
                        g = w
                        b = h
                    }
                    MainChanel.G -> {
                        r = w
                        g = gColor.value.toInt()
                        b = h
                    }
                    MainChanel.B -> {
                        r = w
                        g = h
                        b = bColor.value.toInt()
                    }
                }
            }
            Box {
                Canvas(modifier = Modifier.width(256.dp).height(256.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null, //LocalIndication.current,
//                        enabled = false
                    ) {
                        offsetX.value = cursorPosition.value.x.toDp()
                        offsetY.value = cursorPosition.value.y.toDp()
                        updateFromOffset()
                    }
                    .pointerMoveFilter(onMove = {
                        cursorPosition.value = it
                        true
                    })
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragEnd = {
//                                    updateImage.value = true
                            }
                        ) { change, dragAmount ->
                            if (offsetX.value + dragAmount.x.toDp() <= 256.dp && offsetX.value + dragAmount.x.toDp() >= 0.dp) offsetX.value += dragAmount.x.toDp()
                            if (offsetY.value + dragAmount.y.toDp() <= 256.dp && offsetY.value + dragAmount.y.toDp() >= 0.dp) offsetY.value += dragAmount.y.toDp()
                            updateFromOffset()
                        }
                    }
                ) {


                    for (iw in 0..255) {
                        rr(iw, 0)
                        val col1 = MyColorARGB(alpha, r, g, b).toColor()
                        rr(iw, 255)
                        val col2 = MyColorARGB(alpha, r, g, b).toColor()
                        drawRect(
                            Brush.verticalGradient(listOf(col1, col2)),
                            Offset(iw.dp.toPx(), 0.dp.toPx()),
                            Size(1.dp.toPx(), 256.dp.toPx())
                        )
/*
                        for (ih in 0..255) {
                            rr(iw, ih)
                            drawRect(
                                MyColorARGB(alpha, r, g, b).toColor(),
                                Offset(iw.dp.toPx(), ih.dp.toPx()),
                                Size(1.dp.toPx(), 1.dp.toPx())
                            )
                        }
*/
                    }
                }
                Surface(
                    color = getColorRGB(),
                    modifier = Modifier
                        .width(10.dp)
                        .height(10.dp)
                        .offset {
                            IntOffset(
                                (offsetX.value - radiusMarker).toPx().roundToInt(),
                                (offsetY.value - radiusMarker).toPx().roundToInt()
                            )
                        }
                        .border(1.dp, Color.Black, RoundedCornerShape(7.dp))
                        .padding(1.dp)
                        .border(1.dp, Color.White, RoundedCornerShape(7.dp + Dp.Companion.Hairline))
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragEnd = {
//                                    updateImage.value = true
                                }
                            ) { change, dragAmount ->
//                                second = false
                                if (offsetX.value + dragAmount.x.toDp() <= 256.dp && offsetX.value + dragAmount.x.toDp() >= 0.dp) offsetX.value += dragAmount.x.toDp()
                                if (offsetY.value + dragAmount.y.toDp() <= 256.dp && offsetY.value + dragAmount.y.toDp() >= 0.dp) offsetY.value += dragAmount.y.toDp()
                                updateFromOffset()
                            }
                        }

                ) {
                }
            }
        }

    }

    private fun getAngle(x: Float, y: Float): Double {
        return 0.5 * Math.PI - atan2( //
            x.toDouble(),
            y.toDouble()
        ) //note the atan2 call, the order of paramers is y then x
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun colorCircle(modifier: Modifier = Modifier) {
        with(LocalDensity.current) {
            Canvas(modifier = modifier.padding(5.dp).width(255.dp).height(255.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    val angle = getAngle(
                        cursorPositionH.value.x - 127.dp.toPx(),
                        cursorPositionH.value.y - 127.dp.toPx()
                    ) / 2 / Math.PI * 360f + 90f
                    hColor.value = maxOf(0f, minOf(angle.toFloat() / 360f * 255f, 255f))
                    changeFromHSL()
                }
                .pointerMoveFilter(onMove = {
                    cursorPositionH.value = it
                    true
                })
                .pointerInput(Unit) {
                    detectDragGestures() { change, dragAmount ->
                        val angle = getAngle(
                            cursorPositionH.value.x - 127.dp.toPx(),
                            cursorPositionH.value.y - 127.dp.toPx()
                        ) / 2 / Math.PI * 360f + 90f
                        hColor.value = maxOf(0f, minOf(angle.toFloat() / 360f * 255f, 255f))
                        changeFromHSL()
                    }
                }
            ) {
                val shir = 60.dp.toPx()
                for (iw in 0..359) {
                    val rgb = hslToRgb(iw.toFloat() / 359f, 1f, 0.5f)
                    rotate(iw.toFloat(), Offset(128.dp.toPx(), 128.dp.toPx())) {
                        drawRect(
                            MyColorARGB(255, rgb?.get(0) ?: 255, rgb?.get(1) ?: 255, rgb?.get(2) ?: 255).toColor(),
                            Offset(128.dp.toPx(), 0f), Size(3.dp.toPx(), shir)
                        )
                    }
                }
                rotate(hColor.value / 255f * 360f, Offset(128.dp.toPx(), 128.dp.toPx())) {
                    drawRect(
                        Color.White, //.copy(0.5f),
                        Offset(128.dp.toPx(), -5.dp.toPx()), Size(3.dp.toPx(), shir + 10.dp.toPx()),
                        style = Stroke(1.5.dp.toPx())
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun colorSaturationBox(
        modifier: Modifier = Modifier
    ) { //valueMain: Int, alpha: Int = 255
        with(LocalDensity.current) {
            val shirDp = 40.dp
            val shir = shirDp.toPx()
            Canvas(modifier = modifier.padding(5.dp).width(255.dp).height(shirDp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    sColor.value = maxOf(0f, minOf(cursorPositionS.value.x / 1.dp.toPx(), 255f)) // - padding.toPx()
                    changeFromHSL()
                }
                .pointerMoveFilter(onMove = {
                    cursorPositionS.value = it
                    true
                })
                .pointerInput(Unit) {
                    detectDragGestures() { change, dragAmount ->
//                        sColor.value = maxOf(0f, minOf(cursorPositionS.value.x / 1.dp.toPx(), 255f)) // - padding.toPx()
                        sColor.value =
                            maxOf(0f, minOf(sColor.value + dragAmount.x / 1.dp.toPx(), 255f)) // - padding.toPx()
                        changeFromHSL()
                    }
                }
            ) {
                for (iw in 0..254) {
                    val rgb = hslToRgb(hColor.value / 255f, iw.toFloat() / 254f, lColor.value / 255f)
                    drawRect(
                        MyColorARGB(255, rgb?.get(0) ?: 255, rgb?.get(1) ?: 255, rgb?.get(2) ?: 255).toColor(),
                        Offset(iw.dp.toPx(), 0f), Size(1.dp.toPx(), shir)
                    )
                }
                drawRect(
                    Color.White, //.copy(0.5f),
                    Offset((sColor.value.dp - 1.5.dp).toPx(), -5.dp.toPx()), Size(3.dp.toPx(), shir + 10.dp.toPx()),
                    style = Stroke(1.5.dp.toPx())
                )
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun colorLightBox(modifier: Modifier = Modifier) {
        with(LocalDensity.current) {
            val padding = 5.dp
            val shirDp = 40.dp
            val shir = shirDp.toPx()
            Canvas(modifier = modifier.padding(padding).width(255.dp).height(shirDp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    lColor.value = maxOf(0f, minOf(cursorPositionL.value.x / 1.dp.toPx(), 255f)) // - padding.toPx()
                    changeFromHSL()
                }
                .pointerMoveFilter(onMove = {
                    cursorPositionL.value = it
                    true
                })
                .pointerInput(Unit) {
                    detectDragGestures() { change, dragAmount ->
                        lColor.value =
                            maxOf(0f, minOf(lColor.value + dragAmount.x / 1.dp.toPx(), 255f)) // - padding.toPx()
                        changeFromHSL()
                    }
                }
            ) {
                for (iw in 0..254) {
                    val rgb = hslToRgb(hColor.value / 255f, sColor.value / 255f, iw.toFloat() / 254f)
                    drawRect(
                        MyColorARGB(255, rgb?.get(0) ?: 255, rgb?.get(1) ?: 255, rgb?.get(2) ?: 255).toColor(),
                        Offset(iw.dp.toPx(), 0f), Size(1.dp.toPx(), shir)
                    )
                }
                drawRect(
                    Color.White, //.copy(0.5f),
                    Offset((lColor.value.dp - 1.5.dp).toPx(), -5.dp.toPx()), Size(3.dp.toPx(), shir + 10.dp.toPx()),
                    style = Stroke(1.5.dp.toPx())
                )
            }
        }
    }

    @Composable
    private fun colorSlider(
        valueColor: MutableState<Float>,
        colorVyp: Color,
        modifier: Modifier = Modifier,
        getString: (Float) -> String = { "" },
        onValueChange: ((Float) -> Unit)? = null
    ) {
        RowVA(modifier.fillMaxWidth()) {
            Slider(
                value = valueColor.value / 255f,
                modifier = Modifier.height(20.dp).weight(1f),
                onValueChange = {
                    valueColor.value = 255 * it
                    onValueChange?.invoke(it)
                },
                onValueChangeFinished = {
//                        changeGotov?.invoke(item, redColor.value)
                },
                colors = SliderDefaults.colors(
                    thumbColor = colorVyp, //MaterialTheme.colors.primary,
                    disabledThumbColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
                        .compositeOver(MaterialTheme.colors.surface),
                    activeTrackColor = colorVyp, //MaterialTheme.colors.primary,
                    inactiveTrackColor = Color(0x6FFF8888), //activeTrackColor.copy(alpha = InactiveTrackAlpha),
                    disabledActiveTrackColor = MaterialTheme.colors.onSurface.copy(alpha = SliderDefaults.DisabledActiveTrackAlpha),
                    disabledInactiveTrackColor = MaterialTheme.colors.onSurface.copy(
                        alpha = SliderDefaults.DisabledInactiveTrackAlpha
                    ),
                    activeTickColor = contentColorFor(Color.Blue).copy(alpha = SliderDefaults.TickAlpha),
                    inactiveTickColor = Color.Magenta.copy(alpha = SliderDefaults.TickAlpha),
                    disabledActiveTickColor = contentColorFor(colorVyp).copy(alpha = SliderDefaults.TickAlpha)
                        .copy(alpha = SliderDefaults.DisabledTickAlpha),
                    disabledInactiveTickColor = MaterialTheme.colors.onSurface.copy(
                        alpha = SliderDefaults.DisabledInactiveTrackAlpha
                    )
                        .copy(alpha = SliderDefaults.DisabledTickAlpha)
                )
            )
            Text(
                getString(valueColor.value),
                Modifier.width(40.dp),
                style = MyTextStyleParam.style2.copy(fontSize = 14.sp)
            )
        }
    }
}

/**
 * Converts an HSL color value to RGB. Conversion formula
 * adapted from http://en.wikipedia.org/wiki/HSL_color_space.
 * Assumes h, s, and l are contained in the set [0, 1] and
 * returns r, g, and b in the set [0, 255].
 *
 * @param h       The hue
 * @param s       The saturation
 * @param l       The lightness
 * @return int array, the RGB representation
 */
fun hslToRgb(h: Float, s: Float, l: Float): IntArray? {
    val r: Float
    val g: Float
    val b: Float
    if (s == 0f) {
        b = l
        g = b
        r = g // achromatic
    } else {
        val q = if (l < 0.5f) l * (1 + s) else l + s - l * s
        val p = 2 * l - q
        r = hueToRgb(p, q, h + 1f / 3f)
        g = hueToRgb(p, q, h)
        b = hueToRgb(p, q, h - 1f / 3f)
    }
    return intArrayOf(to255(r), to255(g), to255(b))
}

fun to255(v: Float): Int {
    return Math.min(255f, 256 * v).toInt()
}

/** Helper method that converts hue to rgb  */
fun hueToRgb(p: Float, q: Float, t: Float): Float {
    var t = t
    if (t < 0f) t += 1f
    if (t > 1f) t -= 1f
    if (t < 1f / 6f) return p + (q - p) * 6f * t
    if (t < 1f / 2f) return q
    return if (t < 2f / 3f) p + (q - p) * (2f / 3f - t) * 6f else p
}

/**
 * Converts an RGB color value to HSL. Conversion formula
 * adapted from http://en.wikipedia.org/wiki/HSL_color_space.
 * Assumes pR, pG, and bpBare contained in the set [0, 255] and
 * returns h, s, and l in the set [0, 1].
 *
 * @param pR       The red color value
 * @param pG       The green color value
 * @param pB       The blue color value
 * @return float array, the HSL representation
 */
fun rgbToHsl(pR: Int, pG: Int, pB: Int): FloatArray {
    val r = pR / 255f
    val g = pG / 255f
    val b = pB / 255f
    val max = if (r > g && r > b) r else if (g > b) g else b
    val min = if (r < g && r < b) r else if (g < b) g else b
    var h: Float
    val s: Float
    val l: Float
    l = (max + min) / 2.0f
    if (max == min) {
        s = 0.0f
        h = s
    } else {
        val d = max - min
        s = if (l > 0.5f) d / (2.0f - max - min) else d / (max + min)
        h =
            if (r > g && r > b) (g - b) / d + (if (g < b) 6.0f else 0.0f) else if (g > b) (b - r) / d + 2.0f else (r - g) / d + 4.0f
        h /= 6.0f
    }
    return floatArrayOf(h, s, l)
}