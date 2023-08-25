package MyDialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.BackgroungPanelStyle1
import common.MyTextButtStyle1
import extensions.getValue
import viewmodel.MainDB

fun MyFullScreenPanel(
    dialPan: MyDialogLayout,
    labelButton: String = "Скрыть",
    showHideButton: Boolean = true,
    listener: () -> Unit = {},
    body: @Composable ColumnScope.(MyDialogLayout, () -> Unit) -> Unit
) {
    val dialLayInner = MyDialogLayout()

    dialPan.dial = @Composable {
        BackgroungPanelStyle1(shapePanel = MainDB.styleParam.appBarStyle.shape_window.getValue()) {

            Column(Modifier.fillMaxSize().padding(30.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                body(dialLayInner) {
                    listener()
                    dialPan.close()
                }
                if (showHideButton) MyTextButtStyle1(labelButton, Modifier.padding(top = 10.dp)) {
                    listener()
                    dialPan.close()
                }
            }
        }
        dialLayInner.getLay()
    }
    dialPan.show()
}
