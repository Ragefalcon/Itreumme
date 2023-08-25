package MainTabs.Quest.Items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.mouseClickable
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
import common.tests.IconNode
import extensions.RowVA
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemTreeSkillsQuest
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStartObjOfTrigger
import viewmodel.QuestVM
import java.io.File

class ComItemTreeSkillsQuest(
    val item: ItemTreeSkillsQuest,
    val selection: SingleSelectionType<ItemTreeSkillsQuest>,
    val openTree: (ItemTreeSkillsQuest) -> Unit = {},
    val dropMenu: @Composable ColumnScope.(ItemTreeSkillsQuest, MutableState<Boolean>) -> Unit = { _, _ -> }
) {
    var expandedDropMenu = mutableStateOf(false)
    val expandedOpis = mutableStateOf(!item.sver)

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun getComposable() {
        MyCardStyle1(selection.isActive(item), 0, {
            selection.selected = item
        }, onDoubleClick = {
            openTree(item)
        },
            backColor = MyColorARGB.colorMyMainTheme.toColor(),
            dropMenu = { exp -> dropMenu(item, exp) }
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                RowVA {
                    Box(contentAlignment = Alignment.Center) {
                        IconNode(
                            File(System.getProperty("user.dir"), "Quests").path,
                            "${item.icon}.jpg",
                            "free-icon-tree-shape-42090.png",
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        if (item.visibleStat == -2L || item.visibleStat == -3L) Image(
                            painterResource(if (item.visibleStat == -2L) "ic_round_lock_24.xml" else "ic_round_visibility_off_24.xml"),
                            "statDenPlan",
                            Modifier
                                .height(50.dp)
                                .width(50.dp),
                            alpha = 0.7F,
                            contentScale = ContentScale.FillBounds,
                        )
                    }
                    Column(Modifier.padding(5.dp).weight(1f)) {
                        Text(
                            text = "(${item.id_type_tree})(${item.countNode})${item.name}",
                            modifier = Modifier.padding(5.dp),
                            style = MyTextStyleParam.style1.copy(textAlign = TextAlign.Start)
                        )
                        PlateOrderLayout() {
                            QuestVM.getTriggerMarkersForTriggerChilds(
                                TypeStartObjOfTrigger.STARTTREE.id,
                                item.id.toLong(),
                                emptyMarker = (item.visibleStat == -2L || item.visibleStat == -3L)
                            )
                        }
                    }
                    if (selection.isActive(item)) MyButtDropdownMenuStyle2(
                        Modifier.padding(top = 3.dp).padding(vertical = 0.dp),
                        expandedDropMenu
                    ) {
                        dropMenu(item, expandedDropMenu)
                    }
                    if (item.opis != "") RotationButtStyle1(
                        expandedOpis,
                        Modifier.padding(start = 10.dp, end = 10.dp)
                    ) {
                        item.sver = item.sver.not()
                    }
                    MyTextStyle1(
                        "\uD83D\uDD6E",
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .padding(end = 20.dp)
                            .mouseClickable {
                                openTree(item)
                            })
                }
                if ((item.opis != "")) {
                    BoxExpand(
                        expandedOpis,
                        Modifier.myModWithBound1(),
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
        }
    }
}

