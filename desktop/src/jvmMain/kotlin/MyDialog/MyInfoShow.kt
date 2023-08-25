package MyDialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.BackgroungPanelStyle1
import common.MyTextButtStyle1

fun MyInfoShow(
    dialPan: MyDialogLayout,
    labelButton: String = "Ok",
    listener: () -> Unit = {},
    body: @Composable ColumnScope.() -> Unit
) {
    dialPan.dial = @Composable {
        BackgroungPanelStyle1 {

            Column(
                Modifier.padding(15.dp).widthIn(0.dp, this.maxWidth * 0.6f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                body()
                MyTextButtStyle1(labelButton, Modifier.padding(top = 10.dp)) {
                    listener()
                    dialPan.close()
                }
            }
        }
    }
    dialPan.show()
}
