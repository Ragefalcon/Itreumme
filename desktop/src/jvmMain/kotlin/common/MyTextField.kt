package common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import extensions.MyTextFieldStyleState

@Composable
fun MyTextFieldInt(
    value: MutableState<TextFieldValue>,
    modifier: Modifier = Modifier,
    label: String = "",
    hint: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    width: Dp? = null,
    height: Dp? = null,
    textAlign: TextAlign = TextAlign.Start,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    onValueChange: (TextFieldValue) -> Unit = { value.value = it },
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    style: MyTextFieldStyleState
) {
    val composit = value.value.composition

    fun customStrToInt(str: String): String {
        val matchedResults = Regex(pattern = """[0-9]""").findAll(input = str)
        val result = StringBuilder()
        for (matchedText in matchedResults) {
            result.append(matchedText.value + "")
        }
        return result.toString()
    }
    MyTextField(
        value = value,
        onValueChange = {

            val str1 = "[0-9]+".toRegex().find(it.text, 0)?.value ?: ""

            val check = str1 == customStrToInt(it.text)
            if (check) {
                onValueChange(it.copy(text = str1))
            } else {
                onValueChange(value.value.copy(composition = if (composit != null) null else TextRange.Zero))
            }
        },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        width = width,
        height = height,
        textAlign = textAlign,
        label = label,
        hint = hint,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        interactionSource = interactionSource,
        style = style
    )
}

@Composable
fun MyTextFieldDouble(
    value: MutableState<TextFieldValue>,
    modifier: Modifier = Modifier,
    label: String = "",
    hint: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    width: Dp? = null,
    height: Dp? = null,
    textAlign: TextAlign = TextAlign.Start,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    onValueChange: (TextFieldValue) -> Unit = { value.value = it },
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    style: MyTextFieldStyleState
) {
    val composit = value.value.composition

    fun customStrToDouble(str: String): String {
        val matchedResults = Regex(pattern = """[0-9,.]+""").findAll(input = str)
        val result = StringBuilder()
        for (matchedText in matchedResults) {
            result.append(matchedText.value + "")
        }
        var rez = ",".toRegex().replace(result, ".")
        for (i in 1 until "\\.".toRegex().findAll(rez).count()) {
            rez = "\\.".toRegex().replaceFirst(rez, "")
        }
        rez = "[0-9]+\\.?[0-9]?[0-9]?".toRegex().find(rez, 0)?.value ?: ""
        return rez
    }
    MyTextField(
        value = value,
        onValueChange = {
            var str1 = ",".toRegex().replace(it.text, ".")
            str1 = "[0-9]+\\.?[0-9]?[0-9]?".toRegex().find(str1, 0)?.value ?: ""

            val check = str1 == customStrToDouble(it.text)
            if (check) {
                onValueChange(it.copy(text = str1))
            } else {
                onValueChange(value.value.copy(composition = if (composit != null) null else TextRange.Zero))
            }
        },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        width = width,
        height = height,
        textAlign = textAlign,
        label = label,
        hint = hint,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        interactionSource = interactionSource,
        style = style
    )
}

@Composable
fun MyTextField(
    value: MutableState<TextFieldValue>,
    modifier: Modifier = Modifier,
    label: String = "",
    hint: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    width: Dp? = null,
    height: Dp? = null,
    textAlign: TextAlign = TextAlign.Start,
    onValueChange: (TextFieldValue) -> Unit = { value.value = it },
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    style: MyTextFieldStyleState
) {

    val interactionSourceHover: MutableInteractionSource = remember { MutableInteractionSource() }
    val focus by interactionSource.collectIsFocusedAsState()
    val hover by interactionSourceHover.collectIsHoveredAsState()

    with(style) {
        (BasicTextField(
            value = value.value,
            modifier = modifier


                .defaultMinSize(
                    minWidth = 100.dp,

                    ),
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textMain.copy(textAlign = textAlign),
            cursorBrush = style.cursorBrush,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            singleLine = singleLine,
            maxLines = maxLines,
            decorationBox = @Composable { innerTextField ->
                with(LocalDensity.current) {
                    (if (focus || hover) textNamePoleFocus else textNamePole).let { textName ->
                        MeasureUnconstrainedViewWidth(
                            {
                                Text(
                                    label,
                                    style = textName
                                )
                            }
                        ) { widthText ->
                            (if (focus) panelFocus else panelUnfocus).let { panel ->
                                MyShadowBox(panel.shadow) {
                                    Box(Modifier.hoverable(interactionSourceHover)) {
                                        Row(
                                            Modifier
                                                .padding(top = (textName.fontSize / 2).toDp())
                                                .run {
                                                    width?.let { this.width(it) } ?: this
                                                }
                                                .run {
                                                    height?.let { this.height(it) } ?: this
                                                }
                                                .defaultMinSize(minWidth = START_NAME + 15.dp + widthText)
                                                .background(
                                                    panel.BACKGROUND,
                                                    panel.shape
                                                )
                                                .focusable(true, interactionSource)
                                                .then(inner_padding)
                                        ) {
                                            Box(
                                                Modifier.run {
                                                    width?.let { this.fillMaxWidth() } ?: this
                                                },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Box {
                                                    if (value.value.text.isEmpty()) {
                                                        Text(
                                                            hint,
                                                            style = textHint.copy(textAlign = textAlign)
                                                        )
                                                    }
                                                    innerTextField()
                                                }
                                            }
                                        }
                                        Box(Modifier.matchParentSize().padding(top = (textName.fontSize / 2).toDp())
                                            .drawWithCache {

                                                onDrawWithContent {
                                                    clipRect(
                                                        (START_NAME - 5.dp).toPx(),
                                                        0f,
                                                        (START_NAME + 5.dp + widthText).toPx(),
                                                        textName.fontSize.toPx(),
                                                        ClipOp.Difference
                                                    ) {
                                                        this@onDrawWithContent.drawContent()
                                                    }
                                                }
                                            }
                                            .border(panel.BORDER_WIDTH, panel.BORDER, panel.shape)
                                        )

                                        Text(
                                            label,
                                            Modifier.padding(start = START_NAME),
                                            style = textName
                                        )
                                    }
                                }
                            }

                        }
                    }
                }
            }
        ))
    }
}

