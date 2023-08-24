package extensions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.CommonInterfaceSetting
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.StyleVMspis

@Composable
private fun CommonInterfaceSetting.RazdelSetting.setLaunchedEffect(leFun: CoroutineScope.() -> Unit) {
    forEach {
        LaunchedEffect(it.itrObj.value) {
//            println("leFun() - ${Date().time}")
            leFun()
        }
    }
    list2.forEach {
        it.setLaunchedEffect(leFun)
    }
}

@Composable
fun <K : CommonInterfaceSetting.RazdelSetting, T> K.getComposable(getV: (K) -> T, content: @Composable (T) -> Unit) {
/*
    val itemMutab = remember {  mutableStateOf(getV(this)) }
    setLaunchedEffect {
        itemMutab.value = getV(this@getComposable)
    }
    content(itemMutab.value)
*/
//    println("<K: CommonInterfaceSetting.RazdelSetting, T> K.getComposable(")
    content(getV(this))
}

/*
@Composable
fun <T> CommonInterfaceSetting.RazdelSetting.getComposableTmp(getV: ()->T, content: @Composable (T) -> Unit) {
    val itemMutab = remember {  mutableStateOf(getV()) }
    setLaunchedEffect {
        itemMutab.value = getV()
    }
    content(itemMutab.value)
}
*/


fun CommonInterfaceSetting.MySettings.BrushStyleSetting.getValue(): Brush =
    if (this.GRADIENT_ENABLE.getValue()) LinearGradient(
        this.GRADIENT_COLORS.getValue().map { it.toColor() }, //.compositeOver(Color.Transparent)
        angleInDegrees = this.GRADIENT_ANGLE.getValue().toFloat()
    ) else //SolidColor(this.BRUSH_COLOR.getValue().toColor())
        Brush.horizontalGradient(this.BRUSH_COLOR.getValue().toColor().compositeOver(Color.Transparent)
            .let { listOf(it, it) })

fun CommonInterfaceSetting.MySettings.PaddingStyleItemSetting.getValue(modifier: Modifier): Modifier =
    if (this.PADDING_INDIVIDUALLY.getValue()) modifier.padding(
        top = this.TOP.getValue().dp,
        bottom = this.BOTTOM.getValue().dp,
        start = this.START.getValue().dp,
        end = this.END.getValue().dp
    )
    else modifier.padding(
        horizontal = this.PADDING_HORIZONTAL.getValue().dp, vertical = this.PADDING_VERTICAL.getValue().dp
    )

fun Modifier.paddingStyle(style: CommonInterfaceSetting.MySettings.PaddingStyleItemSetting): Modifier =
    if (style.PADDING_INDIVIDUALLY.getValue()) this.padding(
        top = style.TOP.getValue().dp,
        bottom = style.BOTTOM.getValue().dp,
        start = style.START.getValue().dp,
        end = style.END.getValue().dp
    )
    else this.padding(
        horizontal = style.PADDING_HORIZONTAL.getValue().dp, vertical = style.PADDING_VERTICAL.getValue().dp
    )


fun CommonInterfaceSetting.MySettings.CornerStyleItemSetting.getPath(size: Size): Path = drawTicketPath(
    size,
    topStartShape = this.BORDER_CORNER_TYPE_TOPSTART.getValue(),
    topEndShape = this.BORDER_CORNER_TYPE_TOPEND.getValue(),
    bottomStartShape = this.BORDER_CORNER_TYPE_BOTTOMSTART.getValue(),
    bottomEndShape = this.BORDER_CORNER_TYPE_BOTTOMEND.getValue(),
    topStart = this.BORDER_CORNER_TOPSTART.getValue().toFloat(),
    topEnd = this.BORDER_CORNER_TOPEND.getValue().toFloat(),
    bottomStart = this.BORDER_CORNER_BOTTOMSTART.getValue().toFloat(),
    bottomEnd = this.BORDER_CORNER_BOTTOMEND.getValue().toFloat()
)

fun CommonInterfaceSetting.MySettings.CornerStyleItemSetting.getValue(
    indiv: Boolean = false,
    topStart: Boolean = true,
    topEnd: Boolean = true,
    bottomStart: Boolean = true,
    bottomEnd: Boolean = true,
    padding: Double = 0.0,
    hole: Boolean = false,
    limit: Double? = null
): CustomShape
//= CutCornerShape(
//    this.BORDER_CORNER_TOPSTART.getValue().dp,
//    this.BORDER_CORNER_TOPEND.getValue().dp,
//    this.BORDER_CORNER_BOTTOMEND.getValue().dp,
//    this.BORDER_CORNER_BOTTOMSTART.getValue().dp)
{
    fun testLimit(value: Double): Double = limit?.let { if (value > limit) limit else value } ?: value
    fun addDelta(value: Double): Double = testLimit(if (value - padding >= 0.0) value - padding else 0.0)
//    println("CornerStyleItemSetting.getValue - ${Date().time}")
    return if (this.BORDER_CORNER_INDIVIDUALLY.getValue()) CustomShape(
        topStartShape = this.BORDER_CORNER_TYPE_TOPSTART.getValue(),
        topEndShape = this.BORDER_CORNER_TYPE_TOPEND.getValue(),
        bottomStartShape = this.BORDER_CORNER_TYPE_BOTTOMSTART.getValue(),
        bottomEndShape = this.BORDER_CORNER_TYPE_BOTTOMEND.getValue(),
        topStart = CornerSize(if (indiv || topStart) addDelta(this.BORDER_CORNER_TOPSTART.getValue()).dp else 0.dp),
        topEnd = CornerSize(if (indiv || topEnd) addDelta(this.BORDER_CORNER_TOPEND.getValue()).dp else 0.dp),
        bottomStart = CornerSize(if (indiv || bottomStart) addDelta(this.BORDER_CORNER_BOTTOMSTART.getValue()).dp else 0.dp),
        bottomEnd = CornerSize(if (indiv || bottomEnd) addDelta(this.BORDER_CORNER_BOTTOMEND.getValue()).dp else 0.dp),
        hole = hole
    )
    else CustomShape(
        this.BORDER_CORNER_TYPE_COMMON.getValue(),
        CornerSize(testLimit(this.BORDER_RADIUS.getValue()).dp),
        hole = hole
    )
}

open class BoxOpisStyleState(
    parent: CommonInterfaceSetting.MySettings.CommonBoxOpisStyleItemSetting,
    val shapeCard: Shape = parent.corner.getValue(),
    val background: Brush = parent.back_and_border.BACKGROUND.getValue(),
    val border: Brush = parent.back_and_border.BORDER.getValue(),
    val borderWidth: Dp = parent.back_and_border.BORDER_WIDTH.getValue().dp,
    val padingOuter: Modifier = Modifier.paddingStyle(parent.outer_padding),
    val padingInner: Modifier = Modifier.paddingStyle(parent.inner_padding),
    val textStyle: TextStyle = parent.mainText.getValue(),
    val shadow: Shadow = parent.shadow.getValue(),
)

open class BoxOpisWithButtStyleState(
    parent: CommonInterfaceSetting.MySettings.CommonBoxOpisWithButtStyleItemSetting,
    val elevationButt: Dp = parent.ELEVATION_BUTTON.getValue().dp,
    val colorButt: Color = parent.COLOR_TEXT_BUTTON.getValue().toColor()
) : BoxOpisStyleState(parent)

