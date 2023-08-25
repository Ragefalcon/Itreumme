package MainTabs.Time.Elements

import MainTabs.Time.Items.ComItemPlan
import MainTabs.Time.Items.ComItemPlanPlate
import MainTabs.Time.Items.ComItemPlanStapPlate
import MainTabs.Time.Items.ComItemStapPlan
import MyList
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.MyTextButtStyle1
import common.SingleSelectionType
import common.color.BoxWithName
import extensions.*
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.sharedcode.models.data.ItemPlanStap
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlan
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatPlanStap
import viewmodel.MainDB

class BoxSelectParentPlan(
    val emptyPlan: Boolean = true,
    var arrayIskl: List<Long> = listOf(),
    itemPlanParent: ItemPlan? = null,
    itemPlanStapParent: ItemPlanStap? = null,
    val selectForGoalOrDream: Boolean = false,
    startWithOpenPlan: Boolean = false,
    val characteristic: Boolean = false,
    val onlyPlan: Boolean = false,
    val label: String = "Выберете проект/этап"
) {
    val selectionPlanParent = SingleSelectionType<ItemPlan>().apply {
        selected = itemPlanParent
    }
    private val selectionPlanParentPriv = SingleSelectionType<ItemPlan>()
    val selectionPlanStapParent = SingleSelectionType<ItemPlanStap>().apply { selected = itemPlanStapParent }
    private val selectionPlanStapParentPriv = SingleSelectionType<ItemPlanStap>()
    val expandedSelPlan = mutableStateOf(startWithOpenPlan)
    val expandedSelPlanStap = mutableStateOf(false)

    fun isExpanded() = expandedSelPlan.value || expandedSelPlanStap.value

    @Composable
    fun getComposable(modifier: Modifier = Modifier) {
        var regexStr by mutableStateOf("")
        val scopeR = rememberCoroutineScope()
        selectionPlanParent.launchedEffect {
            it?.let {
                MainDB.timeFun.setPlanForSpisStapPlanForSelect(
                    it.id.toLong(),
                    arrayIskl
                )
            }
        }
        with(MainDB.styleParam.timeParam.boxSelectParentPlanParam) {

            @Composable
            fun plateStap(item: ItemPlanStap) {
                ComItemPlanStapPlate(
                    item,
                    itemPlanStapStyleState = ItemPlanStapStyleState(MainDB.styleParam.timeParam.planTab.itemPlanStap),
                    parentName = selectionPlanParent.selected?.name ?: "",
                    itemCommonStyle = CommonItemStyleState(
                        MainDB.styleParam.timeParam.planTab.itemPlanStap,
                        shapeCard = MainDB.styleParam.timeParam.boxSelectParentPlanParam.cornerStap.getValue(),
                        paddingOuter = Modifier
                    )
                ) {
                    expandedSelPlanStap.value = true
                }
            }

            @Composable
            fun platePlan(item: ItemPlan) {
                ComItemPlanPlate(
                    item,

                    itemPlanStyleState = ItemPlanStyleState(MainDB.styleParam.timeParam.planTab.itemPlan),
                    itemCommonStyle = CommonItemStyleState(
                        MainDB.styleParam.timeParam.planTab.itemPlan,
                        shapeCard = MainDB.styleParam.timeParam.boxSelectParentPlanParam.cornerPlan.getValue(),
                        paddingOuter = Modifier
                    )
                ) {
                    if (!expandedSelPlanStap.value) expandedSelPlan.value = true
                }
            }

            @Composable
            fun SelectionRez() {
                if (!expandedSelPlan.value && !expandedSelPlanStap.value) {
                    RowVA(Modifier.padding(7.dp)) {
                        if (selectionPlanStapParent.selected != null) selectionPlanStapParent.selected?.let {
                            Box(Modifier.weight(1f)) {
                                plateStap(it)
                            }
                            MyTextButtStyle1(
                                "Выбрать проект",
                                modifier = Modifier.padding(start = 10.dp),
                                myStyleTextButton = TextButtonStyleState(buttGetPlan)
                            ) {
                                expandedSelPlan.value = true
                            }
                        } else if (selectionPlanParent.selected != null) selectionPlanParent.selected?.let {
                            Box(Modifier.weight(1f)) {
                                platePlan(it)
                            }
                            if (!onlyPlan && MainDB.timeSpis.spisPlanStapForSelect.getState().size > 0) MyTextButtStyle1(
                                "Выбрать этап",
                                modifier = Modifier.padding(start = 10.dp),
                                myStyleTextButton = TextButtonStyleState(buttGetStap)
                            ) {
                                expandedSelPlanStap.value = true
                            }
                        } else {
                            Spacer(Modifier.weight(1f))
                            MyTextButtStyle1(
                                "Выбрать проект",
                                myStyleTextButton = TextButtonStyleState(buttGetPlan)
                            ) {
                                expandedSelPlan.value = true
                            }
                            Spacer(Modifier.weight(1f))
                            MyTextButtStyle1(
                                "Выбрать этап",
                                modifier = Modifier.padding(start = 10.dp),
                                myStyleTextButton = TextButtonStyleState(buttGetStap)
                            ) {
                                expandedSelPlan.value = true
                                expandedSelPlanStap.value = true
                            }
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
            BoxWithName(
                label,
                SimplePlateWithShadowStyleState(mainPlate),
                textLabelPadding.getValue().dp,
                textLabelOn.getValue(),
                textLabelOff.getValue(),
                modifierOut = modifier
            ) {
                with(LocalDensity.current) {
                    Column(
                        modifier = Modifier
                            .padding(top = textLabelOn.FONT_SIZE.getValue().sp.toDp() / 2f)
                            .run {
                                if (!expandedSelPlan.value && !expandedSelPlanStap.value) this else this.padding(7.dp)
                            }
                            .animateContentSize(), horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        SelectionRez()
                        if (!expandedSelPlan.value) {
                            selectionPlanParent.selected?.let {
                                if (expandedSelPlanStap.value) {
                                    platePlan(it)
                                    MainDB.styleParam.timeParam.planTab.itemPlanStap.getComposable(::ItemPlanStapStyleState) { itemPlanStapStyle ->
                                        MainDB.timeSpis.spisPlanStapForSelect.getState().let { spisStap ->
                                            LaunchedEffect(true) {
                                                var regexStr1 = ""
                                                spisStap.forEach {
                                                    if (regexStr1.toRegex().containsMatchIn(it.sortCTE)
                                                            .not() || regexStr1 == ""
                                                    ) {
                                                        if (it.svernut) regexStr1 += (if (regexStr1 == "") "^" else "|^") + it.sortCTE + "/"
                                                    }
                                                }
                                                if (regexStr1 != regexStr) regexStr = regexStr1
                                            }
                                            MyList(
                                                spisStap,
                                                Modifier.weight(1f).padding(top = 5.dp, bottom = 10.dp)
                                                    .padding(horizontal = 20.dp),
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
                                                            selectionPlanStapParentPriv,
                                                            sverFun = {
                                                                val svernutNew = it.svernut.not()
                                                                MainDB.timeSpis.spisPlanStapForSelect.updateElem(
                                                                    it,
                                                                    it.copy(svernut = svernutNew)
                                                                )
                                                                var regexStr1 = ""
                                                                spisStap.forEach {
                                                                    if (regexStr1.toRegex().containsMatchIn(it.sortCTE)
                                                                            .not() || regexStr1 == ""
                                                                    ) {
                                                                        if (it.svernut) regexStr1 += (if (regexStr1 == "") "^" else "|^") + it.sortCTE + "/"
                                                                    }
                                                                }
                                                                if (regexStr1 != regexStr) regexStr = regexStr1
                                                                scopeR.run {
                                                                    MainDB.timeFun.setExpandStapPlan(
                                                                        it.id.toLong(),
                                                                        svernutNew
                                                                    )
                                                                }
                                                            },
                                                            onDoubleClick = {
                                                                selectionPlanStapParent.selected = it
                                                                expandedSelPlanStap.value = false
                                                            },
                                                            editable = false,
                                                            itemPlanStapStyleState = itemPlanStapStyle
                                                        ).getComposable()
                                                }
                                            }
                                        }
                                    }
                                    RowVA(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                                        MyTextButtStyle1("Назад", myStyleTextButton = TextButtonStyleState(buttBack)) {
                                            expandedSelPlanStap.value = false
                                        }
                                        selectionPlanStapParent.selected?.let {
                                            MyTextButtStyle1(
                                                "Отменить выбор",
                                                Modifier.padding(start = 10.dp),
                                                myStyleTextButton = TextButtonStyleState(buttUnselect)
                                            ) {
                                                selectionPlanStapParent.selected = null
                                                expandedSelPlanStap.value = false
                                            }
                                        }
                                        selectionPlanStapParentPriv.selected?.let {
                                            MyTextButtStyle1(
                                                "Выбрать",
                                                Modifier.padding(start = 10.dp),
                                                myStyleTextButton = TextButtonStyleState(buttSelect)
                                            ) {
                                                selectionPlanStapParent.selected = it
                                                expandedSelPlanStap.value = false
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            MainDB.styleParam.timeParam.planTab.itemPlan.getComposable(::ItemPlanStyleState) { itemPlanStyle ->
                                MyList(
                                    MainDB.timeSpis.spisPlanForSelect,
                                    Modifier.weight(1f).padding(bottom = 10.dp),
                                    darkScroll = true
                                ) { ind, itemPlan ->
                                    var check = true
                                    if (selectForGoalOrDream) {
                                        MainDB.run { if (characteristic) avatarSpis.spisPlanStapOfCharacteristic else avatarSpis.spisPlanStapOfGoal }
                                            .getState().value?.find {
                                                it.id_plan == itemPlan.id && it.stap == "0"
                                            }?.let {
                                                check = false
                                            }
                                    }
                                    if (check && itemPlan.stat != TypeStatPlan.INVIS) {
                                        if (itemPlan.stat == TypeStatPlan.BLOCK) ComItemPlanPlate(
                                            itemPlan,
                                            itemPlanStyleState = ItemPlanStyleState(MainDB.styleParam.timeParam.planTab.itemPlan)
                                        ) else
                                            ComItemPlan(itemPlan, selectionPlanParentPriv, selFun = {

                                            }, onDoubleClick = {
                                                if (selectionPlanParent.selected != itemPlan) {
                                                    selectionPlanStapParent.selected = null
                                                    selectionPlanParent.selected = itemPlan
                                                }
                                                expandedSelPlan.value = false

                                            }, editable = false, itemPlanStyleState = itemPlanStyle).getComposable()
                                    }
                                }
                            }
                            RowVA(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                                MyTextButtStyle1("Назад", myStyleTextButton = TextButtonStyleState(buttBack)) {
                                    expandedSelPlan.value = false
                                }
                                if (emptyPlan && selectionPlanParent.selected != null) MyTextButtStyle1(
                                    "Отменить выбор",
                                    Modifier.padding(start = 10.dp),
                                    myStyleTextButton = TextButtonStyleState(buttUnselect)
                                ) {
                                    selectionPlanParent.selected = null
                                    selectionPlanStapParent.selected = null
                                    expandedSelPlan.value = false
                                }
                                selectionPlanParentPriv.selected?.let {
                                    MyTextButtStyle1(
                                        "Выбрать",
                                        Modifier.padding(start = 10.dp),
                                        myStyleTextButton = TextButtonStyleState(buttSelect)
                                    ) {
                                        selectionPlanParent.selected = it
                                        expandedSelPlan.value = false
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
