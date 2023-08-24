package MainTabs.Time.Tabs

import MainTabs.Setting.Element.PanAddSchetPlan
import MainTabs.Time.Elements.PanAddEffekt
import MainTabs.Time.Elements.PanAddPlan
import MainTabs.Time.Elements.PanAddPlanStap
import MainTabs.Time.Elements.PanViewPlanHistory
import MainTabs.Time.Items.ComItemPlan
import MainTabs.Time.Items.ComItemStapPlan
import MainTabs.Time.Items.ComItemPlanPlate
import MainTabs.Time.Items.ComItemPlanStapPlate
import MyDialog.MyDialogLayout
import MyDialog.MyEmptyPanel
import MyList
import MyShowMessage
import adapters.MyComboBox
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.sharedcode.models.data.ItemPlanStap
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeBindElementForSchetPlan
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlan
import viewmodel.MainDB


/*
class PlanTab(val dialLay: MyDialogLayout) {

    private val selection = SingleSelectionType<ItemPlan>()
    private val selectionStap = SingleSelectionType<ItemPlanStap>()

    val listStatePlan: LazyListState = LazyListState(0)
    val listStateStap = MyLazyListState() //: LazyListState = LazyListState(0)
    var lastSelectPlan by mutableStateOf(-1L)
//    var changeScroll = false


    val vypPlan = mutableStateOf(false)
    val vypPlanStap = mutableStateOf(false)
    val openStapPan = mutableStateOf(false)

    @Composable
    private fun ColumnScope.dropMenuPlanStapItem(
        item: ItemPlanStap,
        expanded: MutableState<Boolean>,
    ) {
        if (item.quest_key_id == 0L) MyDropdownMenuItem(expanded, "Изменить") {
            selection.selected?.let { itemPlan ->
                PanAddPlanStap(dialLay, null, itemPlan, item)
            }
        }

        if (item.stat != TypeStatPlan.COMPLETE.codValue) MyDropdownMenuItem(expanded, "+ Подэтап") {
            selection.selected?.let { itemPlan ->
                PanAddPlanStap(dialLay, item, itemPlan)
            }
        }

        if (item.stat != TypeStatPlan.COMPLETE.codValue) {
            if (item.summa == null) DropdownMenuItem(onClick = {
                MyEmptyPanel(dialLay, false) { dialInner, closeFun ->
                    MyTextButtStyle1("Прикрепить существующий счет-план") {
                        closeFun()
                        MainDB.finSpis.spisSchetPlan.getState().value?.filter { it.id.toLong() != 1L }?.let { listSchpl ->

                            MyEmptyPanel(dialLay, false) { dialInner, closeFun ->
                                val CB_spisSchPl = remember { MyComboBox( listSchpl, nameItem = { it.name }) }
                                CB_spisSchPl.show()
                                Row {
                                    MyTextButtStyle1("Отмена") {
                                        closeFun()
                                    }
                                    CB_spisSchPl.getSelected()?.let { itemSchPlan ->
                                        MyTextButtStyle1("Добавить", Modifier.padding(start = 5.dp)) {
                                            MainDB.addFinFun.addBindForSchetPlan(
                                                TypeBindElementForSchetPlan.PLANSTAP,
                                                item.id.toLong(),
                                                itemSchPlan.id.toLong()
                                            )
                                            closeFun()
                                        }
                                    }
                                }
                            }
                        }
                    }
                    MyTextButtStyle1("Создать новый счет-план") {
                        closeFun()
                        PanAddSchetPlan(
                            dialLay,
                            null,
                            item.name,
                            Pair(TypeBindElementForSchetPlan.PLANSTAP, item.id.toLong())
                        )
                    }
                    MyTextButtStyle1("Отмена") {
                        closeFun()
                    }
                }
                expanded.value = false
            }) {
                Text(text = "Добавить фин. цель", color = Color.White)
            } else DropdownMenuItem(onClick = {
                MyEmptyPanel(dialLay, false) { dialInner, closeFun ->
                    if (item.schplOpen == true) MyTextButtStyle1("Удалить/закрыть счет-план") {
                        MainDB.addFinFun.delBindWithDelOrCloseSchetPlan(
                            TypeBindElementForSchetPlan.PLANSTAP,
                            item.id.toLong()
                        )
                        closeFun()
                    }
                    MyTextButtStyle1("Только отвязать счет-план") {
                        MainDB.addFinFun.deleteBindForSchetPlan(
                            TypeBindElementForSchetPlan.PLANSTAP,
                            item.id.toLong()
                        )
                        closeFun()
                    }
                    MyTextButtStyle1("Отмена") {
                        closeFun()
                    }
                }
                expanded.value = false
            }) {
                Text(text = "Убрать фин. цель", color = Color.White)
            }
        }

        DropdownMenuItem(onClick = {
            PanViewPlanHistory(dialLay, item)
            expanded.value = false
        }) {
            Text(text = "Хроника", color = Color.White)
        }

        MyCompleteDropdownMenuButton(expanded, item.stat == 10L) {
            MainDB.addTime.updStatPlanStap(
                item,
                if (item.stat != TypeStatPlan.COMPLETE.codValue) TypeStatPlan.COMPLETE.codValue else TypeStatPlan.VISIB.codValue
            )
            if (!vypPlanStap.value) selectionStap.selected = null
            if (item.schplOpen == true && item.stat != TypeStatPlan.COMPLETE.codValue) {
                MyEmptyPanel(dialLay, false) { dialInner, closeFun ->
                    Text("Что сделать со счетом-планом, который был привязан?", Modifier.padding(bottom = 10.dp), style = MyTextStyleParam.style2)
                    MyTextButtStyle1("Удалить/закрыть счет-план") {
                        MainDB.addFinFun.delBindWithDelOrCloseSchetPlan(
                            TypeBindElementForSchetPlan.PLANSTAP,
                            item.id.toLong()
                        )
                        closeFun()
                    }
                    MyTextButtStyle1("Отвязать счет-план") {
                        MainDB.addFinFun.deleteBindForSchetPlan(
                            TypeBindElementForSchetPlan.PLANSTAP,
                            item.id.toLong()
                        )
                        closeFun()
                    }
                    MyTextButtStyle1("Ничего не делать") {
                        closeFun()
                    }
                }
            }
        }

        MyDeleteDropdownMenuButton(expanded) {
            if (item.quest_key_id == 0L) {
                if (item.podstapcount > 0) {
                    MyShowMessage(dialLay, "Удалите вначале все подэтапы этого плана")
                } else {
                    if (item.schplOpen == true) {
                        MyEmptyPanel(dialLay, false) { dialInner, closeFun ->
                            Text("Что сделать со счетом-планом, который был привязан?\n(Отвязывание при удалении происходит автоматически)", Modifier.padding(bottom = 10.dp), style = MyTextStyleParam.style2)
                            MyTextButtStyle1("Удалить/закрыть счет-план") {
                                MainDB.addFinFun.delBindWithDelOrCloseSchetPlan(
                                    TypeBindElementForSchetPlan.PLANSTAP,
                                    item.id.toLong()
                                )
                                MainDB.addTime.delPlanStap(item.id.toLong())
                                closeFun()
                            }
                            MyTextButtStyle1("Ничего не делать") {
                                MainDB.addTime.delPlanStap(item.id.toLong())
                                closeFun()
                            }
                        }
                    }   else {
                        MainDB.addTime.delPlanStap(item.id.toLong())
                    }
                }
            } else {
                MyShowMessage(
                    dialLay,
                    "Этот элемент из квеста, его можно удалить только вместе с квестом."
                )
            }
        }

    }
    @Composable
    private fun ColumnScope.dropMenuPlanItem(
        item: ItemPlan,
        expanded: MutableState<Boolean>,
        setDissFun: (() -> Unit) -> Unit
    ) {

        if (item.quest_key_id == 0L) DropdownMenuItem(onClick = {
            PanAddPlan(dialLay, item)
            expanded.value = false
        }) {
            Text(text = "Изменить", color = Color.White)
        }

        if (item.stat != TypeStatPlan.COMPLETE.codValue) {
            if (item.summa == null) DropdownMenuItem(onClick = {
                MyEmptyPanel(dialLay, false) { dialInner, closeFun ->
                    MyTextButtStyle1("Прикрепить существующий счет-план") {
                        closeFun()
                        MainDB.finSpis.spisSchetPlan.getState().value?.filter { it.id.toLong() != 1L }?.let { listSchpl ->
                            MyEmptyPanel(dialLay, false) { dialInner, closeFun ->
                                val CB_spisSchPl =
                                    remember { MyComboBox(listSchpl, nameItem = { it.name }) }
                                CB_spisSchPl.show()
                                Row {
                                    MyTextButtStyle1("Отмена") {
                                        closeFun()
                                    }
                                    CB_spisSchPl.getSelected()?.let { itemSchPlan ->
                                        MyTextButtStyle1("Добавить", Modifier.padding(start = 5.dp)) {
                                            MainDB.addFinFun.addBindForSchetPlan(
                                                TypeBindElementForSchetPlan.PLAN,
                                                item.id.toLong(),
                                                itemSchPlan.id.toLong()
                                            )
                                            closeFun()
                                        }
                                    }
                                }
                            }
                        }
                    }
                    MyTextButtStyle1("Создать новый счет-план") {
                        closeFun()
                        PanAddSchetPlan(
                            dialLay,
                            null,
                            item.name,
                            Pair(TypeBindElementForSchetPlan.PLAN, item.id.toLong())
                        )
                    }
                    MyTextButtStyle1("Отмена") {
                        closeFun()
                    }
                }
                expanded.value = false
            }) {
                Text(text = "Добавить фин. цель", color = Color.White)
            } else DropdownMenuItem(onClick = {
                MyEmptyPanel(dialLay, false) { dialInner, closeFun ->
                    if (item.schplOpen == true) MyTextButtStyle1("Удалить/закрыть счет-план") {
                        MainDB.addFinFun.delBindWithDelOrCloseSchetPlan(
                            TypeBindElementForSchetPlan.PLAN,
                            item.id.toLong()
                        )
                        closeFun()
                    }
                    MyTextButtStyle1("Только отвязать счет-план") {
                        MainDB.addFinFun.deleteBindForSchetPlan(
                            TypeBindElementForSchetPlan.PLAN,
                            item.id.toLong()
                        )
                        closeFun()
                    }
                    MyTextButtStyle1("Отмена") {
                        closeFun()
                    }
                }
                expanded.value = false
            }) {
                Text(text = "Убрать фин. цель", color = Color.White)
            }
        }

        DropdownMenuItem(onClick = {
            PanViewPlanHistory(dialLay, item)
            expanded.value = false
        }) {
            Text(text = "Хроника", color = Color.White)
        }

        if (item.stat != TypeStatPlan.COMPLETE.codValue) DropdownMenuItem(onClick = {
            PanAddEffekt(dialLay, item)
            expanded.value = false
        }) {
            Text(text = "Эффективность", color = Color.White)
        }

        MyCompleteDropdownMenuButton(expanded, item.stat == TypeStatPlan.COMPLETE.codValue) {
            MainDB.addTime.updStatPlan(
                item,
                if (item.stat != TypeStatPlan.COMPLETE.codValue) TypeStatPlan.COMPLETE.codValue else TypeStatPlan.VISIB.codValue
            )
            if (!vypPlan.value) selection.selected = null
            if (item.schplOpen == true && item.stat != TypeStatPlan.COMPLETE.codValue) {
                MyEmptyPanel(dialLay, false) { dialInner, closeFun ->
                    Text("Что сделать со счетом-планом, который был привязан?", Modifier.padding(bottom = 10.dp), style = MyTextStyleParam.style2)
                    MyTextButtStyle1("Удалить/закрыть счет-план") {
                        MainDB.addFinFun.delBindWithDelOrCloseSchetPlan(
                            TypeBindElementForSchetPlan.PLAN,
                            item.id.toLong()
                        )
                        closeFun()
                    }
                    MyTextButtStyle1("Отвязать счет-план") {
                        MainDB.addFinFun.deleteBindForSchetPlan(
                            TypeBindElementForSchetPlan.PLAN,
                            item.id.toLong()
                        )
                        closeFun()
                    }
                    MyTextButtStyle1("Ничего не делать") {
                        closeFun()
                    }
                }
            }
        }

        MyDeleteDropdownMenuButton(expanded, setDissFun) {
            if (item.quest_key_id == 0L) {
                if (item.countstap > 0) {
                    MyShowMessage(dialLay, "Удалите вначале все этапы этого плана")
                } else {
//                    println(item.id)
                    if (item.schplOpen == true) {
                        MyEmptyPanel(dialLay, false) { dialInner, closeFun ->
                            Text("Что сделать со счетом-планом, который был привязан?\n(Отвязывание при удалении происходит автоматически)", Modifier.padding(bottom = 10.dp), style = MyTextStyleParam.style2)
                            MyTextButtStyle1("Удалить/закрыть счет-план") {
                                MainDB.addFinFun.delBindWithDelOrCloseSchetPlan(
                                    TypeBindElementForSchetPlan.PLAN,
                                    item.id.toLong()
                                )
                                MainDB.addTime.delPlan(item.id.toLong())
                                closeFun()
                            }
                            MyTextButtStyle1("Ничего не делать") {
                                MainDB.addTime.delPlan(item.id.toLong())
                                closeFun()
                            }
                        }
                    }   else {
                        MainDB.addTime.delPlan(item.id.toLong())
                    }
                }
            } else {
                MyShowMessage(
                    dialLay,
                    "Этот элемент из квеста, его можно удалить только вместе с квестом."
                )
            }
        }
    }

    @Composable
    private fun spisPlan(scope: ColumnScope) {
//        val listStatePlan: LazyListState = rememberLazyListState(0)
        scope.apply {
            MainDB.styleParam.timeParam.planTab.itemPlan.getComposable(::ItemPlanStyleState) { itemPlanStyle ->
                MyList(
                    MainDB.timeSpis.spisPlan,
                    modifier = Modifier.padding(horizontal = 10.dp).padding(bottom = 10.dp).weight(1f),
                    listStatePlan
                ) { ind, itemPlan ->
                    if (itemPlan.stat != TypeStatPlan.INVIS.codValue) {
                        if (itemPlan.stat == TypeStatPlan.BLOCK.codValue) ComItemPlanPlate(itemPlan,
                            itemPlanStyleState = ItemPlanStyleState(MainDB.styleParam.timeParam.planTab.itemPlan)) else
                            ComItemPlan(itemPlan, selection, selFun = {
//                            changeScroll = true
//                            keyEffect.value = keyEffect.value.not()
                            }, openStaps = {
                                openStapPan.value = true
                            },
                                changeGotov = { item, progress ->
                                    MainDB.addTime.updGotovPlan(item.id.toLong(), progress.toDouble() * 100)
                                }, itemPlanStyleState = itemPlanStyle) { item, expanded, setDissFun ->
                                dropMenuPlanItem(item, expanded, setDissFun)
                            }
                    }
                }
            }
        }
    }


    @Composable
    fun show() {
        LaunchedEffect(selection.selected) {
            selection.selected?.let {
                println("Plan selection.selected?.let {")
                if (lastSelectPlan != it.id.toLong() ) listStateStap.scrollToZero()
                MainDB.timeFun.setPlanForSpisStapPlan(it.id.toLong())
                MainDB.timeFun.setPlanForCountStapPlan(it.id.toLong())
                MainDB.timeFun.setOpenSpisStapPlan(vypPlanStap.value)
                lastSelectPlan = it.id.toLong()
            }
        }

//        val scrollStateStap = rememberLazyListState(0)
        listStateStap.launchEffect()
*/
/*
        LaunchedEffect(lastSelectPlan) {
//            rememberCoroutineScope().launch {
            if (changeScroll) listStateStap.scrollToItem(0, 0)
            changeScroll = false
//            }

        }
*//*

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (openStapPan.value) {
                selection.selected?.let {
                    ComItemPlanPlate(it, modifier = Modifier.padding(top = 10.dp),
                        itemPlanStyleState = ItemPlanStyleState(MainDB.styleParam.timeParam.planTab.itemPlan)) {
                        openStapPan.value = false
                    }
                }
            } else Row(modifier = Modifier.padding(top = 0.dp).padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                MyToggleButtIconStyle1(
                    "ic_round_check_circle_outline_24.xml",
                    "ic_round_check_circle_24.xml",
                    twoIcon = false,
                    value = vypPlan,
                    sizeIcon = 35.dp,
                    modifier = Modifier.height(35.dp).padding(start = 15.dp),
                    width = 70.dp,
                    myStyleToggleButton = ToggleButtonStyleState(MainDB.styleParam.timeParam.planTab.buttVypPlan)
                ) {
//                MyTextToggleButtStyle1("Вып", vypPlan,
//                    modifier = Modifier.padding(start = 15.dp),
//                    height = 45.dp, myStyleToggleButton = ToggleTextButtonStyleState(MainDB.styleParam.timeParam.planTab.buttVypPlan)) {
                    MainDB.timeFun.setOpenSpisPlan(it)
                    selection.selected?.let {
                        if (it.stat == 10L) selection.selected = null
                    }
                }
                Text(
                    "Количесто планов: ${MainDB.timeSpis.spisPlan.getState().value?.size ?: 0}",
                    Modifier.weight(1f).padding(horizontal = 20.dp),
                    style = MainDB.styleParam.timeParam.planTab.textCountPlan.getValue()
                )
                MyTextButtStyle1("+", modifier = Modifier.padding(end = 15.dp), width = 70.dp, height = 35.dp, myStyleTextButton = TextButtonStyleState(MainDB.styleParam.timeParam.planTab.buttAddPlan)) {
                    PanAddPlan(dialLay)
                }
            }
            spisPlan(this)
            selection.selected?.let {
                with(SimplePlateStyleState(MainDB.styleParam.timeParam.planTab.panelPlanStap)) {
                    Column(
                        modifier = Modifier.padding(bottom = 5.dp).animateContentSize()
                            .shadow(elevation = ELEVATION, shape.shadow)
                            .border(BorderStroke(BORDER_WIDTH, BORDER), shape.main)
                            .background(BACKGROUND, shape.main)
*/
/*
                            .border(
                                width = 1.dp,
                                brush = Brush.horizontalGradient(listOf(Color(0xFFFFF7D9), Color(0xFFFFF7D9))),
                                shape = RoundedCornerShape(10.dp)
                            ).background(
                                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                color = Color(0xFFE4E0C7),
                            )
*//*

                    ) {
                        Row(modifier = Modifier.clickable (interactionSource = remember { MutableInteractionSource() },indication = null) {

                            openStapPan.value = true
                        }, verticalAlignment = Alignment.CenterVertically) {
                            MyToggleButtIconStyle1(
                                "ic_round_check_circle_outline_24.xml",
                                "ic_round_check_circle_24.xml",
                                twoIcon = false,
                                value = vypPlanStap,
                                sizeIcon = 35.dp,
                                modifier = Modifier.height(35.dp).padding(start = 15.dp),
                                width = 70.dp,
                                myStyleToggleButton = ToggleButtonStyleState(MainDB.styleParam.timeParam.planTab.buttVypPlanStap)
                            ) {
                                MainDB.timeFun.setOpenSpisStapPlan(it)
                                selectionStap.selected?.let {
                                    if (it.stat == 10L) selectionStap.selected = null
                                }
                            }
                            Text(
                                "Количесто этапов: ${MainDB.timeSpis.countStapPlan.getState().value ?: 0L}",
                                Modifier.weight(1f)
                                    .padding(horizontal = 20.dp),
//                                color = Color(0xFF464D45),
                                style = MainDB.styleParam.timeParam.planTab.textCountPlanStap.getValue()
                            )
                            MyTextButtStyle1(
                                "+",
                                modifier = Modifier.padding(end = 15.dp), width = 70.dp, height = 35.dp,
                                myStyleTextButton = TextButtonStyleState(MainDB.styleParam.timeParam.planTab.buttAddPlanStap)
                            ) {
                                selection.selected?.let {
                                    PanAddPlanStap(dialLay, null, it)
                                }
                            }
                        }
                        if (openStapPan.value)
//                        BoxWithVScrollBarLazyList(Modifier.padding(10.dp).weight(1f), scrollStateStap, dark = true) { scrollState ->

//                            LazyColumn(Modifier.fillMaxHeight().padding(bottom = 10.dp), scrollState) {

//                                if ((MainDB.spisStapPlans.getState().value?.size ?: 0) > 0) {
//                                    MyListItems(MainDB.spisStapPlans, scope = this) { itemPlanStap ->
                            MainDB.styleParam.timeParam.planTab.itemPlanStap.getComposable(::ItemPlanStapStyleState) { itemPlanStapStyle ->
                                MyList(
                                    MainDB.timeSpis.spisPlanStap,
                                    Modifier.padding(horizontal = 10.dp, vertical = 0.dp).weight(1f).padding(bottom = 10.dp),
                                    listStateStap.listState,
                                    darkScroll = true
                                ) { ind, itemPlanStap ->
                                    if (itemPlanStap.stat != TypeStatPlan.INVIS.codValue) {
                                        if (itemPlanStap.stat == TypeStatPlan.BLOCK.codValue) ComItemPlanStapPlate(
                                            itemPlanStap,
                                            itemPlanStapStyleState = ItemPlanStapStyleState(MainDB.styleParam.timeParam.planTab.itemPlanStap)
                                        ) else
                                            ComItemStapPlan(itemPlanStap, selectionStap, sverFun = {
                                                MainDB.timeFun.setExpandStapPlan(it.id.toLong(), it.svernut.not())
                                            }, changeGotov = { item, progress ->
                                                MainDB.addTime.updGotovPlanStap(
                                                    item.id.toLong(),
                                                    progress.toDouble() * 100
                                                )
                                            }, itemPlanStapStyleState = itemPlanStapStyle) { item, expanded ->
                                                dropMenuPlanStapItem(item, expanded)
                                            }.getComposable()
                                    }
                                }
                            }
                    }
                }
            }
        }
    }
}
*/