open class ComplexOpisFontSizeSetState(
    parent: CommonInterfaceSetting.MySettings.ComplexOpisFontSizeSetSetting,
    val fontSize1: TextUnit = parent.fontSize1.getValue().sp,
    val fontSize2: TextUnit = parent.fontSize2.getValue().sp,
    val fontSize3: TextUnit = parent.fontSize3.getValue().sp,
    val fontSize4: TextUnit = parent.fontSize4.getValue().sp,
    val fontSize5: TextUnit = parent.fontSize5.getValue().sp,
) {
    fun getFontSize(num: Long): TextUnit = when (num) {
        1L -> fontSize1
        2L -> fontSize2
        3L -> fontSize3
        4L -> fontSize4
        5L -> fontSize5
        else -> fontSize3
    }

    fun getPairFontSize(num: Long): Pair<Long, TextUnit> = when (num) {
        1L -> 1L to fontSize1
        2L -> 2L to fontSize2
        3L -> 3L to fontSize3
        4L -> 4L to fontSize4
        5L -> 5L to fontSize5
        else -> 3L to fontSize3
    }

    fun getList(): List<Pair<Long, TextUnit>> =
        listOf(
            5L to fontSize5,
            4L to fontSize4,
            3L to fontSize3,
            2L to fontSize2,
            1L to fontSize1
        )
}

open class ComplexOpisColorSetState(
    parent: CommonInterfaceSetting.MySettings.ComplexOpisColorSetSetting,
    val color1: Color = parent.color1.getValue().toColor(),
    val color2: Color = parent.color2.getValue().toColor(),
    val color3: Color = parent.color3.getValue().toColor(),
    val color4: Color = parent.color4.getValue().toColor(),
    val color5: Color = parent.color5.getValue().toColor(),
    val color6: Color = parent.color6.getValue().toColor(),
    val color7: Color = parent.color7.getValue().toColor(),
    val color8: Color = parent.color8.getValue().toColor(),
    val color9: Color = parent.color9.getValue().toColor(),
    val color10: Color = parent.color10.getValue().toColor(),
    val color11: Color = parent.color11.getValue().toColor(),
    val color12: Color = parent.color12.getValue().toColor()
) {
    fun getColor(num: Long): Color = when (num) {
        1L -> color1
        2L -> color2
        3L -> color3
        4L -> color4
        5L -> color5
        6L -> color6
        7L -> color7
        8L -> color8
        9L -> color9
        10L -> color10
        11L -> color11
        12L -> color12
        else -> color1
    }

    fun getPairColor(num: Long): Pair<Long, Color> = when (num) {
        1L -> 1L to color1
        2L -> 2L to color2
        3L -> 3L to color3
        4L -> 4L to color4
        5L -> 5L to color5
        6L -> 6L to color6
        7L -> 7L to color7
        8L -> 8L to color8
        9L -> 9L to color9
        10L -> 10L to color10
        11L -> 11L to color11
        12L -> 12L to color12
        else -> 1L to color1
    }

    fun getList(): List<Pair<Long, Color>> =
        listOf(
            1L to color1,
            2L to color2,
            3L to color3,
            4L to color4,
            5L to color5,
            6L to color6,
            7L to color7,
            8L to color8,
            9L to color9,
            10L to color10,
            11L to color11,
            12L to color12
        )
}

open class ComplexOpisStyleState(
    parent: CommonInterfaceSetting.MySettings.ComplexOpisStyleSetting,
    val plateView: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.plateView),
    val plateEdit: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.plateEdit),
    val plateEditButt: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.plateEditButt),
    val plateLink: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.plateLink),

    val plateCheckboxFalse: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.plateCheckboxFalse),
    val plateCheckboxTrue: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.plateCheckboxTrue),
    val plateCheckboxFail: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.plateCheckboxFail),
    val plateCheckboxQuestion: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.plateCheckboxQuestion),
    val plateCheckboxPriority: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.plateCheckboxPriority),

    val outer_padding_text: Modifier = Modifier.paddingStyle(parent.outer_padding_text),
    val outer_padding: Modifier = Modifier.paddingStyle(parent.outer_padding),
    val inner_padding: Modifier = Modifier.paddingStyle(parent.inner_padding),
    val inner_padding_edit: Modifier = Modifier.paddingStyle(parent.inner_padding_edit),
    val inner_padding_link: Modifier = Modifier.paddingStyle(parent.inner_padding_link),

    val brushBoundImage: Brush = parent.brushBoundImage.getValue(),
    val brushBoundImageActive: Brush = parent.brushBoundImageActive.getValue(),

    val baseText: TextStyle = parent.baseText.getValue(),
    val colorCheckboxFalse: Color = parent.colorCheckboxFalse.getValue().toColor(),
    val colorCheckboxTrue: Color = parent.colorCheckboxTrue.getValue().toColor(),
    val colorCheckboxFail: Color = parent.colorCheckboxFail.getValue().toColor(),
    val colorCheckboxQuestion: Color = parent.colorCheckboxQuestion.getValue().toColor(),
    val colorCheckboxPriority: Color = parent.colorCheckboxPriority.getValue().toColor(),
    val colorArrow: Color = parent.colorArrow.getValue().toColor(),
    val colorToggleTextFalse: Color = parent.colorToggleTextFalse.getValue().toColor(),
    val colorToggleTextTrue: Color = parent.colorToggleTextTrue.getValue().toColor(),
    val colorIconShablon: Color = parent.colorIconShablon.getValue().toColor(),

    val shadowImage: Shadow = parent.shadowImage.getValue(),
    val buttIconLink: IconButtonWithoutBorderStyleState = IconButtonWithoutBorderStyleState(parent.buttIconLink),
    val buttIconDelItem: IconButtonWithoutBorderStyleState = IconButtonWithoutBorderStyleState(parent.buttIconDelItem),
    val buttIconDelImage: IconButtonWithoutBorderStyleState = IconButtonWithoutBorderStyleState(parent.buttIconDelImage),
    val buttIconDelShablon: IconButtonWithoutBorderStyleState = IconButtonWithoutBorderStyleState(parent.buttIconDelShablon),
    val buttIconSaveShablon: IconButtonWithoutBorderStyleState = IconButtonWithoutBorderStyleState(parent.buttIconSaveShablon),
    val textFieldOpis: MyTextFieldStyleState = MyTextFieldStyleState(parent.textFieldOpis),
    val textFieldLink: MyTextFieldStyleState = MyTextFieldStyleState(parent.textFieldLink),
    val buttAddFieldOpis: TextButtonStyleState = TextButtonStyleState(parent.buttAddFieldOpis),
    val buttIconAddFieldOpis: IconButtonStyleState = IconButtonStyleState(parent.buttIconAddFieldOpis),
    val comboBoxShablon: ComboBoxStyleState = ComboBoxStyleState(parent.comboBoxShablon),
    val comboBoxColor: ComboBoxStyleState = ComboBoxStyleState(parent.comboBoxColor),
    val comboBoxFont: ComboBoxStyleState = ComboBoxStyleState(parent.comboBoxFont),
    val comboBoxSizeImage: ComboBoxStyleState = ComboBoxStyleState(parent.comboBoxSizeImage),
    val fontSet: ComplexOpisFontSizeSetState = ComplexOpisFontSizeSetState(parent.fontSet),
    val colorSet: ComplexOpisColorSetState = ComplexOpisColorSetState(parent.colorSet),

    )

