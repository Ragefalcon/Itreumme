package common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import extensions.DropDownMenuStyleState
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.CommonInterfaceSetting
import viewmodel.MainDB
import java.util.*

class StatNabor constructor(private val list: List<Pair<Long, Color>>) : List<Pair<Long, Color>> by list {
    @Composable
    fun getIcon(iconRes: String, stat: Long, sizeIcon: Dp, modifier: Modifier, height: Dp? = null) = Image(
        painterResource(iconRes),
        "statDenPlan",
        modifier
            .width(sizeIcon)
            .height(height ?: sizeIcon),
        colorFilter = ColorFilter.tint(
            list.toMap()[stat] ?: MyColorARGB.colorStatTimeSquareTint_00.toColor(),
            BlendMode.Modulate
        ),
        contentScale = ContentScale.FillBounds,
    )

}

class MySelectStat(
    startStat: Long = 0L, val statNabor: List<Pair<Long, Color>> = statNabor1,
    val iconRes: String = "",
    val styleDropdownStart: CommonInterfaceSetting.MySettings.DropDownMenuStyleSetting = MainDB.styleParam.commonParam.commonDropdownMenuStyle,
) {
    companion object {
        val statNabor1 = StatNabor(
            listOf(
                0L to MyColorARGB.colorStatTint_01.toColor(),
                1L to MyColorARGB.colorStatTint_02.toColor(),
                2L to MyColorARGB.colorStatTint_03.toColor(),
                3L to MyColorARGB.colorStatTint_04.toColor(),
                4L to MyColorARGB.colorStatTint_05.toColor()
            )
        )
        val statNaborPlan = StatNabor(
            listOf(
                0L to MyColorARGB.colorStatTimeSquareTint_00.toColor(),
                1L to MyColorARGB.colorStatTimeSquareTint_01.toColor(),
                2L to MyColorARGB.colorStatTimeSquareTint_02.toColor(),
                3L to MyColorARGB.colorStatTimeSquareTint_03.toColor()
            )
        )
        val statNabor3 = StatNabor(
            listOf(
                0L to Color(0x58666666),
                1L to MyColorARGB.colorStatTimeSquareTint_00.toColor(),
                2L to MyColorARGB.colorStatTimeSquareTint_01.toColor(),
                3L to MyColorARGB.colorStatTimeSquareTint_02.toColor(),
                4L to MyColorARGB.colorStatTimeSquareTint_03.toColor()
            )
        )
    }

    val expandedStat = mutableStateOf(false)
    val timeExpanded = mutableStateOf(0L)

    fun openStat() {
        if (Date().time - timeExpanded.value > 100) expandedStat.value = true else expandedStat.value = false
    }

    private val statMut = mutableStateOf(startStat)

    var value
        get() = statMut.value
        set(value) {
            statMut.value = value
        }


    @Composable
    fun show(modifier: Modifier = Modifier) {
        Box {
            LaunchedEffect(expandedStat.value) {
                timeExpanded.value = Date().time
            }
            if (iconRes != "") {
                MyIconSimpleButt(iconRes, statNabor.find { it.first == statMut.value }?.second ?: Color(0xFFFFF42B)) {
                    openStat()

                }
            } else {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .padding(vertical = 2.dp)
                        .width(35.dp)
                        .height(35.dp)
                        .border(1.dp, MyColorARGB.colorMyBorderStrokeCommon.toColor(), RoundedCornerShape(5.dp))
                        .padding(3.dp)
                        .background(
                            statNabor.find { it.first == statMut.value }?.second ?: Color(0xFFFFF42B),
                            RoundedCornerShape(5.dp)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple()
                        ) {
                            openStat()
                        },
                )
            }
            MyDropdownMenu(expandedStat, DropDownMenuStyleState(styleDropdownStart)) {
                Row {
                    if (iconRes != "") {
                        for (i in statNabor) {
                            MyIconSimpleButt(
                                iconRes,
                                statNabor.find { it.first == i.first }?.second ?: Color(0xFFFFF42B)
                            ) {
                                statMut.value = i.first
                                expandedStat.value = false
                            }
                        }
                    } else {
                        for (i in statNabor) {
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 1.dp)
                                    .width(35.dp)
                                    .height(35.dp)
                                    .border(
                                        1.dp,
                                        MyColorARGB.colorMyBorderStrokeCommon.toColor(),
                                        RoundedCornerShape(5.dp)
                                    )
                                    .padding(3.dp)
                                    .background(
                                        statNabor.find { it.first == i.first }?.second ?: Color(0xFFFFF42B),
                                        RoundedCornerShape(5.dp)
                                    )
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = rememberRipple()
                                    ) {
                                        statMut.value = i.first
                                        expandedStat.value = false
                                    },
                            ) {
                            }
                        }
                    }
                }
            }
        }
    }
}

