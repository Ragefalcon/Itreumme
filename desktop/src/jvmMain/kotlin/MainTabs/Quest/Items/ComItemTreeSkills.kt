package MainTabs.Quest.Items

import androidx.compose.material.Text
import MainTabs.imageFromFile
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.*
import ru.ragefalcon.sharedcode.models.data.ItemTreeSkill
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatTreeSkills
import viewmodel.MainDB
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComItemTreeSkills(
    item: ItemTreeSkill,
    selection: SingleSelectionType<ItemTreeSkill>,
//    val doubleClick: (ItemTreeSkill)->Unit = {},
    openTree: (ItemTreeSkill) -> Unit = {},
    itemTreeSkillStyleState: ItemSkillsTreeState,
    dropMenu: @Composable ColumnScope.(ItemTreeSkill, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    with(itemTreeSkillStyleState) {

        val expandedDropMenu = remember { mutableStateOf(false) }
        val expandedOpis = remember { mutableStateOf(!item.sver) }
        MyCardStyle1(selection.isActive(item), 0, {
            if (item.stat == TypeStatTreeSkills.UNBLOCKNOW) MainDB.addAvatar.setOpenEditTreeSkills(
                item.id.toLong(),
                TypeStatTreeSkills.VISIB
            )
            selection.selected = item
        }, onDoubleClick = {
            openTree(item)
//            if (item.stat != TypeStatTreeSkills.BLOCK) {
//                item.sver = item.sver.not()
//                expandedOpis.value = !expandedOpis.value
//            }
        },
            backBrush =  if (item.completeCountNode == item.countNode) background_brush_complete else when (item.stat) {
                TypeStatTreeSkills.OPEN_EDIT -> null
                TypeStatTreeSkills.VISIB -> background_brush_no_edit
                TypeStatTreeSkills.COMPLETE -> null
                TypeStatTreeSkills.UNBLOCKNOW -> background_brush_unblock
                TypeStatTreeSkills.BLOCK -> background_brush_block
                TypeStatTreeSkills.INVIS -> null
                else -> null
            },
            borderBrush = if (item.completeCountNode == item.countNode) border_brush_complete else when (item.stat) {
                TypeStatTreeSkills.OPEN_EDIT -> null
                TypeStatTreeSkills.VISIB -> border_brush_no_edit
                TypeStatTreeSkills.COMPLETE -> null
                TypeStatTreeSkills.UNBLOCKNOW -> border_brush_unblock
                TypeStatTreeSkills.BLOCK -> border_brush_block
                TypeStatTreeSkills.INVIS -> null
                else -> null
            },
            modifierThen = if (item.namequest == "") Modifier else Modifier.border(
                width = BORDER_WIDTH_QUEST,
                brush = border_quest,
                shape = shapeCard
            ),
            dropMenu = { exp -> dropMenu(item, exp) },
                    styleSettings = itemTreeSkillStyleState
        ) {
            if (item.stat == TypeStatTreeSkills.INVIS) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painterResource("ic_round_visibility_off_24.xml"), //BitmapPainter(
                        "statDenPlan",
                        Modifier
                            .height(80.dp)
                            .width(80.dp),
                        alpha = 0.7F,
                        contentScale = ContentScale.FillBounds,
                        colorFilter = ColorFilter.tint(INFO_ICON_COLOR)
                    )
                }
            } else Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (item.namequest != "") {
                    MyShadowBox(quest_plate.shadow){
                        RowVA(Modifier.fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .withSimplePlate(quest_plate)
                            .padding(3.dp)
                            .padding(horizontal = 2.dp),
                            horizontalArrangement = Arrangement.Center) {
                            Text(
                                item.namequest,
                                Modifier.weight(1f,false),
                                style = questNameText
                            )
                        }
                    }
                }

                RowVA(Modifier.padding(top = topPadding)) {
                    Box(contentAlignment = Alignment.Center) {
                        MyShadowBox(quest_plate.shadow){
                            Image(
                                bitmap = useResource("free-icon-tree-shape-42090.png",::loadImageBitmap),
                                "defaultAvatar",
                                Modifier
                                    .height(70.dp)
                                    .width(70.dp)
                                    .withSimplePlate(icon_plate)
                                    .clip(icon_plate.shape)
                                    .wrapContentSize(),
                                contentScale = ContentScale.Crop,// Fit,
                                colorFilter = ColorFilter.tint(ICON_TREE_COLOR)
                            )
                        }
/*
                        IconNode(
                            File(System.getProperty("user.dir"), "Quests").path,
                            "${item.icon}.jpg",
                            "free-icon-tree-shape-42090.png",
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
*/
                        if (item.stat == TypeStatTreeSkills.BLOCK) Image(
                            painterResource("ic_round_lock_24.xml"), //BitmapPainter(
                            "statDenPlan",
                            Modifier
                                .height(50.dp)
                                .width(50.dp),
                            alpha = 0.7F,
                            contentScale = ContentScale.FillBounds,
                            colorFilter = ColorFilter.tint(INFO_ICON_COLOR),
                        )
                    }
                    Text(
                        text = item.name,
                        modifier = Modifier.padding(start = 15.dp).weight(1f),
                        style = mainTextStyle
                    )
                    if (item.stat != TypeStatTreeSkills.BLOCK) {
                        Text(
                            text = "(${item.completeCountNode}/${item.countNode})",
                            modifier = Modifier.padding(start = 5.dp, top = 10.dp, bottom = 5.dp, end = 5.dp),
                            style = countStapText
                        )
                        if (selection.isActive(item)) MyButtDropdownMenuStyle2(
                            Modifier.padding(top = 3.dp).padding(vertical = 0.dp),
                            expandedDropMenu,
                            buttMenu
                        ) {
                            dropMenu(item, expandedDropMenu)
                        }
                        if (item.opis != "") RotationButtStyle1(
                            expandedOpis,
                            Modifier.padding(start = 10.dp, end = 10.dp),
                            color = boxOpisStyleState.colorButt
                        ) {
                            item.sver = item.sver.not()
                        }
                        MyTextButtSimpleStyle(
                            text = "\uD83D\uDD6E",
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .padding(end = 20.dp),
                            fontSize = 20.sp,
                            color = OPEN_COLOR
                        ) {
                            openTree(item)
                        }
                    }
                }
                if ((item.opis != "")) MyBoxOpisStyle(expandedOpis,item.opis,boxOpisStyleState)
            }
        }
    }
}