data class TextButtonStyleState(
    val parent: CommonInterfaceSetting.MySettings.TextButtonStyleItemSetting,
    val borderWidth: Dp = parent.BORDER_WIDTH.getValue().dp,
    val defaultElevation: Dp = parent.ELEVATION_DEFAULT.getValue().dp,
    val pressedElevation: Dp = parent.ELEVATION_PRESSED.getValue().dp,
    val hoveredElevation: Dp = parent.ELEVATION_HOVERED.getValue().dp,
    val shadow: Shadow = parent.shadow.getValue(),
    val background: Brush = parent.BACKGROUND.getValue(),
    val border: Brush = parent.BORDER.getValue(),
    val shapeCard: Shape = parent.CORNER_BUTTON.getValue(),
    val textStyle: TextStyle = parent.TEXT_BUTTON.getValue(),
    val textStyleShadowHover: TextStyle = textStyle.copy(
        shadow = textStyle.shadow?.let {
            it.copy(
                offset = Offset(it.offset.x * 2, it.offset.y * 2),
                blurRadius = it.blurRadius * 2
            )
        }),
    val offsetTextHover: Offset = Offset(
        textStyle.shadow?.let { -it.offset.x } ?: -2F,
        textStyle.shadow?.let { -it.offset.y } ?: -2F)
) {
    @Composable
    fun getElevation(): ButtonElevation = ButtonDefaults.elevation(
        defaultElevation = defaultElevation,
        pressedElevation = pressedElevation,
        hoveredElevation = hoveredElevation
    )

}

data class CheckboxStyleState(
    val parent: CommonInterfaceSetting.MySettings.MyChekboxStyleSetting,
    val colorFalse: Color = parent.color_false.getValue().toColor(),
    val colorTrue: Color = parent.color_true.getValue().toColor(),
    val textStyle: TextStyle = parent.TEXT_BUTTON.getValue(),
    val textStyleShadowHover: TextStyle = textStyle.copy(
        shadow = textStyle.shadow?.let {
            it.copy(
                offset = Offset(it.offset.x * 2, it.offset.y * 2),
                blurRadius = it.blurRadius * 2
            )
        }),
    val offsetTextHover: Offset = Offset(
        textStyle.shadow?.let { -it.offset.x } ?: -2F,
        textStyle.shadow?.let { -it.offset.y } ?: -2F)
)

open class ToggleButtonStyleState(
    parent: CommonInterfaceSetting.MySettings.ToggleButtonStyleItemSetting,
    val colorFalse: Color = parent.COLOR_FALSE.getValue().toColor(),
    val colorTrue: Color = parent.COLOR_TRUE.getValue().toColor(),
    val padingInner: Modifier = Modifier.paddingStyle(parent.inner_padding),
    val borderWidth: Dp = parent.BORDER_WIDTH.getValue().dp,
    val defaultElevation: Dp = parent.ELEVATION_DEFAULT.getValue().dp,
    val pressedElevation: Dp = parent.ELEVATION_PRESSED.getValue().dp,
    val hoveredElevation: Dp = parent.ELEVATION_HOVERED.getValue().dp,
    val shadow: Shadow = parent.shadow.getValue(),
    val background: Brush = parent.BACKGROUND.getValue(),
    val backgroundTrue: Brush = parent.BACKGROUND_TRUE.getValue(),
    val border: Brush = parent.BORDER.getValue(),
    val borderTrue: Brush = parent.BORDER_TRUE.getValue(),
    val shapeCard: Shape = parent.CORNER_BUTTON.getValue(),
) {
    @Composable
    fun getElevation(): ButtonElevation = ButtonDefaults.elevation(
        defaultElevation = defaultElevation,
        pressedElevation = pressedElevation,
        hoveredElevation = hoveredElevation
    )
}

open class IconButtonStyleState(
    parent: CommonInterfaceSetting.MySettings.IconButtonStyleItemSetting,
    val colorIcon: Color = parent.COLOR.getValue().toColor(),
    val padingInner: Modifier = Modifier.paddingStyle(parent.inner_padding),
    val borderWidth: Dp = parent.BORDER_WIDTH.getValue().dp,
    val defaultElevation: Dp = parent.ELEVATION_DEFAULT.getValue().dp,
    val pressedElevation: Dp = parent.ELEVATION_PRESSED.getValue().dp,
    val hoveredElevation: Dp = parent.ELEVATION_HOVERED.getValue().dp,
    val shadow: Shadow = parent.shadow.getValue(),
    val background: Brush = parent.BACKGROUND.getValue(),
    val border: Brush = parent.BORDER.getValue(),
    val shapeCard: Shape = parent.CORNER_BUTTON.getValue(),
) {
    @Composable
    fun getElevation(): ButtonElevation = ButtonDefaults.elevation(
        defaultElevation = defaultElevation,
        pressedElevation = pressedElevation,
        hoveredElevation = hoveredElevation
    )
}

open class IconButtonWithoutBorderStyleState(
    parent: CommonInterfaceSetting.MySettings.IconButtonWithoutBorderStyleSetting,
    val colorIcon: Color = parent.COLOR.getValue().toColor(),
    val defaultElevation: Dp = parent.ELEVATION_DEFAULT.getValue().dp,
    val pressedElevation: Dp = parent.ELEVATION_PRESSED.getValue().dp,
    val hoveredElevation: Dp = parent.ELEVATION_HOVERED.getValue().dp,
    val shadow: Shadow = parent.shadow.getValue(),
) {
    @Composable
    fun getElevation(): ButtonElevation = ButtonDefaults.elevation(
        defaultElevation = defaultElevation,
        pressedElevation = pressedElevation,
        hoveredElevation = hoveredElevation
    )
}


open class ToggleTextButtonStyleState(
    parent: CommonInterfaceSetting.MySettings.ToggleTextButtonStyleItemSetting,
    val textStyle: TextStyle = parent.styleText.getValue()
) : ToggleButtonStyleState(parent)

open class CommonItemStyleState(
    parent: CommonInterfaceSetting.MySettings.CommonStyleItemSetting,
    val active: Boolean = true,
    val elevation: Dp = parent.ELEVATION_CARD.getValue().dp,
    val paddingOuter: Modifier = Modifier.paddingStyle(parent.outer_padding),
    val paddingInner: Modifier = Modifier.paddingStyle(parent.inner_padding),
    val shapeCard: Shape = parent.corner.getValue(),
    val dropdown: DropDownMenuStyleState = DropDownMenuStyleState(parent.dropdown),
//    val shapeCardShadow: Shape = parent.corner.getValue(true),
    val borderWidth: Dp = parent.back_and_border.BORDER_WIDTH.getValue().dp,
    val borderWidthActive: Dp = parent.back_and_border.BORDER_WIDTH_ACTIVE.getValue().dp,
    val background: Brush = parent.back_and_border.BACKGROUND.getValue(),
    val border: Brush = parent.back_and_border.BORDER.getValue(),
    val borderActive: Brush = parent.back_and_border.BORDER_ACTIVE.getValue(),
    val mainTextStyle: TextStyle = parent.mainText.getValue(),
    val buttMenu: TextButtonStyleState = TextButtonStyleState(parent.menuButt),
    val shadow: Shadow = parent.shadow.getValue()
)

open class ItemStyleWithOpisSettingState(
    parent: CommonInterfaceSetting.MySettings.ItemStyleWithOpisSetting,
    active: Boolean = true,
    val boxOpisStyleState: BoxOpisWithButtStyleState = BoxOpisWithButtStyleState(parent.OPISANIE),
) : CommonItemStyleState(parent, active)

