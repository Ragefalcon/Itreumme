package MainTabs.Setting

import GooglePack.mainFF
import MyDialog.MyDialogLayout
import MyList
import MyShowMessage
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import common.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ragefalcon.sharedcode.extensions.withOffset
import ru.ragefalcon.sharedcode.source.disk.CommonName
import uiItems.ComItemGFile
import viewmodel.MainDB
import viewmodel.StateVM
import java.awt.Desktop
import java.io.File
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class GoogleSincTab(val dialLay: MyDialogLayout) {

    var code = mutableStateOf("code0")

    val progressSett = mutableStateOf(0f)
    val nameNewBD = mutableStateOf(TextFieldValue("NewBD_000"))

    val online = mutableStateOf(false)

    var refreshCount = 0


    fun changeBDtoNetworkBD() {

        val networkBD = File(StateVM.dirGoogle, CommonName.nameFromNetworkDBfile)
//        val networkBD = File(System.getProperty("user.dir") + "\\testNetwork.db")
        if (networkBD.parentFile.exists()) {
            val fileName = MainDB.arg.path
            val myBD = File(fileName)
            if (!myBD.parentFile.exists()) Files.createDirectory(Paths.get(myBD.parentFile.path))
            myBD.createNewFile()
            myBD.writeBytes(networkBD.readBytes())
        }
    }

    init {

    }

    @Composable
    fun show() {
        StateVM.ktorGOA


        Box() {
            Column(
                Modifier.fillMaxSize().padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                Text(code.value, maxLines = 3)
                Row {
                    Column {
                        if (!StateVM.authCodeGet.value && !StateVM.refresh_tokenGet.value) {
                            Image(
                                BitmapPainter(
                                    useResource(
                                        "btn_google_signin_light_normal_web@2x.png",
                                        ::loadImageBitmap
                                    )
                                ),
                                "SignInButt",
                                Modifier.height(50.dp).clickable(onClick = {
                                    mainFF()

                                    val clientID = "your_client_id_from_google_console_project"
                                    openInBrowser(
                                        URI(
                                            "https://accounts.google.com/o/oauth2/v2/auth?" +
                                                    "redirect_uri=http%3A//127.0.0.1%3A5000&" +
                                                    "prompt=consent&" +
                                                    "response_type=code&" +
                                                    "client_id=$clientID&" +
                                                    "scope=https%3A//www.googleapis.com/auth/drive.appdata&" +
                                                    "access_type=offline"
                                        )
                                    )
                                })
                                    .clipToBounds(),
                                contentScale = ContentScale.Fit// .Fit
                            )
                        } else {
                            MyTextButtStyle1("Revoke Token") {
                                StateVM.revokeToken {
                                    MyShowMessage(dialLay, "Токены доступа отозваны")
                                }

                            }
                        }
                    }
                    Spacer(Modifier.weight(0.3f))
                    statusToken()
                }
//                if (online.value) {
                if (StateVM.access_tokenGet.value && StateVM.refresh_tokenGet.value && StateVM.authCodeGet.value) {
                    Row(Modifier.padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
                        MyTextButtStyle1("Files") {
                            StateVM.ktorGOA.GetAppFilesList()
                        }
                        MyTextButtStyle1("Установить базу из сети") {
                            changeBDtoNetworkBD()
                        }
                    }
                    Row(Modifier.padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
                        MyOutlinedTextField("Имя нового файла", nameNewBD)
                        MyTextButtStyle1("Загрузить новый файл") {
                            GlobalScope.launch {
                                StateVM.ktorGOA.uploadFile(CommonName.nameMainDBfile, nameNewBD.value.text) { progress ->
                                    progressSett.value = progress
                                }
                                StateVM.ktorGOA.GetAppFilesList()
                            }
                        }
                    }
                    LinearProgressIndicator(progressSett.value, modifier = Modifier.padding(top = 5.dp))
                    CircularProgressIndicator(progressSett.value, modifier = Modifier.padding(bottom = 5.dp))
                        MyList(StateVM.listGFile ) { ind, item ->
                            ComItemGFile(item) { item, expanded ->
                                DropdownMenuItem(onClick = {
                                    StateVM.ktorGOA.downloadFile(item.id) { progress ->
                                        progressSett.value = progress
//                                progLoad.progress = (progress*100).toInt()
                                    }
                                    expanded.value = false
                                }) {
                                    Text(text = "Загрузить", color = Color.White)
                                }
                                DropdownMenuItem(onClick = {
                                    GlobalScope.launch {
                                        StateVM.ktorGOA.overwriteFile(CommonName.nameMainDBfile, item.id) { progress ->
                                            progressSett.value = progress
                                            if (progress == 1F) progressSett.value = 0f
                                        }
                                    }
                                    expanded.value = false
                                }) {
                                    Text(text = "Перезаписать", color = Color.White)
                                }
                                DropdownMenuItem(onClick = {
                                    GlobalScope.launch {
                                        StateVM.ktorGOA.deleteFile(item.id)
                                        StateVM.ktorGOA.GetAppFilesList()
                                    }
                                    expanded.value = false
                                }) {
                                    Text(text = "Удалить", color = Color.White)
                                }
                            }
                        }

                }
            }
        }
    }
}

