package MainTabs.Avatar.Element

import MainTabs.Avatar.Items.ComItemPrivsGoal
import MainTabs.Time.Elements.BoxSelectParentPlan
import MyDialog.MyDialogLayout
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
import androidx.compose.ui.unit.dp
import common.BackgroungPanelStyle1
import common.MyTextButtStyle1
import common.SingleSelectionType
import ru.ragefalcon.sharedcode.models.data.ItemPrivsGoal
import viewmodel.MainDB
import java.util.*

fun PanAddPrivsGoal(
    idGoal: Long,
    dialPan: MyDialogLayout,
    characteristic: Boolean = false
) {
    val dialLayInner = MyDialogLayout()
    val selection = SingleSelectionType<ItemPrivsGoal>()
    val boxSelect =
        BoxSelectParentPlan(arrayIskl = MainDB.run { if (characteristic) avatarSpis.spisPlanStapOfCharacteristic else avatarSpis.spisPlanStapOfGoal }
            .getState().value?.filter {
            it.stap != "0"
        }?.map {
            it.stap.toLong()
        } ?: listOf(), selectForGoalOrDream = true, startWithOpenPlan = true, characteristic = characteristic)

    dialPan.dial = @Composable {
        BackgroungPanelStyle1 {
            Column(
                Modifier
                    .heightIn(0.dp, dialPan.layHeight.value * 0.7F)
                    .widthIn(0.dp, dialPan.layWidth.value * 0.7F)
                    .padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                boxSelect.getComposable()
                Column(
                    modifier = Modifier.padding(bottom = 5.dp).animateContentSize().border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(listOf(Color(0xFFFFF7D9), Color(0xFFFFF7D9))),
                        shape = RoundedCornerShape(10.dp)
                    ).background(
                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                        color = Color(0xFFE4E0C7),
                    ), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    boxSelect.selectionPlanParent.selected?.let { itemPlan ->
                        val itemStap = boxSelect.selectionPlanStapParent.selected
                        ComItemPrivsGoal(
                            ItemPrivsGoal(
                                id = "-1",
                                name = itemStap?.let { "${it.name}\n[${itemPlan.name}]" } ?: itemPlan.name,
                                namePlan = itemPlan.name,
                                vajn = itemPlan.vajn,
                                stap = itemStap?.let { it.id } ?: "0",
                                id_plan = itemPlan.id,
                                gotov = itemStap?.let { it.gotov } ?: itemPlan.gotov,
                                hour = itemStap?.let { it.hour } ?: itemPlan.hour,
                                opis = itemStap?.let { it.opis } ?: itemPlan.opis
                            ), editable = false, selection = SingleSelectionType()
                        ).getComposable()
                    }
                }
                Row {
                    Spacer(Modifier.weight(1F))
                    MyTextButtStyle1("Отмена") {
                        dialPan.close()
                    }
                    Spacer(Modifier.weight(1F))
                    boxSelect.selectionPlanParent.selected?.let { itemPlan ->
                        MyTextButtStyle1("Привязать", Modifier.padding(start = 5.dp)) {
                            val itemStap = boxSelect.selectionPlanStapParent.selected
                            if (characteristic) MainDB.addAvatar.addPrivsCharacteristic(
                                id_characteristic = idGoal,
                                stap = itemStap?.id?.toLong() ?: 0,
                                id_plan = itemPlan.id.toLong()
                            )
                            else MainDB.addAvatar.addPrivsGoal(
                                id_goal = idGoal,
                                name = itemStap?.let { "${it.name}\n[${itemPlan.name}]" } ?: itemPlan.name,
                                stap = itemStap?.id?.toLong() ?: 0,
                                id_plan = itemPlan.id.toLong(),
                                vajn = itemPlan.vajn,
                                date = Date().time
                            )

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


