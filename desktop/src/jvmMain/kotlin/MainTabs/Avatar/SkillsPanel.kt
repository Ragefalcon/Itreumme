package MainTabs.Avatar

import MainTabs.Avatar.Element.PanAddTreeSkills
import MainTabs.Quest.Element.ComItemTreeSkillsOpened
import MainTabs.Quest.Element.ComItemTreeSkillsQuestOpened
import MainTabs.Quest.Element.PanAddTreeSkillsQuest
import MyDialog.MyDialogLayout
import MyList
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.*
import MainTabs.Quest.Items.ComItemTreeSkills
import MainTabs.Quest.Items.ComItemTreeSkillsQuest
import MyShowMessage
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import extensions.ItemSkillsTreeState
import extensions.getComposable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatTreeSkills
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatTreeSkills.*
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeTreeSkills
import viewmodel.MainDB
import viewmodel.QuestDB
import viewmodel.QuestVM
import viewmodel.StateVM


class SkillsPanel(val quest: Boolean = false) { //  questDB: QuestDB? = null) {

    val vypSkills = mutableStateOf(false)
    val rectListTree = mutableStateOf(Rect(Offset(10f, 10f), Size(10f, 10f)))
    val offsetItem = mutableStateOf(0)
    val sizeItem = mutableStateOf(0)
    val startOpenAnimation = mutableStateOf(false)

    //    val openTS = if (questDB != null) StateVM.openTreeSkillsQuest else StateVM.openTreeSkills
    val openTS = if (quest) StateVM.openTreeSkillsQuest else StateVM.openTreeSkills

    init {
        println("SkillPanel //----------")
        println(this)
        println(quest)
        println("SkillPanel ----------//")

    }

    @Composable
    private fun executeQuestOrNotQuestComposable(
        questFun: @Composable (QuestDB) -> Unit,
        notQuestFun: @Composable () -> Unit
    ) {
        if (!quest) {
            notQuestFun()
        } else {
            QuestVM.openQuestDB?.let {
                questFun(it)
            }
        }
    }

    private fun executeQuestOrNotQuest(questFun: (QuestDB) -> Unit, notQuestFun: () -> Unit) {
        if (!quest) {
            notQuestFun()
        } else {
            QuestVM.openQuestDB?.let {
                questFun(it)
            }
        }
    }