class ItemNapomStyleState(
    parent: StyleVMspis.InterfaceState.ItemNapomStyle,
    active: Boolean = parent.ACTIVETED_ITEM_STYLE.getValue(),
    val buttGotovColor: Color = parent.COLOR_BUTT_GOTOV.getValue().toColor(),
    val timeTextStyle: TextStyle = parent.timeText.getValue(),
    val backgroundGotov: Brush = parent.BACKGROUND_BRUSH_GOTOV.getValue(),
    val borderGotov: Brush = parent.BORDER_BRUSH_GOTOV.getValue(),
) : ItemStyleWithOpisSettingState(parent, active)

class ItemRasxDoxOperStyleState(
    parent: StyleVMspis.InterfaceState.ItemRasxDoxOperStyle,
    active: Boolean = parent.ACTIVETED_ITEM_STYLE.getValue(),
    val textType: TextStyle = parent.textType.getValue(),
    val textSchet: TextStyle = parent.textSchet.getValue(),
    val textSumm: TextStyle = parent.textSumm.getValue(),
    val textDate: TextStyle = parent.textDate.getValue(),
) : CommonItemStyleState(parent, active)

class ItemSchetGrafState(
    parent: StyleVMspis.InterfaceState.ItemSchetGraf,
    val plateMain: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.plateMain),
    val platePolos: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.platePolos),
    val platePolosMinus: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.platePolosMinus),
    val textName: TextStyle = parent.textName.getValue(),
    val textSumm: TextStyle = parent.textSumm.getValue(),
    val textValut: TextStyle = parent.textValut.getValue(),
    val outer_padding: Modifier = Modifier.paddingStyle(parent.outer_padding),
    val inner_padding: Modifier = Modifier.paddingStyle(parent.inner_padding),
    val borderActive: Brush = parent.BORDER_BRUSH_ACTIVE.getValue(),
    val border_width_active: Dp = parent.border_width_active.getValue().dp
)

class ItemSchetPlanGrafState(
    parent: StyleVMspis.InterfaceState.ItemSchetPlanGraf,
    val plateMain: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.plateMain),
    val plateFinGoal: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.plateFinGoal),
    val platePolos: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.platePolos),
    val platePolosMinus: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.platePolosMinus),
    val textName: TextStyle = parent.textName.getValue(),
    val textSumm: TextStyle = parent.textSumm.getValue(),
    val textValut: TextStyle = parent.textValut.getValue(),
    val outer_padding: Modifier = Modifier.paddingStyle(parent.outer_padding),
    val inner_padding: Modifier = Modifier.paddingStyle(parent.inner_padding),
    val outer_padding_goal: Modifier = Modifier.paddingStyle(parent.outer_padding_goal),
    val inner_padding_goal: Modifier = Modifier.paddingStyle(parent.inner_padding_goal),
    val borderActive: Brush = parent.BORDER_BRUSH_ACTIVE.getValue(),
    val border_width_active: Dp = parent.border_width_active.getValue().dp,

    val privPlGotBrush: Brush = parent.privPlGotBrush.getValue(),
    val privGoalGotBrush: Brush = parent.privGoalGotBrush.getValue(),
    val privPlanText: TextStyle = parent.privPlanText.getValue(),
    val platePrivPlan: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.platePrivPlan),
    val privGoalText: TextStyle = parent.privGoalText.getValue(),
    val platePrivGoal: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.platePrivGoal),
)

class PanSelectDateState(
    parent: StyleVMspis.InterfaceState.PanSelectDateStyle,
    val VIGNETTE: Boolean = parent.VIGNETTE.getValue(),
    val platePanel: SimplePlateStyleState = SimplePlateStyleState(parent.platePanel),
    val inner_padding: Modifier = Modifier.paddingStyle(parent.panelPadding),
    val plateDayWeek: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.plateDayWeek),
    val plateWeekend: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.plateWeekend),
    val textDayWeek: TextStyle = parent.textDayWeek.getValue(),
    val textWeekend: TextStyle = parent.textWeekend.getValue(),
    val textDate: TextStyle = parent.textDate.getValue(),
    val textMonth: TextStyle = parent.textMonth.getValue(),
    val buttArrow: TextButtonStyleState = TextButtonStyleState(parent.buttArrow),
    val buttYear: TextButtonStyleState = TextButtonStyleState(parent.buttYear),
    val buttYearActive: TextButtonStyleState = TextButtonStyleState(parent.buttYearActive),
    val buttNumber: TextButtonStyleState = TextButtonStyleState(parent.buttNumber),
    val buttNumberActive: TextButtonStyleState = TextButtonStyleState(parent.buttNumberActive),
    val buttNumberWeekend: TextButtonStyleState = TextButtonStyleState(parent.buttNumberWeekend),
    val buttNumberWeekendActive: TextButtonStyleState = TextButtonStyleState(parent.buttNumberWeekendActive),
    val buttSelect: TextButtonStyleState = TextButtonStyleState(parent.buttSelect),
    val buttCancel: TextButtonStyleState = TextButtonStyleState(parent.buttCancel),
)

class ItemVxodStyleState(
    parent: StyleVMspis.InterfaceState.ItemVxodStyle,
    active: Boolean = parent.ACTIVETED_ITEM_STYLE.getValue(),
    val dateTextStyle: TextStyle = parent.dateText.getValue(),
    val borderMulti: Modifier = Modifier
        .border(
            BorderStroke(
                parent.back_and_border.BORDER_WIDTH.getValue().dp,
                parent.back_and_border.BORDER.getValue()
            ), parent.corner.getValue()
        )
        .border(
            BorderStroke(
                (parent.back_and_border.BORDER_WIDTH.getValue() + parent.BORDER_WIDTH_2.getValue()).dp,
                parent.BORDER_BRUSH_2.getValue()
            ), parent.corner.getValue()
        )
        .border(
            BorderStroke(
                (parent.back_and_border.BORDER_WIDTH.getValue() + parent.BORDER_WIDTH_2.getValue() + parent.BORDER_WIDTH_3.getValue()).dp,
                parent.BORDER_BRUSH_3.getValue()
            ), parent.corner.getValue()
        )
        .border(
            BorderStroke(
                (parent.back_and_border.BORDER_WIDTH.getValue() + parent.BORDER_WIDTH_2.getValue() + parent.BORDER_WIDTH_3.getValue() + parent.BORDER_WIDTH_4.getValue()).dp,
                parent.BORDER_BRUSH_4.getValue()
            ), parent.corner.getValue()
        ),
//            parent.BORDER_BRUSH_GOTOV.getValue(),
) : ItemStyleWithOpisSettingState(parent, active)

class ItemBloknotStyleState(
    parent: StyleVMspis.InterfaceState.ItemBloknotStyle,
    active: Boolean = parent.ACTIVETED_ITEM_STYLE.getValue(),
    val buttOpenColor: Color = parent.COLOR_BUTT_OPEN.getValue().toColor(),
    val countTextStyle: TextStyle = parent.countText.getValue()
) : ItemStyleWithOpisSettingState(parent, active)

class ItemIdeaStyleState(
    parent: StyleVMspis.InterfaceState.ItemIdeaStyle,
    active: Boolean = parent.ACTIVETED_ITEM_STYLE.getValue(),
//    val buttOpenColor: Color = parent.COLOR_BUTT_OPEN.getValue().toColor(),
//    val countTextStyle: TextStyle = parent.countText.getValue()
) : ItemStyleWithOpisSettingState(parent, active)

