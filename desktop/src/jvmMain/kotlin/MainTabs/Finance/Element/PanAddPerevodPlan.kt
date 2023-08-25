package MainTabs.Finance.Element


import MyDialog.MyDialogLayout
import MyDialog.buttDatePickerWithButton
import adapters.MyComboBox
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemCommonFinOper
import ru.ragefalcon.sharedcode.models.data.ItemSchetPlan
import ru.ragefalcon.sharedcode.source.disk.getValue
import viewmodel.MainDB

fun PanAddPerevodPlan(
    dialPan: MyDialogLayout,
    item: ItemCommonFinOper? = null,
    schetId: String? = null
) {
    val dialLayInner = MyDialogLayout()
    val dateInner = mutableStateOf(MainDB.dateFin.value)

    val listKrome: MutableState<List<ItemSchetPlan>?> = mutableStateOf(listOf<ItemSchetPlan>())

    val CB_spisSchetOtprav = MyComboBox(MainDB.finSpis.spisSchetPlan, nameItem = { it.name }) { schetOtprav ->
        listKrome.value =
            MainDB.finSpis.spisSchetPlan.getState().value?.filter { it.id != schetOtprav.id } ?: listOf<ItemSchetPlan>()
    }.apply {
        item?.let {
            if (item.summa > 0) MainDB.finSpis.spisSchetPlan.getState().value?.find { it.id == item.schetid }?.let {
                select(it)
            } else schetId?.let {
                MainDB.finSpis.spisSchetPlan.getState().value?.find { it.id == schetId }?.let {
                    select(it)
                }
            }
        } ?: run {
            schetId?.let {
                MainDB.finSpis.spisSchetPlan.getState().value?.find { it.id == schetId }?.let {
                    select(it)
                }
            }
        }
    }
    val CB_spisSchetPoluch = MyComboBox(listKrome, nameItem = { it.name }).apply {
        item?.let {
            if (item.summa < 0) MainDB.finSpis.spisSchetPlan.getState().value?.find { it.id == item.schetid }?.let {
                select(it)
            } else schetId?.let {
                MainDB.finSpis.spisSchetPlan.getState().value?.find { it.id == schetId }?.let {
                    select(it)
                }
            }
        }
    }


    dialPan.dial = @Composable {
        val text_name = remember { mutableStateOf(TextFieldValue(item?.name ?: "Перевод")) }
        val summa = remember {
            mutableStateOf(TextFieldValue(item?.let {
                (if (it.summa < 0) (-it.summa) else it.summa).roundToString(2)
            } ?: ""))
        }

        with(MainDB.styleParam.finParam.schetParam.panAddPerevodPlan) {
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
                        label = "Коментарий к переводу",
                        hint = "Введите коментарий",
                        style = MyTextFieldStyleState(textName)
                    )
                    MyTextFieldDouble(
                        summa,
                        Modifier.padding(5.dp),
                        width = 250.dp,
                        textAlign = TextAlign.Center,
                        label = "Сумма перевода",
                        hint = "Введите сумму",
                        style = MyTextFieldStyleState(textSumm)
                    )
                    RowVA(Modifier.padding(vertical = 5.dp)) {
                        Text(" << ", style = textArrowOut.getValue())
                        CB_spisSchetOtprav.show(style = ComboBoxStyleState(MainDB.styleParam.finParam.schetParam.cb_spisSchetPlan))
                        Text(" >> ", style = textArrowOut.getValue())
                    }
                    buttDatePickerWithButton(
                        dialLayInner, dateInner, Modifier.padding(5.dp),
                        myStyleTextDate = TextButtonStyleState(MainDB.styleParam.finParam.buttDate),
                        myStyleTextArrow = TextButtonStyleState(MainDB.styleParam.finParam.buttNextDate),
                    )
                    RowVA(Modifier.padding(vertical = 5.dp)) {
                        Text(" >> ", style = textArrowIn.getValue())
                        CB_spisSchetPoluch.show(style = ComboBoxStyleState(MainDB.styleParam.finParam.schetParam.cb_spisSchetPlan))
                        Text(" << ", style = textArrowIn.getValue())
                    }
                    Row(Modifier.padding(5.dp)) {
                        MyTextButtStyle1("Отмена", myStyleTextButton = TextButtonStyleState(buttCancel)) {
                            dialPan.close()
                        }
                        (summa.value.text.toDoubleOrNull() ?: 0.0).let { summ ->
                            if (summ != 0.0) {
                                if (item != null && item.id != "-1") {
                                    MyTextButtStyle1(
                                        "Изменить", Modifier.padding(start = 5.dp),
                                        myStyleTextButton = TextButtonStyleState(buttAdd)
                                    ) {
                                        CB_spisSchetOtprav.getSelected()?.id?.toLong()?.let { schetOtprav ->
                                            CB_spisSchetPoluch.getSelected()?.id?.toLong()?.let { schetPoluch ->
                                                MainDB.addFinFun.updPerevodPlan(
                                                    item,
                                                    text_name.value.text,
                                                    schetOtprav,
                                                    summ,
                                                    schetPoluch,
                                                    dateInner.value.time
                                                )
                                                dialPan.close()
                                            }
                                        }
                                    }
                                } else {
                                    MyTextButtStyle1(
                                        "Добавить", Modifier.padding(start = 5.dp),
                                        myStyleTextButton = TextButtonStyleState(buttAdd)
                                    ) {
                                        CB_spisSchetOtprav.getSelected()?.id?.toLong()?.let { schetOtprav ->
                                            CB_spisSchetPoluch.getSelected()?.id?.toLong()?.let { schetPoluch ->
                                                MainDB.addFinFun.addPerevodPlan(
                                                    text_name.value.text,
                                                    schetOtprav,
                                                    summ,
                                                    schetPoluch,
                                                    dateInner.value.time
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
            }
        }
        dialLayInner.getLay()
    }
    dialPan.show()
}