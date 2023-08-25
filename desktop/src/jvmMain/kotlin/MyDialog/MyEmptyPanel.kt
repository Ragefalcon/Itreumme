package MyDialog


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.BackgroungPanelStyle1
import common.MyTextButtStyle1

fun MyEmptyPanel(
    dialPan: MyDialogLayout,
    showHideButton: Boolean = true,
    labelButton: String = "Скрыть",
    listener: () -> Unit = {},
    body: @Composable ColumnScope.(MyDialogLayout, () -> Unit) -> Unit
) {
    val dialLayInner = MyDialogLayout()

    dialPan.dial = @Composable {
        BackgroungPanelStyle1 {

            Column(Modifier.padding(15.dp).animateContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
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
