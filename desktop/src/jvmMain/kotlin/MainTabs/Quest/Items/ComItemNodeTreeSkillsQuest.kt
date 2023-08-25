package MainTabs.Quest.Items


import MainTabs.Quest.Element.PanAddNodeTreeSkillsQuest
import MainTabs.Quest.Element.PanAddTrigger
import MainTabs.Quest.Element.ShowOpisNodeTreeSkillsQuest
import MyDialog.MyDialogLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import common.tests.IconNode
import extensions.RowVA
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemCountNodeTreeSkillsQuest
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemHandNodeTreeSkillsQuest
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemNodeTreeSkillsQuest
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemPlanNodeTreeSkillsQuest
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.*
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.helpers.parentOfTrig
import viewmodel.QuestDB
import viewmodel.QuestVM


@Composable
fun ShowIconNodeQuest(item: ItemNodeTreeSkillsQuest, dirQuest: String, size: Dp = 50.dp) {
    Box(contentAlignment = Alignment.Center) {
        RowVA {
            IconNode(
                item.icon,
                "icon_skill_color_lamp.png",
                dirQuest,
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                text = "❯",
                modifier = Modifier.align(Alignment.CenterVertically),
                style = MyTextStyleParam.style1.copy(fontSize = 20.sp)
            )
            IconNode(
                item.icon_complete ?: item.icon,
                "icon_skill_color_lamp.png",
                dirQuest,
                complete = item.icon_complete?.let { TypeIconBorder.getType(it.type_ramk) != TypeIconBorder.NONE }
                    ?: true,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        if (item.visible_stat == -2L || item.visible_stat == -3L) Image(
            painterResource(if (item.visible_stat == -2L) "ic_round_lock_24.xml" else "ic_round_visibility_off_24.xml"),
            "statDenPlan",
            Modifier
                .height(50.dp)
                .width(50.dp),
            alpha = 0.7F,
            contentScale = ContentScale.FillBounds,
        )
    }
}

/**
 * Айтем ачивок используется для основного отображения деревьев общего плана и уровневых деревьев.
 * Для уровневых параметры меняются в зависимости от обязательности, а для общих
 * в зависимости от маркера.
 * */
class ComItemNodeTreeSkillsQuest(
    val item: ItemNodeTreeSkillsQuest,
    val questDB: QuestDB,
    val typeTree: TypeTreeSkills,
    val selection: SingleSelectionType<ItemNodeTreeSkillsQuest>,
    val dialLay: MyDialogLayout,
    val doubleClick: (ItemNodeTreeSkillsQuest) -> Unit = {
        ShowOpisNodeTreeSkillsQuest(
            dialLay,
            typeTree = typeTree,
            item = it,
            questDB = questDB
        )
    },
    val dropMenu: @Composable ColumnScope.(ItemNodeTreeSkillsQuest, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    var expandedDropMenu = mutableStateOf(false)

    @Composable
    fun getComposable() {
        MyCardStyle1(selection.isActive(item), 0, {
            questDB.questFun.setMarkerParentAndChildForNodeTreeSkills(item)
            selection.selected = item
        }, {
            doubleClick(item)
        },
            widthBorder = if (item.must_node && typeTree == TypeTreeSkills.LEVELS) 3.dp else 0.5.dp,
            fillWidth = false,
            dropMenu = { exp ->
                dropMenu(item, exp)
                MyDropdownMenuItem(exp, "+ Триггер") {
                    PanAddTrigger(dialLay, item.parentOfTrig(), listOf(item.id_tree))
                }
                MyDropdownMenuItem(exp, "Показать описание") {
                    ShowOpisNodeTreeSkillsQuest(
                        dialLay,
                        typeTree = typeTree,
                        item = item,
                        questDB = questDB
                    )
                }
                MyDropdownMenuItem(exp, "Изменить") {
                    PanAddNodeTreeSkillsQuest(
                        dialLay, questDB,
                        item.id_tree,
                        typeTree = typeTree, item = item
                    )
                }
                MyDeleteDropdownMenuButton(exp) {
                    questDB.addQuest.delNodeTreeSkills(item)
                }
            }
        ) {
            Row(
                Modifier.padding(horizontal = 2.dp).padding(vertical = 2.dp).background(
                    when (item.marker) {
                        MarkerNodeTreeSkills.DIRECTPARENT -> Color.Red
                        MarkerNodeTreeSkills.INDIRECTPARENT -> Color.Black
                        MarkerNodeTreeSkills.DIRECTCHILD -> Color.Blue
                        MarkerNodeTreeSkills.INDIRECTCHILD -> Color.Yellow
                        else -> Color(0xFF464D45)
                    },
                    RoundedCornerShape(15.dp)
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val widthItem = remember { mutableStateOf(40.dp) }
                Column(Modifier.onGloballyPositioned {

                }, horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(Modifier.onGloballyPositioned {
                        widthItem.value = it.size.width.dp - 10.dp
                    }) {
                        ShowIconNodeQuest(item, questDB.dirQuest)
                        if (selection.isActive(item)) MyButtDropdownMenuStyle2(
                            Modifier.padding(top = 0.dp).padding(vertical = 0.dp).align(Alignment.TopStart),
                            expandedDropMenu
                        ) {
                            dropMenu(item, expandedDropMenu)
                        }
                        Text(
                            text = "(${
                                when (item) {
                                    is ItemCountNodeTreeSkillsQuest -> "С"
                                    is ItemHandNodeTreeSkillsQuest -> "Р"
                                    is ItemPlanNodeTreeSkillsQuest -> "П"
                                    else -> ""
                                }
                            })",
                            modifier = Modifier.padding(5.dp).align(Alignment.TopEnd),
                            style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start, fontSize = 8.sp)
                        )
                    }
                    PlateOrderLayout(Modifier.width(widthItem.value)) {
                        QuestVM.getComItemTriggers(TypeParentOfTrig.NODETREESKILLS.code, item.id.toLong())
                        QuestVM.getTriggerMarkersForTriggerChilds(
                            TypeStartObjOfTrigger.STARTNODETREE.id,
                            item.id.toLong(),
                            emptyMarker = (item.visible_stat == -2L || item.visible_stat == -3L)
                        )
                    }
                    Text(
                        text = "(${item.id_type_node})${item.name}",
                        modifier = Modifier.padding(5.dp),
                        style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start, fontSize = 12.sp)
                    )
                }
            }
        }
    }
}


