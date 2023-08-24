package MainTabs.Quest.Items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import common.tests.IconNode
import extensions.ItemNodeTreeSkillsState
import extensions.RowVA
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemCountNodeTreeSkills
import ru.ragefalcon.sharedcode.models.data.ItemHandNodeTreeSkills
import ru.ragefalcon.sharedcode.models.data.ItemNodeTreeSkills
import ru.ragefalcon.sharedcode.models.data.ItemPlanNodeTreeSkills
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.*
import viewmodel.MainDB


@Composable
fun ShowIconNodeFromComlete(item: ItemNodeTreeSkills, size: Dp = 50.dp, pair: Pair<Dp, Brush>? = null) {
    if (item.complete == TypeStatNodeTree.COMPLETE) {
        IconNode(
            item.icon_complete ?: item.icon,
            "icon_skill_color_lamp.png",
//            complete = item.icon_complete?.let { TypeIconBorder.getType(it.type_ramk) != TypeIconBorder.NONE } ?: true,
            size = size,
            modifier = Modifier.padding(horizontal = 8.dp), pair = pair
        )
    } else {
        IconNode(
            item.icon,
            "icon_skill_color_lamp.png",
            size = size,
            modifier = Modifier.padding(horizontal = 8.dp), pair = pair
        )
    }
}

/**
 * Айтем ачивок используется для основного отображения деревьев общего плана и уровневых деревьев.
 * Для уровневых параметры меняются в зависимости от обязательности, а для общих
 * в зависимости от маркера.
 * */
@Composable
fun ComItemNodeLevelTreeSkills(
    item: ItemNodeTreeSkills,
    typeTree: TypeTreeSkills,
    selection: SingleSelectionType<ItemNodeTreeSkills>,
    doubleClick: (ItemNodeTreeSkills) -> Unit = {},
    itemNodeTreeSkillStyleState: ItemNodeTreeSkillsState,
    visibleBinding: Boolean = false,
    dropMenu: @Composable ColumnScope.(ItemNodeTreeSkills, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    with(itemNodeTreeSkillStyleState) {
        val expandedDropMenu = remember { mutableStateOf(false) }
        val block = remember { TypeStatNodeTree.getBlockList().contains(item.complete) }
        MyCardStyle1(selection.isActive(item), 0, {
            if (block.not()) {
                if (item.complete == TypeStatNodeTree.UNBLOCKNOW) MainDB.addAvatar.clearUnlockNowHandNode(item)
                MainDB.avatarFun.setMarkerParentAndChildForNodeTreeSkills(item)
                selection.selected = item
            }
        }, {
            if (block.not()) {
                if (item.complete == TypeStatNodeTree.UNBLOCKNOW) MainDB.addAvatar.clearUnlockNowHandNode(item)
                doubleClick(item)
            }
        },
            widthBorder = if (item.must_node && typeTree == TypeTreeSkills.LEVELS) border_width_must else null,
            modifierThen = Modifier.alpha(if (item.open) 1f else 0.5f).run {
                if (visibleBinding) when (item.marker) {
                    MarkerNodeTreeSkills.DIRECTPARENT -> this.border(
                        width = border_width_parent_child,
                        brush = border_brush_DIRECTPARENT,
                        shape = shapeCard
                    )
                    MarkerNodeTreeSkills.INDIRECTPARENT -> this.border(
                        width = border_width_parent_child,
                        brush = border_brush_INDIRECTPARENT,
                        shape = shapeCard
                    )
                    MarkerNodeTreeSkills.DIRECTCHILD -> this.border(
                        width = border_width_parent_child,
                        brush = border_brush_DIRECTCHILD,
                        shape = shapeCard
                    )
                    MarkerNodeTreeSkills.INDIRECTCHILD -> this.border(
                        width = border_width_parent_child,
                        brush = border_brush_INDIRECTCHILD,
                        shape = shapeCard
                    )
                    else -> this
                } else this
            },
            backBrush = if (item.complete == TypeStatNodeTree.COMPLETE) background_brush_complete else  if (item.complete == TypeStatNodeTree.UNBLOCKNOW) background_brush_unblock else if (item.open) null else background_brush_block,
            borderBrush = if (item.complete == TypeStatNodeTree.COMPLETE) border_brush_complete else if (item.open) null else border_brush_block,
            dropMenu = { exp -> if (block.not()) dropMenu(item, exp) },
            styleSettings = itemNodeTreeSkillStyleState
        ) {
//            RowVA(
/*
                Modifier.padding(horizontal = 2.dp).padding(vertical = 2.dp).run {

                    if (block) this.background(Color.White.copy(alpha = 0.5f)) else if (item.complete == TypeStatNodeTree.UNBLOCKNOW) this.background(
                        MyColorARGB.colorUnblockNowElement.toColor().copy(alpha = 0.7f)
                    ) else this
//                    RoundedCornerShape(15.dp)
                },
*/
//            ) {
                Column(
//                    Modifier.run { if (block) this.background(Color.White.copy(alpha = 0.5f)) else this },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        Box(contentAlignment = Alignment.Center) {
                            if (item.complete != TypeStatNodeTree.INVIS) ShowIconNodeFromComlete(item, pair = border_width_icon to BORDER_ICON_COLOR)
                            if (item.complete == TypeStatNodeTree.BLOCK || item.complete == TypeStatNodeTree.INVIS) Image(
                                painterResource(if (item.complete == TypeStatNodeTree.BLOCK) "ic_round_lock_24.xml" else "ic_round_visibility_off_24.xml"), //BitmapPainter(
                                "statDenPlan",
                                Modifier
                                    .height(80.dp)
                                    .width(80.dp),
                                alpha = 0.7F,
                                contentScale = ContentScale.FillBounds,
                            )
                        }
                        if (selection.isActive(item) && block.not()) MyButtDropdownMenuStyle2(
                            Modifier.padding(top = 0.dp).padding(vertical = 0.dp).align(Alignment.TopStart),
                            expandedDropMenu,
                            buttMenu
                        ) {
                            dropMenu(item, expandedDropMenu)
                        }

                        if (item.complete != TypeStatNodeTree.INVIS) Text(
                            text = "(${
                                when (item) {
                                    is ItemCountNodeTreeSkills -> "С"
                                    is ItemHandNodeTreeSkills -> "Р"
                                    is ItemPlanNodeTreeSkills -> "П"
                                    else -> ""
                                }
                            })",
                            modifier = Modifier.padding(top = 18.dp, end = 5.dp).align(Alignment.TopEnd).alpha(0.3f),
                            style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start, fontSize = 8.sp)
                        )
                        if (item.quest_id != 0L && item.quest_key_id == 0L) Text(
                            modifier = Modifier.padding(end = 5.dp).align(Alignment.TopEnd),
                            text = "*",
                            style = noQuestText
                        )
                    }
                    if (item.complete != TypeStatNodeTree.INVIS) Text(
                        text = item.name, // "(${item.id_type_node})${item.name}",
                        modifier = Modifier.padding(horizontal = 5.dp).padding(bottom = 7.dp),
                        style = mainTextStyle // MyTextStyleParam.style1.copy(textAlign = TextAlign.Start, fontSize = 12.sp)
                    )
                }
