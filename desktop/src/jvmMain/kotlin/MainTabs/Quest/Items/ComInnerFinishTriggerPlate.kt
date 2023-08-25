package MainTabs.Quest.Items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStartObjOfTrigger
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.InnerDialogs.InnerFinishTriggerEnum
import viewmodel.QuestVM

@Composable
fun ComInnerFinishTriggerPlate(
    item: InnerFinishTriggerEnum,
    active: Boolean,
    onClick: (InnerFinishTriggerEnum) -> Unit
) {
    val expandedOpis = remember { mutableStateOf(false) }
    MyCardStyle1(active, 0, {
        onClick(item)
    }) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.padding(5.dp).padding(end = 10.dp).weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            modifier = Modifier.padding(start = 15.dp)
                                .weight(1f),
                            text = item.nameTrig,
                            style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start, fontSize = 17.sp)
                        )
                        Column() {
                            QuestVM.getTriggerMarkersForTriggerChilds(
                                TypeStartObjOfTrigger.INNERFINISH.id,
                                item.id,
                                emptyMarker = true
                            )
                        }
                        if (item.opisTrig != "") RotationButtStyle1(
                            expandedOpis,
                            Modifier.padding(start = 10.dp, end = 10.dp)
                        ) {
                        }
                    }

                }

            }
            if ((item.opisTrig != "")) {
                BoxExpand(
                    expandedOpis,
                    Modifier.myModWithBound1(),
                    Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .padding(5.dp)
                            .padding(start = 10.dp),
                        text = item.opisTrig,
                        style = TextStyle(color = Color(0xFFFFF7D9)),
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}