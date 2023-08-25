package MainTabs.Finance.Element


import MyDialog.MyDialogLayout
import adapters.MyComboBox
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
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
import extensions.ComboBoxStyleState
import extensions.MyTextFieldStyleState
import extensions.SimplePlateStyleState
import extensions.TextButtonStyleState
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemShabRasxod
import ru.ragefalcon.sharedcode.source.disk.getValue
import viewmodel.MainDB

fun PanChangeShabRasxod(
    dialPan: MyDialogLayout,
    itemShabRasxod: ItemShabRasxod
) {
    val dialLayInner = MyDialogLayout()
    val CB_spisSchet = MyComboBox(MainDB.finSpis.spisSchet, nameItem = { it.name }).apply {
        MainDB.finSpis.spisSchet.getState().value?.find { it.id == itemShabRasxod.schet_id.toString() }?.let {
            select(it)
        }
    }
    val CB_spisTypeRasx = MyComboBox(MainDB.finSpis.spisTypeRasx, nameItem = { it.second }).apply {
        MainDB.finSpis.spisTypeRasx.getState().value?.find { it.second == itemShabRasxod.type }?.let {
            select(it)
        }
    }

    val CB_spisSchPl = MyComboBox(MainDB.finSpis.spisSchetPlan, nameItem = { it.name }).apply {
        itemShabRasxod.schpl_id?.let { schpl_id ->
            MainDB.finSpis.spisSchetPlan.getState().value?.find { it.id.toLong() == schpl_id }?.let {
                select(it)
            }
        }
    }
    val text_name = mutableStateOf(TextFieldValue(itemShabRasxod.name))
    val text_name_oper = mutableStateOf(TextFieldValue(itemShabRasxod.nameoper))
    val summa = mutableStateOf(TextFieldValue(itemShabRasxod.summa.roundToString(2)))
    val checkPlans = mutableStateOf(itemShabRasxod.schpl_id?.let { true } ?: false)

    dialPan.dial = @Composable {
        with(MainDB.styleParam.finParam.rasxodParam.panAddRasxod) {
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
                        label = "Название расхода",
                        hint = "Введите название",
                        style = MyTextFieldStyleState(textName)
                    )
                    MyTextFieldDouble(
                        summa,
                        Modifier.padding(5.dp),
                        width = 250.dp,
                        textAlign = TextAlign.Center,
                        label = "Сумма расхода",
                        hint = "Введите сумму",
                        style = MyTextFieldStyleState(textSumm)
                    )
                    CB_spisTypeRasx.show(
                        Modifier.padding(5.dp),
                        style = ComboBoxStyleState(MainDB.styleParam.finParam.rasxodParam.cb_typeRasx)
                    )
                    CB_spisSchet.show(
                        Modifier.padding(5.dp),
                        style = ComboBoxStyleState(MainDB.styleParam.finParam.schetParam.cb_spisSchet)
                    )
                    MyCheckbox(checkPlans, "указать счет-план", Modifier.padding(5.dp))
                    Box(Modifier.animateContentSize()) {
                        if (checkPlans.value) CB_spisSchPl.show(
                            Modifier.padding(5.dp),
                            style = ComboBoxStyleState(MainDB.styleParam.finParam.schetParam.cb_spisSchetPlan)
                        )
                    }
                    Row(Modifier.padding(5.dp)) {
                        MyTextButtStyle1(
                            "Отмена",
                            myStyleTextButton = TextButtonStyleState(buttCancel)
                        ) {
                            dialPan.close()
                        }
                        (summa.value.text.toDoubleOrNull() ?: 0.0).let { summ ->
                            MyTextButtStyle1(
                                "Изменить", Modifier.padding(start = 5.dp),
                                myStyleTextButton = TextButtonStyleState(buttAdd)
                            ) {
                                CB_spisTypeRasx.getSelected()?.second?.let { typer ->
                                    CB_spisSchet.getSelected()?.id?.toLong()?.let { schetid ->
                                        MainDB.addFinFun.updShabRasxod(
                                            itemShabRasxod,
                                            text_name.value.text,
                                            text_name_oper.value.text,
                                            summ,
                                            typer,
                                            schetid,
                                            if (checkPlans.value) CB_spisSchPl.getSelected()?.id?.toLong() else null
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

