package MainTabs.Avatar.Element

import MainTabs.Avatar.Items.ComItemIconNode
import MyDialog.MyDialogLayout
import MyListPlate
import adapters.MyComboBox
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.*
import common.tests.IconNode
import extensions.RowVA
import ru.ragefalcon.sharedcode.models.data.ItemIconNodeTree
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeIconBorder
import viewmodel.MainDB
import viewmodel.QuestDB
import viewmodel.StateVM
import java.io.File

fun PanCreateIconNode(
    dialLay: MyDialogLayout,
    icon: MutableState<ItemIconNodeTree?>,
    questDB: QuestDB? = null
) {

    dialLay.dial = @Composable {

        val cbTypeIconBorder = MyComboBox(TypeIconBorder.values().toList(), nameItem = { it.type })
        val innerIconBuffer = IconImageBuffer()
        val fileForCrop = remember { CropBoxForImageFile(null, innerIconBuffer, "icon_skill_color_lamp.png") }
        val spisTab = if (questDB == null) listOf("Новая" to "new", "Выбрать из списка" to "spis") else listOf(
            "Новая" to "new",
            "Список квеста" to "spisQuest",
            "Внутренний список" to "spis"
        )
        val seekBar = remember { DiskretSeekBar(spisTab, "new") }

        val selectionIconFromSpis = remember { SingleSelectionType<ItemIconNodeTree>() }
        val selectionIconFromSpisQuest = remember { SingleSelectionType<ItemIconNodeTree>() }

        BackgroungPanelStyle1() {
            Column(
                Modifier.padding(15.dp).widthIn(0.dp, this@BackgroungPanelStyle1.maxWidth * 0.8f)
                    .heightIn(0.dp, this@BackgroungPanelStyle1.maxHeight * 0.8f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                seekBar.show()
                Box(Modifier.weight(1f, false)) {
                    when (seekBar.active?.cod) {
                        "new" -> RowVA {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                IconNode(
                                    fileForCrop.outImage.cropImageBitmap.value,
                                    "icon_skill_color_lamp.png",
                                    type = cbTypeIconBorder.getSelected() ?: TypeIconBorder.ROUND
                                )
                                cbTypeIconBorder.show()
                                if (fileForCrop.outImage.cropImageBitmap.value != null) MyTextButtStyle1(
                                    "Сохранить не выбирая",
                                    Modifier.padding(start = 5.dp)
                                ) {
                                    cbTypeIconBorder.getSelected()?.let { type ->
                                        if (questDB == null) {
                                            MainDB.addAvatar.addIconNodeTree(fileForCrop.extension(), type.id)
                                                ?.let { idIcon ->
                                                    fileForCrop.outImage.saveIconFile(
                                                        File(
                                                            StateVM.dirIconNodeTree,
                                                            "icon_$idIcon.${fileForCrop.extension()}"
                                                        ).path
                                                    )
                                                }
                                        } else {
                                            questDB.addQuest.addIconNodeTree(fileForCrop.extension(), type.id)
                                                ?.let { idIcon ->
                                                    fileForCrop.outImage.saveIconFile(
                                                        File(
                                                            questDB.dirQuest,
                                                            "icon_$idIcon.${fileForCrop.extension()}"
                                                        ).path
                                                    )
                                                }
                                        }
                                    }
                                }
                            }
                            Box(
                                Modifier
                                    .width(this@BackgroungPanelStyle1.maxWidth * 0.5f)
                                    .height(this@BackgroungPanelStyle1.maxHeight * 0.6f)
                                    .padding(10.dp),
                                Alignment.Center
                            ) {
                                ImageCropSelector(fileForCrop)
                            }
                        }

                        "spisQuest" -> RowVA {
                            if (questDB != null) MyListPlate(questDB.spisQuest.spisIconNodeTree) { itemIcon ->
                                ComItemIconNode(
                                    itemIcon,
                                    selectionIconFromSpisQuest,
                                    questDB.dirQuest,
                                    doubleClick = { itemDC ->
                                        icon.value = itemDC
                                        dialLay.close()
                                    }) { item, exp ->
                                    MyDeleteDropdownMenuButton(exp) {
                                        val fileIcon = File(questDB.dirQuest, item.name())
                                        if (fileIcon.exists()) fileIcon.delete()
                                        questDB.addQuest.delIconNodeTree(item.id)
                                    }
                                }.getComposable()
                            }
                        }

                        "spis" -> RowVA {
                            MyListPlate(MainDB.avatarSpis.spisIconNodeTree) { itemIcon ->
                                ComItemIconNode(itemIcon, selectionIconFromSpis, doubleClick = { itemDC ->
                                    if (questDB != null) {
                                        questDB.addQuest.addIconNodeTree(itemDC.extension, itemDC.type_ramk)
                                            ?.let { idIcon ->
                                                File(
                                                    StateVM.dirIconNodeTree,
                                                    itemDC.name()
                                                ).copyTo(File(questDB.dirQuest, "icon_$idIcon.${itemDC.extension}"))
                                                icon.value =
                                                    ItemIconNodeTree(idIcon, itemDC.extension, itemDC.type_ramk)
                                                dialLay.close()
                                            }
                                    }
                                }) { item, exp ->
                                    if (questDB != null) MyDropdownMenuItem(exp, "Добавить в квест") {
                                        questDB.addQuest.addIconNodeTree(item.extension, item.type_ramk)
                                            ?.let { idIcon ->
                                                File(StateVM.dirIconNodeTree, item.name()).copyTo(
                                                    File(
                                                        questDB.dirQuest,
                                                        "icon_$idIcon.${item.extension}"
                                                    )
                                                )
                                            }
                                    }
                                    MyDeleteDropdownMenuButton(exp) {
                                        val fileIcon = File(StateVM.dirIconNodeTree, item.name())
                                        if (fileIcon.exists()) fileIcon.delete()
                                        MainDB.addAvatar.delIconNodeTree(item.id)
                                    }
                                }.getComposable()
                            }
                        }

                        else -> {}
                    }
                }
                RowVA {
                    MyTextButtStyle1("Отмена") {
                        dialLay.close()
                    }
                    when (seekBar.active?.cod) {
                        "new" -> if (fileForCrop.outImage.cropImageBitmap.value != null) MyTextButtStyle1(
                            "Сохранить и выбрать",
                            Modifier.padding(start = 5.dp)
                        ) {
                            cbTypeIconBorder.getSelected()?.let { type ->
                                if (questDB == null) {
                                    MainDB.addAvatar.addIconNodeTree(fileForCrop.extension(), type.id)
                                        ?.let { idIcon ->
                                            if (fileForCrop.outImage.saveIconFile(
                                                    File(
                                                        StateVM.dirIconNodeTree,
                                                        "icon_$idIcon.${fileForCrop.extension()}"
                                                    ).path
                                                )
                                            ) {
                                                icon.value = ItemIconNodeTree(idIcon, fileForCrop.extension(), type.id)
                                                dialLay.close()
                                            }
                                        }
                                } else {
                                    questDB.addQuest.addIconNodeTree(fileForCrop.extension(), type.id)
                                        ?.let { idIcon ->
                                            if (fileForCrop.outImage.saveIconFile(
                                                    File(
                                                        questDB.dirQuest,
                                                        "icon_$idIcon.${fileForCrop.extension()}"
                                                    ).path
                                                )
                                            ) {
                                                icon.value = ItemIconNodeTree(idIcon, fileForCrop.extension(), type.id)
                                                dialLay.close()
                                            }
                                        }
                                }
                            }
                        }

                        "spisQuest" -> {
                            selectionIconFromSpisQuest.selected?.let { itemSelectQuest ->
                                MyTextButtStyle1(
                                    "Выбрать",
                                    Modifier.padding(start = 5.dp)
                                ) {
                                    icon.value = itemSelectQuest
                                    dialLay.close()
                                }
                            }
                        }

                        "spis" -> {
                            selectionIconFromSpis.selected?.let { itemSelectInner ->
                                MyTextButtStyle1(
                                    "Выбрать",
                                    Modifier.padding(start = 5.dp)
                                ) {
                                    if (questDB != null) {
                                        questDB.addQuest.addIconNodeTree(
                                            itemSelectInner.extension,
                                            itemSelectInner.type_ramk
                                        )
                                            ?.let { idIcon ->
                                                File(
                                                    StateVM.dirIconNodeTree,
                                                    itemSelectInner.name()
                                                ).copyTo(
                                                    File(
                                                        questDB.dirQuest,
                                                        "icon_$idIcon.${itemSelectInner.extension}"
                                                    )
                                                )
                                                icon.value =
                                                    ItemIconNodeTree(
                                                        idIcon,
                                                        itemSelectInner.extension,
                                                        itemSelectInner.type_ramk
                                                    )
                                                dialLay.close()
                                            }
                                    } else {
                                        icon.value = itemSelectInner
                                        dialLay.close()
                                    }
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    dialLay.show()
}