class ItemIdeaStapStyleState(
    parent: StyleVMspis.InterfaceState.ItemIdeaStapStyle,
    active: Boolean = parent.ACTIVETED_ITEM_STYLE.getValue(),
    val buttOpenColor: Color = parent.COLOR_BUTT_OPEN.getValue().toColor(),
    val opisOpenText: BoxOpisStyleState = BoxOpisStyleState(parent.opisOpenText)
) : ItemStyleWithOpisSettingState(parent, active)

class ItemEffectStyleState(
    parent: StyleVMspis.InterfaceState.ItemEffectStyle,
    active: Boolean = parent.ACTIVETED_ITEM_STYLE.getValue(),
    val hourTextStyle: TextStyle = parent.hourText.getValue()
) : CommonItemStyleState(parent, active)

class ItemDenPlanStyleState(
    parent: StyleVMspis.InterfaceState.ItemDenPlanStyle,
    active: Boolean = parent.ACTIVETED_ITEM_STYLE.getValue(),
    val sliderColor1: Color = parent.SLIDER_COLOR1.getValue().toColor(),
    val sliderColor2: Color = parent.SLIDER_COLOR2.getValue().toColor(),
    val countTextStyle: TextStyle = parent.countText.getValue(),
    val timeTextStyle: TextStyle = parent.timeText.getValue(),
    val privPlanTextStyle: TextStyle = parent.privPlanText.getValue(),
    val privPlanBorderWidth: Dp = parent.BORDER_WIDTH_PRIV_PLAN.getValue().dp,
    val privPlanBorderBrush: Brush = parent.BORDER_PRIV_PLAN.getValue(),
    val shadow_priv_plan: Shadow = parent.shadow_priv_plan.getValue(),
    val privPlanBackground: Brush = parent.BACKGROUND_PRIV_PLAN.getValue(),
    val privPlanShape: Shape = parent.CORNER_PRIV_PLAN.getValue()


) : ItemStyleWithOpisSettingState(parent, active)

open class ItemBasePlateStyleState(
    parent: StyleVMspis.InterfaceState.ItemBasePlate,
    val outer_padding: Modifier = Modifier.paddingStyle(parent.outer_padding),
    val inner_padding: Modifier = Modifier.paddingStyle(parent.inner_padding),
    val plateItem: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.plateItem),
    val mainText: TextStyle = parent.mainText.getValue(),
    val mainTextHover: TextStyle = (mainText.shadow?.offset ?: Offset(2f, 2f)).let { offset ->
        mainText.copy(shadow = mainText.shadow?.copy(
            offset = Offset(offset.x * 2, offset.y * 2),
            blurRadius = mainText.shadow?.blurRadius?.let { it * 1.5f } ?: 4f
        ))
    }
)

class ItemCalendarDenPlanStyleState(
    parent: StyleVMspis.InterfaceState.ItemCalendarDenPlan,
    val denPlanStyle: ItemDenPlanStyleState,
    val color_indik_back: Color = parent.COLOR_INDIK_BACK.getValue().toColor(),
    val color_indik_complete: Color = parent.COLOR_INDIK_COMPLETE.getValue().toColor(),
    val color_indik_border: Color = parent.COLOR_INDIK_BORDER.getValue().toColor(),
    val border_brush_select: Brush = parent.BORDER_BRUSH_SELECT.getValue(),
    val texthour: TextStyle = parent.textHour.getValue(),
) : ItemBasePlateStyleState(parent)

class SimplePlateWithShadowStyleState(
    parent: CommonInterfaceSetting.MySettings.SimplePlateWithShadowStyleItemSetting,
    val BORDER_WIDTH: Dp = parent.BORDER_WIDTH.getValue().dp,
    val shadow: Shadow = parent.shadow.getValue(),
    val BACKGROUND: Brush = parent.BACKGROUND.getValue(),
    val BORDER: Brush = parent.BORDER.getValue(),
    val shape: Shape = parent.SHAPE.getValue()
)

class SimplePlateStyleState(
    parent: CommonInterfaceSetting.MySettings.SimplePlateStyleItemSetting,
    val BORDER_WIDTH: Dp = parent.BORDER_WIDTH.getValue().dp,
    val BACKGROUND: Brush = parent.BACKGROUND.getValue(),
    val BORDER: Brush = parent.BORDER.getValue(),
    val shape: Shape = parent.SHAPE.getValue()
)

class DropDownMenuStyleState(
    parent: CommonInterfaceSetting.MySettings.DropDownMenuStyleSetting,
    val textStyle: TextStyle = parent.textStyle.getValue(),
    val inner_padding: Modifier = Modifier.paddingStyle(parent.inner_padding),
    val BORDER_WIDTH: Dp = parent.BORDER_WIDTH.getValue().dp,
    val verticalPadding: Dp = parent.verticalPadding.getValue().dp,
    val BACKGROUND: Brush = parent.BACKGROUND.getValue(),
    val BACKGROUND_HOVERED: Brush = parent.BACKGROUND_HOVERED.getValue(),
    val BORDER: Brush = parent.BORDER.getValue(),
    val shape: Shape = parent.SHAPE.getValue(),
    val shape_for_shadow: CornerBasedShape = parent.SHAPE.getValue(hole = true),
)

class ComboBoxStyleState(
    parent: CommonInterfaceSetting.MySettings.ComboBoxStyleSetting,
    val textStyle: TextStyle = parent.textStyle.getValue(),
    val panel: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.panel),
    val color_open_butt: MyColorARGB = parent.color_open_butt.getValue(),
    val dropdown: DropDownMenuStyleState = DropDownMenuStyleState(parent.dropdownMenu),
)

class RectDiagramColorStyleState(
    parent: CommonInterfaceSetting.MySettings.RectDiagramColorStyleSetting,
    val color_year: Color = parent.COLOR_YEAR.getValue().toColor(),
    val color_year_border: Color = parent.COLOR_YEAR_BORDER.getValue().toColor(),
    val color_hour: Color = parent.COLOR_HOUR.getValue().toColor(),
    val color_hour_border: Color = parent.COLOR_HOUR_BORDER.getValue().toColor(),
    val color_hour_2: Color = parent.COLOR_HOUR_2.getValue().toColor(),
    val color_month: Color = parent.COLOR_MONTH.getValue().toColor(),
    val color_razdelit: Color = parent.COLOR_RAZDELIT.getValue().toColor(),
    val color_rect: Color = parent.COLOR_RECT.getValue().toColor(),
    val color_rect_select: Color = parent.COLOR_RECT_SELECT.getValue().toColor(),
    val color_rect_border: Color = parent.COLOR_RECT_BORDER.getValue().toColor(),
)

class TimelineDiagramColorsState(
    parent: StyleVMspis.InterfaceState.TimelineDiagramColors,
    val color_ramk: Color = parent.COLOR_RAMK.getValue().toColor(),
    val color_shkala: Color = parent.COLOR_SHKALA.getValue().toColor(),
    val color_between_days: Color = parent.COLOR_BETWEEN_DAYS.getValue().toColor(),
    val color_year: Color = parent.COLOR_YEAR.getValue().toColor(),
    val color_months_days: Color = parent.COLOR_MONTHS_DAYS.getValue().toColor(),
    val color_background: Color = parent.COLOR_BACKGROUND.getValue().toColor(),
    val color_background_near: Color = parent.COLOR_BACKGROUND_NEAR.getValue().toColor(),
    val color_background_end: Color = parent.COLOR_BACKGROUND_END.getValue().toColor(),
    val color_vignette: Color = parent.COLOR_VIGNETTE.getValue().toColor(),
    val color_weekend: Color = parent.COLOR_WEEKEND.getValue().toColor(),
    val color_current: Color = parent.COLOR_CURRENT.getValue().toColor(),
    val color_range_past: Color = parent.COLOR_RANGE_PAST.getValue().toColor(),
    val color_range_present: Color = parent.COLOR_RANGE_PRESENT.getValue().toColor(),
    val color_range_future: Color = parent.COLOR_RANGE_FUTURE.getValue().toColor(),
    val color_background_complete: Color = parent.COLOR_BACKGROUND_COMPLETE.getValue().toColor(),
    val color_background_close: Color = parent.COLOR_BACKGROUND_CLOSE.getValue().toColor(),
    val color_point_work: Color = parent.COLOR_POINT_WORK.getValue().toColor(),
)

