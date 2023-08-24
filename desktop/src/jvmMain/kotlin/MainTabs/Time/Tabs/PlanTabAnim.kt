package MainTabs.Time.Tabs

import MainTabs.Setting.Element.PanAddSchetPlan
import MainTabs.Time.Elements.PanAddEffekt
import MainTabs.Time.Elements.PanAddPlan
import MainTabs.Time.Elements.PanAddPlanStap
import MainTabs.Time.Elements.PanViewPlanHistory
import MainTabs.Time.Items.*
import MyDialog.MyDialogLayout
import MyDialog.MyEmptyPanel
import MyList
import MyShowMessage
import adapters.MyComboBox
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import common.tests.CommonOpenItemPanel
import extensions.*
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.sharedcode.models.data.ItemPlanStap
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeBindElementForSchetPlan
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlan
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlanStap
import viewmodel.MainDB
import java.util.*

class PlanTabAnim() {

    //    private val selection = SingleSelectionType<ItemPlan>()
    private val selectionStap = SingleSelectionType<ItemPlanStap>()

    //    val listStatePlan: LazyListState = LazyListState(0)
    val listStateStap = MyLazyListState() //: LazyListState = LazyListState(0)
    var lastSelectPlan by mutableStateOf(-1L)
//    var changeScroll = false


    val vypPlan = mutableStateOf(false)
    val vypPlanStap = mutableStateOf(false)
    val sortPlanStapEnable = mutableStateOf(false)
    val sortPlanEnable = mutableStateOf(false)

