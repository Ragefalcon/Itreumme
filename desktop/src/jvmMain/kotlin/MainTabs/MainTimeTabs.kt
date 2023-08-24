package MainTabs

import MainTabs.Time.Elements.PanAddEffekt
import MainTabs.Time.Elements.panTimeline
import MainTabs.Time.Items.ComItemEffekt
import MainTabs.Time.Tabs.DenPlanTab
import MainTabs.Time.Tabs.PlanTabAnim
import MainTabs.Time.Tabs.VxodTab
import MyDialog.MyDialogLayout
import MyList
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.extensions.longMinusTimeUTC
import ru.ragefalcon.sharedcode.extensions.unOffset
import ru.ragefalcon.sharedcode.models.data.ItemEffekt
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.tabElement
import viewmodel.MainDB
import viewmodel.StateVM
import java.util.*

class MainTimeTabs(val dialLay: MyDialogLayout) {

//    val dialLay = MyDialogLayout()

    enum class TimeTabsEnum(override val nameTab: String) : tabElement {
        Plans("Проекты"),
        DenPlans("Ежедневник"),
        Vxod("Входящие");
    }

    val timeSeekBar =
        EnumDiskretSeekBar(TimeTabsEnum::class, TimeTabsEnum.DenPlans)
    val denPlans = DenPlanTab(dialLay)
    val vxodTab = VxodTab(dialLay)

    //    val planTab = PlanTab(dialLay)
    val planTab = PlanTabAnim()

    private val selection = SingleSelectionType<ItemEffekt>()

    @Composable
    fun show() {
        LaunchedEffect(MainDB.denPlanDate.value) {
            if (MainDB.timeFun.getDay() != MainDB.denPlanDate.value.time) MainDB.timeFun.setDay(MainDB.denPlanDate.value.time)
        }
        Box() {
            Row {
                Column(
                    Modifier.weight(1f).padding(horizontal = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    timeSeekBar.show(
                        Modifier.fillMaxWidth().padding(bottom = 0.dp),
                        MainDB.styleParam.timeParam.seekBarStyle
                    )//weight(1f))
                    when (timeSeekBar.active) {
                        TimeTabsEnum.Plans -> planTab.show(dialLay)
                        TimeTabsEnum.DenPlans -> denPlans.show()
                        TimeTabsEnum.Vxod -> vxodTab.show()
                    }
                }
                Column {
                    Column(Modifier.weight(1f).padding(bottom = 5.dp)) {
                        MainDB.styleParam.timeParam.itemEffect.getComposable(::ItemEffectStyleState) { itemEffectStyle ->
                            MyList(MainDB.timeSpis.spisEffekt, Modifier.width(300.dp)) { ind, itemEffekt ->
                                ComItemEffekt(
                                    itemEffekt,
                                    selection,
                                    itemEffectStyleState = itemEffectStyle
                                ) { item, expanded ->
                                    DropdownMenuItem(onClick = {
                                        MainDB.timeSpis.spisPlan.getState().find {
                                            it.id.toLong() == item.idplan
                                        }?.let { itemPlan ->
                                            PanAddEffekt(dialLay, itemPlan)
                                        }
                                        expanded.value = false
                                    }) {
                                        Text(text = "Изменить", color = Color.White)
                                    }
                                    DropdownMenuItem(onClick = {
                                        MainDB.addTime.delEffekt(item.id.toLong())
                                        expanded.value = false
                                    }) {
                                        Text(text = "Удалить", color = Color.White)
                                    }

                                }.getComposable()
                            }
                        }
                    }
                    LaunchedEffect(MainDB.timeFun.currentDate.getState().value){
                        MainDB.timeSpis.spisSrokForPlanAndStap.getState().value?.let { listSrok ->
                            Date().time.longMinusTimeUTC().let { nowTime ->
                                with(MainDB.interfaceSpis.timeServiceParam) {
                                    if (listSrok.count { it.data1.unOffset() == nowTime } > 0 && nowTime != dateAlarmSrokUpdate.getValue()) {
                                        alarmSrokStart.itrObj.value = true
                                        dateAlarmSrokUpdate.itrObj.value = nowTime
                                    }
                                }
                            }
                        }
                    }
                    MainDB.timeSpis.spisSrokForPlanAndStap.getState().value?.let { listSrok ->
                        Date().time.longMinusTimeUTC().let { nowTime ->
                            with(TextButtonStyleState(MainDB.styleParam.timeParam.timelineCommonParam.buttTimeline)) {
                            val interactionSource = remember { MutableInteractionSource() }
                            val isHovered by interactionSource.collectIsHoveredAsState()

                            with(LocalDensity.current) {
                                MyShadowBox(
                                    shadow.copy(
                                        blurRadius = getElevation().elevation(
                                            true,
                                            interactionSource
                                        ).value.toPx()
                                    ),
                                    Modifier
                                ) {
                                    Box(
                                        Modifier
                                            .clip(shapeCard)
                                            .width(300.dp)
                                            .run{
                                                if (MainDB.interfaceSpis.timeServiceParam.alarmSrokStart.getValue()) this
                                                    .border(BorderStroke(borderWidth, MainDB.styleParam.timeParam.timelineCommonParam.borderButtTimeline.getValue()), shapeCard)
                                                    .background( MainDB.styleParam.timeParam.timelineCommonParam.backgroundButtTimeline.getValue(), shapeCard)
                                                else this
                                                    .border(BorderStroke(borderWidth, border), shapeCard)
                                                    .background(background, shapeCard)
                                            }
                                            .padding(vertical = 8.dp)
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null//rememberRipple(),
                                            ) {
                                                if (MainDB.interfaceSpis.timeServiceParam.alarmSrokStart.getValue()){
                                                    MainDB.interfaceSpis.timeServiceParam.alarmSrokStart.itrObj.value = false
                                                }
                                                panTimeline(dialLay)
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
//                                            Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                "Таймлайн",
                                                modifier = Modifier
                                                    .offset(
                                                        if (isHovered) offsetTextHover.x.dp else 0.dp,
                                                        if (isHovered) offsetTextHover.y.dp else 0.dp
                                                    )
                                                    .padding(0.dp),
                                                style = textStyle.copy(
                                                    fontSize = textStyle.fontSize,
                                                    textAlign = TextAlign.Center,
                                                    shadow = if (isHovered) textStyleShadowHover.shadow else textStyle.shadow
                                                )
                                            )
                                            RowVA {
                                                    with(MainDB.styleParam.timeParam.timelineCommonParam) {
                                                        textTimelineInfo.getValue().let { textInfo ->
                                                            Text(
                                                                listSrok.count { it.data1.unOffset() > nowTime }.toString(),
                                                                style = textInfo.copy(
                                                                    color = color_info_future.getValue().toColor()
                                                                )
                                                            )
                                                            Text(
                                                                listSrok.count { it.data2.unOffset() >= nowTime && it.data1.unOffset() <= nowTime }.toString(),
                                                                modifier = Modifier.padding(start = 10.dp),
                                                                style = textInfo.copy(
                                                                    color = color_info_present.getValue().toColor()
                                                                )
                                                            )
                                                            listSrok.count { it.data2.unOffset() < nowTime }.let{
                                                                if (it > 0) Text(
                                                                    it.toString(),
                                                                    modifier = Modifier.padding(start = 10.dp),
                                                                    style = textInfo.copy(
                                                                        color = color_info_past.getValue().toColor()
                                                                    )
                                                                )
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
                    }
                }
            }
        }
    }
}