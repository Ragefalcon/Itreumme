package ru.ragefalcon.tutatores

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock.sleep
import android.util.AttributeSet
import android.util.Log
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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.ragefalcon.sharedcode.Database
import ru.ragefalcon.sharedcode.source.disk.DatabaseCreator
import ru.ragefalcon.sharedcode.source.disk.DbArgs
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
        Log.d("MyTut", "Povis: first log");
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        /**
         * походу setWindowTransparency просто не запускается на этом этапе...
         *         setWindowTransparency { statusBarSize, navigationBarSize ->
        stateViewModel.statusBarSize.value = statusBarSize
        Log.d("statBar","Main Activity onCreate stateViewModel.statusBarSize: ${stateViewModel.statusBarSize}")
        Log.d("statBar","Main Activity onCreate stateViewModel.navigationBarSize: ${stateViewModel.navigationBarSize}")
        stateViewModel.navigationBarSize.value = navigationBarSize
        //            setMargins(container, container.paddingLeft, 0, container.paddingRight, 0)
        //            setMargins(container, container.marginLeft, container.marginTop, container.marginRight, navigationBarSize)
        //            setMargins(container, container.marginLeft, statusBarSize, container.marginRight, navigationBarSize)
        //            container.refreshDrawableState()
        }

         * */
        setContentView(view)
//        setContentView(R.layout.activity_main)
//        window.statusBarColor = Color.TRANSPARENT
//        window.navigationBarColor = Color.TRANSPARENT
//        setWindowTransparency { statusBarSize, navigationBarSize ->
//            setMargins(tv_allCapital, 0, statusBarSize, 0, 0)
//            setMargins(butt_week, 0, 0, 0, navigationBarSize)
//        }
//        setWindowTransparency { statusBarSize, navigationBarSize ->
//            stateViewModel.statusBarSize = statusBarSize
//            stateViewModel.navigationBarSize = navigationBarSize
//            Log.d("statBar","statusBarSize: $statusBarSize")
//            Log.d("statBar","stateViewModel.statusBarSize: ${stateViewModel.statusBarSize}")
////            setMargins(container, container.paddingLeft, 0, container.paddingRight, container.paddingBottom) //statusBarSize + 4
////            setMargins(container, container.marginLeft, container.marginTop, container.marginRight, navigationBarSize)
//        }
        container = findViewById(R.id.main_fragment)
        drawer = findViewById(R.id.drawer_layout)

        container.alpha = 0F
        val finAlpha = binding.imgVerxPaper.alpha
        binding.imgVerxPaper.alpha = 0F
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_fragment) as NavHostFragment? ?: return
        navController = host.navController
//        drawer.closeDrawer(Gravity.RIGHT)
        // включаем боковое меню
        val sideBar = findViewById<NavigationView>(R.id.nav_view)
        sideBar?.setupWithNavController(navController)
        sideBar?.menu?.findItem(R.id.exitApp)?.setOnMenuItemClickListener {
            Log.d("MyTut", "exitApp: launch");
            this@MainActivity.finish()
            exitProcess(0)
//            true
        }
//        loadBaseFileDB(this)

