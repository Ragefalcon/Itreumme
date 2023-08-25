package MainTabs.Avatar

import MainTabs.Avatar.Element.PanAddGoal
import MainTabs.Avatar.Element.PanPrivsGoal
import MainTabs.Avatar.Items.ComItemGoal
import MainTabs.Setting.Element.PanAddSchetPlan
import MyDialog.MyDialogLayout
import MyDialog.MyEmptyPanel
import MyList
import adapters.MyComboBox
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.models.data.ItemGoal
import ru.ragefalcon.sharedcode.models.data.ItemYearGraf
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeBindElementForSchetPlan
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.Interface.StyleVMspis
import viewmodel.MainDB


class GoalsPanel {

    private val selection = SingleSelectionType<ItemGoal>()
    val vypGoal = mutableStateOf(false)
    val spisGoalView = mutableStateOf(false)
    val rectDiagram = DrawRectDiagram()


    val list = mutableStateOf<List<ItemYearGraf>>(listOf())
    var widthBox = mutableStateOf(100.dp)
    var heightBox = mutableStateOf(1000.dp)
    var contentWidth = mutableStateOf(200.dp)


    fun selFirst() {
        if (selection.selected == null) MainDB.avatarSpis.spisGoals.getState().value?.let {
            it.firstOrNull()?.let {
                selection.selected = it
                MainDB.avatarFun.selectGoalForDiagram(it.id.toLong())
                MainDB.avatarFun.setSelectedGoalListenerForStatistik(it.id.toLong())
                MainDB.avatarFun.setSelectedIdForPrivsGoal(it.id.toLong())
            }
        }
    }

