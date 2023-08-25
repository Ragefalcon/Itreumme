package ru.ragefalcon.tutatores

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock.sleep
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ragefalcon.tutatores.databinding.ActivityMainBinding
import ru.ragefalcon.tutatores.extensions.getMyAnimListener
import ru.ragefalcon.tutatores.extensions.setMargins
import ru.ragefalcon.tutatores.extensions.setWindowTransparency
import ru.ragefalcon.tutatores.ui.finance.Postman
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity(), Postman {

    private val bbb by lazy { sleep(600) }

    private lateinit var container: FrameLayout
    private lateinit var navController: NavController
    private lateinit var drawer: DrawerLayout

    val viewmodel: AndroidFinanceViewModel by viewModels()
    val stateViewModel: MyStateViewModel by viewModels {
        SavedStateViewModelFactory(application, this)
    }

    private var keyUpVol: (() -> Unit)? = {}

    private lateinit var binding: ActivityMainBinding


    private fun googleSilentSignIn() {
        if (stateViewModel.authCode.value == "") {
            val googleSignInClient = GoogleSignIn.getClient(this, stateViewModel.gso);
            val task: Task<GoogleSignInAccount> = googleSignInClient.silentSignIn()
            if (task.isSuccessful) {
                val signInAccount = task.result
                stateViewModel.authCode.value = signInAccount?.serverAuthCode
            } else {
                task.addOnCompleteListener { task ->
                    try {
                        val signInAccount = task.getResult(ApiException::class.java)
                        stateViewModel.authCode.value = signInAccount?.serverAuthCode
                    } catch (apiException: ApiException) {
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        
        setContentView(view)















        container = findViewById(R.id.main_fragment)
        drawer = findViewById(R.id.drawer_layout)

        container.alpha = 0F
        val finAlpha = binding.imgVerxPaper.alpha
        binding.imgVerxPaper.alpha = 0F
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_fragment) as NavHostFragment? ?: return
        navController = host.navController


        val sideBar = findViewById<NavigationView>(R.id.nav_view)
        sideBar?.setupWithNavController(navController)
        sideBar?.menu?.findItem(R.id.exitApp)?.setOnMenuItemClickListener {
            this@MainActivity.finish()
            exitProcess(0)

        }



        viewmodel.addDateOwner(this)

        val tvTitleHeader = sideBar?.getHeaderView(0)?.findViewById<TextView>(R.id.title_my_header)
        tvTitleHeader?.text = "Turum-pum-pum"

        stateViewModel.authCode.observe(this) {

            if (it != "") {
                sideBar?.getHeaderView(0)?.findViewById<ToggleButton>(R.id.tb_google_online)?.isChecked = true






            }   else    {
                sideBar?.getHeaderView(0)?.findViewById<ToggleButton>(R.id.tb_google_online)?.isChecked = false
            }
        }
        CoroutineScope(Dispatchers.Default).launch {
            googleSilentSignIn()
        }



        ViewCompat.animate(binding.imgVerxPaper)
            .alpha(finAlpha)
            .setDuration(500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(
                getMyAnimListener(aEnd = {
                    ViewCompat.animate(container)
                        .alpha(1f)
                        .setDuration(500)
                        .setInterpolator(AccelerateDecelerateInterpolator())
                        .setListener(getMyAnimListener {
                            supportFragmentManager.setFragmentResult("StartFirstDialog", androidx.core.os.bundleOf())
                        })
                })
            )

    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }

    override fun onResume() {
        super.onResume()
        setWindowTransparency { statusBarSize, navigationBarSize ->

            if (stateViewModel.firstStart) {
                stateViewModel.statusBarSize.value = statusBarSize
                stateViewModel.navigationBarSize.value = navigationBarSize
                stateViewModel.firstStart = false
            }
            setMargins(container, container.paddingLeft, 0, container.paddingRight, 0)


        }














    }

    override fun onBackPressed() {
        drawer.openDrawer(Gravity.START)

    }

    companion object {
        
        const val FLAGS_FULLSCREEN =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        
        const val ANIMATION_FAST_MILLIS = 50L
        const val ANIMATION_SLOW_MILLIS = 100L
        private const val IMMERSIVE_FLAG_TIMEOUT = 500L
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)




        Snackbar.make(
            container,
            "$requestCode == ${stateViewModel.RC_SIGN_IN}\n resultCode = $data -- $RESULT_OK -- $RESULT_CANCELED",
            Snackbar.LENGTH_LONG
        )
            .show()

        Snackbar.make(container, "requestCode === stateViewModel.RC_SIGN_IN", Snackbar.LENGTH_LONG)
            .show()


        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
            Snackbar.make(container, "authCode === ${account?.serverAuthCode}", Snackbar.LENGTH_LONG)
                .show()
            stateViewModel.authCode.value = account?.serverAuthCode






        } catch (e: ApiException) {


        }



    }

    override fun setKeyUpVol(keyUV: () -> Unit) {
        keyUpVol = keyUV
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {














            KeyEvent.KEYCODE_VOLUME_UP -> {
                keyUpVol?.invoke()



                return true
            }
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                keyUpVol?.invoke()
                Snackbar.make(container, "Нажата кнопка громкости DOWN", Snackbar.LENGTH_LONG)
                    .show()


                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}


private fun loadBaseFileDB(context: Context) {
    val ASSETS_PATH = "databases"
    val DATABASE_NAME = "databasefff"

    loadBaseFileDB(context, context.assets.open("$ASSETS_PATH/$DATABASE_NAME.db"), "$ASSETS_PATH/$DATABASE_NAME.db")
}

private fun loadBaseFileDB(context: Context, inputStream: InputStream, pathFile: String = "") {

    try {

        val outputFile = File(context.getDatabasePath("databasefff.db").path)
        val outputStream = FileOutputStream(outputFile)

        inputStream.copyTo(outputStream)
        inputStream.close()

        outputStream.flush()
        outputStream.close()
    } catch (exception: Throwable) {
        throw RuntimeException("The $pathFile database couldn't be installed.", exception)
    }
}
