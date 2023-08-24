package viewmodel

import MainTabs.Setting.TimeSettings
import MyDialog.InnerFinishAction
import MyDialog.MyDialogLayout
import androidx.compose.runtime.*
import androidx.compose.ui.layout.LayoutCoordinates
import common.SingleSelectionType
import extensions.*
import kotlinx.coroutines.*
import ru.ragefalcon.sharedcode.models.data.ItemBloknot
import ru.ragefalcon.sharedcode.models.data.ItemIdea
import ru.ragefalcon.sharedcode.models.data.ItemIdeaStap
import ru.ragefalcon.sharedcode.models.data.ItemTreeSkill
import ru.ragefalcon.sharedcode.models.data.itemQuest.ItemTreeSkillsQuest
import ru.ragefalcon.sharedcode.myGoogleLib.ItemGDriveFile
import ru.ragefalcon.sharedcode.myGoogleLib.ItemKtorGoogleParams
import ru.ragefalcon.sharedcode.myGoogleLib.KtorGoogleOAuth
import ru.ragefalcon.sharedcode.source.disk.DbArgs
import ru.ragefalcon.tutatoresFX.mygooglelib.MyShifr
import java.io.File
import java.util.*

object StateVM {

//    var lastOpenDir: String = System.getProperty("user.dir")

    val dialLayForViewPicture = MyDialogLayout()


    val timeSettings = TimeSettings()

    val dirMain: String =
        File(System.getProperty("user.home"), "ItreummeData").path.also {
//            println("Mydir?: ${System.getProperty ("user.home")}")
            if (!File(it).exists()) {
                File(it).mkdirs()
            }
        }

    var lastOpenDir: String = System.getProperty("user.dir")

    val dirLoadedQuestFiles: String =
        File(dirMain, "LoadedQuestFiles").path.also {
            if (!File(it).exists()) {
                File(it).mkdirs()
            }
        }

    val dirIconNodeTree: String =
        File(dirMain, "TreeNodeIcons").path.also {
            if (!File(it).exists()) {
                File(it).mkdirs()
            }
        }

    val dirQuest: String =
        File(dirMain, "Quests").path.also {
            if (!File(it).exists()) {
                File(it).mkdirs()
            }
        }

    val dirStyle: String =
        File(dirMain, "Styles").path.also {
            if (!File(it).exists()) {
                File(it).mkdirs()
            }
        }

    val dirInnerDialogs: String =
        File(dirMain, "InnerDialogs").path.also {
            if (!File(it).exists()) {
                File(it).mkdirs()
            }
        }

    val dirAvatar: String =
        File(dirMain, "Avatar").path.also {
            if (!File(it).exists()) {
                File(it).mkdirs()
            }
        }

    val dirBestDaysImages: String =
        File(dirMain, "BestDaysImages").path.also {
            if (!File(it).exists()) {
                File(it).mkdirs()
            }
        }

    val dirDreamsImages: String =
        File(dirMain, "DreamsImages").path.also {
            if (!File(it).exists()) {
                File(it).mkdirs()
            }
        }

    val dirComplexOpisImages: String =
        File(dirMain, "ComplexOpisImages").path.also {
            if (!File(it).exists()) {
                File(it).mkdirs()
            }
        }

    val dirGoogle: String =
        File(dirMain, "NetworkFiles").path.also {
            if (!File(it).exists()) {
                File(it).mkdirs()
            }
        }

    val dirTemp: String =
        File(dirMain, "Temp").path.also {
            if (!File(it).exists()) {
                File(it).mkdirs()
            }
        }


    var tmpVajnLayCoor: LayoutCoordinates? = null

    val listGFile = mutableStateListOf<ItemGDriveFile>()

    private val fileToken = "fileTok"

    private val clientID = "your_client_id_from_google_console_project"
    private val clientSecret = "your_clientSecret_from_google_console_project"

    val params = mutableStateOf(ItemKtorGoogleParams())

    val authCodeGet = mutableStateOf(false)
    val access_tokenGet = mutableStateOf(false)
    val refresh_tokenGet = mutableStateOf(false)

    val ktorGOA by lazy {
        KtorGoogleOAuth(DbArgs(dirMain), params.value ?: ItemKtorGoogleParams(), DbArgs(dirGoogle)){
            params.value = it
        }.apply {
            params.clientID = clientID
            params.clientSecret = clientSecret
            val strLoad = MyShifr.loadToken(fileToken)
            strLoad?.lines()?.let { lines ->
                if (lines.size == 5) {
                    with(StateVM) {
                        this@apply.params.authCode = lines[0]
                        this@apply.params.access_token = lines[1]
                        this@apply.params.refresh_token = lines[2]
                        this@apply.params.expires_in = lines[3].toInt()
                        this@apply.params.lastTimeCode = lines[4].toLong()
                        authCodeGet.value = this@apply.params.authCode != ""
                        access_tokenGet.value = this@apply.params.access_token != ""
                        refresh_tokenGet.value = this@apply.params.refresh_token != ""
                    }
                }
            }
            pushListDriveFile = {
                listGFile.clear()
                listGFile.addAll(it)
            }
        }
    }

