package common

import MyDialog.MyDialogLayout
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import extensions.ComplexOpisStyleState
import extensions.MyTextFieldStyleState
import extensions.RowVA
import extensions.getStartListComplexOpis
import ru.ragefalcon.sharedcode.models.data.ItemComplexOpis
import ru.ragefalcon.sharedcode.models.data.ItemComplexOpisText
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TableNameForComplexOpis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeOpisBlock
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.CommonInterfaceSetting
import viewmodel.MainDB
import java.util.*

class MyComplexOpisWithNameBox (
    private val labelName: String,
    private val text_name: MutableState<TextFieldValue>,
    private val label: String,
    private val item_id: Long,
    private val tableName: TableNameForComplexOpis,
    private val style: CommonInterfaceSetting.MySettings.ComplexOpisStyleSetting,
    listOpisIn: SnapshotStateList<ItemComplexOpis>?,
    private val styleName: CommonInterfaceSetting.MySettings.MyTextFieldStyle? = null,
    private val leftNameComposable: @Composable ()->Unit = {},
    private val rightNameComposable: @Composable ()->Unit = {},
) {
    val listOpis: SnapshotStateList<ItemComplexOpis> = listOpisIn ?: getStartListComplexOpis(tableName, item_id)
    private val reqFocus = FocusRequester()
    private val timeKeyEvent: MutableState<Long> = mutableStateOf(0L)
    private val complexOpis =
        MyComplexOpisEditBox(
            label,
            item_id,
            tableName,
            listOpis,
            reqFocus,
            timeKeyEvent
        )

    fun loadNewListOpis(newList: List<ItemComplexOpis>){
        complexOpis.loadNewListOpis(newList)
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun show(scope: ColumnScope, dialLay: MyDialogLayout) {
        scope.apply {
            RowVA(horizontalArrangement = Arrangement.Center){
                leftNameComposable()
                MyTextField(value =  text_name,
                    label = labelName,
                    modifier = Modifier
                        .focusRequester(reqFocus)
                        .onKeyEvent {

                            when (it.key.keyCode) {
                                Key.PageDown.keyCode -> {
                                    complexOpis.setFocusFirst()
                                    true
                                }

                                Key.PageUp.keyCode -> {
                                    complexOpis.setFocusLast()
                                    true
                                }

                                Key.Tab.keyCode -> {
                                    if (it.isCtrlPressed) {
                                        val time = Date().time
                                        if (time - timeKeyEvent.value > 200) {
                                            timeKeyEvent.value = time
                                            complexOpis.setFocus()
                                        }
                                    }
                                    true
                                }

                                else -> false
                            }
                        }, style = MyTextFieldStyleState(styleName ?: MainDB.styleParam.commonParam.commonTextField),
//                    maxLines = 1
                )
                rightNameComposable()
            }
            Box(Modifier.padding(horizontal = 20.dp, vertical = 5.dp).weight(1f)) {
                complexOpis.show(
                    Modifier,
                    dialLay,
                    ComplexOpisStyleState(style)
                )
            }
        }
    }

}