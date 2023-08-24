package MainTabs.Quest.Element

import MyDialog.MyDialogLayout
import adapters.MyComboBox
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.RowVA
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemLevelTreeSkillsQuest
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatQuestElementVisible
import viewmodel.QuestDB

fun PanAddLevelTreeSkillsQuest(
    dialPan: MyDialogLayout,
    questDB: QuestDB,
    id_tree: Long,
    item: ItemLevelTreeSkillsQuest? = null,
    level: Long? = null
) {
    val dialLayInner = MyDialogLayout()
/*
    val CB_spisStartVisible = MyComboBox(TypeStatQuestElementVisible.values().toList(), nameItem = { it.nameType }).apply {
        item?.let {  itemTree ->
            TypeStatQuestElementVisible.getType(item.visible_stat)?.let {
                select(it)
            }

        }
    }
*/

    dialPan.dial = @Composable {
        val text_name = remember { mutableStateOf(TextFieldValue(item?.name ?: "")) }
        val text_opis = remember { mutableStateOf(TextFieldValue(item?.opis ?: "")) }
//        val text_proc = remember { mutableStateOf(TextFieldValue(item?.proc_porog?.toString() ?: "0.0")) }
        val progressGotov = remember { mutableStateOf(((item?.proc_porog ?: 0.0)/100f).toFloat()) }
        BackgroungPanelStyle1 { //modif ->
            Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                MyOutlinedTextField("Название уровня", text_name)
                MyOutlinedTextField(
                    "Описание уровня",
                    text_opis,
                    Modifier.heightIn(200.dp, 500.dp),
                    TextAlign.Start
                )
//                CB_spisStartVisible.show()
                RowVA {
                    Text(
                        text = "Минимальный процент необязательных достижений:",
                        modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 5.dp, end = 5.dp),
                        style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start, fontSize = 18.sp, color = MyColorARGB.colorSchetTheme.toColor())
                    )
                    Text(
                        text = "${(progressGotov.value*100.0).toInt()}%",
                        modifier = Modifier.padding(start = 5.dp, top = 10.dp, bottom = 5.dp, end = 15.dp),
                        style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start, fontSize = 18.sp, color = MyColorARGB.colorDoxodTheme.toColor())
                    )
                }
                val colorVyp = MyColorARGB.colorEffektShkal_Nedel.toColor()
                Slider(
                    value = progressGotov.value,
                    modifier = Modifier.height(20.dp).width(400.dp),
                    onValueChange = {
                        progressGotov.value = it
                    },
                    onValueChangeFinished = {
//                        changeGotov?.invoke(item, progressGotov.value)
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = colorVyp, //MaterialTheme.colors.primary,
                        disabledThumbColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
                            .compositeOver(MaterialTheme.colors.surface),
                        activeTrackColor = colorVyp, //MaterialTheme.colors.primary,
                        inactiveTrackColor = Color(0x6FFF8888), //activeTrackColor.copy(alpha = InactiveTrackAlpha),
                        disabledActiveTrackColor = MaterialTheme.colors.onSurface.copy(alpha = SliderDefaults.DisabledActiveTrackAlpha),
                        disabledInactiveTrackColor = MaterialTheme.colors.onSurface.copy(
                            alpha = SliderDefaults.DisabledInactiveTrackAlpha
                        ),
                        activeTickColor = contentColorFor(Color.Blue).copy(alpha = SliderDefaults.TickAlpha),
                        inactiveTickColor = Color.Magenta.copy(alpha = SliderDefaults.TickAlpha),
                        disabledActiveTickColor = contentColorFor(colorVyp).copy(alpha = SliderDefaults.TickAlpha)
                            .copy(alpha = SliderDefaults.DisabledTickAlpha),
                        disabledInactiveTickColor = MaterialTheme.colors.onSurface.copy(
                            alpha = SliderDefaults.DisabledInactiveTrackAlpha
                        )
                            .copy(alpha = SliderDefaults.DisabledTickAlpha)
                    )
                )

//                MyOutlinedTextFieldDouble("Минимальный процент необязательных достижений", text_proc)
                Row {
                    MyTextButtStyle1("Отмена") {
                        dialPan.close()
                    }
                    MyTextButtStyle1(item?.let { "Изменить" } ?: "Добавить", Modifier.padding(start = 5.dp)) {
                        if (text_name.value.text != "")
                            item?.let {
                                questDB.addQuest.updLevelTreeSkills(
                                    item = it,
                                    name = text_name.value.text,
                                    opis = text_opis.value.text,
                                    proc_porog = (progressGotov.value*100.0).toInt().toDouble(),
                                    visible_stat = 0 //CB_spisStartVisible.getSelected()?.codValue ?: 0
                                )
                                dialPan.close()
                            } ?: run {
                                questDB.addQuest.addLevelTreeSkills(
                                    id_tree = id_tree,
                                    name = text_name.value.text,
                                    opis = text_opis.value.text,
                                    proc_porog = (progressGotov.value*100.0).toInt().toDouble(), //text_proc.value.text.toDouble(),
                                    level = level,
                                    visible_stat = 0 //CB_spisStartVisible.getSelected()?.codValue ?: 0
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
