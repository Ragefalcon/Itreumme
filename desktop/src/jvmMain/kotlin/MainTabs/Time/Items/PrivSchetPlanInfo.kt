package MainTabs.Time.Items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import common.MyShadowBox
import common.PlateOrderLayout
import extensions.PrivSchetPlanInfoStyleState
import extensions.RowVA
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import viewmodel.StateVM

@Composable
fun privSchetPlanInfo(
    summav: Double?,
    min_aimv: Double?,
    max_aimv: Double?,
    summaRasxodv: Double?,
    schplOpen: Boolean?,
    style: PrivSchetPlanInfoStyleState = StateVM.commonPrivSchetPlanInfo.value,
    modifier: Modifier = Modifier
) {
    summav?.let { summa ->
        min_aimv?.let { min_aim ->
            max_aimv?.let { max_aim ->
                summaRasxodv?.let { summaRasxod ->
                    with(style) {
                        Column(modifier) {
                            if (min_aim >= 0) {
                                var maxS = if (max_aim > 0) max_aim else min_aim
                                if (maxS == 0.0) maxS = 1.0
                                val rasxod_proc_aim =
                                    if (summaRasxod < 0) 0.0 else if (summaRasxod > maxS) 1.0 else summaRasxod / maxS
                                val proc_aim =
                                    if (summa < 0) 0.0 else if (summa > maxS) 1.0 else summa / maxS
                                val pererasxod = summaRasxod - maxS
                                var ostatok = if (pererasxod > 0) -summa else maxS - summa - summaRasxod
                                if (ostatok < 0) {
                                    ostatok = 0.0
                                }
                                PlateOrderLayout(
                                    Modifier.padding(vertical = 3.dp).alpha(if (schplOpen != false) 0.7f else 0.2f)
                                ) {
                                    RowVA(Modifier.padding(end = 10.dp)) {
                                        Text(text = "Цель:  ", style = textStyle)
                                        if (min_aim != -1.0) Text(
                                            text = min_aim.roundToStringProb(2),
                                            style = textStyle.copy(color = color_t_goal)
                                        )
                                        if (min_aim != -1.0 && max_aim != -1.0) Text(
                                            text = " - ",
                                            style = textStyle
                                        )
                                        if (max_aim != -1.0) Text(
                                            text = max_aim.roundToStringProb(2),
                                            style = textStyle.copy(color = color_t_goal)
                                        )
                                    }
                                    if (min_aim != -1.0) RowVA(Modifier.padding(end = 10.dp)) {
                                        Text(text = "Осталось:  ", style = textStyle)
                                        Text(
                                            text = "${if ((pererasxod > 0 && ostatok > 0) || ostatok > maxS) "+ " else ""}${
                                                ostatok.roundToStringProb(
                                                    2
                                                )
                                            }",
                                            style = textStyle.copy(
                                                color = if ((pererasxod > 0 && ostatok > 0) || ostatok > maxS) color_t_perer else color_t_ostat,
                                            )
                                        )
                                    }
                                    if (pererasxod > 0 && min_aim != -1.0) RowVA {
                                        Text(
                                            text = "Перерасход:  ",
                                            style = textStyle
                                        )
                                        Text(
                                            text = pererasxod.roundToStringProb(2),
                                            style = textStyle.copy(color = color_t_perer)
                                        )
                                        Text(
                                            text = " из ",
                                            style = textStyle
                                        )
                                        Text(
                                            text = summaRasxod.roundToStringProb(2),
                                            style = textStyle.copy(color = color_t_iz)
                                        )
                                    }
                                }
                                MyShadowBox(shadow = shadow){
                                    BoxWithConstraints(
                                        Modifier//.padding(top = 5.dp)
                                            .background(
                                                color = if (max_aim > 0) color_b_max_back
                                                else color_b_min_back,
                                                shape = shapeBox
                                            )
                                            .border(borderWidth, borderBrush, shapeBox)
                                            .fillMaxWidth()
                                            .height(15.dp)
                                            .clip(shapeBox)
                                            .alpha(if (schplOpen != false) 1f else 0.2f)
                                    ) {
                                        if (max_aim > 0 && min_aim < max_aim) Box(
                                            Modifier
//                            .shadow(5.dp, shape = RoundedCornerShape(5.dp))
                                                .background(
                                                    color = color_b_min_back,
                                                    shape = shapeBox
                                                )
                                                .fillMaxWidth((min_aim / max_aim).toFloat())
                                                .fillMaxHeight()
                                        )
                                        Row {
                                            Box(
                                                Modifier
//                            .shadow(5.dp, shape = RoundedCornerShape(5.dp))
                                                    .background(
                                                        color = color_b_rasxod,
                                                    )
                                                    .width(this@BoxWithConstraints.maxWidth * rasxod_proc_aim.toFloat())
                                                    .fillMaxHeight()
                                            ) {
                                                if (summa < 0) {
                                                    val procMinus =
                                                        if (-summa > summaRasxod) 1f else -summa / summaRasxod.toFloat()
                                                    Box(
                                                        Modifier
                                                            .background(
                                                                color = color_b_rasxod2,
                                                            )
                                                            .fillMaxWidth(procMinus.toFloat())
                                                            .fillMaxHeight()
                                                            .align(Alignment.CenterEnd)
                                                    )
                                                }
                                            }
                                            if (proc_aim > 0.0) Box(
                                                Modifier
//                            .shadow(5.dp, shape = RoundedCornerShape(5.dp))
                                                    .background(
                                                        color = color_b_doxod,
                                                    )
                                                    .width(this@BoxWithConstraints.maxWidth * proc_aim.toFloat())
//                                .fillMaxWidth(proc_aim.toFloat())
                                                    .fillMaxHeight()
                                            )
                                        }
                                        RowVA(modifier = Modifier.align(Alignment.Center)) {
                                            Text(
                                                text = "${
                                                    ((summa + summaRasxod) / maxS * 100.0).roundToStringProb(
                                                        1
                                                    )
                                                } %",
                                                style = textStyleInBox
                                            )
                                            Text(
                                                text = " (${(summaRasxod / maxS * 100.0).roundToStringProb(1)} %)",
                                                style = textStyleInBox2
                                            )
                                        }

                                    }
                                }
                            } else {
                                var maxS = maxOf(summaRasxod, summaRasxod + summa)
                                if (maxS == 0.0) maxS = 1.0
                                val rasxod_proc_aim =
                                    if (summaRasxod < 0) 0.0 else if (summaRasxod > maxS) 1.0 else summaRasxod / maxS
                                val proc_aim =
                                    if (summa < 0) 0.0 else if (summa > maxS) 1.0 else summa / maxS
                                val pererasxod = summaRasxod - maxS
                                var ostatok = if (pererasxod > 0) -summa else maxS - summa - summaRasxod
                                if (ostatok < 0) {
                                    ostatok = 0.0
                                }
                                MyShadowBox(shadow = shadow){
                                    BoxWithConstraints(
                                        Modifier
                                            .padding(top = 5.dp)
                                            .background(
                                                color = color_b_min_back,
                                                shape = shapeBox
                                            )
                                            .border(borderWidth, borderBrush, shapeBox)
                                            .fillMaxWidth()
                                            .height(15.dp)
                                            .clip(shapeBox)
                                            .alpha(if (schplOpen != false) 1f else 0.2f)
                                    ) {
                                        Row {
                                            Box(
                                                Modifier
//                            .shadow(5.dp, shape = RoundedCornerShape(5.dp))
                                                    .background(
                                                        color = color_b_rasxod,
                                                    )
                                                    .width(this@BoxWithConstraints.maxWidth * rasxod_proc_aim.toFloat())
                                                    .fillMaxHeight()
                                            ) {
                                                if (summa < 0) {
                                                    val procMinus =
                                                        if (-summa > summaRasxod) 1f else -summa / summaRasxod.toFloat()
                                                    Box(
                                                        Modifier
                                                            .background(
                                                                color = color_b_rasxod2,
                                                            )
                                                            .fillMaxWidth(procMinus.toFloat())
                                                            .fillMaxHeight()
                                                            .align(Alignment.CenterEnd)
                                                    )
                                                }
                                            }
                                            if (proc_aim > 0.0) Box(
                                                Modifier
//                            .shadow(5.dp, shape = RoundedCornerShape(5.dp))
                                                    .background(
                                                        color = color_b_doxod,
                                                    )
                                                    .width(this@BoxWithConstraints.maxWidth * proc_aim.toFloat())
//                                .fillMaxWidth(proc_aim.toFloat())
                                                    .fillMaxHeight()
                                            )
                                        }
                                        RowVA(modifier = Modifier.align(Alignment.Center)) {
                                            Text(
                                                text = summa.roundToStringProb(2),
                                                style = textStyleInBox
                                            )
                                            Text(
                                                text = " (${summaRasxod.roundToStringProb(2)})",
                                                style = textStyleInBox2
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