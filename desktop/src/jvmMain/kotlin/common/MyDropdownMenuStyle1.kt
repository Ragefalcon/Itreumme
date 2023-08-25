package common

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import extensions.DropDownMenuStyleState
import extensions.TextButtonStyleState
import extensions.toColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import viewmodel.MainDB
import java.util.*

@OptIn(ExperimentalFoundationApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun DropMenuRightClickArea(
    modifier: Modifier = Modifier,
    onClick: MouseClickScope.() -> Unit = {},
    onDoubleClick: MouseClickScope.() -> Unit = {},
    dropMenu: @Composable ColumnScope.(MutableState<Boolean>) -> Unit = { },
    content: @Composable () -> Unit
) {
    var timePause = Date().time
    var xBox by remember { mutableStateOf(0.dp) }
    var yBox by remember { mutableStateOf(0.dp) }
    val expandedDropMenuRightButton = remember { mutableStateOf(false) }

    Box(modifier
        .pointerMoveFilter(
            onMove = {
                if (!expandedDropMenuRightButton.value) {
                    xBox = it.x.dp - 8.dp
                    yBox = it.y.dp - 1.dp
                }
                false
            }
        )
        .mouseClickable(
            onClick = {
                val tmpTime = Date().time
                val tmpTime2 = timePause
                if (this.buttons.isPrimaryPressed) {
                    timePause = tmpTime
                }
                if (tmpTime - tmpTime2 < 250 && this.buttons.isPrimaryPressed) {
                    onDoubleClick()
                } else {
                    expandedDropMenuRightButton.value = this.buttons.isSecondaryPressed
                    onClick()
                }
            }
        )
    ) {
        content()
        if (expandedDropMenuRightButton.value) Box(
            Modifier.fillMaxWidth()
                .padding(start = if (xBox >= 0.dp) xBox else 0.dp, top = if (yBox >= 0.dp) yBox else 0.dp)
        ) {
            Box(Modifier.height(0.dp).width(0.dp)) {
                MyDropdownMenuStyle1(expandedDropMenuRightButton) { setDissFun ->
                    dropMenu(expandedDropMenuRightButton)
                }
            }
        }
    }
}


@Composable
fun MyDropdownMenuStyle1(
    expanded: MutableState<Boolean>,
    dropMenu: @Composable ColumnScope.((() -> Unit) -> Unit) -> Unit
) {
    var dismissFun: () -> Unit = {}
    fun setDismissFun(dissFun: () -> Unit): Unit {
        dismissFun = dissFun
    }
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = {
            expanded.value = false
        },
        modifier = Modifier.background(Color(0xFF4F534F)).heightIn(0.dp, 400.dp)
            .wrapContentHeight()
            .graphicsLayer {
                shape = RoundedCornerShape(1.dp)
            }
    ) {
        dropMenu(::setDismissFun)
    }
}

@Composable
fun MyButtDropdownMenuStyle1(
    modifier: Modifier = Modifier,
    expandedDropMenu: MutableState<Boolean>,
    dropMenu: @Composable ColumnScope.((() -> Unit) -> Unit) -> Unit
) {
    Box {
        MyTextButtStyle1(
            "⋮",
            modifier = modifier.width(40.dp).height(40.dp),
            7.dp,
            modifierText = Modifier.padding(bottom = 3.dp),
            fontSize = 13.sp
        ) {
            expandedDropMenu.value = true
        }
        MyDropdownMenuStyle1(expandedDropMenu) { setDissFun ->
            dropMenu(setDissFun)
        }
    }
}

@Composable
fun MyButtDropdownMenuStyle2(
    modifier: Modifier = Modifier,
    expandedDropMenu: MutableState<Boolean>,
    myStyleTextButton: TextButtonStyleState? = null,
    styleDropdown: DropDownMenuStyleState = DropDownMenuStyleState(MainDB.styleParam.commonParam.commonDropdownMenuStyle),
    dropMenu: @Composable ColumnScope.() -> Unit
) {
    Box {
        MyTextButtStyle2(
            "⋮",
            modifier = modifier.width(25.dp).height(25.dp),
            modifierText = Modifier.padding(bottom = 0.dp),
            fontSize = 18.sp,
            myStyleTextButton = myStyleTextButton
        ) {
            expandedDropMenu.value = true
        }
        MyDropdownMenu(expandedDropMenu, styleDropdown) {
            dropMenu()
        }
    }
}


@Composable
fun MyDropdownMenuItem(expanded: MutableState<Boolean>, name: String, onClick: () -> Unit) {
    DropdownMenuItem(onClick = {
        onClick()
        expanded.value = false
    }) {
        Text(
            text = name,
            color = Color.White
        )
    }
}


@Composable
fun MyDeleteDropdownMenuButton(
    expanded: MutableState<Boolean>,
    onFinal: () -> Unit
) {
    MyCommonSliderButt(
        text = "Удалить",
        MySliderButtColor.red,
        reverse = true
    ) {
        expanded.value = false
        onFinal()
    }
}

@Composable
fun MyCompleteDropdownMenuButton(
    expanded: MutableState<Boolean>,
    complete: Boolean = false,
    textToFalse: String = "Развыполнить",
    textToTrue: String = "Выполнить",
    onFinal: () -> Unit
) {
    MyCommonSliderButt(
        text = if (complete) textToFalse else textToTrue,
        MySliderButtColor.run { if (complete) uncompleteCol else completeCol }
    ) {
        expanded.value = false
        onFinal()
    }
}

