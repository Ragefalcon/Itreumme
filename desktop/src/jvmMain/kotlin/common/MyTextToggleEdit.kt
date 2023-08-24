package common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.mouseClickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import extensions.RowVA
import extensions.toColor
import extensions.toMyColorARGB
import ru.ragefalcon.sharedcode.extensions.MyColorARGB

@Composable
fun MyTextToggleEdit(
    label: String = "",
    text: String,
    modifier: Modifier = Modifier,
    dropMenu: @Composable ColumnScope.(MutableState<Boolean>) -> Unit = { },
    paramOutline: TextStyle = MyTextStyleParam.style2,
    rezEdit: (String) -> Unit
) =
    MyTextToggleEditCommon(
        comLabel = { lab ->
            if (lab != "") Text(
                modifier = Modifier.padding(0.dp),
                text = lab,
                style = TextStyle(
                    color = Color.Cyan.toMyColorARGB().plusDark().plusDark().toColor().copy(alpha = 0.7f)
                ),
                fontSize = 12.sp
            )
        },
        comText = { txt ->
            Text(txt, Modifier,  style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start))
        },
        label,
        text,
        modifier.border(1.dp, MyColorARGB.colorSchetItem0.toColor(), RoundedCornerShape(10.dp)),
        dropMenu,
        paramOutline,
        rezEdit
    )


@Composable
fun MyTextToggleEdit2(
    label: String = "",
    text: String,
    modifier: Modifier = Modifier,
    dropMenu: @Composable ColumnScope.(MutableState<Boolean>) -> Unit = { },
    paramOutline: TextStyle = MyTextStyleParam.style2,
    rezEdit: (String) -> Unit
) =
    MyTextToggleEditCommon(
        comLabel = { lab ->
            if (lab != "") Text(lab, Modifier,  style = MyTextStyleParam.style2.copy(color = MyColorARGB.colorSchetItemText.toColor()))
        },
        comText = { txt ->
            Text(txt, Modifier,  style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start))
        }, label, text, modifier, dropMenu, paramOutline, rezEdit
    )

@Composable
fun MyTextToggleEdit3(
    text: String,
    modifier: Modifier = Modifier,
    paramOutline: TextStyle = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start),
    dropMenu: @Composable ColumnScope.(MutableState<Boolean>) -> Unit = { },
    rezEdit: (String) -> Unit
) =
    MyTextToggleEditCommon(
        comLabel = { lab ->
            if (lab != "") Text(lab, Modifier, style = MyTextStyleParam.style2.copy(color = MyColorARGB.colorSchetItemText.toColor()))
        },
        comText = { txt ->
            Text(txt, Modifier,  style = paramOutline)
        }, "", text, modifier, dropMenu, paramOutline, rezEdit
    )

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun MyTextToggleEditCommon(
    comLabel: @Composable (String) -> Unit,
    comText: @Composable (String) -> Unit,
    label: String = "",
    text: String,
    modifier: Modifier = Modifier,
    dropMenu: @Composable ColumnScope.(MutableState<Boolean>) -> Unit = { },
    paramOutline: TextStyle = MyTextStyleParam.style2,
    rezEdit: (String) -> Unit
) {
    var edit by remember { mutableStateOf(false) }

    val editText = remember { mutableStateOf(TextFieldValue(text)) }

    RowVA(
        modifier//.combinedClickable(onClick = {}, onDoubleClick = { edit = true })
    ) {
        if (edit) {
            MyOutlinedTextField(
                label,
                editText,
                Modifier.padding(bottom = 5.dp).weight(1f),
                textStyle = paramOutline
            )
            MyTextButtStyle1("Ok", Modifier.padding(start = 8.dp), fontSize = 17.sp) {
                edit = false
                rezEdit(editText.value.text)
            }
        } else {
            DropMenuRightClickArea(dropMenu = dropMenu, onDoubleClick = { edit = true }) {
                RowVA {
                    Column(
                        Modifier.padding(5.dp).weight(1f)
                    ) {

                        comLabel(label)
                        comText(text)
                    }
//                    Spacer(Modifier.weight(1f))
                    MyTextStyle1(
                        "\uD83D\uDD6E",
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
//                            .padding(end = 20.dp)
                            .mouseClickable {
                                edit = true
                            })
                }
            }

        }
    }
}

