package MainTabs.Time.Elements

import MyDialog.MyDialogLayout
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import common.*
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemEffekt
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import viewmodel.MainDB

fun PanAddEffekt(
    dialPan: MyDialogLayout,
    itemPlan: ItemPlan
) {


    val itemEffekt: ItemEffekt? = MainDB.timeSpis.spisEffekt.getState().value?.find { it.idplan == itemPlan.id.toLong() }

    val typeSeekBar =
        DiskretSeekBar(
            listOf("Эффективность" to "Effekt", "Злоупотребление" to "Zlo"),
            itemEffekt?.let { if (it.norma > 0) "Effekt" else "Zlo"} ?: "Effekt",
        )

    val dialLayInner = MyDialogLayout()
    dialPan.dial = @Composable {
//        val textNorma = remember { mutableStateOf(itemEffekt?.let { if (it.norma > 0) it.norma else -it.norma } ?: 10.0) }
        val textNorma = remember { mutableStateOf(TextFieldValue(itemEffekt?.let { if (it.norma > 0) it.norma else -it.norma }?.roundToString(2) ?: "10.00")) }
        BackgroungPanelStyle1 { //modif ->
            Column(Modifier.padding(15.dp).fillMaxWidth(0.6F), horizontalAlignment = Alignment.CenterHorizontally) {
//                Row(verticalAlignment = Alignment.CenterVertically){
                typeSeekBar.show(Modifier.padding(bottom = 10.dp))//weight(1f))
                typeSeekBar.active?.let {
                    when (it.cod) {
                        "Effekt" -> {
                            MyTextStyle1("${itemEffekt?.let { "Изменить" } ?: "Добавить"} отслеживание эффективности по проекту \"${itemPlan.name}\" и установить норму:")
                        }
                        "Zlo" -> {
                            MyTextStyle1("${itemEffekt?.let { "Изменить" } ?: "Добавить"} отслеживание злоупотребления по проекту \"${itemPlan.name}\" и установить норму:")
                        }
                    }
                }
                MyOutlinedTextFieldDouble("часов в неделю", textNorma)
                Row {
                    MyTextButtStyle1("Отмена") {
                        dialPan.close()
                    }
                    MyTextButtStyle1(itemEffekt?.let { "Изменить" } ?: "Добавить", Modifier.padding(start = 5.dp)) {
                        var norm: Double = 0.0
                        typeSeekBar.active?.let {
                            when (it.cod) {
                                "Effekt" -> {
                                    norm = textNorma.value.text.toDoubleOrNull() ?: 1.0
                                }
                                "Zlo" -> {
                                    norm = -(textNorma.value.text.toDoubleOrNull() ?: 1.0)
                                }
                            }
                        }
                        itemEffekt?.let { itEff ->
                            MainDB.addTime.updEffekt(
                                id = itEff.id.toLong(),
                                name = itemPlan.name,
                                norma = norm,
                            )
                            dialPan.close()
                        } ?: run {
                            MainDB.addTime.addEffekt(
                                name = itemPlan.name,
                                idplan = itemPlan.id.toLong(),
                                norma = norm,
                            )
                            dialPan.close()
                        }
                    }
                }
            }
        }
        dialLayInner.getLay()
    }

    dialPan.show()
}
