package MainTabs.Setting

import MyDialog.MyDialogLayout
import MyShowMessage
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import common.MyTextButtStyle1
import extensions.BoxWithVScrollBar
import extensions.MyRectF
import extensions.getAbsolutRect
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.InnerDialogs.InnerStartTriggerEnum
import tests.TestComposeCount
import viewmodel.MainDB
import viewmodel.StateVM


class SettHelpTab(val dialLay: MyDialogLayout) {

    val testComposeCount = TestComposeCount()

    @Composable
    fun show() {
        Row {
            Column(
                Modifier
                    .weight(1f)

            ) {
                MyTextButtStyle1("DirHome", modifier = Modifier.padding(start = 15.dp)) {
                    MyShowMessage(dialLay, StateVM.dirMain)
                }
                MyTextButtStyle1("testN", modifier = Modifier.padding(start = 15.dp)) {
                    MyShowMessage(
                        dialLay, "testMessage", hole = MyRectF(
                            StateVM.tmpVajnLayCoor?.getAbsolutRect() ?: Rect(
                                Offset(50f, 50f),
                                Size(100f, 100f)
                            )
                        )
                    )
                }
                MyTextButtStyle1("Стартовый диалог", modifier = Modifier.padding(start = 15.dp)) {
                    MainDB.addTime.startInnerTrigger(InnerStartTriggerEnum.StartTrigger)
                }

                MyTextButtStyle1("Очистить ReplicateRecord", modifier = Modifier.padding(start = 15.dp)) {
                    MainDB.sincFun.cleanReplicateRecord()
                }

                MyTextButtStyle1("Помощь", modifier = Modifier.padding(start = 15.dp)) {
                    MainDB.addTime.startInnerTrigger(InnerStartTriggerEnum.HelpOpis)
                }

                MyTextButtStyle1("Тестируемый диалог", modifier = Modifier.padding(start = 15.dp)) {
                    MainDB.addTime.startInnerTrigger(InnerStartTriggerEnum.TestTrigger)
                }

                MyTextButtStyle1("Редактор стилей", modifier = Modifier.padding(start = 15.dp)) {
                    StateVM.openEditStyle.value = true
                }
            }
            BoxWithVScrollBar(Modifier.weight(1f).alpha(0.5f)) {
                testComposeCount.getComposable()
            }
        }
    }
}
