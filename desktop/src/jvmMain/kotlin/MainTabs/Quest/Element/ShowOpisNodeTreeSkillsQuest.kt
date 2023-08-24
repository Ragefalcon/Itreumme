package MainTabs.Quest.Element

import androidx.compose.material.Text
import MainTabs.Quest.Items.ComItemNodeTreeSkillsSelParentsQuest
import MainTabs.Quest.Items.ItemPlanQuestPlate
import MainTabs.Quest.Items.ItemPlanStapQuestPlate
import MyDialog.MyDialogLayout
import MyDialog.MyInfoShow
import MyListRow
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import common.tests.IconNode
import extensions.BoxWithVScrollBar
import extensions.RowVA
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemCountNodeTreeSkillsQuest
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemHandNodeTreeSkillsQuest
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemNodeTreeSkillsQuest
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemPlanNodeTreeSkillsQuest
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeIconBorder
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeTreeSkills
import viewmodel.QuestDB

fun ShowOpisNodeTreeSkillsQuest(
    dialLay: MyDialogLayout,
    typeTree: TypeTreeSkills,
    questDB: QuestDB,
    item: ItemNodeTreeSkillsQuest
) {
    if (typeTree == TypeTreeSkills.TREE) {
        questDB.questFun.updateInfoSpisNode(item)
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
                    questDB.dirQuest,
                    complete = false,
                    size = 100.dp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Column(Modifier.weight(1f, false), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "(${item.id_type_node})${item.name}",
                        modifier = Modifier.padding(5.dp),
                        style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start)
                    )
                    RowVA {
                        Text(
                            text = "Тип выполнения: ${
                                when (item) {
                                    is ItemCountNodeTreeSkillsQuest -> "Счетчик"
                                    is ItemHandNodeTreeSkillsQuest -> "Ручной"
                                    is ItemPlanNodeTreeSkillsQuest -> "Проект"
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
                                is ItemHandNodeTreeSkillsQuest -> "После выполнения условий/заданий пользователь сам выставляет выполнение достижения (правая кнопка мышы на достижении и \"Выполнить\")."
                                is ItemPlanNodeTreeSkillsQuest -> "Достижение будет автоматически защитано после того как пользователь выполнит привязанный к достижению проект/этап, который указан ниже."
                                else -> ""
                            },
                            textAlign = TextAlign.Start,
                            fontSize = 12.sp
                        )
                    }
                    if (typeTree == TypeTreeSkills.LEVELS) {
                        questDB.spisQuest.spisLevelTreeSkills.getState().value?.let { listLevel ->
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
                IconNode(
                    item.icon_complete ?: item.icon,
                    "icon_skill_color_lamp.png",
                    questDB.dirQuest,
                    complete = item.icon_complete?.let { TypeIconBorder.getType(it.type_ramk) != TypeIconBorder.NONE }
                        ?: true,
                    size = 100.dp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
            if (typeTree == TypeTreeSkills.TREE) {
                MyListRow(
                    questDB.spisQuest.spisNodeTreeSkillsForInfo,
                    Modifier.padding(top = 8.dp).heightIn(0.dp, 150.dp)
                ) { ind, nodeTreeSkills -> //.heightIn(0.dp, 150.dp) .height(150.dp)
                    ComItemNodeTreeSkillsSelParentsQuest(nodeTreeSkills,questDB).getComposable()
                }
            }
            if (item is ItemPlanNodeTreeSkillsQuest) {
                Text(
                    "Привязанный проект/этап",
                    Modifier.padding(top = 15.dp),
                    style = MyTextStyleParam.style1.copy(fontSize = 18.sp)
                )
                println("ItemNode: ${item.stap_prpl}")
                questDB.spisQuest.spisPlan.getState().value?.find { itemPlan ->
                    itemPlan.id.toLong() == item.privplan
                }?.let {
                    ItemPlanQuestPlate(
                        it,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                }
                questDB.spisQuest.spisAllStapPlan.getState().value?.let {

                    println("ItemListCount: ${it.size}")
                    it.find { itemPlanStap ->
                        println("ItemNode: ${itemPlanStap.id}")
                        itemPlanStap.id.toLong() == item.stap_prpl
                    }?.let {
                        ItemPlanStapQuestPlate(
                            it,
                            Modifier.padding(horizontal = 20.dp)
                        )
                    }
                }
            }
            Text(
                "Описание",
                Modifier.padding(top = 15.dp),
                style = MyTextStyleParam.style1.copy(fontSize = 18.sp)
            )
            val scroll = rememberScrollState(0)
            BoxWithVScrollBar(Modifier.padding(10.dp), scroll) { scrollStateBox ->
//                            SelectionContainer {
                Text(
                    item.opis,
                    Modifier.verticalScroll(scrollStateBox, enabled = true),
                    style = MyTextStyleParam.style2.copy(textAlign = TextAlign.Start, fontSize = 15.sp)
                )
//                            }
            }
        }
    }
}


