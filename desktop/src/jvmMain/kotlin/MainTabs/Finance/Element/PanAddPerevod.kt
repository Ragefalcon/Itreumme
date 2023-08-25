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
import ru.ragefalcon.sharedcode.models.data.ItemSchet
import ru.ragefalcon.sharedcode.source.disk.getValue
import viewmodel.MainDB
import java.util.*

fun PanAddPerevod(
    dialPan: MyDialogLayout,
    item: ItemCommonFinOper? = null,
    schetId: String? = null
) {
    val dialLayInner = MyDialogLayout()
    val dateInner = mutableStateOf(item?.data?.let { Date(it) } ?: MainDB.dateFin.value)
    val convert = mutableStateOf(false)
    val val1 = mutableStateOf(-1L)
    val val2 = mutableStateOf(-1L)
    val listKrome: MutableState<List<ItemSchet>?> = mutableStateOf(listOf<ItemSchet>())

    val CB_spisSchetOtprav = MyComboBox(MainDB.finSpis.spisSchet, nameItem = { it.name }) { schetOtprav ->
        listKrome.value =
            MainDB.finSpis.spisSchet.getState().value?.filter { it.id != schetOtprav.id } ?: listOf<ItemSchet>()
        val1.value = schetOtprav.id.toLong()
        convert.value = MainDB.finFun.sravnVal(val1.value, val2.value) != null
    }.apply {
        item?.let {
            if (item.summa > 0) MainDB.finSpis.spisSchet.getState().value?.find { it.id == item.schetid }?.let {
                select(it)
            } else schetId?.let {
                MainDB.finSpis.spisSchet.getState().value?.find { it.id == schetId }?.let {
                    select(it)
                }
            }
        } ?: run {
            schetId?.let {
                MainDB.finSpis.spisSchet.getState().value?.find { it.id == schetId }?.let {
                    select(it)
                }
            }
        }
    }
    val CB_spisSchetPoluch = MyComboBox(listKrome, nameItem = { it.name }) { schetPoluch ->
        val2.value = schetPoluch.id.toLong()
        convert.value = MainDB.finFun.sravnVal(val1.value, val2.value) != null
    }.apply {
        item?.let {
            if (item.summa < 0) MainDB.finSpis.spisSchet.getState().value?.find { it.id == item.schetid }?.let {
                select(it)
            } else schetId?.let {
                MainDB.finSpis.spisSchet.getState().value?.find { it.id == schetId }?.let {
                    select(it)
                }
            }
        }
    }
    dialPan.dial = @Composable {
        val text_name = remember { mutableStateOf(TextFieldValue(item?.name ?: "Перевод")) }
        val summa = remember {
            mutableStateOf(TextFieldValue(item?.let {
                if (it.summa < 0) (-it.summa).roundToString(2) else (-it.summa2).roundToString(2)
            } ?: ""))
        }
        val summa_zach = remember {
            mutableStateOf(TextFieldValue(item?.let {
                if (it.summa > 0) it.summa.roundToString(2) else it.summa2.roundToString(2)
            } ?: ""))
        }
        with(MainDB.styleParam.finParam.schetParam.panAddPerevod) {
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
                        CB_spisSchetOtprav.show(style = ComboBoxStyleState(MainDB.styleParam.finParam.schetParam.cb_spisSchet))
                        Text(" >> ", style = textArrowOut.getValue())
                    }
                    buttDatePickerWithButton(
                        dialLayInner, dateInner, Modifier.padding(5.dp),
                        myStyleTextDate = TextButtonStyleState(MainDB.styleParam.finParam.buttDate),
                        myStyleTextArrow = TextButtonStyleState(MainDB.styleParam.finParam.buttNextDate),
                    )
                    RowVA(Modifier.padding(vertical = 5.dp)) {
                        Text(" >> ", style = textArrowIn.getValue())
                        CB_spisSchetPoluch.show(style = ComboBoxStyleState(MainDB.styleParam.finParam.schetParam.cb_spisSchet))
                        Text(" << ", style = textArrowIn.getValue())
                    }
                    if (convert.value) MyTextFieldDouble(
                        summa_zach,
                        Modifier.padding(5.dp),
                        width = 250.dp,
                        textAlign = TextAlign.Center,
                        label = "Сумма зачисления(${MainDB.finFun.sravnVal(CB_spisSchetPoluch.getSelected()?.id?.toLong() ?: 1)})",
                        hint = "Введите сумму",
                        style = MyTextFieldStyleState(textSummPerevod)
                    )
                    Row(Modifier.padding(5.dp)) {
                        MyTextButtStyle1("Отмена", myStyleTextButton = TextButtonStyleState(buttCancel)) {
                            dialPan.close()
                        }
                        (summa.value.text.toDoubleOrNull() ?: 0.0).let { summ ->
                            (summa_zach.value.text.toDoubleOrNull() ?: 0.0).let { summ_zach ->
                                if (summ != 0.0 && (!convert.value || summ_zach != 0.0)) {
                                    if (item != null && item.id != "-1") {
                                        MyTextButtStyle1(
                                            "Изменить", Modifier.padding(start = 5.dp),
                                            myStyleTextButton = TextButtonStyleState(buttAdd)
                                        ) {
                                            CB_spisSchetOtprav.getSelected()?.id?.toLong()?.let { schetOtprav ->
                                                CB_spisSchetPoluch.getSelected()?.id?.toLong()?.let { schetPoluch ->
                                                    MainDB.addFinFun.updPerevod(
                                                        item,
                                                        text_name.value.text,
                                                        schetOtprav,
                                                        summa.value.text.toDoubleOrNull() ?: 0.0,
                                                        schetPoluch,
                                                        if (!convert.value) summ else summ_zach,
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
                                                    MainDB.addFinFun.addPerevod(
                                                        text_name.value.text,
                                                        schetOtprav,
                                                        summa.value.text.toDoubleOrNull() ?: 0.0,
                                                        schetPoluch,
                                                        if (!convert.value) summ else summ_zach,
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
        }
        dialLayInner.getLay()
    }
    dialPan.show()
}