@Composable
fun statusToken(){
    val online = mutableStateOf(false)

    var refreshCount = 0
    val textCodeTime = remember { mutableStateOf("") }
    val isTimerRunning = remember { mutableStateOf(true) }
    LaunchedEffect(key1 = textCodeTime.value, key2 = isTimerRunning.value) {

        delay(1000L)
        val restTime =
            StateVM.ktorGOA.params.expires_in - (Date().time.withOffset() - StateVM.ktorGOA.params.lastTimeCode) / 1000
        if (restTime < 0) {
            textCodeTime.value = "offline"
            StateVM.ktorGOA.params.access_token = ""
            StateVM.access_tokenGet.value = false
            online.value =
                StateVM.access_tokenGet.value && StateVM.refresh_tokenGet.value && StateVM.authCodeGet.value
            if (StateVM.authCodeGet.value && StateVM.refresh_tokenGet.value) {
                StateVM.ktorGOA.refreshToken {
                    StateVM.saveToken()
                    refreshCount++
                }
            }

        } else {
            textCodeTime.value = "($refreshCount): $restTime"
            online.value =
                StateVM.access_tokenGet.value && StateVM.refresh_tokenGet.value && StateVM.authCodeGet.value
        }
        isTimerRunning.value = !isTimerRunning.value
    }
    Column(Modifier.padding(end = 20.dp)) {
        MyIconTextIndikatorStyle1(text = "authCode", value = StateVM.authCodeGet, sizeIcon = 25.dp)
        MyIconTextIndikatorStyle1(
            text = "refresh_token",
            value = StateVM.refresh_tokenGet,
            sizeIcon = 25.dp
        )
    }
    Column(Modifier.padding(end = 20.dp)) {
        MyIconTextIndikatorStyle1(
            text = "access_token",
            value = StateVM.access_tokenGet,
            sizeIcon = 25.dp
        )
        Text(textCodeTime.value, style = MyTextStyleParam.style1)
//                        MyIconTextIndikatorStyle1(text = "expires_in",StateVM.expires_inGet)
    }
}

fun openInBrowser(uri: URI) {
    val osName by lazy(LazyThreadSafetyMode.NONE) { System.getProperty("os.name").lowercase(Locale.getDefault()) }
    val desktop = Desktop.getDesktop()
    when {
        Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE) -> desktop.browse(uri)
        "mac" in osName -> Runtime.getRuntime().exec("open $uri")
        "nix" in osName || "nux" in osName -> Runtime.getRuntime().exec("xdg-open $uri")
        else -> throw RuntimeException("cannot open $uri")
    }
}

