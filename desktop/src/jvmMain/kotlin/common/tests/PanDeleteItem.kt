package common.tests

import MyDialog.MyDialogLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.BackgroungPanelStyle1
import common.MyDeleteButton
import common.MyTextButtStyle1
import common.MyTextStyleParam
import extensions.RowVA


fun PanDeleteItem(
    dialPan: MyDialogLayout,
    item: @Composable () -> Unit = {},
    cancelListener: () -> Unit = {},
    finishListener: () -> Unit = {}
) {
    dialPan.dial = @Composable {
        BackgroungPanelStyle1 {
            Column(Modifier.padding(15.dp).fillMaxWidth(0.6f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Удалить", Modifier.padding(bottom = 10.dp), style = MyTextStyleParam.style1)
                item()
                RowVA(Modifier.padding(top = 10.dp)) {
                    MyTextButtStyle1("Отмена", Modifier.padding(end = 15.dp)) {
                        cancelListener()
                        dialPan.close()
                    }
                    MyDeleteButton() {
                        finishListener()
                        dialPan.close()
                    }
                }

            }
        }
    }

    dialPan.show()
}