/*
        var mDatabase2: Database? = DatabaseCreator.getDataBase(DbArgs(application, "testtt00.db"))
        println(
            "GetDB:${mDatabase2}"
        )
//        Database.Schema.create(DatabaseCreator.sqlDriver!!)
        println(
            "GetDB:mDatabase2"
        )
        mDatabase2
//        mDatabase2!!.schetaQueries.sumAllCapital().execute()
//        viewmodel
*/
        viewmodel.addDateOwner(this)

        val tvTitleHeader = sideBar?.getHeaderView(0)?.findViewById<TextView>(R.id.title_my_header)
        tvTitleHeader?.text = "Turum-pum-pum"

        stateViewModel.authCode.observe(this) {
//                tv_authCode.text = it
            if (it != "") {
                sideBar?.getHeaderView(0)?.findViewById<ToggleButton>(R.id.tb_google_online)?.isChecked = true
//                ktorGOA.getToken(it, stateViewModel.clientID, stateViewModel.clientSecret) { token ->
//                    Log.d("GetToken", token)
//                    GlobalScope.launch(Dispatchers.Main) {
//                        tvAuthCode.text = token
//                    }
//                }
            }   else    {
                sideBar?.getHeaderView(0)?.findViewById<ToggleButton>(R.id.tb_google_online)?.isChecked = false
            }
        }
        GlobalScope.launch {
            googleSilentSignIn()
        }



        Log.d("MyTut", "startDial: testttMA");
        ViewCompat.animate(binding.imgVerxPaper)
            .alpha(finAlpha)
            .setDuration(500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(
                getMyAnimListener(aEnd = {
                    Log.d("MyTut", "startDial: testttMA1");
                    ViewCompat.animate(container)
                        .alpha(1f)
                        .setDuration(500)
                        .setInterpolator(AccelerateDecelerateInterpolator())
                        .setListener(getMyAnimListener {
                            Log.d("MyTut", "startDial: testttMA2");
                            supportFragmentManager.setFragmentResult("StartFirstDialog", androidx.core.os.bundleOf())
                            Log.d("MyTut", "startDial: testttMA3");
                        })
                })
            )
//        supportFragmentManager.setFragmentResult("StartFirstDialog", androidx.core.os.bundleOf())
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }

    override fun onResume() {
        super.onResume()
        setWindowTransparency { statusBarSize, navigationBarSize ->

            if (stateViewModel.firstStart) {
                stateViewModel.statusBarSize.value = statusBarSize
                Log.d(
                    "statBar",
                    "Main Activity onResume stateViewModel.statusBarSize: ${stateViewModel.statusBarSize}"
                )
                Log.d(
                    "statBar",
                    "Main Activity onResume stateViewModel.navigationBarSize: ${stateViewModel.navigationBarSize}"
                )
                stateViewModel.navigationBarSize.value = navigationBarSize
                stateViewModel.firstStart = false
            }
            setMargins(container, container.paddingLeft, 0, container.paddingRight, 0)
//            setMargins(container, container.marginLeft, container.marginTop, container.marginRight, navigationBarSize)
//            setMargins(container, container.marginLeft, statusBarSize, container.marginRight, navigationBarSize)
        }
        // Before setting full screen flags, we must wait a bit to let UI settle; otherwise, we may
        // be trying to set app to immersive mode before it's ready and the flags do not stick
//        container.postDelayed({
//            /** Если оставить эту строчку, то видимо фрагмент пытается растянуться на весь экран, при этом
//             *  тэг fullscreen не понятно до конца как работает, потому что статус бар в этом случае черный,
//             *  видимо это перекликается с разрешениями на полноэкранный режим, а может еще с чем то...)
//             *  в итоге когда у меня уже установлен прозрачный статус бар и отступы для элементов это вызывает
//             *  временное закрашивание статус бара в черный и мерцание и ерзание в это время всего интерфейса.
//             **/
////            setFitsSystemWindows(true);
////            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//            container.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
////            container.systemUiVisibility = FLAGS_FULLSCREEN
//        }, IMMERSIVE_FLAG_TIMEOUT)
    }

    override fun onBackPressed() {
        drawer.openDrawer(Gravity.START)
//        hideAddLayout(150)
    }

    companion object {
        /** Combination of all flags required to put activity into
         *  !!! Нужно бы поподробнее изучить, что каждый из этих флагов значит...
         **/
        const val FLAGS_FULLSCREEN =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        /** Milliseconds used for UI animations */
        const val ANIMATION_FAST_MILLIS = 50L
        const val ANIMATION_SLOW_MILLIS = 100L
        private const val IMMERSIVE_FLAG_TIMEOUT = 500L
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        Snackbar.make(container, "onActivityResult", Snackbar.LENGTH_LONG)
//            .show()
        Snackbar.make(
            container,
            "$requestCode == ${stateViewModel.RC_SIGN_IN}\n resultCode = $data -- $RESULT_OK -- $RESULT_CANCELED",
            Snackbar.LENGTH_LONG
        )
            .show()
//        if (requestCode == stateViewModel.RC_SIGN_IN) {
        Snackbar.make(container, "requestCode === stateViewModel.RC_SIGN_IN", Snackbar.LENGTH_LONG)
            .show()
        // The Task returned from this call is always completed, no need to attach
        // a listener.
        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
            Snackbar.make(container, "authCode === ${account?.serverAuthCode}", Snackbar.LENGTH_LONG)
                .show()
            stateViewModel.authCode.value = account?.serverAuthCode
//                Log.d("GoogleSignIn", stateViewModel.authCode.value)
//                println("GoogleSignIn ${stateViewModel.authCode.value}")
            // Show signed-un UI
//                    updateUI(account)

//                     TODO(developer): send code to server and exchange for access/refresh/ID tokens
        } catch (e: ApiException) {
//                    Log.w(TAG, "Sign-in failed", e)
//                    updateUI(null)
        }
//            handleSignInResult(task)
//        }

    }

    override fun setKeyUpVol(keyUV: () -> Unit) {
        keyUpVol = keyUV
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
//            KeyEvent.KEYCODE_MENU -> {
//                Toast.makeText(this, "Нажата кнопка Меню", Toast.LENGTH_SHORT)
//                    .show()
//                return true
//            }
//            KeyEvent.KEYCODE_SEARCH -> {
//                Toast.makeText(this, "Нажата кнопка Поиск", Toast.LENGTH_SHORT)
//                    .show()
//                return true
//            }
//            KeyEvent.KEYCODE_BACK -> {
//                onBackPressed()
//                return true
//            }
            KeyEvent.KEYCODE_VOLUME_UP -> {
                keyUpVol?.invoke()
//                Snackbar.make(container, "Нажата кнопка громкости UP", Snackbar.LENGTH_LONG)
//                    .show()
//                event.startTracking()
                return true
            }
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                keyUpVol?.invoke()
                Snackbar.make(container, "Нажата кнопка громкости DOWN", Snackbar.LENGTH_LONG)
                    .show()
//                Toast.makeText(this, "Нажата кнопка громкости", Toast.LENGTH_SHORT)
//                    .show()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}


private fun loadBaseFileDB(context: Context) {
    val ASSETS_PATH = "databases"
    val DATABASE_NAME = "databasefff"
//        val DATABASE_NAME = "MyMainDB_2021_06_24"
    loadBaseFileDB(context, context.assets.open("$ASSETS_PATH/$DATABASE_NAME.db"), "$ASSETS_PATH/$DATABASE_NAME.db")
}

private fun loadBaseFileDB(context: Context, inputStream: InputStream, pathFile: String = "") {
    Log.d("LoadBD", "До открытия входного потока")
//        val inputStream = requireContext().assets.open(pathFile)
    Log.d("LoadBD", "Поток открыт")

    try {

        val outputFile = File(context.getDatabasePath("databasefff.db").path)
        Log.d("LoadBD", "Открыт выходной файл")
        val outputStream = FileOutputStream(outputFile)
        Log.d("LoadBD", "Открыт поток в выходной файл")

        inputStream.copyTo(outputStream)
        Log.d("LoadBD", "База скопирована в выходной поток")
        inputStream.close()

        outputStream.flush()
        outputStream.close()
    } catch (exception: Throwable) {
        throw RuntimeException("The $pathFile database couldn't be installed.", exception)
    }
}
