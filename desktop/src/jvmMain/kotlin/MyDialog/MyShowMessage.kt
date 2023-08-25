import MyDialog.MyDialogLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import common.BackgroungPanelStyle1
import common.MyTextButtStyle1
import extensions.MyRectF
import extensions.SimplePlateStyleState
import extensions.TextButtonStyleState
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.StyleVMspis
import viewmodel.MainDB

fun MyShowMessage(
    dialPan: MyDialogLayout, message: String, answer: String = "Хорошо", hole: MyRectF? = null,
    stylePanel: StyleVMspis.InterfaceState.PanMessage = MainDB.styleParam.commonParam.commonPanel,
    rezFun: () -> Unit = {}
) {
    dialPan.dial = {

        BackgroungPanelStyle1(
            vignette = stylePanel.VIGNETTE.getValue(),
            style = SimplePlateStyleState(stylePanel.platePanel)
        ) {
            Column(Modifier.padding(15.dp).fillMaxWidth(0.6F), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(message, style = stylePanel.textName.getValue().copy(textAlign = TextAlign.Center))
                MyTextButtStyle1(
                    answer,
                    Modifier.padding(top = 5.dp),
                    myStyleTextButton = TextButtonStyleState(stylePanel.butt)
                ) {
                    rezFun()
                    dialPan.close()
                }
            }
        }
    }

    dialPan.show(hole)
}