    @Composable
    fun show(
        dialLay: MyDialogLayout,
        modifier: Modifier = Modifier
    ) {
        val state: LazyListState = rememberLazyListState()
        LaunchedEffect(openTS.value) {
            if (openTS.value) {
//                QuestVM.openQuestDB?.myLetOrElse(
                executeQuestOrNotQuest({ qDB ->
                    StateVM.selectionTreeSkillsQuest.selected?.let { treeSkills ->
                        TypeTreeSkills.Companion.getType(treeSkills.id_type_tree)?.let { typeTree ->
                            qDB.questFun.setSelectTreeSkills(treeSkills.id, typeTree)
                        }
                    }
                }) {
                    StateVM.selectionTreeSkills.selected?.let { treeSkills ->
                        TypeTreeSkills.Companion.getType(treeSkills.id_type_tree)?.let { typeTree ->
                            MainDB.avatarFun.setSelectTreeSkills(treeSkills.id.toLong(), typeTree)
                        }
                    }
                }
            }
        }
        Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            if (openTS.value) {
                TreeSkills(dialLay)
            } else {
//                QuestVM.openQuestDB?.myLetOrElse({
                executeQuestOrNotQuestComposable({ qDB ->
                    SpisTreeSkillsQuest(dialLay, state, qDB)
                }) {
                    SpisTreeSkills(dialLay, state)
                }
            }
        }
    }

    @Composable
    private fun TreeSkills(dialLay: MyDialogLayout) {
        val firstStart = remember { mutableStateOf(true) }
        rememberCoroutineScope().launch {
            delay(100)
            if (firstStart.value) {
                startOpenAnimation.value = true
                firstStart.value = false
            }
        }
        val durationAnim = 300
        val topOffsetTree: Float by animateFloatAsState(
            targetValue = if (startOpenAnimation.value) 0f else zeroIfMinus(rectListTree.value.top + offsetItem.value),
            // Configure the animation duration and easing.
            animationSpec = tween(durationMillis = durationAnim, easing = FastOutSlowInEasing)
        ) {
            println("rectListTree = ${rectListTree.value}")
            if (!startOpenAnimation.value && openTS.value) openTS.value = false
        }
        val startOffsetTree: Float by animateFloatAsState(
            targetValue = if (startOpenAnimation.value) 0f else zeroIfMinus(rectListTree.value.left),
            // Configure the animation duration and easing.
            animationSpec = tween(durationMillis = durationAnim, easing = FastOutSlowInEasing)
        ) {
            if (!startOpenAnimation.value && openTS.value) openTS.value = false
        }
        val endOffsetTree: Float by animateFloatAsState(
            targetValue = if (startOpenAnimation.value) 0f else 68f,
            // Configure the animation duration and easing.
            animationSpec = tween(durationMillis = durationAnim, easing = FastOutSlowInEasing)
        ) {
            if (!startOpenAnimation.value && openTS.value) openTS.value = false
        }
        val bottomOffsetTree: Float by animateFloatAsState(
            targetValue = if (startOpenAnimation.value) 0f else zeroIfMinus(rectListTree.value.bottom - rectListTree.value.top - offsetItem.value - sizeItem.value),
            // Configure the animation duration and easing.
            animationSpec = tween(durationMillis = durationAnim, easing = FastOutSlowInEasing)
        ) {
            if (!startOpenAnimation.value && openTS.value) openTS.value = false
        }

        Box(
            Modifier.padding(
                top = topOffsetTree.dp,
                bottom = bottomOffsetTree.dp,
                start = startOffsetTree.dp,
                end = endOffsetTree.dp
            )
        ) {
            executeQuestOrNotQuestComposable({ qDB ->
                StateVM.selectionTreeSkillsQuest.selected?.let { itemTreeSkill ->
                    ComItemTreeSkillsQuestOpened(dialLay, qDB, itemTreeSkill, StateVM.selectionTreeSkillsQuest,
                        openTree = {
                            println("rectListTree quest close = ${rectListTree.value}")
                            startOpenAnimation.value = false
                        }
                    ) { item, expanded ->
                    }.getComposable()
                }
            }) {
                StateVM.selectionTreeSkills.selected?.let { itemTreeSkill ->
                    ComItemTreeSkillsOpened(dialLay, itemTreeSkill,
                        openTree = {
                            println("rectListTree close = ${rectListTree.value}")
                            startOpenAnimation.value = false
                            //                                println("stateList.layoutInfo.viewportStartOffset = ${stateList.layoutInfo.viewportStartOffset}")
                            //                                println("stateList.layoutInfo.visibleItemsInfo[ind].offset = ${stateList.layoutInfo.visibleItemsInfo[ind].offset}")
                        }, itemTreeSkillStyleState = ItemSkillsTreeState(MainDB.styleParam.avatarParam.skillTab.itemSkill)
                    )
                }
            }
        }
    }

    @Composable
    fun ColumnScope.SpisTreeSkills(dialLay: MyDialogLayout, stateList: LazyListState) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MyTextToggleButtStyle1("Вып", vypSkills, modifier = Modifier.padding(start = 15.dp)) {
                StateVM.selectionTreeSkills.selected = null
            }
            MyTextStyle1(
                "Ветвей навыков: ${MainDB.avatarSpis.spisTreeSkills.getState().value?.size ?: 0}",
                Modifier.weight(1f).padding(horizontal = 10.dp)
            )
