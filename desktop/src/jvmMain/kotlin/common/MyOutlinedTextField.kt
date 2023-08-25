package common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp


@Composable
fun MyOutlinedTextField(
    label: String,
    textVal: MutableState<TextFieldValue>,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    textStyle: TextStyle = TextStyle(
        color = Color(0xFFFFF7D9),
        fontSize = 20.sp,
        fontFamily = FontFamily.SansSerif,
        textAlign = textAlign,
        shadow = Shadow(
            color = Color.Black,
            offset = Offset(2f, 2f),
            blurRadius = 2f
        ),
    ),
    onValueChange: (TextFieldValue) -> Unit = {}
) {
    MyOutlinedTextFieldCommon(
        label,
        textVal,
        modifier,
        textAlign,
        textStyle
    ) {
        textVal.value = it
        onValueChange(it)
    }
}


@Composable
fun MyOutlinedTextFieldDouble(
    label: String,
    textDouble: MutableState<TextFieldValue>,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    textStyle: TextStyle = TextStyle(
        color = Color(0xFFFFF7D9),
        fontSize = 20.sp,
        fontFamily = FontFamily.SansSerif,
        textAlign = textAlign,
        shadow = Shadow(
            color = Color.Black,
            offset = Offset(2f, 2f),
            blurRadius = 2f
        ),
    ),
    onValueChange: (Double) -> Unit = {}
) {


    MyOutlinedTextFieldCommon(
        label,
        textDouble,
        modifier,
        textAlign,
        textStyle,
        placeholder = { Text("1.00") },
    ) {
        if (it.text.toDoubleOrNull() != null) {
            if (it.selection.length == 0) {
                var bb = it.text

                while (bb[0] == '0') {
                    bb = bb.substring(1)
                }

                if (bb[0] == '.') bb = "0$bb"

                var after = bb.substringAfter(".")
                val ll = after.length


                if (ll != 2) {
                    if (ll >= 2) after = bb.substringAfter(".").subSequence(0, 2).toString()
                    if (ll == 1) after = "${bb.substringAfter(".")}0"
                }
                val rez = "${bb.substringBefore(".")}.${after}"

                var k = 0
                if ((textDouble.value.text == rez) && (bb != it.text)) k = 1
                val aa = it.copy(
                    text = rez,
                    selection = TextRange(it.selection.start - k, it.selection.end - k),
                    composition = TextRange((it.composition?.start ?: 1) - k, (it.composition?.end ?: 1) - k)
                )
                if (textDouble.value.text == aa.text) textDouble.value = it
                textDouble.value = aa
                onValueChange(aa.text.toDouble())
            } else {
                textDouble.value = it
                onValueChange(it.text.toDouble())
            }
        } else {

            if (it.text != "") {
                val aa = it.copy(
                    text = textDouble.value.text,
                    selection = TextRange(it.selection.start - 1, it.selection.end - 1),
                    composition = TextRange((it.composition?.start ?: 1) - 1, (it.composition?.end ?: 1) - 1)
                )
                textDouble.value = it
                textDouble.value = aa
            }
        }
    }
}

@Composable
fun MyOutlinedTextFieldInt(
    label: String,
    textInt: MutableState<TextFieldValue>,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    textStyle: TextStyle = TextStyle(
        color = Color(0xFFFFF7D9),
        fontSize = 20.sp,
        fontFamily = FontFamily.SansSerif,

        textAlign = textAlign,
        shadow = Shadow(
            color = Color.Black,
            offset = Offset(2f, 2f),
            blurRadius = 2f
        ),
    ),
    onValueChange: (Int) -> Unit = {}
) {


    MyOutlinedTextFieldCommon(
        label,
        textInt,
        modifier,
        textAlign,
        textStyle
    ) {
        if (it.text.toIntOrNull() != null) {
            if (it.selection.length == 0) {
                var bb = it.text
                while (bb[0] == '0' && bb.length > 1) {
                    bb = bb.substring(1)
                }
                var k = 0
                if ((textInt.value.text == bb) && (bb != it.text)) k = 1

                val aa = it.copy(
                    text = bb,
                    selection = TextRange(it.selection.start - k, it.selection.end - k),
                    composition = TextRange(it.selection.start - k, it.selection.end - k)
                )

                if (textInt.value.text == aa.text) textInt.value = it
                textInt.value = aa

                onValueChange(aa.text.toInt())
            } else {
                textInt.value = it

                onValueChange(it.text.toInt())
            }
        } else {
            val aa = it.copy(
                text = textInt.value.text,
                selection = TextRange(it.selection.start - 1, it.selection.end - 1),
                composition = TextRange(it.selection.start - 1, it.selection.end - 1)
            )

            textInt.value = it
            textInt.value = aa
        }
    }
}

@Composable
fun MyOutlinedTextFieldCommon(
    label: String,
    textVal: MutableState<TextFieldValue>,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    textStyle: TextStyle = TextStyle(
        color = Color(0xFFFFF7D9),
        fontSize = 20.sp,
        fontFamily = FontFamily.SansSerif,
        textAlign = textAlign,
        shadow = Shadow(
            color = Color.Black,
            offset = Offset(2f, 2f),
            blurRadius = 2f
        ),
    ),
    placeholder: @Composable (() -> Unit)? = null,
    onValueChange: (TextFieldValue) -> Unit = {}
) {
    MaterialTheme(
        colors = Colors(
            /** primary - цвет выделения текста**/
            primary = Color(0xFFFFFFFF),
            primaryVariant = Color(0xFFFF0000),
            secondary = Color(0xFFFF0000),
            secondaryVariant = Color(0xFF00FF00),
            background = Color(0xFF464D45),
            surface = Color(0xFFFF0000),
            error = Color(0xFFFF0000),
            onPrimary = Color(0xFF000000),
            onSecondary = Color(0xFFFF0000),
            onBackground = Color(0xFF0000FF),
            onSurface = Color(0xAF000000),
            onError = Color(0xFFFFFF00),
            isLight = false
        ),
    ) {
        OutlinedTextField(
            modifier = modifier,
            value = textVal.value,
            textStyle = textStyle,
            label = { androidx.compose.material.Text(label) },
            onValueChange = onValueChange,
            placeholder = placeholder,
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(0xCFFFF7D9),
                placeholderColor = Color.Cyan,
                unfocusedLabelColor = Color(0x7FFFF7D9),
                focusedIndicatorColor = Color(0xFF9eba85),
                cursorColor = Color(0xFF9eba85),
                focusedLabelColor = Color(0xFF9eba85)
            ),
            visualTransformation = VisualTransformation.None,
        )
    }
}


@Preview
@Composable
fun PreviewMyOutlinedTextField() {
    val text_name = remember { mutableStateOf(TextFieldValue("NameAAA")) }
    MyOutlinedTextField("Nazvanie", text_name) {}
}


fun maskFilter(text: AnnotatedString): TransformedText {
    val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
    var out = ""
    for (i in trimmed.indices) {
        out += trimmed[i]
        if (i == 4) out += "-"
    }

    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 4) return offset
            if (offset <= 8) return offset + 1
            return 9

        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 5) return offset
            if (offset <= 9) return offset - 1
            return 8
        }
    }

    return TransformedText(AnnotatedString(out), numberOffsetTranslator)
}