@Composable
fun ComItemTreeSkillsPlate(
    item: ItemTreeSkill,
) {
    val avatarFile = File(
        File(System.getProperty("user.dir"), "Quests").path,
        "${item.icon}.jpg"
//        "${File(QuestVM.openQuestDB?.arg?.path ?: "").nameWithoutExtension}_${item.id}.jpg"
    )
    val avatarF =
        if (avatarFile.exists()) imageFromFile(avatarFile) else useResource(
            "free-icon-tree-shape-42090.png",
            ::loadImageBitmap
        )

    val shape = RoundedCornerShape(75.dp) //CircleShape
    Row(
        Modifier.padding(vertical = 5.dp)//.height(130.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            bitmap = avatarF,
            "defaultAvatar",
            Modifier.wrapContentSize().padding(horizontal = 5.dp)
                .height(50.dp)
                .width(50.dp)
                .clip(shape)
                .border(1.dp, Color.White, shape)
                .padding(1.dp)
                .border(3.dp, Color(0x7FFFF7D9), RoundedCornerShape(74.dp))
                .shadow(2.dp, shape),
            contentScale = ContentScale.Crop,// Fit,
        )
        Text(
            text = item.name,
            modifier = Modifier,
            style = MyTextStyleParam.style2.copy(fontSize = 15.sp, textAlign = TextAlign.Start)
        )
    }
}