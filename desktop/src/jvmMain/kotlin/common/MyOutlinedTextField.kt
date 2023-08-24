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
//                letterSpacing = 4.sp,
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


/*

class AmountOrMessageVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {

        val originalText = text.text
        val formattedText = formatAmountOrMessage(text.text)

        val offsetMapping = object : OffsetMapping {

            override fun originalToTransformed(offset: Int): Int {
                if (originalText.isValidFormattableAmount) {
                    val commas = formattedText.count { it == ',' }
                    return when {
                        offset <= 1 -> offset
                        offset <= 3 -> if (commas >= 1) offset + 1 else offset
                        offset <= 5 -> if (commas == 2) offset + 2 else offset + 1
                        else -> 8
                    }
                }
                return offset
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (originalText.isValidFormattableAmount) {
                    val commas = formattedText.count { it == ',' }
                    return when (offset) {
                        8, 7 -> offset - 2
                        6 -> if (commas == 1) 5 else 4
                        5 -> if (commas == 1) 4 else if (commas == 2) 3 else offset
                        4, 3 -> if (commas >= 1) offset - 1 else offset
                        2 -> if (commas == 2) 1 else offset
                        else -> offset
                    }
                }
                return offset
            }
        }

        return TransformedText(
            text = AnnotatedString(formattedText),
            offsetMapping = offsetMapping
        )
    }
}
*/



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
//                letterSpacing = 4.sp,
        textAlign = textAlign,
        shadow = Shadow(
            color = Color.Black,
            offset = Offset(2f, 2f),
            blurRadius = 2f
        ),
    ),
    onValueChange: (Double) -> Unit = {}
) {
//    val textDouble = remember { mutableStateOf(TextFieldValue(textVal.value.toDouble().roundToString(2))) }
//    MyOutlinedTextFieldCommon(
    MyOutlinedTextFieldCommon(
        label,
        textDouble,
        modifier,
        textAlign,
        textStyle,
        placeholder = { Text("1.00") }, //, style = textStyle
    ) {
        if (it.text.toDoubleOrNull() != null) {
            if (it.selection.length == 0) {
                /** Проеверка на обновление по случаю выделения куска текста */
                /**
                 * Переменная bb для удаления нулей в начале текста
                 * **/
                var bb = it.text//.toDouble().toString()
//                println("0 $bb")
                while (bb[0] == '0') {
                    bb = bb.substring(1)
                }
//                println("1 $bb")
                if (bb[0] == '.') bb = "0$bb"

                /**
                 * after и ниже для добавления до двух знаков после запятой
                 * */
                var after = bb.substringAfter(".")
                val ll = after.length
//                println("2 $bb")
//                println(bb.substringAfter("."))
                if (ll != 2) {
                    /** Проверка на наличие точки как таковой, если длины одинаковые, то ее нет и нужно просто добвать 00 */
                    if (ll >= 2) after = bb.substringAfter(".").subSequence(0, 2).toString()
                    if (ll == 1) after = "${bb.substringAfter(".")}0"
                }
                val rez = "${bb.substringBefore(".")}.${after}"
//                println("rez $rez")
                var k = 0
                if ((textDouble.value.text == rez) && (bb != it.text)) k = 1
                /** Если был добавлен 0 в начало, то курсор нужно вернуть на место, а если это было просто перемещение курсора, то не нужно  **/
                val aa = it.copy(
                    text = rez,
                    selection = TextRange(it.selection.start - k, it.selection.end - k),
                    composition = TextRange((it.composition?.start ?: 1) - k, (it.composition?.end ?: 1) - k)
                )
                /** Если текст не нужно менять, а курсор нужно вернуть в исходное положение, то для применения изменений
                 * нужно вначале внести изменения, а потом откатить, иначе где то внутри it сохраняется измененный текст
                 * и повторные неверные вводы приводят к неправильным проверкам
                 * */
                if (textDouble.value.text == aa.text) textDouble.value = it
                textDouble.value = aa
//                println("aa $aa")
//                println("aa.text ${aa.text}")
                onValueChange(aa.text.toDouble())
//                textDouble.value = it
//                println("it $it")
//                println("it.text ${it.text}")
            } else {
//                println("it.selection.length != 0")
                textDouble.value = it
//                textVal.value = it.text.toDouble()
                onValueChange(it.text.toDouble())
            }
        } else {
//            println("it.text.toDoubleOrNull() == null")
            if (it.text != "") {
                /**
                 * Если текст не нужно менять, а курсор нужно вернуть в исходное положение
                 * ("TextRange(it.selection.start - 1, it.selection.end - 1)"), то для применения изменений
                 * нужно вначале внести изменения ("textDouble.value = it"), а потом откатить, иначе где то внутри it сохраняется измененный текст
                 * и повторные неверные вводы приводят к неправильным проверкам
                 * */
                val aa = it.copy(
                    text = textDouble.value.text,
                    selection = TextRange(it.selection.start - 1, it.selection.end - 1),
                    composition = TextRange((it.composition?.start ?: 1) - 1, (it.composition?.end ?: 1) - 1)
                )
//            println("aa: ${aa.selection}")
//            println("aa start: ${aa.text}")
//            println("it start: ${it.text}")
//            println("it start: ${it.selection.start}")
//            println("it end: ${it.selection.end}")
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
//                letterSpacing = 4.sp,
        textAlign = textAlign,
        shadow = Shadow(
            color = Color.Black,
            offset = Offset(2f, 2f),
            blurRadius = 2f
        ),
    ),
    onValueChange: (Int) -> Unit = {}
) {
//    val textInt = remember { mutableStateOf(TextFieldValue(textVal.value.toString())) }
//    textVal.observe {
//        textInt.value = TextFieldValue(textVal.value.toString())
//    }
    MyOutlinedTextFieldCommon(
        label,
        textInt,
        modifier,
        textAlign,
        textStyle
    ) {
        if (it.text.toIntOrNull() != null) {
            /** Проеверка на обновление по случаю выделения куска текста */
            if (it.selection.length == 0) {
                /**
                 * Переменная bb для удаления нулей в начале текста
                 * **/
                var bb = it.text//.toDouble().toString()
                while (bb[0] == '0' && bb.length > 1) {
                    bb = bb.substring(1)
                }
                var k = 0
                if ((textInt.value.text == bb) && (bb != it.text)) k = 1
                /** Если был добавлен 0 в начало, то курсор нужно вернуть на место, а если это было просто перемещение курсора, то не нужно  **/
                val aa = it.copy(
                    text = bb,
                    selection = TextRange(it.selection.start - k, it.selection.end - k),
                    composition = TextRange(it.selection.start - k, it.selection.end - k)
                )
                /** Если текст не нужно менять, а курсор нужно вернуть в исходное положение, то для применения изменений
                 * нужно вначале внести изменения, а потом откатить, иначе где то внутри it сохраняется измененный текст
                 * и повторные неверные вводы приводят к неправильным проверкам
                 * */
                if (textInt.value.text == aa.text) textInt.value = it
                textInt.value = aa
//                textVal.value = aa.text.toInt()
                onValueChange(aa.text.toInt())
            } else {
                textInt.value = it
//                textVal.value = it.text.toInt()
                onValueChange(it.text.toInt())
            }
        } else {

            /**
             * Если текст не нужно менять, а курсор нужно вернуть в исходное положение
             * ("TextRange(it.selection.start - 1, it.selection.end - 1)"), то для применения изменений
             * нужно вначале внести изменения ("textDouble.value = it"), а потом откатить, иначе где то внутри it сохраняется измененный текст
             * и повторные неверные вводы приводят к неправильным проверкам
             * */
            val aa = it.copy(
                text = textInt.value.text,
                selection = TextRange(it.selection.start - 1, it.selection.end - 1),
                composition = TextRange(it.selection.start - 1, it.selection.end - 1)
            )
//            println("aa: ${aa.selection}")
//            println("aa start: ${aa.text}")
//            println("it start: ${it.text}")
//            println("it start: ${it.selection.start}")
//            println("it end: ${it.selection.end}")
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
//                letterSpacing = 4.sp,
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
            primary = Color(0xFFFFFFFF), //Color(0xFF9eba85),//99851F),
            primaryVariant = Color(0xFFFF0000),//Color(0xFF9eba85),
            secondary = Color(0xFFFF0000),//Color(0xFF9eba85),
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
//                        disabledIndicatorColor = Color.Yellow,
//                        disabledTextColor = Color.Green,
                focusedIndicatorColor = Color(0xFF9eba85),
//                        unfocusedIndicatorColor = Color.Black,
                cursorColor = Color(0xFF9eba85),
                focusedLabelColor = Color(0xFF9eba85)
            ),
            visualTransformation = VisualTransformation.None,
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, autoCorrect = true),
//            keyboardActions = KeyboardActions()

        )
    }
}


@Preview
@Composable
fun PreviewMyOutlinedTextField() {
    val text_name = remember { mutableStateOf(TextFieldValue("NameAAA")) }
    MyOutlinedTextField("Nazvanie", text_name) {}
}

class MaskTransformation() : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return maskFilter(text)
    }
}


fun maskFilter(text: AnnotatedString): TransformedText {

    // NNNNN-NNN
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