class TwoRectDiagramColorStyleState(
    parent: CommonInterfaceSetting.MySettings.TwoRectDiagramColorStyleSetting,
    val color_year: Color = parent.COLOR_YEAR.getValue().toColor(),
    val color_year_stroke: Color = parent.COLOR_YEAR_BORDER.getValue().toColor(),
    val color_summa: Color = parent.COLOR_SUMMA.getValue().toColor(),
    val color_summa_2: Color = parent.COLOR_SUMMA_2.getValue().toColor(),
    val color_summa_stroke: Color = parent.COLOR_SUMMA_BORDER.getValue().toColor(),
    val color_summa_stroke_2: Color = parent.COLOR_SUMMA_BORDER_2.getValue().toColor(),
    val color_summa_os: Color = parent.COLOR_SUMMA_OS.getValue().toColor(),
    val color_summa_os_2: Color = parent.COLOR_SUMMA_OS_2.getValue().toColor(),
    val color_month: Color = parent.COLOR_MONTH.getValue().toColor(),
    val color_razdelit: Color = parent.COLOR_RAZDELIT.getValue().toColor(),
    val color_rect: Color = parent.COLOR_RECT.getValue().toColor(),
    val color_rect_select: Color = parent.COLOR_RECT_SELECT.getValue().toColor(),
    val color_rect_stroke: Color = parent.COLOR_RECT_BORDER.getValue().toColor(),
    val color_rect_2: Color = parent.COLOR_RECT_2.getValue().toColor(),
    val color_rect_select_2: Color = parent.COLOR_RECT_SELECT_2.getValue().toColor(),
    val color_rect_stroke_2: Color = parent.COLOR_RECT_BORDER_2.getValue().toColor(),
)

class GrafikColorStyleState(
    parent: CommonInterfaceSetting.MySettings.GrafikColorStyleSetting,
    val color_year: Color = parent.COLOR_YEAR.getValue().toColor(),
    val color_year_stroke: Color = parent.COLOR_YEAR_STROKE.getValue().toColor(),
    val color_summa: Color = parent.COLOR_SUMMA.getValue().toColor(),
    val color_razdelit: Color = parent.COLOR_RAZDELIT.getValue().toColor(),
    val color_os: Color = parent.COLOR_OS.getValue().toColor(),
    val color_os_cursor: Color = parent.COLOR_OS_CURSOR.getValue().toColor(),
    val color_summa_cursor: Color = parent.COLOR_SUMMA_CURSOR.getValue().toColor(),
    val color_cursor: Color = parent.COLOR_CURSOR.getValue().toColor(),
    val color_cursor_stroke: Color = parent.COLOR_CURSOR_STROKE.getValue().toColor(),
    val color_graf: Color = parent.COLOR_GRAF.getValue().toColor(),
    val color_graf_stroke: Color = parent.COLOR_GRAF_STROKE.getValue().toColor(),
)

class MyTextFieldStyleState(
    parent: CommonInterfaceSetting.MySettings.MyTextFieldStyle,
    val panelFocus: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.panelFocus),
    val panelUnfocus: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.panelUnfocus),
    val textMain: TextStyle = parent.textMain.getValue(),
    val textHint: TextStyle = parent.textHint.getValue(),
    val textNamePole: TextStyle = parent.textNamePole.getValue(),
    val textNamePoleFocus: TextStyle = parent.textNamePoleFocus.getValue(),
    val inner_padding: Modifier = Modifier.paddingStyle(parent.inner_padding),
    val START_NAME: Dp = parent.START_NAME.getValue().dp,
    val cursorBrush: Brush = parent.cursorBrush.getValue(),
)


class ItemSkillsTreeState(
    parent: StyleVMspis.InterfaceState.ItemSkillsTreeStyle,
    active: Boolean = parent.ACTIVETED_ITEM_STYLE.getValue(),
    val topPadding: Dp = parent.topPadding.getValue().dp,
    val OPEN_COLOR: Color = parent.OPEN_COLOR.getValue().toColor(),
    val ICON_TREE_COLOR: Color = parent.ICON_TREE_COLOR.getValue().toColor(),
    val INFO_ICON_COLOR: Color = parent.INFO_ICON_COLOR.getValue().toColor(),
    val countStapText: TextStyle = parent.countStapText.getValue(),
    val questNameText: TextStyle = parent.questNameText.getValue(),
    val BORDER_WIDTH_QUEST: Dp = parent.BORDER_WIDTH_QUEST.getValue().dp,
    val border_quest: Brush = parent.border_quest.getValue(),
    val icon_plate: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.icon_plate),
    val quest_plate: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.quest_plate),
    val background_brush_no_edit: Brush = parent.background_brush_no_edit.getValue(),
    val border_brush_no_edit: Brush = parent.border_brush_no_edit.getValue(),
    val background_brush_unblock: Brush = parent.background_brush_unblock.getValue(),
    val border_brush_unblock: Brush = parent.border_brush_unblock.getValue(),
    val background_brush_block: Brush = parent.background_brush_block.getValue(),
    val border_brush_block: Brush = parent.border_brush_block.getValue(),
    val background_brush_complete: Brush = parent.background_brush_complete.getValue(),
    val border_brush_complete: Brush = parent.border_brush_complete.getValue(),
) : ItemStyleWithOpisSettingState(parent, active)

class ItemNodeTreeSkillsState(
    parent: StyleVMspis.InterfaceState.ItemNodeTreeSkillsStyle,
    active: Boolean = parent.ACTIVETED_ITEM_STYLE.getValue(),
    val border_width_must: Dp = parent.border_width_must.getValue().dp,
    val border_width_icon: Dp = parent.border_width_icon.getValue().dp,
    val border_width_parent_child: Dp = parent.border_width_parent_child.getValue().dp,
    val BORDER_ICON_COLOR: Brush = parent.BORDER_ICON_COLOR.getValue(),
    val INFO_ICON_COLOR: Color = parent.INFO_ICON_COLOR.getValue().toColor(),
    val noQuestText: TextStyle = parent.noQuestText.getValue(),
    val background_brush_complete: Brush = parent.background_brush_complete.getValue(),
    val border_brush_complete: Brush = parent.border_brush_complete.getValue(),
    val background_brush_unblock: Brush = parent.background_brush_unblock.getValue(),
    val border_brush_unblock: Brush = parent.border_brush_unblock.getValue(),
    val background_brush_block: Brush = parent.background_brush_block.getValue(),
    val border_brush_block: Brush = parent.border_brush_block.getValue(),
    val border_brush_DIRECTPARENT: Brush = parent.border_brush_DIRECTPARENT.getValue(),
    val border_brush_INDIRECTPARENT: Brush = parent.border_brush_INDIRECTPARENT.getValue(),
    val border_brush_DIRECTCHILD: Brush = parent.border_brush_DIRECTCHILD.getValue(),
    val border_brush_INDIRECTCHILD: Brush = parent.border_brush_INDIRECTCHILD.getValue(),
) : ItemStyleWithOpisSettingState(parent, active)

