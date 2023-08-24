package MainTabs.Finance.Element

import MainTabs.Finance.Items.ComItemRasxodShab
import MyDialog.MyDialogLayout
import MyDialog.MyOneVopros
import MyDialog.buttDatePickerWithButton
import MyShowMessage
import adapters.MyComboBox
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
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
import ru.ragefalcon.sharedcode.models.data.ItemRasxod
import ru.ragefalcon.sharedcode.models.data.ItemShabRasxod
import ru.ragefalcon.sharedcode.source.disk.getValue
import viewmodel.MainDB
import java.util.*

fun PanAddRasx(
    dialPan: MyDialogLayout,
    item: ItemRasxod? = null,
    schetId: String? = null,
    schetPlanId: String? = null,
    itemShabRasxod: ItemShabRasxod? = null
) {
    val dialLayInner = MyDialogLayout()
    val dateInner = mutableStateOf(item?.data?.let { Date(it) } ?: MainDB.dateFin.value)
    val CB_spisSchet = MyComboBox(MainDB.finSpis.spisSchet, nameItem = { it.name }).apply {
        item?.schetid?.let { schetid ->
            MainDB.finSpis.spisSchet.getState().value?.find { it.id == schetid }?.let {
                select(it)
            }
        } ?: run {
            schetId?.let {
                MainDB.finSpis.spisSchet.getState().value?.find { it.id == schetId }?.let {
                    select(it)
                }
            }
        }
    }
    val CB_spisTypeRasx = MyComboBox(MainDB.finSpis.spisTypeRasx, nameItem = { it.second }).apply {
        item?.typeid?.let { typeid ->
            MainDB.finSpis.spisTypeRasx.getState().value?.find { it.first == typeid }?.let {
                select(it)
            }
        }
    }

    val CB_spisSchPl = MyComboBox(MainDB.finSpis.spisSchetPlan, nameItem = { it.name }).apply {
        item?.schpl_id?.let { schpl_id ->
            MainDB.finSpis.spisSchetPlan.getState().value?.find { it.id.toLong() == schpl_id }?.let {
                select(it)
            }
        } ?: run {
            schetPlanId?.let {
                MainDB.finSpis.spisSchetPlan.getState().value?.find { it.id == schetPlanId }?.let {
                    select(it)
                }
            }
        }
    }
    val text_name = mutableStateOf(TextFieldValue(item?.name ?: ""))
    val summa = mutableStateOf(TextFieldValue(item?.summa?.roundToString(2) ?: ""))
    val checkPlans = mutableStateOf(item?.let { itemRasx ->
        MainDB.finSpis.spisTyperasxodForPlan.getState().value?.find { it.id == itemRasx.typeid }?.let {
            it.schpl_id != itemRasx.schpl_id
        }
    } ?: schetPlanId?.let { true } ?: false)

    fun loadShablon(itemShab: ItemShabRasxod){
        if (itemShab.nameoper != "") text_name.value = TextFieldValue(itemShab.nameoper)
        if (itemShab.summa > 0) summa.value = TextFieldValue(itemShab.summa.roundToString(2))
        var message = ""
        MainDB.finSpis.spisSchet.getState().value?.find { it.id.toLong() == itemShab.schet_id }?.let {
            CB_spisSchet.select(it)
        } ?: run {
            message += "В загружаемом шаблоне указан счет, который уже закрыт или удален, поэтому данный параметр оставлен без изменений.\n\n"
        }
        MainDB.finSpis.spisTypeRasx.getState().value?.find { it.second == itemShab.type }?.let { typeRasx ->
            CB_spisTypeRasx.select(typeRasx)
        } ?: run {
            message += "В загружаемом шаблоне указан тип расхода, который уже закрыт или удален, поэтому данный параметр оставлен без изменений.\n\n"
        }
        checkPlans.value = false
        itemShab.schpl_id?.let { schpl_id ->
            checkPlans.value = true
            MainDB.finSpis.spisSchetPlan.getState().value?.find { it.id.toLong() == schpl_id }?.let {
                CB_spisSchPl.select(it)
            } ?: run {
                message += "В загружаемом шаблоне указан счет-план, который уже закрыт или удален, поэтому данный параметр оставлен без изменений.\n\n"
            }
        }
        if (message != "") MyShowMessage(dialLayInner, message)
    }

    @Composable
    fun shablonButton() = Row(Modifier.padding(5.dp)) {
        MyIconButtStyle(
            "ic_baseline_save_24.xml",
            sizeIcon = 40.dp,
            myStyleButton = IconButtonStyleState(MainDB.styleParam.finParam.rasxodParam.panAddRasxod.shablSaveButt)
        )
        {
            CB_spisSchet.getSelected()?.id?.toLong()?.let { schetid ->
                CB_spisTypeRasx.getSelected()?.second?.let { type ->
                    MyOneVopros(
                        dialLayInner,
                        "Введите название шаблона:",
                        "Сохранить",
                        "Название",
                        text_name.value.text,
                        styleTextField = MyTextFieldStyleState(MainDB.styleParam.finParam.rasxodParam.panAddRasxod.textName)
                    ) { nameoper ->
                        MainDB.addFinFun.addShabRasxod(
                            nameoper,
                            text_name.value.text,
                            summa.value.text.toDoubleOrNull() ?: 0.0,
                            type,
                            schetid,
                            if (checkPlans.value) CB_spisSchPl.getSelected()?.id?.toLong() else null
                        )
                    }
                }
            }
        }
        MyIconButtStyle(
            "ic_baseline_cloud_upload_24.xml",
            Modifier.padding(start = 5.dp),
            sizeIcon = 40.dp,
            myStyleButton = IconButtonStyleState(MainDB.styleParam.finParam.rasxodParam.panAddRasxod.shablLoadButt)
        )
        {
            PanSelectOneItem(
                dialLayInner, MainDB.finSpis.spisShabRasxod, { itemShab ->
                    loadShablon(itemShab)
                },
                stylePanelItem = MainDB.styleParam.finParam.rasxodParam.panAddRasxod.plateShablon
            ) { item, selection, dialL, listener ->
                ComItemRasxodShab(
                    item,
                    selection,
                    listener,
                    MainDB.styleParam.finParam.rasxodParam.panAddRasxod.itemRasxodShablon,
                    dialL
                )
            }
        }
    }

    itemShabRasxod?.let { loadShablon(it) }

    dialPan.dial = @Composable {
        with(MainDB.styleParam.finParam.rasxodParam.panAddRasxod) {
            BackgroungPanelStyle1(
                vignette = VIGNETTE.getValue(),
                style = SimplePlateStyleState(platePanel)
            ) { //modif ->
                Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    MyTextField(
                        text_name,
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
                    buttDatePickerWithButton(
                        dialLayInner, dateInner, Modifier.padding(5.dp),
                        myStyleTextDate = TextButtonStyleState(MainDB.styleParam.finParam.buttDate),
                        myStyleTextArrow = TextButtonStyleState(MainDB.styleParam.finParam.buttNextDate),
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
                    shablonButton()
                    Row(Modifier.padding(5.dp)) {
                        MyTextButtStyle1(
                            "Отмена",
                            myStyleTextButton = TextButtonStyleState(buttCancel)
                        ) {
                            dialPan.close()
                        }
                        (summa.value.text.toDoubleOrNull() ?: 0.0).let { summ ->
                            if (summ != 0.0) {
                                if (item != null && item.id != "-1") {
                                    MyTextButtStyle1(
                                        "Изменить", Modifier.padding(start = 5.dp),
                                        myStyleTextButton = TextButtonStyleState(buttAdd)
                                    ) {
                                        CB_spisTypeRasx.getSelected()?.first?.toLong()?.let { typeid ->
                                            CB_spisSchet.getSelected()?.id?.toLong()?.let { schetid ->
                                                MainDB.addFinFun.updRasxod(
                                                    item,
                                                    text_name.value.text,
                                                    summ,
                                                    typeid,
                                                    dateInner.value.time,
                                                    schetid,
                                                    if (checkPlans.value) CB_spisSchPl.getSelected()?.id?.toLong() else null
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
                                        CB_spisTypeRasx.getSelected()?.first?.toLong()?.let { typeid ->
                                            CB_spisSchet.getSelected()?.id?.toLong()?.let { schetid ->
                                                MainDB.addFinFun.addRasxod(
                                                    text_name.value.text,
                                                    summ,
                                                    typeid,
                                                    dateInner.value.time,
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
            }
        }
        dialLayInner.getLay()
    }
    dialPan.show()
}

