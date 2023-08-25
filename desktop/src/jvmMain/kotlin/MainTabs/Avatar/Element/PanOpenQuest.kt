package MainTabs.Avatar.Element

import MainTabs.Quest.Items.ComItemFileQuest
import MyDialog.MyDialogLayout
import MyList
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import viewmodel.MainDB
import viewmodel.QuestDB
import viewmodel.QuestVM
import viewmodel.StateVM
import java.io.File

fun PanOpenQuest(
    dialPan: MyDialogLayout,
    listener: (File) -> Unit
) {
    val dialLayInner = MyDialogLayout()

    val filesList = mutableStateListOf<File>()

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

    val selectionFileQuest = SingleSelectionType<File>()
    val openOpisQuest = mutableStateOf(false)

    dialPan.dial = @Composable {
        BackgroungPanelStyle1 {
            Column(
                Modifier
                    .heightIn(0.dp, dialPan.layHeight.value * 0.7F)
                    .widthIn(0.dp, dialPan.layWidth.value * 0.7F)
                    .padding(10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 5.dp).animateContentSize().border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(listOf(Color(0xFFFFF7D9), Color(0xFFFFF7D9))),
                        shape = RoundedCornerShape(10.dp)
                    ).background(
                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                        color = Color(0xFFE4E0C7),
                    ), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (openOpisQuest.value) {
                        selectionFileQuest.selected?.let { selFile ->
                            ComItemFileQuest(
                                selFile,
                                selectionFileQuest,
                                false,
                                onClick = { openOpisQuest.value = false })
                        }
                    } else {
                        MyList(
                            filesList,
                            Modifier.heightIn(0.dp, dialPan.layHeight.value * 0.7F).padding(vertical = 5.dp).weight(1f)
                        ) { ind, it ->
                            ComItemFileQuest(it, selectionFileQuest, true, openFile = {

                                QuestVM.loadQuestDB = QuestDB(it)

                                openOpisQuest.value = true
                            }, dropMenu = { fileQuest, exp ->
                                MyDropdownMenuItem(exp, "Открыть описание") {
                                    QuestVM.loadQuestDB = QuestDB(fileQuest)
                                    openOpisQuest.value = true
                                }
                                MyDropdownMenuItem(exp, "Начать") {
                                    QuestVM.loadQuestDB = QuestDB(fileQuest)

                                    QuestVM.loadQuestDB?.ObserFM?.let { questBVM ->
                                        MainDB.loadQuestFromFileAttach(fileQuest, null)
                                    }
                                }
                            })
                        }
                        MyTextButtStyle1("Отрыть из файла", Modifier.padding(vertical = 5.dp)) {
                            dialPan.close()
                        }
                    }
                }
                if (openOpisQuest.value) {
                    selectionFileQuest.selected?.let { selFile ->
                        QuestVM.loadQuestDB?.let { questDB ->
                            questDB.spisQuest.spisMainParam.getState().value?.let { list ->
                                Column(Modifier.padding(20.dp).weight(1f, false)) {
                                    list.find { itemMainParam -> itemMainParam.name == "name" }?.let { nameMainParam ->
                                        Text(
                                            nameMainParam.stringparam,
                                            Modifier.padding(bottom = 20.dp),
                                            style = MyTextStyleParam.style1
                                        )
                                    }
                                    MyList(questDB.spisQuest.spisMainParam, Modifier) { ind, item ->
                                        if (item.name != "name") {
                                            Text(
                                                if (item.name == "opis") "Описание" else item.name,
                                                style = MyTextStyleParam.style2.copy(fontSize = 18.sp)
                                            )
                                            Text(
                                                item.stringparam,
                                                Modifier.padding(bottom = 15.dp),
                                                style = MyTextStyleParam.style5.copy(fontSize = 15.sp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Row {
                }
                Row {
                    Spacer(Modifier.weight(1F))
                    MyTextButtStyle1("Отмена") {
                        dialPan.close()
                    }
                    Spacer(Modifier.weight(1F))
                    selectionFileQuest.selected?.let { fileQuest ->
                        MyTextButtStyle1("Начать", Modifier.padding(start = 5.dp)) {
                            QuestVM.loadQuestDB = QuestDB(fileQuest)
                            QuestVM.loadQuestDB?.ObserFM?.let { questBVM ->
                                MainDB.loadQuestFromFileAttach(fileQuest, null)
                            }
                            dialPan.close()
                        }
                        Spacer(Modifier.weight(1F))
                    }
                }
            }
        }
        dialLayInner.getLay()
    }

    dialPan.show()
}


