package MainTabs

import MainTabs.Quest.Element.QuestOpis
import MainTabs.Quest.Items.ComItemFileQuest
import MainTabs.Quest.QuestContent
import MainTabs.Quest.QuestsPanel
import MyDialog.MyDialogLayout
import MyDialog.MyOneVopros
import MyDialog.MyOneVoprosTransit
import MyList
import MyShowMessage
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.*
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import viewmodel.MainDB
import viewmodel.QuestDB
import viewmodel.QuestVM.openQuestDB
import viewmodel.StateVM
import java.io.File
import java.util.*

class MainQuestTabs(val dialLay: MyDialogLayout) {

    val filesList = mutableStateListOf<File>()

    val questSeekBar =
        DiskretSeekBar(
            listOf("Прохождение" to "Loading", "Создание" to "Creating"),
            "Loading"
        ) {}

    init {
        updateFileList()
    }

    fun updateFileList() {
        val f = File(StateVM.dirQuest)
        if (f.exists()) {
            filesList.clear()
            for (ff in f.listFiles()) {
                if (ff.isDirectory) {
                    val tmpF = File(ff.path, "${ff.name}.db")
                    if (tmpF.extension == "db") {
                        filesList.add(tmpF)
                    }
                }
            }
        } else {
            f.mkdir()
        }
    }

    val selectionFileQuest = SingleSelectionType<File>()

    val questLoad = QuestsPanel()

    val questContent = QuestContent(dialLay)

    @Composable
    fun show() {
        Column {
            questSeekBar.show(Modifier.fillMaxWidth().padding(bottom = 10.dp))
            when (questSeekBar.active?.cod) {
                "Loading" -> questLoad.show(dialLay, Modifier.fillMaxWidth())
                "Creating" -> Box() {
                    Row(Modifier.padding(10.dp)) {
                        Column(Modifier.fillMaxWidth(0.35f)) {

                            fun openQuest(fileQuest: File) {
                                StateVM.openTreeSkillsQuest.value = false
                                StateVM.selectionTreeSkillsQuest.selected = null
                                openQuestDB = QuestDB(fileQuest)
                            }
                            MyList(filesList, Modifier.heightIn(0.dp, 250.dp)) { ind, itemF ->
                                ComItemFileQuest(itemF, selectionFileQuest, true, openFile = {
                                    openQuest(it)
                                }, dropMenu = { item, exp ->
                                    DropdownMenuItem(onClick = {
                                        exp.value = false
                                        openQuest(item)
                                    }) {
                                        Text(text = "Открыть", color = Color.White)
                                    }
                                    MyDropdownMenuItem(exp, "LoadQuestToBase") {
                                        MainDB.loadQuestFromFileAttach(item, questId = null)
                                    }

                                    MyDropdownMenuItem(exp, "Переименовать") {
                                        MyOneVopros(
                                            dialLay,
                                            "Введите новое имя файла:",
                                            "Переименовать",
                                            "Новое название файла",
                                            item.nameWithoutExtension
                                        ) { newName ->
                                            item.renameTo(File(item.parent, "$newName.db"))
                                            val dirParent = File(item.parent)
                                            dirParent.renameTo(File(dirParent.parent, newName))
                                            openQuestDB = null
                                            updateFileList()
                                        }
                                    }

                                    MyDeleteDropdownMenuButton(exp) {
                                        item.delete()
                                        updateFileList()
                                    }
                                })
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                MyTextButtStyle1("+", modifier = Modifier.padding(start = 15.dp).width(80.dp)) {
                                    MyOneVoprosTransit(
                                        dialLay,
                                        "Введите имя файла нового квеста:",
                                        "Создать",
                                        "Название файла",
                                        "NewQuest_${Date().time}"
                                    ) { nameNewQuest ->
                                        val dirQ = File(StateVM.dirQuest, nameNewQuest).path
                                        if (!File(dirQ).exists()) {
                                            File(dirQ).mkdirs()

                                            openQuest(File(dirQ, "$nameNewQuest.db"))
                                            updateFileList()
                                            return@MyOneVoprosTransit false
                                        } else {
                                            MyShowMessage(dialLay, "Квест с таким именем уже существует")
                                            return@MyOneVoprosTransit true
                                        }
                                    }
                                }
                                MyTextButtStyle1(
                                    "ID",
                                    modifier = Modifier.padding(start = 15.dp).width(80.dp),
                                    backgroundColor = MyColorARGB.colorRasxodTheme0.toColor()
                                ) {
                                    openQuest(File(StateVM.dirInnerDialogs, "InnerDialogs.db"))
                                }
                            }
                            openQuestDB?.let { questDB ->
                                QuestOpis(dialLay, questDB)
                            }
                        }
                        openQuestDB?.let {
                            questContent.show()
                        }
                    }
                }
            }
        }
    }
}