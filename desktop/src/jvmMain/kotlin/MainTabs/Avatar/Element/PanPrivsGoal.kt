package MainTabs.Avatar.Element

import MainTabs.Avatar.Items.ComItemPrivsGoal
import MyDialog.MyDialogLayout
import MyList
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import common.BackgroungPanelStyle1
import common.MyTextButtStyle1
import common.MyTextStyleParam
import common.SingleSelectionType
import ru.ragefalcon.sharedcode.models.data.ItemPrivsGoal
import viewmodel.MainDB

fun PanPrivsGoal(
    idGoal: Long,
    dialPan: MyDialogLayout,
    characteristics: Boolean = false
) {
    val dialLayInner = MyDialogLayout()
    val selection = SingleSelectionType<ItemPrivsGoal>()

    dialPan.dial = @Composable {
        BackgroungPanelStyle1 {
            Column(
                Modifier
                    .heightIn(0.dp, dialPan.layHeight.value * 0.8F)
                    .widthIn(0.dp, dialPan.layWidth.value * 0.8F)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Привязанные проекты и этапы:",
                    Modifier.padding(10.dp),
                    style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Center)
                )

                Column(
                    modifier = Modifier.padding(bottom = 5.dp).fillMaxWidth().weight(1f).animateContentSize().border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(listOf(Color(0xFFFFF7D9), Color(0xFFFFF7D9))),
                        shape = RoundedCornerShape(10.dp)
                    ).background(
                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                        color = Color(0xFFE4E0C7),
                    ), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MyList(
                        MainDB.run { if (characteristics) avatarSpis.spisPlanStapOfCharacteristic else avatarSpis.spisPlanStapOfGoal },
                        Modifier.weight(1f).padding(10.dp),
                        darkScroll = true
                    ) { _, itemPrivsGoal ->
                        ComItemPrivsGoal(itemPrivsGoal, selection) { item, expanded ->
                            DropdownMenuItem(onClick = {
                                if (characteristics) MainDB.addAvatar.delPrivsCharacteristic(item.id.toLong())
                                else MainDB.addAvatar.delPrivsGoal(item.id.toLong())
                                expanded.value = false
                            }) {
                                Text(text = "Отвязать", color = Color.White)
                            }

                        }.getComposable()
                    }
                }
                Row {
                    Spacer(Modifier.weight(1F))
                    MyTextButtStyle1("Скрыть") {
                        dialPan.close()
                    }
                    Spacer(Modifier.weight(1F))
                    MyTextButtStyle1("+", Modifier.padding(start = 5.dp)) {
                        PanAddPrivsGoal(idGoal, dialLayInner, characteristics)
                    }
                    Spacer(Modifier.weight(1F))
                }
            }
        }
        dialLayInner.getLay()
    }

    dialPan.show()
}