    @Composable
    private fun dropMenuItemGoal(item: ItemGoal, expanded: MutableState<Boolean>, dialLay: MyDialogLayout) {
        DropdownMenuItem(onClick = {
            PanAddGoal(dialLay, item)
            expanded.value = false
        }) {
            Text(text = "Изменить", color = Color.White)
        }
        if (item.gotov != 100.0) {
            if (item.summa == null) DropdownMenuItem(onClick = {
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
                                                    TypeBindElementForSchetPlan.GOAL,
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
                            Pair(TypeBindElementForSchetPlan.GOAL, item.id.toLong())
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
                            TypeBindElementForSchetPlan.GOAL,
                            item.id.toLong()
                        )
                        closeFun()
                    }
                    MyTextButtStyle1("Только отвязать счет-план") {
                        MainDB.addFinFun.deleteBindForSchetPlan(
                            TypeBindElementForSchetPlan.GOAL,
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

        MyCompleteDropdownMenuButton(expanded, item.gotov == 100.0) {
            MainDB.addAvatar.setOpenGoal(item.id.toLong(), item.gotov != 100.0)
            if (!vypGoal.value) {
                selection.selected = null
            }
            if (item.schplOpen == true && item.gotov != 100.0) {
                MyEmptyPanel(dialLay, false) { dialInner, closeFun ->
                    Text(
                        "Что сделать со счетом-планом, который был привязан?",
                        Modifier.padding(bottom = 10.dp),
                        style = MyTextStyleParam.style2
                    )
                    MyTextButtStyle1("Удалить/закрыть счет-план") {
                        MainDB.addFinFun.delBindWithDelOrCloseSchetPlan(
                            TypeBindElementForSchetPlan.GOAL,
                            item.id.toLong()
                        )
                        closeFun()
                    }
                    MyTextButtStyle1("Отвязать счет-план") {
                        MainDB.addFinFun.deleteBindForSchetPlan(
                            TypeBindElementForSchetPlan.GOAL,
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

            if (item.schplOpen == true) {
                MyEmptyPanel(dialLay, false) { dialInner, closeFun ->
                    Text(
                        "Что сделать со счетом-планом, который был привязан?\n(Отвязывание при удалении происходит автоматически)",
                        Modifier.padding(bottom = 10.dp),
                        style = MyTextStyleParam.style2
                    )
                    MyTextButtStyle1("Удалить/закрыть счет-план") {
                        MainDB.addFinFun.delBindWithDelOrCloseSchetPlan(
                            TypeBindElementForSchetPlan.GOAL,
                            item.id.toLong()
                        )
                        MainDB.addAvatar.delGoal(item.id.toLong())
                        closeFun()
                    }
                    MyTextButtStyle1("Ничего не делать") {
                        MainDB.addAvatar.delGoal(item.id.toLong())
                        closeFun()
                    }
                }
            } else {
                MainDB.addAvatar.delGoal(item.id.toLong())
            }
            selection.selected = null
        }

    }

    @Composable
    private fun StyleVMspis.InterfaceState.GoalTabParam.topPanel(dialLay: MyDialogLayout) {
        RowVA(Modifier.padding(bottom = 0.dp)) {
            MainDB.avatarSpis.spisGoals.getState().value?.let { list ->
                if (list.count() > 1) MyTextButtStyle1(
                    "❮❮",
                    modifier = Modifier.padding(end = 5.dp),
                    myStyleTextButton = TextButtonStyleState(buttArrowGoal)
                ) {
                    list.indexOf(selection.selected).let { indexSel ->
                        selection.selected =
                            if (indexSel > 0) list[indexSel - 1] else list.lastOrNull()
                        selection.selected?.let {
                            MainDB.avatarFun.selectGoalForDiagram(it.id.toLong())
                            MainDB.avatarFun.setSelectedGoalListenerForStatistik(it.id.toLong())
                            MainDB.avatarFun.setSelectedIdForPrivsGoal(it.id.toLong())
                        }
                    }
                }
                Box(Modifier.weight(1f)) {
                    selection.selected?.let {
                        ComItemGoal(it, selection, selFun = {

                        }, selectable = false, itemGoalStyleState = ItemGoalStyleState(itemGoal)) { item, expanded ->
                            dropMenuItemGoal(item, expanded, dialLay)
                        }.getComposable()
                    }
                }
                if (list.count() > 1) MyTextButtStyle1(
                    "❯❯",
                    modifier = Modifier.padding(start = 5.dp),
                    myStyleTextButton = TextButtonStyleState(buttArrowGoal)
                ) {
                    list.indexOf(selection.selected).let { indexSel ->
                        selection.selected =
                            if (indexSel < list.count() - 1) list[indexSel + 1] else list[0]
                        selection.selected?.let {
                            MainDB.avatarFun.selectGoalForDiagram(it.id.toLong())
                            MainDB.avatarFun.setSelectedGoalListenerForStatistik(it.id.toLong())
                            MainDB.avatarFun.setSelectedIdForPrivsGoal(it.id.toLong())
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun StyleVMspis.InterfaceState.GoalTabParam.buttPanel(dialLay: MyDialogLayout) {
        Row(Modifier.padding(top = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            MyToggleButtIconStyle1(
                "ic_round_check_circle_outline_24.xml",
                "ic_round_check_circle_24.xml",
                twoIcon = false,
                value = vypGoal,

                modifier = Modifier.padding(start = 15.dp),
                width = 70.dp,
                height = 35.dp,
                myStyleToggleButton = ToggleButtonStyleState(buttVypGoal)
            ) {

                MainDB.avatarFun.setOpenspisGoals(it)
                selection.selected = null
            }
            MyToggleButtIconStyle1(
                "ic_round_view_list_24.xml",
                twoIcon = false,
                value = spisGoalView,
                sizeIcon = 35.dp,
                modifier = Modifier.height(35.dp).padding(start = 15.dp),
                width = 70.dp,
                myStyleToggleButton = ToggleButtonStyleState(buttViewSpisGoal)
            )

            Text(
                text = "Цели: ${MainDB.avatarSpis.spisGoals.getState().value?.size ?: 0}",
                modifier = Modifier.weight(1f).padding(horizontal = 20.dp),
                style = textCountGoal.getValue()
            )
            MyTextButtStyle1(
                "+",
                modifier = Modifier.padding(end = 15.dp),
                width = 70.dp, height = 35.dp,
                myStyleTextButton = TextButtonStyleState(buttAddGoal)
            ) {
                PanAddGoal(dialLay)
            }
        }
    }

    @Composable
    private fun StyleVMspis.InterfaceState.GoalTabParam.spisok(dialLay: MyDialogLayout, modifier: Modifier) {
        itemGoal.getComposable(::ItemGoalStyleState) { itemStyle ->
            MyList(MainDB.avatarSpis.spisGoals, modifier) { ind, itemGoal ->
                ComItemGoal(itemGoal, selection, selFun = {
                    MainDB.avatarFun.selectGoalForDiagram(it.id.toLong())
                    MainDB.avatarFun.setSelectedGoalListenerForStatistik(it.id.toLong())
                    MainDB.avatarFun.setSelectedIdForPrivsGoal(it.id.toLong())
                }, itemGoalStyleState = itemStyle) { item, expanded ->
                    dropMenuItemGoal(item, expanded, dialLay)
                }.getComposable()
            }
        }
    }

    @Composable
    private fun StyleVMspis.InterfaceState.GoalTabParam.rectDiagramm(dialLay: MyDialogLayout) {
        selection.selected?.let {
            RowVA(Modifier.padding(horizontal = 10.dp)) {
                Column(Modifier.weight(1f)) {
                    textHourGoal1.getValue().let { text1 ->
                        textHourGoal2.getValue().let { text2 ->
                            Text(
                                "Всего (последний год/месяц/неделя):",
                                modifier = Modifier.padding(end = 10.dp),
                                style = text1
                            )
                            Row(Modifier.padding(end = 10.dp)) {
                                Text(
                                    MainDB.goalHourAll.value,
                                    modifier = Modifier,
                                    style = text2.copy(color = TEXT_ALL_HOUR_COLOR.getValue().toColor())
                                )
                                Text(" ( ", modifier = Modifier, style = text2)
                                Text(
                                    MainDB.goalHourYear.value,
                                    modifier = Modifier,
                                    style = text2.copy(color = TEXT_REST_HOUR_COLOR.getValue().toColor())
                                )
                                Text(" / ", modifier = Modifier, style = text2)
                                Text(
                                    MainDB.goalHourMonth.value,
                                    modifier = Modifier,
                                    style = text2.copy(color = TEXT_REST_HOUR_COLOR.getValue().toColor())
                                )
                                Text(" / ", modifier = Modifier, style = text2)
                                Text(
                                    MainDB.goalHourWeek.value,
                                    modifier = Modifier,
                                    style = text2.copy(color = TEXT_REST_HOUR_COLOR.getValue().toColor())
                                )
                                Text(" )", modifier = Modifier, style = text2)
                            }
                        }
                    }
                }
                MyTextButtStyle1(
                    "Проекты: ${MainDB.goalCountPlan.value}",
                    Modifier.padding(horizontal = 0.dp).padding(bottom = 0.dp),
                    myStyleTextButton = TextButtonStyleState(buttPrivPlan)
                ) {
                    selection.selected?.id?.let {
                        PanPrivsGoal(it.toLong(), dialLay)
                    }
                }
            }

            rectDiagram.drawDiagram(
                Modifier.fillMaxHeight().fillMaxWidth(1f),
                MainDB.goalYearStatistik.value,
                darkBackground = false,
                styleState = RectDiagramColorStyleState(rectDiagColor)
            )
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun show(
        dialLay: MyDialogLayout,
        modifier: Modifier = Modifier
    ) {
        LaunchedEffect(MainDB.avatarSpis.spisGoals.getState().value) {
            MainDB.avatarSpis.spisGoals.getState().value?.let {
                if (!it.contains(selection.selected)) {
                    selection.selected = it.firstOrNull()
                    selection.selected?.let {
                        MainDB.avatarFun.selectGoalForDiagram(it.id.toLong())
                        MainDB.avatarFun.setSelectedGoalListenerForStatistik(it.id.toLong())
                        MainDB.avatarFun.setSelectedIdForPrivsGoal(it.id.toLong())
                    }
                }
            }
        }
        with(MainDB.styleParam.avatarParam.goalTab) {
            val scrollState = rememberScrollState(0)
            Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                if (spisGoalView.value) {
                    spisok(dialLay, Modifier.padding(vertical = 10.dp).weight(1f))
                } else {
                    topPanel(dialLay)
                    MyShadowBox(panelDiagram.shadow.getValue(), Modifier.weight(1f)) {
                        Column(
                            Modifier
                                .withSimplePlate(SimplePlateWithShadowStyleState(panelDiagram))
                                .fillMaxWidth().padding(10.dp).animateContentSize()
                        ) {
                            rectDiagramm(dialLay)
                        }
                    }
                }
                buttPanel(dialLay)
            }
        }
    }
}