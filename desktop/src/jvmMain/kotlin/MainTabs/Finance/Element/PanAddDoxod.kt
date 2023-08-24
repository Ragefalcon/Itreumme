package MainTabs.Finance.Element

import MainTabs.Finance.Items.ComItemDoxodShab
import MyDialog.MyDialogLayout
import MyDialog.MyOneVopros
import MyDialog.buttDatePickerWithButton
import MyShowMessage
import adapters.MyComboBox
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import ru.ragefalcon.sharedcode.models.data.ItemDoxod
import ru.ragefalcon.sharedcode.models.data.ItemShabDoxod
import ru.ragefalcon.sharedcode.models.data.ItemShabRasxod
import ru.ragefalcon.sharedcode.source.disk.getValue
import viewmodel.MainDB
import java.util.*

fun PanAddDoxod(
    dialPan: MyDialogLayout,
    item: ItemDoxod? = null,
    schetId: String? = null,
    itemShabDoxod: ItemShabDoxod? = null
) {
    val dialLayInner = MyDialogLayout()
    val dateInner = mutableStateOf(item?.data?.let { Date(it) } ?: MainDB.dateFin.value)
    val CB_spisSchet = MyComboBox(MainDB.finSpis.spisSchet, nameItem = { it.name }).apply {
        item?.let {
            MainDB.finSpis.spisSchet.getState().value?.find { it.id == item.schetid }?.let {
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
    val CB_spisTypeDox = MyComboBox(MainDB.finSpis.spisTypeDox, nameItem = { it.second }).apply {
        item?.let {
            MainDB.finSpis.spisTypeDox.getState().value?.find { it.first == item.typeid }?.let {
                select(it)
            }
        }
    }

    val text_name = mutableStateOf(TextFieldValue(item?.name ?: ""))
    val summa = mutableStateOf(TextFieldValue(item?.summa?.roundToString(2) ?: ""))

    fun loadShablon(itemShab: ItemShabDoxod) {
        if (itemShab.nameoper != "") text_name.value = TextFieldValue(itemShab.nameoper)
        if (itemShab.summa > 0) summa.value = TextFieldValue(itemShab.summa.roundToString(2))
        var message = ""
        MainDB.finSpis.spisSchet.getState().value?.find { it.id.toLong() == itemShab.schet_id }?.let {
            CB_spisSchet.select(it)
        } ?: run {
            message += "В загружаемом шаблоне указан счет, который уже закрыт или удален, поэтому данный параметр оставлен без изменений.\n\n"
        }
        MainDB.finSpis.spisTypeDox.getState().value?.find { it.second == itemShab.type }?.let {
            CB_spisTypeDox.select(it)
        } ?: run {
            message += "В загружаемом шаблоне указан тип дохода, который уже закрыт или удален, поэтому данный параметр оставлен без изменений.\n\n"
        }
        if (message != "") MyShowMessage(dialLayInner, message)
    }
    @Composable
    fun shablonButton() = Row(Modifier.padding(5.dp)) {
        MyIconButtStyle(
            "ic_baseline_save_24.xml",
            sizeIcon = 40.dp,
            myStyleButton = IconButtonStyleState(MainDB.styleParam.finParam.doxodParam.panAddDoxod.shablSaveButt)
        ) {
            CB_spisSchet.getSelected()?.id?.toLong()?.let { schetid ->
                CB_spisTypeDox.getSelected()?.second?.let { type ->
                    MyOneVopros(
                        dialLayInner,
                        "Введите название шаблона:",
                        "Сохранить",
                        "Название",
                        text_name.value.text,
                        styleTextField = MyTextFieldStyleState(MainDB.styleParam.finParam.doxodParam.panAddDoxod.textName)
                    ) { nameoper ->
                        MainDB.addFinFun.addShabDoxod(
                            nameoper,
                            text_name.value.text,
                            summa.value.text.toDoubleOrNull() ?: 0.0,
                            type,
                            schetid
                        )
                    }
                }
            }
        }
        MyIconButtStyle(
            "ic_baseline_cloud_upload_24.xml",
            Modifier.padding(start = 5.dp),
            sizeIcon = 40.dp,
            myStyleButton = IconButtonStyleState(MainDB.styleParam.finParam.doxodParam.panAddDoxod.shablLoadButt)
        ) {
            PanSelectOneItem(dialLayInner, MainDB.finSpis.spisShabDoxod, { itemShab ->
                loadShablon(itemShab)
            },
            stylePanelItem = MainDB.styleParam.finParam.doxodParam.panAddDoxod.plateShablon
                ) { item, selection, dialL, listener ->
                ComItemDoxodShab(item, selection, listener, MainDB.styleParam.finParam.doxodParam.panAddDoxod.itemDoxodShablon,dialL)
            }
        }
    }

    itemShabDoxod?.let { loadShablon(it) }

    dialPan.dial = @Composable {
        with(MainDB.styleParam.finParam.doxodParam.panAddDoxod) {
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
                    buttDatePickerWithButton(
                        dialLayInner, dateInner, Modifier.padding(5.dp),
                        myStyleTextDate = TextButtonStyleState(MainDB.styleParam.finParam.buttDate),
                        myStyleTextArrow = TextButtonStyleState(MainDB.styleParam.finParam.buttNextDate),
                    )
                    CB_spisSchet.show(
                        Modifier.padding(5.dp),
                        style = ComboBoxStyleState(MainDB.styleParam.finParam.schetParam.cb_spisSchet)
                    )
                    shablonButton()
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
                                        CB_spisTypeDox.getSelected()?.first?.toLong()?.let { typeid ->
                                            CB_spisSchet.getSelected()?.id?.toLong()?.let { schetid ->
                                                MainDB.addFinFun.updDoxod(
                                                    item,
                                                    text_name.value.text,
                                                    summ,
                                                    typeid,
                                                    dateInner.value.time,
                                                    schetid
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
                                        CB_spisTypeDox.getSelected()?.first?.toLong()?.let { typeid ->
                                            CB_spisSchet.getSelected()?.id?.toLong()?.let { schetid ->
                                                MainDB.addFinFun.addDoxod(
                                                    text_name.value.text,
                                                    summ,
                                                    typeid,
                                                    dateInner.value.time,
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
            }
        }
        dialLayInner.getLay()
    }
    dialPan.show()
}