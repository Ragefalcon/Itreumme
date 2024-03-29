import CommonWindow.BorderWindow
import GooglePack.sess
import MainTabs.*
import MainTabs.Setting.WindowEditStyle
import MyDialog.DialogEventLayout
import MyDialog.MessageProgressCharacteristics
import MyDialog.MyDialogLayout
import MyDialog.SplashScreen
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import common.EnumDiskretSeekBar
import common.MyButtIconStyle
import common.MyShadowBox
import extensions.SimplePlateWithShadowStyleState
import extensions.ToggleButtonStyleState
import extensions.getValue
import extensions.withSimplePlate
import ru.ragefalcon.sharedcode.source.disk.getValue
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.tabElement
import viewmodel.MainDB
import viewmodel.StateVM
import java.io.File


enum class MainTabsEnum(override val nameTab: String) : tabElement {
    Avatar("Аватар"),
    Journal("Журнал"),
    Time("Время"),
    Finance("Финансы"),
    Quest("Квесты"),
    Settings("Настройки");
}


@OptIn(ExperimentalFoundationApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
fun main() = application {

    /**
     * Возможные значения  "METAL","SOFTWARE","OPENGL" и наверное "DIRECTX" или "DIRECT3D"
     * На ноуте с виндой и с дискретной и с встроенной видеокартой если не стоит принудительного выбора
     * дискретного ускорителя, то вываливается ошибка Failed Direct3D call через какое то время после запуска.
     * Если установить OPENGL, то вроде все работает без настроек в системе.
     * */
    System.setProperty("skiko.renderApi", "OPENGL")

    val dialLay = remember { MyDialogLayout() }

    val timeScreen = remember { MainTimeTabs(dialLay) }
    val avatarScreen = remember { MainAvatarTabs(dialLay) }
    val journalScreen = remember { MainJournalTabs(dialLay) }
    val finScreen = remember { MainFinTabs(dialLay) }
    val settScreen = remember { MainSettTabs(dialLay) }
    val questScreen = remember { MainQuestTabs(dialLay) }
    val statPanel = remember { MainStatusPanel(dialLay) }
    val avatarSize = 130.dp

    val mainSeekBar = remember { EnumDiskretSeekBar(MainTabsEnum::class, MainTabsEnum.Time, true) }


    val state = rememberWindowState(
        WindowPlacement.Floating,
        position = WindowPosition(Alignment.Center),
        width = 1050.dp,
        height = 800.dp
    )


    Window(
        onCloseRequest = {
            /**
             * Код отсюда выполняется при нажатии комбинации клавиш Alt + F4
             * */
            sess?.stop(10, 10)
            val tempFiles = File(StateVM.dirTemp)
            if (tempFiles.exists()) tempFiles.deleteRecursively()
            exitApplication()
        },

        onKeyEvent = { saf ->
            /**
             * Здесь можно делать проверку на то какая клавиша была нажата и по итогу выполнять какой-нибудь код.
             * После нужно вернуть true или false, если true - то больше ничего выполняться не будет, видимо
             * подразумевается, что клавиша перехвачена и выполнен собственный код. Если false - то после будут
             * еще выполнены действия по умолчанию, которые идут я так понимаю от системы и т.д. Такие как вызов контекстного
             * меню окна из которого его можно закрыть, свернуть и т.д. Фокус на это меню переходит после нажатия
             * на клавишу Alt, которая похоже иногда дает дополнительное ложное срабатывание после комбинации Alt + Shift
             * для смены раскладки.
             * */
            saf.key == Key.AltLeft || saf.key == Key.AltRight
        },
        state = state,
        transparent = true,
        undecorated = true
    ) {

        MaterialTheme {
            val widthRamk = if (MainDB.styleParam.commonParam.HARD_BORDER.getValue()) (20 * 1.5).dp else 0.dp

            StateVM.startLaunchCommonStyle()
            Box(
                Modifier.clip(MainDB.styleParam.appBarStyle.shape_window.getValue(limit = 50.0))
                    .border(
                        if (MainDB.styleParam.commonParam.HARD_BORDER.getValue()) BorderStroke(
                            0.25.dp,
                            Color(0x2FFFF7D9)
                        )
                        else BorderStroke(
                            MainDB.styleParam.commonParam.BORDER_WIDTH.getValue().dp,
                            MainDB.styleParam.commonParam.BORDER_BRUSH.getValue()
                        ), MainDB.styleParam.appBarStyle.shape_window.getValue(limit = 50.0)
                    )
            ) {
                Column(
                    Modifier
                        .background(
                            brush = MainDB.styleParam.commonParam.COLOR_BACKGROUND.getValue()
                        )
                        .padding(widthRamk / 2f)
                        .fillMaxSize()
                ) {
                    Box() {
                        Column {
                            AppWindowTitleBar(this@application, state)
                            statPanel.show(avatarSize, 60.dp)
                        }
                        avatar(dialLay, Modifier, avatarSize)
                    }
                    Row(
                        Modifier.padding(10.dp)
                    ) {
                        mainSeekBar.show(Modifier.fillMaxHeight())
                        when (mainSeekBar.active) {
                            MainTabsEnum.Avatar -> avatarScreen.show()
                            MainTabsEnum.Journal -> journalScreen.show()
                            MainTabsEnum.Time -> timeScreen.show()
                            MainTabsEnum.Finance -> finScreen.show()
                            MainTabsEnum.Quest -> questScreen.show()
                            MainTabsEnum.Settings -> settScreen.show()
                        }
                    }
                }
                DialogEventLayout()
                MessageProgressCharacteristics()
                dialLay.getLay()
                if (MainDB.styleParam.commonParam.OLD_PAPER.getValue()) Image(
                    BitmapPainter(useResource("desk_paper_back.png", ::loadImageBitmap)),
                    "paperback",
                    Modifier.fillMaxSize(),
                    alpha = 0.6F,
                    contentScale = ContentScale.FillBounds
                )
                StateVM.dialLayForViewPicture.getLay()
                SplashScreen()
                if (MainDB.styleParam.commonParam.HARD_BORDER.getValue()) BorderWindow(widthRamk)
            }
        }
    }

    if (StateVM.openEditStyle.value) {
        WindowEditStyle()
    }
}


@Composable
private fun WindowScope.AppWindowTitleBar(appScope: ApplicationScope, state: WindowState) =
    WindowDraggableArea(Modifier) {
        MainDB.styleParam.appBarStyle.let { style ->
            MyShadowBox(style.plateAppBar.shadow.getValue()) {
                Row(
                    modifier = Modifier
                        .withSimplePlate(SimplePlateWithShadowStyleState(style.plateAppBar))
                        .fillMaxWidth()
                        .height(25.dp)
                        .padding(start = 20.dp, end = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.weight(1f).height(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Itreumme",
                            Modifier,
                            style = style.textItreumme.getValue().copy(fontSize = 15.sp)
                        )
                    }
                    Row {
                        MyButtIconStyle(
                            "ic_round_minimize_24.xml",
                            style.outerIconPadding.getValue(Modifier),
                            sizeIcon = 15.dp,
                            myStyleToggleButton = ToggleButtonStyleState(style.buttIcon)
                        ) {
                            state.isMinimized = !state.isMinimized
                        }
                        MyButtIconStyle(
                            "ic_baseline_code_24.xml",
                            style.outerIconPadding.getValue(Modifier),
                            sizeIcon = 15.dp,
                            myStyleToggleButton = ToggleButtonStyleState(style.buttIcon),
                            rotation = true
                        ) {
                            state.placement = if (state.placement == WindowPlacement.Floating) {
                                WindowPlacement.Maximized
                            } else {
                                WindowPlacement.Floating
                            }
                        }
                        MyButtIconStyle(
                            "ic_round_close_24.xml",
                            style.outerIconPadding.getValue(Modifier),
                            sizeIcon = 15.dp,
                            myStyleToggleButton = ToggleButtonStyleState(style.buttIcon)
                        ) {
                            sess?.stop(10, 10)
                            val tempFiles = File(StateVM.dirTemp)
                            if (tempFiles.exists()) tempFiles.deleteRecursively()
                            appScope.exitApplication()
                        }
                    }
                }
            }

        }
    }

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Button(
    text: String = "",
    onClick: () -> Unit = {},
    color: Color = Color(210, 210, 210),
    size: Int = 16
) {
    val buttonHover = remember { mutableStateOf(false) }
    Surface(
        color = if (buttonHover.value)
            Color(color.red / 1.3f, color.green / 1.3f, color.blue / 1.3f)
        else
            color,
        shape = RoundedCornerShape((size / 2).dp)
    ) {
        Box(
            modifier = Modifier
                .clickable(onClick = onClick)
                .size(size.dp, size.dp)
                .pointerMoveFilter(
                    onEnter = {
                        buttonHover.value = true
                        false
                    },
                    onExit = {
                        buttonHover.value = false
                        false
                    },
                    onMove = { false }
                )
        ) {
            Text(text = text)
        }
    }
}


typealias ComposableFun = @Composable () -> Unit



