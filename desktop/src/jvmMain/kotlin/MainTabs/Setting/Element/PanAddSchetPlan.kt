package MainTabs.Setting.Element


import MyDialog.MyDialogLayout
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import common.*
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemSettSchetPlan
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeBindElementForSchetPlan
import viewmodel.MainDB

fun PanAddSchetPlan(
    dialPan: MyDialogLayout,
    item: ItemSettSchetPlan? = null,
    nameElement: String? = null,
    bind_element: Pair<TypeBindElementForSchetPlan,Long>? = null
) {
    dialPan.dial = @Composable {
        val text_name = remember { mutableStateOf(TextFieldValue(item?.name ?: nameElement ?: "")) }
        val summa_min = remember {
            mutableStateOf(TextFieldValue(item?.min_aim?.let { if (it >= 0.0) it.roundToString(2) else null }
                ?: "0.00"))
        }
        val summa_max = remember {
            mutableStateOf(TextFieldValue(item?.max_aim?.let { if (it >= 0.0) it.roundToString(2) else null }
                ?: "0.00"))
        }
        val checkMin = remember { mutableStateOf(item?.min_aim?.let { if (it > 0.0) true else null } ?: false) }
        val checkMax = remember { mutableStateOf(item?.max_aim?.let { if (it > 0.0) true else null } ?: false) }
        fun getMimMax(textV: TextFieldValue): Double? {
            return textV.text.toDoubleOrNull()?.let {
                if (it <= 0) null else it
            }
        }

        fun getMax(): Double {
            val max = getMimMax(summa_max.value)
            val min = getMimMax(summa_min.value)
            max?.let { maxx ->
                min?.let { minn ->
                    if (maxx <= minn) return -1.0 else return maxx
                }
                return maxx
            }
            return -1.0
        }
        BackgroungPanelStyle1 { //modif ->
            Column(Modifier.padding(15.dp).animateContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                MyOutlinedTextField("Название счета", text_name)
                MyCheckbox(checkMin, "Минимальная цель", Modifier.padding(end = 15.dp))
                if (checkMin.value) {
                    MyOutlinedTextFieldDouble("", summa_min)
                    MyCheckbox(checkMax, "Максимальная цель", Modifier.padding(start = 10.dp, end = 15.dp))
                    if (checkMax.value) MyOutlinedTextFieldDouble("", summa_max, Modifier.padding(start = 10.dp))
                }
                Row {
                    MyTextButtStyle1("Отмена") {
                        dialPan.close()
                    }
                    if (text_name.value.text != "") {
                        if (item != null) {
                            MyTextButtStyle1("Изменить", Modifier.padding(start = 5.dp)) {
                                MainDB.addFinFun.updSchetPLan(
                                    id = item.id.toLong(),
                                    name = text_name.value.text,
                                    min_aim = if (checkMin.value) getMimMax(summa_min.value) ?: -1.0 else -1.0,
                                    max_aim = if (checkMax.value) getMax() else -1.0,
                                )
                                dialPan.close()
                            }
                        } else {
                            MyTextButtStyle1("Добавить", Modifier.padding(start = 5.dp)) {
                                MainDB.addFinFun.addSchetPlan(
                                    name = text_name.value.text,
                                    min_aim = if (checkMin.value) getMimMax(summa_min.value) ?: -1.0 else -1.0,
                                    max_aim = if (checkMax.value) getMax() else -1.0,
                                    open = 1,
                                    bind_element = bind_element
                                )
                                dialPan.close()
                            }
                        }
                    }
                }
            }
        }
    }
    dialPan.show()
}

