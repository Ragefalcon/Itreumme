package MainTabs.Setting.Element


import MainTabs.Setting.Items.ComItemShablonStyle
import MyDialog.MyDialogLayout
import MyDialog.MyOneVopros
import MyList
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import common.*
import ru.ragefalcon.sharedcode.models.data.ItemSaveSetStyle
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.TypeSaveStyleSet
import viewmodel.MainDB

fun PanSelectShablonStyle(
    dialPan: MyDialogLayout,
    type: TypeSaveStyleSet,
    listener: (ItemSaveSetStyle) -> Unit
) {
    val dialLayInner = MyDialogLayout()
    val selection = SingleSelectionType<ItemSaveSetStyle>()
//    val loadPovtor = mutableStateOf(true)
//    val loadTime = mutableStateOf(true)

    dialPan.dial = @Composable {
        BackgroungPanelStyle1 {
            Column(
                Modifier
                    .heightIn(0.dp, dialPan.layHeight.value * 0.7F)
                    .widthIn(0.dp, dialPan.layWidth.value * 0.7F)
                    .padding(10.dp)
            ) {
                MyTextStyle1(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    text = type.nameSpis,
                    textAlign = TextAlign.Center
                )
                Column(
                    modifier = Modifier.padding(bottom = 5.dp).weight(1f).animateContentSize().border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(listOf(Color(0xFFFFF7D9), Color(0xFFFFF7D9))),
                        shape = RoundedCornerShape(10.dp)
                    ).background(
                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                        color = Color(0xFFE4E0C7),
                    ), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MainDB.editStyleSpis.getSpisSaveSetStyle(type).let { spis ->
                        if (spis?.getState()?.value?.isNotEmpty() == true) {
                            MyList(
                                spis,
                                Modifier.weight(1f).padding(bottom = 10.dp).padding(horizontal = 20.dp),
                                darkScroll = true
                            ) { ind, itemShablonStyle ->
                                ComItemShablonStyle(itemShablonStyle, selection, doubleClick = {
                                    listener(it)
                                    dialPan.close()
                                }) { item, expanded ->
                                    MyDropdownMenuItem(expanded, "Изменить") {
                                        MyOneVopros(
                                            dialPan = dialLayInner,
                                            "Введите новое название для шаблона стиля.",
                                            "Изменить",
                                            otvetDefault = item.name
                                        ) {
                                            if (it != "") MainDB.addEditStyle.updSaveSetStyle(item.id, it)
                                        }
                                    }
                                    MyDeleteDropdownMenuButton(expanded) {
                                        MainDB.addEditStyle.delSaveSetStyle(item.id)
                                    }

                                }.getComposable()
                            }
                        } else {
                            Spacer(Modifier.weight(1f))
                            MyTextStyle1(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth(),
                                text = "Здесь пока нет ни одного шаблона",
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
//                MyTextStyle1("Перенести из шаблона")
//                Row {
//                    MyCheckbox(loadPovtor,"Повторы")
//                    MyCheckbox(loadTime,"Время")
//                }
                Row {
                    Spacer(Modifier.weight(1F))
                    MyTextButtStyle1("Отмена") {
                        dialPan.close()
                    }
                    Spacer(Modifier.weight(1F))
                    selection.selected?.let { itemShab ->
                        MyTextButtStyle1("Загрузить", Modifier.padding(start = 5.dp)) {
                            listener(itemShab)
                            dialPan.close()
                        }
                        Spacer(Modifier.weight(1F))
                    }
                }
            }
        }
        dialLayInner.getLay()
    }

    dialPan.show()
}