/**
 * Айтем для отображения ачивок с чекбоксом для выделения в панели выбора родителей.
 * */
class ComItemNodeTreeSkillsCheckableQuest(
    val item: ItemNodeTreeSkillsQuest,
    val questDB: QuestDB,
    val selection: SingleSelectionType<ItemNodeTreeSkillsQuest>,
) {
    @Composable
    fun getComposable() {
        val markerCheck = remember { mutableStateOf(item.check) }
        fun checkedItem() {
            if (item.marker != MarkerNodeTreeSkills.UNENABLE && item.marker != MarkerNodeTreeSkills.UNENABLELVL2) {
                questDB.questFun.changeCheckNode(item)
                markerCheck.value = !markerCheck.value
                /**
                 * Строчка ниже нужна, чтобы после изменения маркеров происходило обновление всех айтемов,
                 * т.к. selection привязывается сразу ко всем, то и обновление будет сразу у всех. Пусть сам selection тут и не нужен.
                 * Без него обновления не будет, т.к. поля ItemNodeTreeSkills не являются State.
                 * */
                if (selection.selected == null) selection.selected = item else selection.selected = null
            }
        }
        MyCardStyle1(
            false,
            0,
            {
            },
            {
                checkedItem()
            },
            fillWidth = false,
            modifierThen = if (item.marker == MarkerNodeTreeSkills.UNENABLE) Modifier.alpha(0.5f) else if (item.marker == MarkerNodeTreeSkills.UNENABLELVL2) Modifier.alpha(
                0.05f
            ) else Modifier
        ) {
            RowVA(Modifier.padding(horizontal = 2.dp).padding(vertical = 2.dp)) {
                Checkbox(
                    markerCheck.value,
                    modifier = Modifier.padding(start = 0.dp),
                    onCheckedChange = {
                        checkedItem()
                    })
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ShowIconNodeQuest(item, questDB.dirQuest)
                    Text(
                        text = "(${item.id_type_node})${item.name}",
                        modifier = Modifier.padding(5.dp),
                        style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start, fontSize = 12.sp)
                    )
                }
            }
        }
    }
}


/**
 * Айтем для отображения выбранных родителей в панели добавления/изменения ачивок,
 * а так же для отображения родителей на панели информации ачивки.
 * */
class ComItemNodeTreeSkillsSelParentsQuest(
    val item: ItemNodeTreeSkillsQuest,
    val questDB: QuestDB,
    val selection: SingleSelectionType<ItemNodeTreeSkillsQuest>? = null,
) {
    @Composable
    fun getComposable() {
        val markerCheck = remember { mutableStateOf(item.check) }
        MyCardStyle1(selection?.isActive(item) ?: false, 0, {
            selection?.selected = item
        }, {
        },
            fillWidth = false
        ) {
            RowVA(
                Modifier.padding(horizontal = 2.dp).padding(vertical = 2.dp).background(
                    when (item.marker) {
                        MarkerNodeTreeSkills.INDIRECTPARENT -> Color.Black
                        else -> Color(0xFF464D45)
                    },
                    RoundedCornerShape(15.dp)
                )
            ) {
                val widthItem = remember { mutableStateOf(40.dp) }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(Modifier.onGloballyPositioned {
                        widthItem.value = it.size.width.dp - 10.dp
                    }) {
                        ShowIconNodeQuest(item, questDB.dirQuest)
                        Text(
                            text = "(${item.level})",
                            modifier = Modifier.padding(5.dp).align(Alignment.TopStart),
                            style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start, fontSize = 8.sp)
                        )
                    }
                    PlateOrderLayout(Modifier.width(widthItem.value)) {
                        QuestVM.getComItemTriggers(
                            TypeParentOfTrig.NODETREESKILLS.code,
                            item.id.toLong(),
                            editable = false
                        )
                        QuestVM.getTriggerMarkersForTriggerChilds(
                            TypeStartObjOfTrigger.STARTNODETREE.id,
                            item.id.toLong(),
                            emptyMarker = (item.visible_stat == -2L || item.visible_stat == -3L)
                        )
                    }
                    Text(
                        text = "(${item.id_type_node})${item.name}",
                        modifier = Modifier.padding(5.dp),
                        style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start, fontSize = 12.sp)
                    )
                }
            }
        }
    }
}
