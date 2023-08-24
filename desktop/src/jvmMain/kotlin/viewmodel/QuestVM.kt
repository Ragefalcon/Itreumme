package viewmodel

import MainTabs.Quest.Items.ComItemTrigger
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.* //mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.Text
import common.MyTextStyleParam
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeParentOfTrig

object QuestVM {
    var openQuestDB by mutableStateOf<QuestDB?>(null)
    var loadQuestDB by mutableStateOf<QuestDB?>(null)

    fun withDB(ff: (QuestDB)->Unit) = openQuestDB?.let { ff(it) }
    @Composable
    fun withDBC(ff: @Composable (QuestDB)->Unit) = openQuestDB?.let { ff(it) }

    @Composable
    fun getComItemTriggers(code: String, parent_id: Long, modifier: Modifier = Modifier.padding(end = 5.dp), emptyMarker: Boolean = false, editable: Boolean = true){
        openQuestDB?.spisQuest?.spisTrigger?.getState()?.value?.let { listTrig ->
            listTrig.filter { it.parent_type == code && it.parent_id == parent_id }?.let {
                it.forEach { trig ->
                    ComItemTrigger(trig, modifier, editable)
                }
                if (it.isEmpty() && emptyMarker) Box(
                    modifier.background(
                        color = Color.Red.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(3.dp)
                    ).defaultMinSize(80.dp, 10.dp)
                ) {}
            }
        }

    }

    @Composable
    fun getTriggerMarkersForTriggerChilds(type_id: Long, child_id: Long, modifier: Modifier = Modifier.padding(end = 5.dp), emptyMarker: Boolean = false){
        QuestVM.openQuestDB?.spisQuest?.spisTrigger?.getState()?.value?.let { listTrig ->
            listTrig.filter { it.type_id == type_id && it.child_id == child_id }
                .let {
                    it.forEach { trig ->
                        Row(
                            modifier
                                .background(
                                    if (trig.parent_type != TypeParentOfTrig.STARTQUESTDIALOG.code)
                                        MyColorARGB.colorRasxodTheme0.toColor().copy(alpha = 0.3f)
                                    else MyColorARGB.colorStatTint_01.toColor().copy(alpha = 0.3f),
                                    RoundedCornerShape(5.dp)
                                )
                                .border(
                                    width = 0.5.dp,
                                    brush = if (trig.parent_type != TypeParentOfTrig.STARTQUESTDIALOG.code)
                                        Brush.horizontalGradient(
                                            listOf(Color(0x4FFFF7D9), Color(0x4FFFF7D9))
                                        )
                                    else Brush.horizontalGradient(
                                        listOf(
                                            MyColorARGB.colorStatTint_01.toColor()
                                                .copy(alpha = 0.6f),
                                            MyColorARGB.colorStatTint_01.toColor()
                                                .copy(alpha = 0.6f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(5.dp)
                                )
                        ) {
                            Text(
                                trig.parent_type,
                                Modifier.padding(horizontal = 2.dp, vertical = 3.dp)
                                    .padding(end = 8.dp),
                                style = MyTextStyleParam.style5
                            )
                        }
                    }
                    if (it.isEmpty() && emptyMarker) Box(
                        modifier.background(
                            color = Color.Red.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(5.dp)
                        ).border(
                            width = 3.dp,
                            brush = Brush.horizontalGradient(
                                listOf(Color(0x4FFFF7D9), Color(0x4FFFF7D9))
                            ),
                            shape = RoundedCornerShape(5.dp)
                        ).defaultMinSize(70.dp, 20.dp)
                    ) {}
                }
        }
    }

}