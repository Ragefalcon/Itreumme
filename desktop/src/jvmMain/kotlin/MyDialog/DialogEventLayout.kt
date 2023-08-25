package MyDialog

import MainTabs.Avatar.Items.ComItemLoadOtvetDialog
import MainTabs.imageFromFile
import MyList
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.*
import extensions.BoxWithVScrollBar
import extensions.toColor
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.sharedcode.models.data.ItemDialogLine
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemDialogQuestEvent
import ru.ragefalcon.sharedcode.source.disk.getPathWithSeparator
import viewmodel.MainDB
import viewmodel.StateVM
import java.io.File

@Composable
fun DialogEventLayout() {
    val keyDial = remember { mutableStateOf(false) }
    val dialLay = remember { MyDialogLayout() }

    if (MainDB.loadQuestSpis.dialogEvent.getState().value != null) keyDial.value = true
    val alpha: Float by animateFloatAsState(
        targetValue = if (MainDB.loadQuestSpis.dialogEvent.getState().value != null) 1f else 0.0f,
        animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing)
    ) { if (MainDB.loadQuestSpis.dialogEvent.getState().value == null) keyDial.value = false }

    BoxWithConstraints(Modifier.fillMaxSize().alpha(alpha), Alignment.Center) {
        if (keyDial.value)
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0x88000000)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    dialogPanel(
                        MainDB.loadQuestSpis.dialogEvent.getState().value,
                        MainDB.loadQuestSpis.dialogForEvent.getState().value
                    )
                }
            }
    }
    dialLay.getLay()
    LaunchedEffect(MainDB.loadQuestSpis.innerFinishTriggerAction.getState().value) {
        MainDB.loadQuestSpis.innerFinishTriggerAction.getState().value?.let {
            StateVM.innerFinishAction.value?.finishAction(it, dialLay)
        }
    }
}

@Composable
private fun dialogPanel(dialogLine: ItemDialogLine?, dialEvent: ItemDialogQuestEvent?) {

    val scrollOpis: ScrollState = rememberScrollState(0)
    LaunchedEffect(dialEvent?.maintext) {
        scrollOpis.scrollTo(0)
    }
    dialogLine?.let { dialLine ->
        dialEvent?.let { dial ->
            val loadAva =
                mutableStateOf(
                    File(
                        if (dial.image_govorun != "") getPathWithSeparator(
                            listOf(
                                StateVM.dirLoadedQuestFiles,
                                "Quest_${dial.quest_id}",
                                dial.image_govorun
                            )
                        ) else "null.jpg"
                    )
                )

            Box {
                val heightPlate = remember { mutableStateOf(0.dp) }
                StateVM.innerFinishAction.value?.let {
                    Box(Modifier
                        .padding(start = if (loadAva.value.exists()) 100.dp else 0.dp)
                        .fillMaxWidth(0.7f)
                        .padding(start = if (loadAva.value.exists()) 80.dp else 0.dp)
                        .onGloballyPositioned {
                            heightPlate.value = it.size.height.dp
                        }
                    ) {
                        it.objectForAction()
                    }
                }
                Box(Modifier.padding(top = if (loadAva.value.exists()) if (heightPlate.value - 75.dp >= 0.dp) heightPlate.value - 75.dp else 0.dp else heightPlate.value)) {
                    BackgroungPanelStyle1(
                        Modifier.padding(
                            start = if (loadAva.value.exists()) 100.dp else 0.dp,
                            top = if (loadAva.value.exists()) 75.dp else 0.dp
                        )
                    ) {
                        if (dial.quest_name != "") {
                            Text(
                                dial.quest_name,
                                Modifier.fillMaxWidth(0.7F)
                                    .padding(start = if (loadAva.value.exists()) 60.dp else 0.dp)
                                    .padding(start = 15.dp)
                                    .shadow(3.dp, RoundedCornerShape(15.dp))
                                    .background(Color(0xFFFFF7D9), RoundedCornerShape(15.dp))
                                    .border(
                                        width = 0.5.dp,
                                        brush = Brush.horizontalGradient(
                                            listOf(Color(0x4FFFF7D9), Color(0x4FFFF7D9))
                                        ),
                                        shape = RoundedCornerShape(15.dp)
                                    )
                                    .padding(3.dp)
                                    .padding(horizontal = 2.dp),
                                style = MyTextStyleParam.style5.copy(
                                    color = Color(0xFF464D45),
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                        Column(
                            Modifier.fillMaxWidth(0.7F).padding(15.dp).animateContentSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (dial.govorun_name != "") Text(
                                "${dial.govorun_name} :",
                                Modifier.padding(top = 20.dp, start = if (loadAva.value.exists()) 65.dp else 15.dp)
                                    .fillMaxWidth(),
                                style = MyTextStyleParam.style1.copy(
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Start,
                                    color = MyColorARGB.colorEffektShkal_Nedel.toColor()
                                )
                            )
                            BoxWithVScrollBar(
                                Modifier.padding(10.dp),
                                maxHeight = (this@BackgroungPanelStyle1.maxHeight.value * 0.4).toInt(),
                                scroll = scrollOpis
                            ) { scrollStateBox ->
                                Text(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .padding(
                                            start = 10.dp,
                                            top = if (dial.govorun_name != "") 5.dp else 15.dp
                                        )
                                        .fillMaxWidth().verticalScroll(scrollStateBox, enabled = true),
                                    text = dial.maintext,
                                    style = MyTextStyleParam.style4.copy(
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                )
                            }
                            MyList(
                                MainDB.loadQuestSpis.spisOtvetDialogForEvent,
                                maxHeight = (this@BackgroungPanelStyle1.maxHeight.value * 0.3).toInt(),
                            ) { ind, item ->
                                ComItemLoadOtvetDialog(item)
                            }
                            MainDB.loadQuestSpis.spisOtvetDialogForEvent.getState().value?.let {
                                if (it.isEmpty()) MyTextButtStyle1("Ok", Modifier.padding(start = 5.dp)) {
                                    MainDB.addQuest.completeDialogEvent()
                                }
                            }
                        }
                    }
                    if (loadAva.value.exists()) {
                        val shape = RoundedCornerShape(75.dp)
                        Row {
                            Image(
                                bitmap = imageFromFile(loadAva.value),
                                "defaultAvatar",
                                Modifier.wrapContentSize().padding(horizontal = 20.dp)
                                    .height(140.dp)
                                    .width(140.dp)
                                    .clip(shape)
                                    .border(1.dp, Color.White, shape)
                                    .padding(1.dp)
                                    .border(3.dp, Color(0x7FFFF7D9), RoundedCornerShape(74.dp))
                                    .shadow(2.dp, shape),
                                contentScale = ContentScale.Crop,
                            )
                        }
                    }
                }
            }
        }
    }
}