class ItemSkillsTreeLevelState(
    parent: StyleVMspis.InterfaceState.ItemSkillsTreeLevelStyle,
    active: Boolean = parent.ACTIVETED_ITEM_STYLE.getValue(),
    val INFO_COLOR_1: Color = parent.INFO_COLOR_1.getValue().toColor(),
    val INFO_COLOR_2: Color = parent.INFO_COLOR_2.getValue().toColor(),
    val infoText: TextStyle = parent.infoText.getValue(),
    val noQuestText: TextStyle = parent.noQuestText.getValue(),
    val background_brush_no_edit: Brush = parent.background_brush_no_edit.getValue(),
    val border_brush_no_edit: Brush = parent.border_brush_no_edit.getValue(),
    val background_brush_no_edit_block: Brush = parent.background_brush_no_edit_block.getValue(),
    val border_brush_no_edit_block: Brush = parent.border_brush_no_edit_block.getValue(),
    val background_brush_block: Brush = parent.background_brush_block.getValue(),
    val border_brush_block: Brush = parent.border_brush_block.getValue(),
) : ItemStyleWithOpisSettingState(parent, active)

class ItemGoalStyleState(
    parent: StyleVMspis.InterfaceState.ItemGoalStyle,
    active: Boolean = parent.ACTIVETED_ITEM_STYLE.getValue(),
    val arrow_color: Color = parent.ARROW_COLOR.getValue().toColor(),
    val hourTextStyle: TextStyle = parent.hourText.getValue(),
    val background_brush_gotov: Brush = parent.background_brush_gotov.getValue(),
    val border_brush_gotov: Brush = parent.border_brush_gotov.getValue(),
) : ItemStyleWithOpisSettingState(parent, active)

class ItemDreamStyleState(
    parent: StyleVMspis.InterfaceState.ItemDreamStyle,
    active: Boolean = parent.ACTIVETED_ITEM_STYLE.getValue(),
    val arrow_color: Color = parent.ARROW_COLOR.getValue().toColor(),
    val background_brush_gotov: Brush = parent.background_brush_gotov.getValue(),
    val border_brush_gotov: Brush = parent.border_brush_gotov.getValue(),
) : ItemStyleWithOpisSettingState(parent, active)

class ItemBestDayStyleState(
    parent: StyleVMspis.InterfaceState.ItemBestDayStyle,
    active: Boolean = parent.ACTIVETED_ITEM_STYLE.getValue(),
    val BLACKandWHITE: Boolean = parent.BLACKandWHITE.getValue(),
    val TINT: Color = parent.TINT.getValue().toColor(),
    val image_border_width: Dp = parent.image_border_width.getValue().dp,
    val image_border_brush: Brush = parent.image_border_brush.getValue(),
    val image_shape: Shape = parent.image_shape.getValue(),
    val image_shadow: Shadow = parent.image_shadow.getValue(),
    val open_butt_color: Color = parent.OPEN_BUTT_COLOR.getValue().toColor(),
    val dateText: TextStyle = parent.dateText.getValue()
) : ItemStyleWithOpisSettingState(parent, active)

class ItemCharacteristicState(
    parent: StyleVMspis.InterfaceState.ItemCharacteristicStyle,
    active: Boolean = parent.ACTIVETED_ITEM_STYLE.getValue(),
    val plateNonEdit: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.plateNonEdit),
    val outerPaddingNonEdit: Modifier = Modifier.paddingStyle(parent.outerPaddingNonEdit),
    val innerPaddingNonEdit: Modifier = Modifier.paddingStyle(parent.innerPaddingNonEdit),
    val ARROW_COLOR: Color = parent.ARROW_COLOR.getValue().toColor(),
    val COLOR_INDIK_BACK: Color = parent.COLOR_INDIK_BACK.getValue().toColor(),
    val COLOR_INDIK_COMPLETE: Color = parent.COLOR_INDIK_COMPLETE.getValue().toColor(),
    val COLOR_INDIK_BORDER: Color = parent.COLOR_INDIK_BORDER.getValue().toColor(),
    val startValueText: TextStyle = parent.startValueText.getValue(),
    val valueText: TextStyle = parent.valueText.getValue()
) : ItemStyleWithOpisSettingState(parent, active)

class ItemStatusStyleState(
    parent: StyleVMspis.InterfaceState.ItemStatusStyle,
    val outer_padding: Modifier = Modifier.paddingStyle(parent.outer_padding),
    val inner_padding: Modifier = Modifier.paddingStyle(parent.inner_padding),
    val shape: Shape = parent.shape.getValue(),
    val BORDER_WIDTH: Dp = parent.BORDER_WIDTH.getValue().dp,
    val BACKGROUND: Brush = parent.BACKGROUND.getValue(),
    val BORDER: Brush = parent.BORDER.getValue(),
    val mainText: TextStyle = parent.mainText.getValue(),
    val valueText: TextStyle = parent.valueText.getValue(),
    val shadow: Shadow = parent.shadow.getValue(),
)

class ItemPlanStyleState(
    parent: StyleVMspis.InterfaceState.ItemPlanStyle,
    active: Boolean = parent.ACTIVETED_ITEM_STYLE.getValue(),
    val sliderInactive: Color = parent.SLIDER_COLOR1.getValue().toColor(),
    val sliderThumb: Color = parent.SLIDER_COLOR2.getValue().toColor(),
    val hourTextStyle: TextStyle = parent.hourText.getValue(),

    val countStapText: TextStyle = parent.countStapText.getValue(),
    val questNameText: TextStyle = parent.questNameText.getValue(),
    val BORDER_WIDTH_QUEST: Dp = parent.BORDER_WIDTH_QUEST.getValue().dp,
    val border_quest: Brush = parent.border_quest.getValue(),
    val background_brush_gotov: Brush = parent.background_brush_gotov.getValue(),
    val border_brush_gotov: Brush = parent.border_brush_gotov.getValue(),
    val background_brush_unblock: Brush = parent.background_brush_unblock.getValue(),
    val border_brush_unblock: Brush = parent.border_brush_unblock.getValue(),
    val background_brush_freeze: Brush = parent.background_brush_freeze.getValue(),
    val border_brush_freeze: Brush = parent.border_brush_freeze.getValue(),
    val background_brush_close: Brush = parent.background_brush_close.getValue(),
    val border_brush_close: Brush = parent.border_brush_close.getValue(),

    val background_brush_direction: Brush = parent.background_brush_direction.getValue(),
    val border_brush_direction: Brush = parent.border_brush_direction.getValue(),
    val background_brush_direction_freeze: Brush = parent.background_brush_direction_freeze.getValue(),
    val border_brush_direction_freeze: Brush = parent.border_brush_direction_freeze.getValue(),
    val background_brush_direction_close: Brush = parent.background_brush_direction_close.getValue(),
    val border_brush_direction_close: Brush = parent.border_brush_direction_close.getValue(),
    val dataText: TextStyle = parent.dataText.getValue(),
    val plateSrok: SimplePlateStyleState = SimplePlateStyleState(parent.plateSrok),
    val plateSrokIn: SimplePlateStyleState = SimplePlateStyleState(parent.plateSrokIn),

    val quest_plate: SimplePlateWithShadowStyleState = SimplePlateWithShadowStyleState(parent.quest_plate),
) : ItemStyleWithOpisSettingState(parent, active)

