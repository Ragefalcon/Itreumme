package MainTabs.Finance.Element


import MyDialog.MyDialogLayout
import adapters.MyComboBox
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemShabDoxod
import ru.ragefalcon.sharedcode.source.disk.getValue
import viewmodel.MainDB
import java.util.*

fun PanChangeShabDoxod(
    dialPan: MyDialogLayout,
    itemShabDoxod: ItemShabDoxod
) {
    val dialLayInner = MyDialogLayout()
    val CB_spisSchet = MyComboBox(MainDB.finSpis.spisSchet, nameItem = { it.name }).apply {
        MainDB.finSpis.spisSchet.getState().value?.find { it.id == itemShabDoxod.schet_id.toString() }?.let {
            select(it)
        }
    }
    val CB_spisTypeDox = MyComboBox(MainDB.finSpis.spisTypeDox, nameItem = { it.second }).apply {
        MainDB.finSpis.spisTypeDox.getState().value?.find { it.second == itemShabDoxod.type }?.let {
            select(it)
        }
    }

    val text_name = mutableStateOf(TextFieldValue(itemShabDoxod.name))
    val text_name_oper = mutableStateOf(TextFieldValue(itemShabDoxod.nameoper))
    val summa = mutableStateOf(TextFieldValue(itemShabDoxod.summa.roundToString(2)))

    dialPan.dial = @Composable {
        with(MainDB.styleParam.finParam.doxodParam.panAddDoxod) {
            BackgroungPanelStyle1(
                vignette = VIGNETTE.getValue(),
                style = SimplePlateStyleState(platePanel)
            ) {
                Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    MyTextField(
                        text_name,
                        Modifier.padding(5.dp),
                        width = 300.dp,
                        textAlign = TextAlign.Center,
                        label = "Название шаблона",
                        hint = "Введите название",
                        style = MyTextFieldStyleState(textName)
                    )
                    MyTextField(
                        text_name_oper,
                        Modifier.padding(5.dp),
                        width = 300.dp,
                        textAlign = TextAlign.Center,
                        label = "Название дохода",
                        hint = "Введите название",
                        style = MyTextFieldStyleState(textName)
                    )
                    MyTextFieldDouble(
                        summa,
                        Modifier.padding(5.dp),
                        width = 250.dp,
                        textAlign = TextAlign.Center,
                        label = "Сумма дохода",
                        hint = "Введите сумму",
                        style = MyTextFieldStyleState(textSumm)
                    )
                    CB_spisTypeDox.show(
                        Modifier.padding(5.dp),
                        style = ComboBoxStyleState(MainDB.styleParam.finParam.doxodParam.cb_typeDox)
                    )
                    CB_spisSchet.show(
                        Modifier.padding(5.dp),
                        style = ComboBoxStyleState(MainDB.styleParam.finParam.schetParam.cb_spisSchet)
                    )
                    Row(Modifier.padding(5.dp)) {
                        MyTextButtStyle1("Отмена", myStyleTextButton = TextButtonStyleState(buttCancel)) {
                            dialPan.close()
                        }
                        (summa.value.text.toDoubleOrNull() ?: 0.0).let { summ ->
                            MyTextButtStyle1(
                                "Изменить", Modifier.padding(start = 5.dp),
                                myStyleTextButton = TextButtonStyleState(buttAdd)
                            ) {
                                CB_spisTypeDox.getSelected()?.second?.let { typed ->
                                    CB_spisSchet.getSelected()?.id?.toLong()?.let { schetid ->
                                        MainDB.addFinFun.updShabDoxod(
                                            itemShabDoxod,
                                            text_name.value.text,
                                            text_name_oper.value.text,
                                            summ,
                                            typed,
                                            schetid
                                        )
                                        dialPan.close()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        dialLayInner.getLay()
    }
    dialPan.show()
}