//            }
        }
    }
}


/**
 * Айтем для отображения ачивок с чекбоксом для выделения в панели выбора родителей.
 * */
class ComItemNodeLevelTreeSkillsCheckable(
    val item: ItemNodeTreeSkills,
    val selection: SingleSelectionType<ItemNodeTreeSkills>,
) {

    @Composable
    fun getComposable() {
        val markerCheck = remember { mutableStateOf(item.check) }
        fun checkedItem() {
            if (item.marker != MarkerNodeTreeSkills.UNENABLE && item.marker != MarkerNodeTreeSkills.UNENABLELVL2) {
                MainDB.avatarFun.changeCheckNode(item)
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
            if (selection.selected == null) false else false,
            0,
            {
//            selection.selected = item
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
                if (item.complete != TypeStatNodeTree.BLOCK && item.complete != TypeStatNodeTree.INVIS) Checkbox(
                    markerCheck.value,
                    modifier = Modifier.padding(start = 0.dp),
                    onCheckedChange = {
                        checkedItem()
                    })
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(contentAlignment = Alignment.Center) {
                        if (item.complete != TypeStatNodeTree.INVIS) ShowIconNodeFromComlete(item)
                        if (item.complete == TypeStatNodeTree.BLOCK || item.complete == TypeStatNodeTree.INVIS) Image(
                            painterResource(if (item.complete == TypeStatNodeTree.BLOCK) "ic_round_lock_24.xml" else "ic_round_visibility_off_24.xml"), //BitmapPainter(
                            "statDenPlan",
                            Modifier
                                .height(80.dp)
                                .width(80.dp),
                            alpha = 0.7F,
                            contentScale = ContentScale.FillBounds,
                        )
                        if (item.quest_id != 0L && item.quest_key_id == 0L) Text(
                            modifier = Modifier.padding(end = 5.dp).matchParentSize(),
                            text = "*",
                            style = TextStyle(color = MyColorARGB.colorDoxodItem0.toColor(), textAlign = TextAlign.End),
                            fontSize = 25.sp
                        )
                    }
                    if (item.complete != TypeStatNodeTree.INVIS) Text(
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
class ComItemNodeLevelTreeSkillsSelParents(
    val item: ItemNodeTreeSkills,
) {
    @Composable
    fun getComposable() {
        val markerCheck = remember { mutableStateOf(item.check) }
        MyCardStyle1(false, 0, {
//            selection.selected = item
        }, {
//            item.marker = item.marker.not()
//            markerCheck.value = !markerCheck.value
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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box {
                        Box(contentAlignment = Alignment.Center) {
                            if (item.complete != TypeStatNodeTree.INVIS) ShowIconNodeFromComlete(item)
                            if (item.complete == TypeStatNodeTree.BLOCK || item.complete == TypeStatNodeTree.INVIS) Image(
                                painterResource(if (item.complete == TypeStatNodeTree.BLOCK) "ic_round_lock_24.xml" else "ic_round_visibility_off_24.xml"), //BitmapPainter(
                                "statDenPlan",
                                Modifier
                                    .height(80.dp)
                                    .width(80.dp),
                                alpha = 0.7F,
                                contentScale = ContentScale.FillBounds,
                            )
                        }
                        if (item.complete != TypeStatNodeTree.INVIS) Text(
                            text = "(${item.level})",
                            modifier = Modifier.padding(5.dp).align(Alignment.TopStart),
                            style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start, fontSize = 8.sp)
                        )
                        if (item.quest_id != 0L && item.quest_key_id == 0L) Text(
                            modifier = Modifier.padding(end = 5.dp).align(Alignment.TopEnd),
                            text = "*",
                            style = TextStyle(color = MyColorARGB.colorDoxodItem0.toColor()),
                            fontSize = 25.sp
                        )
                    }
                    if (item.complete != TypeStatNodeTree.INVIS) Text(
                        text = "(${item.id_type_node})${item.name}",
                        modifier = Modifier.padding(5.dp),
                        style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start, fontSize = 12.sp)
                    )
                }
            }
        }
    }
}
