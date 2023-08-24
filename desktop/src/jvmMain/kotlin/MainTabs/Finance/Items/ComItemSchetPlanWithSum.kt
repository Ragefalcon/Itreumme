package MainTabs.Finance.Items


import MainTabs.Time.Items.privSchetPlanInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.Text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import com.soywiz.korio.lang.substr
import common.MyShadowBox
import common.MyTextStyleParam
import common.PlateOrderLayout
import extensions.*
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemBindForSchplWithName
import ru.ragefalcon.sharedcode.models.data.ItemSchetPlanWithSum
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeBindElementForSchetPlan
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlan
import viewmodel.MainDB

@Composable
fun ComItemSchetPlanWithSum(
    item: ItemSchetPlanWithSum,
    bind: List<ItemBindForSchplWithName> = listOf(),
    style: ItemSchetPlanGrafState,
    onClick: (ItemSchetPlanWithSum) -> Unit = {}
) {

    with(style) {
        MyShadowBox(plateMain.shadow) {
            Column(outer_padding
                .run {
                    if (item.id == MainDB.CB_spisSchetPlan.getSelected()?.id?.toLong())
                        this.border(border_width_active, borderActive, plateMain.shape)
                    else this
                }
                .withSimplePlate(plateMain)
                .clickable { onClick(item) }
                .then(inner_padding)
                .fillMaxWidth(),
                horizontalAlignment = Alignment.Start) {
                Row(Modifier.padding(bottom = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        item.name,
                        style = textName,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = AnnotatedString(
                            text = item.summa.roundToStringProb(2).let { it.substr(0, it.length - 2) },
                            spanStyle = SpanStyle(fontSize = textSumm.fontSize)
                        ).plus(
                            AnnotatedString(
                                text = item.summa.roundToStringProb(2).let { it.substr(it.length - 2, 2) },
                                spanStyle = SpanStyle(fontSize = textSumm.fontSize * 0.7f)
                            )
                        ),
                        style = textSumm
                    )
                    Text(
                        modifier = Modifier.padding(start = 3.dp), //.align(Alignment.CenterVertically)
                        text = "RUB",
                        style = textValut
                    )
                }
//            MyTextStyle(item.summaStr,param = MyTextStyleParam.style3.copy( fontSize = 14.sp))
                val minus = item.summa < 0
                val width = if (minus) -item.procent else item.procent
                MyShadowBox(if (minus) platePolosMinus.shadow else platePolos.shadow) {
                    Box(
                        Modifier
                            .withSimplePlate(if (minus) platePolosMinus else platePolos)
                            .fillMaxWidth(width.toFloat())
                            .height(10.dp)
                    )
                }
                if (item.min_aim >= 0 || bind.isNotEmpty()) {
                    MyShadowBox(plateFinGoal.shadow){
                        Column(outer_padding_goal.withSimplePlate(plateFinGoal).then(inner_padding_goal)){
                            PlateOrderLayout(Modifier.padding(vertical = 0.dp)) {
                                bind.forEach {
                                    MyShadowBox(if (it.type == TypeBindElementForSchetPlan.GOAL) platePrivGoal.shadow else platePrivPlan.shadow) {
                                        Text(
                                            it.name,
                                            Modifier
                                                .padding(end = 10.dp)
                                                .padding(top = 5.dp)
                                                .withSimplePlate(if (it.type == TypeBindElementForSchetPlan.GOAL) platePrivGoal else platePrivPlan)
                                                .run {
                                                    when (it.stat) {
                                                        TypeStatPlan.COMPLETE.codValue -> {
                                                            if (it.type == TypeBindElementForSchetPlan.GOAL)
                                                                this.background(privGoalGotBrush, platePrivGoal.shape)
                                                            else this.background(privPlGotBrush, platePrivPlan.shape)
                                                        }
                                                        100L -> {
                                                            if (it.type == TypeBindElementForSchetPlan.GOAL)
                                                                this.background(privGoalGotBrush, platePrivGoal.shape)
                                                            else this.background(privPlGotBrush, platePrivPlan.shape)
                                                        }
                                                        else -> this
                                                    }
                                                }
                                                .padding(2.dp)
                                                .padding(horizontal = 6.dp),
                                            style = if (it.type == TypeBindElementForSchetPlan.GOAL) privGoalText else privPlanText
                                        )
                                    }
                                }
                            }
                            if (item.min_aim >= 0) privSchetPlanInfo(
                                item.summa,
                                item.min_aim,
                                item.max_aim,
                                item.summaRasxod,
                                true, //item.schplOpen
                                PrivSchetPlanInfoStyleState(MainDB.styleParam.finParam.schetParam.privSchetPlanInfoForSchetGraf)
                            )
                        }
                    }
                }
            }
        }
    }
}