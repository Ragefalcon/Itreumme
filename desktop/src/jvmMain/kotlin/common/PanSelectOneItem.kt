package common

import MyDialog.MyDialogLayout
import MyList
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import extensions.SimplePlateStyleState
import extensions.SimplePlateWithShadowStyleState
import extensions.TextButtonStyleState
import extensions.withSimplePlate
import ru.ragefalcon.sharedcode.models.data.Id_class
import ru.ragefalcon.sharedcode.source.disk.ItrCommObserveObj
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.CommonInterfaceSetting
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.StyleVMspis
import viewmodel.MainDB

fun <T : Id_class> PanSelectOneItem(
    dialPan: MyDialogLayout,
    spisok: ItrCommObserveObj<List<T>>,
    listener: (T) -> Unit,
    cancelListener: () -> Unit = {},
    stylePanel: StyleVMspis.InterfaceState.PanMessage = MainDB.styleParam.commonParam.commonPanel,
    stylePanelItem: CommonInterfaceSetting.MySettings.SimplePlateWithShadowStyleItemSetting,
    comItem: @Composable (T, SingleSelectionType<T>, MyDialogLayout, (T) -> Unit) -> Unit
) {
    val dialLayInner = MyDialogLayout()
    val selection = SingleSelectionType<T>()
    val loadPovtor = mutableStateOf(true)
    val loadTime = mutableStateOf(true)

    dialPan.dial = @Composable {
        BackgroungPanelStyle1(
            vignette = stylePanel.VIGNETTE.getValue(),
            style = SimplePlateStyleState(stylePanel.platePanel)
        ) {
            Column(
                Modifier
                    .heightIn(0.dp, dialPan.layHeight.value * 0.7F)
                    .widthIn(0.dp, dialPan.layWidth.value * 0.7F)
                    .padding(15.dp)
            ) {

                Column(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .weight(1f)
                        .withSimplePlate(SimplePlateWithShadowStyleState(stylePanelItem))
                        .padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MyList(
                        stateObj = spisok,
                        modifier = Modifier.weight(1f),
                        darkScroll = true
                    ) { ind, itemShablonDenPlan ->
                        comItem(itemShablonDenPlan, selection, dialLayInner) {
                            dialPan.close()
                            listener(it)
                        }
                    }
                }
                Row {
                    Spacer(Modifier.weight(1F))
                    MyTextButtStyle1("Отмена", myStyleTextButton = TextButtonStyleState(stylePanel.butt)) {
                        cancelListener()
                        dialPan.close()
                    }
                    Spacer(Modifier.weight(1F))
                    selection.selected?.let { itemSelect ->
                        MyTextButtStyle1("Выбрать", myStyleTextButton = TextButtonStyleState(stylePanel.butt)) {
                            dialPan.close()
                            listener(itemSelect)
                        }
                        Spacer(Modifier.weight(1F))
                    }
                }
            }
        }
        dialLayInner.getLay()
    }

    dialPan.show()
}


