package MainTabs.Quest.Items


import MainTabs.Quest.Element.PanAddNodeTreeSkillsQuest
import MyDialog.MyDialogLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.RowVA
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemLevelTreeSkillsQuest
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemNodeTreeSkillsQuest
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStartObjOfTrigger
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeTreeSkills
import viewmodel.QuestDB
import viewmodel.QuestVM

@Composable
private fun MainOpisLevel(
    item: ItemLevelTreeSkillsQuest,
    opisOn: MutableState<Boolean>?,
    rowAdvanc: @Composable RowScope.() -> Unit = {}
) {
    RowVA {
        Text(
            text = "Уровень ${item.num_level}. ${item.name}",
            modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 5.dp).weight(1f),
            style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start, fontSize = 18.sp)
        )
        Text(
            text = "${item.countNode} = ${item.mustCountNode} + ${item.countNode - item.mustCountNode}(~${
                ((item.countNode - item.mustCountNode) * item.proc_porog / 100.0).roundToString(
                    1
                )
            }-${(item.proc_porog).toInt()}%)",
            modifier = Modifier.padding(start = 5.dp, top = 10.dp, bottom = 5.dp, end = 5.dp),
            style = MyTextStyleParam.style1.copy(
                textAlign = TextAlign.Start,
                fontSize = 12.sp,
                color = MyColorARGB.colorSchetTheme.toColor()
            )
        )
        if (item.visible_stat == -2L || item.visible_stat == -3L) Image(
            painterResource(if (item.visible_stat == -2L) "ic_round_lock_24.xml" else "ic_round_visibility_off_24.xml"),
            "statDenPlan",
            Modifier
                .height(50.dp)
                .width(50.dp),
            alpha = 0.7F,
            contentScale = ContentScale.FillBounds,
        )
        rowAdvanc()
    }
    opisOn?.let { expandedOpis ->
        if ((item.opis != "")) {
            BoxExpand(
                expandedOpis,
                Modifier.padding(horizontal = 10.dp).padding(bottom = 5.dp).myModWithBound1(),
                Modifier.fillMaxWidth()
            ) {
                MyTextStyle2(
                    item.opis,
                    textAlign = TextAlign.Start,
                    fontSize = 15.sp
                )
            }
        }
    }
    PlateOrderLayout(Modifier.padding(start = 15.dp)) {
        QuestVM.getTriggerMarkersForTriggerChilds(
            TypeStartObjOfTrigger.STARTLEVELTREE.id,
            item.id.toLong(),
            emptyMarker = (item.visible_stat == -2L || item.visible_stat == -3L)
        )
    }
}

class ComItemLevelTreeSkillsQuest(
    val dialLay: MyDialogLayout,
    val questDB: QuestDB,
    val item: ItemLevelTreeSkillsQuest,
    val selection: SingleSelectionType<ItemNodeTreeSkillsQuest>,
    val listNode: List<ItemNodeTreeSkillsQuest>,
    val dropMenu: @Composable ColumnScope.(ItemLevelTreeSkillsQuest, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    var expandedDropMenu = mutableStateOf(false)
    val expandedOpis = mutableStateOf(!item.sver)

    @Composable
    fun getComposable() {
        MyCardStyle1(false, 0, {

        }, onDoubleClick = {
            item.sver = item.sver.not()
            expandedOpis.value = !expandedOpis.value
        },
            dropMenu = { exp -> dropMenu(item, exp) }
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                MainOpisLevel(item, expandedOpis) {
                    MyButtDropdownMenuStyle2(
                        Modifier.padding(top = 3.dp).padding(vertical = 0.dp),
                        expandedDropMenu
                    ) {
                        dropMenu(item, expandedDropMenu)
                    }
                    if (item.opis != "") RotationButtStyle1(
                        expandedOpis,
                        Modifier.padding(start = 10.dp, end = 15.dp)
                    ) {
                        item.sver = item.sver.not()
                    }
                    MyTextButtStyle1("+", modifier = Modifier.padding(end = 15.dp)) {
                        PanAddNodeTreeSkillsQuest(
                            dialLay, questDB,
                            item.id_tree,
                            item.num_level, typeTree = TypeTreeSkills.LEVELS
                        )
                    }
                }
                PlateOrderLayout(Modifier.padding(bottom = 8.dp), alignmentCenter = true) {
                    listNode.forEach { nodeTreeSkills ->
                        ComItemNodeTreeSkillsQuest(
                            nodeTreeSkills,
                            questDB,
                            TypeTreeSkills.LEVELS,
                            selection,
                            dialLay
                        ).getComposable()
                    }
                }
            }
        }
    }
}

class ComItemLevelTreeSkillsForSelectNodeQuest(
    val questDB: QuestDB,
    val item: ItemLevelTreeSkillsQuest,
    val selection: SingleSelectionType<ItemNodeTreeSkillsQuest>,
    val listNode: List<ItemNodeTreeSkillsQuest>,
) {
    var expandedDropMenu = mutableStateOf(false)

    @Composable
    fun getComposable() {
        MyCardStyle1(false, 0) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                MainOpisLevel(item, null)
                PlateOrderLayout(Modifier.padding(bottom = 8.dp), alignmentCenter = true) {
                    listNode.forEach { nodeTreeSkills ->
                        ComItemNodeTreeSkillsSelParentsQuest(
                            nodeTreeSkills,
                            questDB,
                            selection
                        ).getComposable()
                    }
                }
            }
        }
    }
}

class ComItemLevelTreeSkillsForSelectQuest(
    val questDB: QuestDB,
    val item: ItemLevelTreeSkillsQuest,
    val selection: SingleSelectionType<ItemLevelTreeSkillsQuest>,
    val listNode: List<ItemNodeTreeSkillsQuest>,
) {
    var expandedDropMenu = mutableStateOf(false)
    val expandedOpis = mutableStateOf(!item.sver)

    @Composable
    fun getComposable() {
        MyCardStyle1(selection.isActive(item), 0, {
            selection.selected = item
        }) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                MainOpisLevel(item, null)
                PlateOrderLayout(Modifier.padding(bottom = 8.dp), alignmentCenter = true) {
                    listNode.forEach { nodeTreeSkills ->
                        ComItemNodeTreeSkillsSelParentsQuest(
                            nodeTreeSkills,
                            questDB,
                            null
                        ).getComposable()
                    }
                }
            }
        }
    }
}