class ItemPlanStapStyleState(
    parent: StyleVMspis.InterfaceState.ItemPlanStapStyle,
    active: Boolean = parent.ACTIVETED_ITEM_STYLE.getValue(),
    val levelValue: Double = parent.levelValue.getValue(),
    val sliderInactive: Color = parent.SLIDER_COLOR1.getValue().toColor(),
    val sliderThumb: Color = parent.SLIDER_COLOR2.getValue().toColor(),
    val plus_color: Color = parent.PLUS_COLOR.getValue().toColor(),
    val minus_color: Color = parent.MINUS_COLOR.getValue().toColor(),
    val hourTextStyle: TextStyle = parent.hourText.getValue(),
    val countStapText: TextStyle = parent.countStapText.getValue(),
    val noQuestText: TextStyle = parent.noQuestText.getValue(),
    val background_brush_gotov: Brush = parent.background_brush_gotov.getValue(),
    val border_brush_gotov: Brush = parent.border_brush_gotov.getValue(),
    val background_brush_unblock: Brush = parent.background_brush_unblock.getValue(),
    val border_brush_unblock: Brush = parent.border_brush_unblock.getValue(),
    val background_brush_freeze: Brush = parent.background_brush_freeze.getValue(),
    val border_brush_freeze: Brush = parent.border_brush_freeze.getValue(),
    val background_brush_close: Brush = parent.background_brush_close.getValue(),
    val border_brush_close: Brush = parent.border_brush_close.getValue(),
    val dataText: TextStyle = parent.dataText.getValue(),
    val plateSrok: SimplePlateStyleState = SimplePlateStyleState(parent.plateSrok),
    val plateSrokIn: SimplePlateStyleState = SimplePlateStyleState(parent.plateSrokIn)
) : ItemStyleWithOpisSettingState(parent, active)

class ButtonSeekBarStyleState(
    parent: CommonInterfaceSetting.MySettings.ButtonSeekBarStyleItemSetting,
    val borderWidth: Dp = parent.BORDER_WIDTH.getValue().dp,
    val defaultElevation: Dp = parent.ELEVATION_DEFAULT.getValue().dp,
    val pressedElevation: Dp = parent.ELEVATION_PRESSED.getValue().dp,
    val hoveredElevation: Dp = parent.ELEVATION_HOVERED.getValue().dp,
    val shadow: Shadow = parent.shadow.getValue(),
    val cornerIndiv: Boolean = parent.CORNER_IND_ENABLE.getValue(),
    val background: Brush = parent.BACKGROUND.getValue(),
    val border: Brush = parent.BORDER.getValue(),
    val backgroundActive: Brush = parent.BACKGROUND_ACTIVE.getValue(),
    val borderActive: Brush = parent.BORDER_ACTIVE.getValue(),

    val shapeCardCenter: Shape = parent.CORNER_BUTTON.getValue(
        indiv = parent.CORNER_IND_ENABLE.getValue(),
        topStart = false,
        bottomStart = false,
        topEnd = false,
        bottomEnd = false
    ),
    val shapeCardIndiv: Shape = parent.CORNER_BUTTON.getValue(),
    val shapeCardTop: Shape = parent.CORNER_BUTTON.getValue(
        indiv = parent.CORNER_IND_ENABLE.getValue(),
        topStart = true,
        bottomStart = false,
        topEnd = true,
        bottomEnd = false
    ),
    val shapeCardBottom: Shape = parent.CORNER_BUTTON.getValue(
        indiv = parent.CORNER_IND_ENABLE.getValue(),
        topStart = false,
        bottomStart = true,
        topEnd = false,
        bottomEnd = true
    ),
    val shapeCardStart: Shape = parent.CORNER_BUTTON.getValue(
        indiv = parent.CORNER_IND_ENABLE.getValue(),
        topStart = true,
        bottomStart = true,
        topEnd = false,
        bottomEnd = false
    ),
    val shapeCardEnd: Shape = parent.CORNER_BUTTON.getValue(
        indiv = parent.CORNER_IND_ENABLE.getValue(),
        topStart = false,
        bottomStart = false,
        topEnd = true,
        bottomEnd = true
    ),
    val textStyle: TextStyle = parent.TEXT_BUTTON.getValue(),
    val textStyleShadowHover: TextStyle = textStyle.copy(
        shadow = textStyle.shadow?.let {
            it.copy(
                offset = Offset(it.offset.x * 2, it.offset.y * 2),
                blurRadius = it.blurRadius * 2
            )
        }),
    val offsetTextHover: Offset = Offset(
        textStyle.shadow?.let { -it.offset.x } ?: -2F,
        textStyle.shadow?.let { -it.offset.y } ?: -2F)
) {
    @Composable
    fun getElevation(): ButtonElevation = ButtonDefaults.elevation(
        defaultElevation = defaultElevation,
        pressedElevation = pressedElevation,
        hoveredElevation = hoveredElevation
    )
}

data class TodayPlateStyleState(
    val parent: StyleVMspis.InterfaceState.TodayPlateParam,
    val colorToday: Color = parent.COLOR_TODAY.getValue().toColor(),
    val colorTomorrow: Color = parent.COLOR_TOMORROW.getValue().toColor(),
    val colorYesterday: Color = parent.COLOR_YESTERDAY.getValue().toColor(),
    val colorTomorrowEnd: Color = parent.COLOR_TOMORROW_END.getValue().toColor(),
    val colorYesterdayEnd: Color = parent.COLOR_YESTERDAY_END.getValue().toColor(),
    val borderWidth: Dp = parent.BORDER_WIDTH.getValue().dp,
    val borderBrush: Brush = parent.BORDER_BRUSH.getValue(),
    val shadow: Shadow = parent.shadow.getValue(),
    val padingInner: Modifier = Modifier.paddingStyle(parent.inner_padding),
    val shape: Shape = parent.shape.getValue(),
    val textStyle: TextStyle = parent.textStyle.getValue()
)

data class PrivSchetPlanInfoStyleState(
    val parent: StyleVMspis.InterfaceState.PrivSchetPlanInfo,
    val textStyle: TextStyle = parent.textStyle.getValue(),
    val textStyleInBox: TextStyle = parent.textStyleInBox.getValue(),
    val textStyleInBox2: TextStyle = parent.textStyleInBox2.getValue(),
    val color_t_goal: Color = parent.COLOR_T_GOAL.getValue().toColor(),
    val color_t_ostat: Color = parent.COLOR_T_OSTAT.getValue().toColor(),
    val color_t_perer: Color = parent.COLOR_T_PERER.getValue().toColor(),
    val color_t_iz: Color = parent.COLOR_T_IZ.getValue().toColor(),
    val shapeBox: Shape = parent.shapeBox.getValue(),
    val shadow: Shadow = parent.shadow.getValue(),

    val color_b_max_back: Color = parent.COLOR_B_MAX_BACK.getValue().toColor(),
    val color_b_min_back: Color = parent.COLOR_B_MIN_BACK.getValue().toColor(),
    val color_b_rasxod: Color = parent.COLOR_B_RASXOD.getValue().toColor(),
    val color_b_rasxod2: Color = parent.COLOR_B_RASXOD2.getValue().toColor(),
    val color_b_doxod: Color = parent.COLOR_B_DOXOD.getValue().toColor(),

    val borderWidth: Dp = parent.BORDER_WIDTH.getValue().dp,
    val borderBrush: Brush = parent.BORDER_BRUSH.getValue(),
)
