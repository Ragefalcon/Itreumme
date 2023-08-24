package MainTabs.Quest.Element

import MainTabs.Quest.Items.ComItemNodeLevelTreeSkillsSelParents
import MainTabs.Time.Items.ComItemPlanPlate
import MainTabs.Time.Items.ComItemPlanStapPlate
import MainTabs.imageFromFile
import MyDialog.MyDialogLayout
import MyDialog.MyInfoShow
import MyListRow
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import common.tests.IconNode
import extensions.*
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemCountNodeTreeSkills
import ru.ragefalcon.sharedcode.models.data.ItemHandNodeTreeSkills
import ru.ragefalcon.sharedcode.models.data.ItemNodeTreeSkills
import ru.ragefalcon.sharedcode.models.data.ItemPlanNodeTreeSkills
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeIconBorder
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatNodeTree
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeTreeSkills
import viewmodel.MainDB
import java.io.File

fun ShowOpisNodeTreeSkills(
    dialLay: MyDialogLayout,
    typeTree: TypeTreeSkills,
    item: ItemNodeTreeSkills
) {
    val avatarFile = File(
        File(System.getProperty("user.dir"), "Quests").path,
        "${item.icon}.jpg"
//        "${File(QuestVM.openQuestDB?.arg?.path ?: "").nameWithoutExtension}_${item.id}.jpg"
    )
    val avatarF =
        if (avatarFile.exists()) imageFromFile(avatarFile) else useResource(
            "icon_skill_color_lamp.png",
            ::loadImageBitmap
        )

    val shape = RoundedCornerShape(35.dp) //CircleShape

    if (typeTree == TypeTreeSkills.TREE) {
        MainDB.avatarFun.updateInfoSpisNode(item)
    }
    val expandedOpis = mutableStateOf(!item.sver)

    MyInfoShow(dialLay) {
        Column(
            Modifier.padding(20.dp).width(dialLay.layWidth.value * 0.8f)
                .fillMaxHeight(0.8f)//.heightIn(0.dp, 500.dp) .fillMaxSize(0.8f)//
        ) {
            Row(
                Modifier.padding(horizontal = 2.dp).padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconNode(
                    item.icon,
                    "icon_skill_color_lamp.png",
                    complete = item.icon_complete?.let { false } ?: item.complete == TypeStatNodeTree.COMPLETE,
                    size = 100.dp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
//                ShowIconNodeFromComlete(item, 100.dp)
                Column(Modifier.weight(1f, false), horizontalAlignment = Alignment.CenterHorizontally) {
                    RowVA {
                        Text(
                            text = "(${item.id_type_node})${item.name}",
                            modifier = Modifier.padding(5.dp),
                            style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start)
                        )
                        if (item.quest_id != 0L && item.quest_key_id == 0L) Text(
                            modifier = Modifier.padding(5.dp),
                            text = "*",
                            style = TextStyle(color = MyColorARGB.colorDoxodItem0.toColor(), textAlign = TextAlign.End),
                            fontSize = 25.sp
                        )
                    }
                    RowVA {
                        Text(
                            text = "Тип выполнения: ${
                                when (item) {
                                    is ItemCountNodeTreeSkills -> "Счетчик"
                                    is ItemHandNodeTreeSkills -> "Ручной"
                                    is ItemPlanNodeTreeSkills -> "Проект"
                                    else -> ""
                                }
                            }",
                            modifier = Modifier.padding(5.dp),
                            style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start, fontSize = 12.sp)
                        )
                        RotationButtStyle1(
                            expandedOpis,
                            Modifier.padding(start = 10.dp, end = 15.dp)
                        ) {}
                    }
                    BoxExpand(
                        expandedOpis,
                        Modifier.padding(horizontal = 10.dp).padding(bottom = 5.dp).myModWithBound1(),
                        Modifier.fillMaxWidth()
                    ) {
                        MyTextStyle2(
                            when (item) {
//                                    is ItemCountNodeTreeSkills -> "Счетчик"
                                is ItemHandNodeTreeSkills -> "После выполнения условий/заданий пользователь сам выставляет выполнение достижения (правая кнопка мышы на достижении и \"Выполнить\")."
                                is ItemPlanNodeTreeSkills -> "Достижение будет автоматически защитано после того как пользователь выполнит привязанный к достижению проект/этап, который указан ниже."
                                else -> ""
                            },
                            textAlign = TextAlign.Start,
                            fontSize = 12.sp
                        )
                    }
                    if (typeTree == TypeTreeSkills.LEVELS) {
                        MainDB.avatarSpis.spisLevelTreeSkills.getState().value?.let { listLevel ->
                            listLevel.find { it.num_level == item.level }?.let {
                                Text(
                                    text = "Уровень ${it.num_level}. ${it.name}",
                                    modifier = Modifier.padding(5.dp),
                                    style = MyTextStyleParam.style2.copy(textAlign = TextAlign.Start, fontSize = 14.sp)
                                )
                            }
                        }
                    }
                }
                if (item.complete == TypeStatNodeTree.COMPLETE && item.icon_complete != null) {
                    IconNode(
                        item.icon_complete ?: item.icon,
                        "icon_skill_color_lamp.png",
                        complete = item.icon_complete?.let { TypeIconBorder.getType(it.type_ramk) != TypeIconBorder.NONE }
                            ?: true,
                        size = 100.dp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
            if (typeTree == TypeTreeSkills.TREE) {
                MyListRow(
                    MainDB.avatarSpis.spisNodeTreeSkillsForInfo,
                    Modifier.padding(top = 8.dp).heightIn(0.dp, 150.dp)
                ) { ind, nodeTreeSkills -> //.heightIn(0.dp, 150.dp) .height(150.dp)
                    ComItemNodeLevelTreeSkillsSelParents(nodeTreeSkills).getComposable()
                }
            }
            if (item is ItemPlanNodeTreeSkills) {
                Text(
                    "Привязанный проект/этап",
                    Modifier.padding(top = 15.dp),
                    style = MyTextStyleParam.style1.copy(fontSize = 18.sp)
                )
                MainDB.timeSpis.spisAllPlan.getState().value?.find { itemPlan ->
                    itemPlan.id.toLong() == item.privplan
                }?.let {
                    ComItemPlanPlate(
                        it,
                        modifier = Modifier.padding(bottom = 5.dp),
                        itemPlanStyleState = ItemPlanStyleState(MainDB.styleParam.timeParam.planTab.itemPlan)
                    )
                }
                MainDB.timeSpis.spisAllPlanStap.getState().value?.find { itemPlanStap ->
                    itemPlanStap.id.toLong() == item.stap_prpl
                }?.let {
                    ComItemPlanStapPlate(
                        it,
                        Modifier.padding(horizontal = 20.dp),
                        itemPlanStapStyleState = ItemPlanStapStyleState(MainDB.styleParam.timeParam.planTab.itemPlanStap)
                    )
                }
            }
            Text(
                "Описание",
                Modifier.padding(top = 15.dp),
                style = MyTextStyleParam.style1.copy(fontSize = 18.sp)
            )
            val scroll = rememberScrollState(0)
            BoxWithVScrollBar(Modifier.padding(10.dp), scroll) { scrollStateBox ->
                SelectionContainer {
                    Text(
                        item.opis,
                        Modifier.verticalScroll(scrollStateBox, enabled = true),
                        style = MyTextStyleParam.style2.copy(textAlign = TextAlign.Start, fontSize = 15.sp)
                    )
                }
            }
        }
    }
}


