package MyDialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import common.*
import extensions.MyTextFieldStyleState
import extensions.SimplePlateStyleState
import extensions.TextButtonStyleState
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.StyleVMspis
import viewmodel.MainDB

fun MyOneVopros(
    dialPan: MyDialogLayout,
    vopros: String,
    labelButton: String,
    label: String = "",
    otvetDefault: String = "",
    cancelListener: () -> Unit = {},
    stylePanel: StyleVMspis.InterfaceState.PanMessage = MainDB.styleParam.commonParam.commonPanel,
    styleTextField: MyTextFieldStyleState = MyTextFieldStyleState(MainDB.styleParam.commonParam.commonTextField),
    listener: (String) -> Unit
) {

    dialPan.dial = @Composable {
        val textOtvet = remember { mutableStateOf(TextFieldValue(otvetDefault)) }
        BackgroungPanelStyle1(
            vignette = stylePanel.VIGNETTE.getValue(),
            style = SimplePlateStyleState(stylePanel.platePanel)
        ) {
            Column(Modifier.padding(15.dp).fillMaxWidth(0.6F), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(vopros, style = stylePanel.textName.getValue())
                MyTextField(
                    textOtvet,
                    Modifier.padding(top = 5.dp, bottom = 15.dp),
                    label = label,
                    style = styleTextField
                )
                Row {
                    MyTextButtStyle1("Отмена", myStyleTextButton = TextButtonStyleState(stylePanel.butt)) {
                        cancelListener()
                        dialPan.close()
                    }
                    MyTextButtStyle1(
                        labelButton,
                        Modifier.padding(start = 15.dp),
                        myStyleTextButton = TextButtonStyleState(stylePanel.butt)
                    ) {
                        listener(textOtvet.value.text)
                        dialPan.close()
                    }
                }
            }
        }
    }

    dialPan.show()
}

fun MyOneVoprosTransit(
    dialPan: MyDialogLayout,
    vopros: String,
    labelButton: String,
    label: String = "",
    otvetDefault: String = "",
    cancelListener: () -> Unit = {},
    listener: (String) -> Boolean
) {


    dialPan.dial = @Composable {
        val textOtvet = remember { mutableStateOf(TextFieldValue(otvetDefault)) }
        BackgroungPanelStyle1 {
            Column(Modifier.padding(15.dp).fillMaxWidth(0.6F), horizontalAlignment = Alignment.CenterHorizontally) {
                MyTextStyle1(vopros)
                MyOutlinedTextField(label, textOtvet)
                Row {
                    MyTextButtStyle1("Отмена") {
                        cancelListener()
                        dialPan.close()
                    }
                    MyTextButtStyle1(labelButton, Modifier.padding(start = 5.dp)) {
                        dialPan.close()
                        if (listener(textOtvet.value.text)) dialPan.show()
                    }
                }
            }
        }
    }

    dialPan.show()
}