data class MySliderButtColor(
    val mainColor: Color,
    val thumbColor: Color,
    val activeTrackColor: Color,
    val inactiveTrackColor: Color,
    val disabledActiveTickColor: Color,
) {
    companion object {
        val completeCol = MySliderButtColor(
            mainColor = MyColorARGB.colorEffektShkal_Nedel.toColor(),
            thumbColor = MyColorARGB.colorEffektShkal_Nedel.toColor(),
            activeTrackColor = MyColorARGB.colorEffektShkal_Nedel.toColor(),
            inactiveTrackColor = Color(0xAFFF8888),
            disabledActiveTickColor = MyColorARGB.colorEffektShkal_Nedel.toColor()
                .copy(alpha = SliderDefaults.TickAlpha)
                .copy(alpha = SliderDefaults.DisabledTickAlpha)
        )
        val uncompleteCol = MySliderButtColor(
            mainColor = MyColorARGB.colorEffektShkal_Nedel.toColor(),
            thumbColor = Color(0xAFFF8888),
            activeTrackColor = Color(0xAFFF8888),
            inactiveTrackColor = MyColorARGB.colorEffektShkal_Nedel.toColor(),
            disabledActiveTickColor = Color(0xAFFF8888).copy(alpha = SliderDefaults.DisabledTickAlpha)
        )
        val yellow = MySliderButtColor(
            mainColor = Color.Yellow,
            thumbColor = MyColorARGB.colorEffektShkal_Nedel.toColor(),
            activeTrackColor = MyColorARGB.colorEffektShkal_Nedel.toColor(),
            inactiveTrackColor = Color(0xAFFF8888),
            disabledActiveTickColor = MyColorARGB.colorEffektShkal_Nedel.toColor()
        )
        val red = MySliderButtColor(
            mainColor = Color.Red,
            thumbColor = Color(0x6FFF8888),
            activeTrackColor = MyColorARGB.colorEffektShkal_Nedel.toColor(),
            inactiveTrackColor = Color(0xAFFF8888),
            disabledActiveTickColor = Color.Green.copy(alpha = SliderDefaults.TickAlpha)
                .copy(alpha = SliderDefaults.DisabledTickAlpha)
        )
    }
}

@Composable
fun MyCommonSliderButt(
    text: String,
    colors: MySliderButtColor = MySliderButtColor.yellow,
    modifier: Modifier = Modifier.width(130.dp),
    reverse: Boolean = false,
    oneMoment: Boolean = false,
    onFinal: () -> Unit
) {
    val startValue: Float = if (reverse) 1f else 0f
    val finishValue: Float = if (reverse) 0f else 1f
    val progressGotov = remember { mutableStateOf(startValue) }
    val animProg = remember { mutableStateOf(false) }
    val fin = remember { mutableStateOf(false) }
    Column(
        modifier
            .background(colors.mainColor.copy(alpha = 0.3f), RoundedCornerShape(5.dp))
            .border(1.dp, colors.mainColor, RoundedCornerShape(5.dp))
            .padding(3.dp),
        horizontalAlignment = Alignment.run { if (reverse) Start else End }
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Box() {
            Slider(
                value = progressGotov.value,
                modifier = Modifier.height(25.dp).fillMaxWidth().padding(start = 0.dp),
                onValueChange = {
                    animProg.value = false
                    progressGotov.value = it
                    if (progressGotov.value == finishValue && oneMoment) {
                        if (!fin.value) {
                            fin.value = true
                            onFinal()
                        }
                    }
                },
                onValueChangeFinished = {
                    if (progressGotov.value == finishValue) {
                        progressGotov.value = startValue
                        if (!fin.value) {
                            fin.value = true
                            onFinal()
                            progressGotov.value = startValue
                            fin.value = false
                        }
                    } else {
                        animProg.value = true
                        CoroutineScope(Dispatchers.Default).launch {
                            if (reverse) while (animProg.value && progressGotov.value + 0.03 <= startValue) {
                                progressGotov.value = progressGotov.value + 0.03f
                                delay(2)
                            } else while (animProg.value && progressGotov.value - 0.03 >= startValue) {
                                progressGotov.value = progressGotov.value - 0.03f
                                delay(2)
                            }
                            if (animProg.value) progressGotov.value = startValue
                        }
                    }
                },
                colors = SliderDefaults.colors(
                    thumbColor = colors.thumbColor,
                    disabledThumbColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
                        .compositeOver(MaterialTheme.colors.surface),
                    activeTrackColor = colors.activeTrackColor,
                    inactiveTrackColor = colors.inactiveTrackColor,
                    disabledActiveTrackColor = MaterialTheme.colors.onSurface.copy(alpha = SliderDefaults.DisabledActiveTrackAlpha),
                    disabledInactiveTrackColor = MaterialTheme.colors.onSurface.copy(alpha = SliderDefaults.DisabledInactiveTrackAlpha),
                    activeTickColor = contentColorFor(Color.Blue).copy(alpha = SliderDefaults.TickAlpha),
                    inactiveTickColor = Color.Magenta.copy(alpha = SliderDefaults.TickAlpha),
                    disabledActiveTickColor = colors.disabledActiveTickColor,
                    disabledInactiveTickColor = MaterialTheme.colors.onSurface.copy(alpha = SliderDefaults.DisabledInactiveTrackAlpha)
                        .copy(alpha = SliderDefaults.DisabledTickAlpha)
                )
            )
            Box(Modifier.padding(start = if (reverse) 0.dp else 40.dp, end = if (reverse) 40.dp else 0.dp).height(25.dp)
                .fillMaxWidth().clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { progressGotov.value = startValue })
        }
    }
}