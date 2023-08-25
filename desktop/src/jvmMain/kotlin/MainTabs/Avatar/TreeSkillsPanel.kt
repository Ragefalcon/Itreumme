package MainTabs.Avatar

import MainTabs.Avatar.Element.PanAddTreeSkills
import MainTabs.Quest.Element.ComItemTreeSkillsOpened
import MainTabs.Quest.Items.ComItemTreeSkills
import MyDialog.MyDialogLayout
import MyList
import MyShowMessage
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import common.*
import common.tests.CommonOpenItemPanel
import extensions.*
import ru.ragefalcon.sharedcode.models.data.ItemTreeSkill
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeStatTreeSkills
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeTreeSkills
import viewmodel.MainDB

class TreeSkillsPanel {
    val vypSkills = mutableStateOf(false)
    val commonSkillPanel = CommonOpenItemPanel<ItemTreeSkill>(
        whenOpen = {
            TypeTreeSkills.Companion.getType(it.id_type_tree)?.let { typeTree ->
                MainDB.avatarFun.setSelectTreeSkills(it.id.toLong(), typeTree)
            }
        },
        mainSpis = { modifierList, selection, openSpis_Index_Item, lazyListState, dialLay ->
            MainDB.styleParam.avatarParam.skillTab.itemSkill.getComposable(::ItemSkillsTreeState) { itemStyle ->
                MyList(
                    MainDB.avatarSpis.spisTreeSkills,
                    Modifier.weight(1f).padding(horizontal = 60.dp, vertical = 10.dp).then(modifierList),
                    lazyListState
                ) { ind, itemTreeSkill ->
                    if (itemTreeSkill.stat != TypeStatTreeSkills.INVIS) ComItemTreeSkills(
                        itemTreeSkill,
                        selection,
                        openTree = {
                            openSpis_Index_Item(ind, it)
                        }, itemTreeSkillStyleState = itemStyle
                    ) { item, expanded ->
                        @Composable
                        fun visibMenu() {
                            MyDropdownMenuItem(expanded, "Открыть редактирование") {
                                MainDB.addAvatar.setOpenEditTreeSkills(
                                    item.id.toLong(),
                                    TypeStatTreeSkills.OPEN_EDIT
                                )
                            }
                        }
                        when (item.stat) {
                            TypeStatTreeSkills.OPEN_EDIT -> {
                                MyDropdownMenuItem(expanded, "Закрыть редактирование") {
                                    MainDB.addAvatar.setOpenEditTreeSkills(
                                        item.id.toLong(),
                                        TypeStatTreeSkills.VISIB
                                    )
                                }
                                dialLay?.let { dialL ->
                                    if (item.quest_id == 0L) MyDropdownMenuItem(expanded, "Изменить") {
                                        PanAddTreeSkills(dialL, item)
                                    }
                                    MyDeleteDropdownMenuButton(expanded) {
                                        if (item.quest_id == 0L) {
                                            if (item.countNode == 0L) {
                                                MainDB.addAvatar.delTreeSkills(item.id.toLong())
                                                selection.selected = null

                                            } else {
                                                MyShowMessage(dialL, "Удалите вначале все содержимое дерева")
                                            }
                                        } else {
                                            MyShowMessage(
                                                dialL,
                                                "Этот элемент из квеста, его можно удалить только вместе с квестом."
                                            )
                                        }
                                    }
                                }
                            }

                            TypeStatTreeSkills.VISIB -> visibMenu()
                            TypeStatTreeSkills.COMPLETE -> Unit
                            TypeStatTreeSkills.UNBLOCKNOW -> visibMenu()
                            TypeStatTreeSkills.BLOCK -> Unit
                            TypeStatTreeSkills.INVIS -> Unit
                        }

                    }
                }
            }
            with(MainDB.styleParam.avatarParam.skillTab) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MyToggleButtIconStyle1(
                        "ic_round_settings_24.xml", value = vypSkills,
                        modifier = Modifier.padding(start = 15.dp),
                        width = 50.dp,
                        height = 50.dp,
                        myStyleToggleButton = ToggleButtonStyleState(buttEdit)
                    )
                    Text(
                        "Ветвей навыков: ${MainDB.avatarSpis.spisTreeSkills.getState().value?.size ?: 0}",
                        Modifier.weight(1f).padding(horizontal = 10.dp),
                        style = textCount.getValue().copy(textAlign = TextAlign.Center)
                    )
                    MyTextButtStyle1(
                        "+",
                        modifier = Modifier.padding(end = 15.dp),
                        width = 50.dp,
                        height = 50.dp,
                        myStyleTextButton = TextButtonStyleState(buttAdd)
                    ) {
                        dialLay?.let { PanAddTreeSkills(it) }
                    }
                }
            }
        },
        openedItem = { itemTS, startOpenAnimation, _, dialLay ->
            if (dialLay != null) ComItemTreeSkillsOpened(
                dialLay, itemTS,
                openTree = {
                    startOpenAnimation.value = false
                }, itemTreeSkillStyleState = ItemSkillsTreeState(MainDB.styleParam.avatarParam.skillTab.itemSkill)
            )
        }
    )

    @Composable
    fun show(
        dialLay: MyDialogLayout,
        modifier: Modifier = Modifier
    ) {
        commonSkillPanel.show(modifier, dialLay)
    }
}