    fun saveToken(){
            authCodeGet.value = ktorGOA.params.authCode != ""
            access_tokenGet.value = ktorGOA.params.access_token != ""
            refresh_tokenGet.value = ktorGOA.params.refresh_token != ""
        if (access_tokenGet.value && refresh_tokenGet.value && authCodeGet.value) {
            val strSave = "" +
                    ktorGOA.params.authCode + "\n" +
                    ktorGOA.params.access_token + "\n" +
                    ktorGOA.params.refresh_token + "\n" +
                    ktorGOA.params.expires_in + "\n" +
                    ktorGOA.params.lastTimeCode
            MyShifr.saveToken(strSave, fileToken)
        }
    }

    fun revokeToken(rez: (String)->Unit = {}){
        ktorGOA.revokeRefreshToken{
            rez("refresh")
            authCodeGet.value = ktorGOA.params.authCode != ""
            access_tokenGet.value = ktorGOA.params.access_token != ""
            refresh_tokenGet.value = ktorGOA.params.refresh_token != ""
        }
        ktorGOA.revokeAccessToken {
            rez("access")
            authCodeGet.value = ktorGOA.params.authCode != ""
            access_tokenGet.value = ktorGOA.params.access_token != ""
            refresh_tokenGet.value = ktorGOA.params.refresh_token != ""
        }
        ktorGOA.params.authCode = ""
        MyShifr.deleteFileToken(fileToken)
    }

    fun getMyAuthCode(authcode: String) = run {
        CoroutineScope(Dispatchers.IO).launch {
            authCodeGet.value = authcode != ""
            ktorGOA.getToken(
                authcode,
                clientID,
                clientSecret,
                "http://127.0.0.1:5000"
            ) { token ->
                saveToken()
            }
        }
    }

    val openEditStyle =  mutableStateOf(false)

    val selectionIdea = SingleSelectionType<ItemIdea>()
    val filterIdea: MutableState<String> = mutableStateOf("name")
    val filterIdeaStap: MutableState<String> = mutableStateOf("name")
    val selectBloknot: MutableState<ItemBloknot?> = mutableStateOf(null)
    val selectionBloknot = SingleSelectionType<ItemBloknot>()
    val selectionIdeaStap = SingleSelectionType<ItemIdeaStap>()
    val openIdeaOpis = mutableStateOf(false)

    val selectionTreeSkills = SingleSelectionType<ItemTreeSkill>()
    val openTreeSkills = mutableStateOf(false)

    val selectionTreeSkillsQuest = SingleSelectionType<ItemTreeSkillsQuest>()
    val openTreeSkillsQuest = mutableStateOf(false)
    val innerFinishAction: MutableState<InnerFinishAction?> = mutableStateOf(null)

    val commonItemStyleState = mutableStateOf(CommonItemStyleState(MainDB.styleParam.commonParam.commonItemStyle))
    val commonButtonStyleState = mutableStateOf(TextButtonStyleState(MainDB.styleParam.commonParam.commonTextButt))
    val commonButtonSeekBarStyleState = mutableStateOf(ButtonSeekBarStyleState(MainDB.styleParam.commonParam.mainSeekBarStyle))
    val commonPrivSchetPlanInfo = mutableStateOf(PrivSchetPlanInfoStyleState(MainDB.styleParam.commonParam.privSchetPlanInfo))

    @Composable
    fun startLaunchCommonStyle(){
        MainDB.styleParam.commonParam.commonItemStyle.getComposable(::CommonItemStyleState){
            println("startLaunchCommonStyle1")
            if (commonItemStyleState.value != it) commonItemStyleState.value = it
        }
        MainDB.styleParam.commonParam.mainSeekBarStyle.getComposable(::ButtonSeekBarStyleState) {
            println("startLaunchCommonStyle2")
            if (commonButtonSeekBarStyleState.value != it) commonButtonSeekBarStyleState.value = it
        }
        MainDB.styleParam.commonParam.commonTextButt.getComposable(::TextButtonStyleState) {
            println("startLaunchCommonStyle3")
            if (commonButtonStyleState.value != it) commonButtonStyleState.value = it
        }
        MainDB.styleParam.commonParam.privSchetPlanInfo.getComposable(::PrivSchetPlanInfoStyleState) {
            println("startLaunchCommonStyle4")
            if (commonPrivSchetPlanInfo.value != it) commonPrivSchetPlanInfo.value = it
        }
    }

    var time = Date().time

    fun timerStart(metka: String = ""){
        time = Date().time
        println("timerStart($metka): $time")
    }
    fun timerStop(metka: String = ""){
        val time2 = Date().time
        println("timerDelta($metka): ${time2 - time}")
        println("timerStop($metka): ${time2}")
    }

}