@Composable
fun MyTextFieldForCompexOpis(
    value: MutableState<TextFieldValue>,
    hint: String = "",
    modifier: Modifier = Modifier,
    textColor: Color,
    fontSize: TextUnit,
    cursive: Boolean,
    thin: Int,
    onValueChange: (TextFieldValue) -> Unit = { value.value = it },
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorPosition: MutableState<Float>,
    parentScrollbarLC: LayoutCoordinates?,
    style: MyTextFieldStyleState,
    fill: Boolean = true
) {

    val topCoor = remember { mutableStateOf(0f) }
    val heightCoor = remember { mutableStateOf(10f) }
    var lineEndCur by remember { mutableStateOf(1) }
    var lineAll by remember { mutableStateOf(1) }

    val interactionSourceHover: MutableInteractionSource = remember { MutableInteractionSource() }
    val focus by interactionSource.collectIsFocusedAsState()
    val hover by interactionSourceHover.collectIsHoveredAsState()
    with(LocalDensity.current) {
        with(style) {
            @OptIn(ExperimentalMaterialApi::class)
            (BasicTextField(
                value = value.value,
                modifier = modifier
                    .defaultMinSize(
                        minWidth = 100.dp,
                    ),
                onValueChange = {
                    lineEndCur = it.text.subSequence(0, it.selection.end).lines().count()
                    lineAll = it.text.lines().count()
                    cursorPosition.value = topCoor.value + heightCoor.value / lineAll * lineEndCur
                    onValueChange(it)
                },
                enabled = true,
                readOnly = false,
                textStyle = textMain.copy(
                    color = textColor,
                    fontSize = fontSize,
                    fontStyle = if (cursive) FontStyle.Italic else FontStyle.Normal,
                    fontWeight = FontWeight(if (thin == 1) 600 else 400)

                ),
                cursorBrush = SolidColor(Color.White),
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                interactionSource = interactionSource,
                singleLine = singleLine,
                maxLines = maxLines,
                decorationBox = @Composable { innerTextField ->
                    with(LocalDensity.current) {
                        (if (focus || hover) textNamePoleFocus else textNamePole).let { textName ->
                            (if (focus) panelFocus else panelUnfocus).let { panel ->
                                MyShadowBox(panel.shadow) {
                                    Row(
                                        Modifier

                                            .hoverable(interactionSourceHover)
                                            .background(
                                                panel.BACKGROUND,
                                                panel.shape
                                            )
                                            .border(panel.BORDER_WIDTH, panel.BORDER, panel.shape)
                                            .focusable(true, interactionSource)
                                            .then(inner_padding)

                                    ) {
                                        Box(
                                            Modifier
                                                .run {
                                                    if (fill) this.weight(1f) else this
                                                }
                                                .onGloballyPositioned { layCoor ->
                                                    parentScrollbarLC?.localBoundingBoxOf(layCoor)?.let {
                                                        topCoor.value = it.top
                                                        heightCoor.value = layCoor.boundsInParent().height
                                                        cursorPosition.value =
                                                            topCoor.value + heightCoor.value / lineAll * lineEndCur
                                                    }
                                                },
                                        ) {
                                            Box {
                                                if (value.value.text.isEmpty()) {
                                                    Text(
                                                        hint,
                                                        style = textHint.copy(fontSize = fontSize)
                                                    )
                                                }
                                                innerTextField()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            ))
        }
    }
}
