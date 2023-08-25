package ru.ragefalcon.tutatores.ui.viewmodels

import android.app.Application
import android.util.SparseArray
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope
import ru.ragefalcon.sharedcode.models.data.*
import ru.ragefalcon.sharedcode.myGoogleLib.ItemKtorGoogleParams
import ru.ragefalcon.sharedcode.myGoogleLib.KtorGoogleOAuth
import ru.ragefalcon.sharedcode.source.disk.DbArgs
import ru.ragefalcon.tutatores.adapter.FinanceType

class MyStateViewModel(application: Application, private val savedStateHandle: SavedStateHandle) :
    AndroidViewModel(application) {

    val someState: MutableLiveData<String> = savedStateHandle.getLiveData("someState")
    var firstStart = true
    var statusBarSize = savedStateHandle.getLiveData<Int>("statusBarSize", 0)
    var navigationBarSize = savedStateHandle.getLiveData<Int>("navigationBarSize", 0)

    val clientID = "your_client_id_from_google_console_project"
    val clientSecret = "your_clientSecret_from_google_console_project"

    val RC_SIGN_IN = 9001
    var gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestScopes(Scope(Scopes.DRIVE_APPFOLDER))
        .requestServerAuthCode(clientID)
        .build()

    val params = savedStateHandle.getLiveData<ItemKtorGoogleParams>("params", ItemKtorGoogleParams())
    val authCode = savedStateHandle.getLiveData<String>("authCode", "")
    val ktorGOA = KtorGoogleOAuth(DbArgs(application), params.value ?: ItemKtorGoogleParams()) { params.value = it }

    var currentFinType: FinanceType = FinanceType.RASXOD
    var changeItemCommonFinOper: ItemCommonFinOper? = savedStateHandle.get<ItemCommonFinOper?>("changeItemRasxod")
    val selectItemPlan: LiveData<ItemPlan?> = savedStateHandle.getLiveData<ItemPlan?>("selectItemPlan", null)
    val selectItemBloknot = savedStateHandle.getLiveData<ItemBloknot?>("selectItemBloknot", null)
    val selectItemGoal = savedStateHandle.getLiveData<ItemGoal?>("selectItemGoal", null)
    val selectItemDream = savedStateHandle.getLiveData<ItemDream?>("selectItemDream", null)
    val selectItemIdea = savedStateHandle.getLiveData<ItemIdea?>("selectItemIdea", null)
    val visAddFinPanel = savedStateHandle.getLiveData<Boolean>("visAddFinPanel", false)
    val visChangeFinPanel = savedStateHandle.getLiveData<Boolean>("visChangeFinPanel", false)
    val visFilterFinPanel = savedStateHandle.getLiveData<Boolean>("visFilterFinPanel", false)


    val gotovSelDenPlan = savedStateHandle.getLiveData<Int>("gotovSelDenPlan", 0)
    val gotovSelPlan = savedStateHandle.getLiveData<Int>("gotovSelPlan", 0)
    val gotovSelPlanStap = savedStateHandle.getLiveData<Int>("gotovSelPlanStap", 0)


    enum class journal_nav { bloknot, idea, stap_idea }

    val sel_jornal_nav = MutableLiveData<journal_nav>().apply { this.value = journal_nav.bloknot }
    val needUpdateGoalDetailFrag = savedStateHandle.getLiveData<Boolean>("needUpdateGoalDetailFrag", false)


    var tmpItemNapom: ItemNapom? = null
    val saveInstanceUniItem =
        savedStateHandle.get<SparseArray<UniItem>>("saveInstanceUniItem") ?: SparseArray<UniItem>()


    var tmpTimeStampLong: Long = 0

}