    @Composable
    private fun ColumnScope.dropMenuPlanStapItem(
        item: ItemPlanStap,
        selection: SingleSelectionType<ItemPlan>,
        dialLay: MyDialogLayout,
        expanded: MutableState<Boolean>,
        itemPlanStapStyle: ItemPlanStapStyleState,
    ) {

        Text(
            text = item.name,
            modifier = Modifier.padding(bottom = 5.dp),
            style = itemPlanStapStyle.mainTextStyle.copy(fontSize = 14.sp)
        )
        with(LocalDensity.current) {
            Row {
                for (cc in 0..4) {
                    MyIconSimpleButt("bookmark_01.svg", MySelectStat.statNabor3.find { it.first == cc.toLong() }?.second ?: Color(0xFFFFF42B)){
                        MainDB.addTime.updMarkerPlanStap(id = item.id.toLong(), marker = cc.toLong())
                        expanded.value = false
                    }
                }
            }
        }

        if (item.quest_key_id == 0L) MyDropdownMenuItem(expanded, itemPlanStapStyle.dropdown, "Изменить") {
            selection.selected?.let { itemPlan ->
                PanAddPlanStap(dialLay, null, itemPlan, item)
            }
        }

        if (item.stat != TypeStatPlanStap.COMPLETE) MyDropdownMenuItem(
            expanded,
            itemPlanStapStyle.dropdown,
            "+ Подэтап"
        ) {
            selection.selected?.let { itemPlan ->
                PanAddPlanStap(dialLay, item, itemPlan)
            }
        }

        if (item.stat != TypeStatPlanStap.COMPLETE) {
            if (item.summa == null) MyDropdownMenuItem(expanded, itemPlanStapStyle.dropdown, "Добавить фин. цель") {
                MyEmptyPanel(dialLay, false) { dialInner, closeFun ->
                    MyTextButtStyle1("Прикрепить существующий счет-план") {
                        closeFun()
                        MainDB.finSpis.spisSchetPlan.getState().value?.filter { it.id.toLong() != 1L }
                            ?.let { listSchpl ->

                                MyEmptyPanel(dialLay, false) { dialInner, closeFun ->
                                    val CB_spisSchPl = remember { MyComboBox(listSchpl, nameItem = { it.name }) }
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
//                expanded.value = false
            } else MyDropdownMenuItem(expanded, itemPlanStapStyle.dropdown, "Убрать фин. цель") {
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
//                expanded.value = false
            }
        }

        MyDropdownMenuItem(expanded, itemPlanStapStyle.dropdown, "Хроника") {
            PanViewPlanHistory(dialLay, item)
//            expanded.value = false
        }

        fun checkSchetPlanStap(itemInner: ItemPlanStap) {
            MyEmptyPanel(dialLay, false) { dialInner, closeFun ->
                Text(
                    "Что сделать со счетом-планом, который был привязан?",
                    Modifier.padding(bottom = 10.dp),
                    style = MyTextStyleParam.style2
                )
                MyTextButtStyle1("Удалить/закрыть счет-план") {
                    MainDB.addFinFun.delBindWithDelOrCloseSchetPlan(
                        TypeBindElementForSchetPlan.PLANSTAP,
                        itemInner.id.toLong()
                    )
                    closeFun()
                }
                MyTextButtStyle1("Отвязать счет-план") {
                    MainDB.addFinFun.deleteBindForSchetPlan(
                        TypeBindElementForSchetPlan.PLANSTAP,
                        itemInner.id.toLong()
                    )
                    closeFun()
                }
                MyTextButtStyle1("Ничего не делать") {
                    closeFun()
                }
            }
        }

        MyDropdownMenuItem(
            expanded,
            itemPlanStapStyle.dropdown,
            if (item.stat != TypeStatPlanStap.FREEZE) "Заморозить" else "Разморозить"
        ) {
            MainDB.addTime.updStatPlanStap(
                item,
                if (item.stat != TypeStatPlanStap.FREEZE) TypeStatPlanStap.FREEZE else TypeStatPlanStap.VISIB
            )
        }

        MyDropdownMenuItem(
            expanded,
            itemPlanStapStyle.dropdown,
            if (item.stat != TypeStatPlanStap.NEXTACTION) "В следующие действия" else "Убрать из след. д."
        ) {
            MainDB.addTime.updStatPlanStap(
                item,
                if (item.stat != TypeStatPlanStap.NEXTACTION) TypeStatPlanStap.NEXTACTION else TypeStatPlanStap.VISIB
            )
        }

        MyDropdownMenuItem(
            expanded,
            itemPlanStapStyle.dropdown,
            if (item.stat != TypeStatPlanStap.CLOSE) "Закрыть" else "Открыть"
        ) {
            MainDB.addTime.updStatPlanStap(
                item,
                if (item.stat != TypeStatPlanStap.CLOSE) TypeStatPlanStap.CLOSE else TypeStatPlanStap.VISIB
            )
            if (!vypPlan.value) selectionStap.selected = null
            if (item.schplOpen == true && item.stat != TypeStatPlanStap.CLOSE) {
                checkSchetPlanStap(item)
            }
        }

        MyCompleteDropdownMenuButton(expanded, item.stat == TypeStatPlanStap.COMPLETE) {
            MainDB.addTime.updStatPlanStap(
                item,
                if (item.stat != TypeStatPlanStap.COMPLETE) TypeStatPlanStap.COMPLETE else TypeStatPlanStap.VISIB
            )
            if (!vypPlanStap.value) selectionStap.selected = null
            if (item.schplOpen == true && item.stat != TypeStatPlanStap.COMPLETE) {
                checkSchetPlanStap(item)
            }
        }

        MyDeleteDropdownMenuButton(expanded) {
            fun delPlanStap(itemPlanStap: ItemPlanStap) {
                MainDB.addTime.delPlanStap(itemPlanStap.id.toLong()) {
                    MainDB.complexOpisSpis.spisComplexOpisForStapPlan.delAllImageForItem(itemPlanStap.id.toLong())
                }
            }

            if (item.quest_key_id == 0L) {
                if (item.podstapcount > 0) {
                    MyShowMessage(dialLay, "Удалите вначале все подэтапы этого плана")
                } else {
                    if (item.schplOpen == true) {
                        MyEmptyPanel(dialLay, false) { dialInner, closeFun ->
                            Text(
                                "Что сделать со счетом-планом, который был привязан?\n(Отвязывание при удалении происходит автоматически)",
                                Modifier.padding(bottom = 10.dp),
                                style = MyTextStyleParam.style2
                            )
                            MyTextButtStyle1("Удалить/закрыть счет-план") {
                                MainDB.addFinFun.delBindWithDelOrCloseSchetPlan(
                                    TypeBindElementForSchetPlan.PLANSTAP,
                                    item.id.toLong()
                                )
                                delPlanStap(item)
//                                MainDB.addTime.delPlanStap(item.id.toLong())
                                closeFun()
                            }
                            MyTextButtStyle1("Ничего не делать") {
                                delPlanStap(item)
//                                MainDB.addTime.delPlanStap(item.id.toLong())
                                closeFun()
                            }
                        }
                    } else {
                        delPlanStap(item)
//                        MainDB.addTime.delPlanStap(item.id.toLong())
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
        selection: SingleSelectionType<ItemPlan>,
        dialLay: MyDialogLayout,
        expanded: MutableState<Boolean>,
        itemPlanStyle: ItemPlanStyleState,
        setDissFun: (() -> Unit) -> Unit
    ) {

        Text(
            text = item.name,
            modifier = Modifier.padding(bottom = 5.dp),
            style = itemPlanStyle.mainTextStyle.copy(fontSize = 14.sp)
        )

        if (item.quest_key_id == 0L) MyDropdownMenuItem(expanded, itemPlanStyle.dropdown, "Изменить") {
            PanAddPlan(dialLay, item)
        }

        if (item.stat != TypeStatPlan.COMPLETE) {
            if (item.summa == null) MyDropdownMenuItem(expanded, itemPlanStyle.dropdown, "Добавить фин. цель") {
                MyEmptyPanel(dialLay, false) { dialInner, closeFun ->
                    MyTextButtStyle1("Прикрепить существующий счет-план") {
                        closeFun()
                        MainDB.finSpis.spisSchetPlan.getState().value?.filter { it.id.toLong() != 1L }
                            ?.let { listSchpl ->
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
//                expanded.value = false
            } else MyDropdownMenuItem(expanded, itemPlanStyle.dropdown, "Убрать фин. цель") {
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
//                expanded.value = false
            }
        }

        MyDropdownMenuItem(expanded, itemPlanStyle.dropdown, "Хроника") {
            PanViewPlanHistory(dialLay, item)
        }

        if (item.stat != TypeStatPlan.COMPLETE) MyDropdownMenuItem(expanded, itemPlanStyle.dropdown, "Эффективность") {
            PanAddEffekt(dialLay, item)
        }

        fun checkSchetPlan(itemInner: ItemPlan) {
            MyEmptyPanel(dialLay, false) { dialInner, closeFun ->
                Text(
                    "Что сделать со счетом-планом, который был привязан?",
                    Modifier.padding(bottom = 10.dp),
                    style = MyTextStyleParam.style2
                )
                MyTextButtStyle1("Удалить/закрыть счет-план") {
                    MainDB.addFinFun.delBindWithDelOrCloseSchetPlan(
                        TypeBindElementForSchetPlan.PLAN,
                        itemInner.id.toLong()
                    )
                    closeFun()
                }
                MyTextButtStyle1("Отвязать счет-план") {
                    MainDB.addFinFun.deleteBindForSchetPlan(
                        TypeBindElementForSchetPlan.PLAN,
                        itemInner.id.toLong()
                    )
                    closeFun()
                }
                MyTextButtStyle1("Ничего не делать") {
                    closeFun()
                }
            }
        }
        MyDropdownMenuItem(
            expanded,
            itemPlanStyle.dropdown,
            if (item.stat != TypeStatPlan.FREEZE) "Заморозить" else "Разморозить"
        ) {
            MainDB.addTime.updStatPlan(
                item,
                if (item.stat != TypeStatPlan.FREEZE) TypeStatPlan.FREEZE else TypeStatPlan.VISIB
            )
        }

        MyDropdownMenuItem(
            expanded,
            itemPlanStyle.dropdown,
            if (item.stat != TypeStatPlan.CLOSE) "Закрыть" else "Открыть"
        ) {
            MainDB.addTime.updStatPlan(
                item,
                if (item.stat != TypeStatPlan.CLOSE) TypeStatPlan.CLOSE else TypeStatPlan.VISIB
            )
            if (!vypPlan.value) selection.selected = null
            if (item.schplOpen == true && item.stat != TypeStatPlan.CLOSE) {
                checkSchetPlan(item)
            }
        }

        if (item.direction.not()) MyCompleteDropdownMenuButton(expanded, item.stat == TypeStatPlan.COMPLETE) {
            MainDB.addTime.updStatPlan(
                item,
                if (item.stat != TypeStatPlan.COMPLETE) TypeStatPlan.COMPLETE else TypeStatPlan.VISIB
            )
            if (!vypPlan.value) selection.selected = null
            if (item.schplOpen == true && item.stat != TypeStatPlan.COMPLETE) {
                checkSchetPlan(item)
            }
        }

        MyDeleteDropdownMenuButton(expanded) {
            fun delPlan(itemPlan: ItemPlan) {
                MainDB.addTime.delPlan(itemPlan.id.toLong()) {
                    MainDB.complexOpisSpis.spisComplexOpisForPlan.delAllImageForItem(itemPlan.id.toLong())
                }
            }

            if (item.quest_key_id == 0L) {
                if (item.countstap > 0) {
                    MyShowMessage(dialLay, "Удалите вначале все этапы этого плана")
                } else {
//                    println(item.id)
                    if (item.schplOpen == true) {
                        MyEmptyPanel(dialLay, false) { dialInner, closeFun ->
                            Text(
                                "Что сделать со счетом-планом, который был привязан?\n(Отвязывание при удалении происходит автоматически)",
                                Modifier.padding(bottom = 10.dp),
                                style = MyTextStyleParam.style2
                            )
                            MyTextButtStyle1("Удалить/закрыть счет-план") {
                                MainDB.addFinFun.delBindWithDelOrCloseSchetPlan(
                                    TypeBindElementForSchetPlan.PLAN,
                                    item.id.toLong()
                                )
                                delPlan(item)
//                                MainDB.addTime.delPlan(item.id.toLong())
                                closeFun()
                            }
                            MyTextButtStyle1("Ничего не делать") {
                                delPlan(item)
//                                MainDB.addTime.delPlan(item.id.toLong())
                                closeFun()
                            }
                        }
                    } else {
                        delPlan(item)
//                        MainDB.addTime.delPlan(item.id.toLong())
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

    var regexStr by mutableStateOf("")
    val planAnimPanel = CommonOpenItemPanel<ItemPlan>(
        whenOpen = {
            regexStr = ""
            if (lastSelectPlan != it.id.toLong()) listStateStap.scrollToZero()
            MainDB.timeFun.setPlanForSpisStapPlan(it.id.toLong())
            MainDB.timeFun.setPlanForCountStapPlan(it.id.toLong())
            MainDB.timeFun.setOpenSpisStapPlan(vypPlanStap.value)
            lastSelectPlan = it.id.toLong()
        },
        mainSpis = { modifierList, selection, openSpis_Index_Item, lazyListState, dialLay ->
            MainDB.styleParam.timeParam.planTab.itemPlan.getComposable(::ItemPlanStyleState) { itemPlanStyle ->
                MyList(
                    MainDB.timeSpis.spisPlan.getState().sortedBy { it.stat == TypeStatPlan.FREEZE } ?: listOf(),
                    modifier = Modifier.padding(horizontal = 10.dp).padding(top = 5.dp, bottom = 5.dp).weight(1f)
                        .then(modifierList),
                    lazyListState
                ) { ind, itemPlan ->
                    if (itemPlan.stat != TypeStatPlan.INVIS) {
                        if (itemPlan.stat == TypeStatPlan.BLOCK) ComItemPlanPlate(
                            itemPlan,
                            itemPlanStyleState = ItemPlanStyleState(MainDB.styleParam.timeParam.planTab.itemPlan)
                        ) else
                            ComItemPlan(
                                itemPlan,
                                selection,
                                selFun = {
//                            changeScroll = true
//                            keyEffect.value = keyEffect.value.not()
                                },
                                onDoubleClick = {
                                    openSpis_Index_Item(ind, itemPlan)
//                                openStapPan.value = true
                                },
                                changeGotov = { item, progress ->
                                    MainDB.addTime.updGotovPlan(item.id.toLong(), progress.toDouble() * 100)
                                },
                                itemPlanStyleState = itemPlanStyle,
                                dialLay = dialLay,
                                sortEnable = sortPlanEnable.value
                            ) { item, expanded, setDissFun ->
                                if (dialLay != null) dropMenuPlanItem(
                                    item,
                                    selection,
                                    dialLay,
                                    expanded,
                                    itemPlanStyle,
                                    setDissFun
                                )
                            }.getComposable()
                    }
                }
            }

        },
        openedItem = { itemTS, startOpenAnimation, selection, dialLay ->
//            Column(Modifier.padding(top = 10.dp, bottom = 0.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            selection.selected?.let {
                val scopeR = rememberCoroutineScope()
                ComItemPlanOpen(
                    it,
                    closeStaps = {
                        startOpenAnimation.value = false
                    },
                    itemPlanStyleState = ItemPlanStyleState(MainDB.styleParam.timeParam.planTab.itemPlan)
                ) {
                    if (startOpenAnimation.value) {
                        MainDB.styleParam.timeParam.planTab.itemPlanStap.getComposable(::ItemPlanStapStyleState) { itemPlanStapStyle ->
                            MainDB.timeSpis.spisPlanStap.getState().let { spisStap ->
                                LaunchedEffect(spisStap) {
                                    var regexStr1 = ""
                                    spisStap.forEach {
                                        if (regexStr1.toRegex().containsMatchIn(it.sortCTE).not() || regexStr1 == "") {
                                            if (it.svernut) regexStr1 += (if (regexStr1 == "") "^" else "|^") + it.sortCTE + "/"
                                        }
                                    }
                                    if (regexStr1 != regexStr) regexStr = regexStr1
                                    println("regexStr = $regexStr")
                                }
                                MyList(
                                    spisStap,
                                    Modifier.padding(horizontal = 0.dp, vertical = 5.dp).weight(1f)
                                        .padding(bottom = 10.dp),
                                    listStateStap.listState,
                                    darkScroll = true
                                ) { ind, itemPlanStap ->
                                    if (itemPlanStap.stat != TypeStatPlanStap.INVIS && (regexStr == "" || regexStr.toRegex()
                                            .containsMatchIn(itemPlanStap.sortCTE).not())
                                    ) {
                                        if (itemPlanStap.stat == TypeStatPlanStap.BLOCK) ComItemPlanStapPlate(
                                            itemPlanStap,
                                            itemPlanStapStyleState = ItemPlanStapStyleState(MainDB.styleParam.timeParam.planTab.itemPlanStap)
                                        ) else
                                            ComItemStapPlan(
                                                itemPlanStap,
                                                selectionStap,
                                                sverFun = {
                                                    val svernutNew = it.svernut.not()
                                                    MainDB.timeSpis.spisPlanStap.updateElem(
                                                        it,
                                                        it.copy(svernut = svernutNew)
                                                    )
//                                                    it.svernut = it.svernut.not()
//                                                    var regexStr1 = "|^" + it.sortCTE + "/|"

                                                    var regexStr1 = ""
                                                    spisStap.forEach {
                                                        if (regexStr1.toRegex().containsMatchIn(it.sortCTE)
                                                                .not() || regexStr1 == ""
                                                        ) {
                                                            if (it.svernut) regexStr1 += (if (regexStr1 == "") "^" else "|^") + it.sortCTE + "/"
                                                        }
                                                    }
                                                    if (regexStr1 != regexStr) regexStr = regexStr1
/*
                                                    println("regexStr0 = $regexStr")
                                                    regexStr = "\\|\\^${it.sortCTE}/\\|".toRegex().replace(regexStr, "|")
                                                    println("regexStr1 = $regexStr")
                                                    regexStr = "^\\^${it.sortCTE}/\\|".toRegex().replace(regexStr, "")
                                                    println("regexStr2 = $regexStr")
                                                    regexStr = "\\|\\^${it.sortCTE}/^".toRegex().replace(regexStr, "")
                                                    println("regexStr3 = $regexStr")
*/
/*
                                                    spisStap.forEach {
                                                        if (regexStr1.toRegex().containsMatchIn(it.sortCTE).not() || regexStr1 == "") {
                                                            if (it.svernut) regexStr1 += (if (regexStr1 == "") "^" else "|^") + it.sortCTE + "/"
                                                        }
                                                    }
                                                    if (regexStr1 != regexStr) regexStr = regexStr1
*/
                                                    scopeR.run {
//                                                    GlobalScope.async {
                                                        MainDB.timeFun.setExpandStapPlan(
                                                            it.id.toLong(),
                                                            svernutNew
                                                        )
                                                    }
                                                },
                                                changeGotov = { item, progress ->
                                                    MainDB.timeSpis.spisPlanStap.updateElem(
                                                        item,
                                                        item.copy(gotov = progress.toDouble() * 100)
                                                    )
                                                    scopeR.run {
                                                        MainDB.addTime.updGotovPlanStap(
                                                            item.id.toLong(),
                                                            progress.toDouble() * 100
                                                        )
                                                    }
                                                },
                                                itemPlanStapStyleState = itemPlanStapStyle,
                                                dialLay = dialLay,
                                                sortEnable = sortPlanStapEnable.value
                                            ) { item, expanded ->
                                                if (dialLay != null) dropMenuPlanStapItem(
                                                    item,
                                                    selection,
                                                    dialLay,
                                                    expanded,
                                                    itemPlanStapStyle
                                                )
                                            }.getComposable()
                                    }
                                }
                            }
                        }
                    } //  else Spacer(Modifier.weight(1f))
                }
            }
            selection.selected?.let {
            }
//            }
        }
    )

    val timeKeyEvent: MutableState<Long> = mutableStateOf(0L)

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun show(dialLay: MyDialogLayout) {
        val focusRequester = remember { FocusRequester() }
        val remScope = rememberCoroutineScope()
        listStateStap.launchEffect()
        Column(Modifier.padding(top = 0.dp, bottom = 0.dp)
            .onKeyEvent {
                when (it.key.keyCode) {
                    Key.DirectionUp.keyCode -> {
                        val time = Date().time
                        if (time - timeKeyEvent.value > 200) {
                            timeKeyEvent.value = time
//                        if (it.isCtrlPressed) {
//                        } else false
                            if (planAnimPanel.openTS.value && sortPlanStapEnable.value) {
                                selectionStap.selected?.let { item ->
                                    MainDB.timeSpis.spisPlanStap.getState().let {
                                        it.findLast { it.sort < item.sort && it.parent_id == item.parent_id }
                                            ?.let { itFind ->
/*
                                                val regexOld = item.sortCTE.toRegex()
                                                val regexNew = itFind.sortCTE.toRegex()
                                                val oldSpis = MainDB.timeSpis.spisPlanStap.getState()
                                                    .filter { regexOld.containsMatchIn(it.sortCTE) }
                                                    .toList()
                                                val newSpis = MainDB.timeSpis.spisPlanStap.getState()
                                                    .filter { regexNew.containsMatchIn(it.sortCTE) }
                                                    .toList()
                                                oldSpis.forEach {
                                                    MainDB.timeSpis.spisPlanStap.updateElem(
                                                        it,
                                                        it.copy(
                                                            sort = if (it.sort == item.sort) itFind.sort else it.sort,
                                                            sortCTE = regexOld.replace(
                                                                it.sortCTE,
                                                                itFind.sortCTE
                                                            )
                                                        )
                                                    )
                                                }
                                                newSpis.forEach {
                                                    MainDB.timeSpis.spisPlanStap.updateElem(
                                                        it,
                                                        it.copy(
                                                            sort = if (it.sort == itFind.sort) item.sort else it.sort,
                                                            sortCTE = regexNew.replace(
                                                                it.sortCTE,
                                                                item.sortCTE
                                                            )
                                                        )
                                                    )
                                                }
                                                MainDB.timeSpis.spisPlanStap.sortBy { it.sort }
*/
                                                MainDB.addTime.setSortPlanStap(item, itFind.sort)
                                                selectionStap.selected = item.copy(sort = itFind.sort)
                                            }
                                    }
                                    true
                                } ?: false

                            } else if (sortPlanEnable.value) {
                                planAnimPanel.selectionMainSpis.selected?.let { item ->
                                    MainDB.timeSpis.spisPlan.getState().let {
                                        it.filter { if (item.stat == TypeStatPlan.FREEZE) it.stat == TypeStatPlan.FREEZE else it.stat != TypeStatPlan.FREEZE }
                                            .findLast { it.sort > item.sort }?.let {
                                                MainDB.timeSpis.spisPlan.updateElem(item, item.copy(sort = it.sort))
                                                MainDB.timeSpis.spisPlan.updateElem(
                                                    it,
                                                    it.copy(sort = item.sort)
                                                ) { itt -> itt.sort }
                                                remScope.run {
                                                    MainDB.addTime.setSortPlan(item, it.sort)
                                                }
                                                planAnimPanel.selectionMainSpis.selected = item.copy(sort = it.sort)
                                            }
                                    }
                                    true
                                } ?: false
                            } else false
                        } else false
                    }
                    Key.DirectionDown.keyCode -> {
//                        if (it.isCtrlPressed) {
//                        } else false
                        val time = Date().time
                        if (time - timeKeyEvent.value > 250) {
                            timeKeyEvent.value = time

                            if (planAnimPanel.openTS.value && sortPlanStapEnable.value) {
                                selectionStap.selected?.let { item ->
                                    MainDB.timeSpis.spisPlanStap.getState().let {
                                        it.find { it.sort > item.sort && it.parent_id == item.parent_id }
                                            ?.let { itNewPos ->
/*
                                                val regexOld = item.sortCTE.toRegex()
                                                val regexNew = itNewPos.sortCTE.toRegex()
                                                val spis = MainDB.timeSpis.spisPlanStap.getState()
//                                                    .filter { regexOld.containsMatchIn(it.sortCTE) }
                                                    .toList()
                                                val newSpis = MainDB.timeSpis.spisPlanStap.getState()
                                                    .filter { regexNew.containsMatchIn(it.sortCTE) }
                                                    .toList()
                                                spis.forEach {
                                                    val newSort = if (it.sort == item.sort) itNewPos.sort else if (it.sort >= itNewPos.sort && it.sort < item.sort) it.sort + 1 else it.sort
                                                    MainDB.timeSpis.spisPlanStap.updateElem(
                                                        it,
                                                        it.copy(
                                                            sort = newSort,
                                                            sortCTE = regexOld.replace(
                                                                it.sortCTE,
                                                                itNewPos.sortCTE
                                                            )
                                                        )
                                                    )
                                                }
                                                newSpis.forEach {
                                                    MainDB.timeSpis.spisPlanStap.updateElem(
                                                        it,
                                                        it.copy(
                                                            sort = if (it.sort == itNewPos.sort) item.sort else it.sort,
                                                            sortCTE = regexNew.replace(
                                                                it.sortCTE,
                                                                item.sortCTE
                                                            )
                                                        )
                                                    )
                                                }
                                                MainDB.timeSpis.spisPlanStap.sortBy { it.sort }
*/
                                                MainDB.addTime.setSortPlanStap(item, itNewPos.sort)
                                                selectionStap.selected = item.copy(sort = itNewPos.sort)
                                            }
                                    }
                                    true
                                } ?: false

                            } else if (sortPlanEnable.value) {
                                planAnimPanel.selectionMainSpis.selected?.let { item ->
                                    MainDB.timeSpis.spisPlan.getState().let {
                                        it.filter { if (item.stat == TypeStatPlan.FREEZE) it.stat == TypeStatPlan.FREEZE else it.stat != TypeStatPlan.FREEZE }
                                            .find { it.sort < item.sort }?.let {
                                                MainDB.timeSpis.spisPlan.updateElem(item, item.copy(sort = it.sort))
                                                MainDB.timeSpis.spisPlan.updateElem(
                                                    it,
                                                    it.copy(sort = item.sort)
                                                ) { itt -> itt.sort }
                                                remScope.run {
                                                    MainDB.addTime.setSortPlan(item, it.sort)
                                                }
                                                planAnimPanel.selectionMainSpis.selected = item.copy(sort = it.sort)
                                            }
                                    }
                                    true
                                } ?: false
                            } else false
                        } else false
                    }
                    else -> false
                }
            }
            .focusRequester(focusRequester).focusable(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            planAnimPanel.show(Modifier.weight(1f), dialLay)
            if (planAnimPanel.openTS.value) {
                Row(
                    modifier = Modifier.padding(top = 0.dp).padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                            if (TypeStatPlanStap.getCloseSelectList().contains(it.stat)) selectionStap.selected = null
                        }
                    }
                    MyToggleButtIconStyle1(
                        "ic_round_swap_vert_24.xml",
                        twoIcon = false,
                        value = sortPlanStapEnable,
                        sizeIcon = 35.dp,
                        modifier = Modifier.height(35.dp).padding(start = 15.dp),
                        width = 35.dp,
                        myStyleToggleButton = ToggleButtonStyleState(MainDB.styleParam.timeParam.planTab.buttSortPlanStap)
                    )
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
                        planAnimPanel.selectionMainSpis.selected?.let {
                            if (dialLay != null) PanAddPlanStap(dialLay, null, it)
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier.padding(top = 0.dp).padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                        MainDB.timeFun.setOpenSpisPlan(it)
                        planAnimPanel.selectionMainSpis.selected?.let {
                            if (TypeStatPlan.getCloseSelectList()
                                    .contains(it.stat)
                            ) planAnimPanel.selectionMainSpis.selected = null
                        }
                    }
                    MyToggleButtIconStyle1(
                        "ic_round_swap_vert_24.xml",
                        twoIcon = false,
                        value = sortPlanEnable,
                        sizeIcon = 35.dp,
                        modifier = Modifier.height(35.dp).padding(start = 15.dp),
                        width = 35.dp,
                        myStyleToggleButton = ToggleButtonStyleState(MainDB.styleParam.timeParam.planTab.buttSortPlan)
                    )
                    Text(
                        "Количесто проектов: ${MainDB.timeSpis.spisPlan.getState().size}",
                        Modifier.weight(1f).padding(horizontal = 20.dp),
                        style = MainDB.styleParam.timeParam.planTab.textCountPlan.getValue()
                    )
                    MyTextButtStyle1(
                        "+",
                        modifier = Modifier.padding(end = 15.dp),
                        width = 70.dp,
                        height = 35.dp,
                        myStyleTextButton = TextButtonStyleState(MainDB.styleParam.timeParam.planTab.buttAddPlan)
                    ) {
                        if (dialLay != null) PanAddPlan(dialLay)
                    }
                }
            }
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}
