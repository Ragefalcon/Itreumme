package MainTabs.Setting

import MyDialog.MyDialogLayout
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.MyCheckbox
import ru.ragefalcon.sharedcode.source.disk.getValue
import viewmodel.MainDB

class SettTimeTabs(val dialLay: MyDialogLayout) {
    @Composable
    fun show() {
        Row {
            Column(
                Modifier
                    .weight(1f)
            ) {
                Column(
                    Modifier.padding(5.dp)
                        .animateContentSize()
                ) {
                    MyCheckbox(
                        MainDB.interfaceSpis.ADD_DEN_PLAN_WITH_100_PERCENT.itrObj,
                        "100% еж. план",
                        Modifier.padding(end = 15.dp)
                    )
                    if (MainDB.interfaceSpis.ADD_DEN_PLAN_WITH_100_PERCENT.getValue()) {
                        MyCheckbox(
                            MainDB.interfaceSpis.ADD_DEN_PLAN_WITH_100_PERCENT_FROM_TIME.itrObj,
                            "В зависимости от времени",
                            Modifier.padding(start = 20.dp, end = 15.dp)
                        )
                    }
                    MyCheckbox(
                        MainDB.interfaceSpis.DefaultPercentForPlan.itrObj,
                        "Добавлять % для проектов и этапов по умолчанию",
                        Modifier.padding(top = 5.dp, end = 15.dp)
                    )
                    MyCheckbox(
                        MainDB.interfaceSpis.timeServiceParam.shablonCheckRepeat.itrObj,
                        MainDB.interfaceSpis.timeServiceParam.shablonCheckRepeat.nameSett,
                        Modifier.padding(top = 5.dp, end = 15.dp)
                    )
                    MyCheckbox(
                        MainDB.interfaceSpis.timeServiceParam.shablonCheckTime.itrObj,
                        MainDB.interfaceSpis.timeServiceParam.shablonCheckTime.nameSett,
                        Modifier.padding(top = 5.dp, end = 15.dp)
                    )
                    MyCheckbox(
                        MainDB.interfaceSpis.timeServiceParam.shablonCheckStapName.itrObj,
                        MainDB.interfaceSpis.timeServiceParam.shablonCheckStapName.nameSett,
                        Modifier.padding(top = 5.dp, end = 15.dp)
                    )
                    MyCheckbox(
                        MainDB.interfaceSpis.timeServiceParam.shablonCheckStapOpis.itrObj,
                        MainDB.interfaceSpis.timeServiceParam.shablonCheckStapOpis.nameSett,
                        Modifier.padding(top = 5.dp, end = 15.dp)
                    )
                }
            }
        }
    }
}