//                modifier = Modifier.padding(horizontal = 60.dp),
            MyTextButtStyle1("+", modifier = Modifier.padding(end = 15.dp)) {
                PanAddTreeSkills(dialLay)
            }
        }
        MainDB.styleParam.avatarParam.skillTab.itemSkill.getComposable(::ItemSkillsTreeState) { itemStyle ->
            MyList(MainDB.avatarSpis.spisTreeSkills, Modifier.weight(1f).padding(horizontal = 60.dp).onGloballyPositioned {
//            println("it.parentCoordinates = ${it.parentCoordinates?.size}")
//            println("it.parentLayoutCoordinates = ${it.parentLayoutCoordinates?.size}")
//            println("it.boundsInParent = ${it.boundsInParent()}")
                rectListTree.value = it.boundsInParent()
//            println("it.size = ${it.size}")
            }, stateList) { ind, itemTreeSkill ->
                if ( itemTreeSkill.stat != TypeStatTreeSkills.INVIS ) ComItemTreeSkills(itemTreeSkill, StateVM.selectionTreeSkills, //
                    openTree = {
                        StateVM.selectionTreeSkills.selected = it
                        StateVM.openTreeSkills.value = true
//                    stateList.layoutInfo.visibleItemsInfo.forEach {
//                        println("it.key = ${it.key}")
//                        println("it.index = ${it.index}")
//                    }
//                    println("stateList.layoutInfo.visibleItemsInfo = ${stateList.layoutInfo.visibleItemsInfo}")
//                    println("stateList.layoutInfo.totalItemsCount = ${stateList.layoutInfo.totalItemsCount}")
                        val visibleItem = stateList.layoutInfo.visibleItemsInfo.find { it.index == ind }
                        offsetItem.value = visibleItem?.offset ?: 0
                        sizeItem.value = visibleItem?.size ?: 0
                    }, itemTreeSkillStyleState = itemStyle
                ) { item, expanded ->
                    @Composable
                    fun visibMenu(){
                        MyDropdownMenuItem(expanded, "Открыть редактирование") {
                            MainDB.addAvatar.setOpenEditTreeSkills(item.id.toLong(), OPEN_EDIT)
                        }
                    }
                    when (item.stat) {
                        OPEN_EDIT -> {
                            MyDropdownMenuItem(expanded, "Закрыть редактирование") {
                                MainDB.addAvatar.setOpenEditTreeSkills(item.id.toLong(), VISIB)
                            }
                            if (item.quest_id == 0L) MyDropdownMenuItem(expanded, "Изменить") {
                                PanAddTreeSkills(dialLay, item)
                            }
                            MyDeleteDropdownMenuButton(expanded) {
                                if (item.quest_id == 0L) {
                                    if (item.countNode == 0L) {
                                        MainDB.addAvatar.delTreeSkills(item.id.toLong())
                                        StateVM.selectionTreeSkills.selected = null
                                    } else {
                                        MyShowMessage(dialLay, "Удалите вначале все содержимое дерева")
                                    }
                                } else {
                                    MyShowMessage(
                                        dialLay,
                                        "Этот элемент из квеста, его можно удалить только вместе с квестом."
                                    )
                                }
                            }
                        }
                        VISIB -> visibMenu()
                        COMPLETE -> {}/*TODO()*/
                        UNBLOCKNOW -> visibMenu()
                        BLOCK -> {}
                        INVIS -> {}
                    }

                }
            }
        }
    }

    @Composable
    fun ColumnScope.SpisTreeSkillsQuest(dialLay: MyDialogLayout, stateList: LazyListState, qDB: QuestDB) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MyTextToggleButtStyle1("Вып", vypSkills, modifier = Modifier.padding(start = 15.dp)) {
                StateVM.selectionTreeSkillsQuest.selected = null
            }
            MyTextStyle1(
                "Ветвей навыков: ${qDB.spisQuest.spisTreeSkills.getState().value?.size ?: 0}",
                Modifier.weight(1f).padding(horizontal = 10.dp)
            )
            MyTextButtStyle1("+", modifier = Modifier.padding(end = 15.dp)) {
                PanAddTreeSkillsQuest(dialLay)
            }
        }
        MyList(qDB.spisQuest.spisTreeSkills, Modifier.weight(1f).padding(horizontal = 60.dp).onGloballyPositioned {
            rectListTree.value = it.boundsInParent()
        }, stateList) { ind, itemTreeSkill ->
            ComItemTreeSkillsQuest(itemTreeSkill, StateVM.selectionTreeSkillsQuest, //
                openTree = {
                    val visibleItem = stateList.layoutInfo.visibleItemsInfo.find { it.index == ind }
                    offsetItem.value = visibleItem?.offset ?: 0
                    sizeItem.value = visibleItem?.size ?: 0
                    StateVM.selectionTreeSkillsQuest.selected = it
                    StateVM.openTreeSkillsQuest.value = true
                }
            ) { item, expanded ->
                MyDropdownMenuItem(expanded, "Изменить") {
                    PanAddTreeSkillsQuest(dialLay, item)
                }
                MyDeleteDropdownMenuButton(expanded) {
                    if (item.countNode == 0L) {
                        qDB.addQuest.delTreeSkills(item.id)
                        StateVM.selectionTreeSkillsQuest.selected = null
                    } else {
                        MyShowMessage(dialLay, "Удалите вначале все содержимое дерева")
                    }
                }
            }.getComposable()
        }
    }

    fun zeroIfMinus(value: Float): Float = if (value < 0f) 0f else value

}

