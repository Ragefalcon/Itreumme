package MainTabs.Setting.Items


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.RowVA
import extensions.toColor
import extensions.toMyColorARGB
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemBindForSchplWithName
import ru.ragefalcon.sharedcode.models.data.ItemSettSchetPlan
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeBindElementForSchetPlan
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlan

class ComItemSchetPlanSett(
    val item: ItemSettSchetPlan,
    val listTypeRasx: List<String>,
    val selection: SingleSelectionType<ItemSettSchetPlan>,
    val bind: List<ItemBindForSchplWithName> = listOf(),
    val dropMenu: @Composable ColumnScope.(ItemSettSchetPlan, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    var expandedDropMenu = mutableStateOf(false)
    val expandedOpis = mutableStateOf(false)
    var opisType: String = ""
    val first by lazy {
        var i = 0
        for (type in listTypeRasx) {
            if (i != 0) opisType += ", "//\n"
            opisType += type
            i++
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun getComposable() {
        first
        MyCardStyle1(selection.isActive(item), 0, {
            selection.selected = item
//            expandedDropMenu.value = this.buttons.isSecondaryPressed
        },{
            if (listTypeRasx.isNotEmpty()) expandedOpis.value = !expandedOpis.value
        }, backColor = if (item.open_ != 1L)
            Color.Red.toMyColorARGB().plusWhite().plusWhite().toColor().copy(alpha = 0.7f)// Color(0xFF468F45)
        else
            Color(0xFF464D45),
            dropMenu = { exp -> dropMenu(item, exp) }

        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.padding(5.dp).padding(start = 15.dp, end = 10.dp).weight(1f).animateContentSize()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

//                        Column(Modifier.padding(0.dp).weight(1f)) {
                        Text(
                            modifier = Modifier.padding(bottom = 2.dp).weight(1f),
                            text = item.name,
                            style = MyTextStyleParam.style2
                        )
                        if (selection.isActive(item)) MyButtDropdownMenuStyle2(
                            Modifier.padding(end = 5.dp).padding(vertical = 0.dp),
                            expandedDropMenu
                        ) {
                            dropMenu(item, expandedDropMenu)
                        }
                        if (listTypeRasx.isNotEmpty()) RotationButtStyle1(
                            expandedOpis,
                            Modifier.padding(start = 10.dp, end = 10.dp)
                        ) {
//                            item.sver = item.sver.not()
                        }
                        Text(
                            modifier = Modifier,
                            text = item.summa.roundToStringProb(2),
                            style = MyTextStyleParam.style2.copy(
                                color = Color.Green.toMyColorARGB().plusWhite().toColor(),
                                fontSize = 16.sp
                            )
                        )
                    }
                    bind.forEach{
                        Text(
                            it.name,
                            Modifier
                                .padding(horizontal = 5.dp)
                                .padding(top = 5.dp)
                                .background(
                                    when (it.stat) {
                                        TypeStatPlan.COMPLETE.codValue -> Color.Green.copy(0.3f)
                                        else -> Color.Transparent
                                    },
                                    shape = RoundedCornerShape(if (it.type != TypeBindElementForSchetPlan.GOAL) 5.dp else 0.dp)
                                )
                                .border(
                                    width = 0.5.dp,
                                    brush = Brush.horizontalGradient(
                                        if (it.type != TypeBindElementForSchetPlan.GOAL)
                                            listOf(Color(0x4FFFF7D9), Color(0x4FFFF7D9))
                                        else
                                            listOf(Color.Yellow, Color.Yellow)
                                    ),
                                    shape = RoundedCornerShape(if (it.type != TypeBindElementForSchetPlan.GOAL) 5.dp else 0.dp)
                                )
                                .padding(2.dp)
                                .padding(horizontal = 2.dp),
                            style = if (it.type != TypeBindElementForSchetPlan.GOAL) MyTextStyleParam.style5 else MyTextStyleParam.style5.copy(
                                color = Color.Yellow
                            )
                        )
                    }
                    if (item.min_aim >= 0) {
                        var maxS = if (item.max_aim > 0) item.max_aim else item.min_aim
                        if (maxS == 0.0) maxS = 1.0
                        val rasxod_proc_aim =
                            if (item.summaRasxod < 0) 0.0 else if (item.summaRasxod > maxS) 1.0 else item.summaRasxod / maxS
                        val proc_aim = if (item.summa < 0) 0.0 else if (item.summa > maxS) 1.0 else item.summa / maxS
                        val pererasxod = item.summaRasxod - maxS
                        var ostatok = if (pererasxod > 0) -item.summa else maxS - item.summa - item.summaRasxod
                        if (ostatok < 0) {
                            ostatok = 0.0
                        }
//                Row(Modifier.padding(vertical = 3.dp).alpha(0.7f), verticalAlignment = Alignment.CenterVertically) {
                        PlateOrderLayout(Modifier.padding(vertical = 3.dp).alpha(0.7f)) {
                            RowVA(Modifier.padding(end = 10.dp)) {
                                Text(
                                    modifier = Modifier,
                                    text = "Цель:  ",
                                    style = MyTextStyleParam.style2.copy(
                                        fontSize = 12.sp
                                    )
                                )
//                        Spacer(Modifier.weight(1f))
                                if (item.min_aim != -1.0) Text(
                                    modifier = Modifier,
                                    text = item.min_aim.roundToStringProb(2),
                                    style = MyTextStyleParam.style2.copy(
                                        color = Color.Green.toMyColorARGB().plusWhite().toColor(),
                                        fontSize = 12.sp
                                    )
                                )
                                if (item.min_aim != -1.0 && item.max_aim != -1.0) Text(
                                    modifier = Modifier,
                                    text = " - ",
                                    style = MyTextStyleParam.style2.copy(
                                        fontSize = 12.sp
                                    )
                                )
                                if (item.max_aim != -1.0) Text(
                                    modifier = Modifier,
                                    text = item.max_aim.roundToStringProb(2),
                                    style = MyTextStyleParam.style2.copy(
                                        color = Color.Green.toMyColorARGB().plusWhite().toColor(),
                                        fontSize = 12.sp
                                    )
                                )
                            }
                            if (item.min_aim != -1.0) RowVA(Modifier.padding(end = 10.dp)) {
                                Text(
                                    modifier = Modifier,
                                    text = "Осталось:  ",
                                    style = MyTextStyleParam.style2.copy(
                                        fontSize = 12.sp
                                    )
                                )
                                Text(
                                    modifier = Modifier,
                                    text = "${if ((pererasxod > 0 && ostatok > 0) || ostatok > maxS) "+ " else ""}${
                                        ostatok.roundToStringProb(
                                            2
                                        )
                                    }",
                                    style = MyTextStyleParam.style2.copy(
                                        color = (if ((pererasxod > 0 && ostatok > 0) || ostatok > maxS) Color.Red else Color.Green).toMyColorARGB()
                                            .plusWhite(1.3f).toColor(),
                                        fontSize = 12.sp
                                    )
                                )
                            }
                            if (pererasxod > 0 && item.min_aim != -1.0) RowVA {
                                Text(
                                    modifier = Modifier,
                                    text = "Перерасход:  ",
                                    style = MyTextStyleParam.style2.copy(
                                        fontSize = 12.sp
                                    )
                                )
                                Text(
                                    modifier = Modifier,
                                    text = pererasxod.roundToStringProb(2),
                                    style = MyTextStyleParam.style2.copy(
                                        color =  Color.Red.toMyColorARGB().plusWhite(1.3f).toColor(),
                                        fontSize = 12.sp
                                    )
                                )
                                Text(
                                    modifier = Modifier,
                                    text = " из ",
                                    style = MyTextStyleParam.style2.copy(
                                        fontSize = 12.sp
                                    )
                                )
                                Text(
                                    modifier = Modifier,
                                    text = item.summaRasxod.roundToStringProb(2),
                                    style = MyTextStyleParam.style2.copy(
                                        color =  Color.Yellow.toMyColorARGB().plusWhite().toColor(),
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                        BoxWithConstraints(
                            Modifier//.padding(top = 5.dp)
                                .shadow(5.dp, shape = RoundedCornerShape(5.dp))
                                .background(
                                    color = if (item.max_aim > 0) Color.DarkGray
                                    else Color.Gray,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .fillMaxWidth()
                                .height(15.dp)
                                .clip(RoundedCornerShape(5.dp))
                        ) {
                            if (item.max_aim > 0 && item.min_aim < item.max_aim) Box(
                                Modifier
//                            .shadow(5.dp, shape = RoundedCornerShape(5.dp))
                                    .background(
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(5.dp)
                                    )
                                    .fillMaxWidth((item.min_aim / item.max_aim).toFloat())
                                    .fillMaxHeight()
                            )
                            Row {
                                Box(
                                    Modifier
//                            .shadow(5.dp, shape = RoundedCornerShape(5.dp))
                                        .background(
                                            color = Color.Yellow.copy(0.5f),
                                        )
                                        .width(this@BoxWithConstraints.maxWidth * rasxod_proc_aim.toFloat())
                                        .fillMaxHeight()
                                ) {
                                    if (item.summa < 0) {
                                        val procMinus =
                                            if (-item.summa > item.summaRasxod) 1f else -item.summa / item.summaRasxod.toFloat()
                                        Box(
                                            Modifier
                                                .background(
                                                    color = Color.Red.copy(0.5f),
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
                                            color = Color.Green.copy(0.5f),
                                        )
                                        .width(this@BoxWithConstraints.maxWidth * proc_aim.toFloat())
//                                .fillMaxWidth(proc_aim.toFloat())
                                        .fillMaxHeight()
                                )
                            }
                            RowVA(modifier = Modifier.align(Alignment.Center)) {
                                Text(
                                    text = "${((item.summa + item.summaRasxod) / maxS * 100.0).roundToStringProb(1)} %",
                                    style = MyTextStyleParam.style2.copy(
                                        fontSize = 12.sp
                                    )
                                )
                                Text(
                                    text = " (${(item.summaRasxod / maxS * 100.0).roundToStringProb(1)} %)",
                                    style = MyTextStyleParam.style2.copy(
                                        fontSize = 12.sp
                                    )
                                )
                            }

                        }
                    }
                    if (expandedOpis.value) { //(isActive()) &&
                        BoxExpand(
                            expandedOpis,
                            Modifier.myModWithBound1(),
                            Modifier.fillMaxWidth()
                        ) {  //, endModif = Modifier::withMyBound1
                            Text(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .padding(start = 10.dp),
                                text = opisType,
                                style = TextStyle(color = Color(0xFFFFF7D9)),
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}


