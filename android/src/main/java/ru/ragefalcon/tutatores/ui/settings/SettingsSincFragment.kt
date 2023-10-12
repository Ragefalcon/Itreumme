package ru.ragefalcon.tutatores.ui.settings

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.os.IBinder
import android.provider.Settings
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ragefalcon.sharedcode.myGoogleLib.ItemGDriveFile
import ru.ragefalcon.sharedcode.myGoogleLib.KtorGoogleOAuth
import ru.ragefalcon.sharedcode.source.disk.CommonName
import ru.ragefalcon.tutatores.BuildConfig.APPLICATION_ID
import ru.ragefalcon.tutatores.ItreummeApplication
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.ShowReasonsPermissions
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.GDriveFileRVItem
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.commonfragments.MenuPopupButton
import ru.ragefalcon.tutatores.commonfragments.MyPopupMenuItem
import ru.ragefalcon.tutatores.commonfragments.OneVoprosStrDial
import ru.ragefalcon.tutatores.databinding.FragmentSincSettBinding
import ru.ragefalcon.tutatores.extensions.showMyMessage
import ru.ragefalcon.tutatores.services.UploadFileBD
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Inject

/**
 *
 *   Вот по этим сслыкам можно найти почти все необходимое по части авторизации в Google Api:
 *
 *   https://developers.google.com/identity/sign-in/android/sign-in
 *   https://developers.google.com/identity/sign-in/android/offline-access
 *
 * */

class SettingsSincFragment() : BaseFragmentVM<FragmentSincSettBinding>(FragmentSincSettBinding::inflate) {

    @Inject
    lateinit var ktorGOA: KtorGoogleOAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ItreummeApplication.appComponent.inject(this)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    private var rvmAdapter = UniRVAdapter()

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful) {
                val signInAccount = task.result
                this.view?.let {
                    Snackbar
                        .make(it, "Вход в Google drive успешно выполнен", Snackbar.LENGTH_LONG)
                        .show()
                }
                Log.d("MyTag", "authCode === ${signInAccount?.serverAuthCode}")
                stateViewModel.authCode.value = signInAccount?.serverAuthCode
            } else {
                task.addOnCompleteListener { task ->
                    try {
                        val signInAccount = task.getResult(ApiException::class.java)
                        this.view?.let {
                            Snackbar
                                .make(it, "Вход в Google drive успешно выполнен", Snackbar.LENGTH_LONG)
                                .show()
                        }
                        stateViewModel.authCode.value = signInAccount?.serverAuthCode
                    } catch (apiException: ApiException) {
                    }
                }
            }
        } else {
            Log.d(
                "MyTag",
                "${result.resultCode} == ${AppCompatActivity.RESULT_OK}",
            )
        }
    }

    // setup permission callback
    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // success
                Log.d("MyTag", "registerForActivityResult success")
            } else {
                Log.d("MyTag", "registerForActivityResult failure")
                // failure
            }
        }

    private fun checkVisibleOnlineButt(value: Boolean) {
        with(binding) {
            if (value) {
                signInButton.visibility = INVISIBLE
                buttExit.visibility = VISIBLE
                buttGetFiles.visibility = VISIBLE
                buttUploadMyBase.visibility = VISIBLE
                buttNetworkMyBase.visibility = VISIBLE
            } else {
                signInButton.visibility = VISIBLE
                buttExit.visibility = INVISIBLE
                buttGetFiles.visibility = INVISIBLE
                buttUploadMyBase.visibility = INVISIBLE
                buttNetworkMyBase.visibility = INVISIBLE
            }
        }
    }

    private val gFiles: MutableLiveData<List<ItemGDriveFile>> = MutableLiveData<List<ItemGDriveFile>>(listOf())

    private val progressValue = MutableLiveData<Int>(0)
    private var mBound = false


    private val connection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, binder: IBinder?) {
            mBound = true
            (binder as? UploadFileBD.LocalBinder)?.getService()?.get()?.let { serviceUploadBD ->
                serviceUploadBD.progressValue.observe(viewLifecycleOwner) {
                    progressValue.value = it
                }
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName?) {
            mBound = false
        }
    }

    override fun onStop() {
        super.onStop()
        requireContext().unbindService(connection)
        mBound = false
    }

    override fun onStart() {
        super.onStart()
        val intentBindService = Intent(requireContext(), UploadFileBD::class.java)
        requireContext().bindService(intentBindService, connection, 0) //Context.BIND_AUTO_CREATE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            with(rvDriveFileList) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
                (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            }

            val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), stateViewModel.gso);

            progressValue.observe(viewLifecycleOwner) {
                binding.progLoad.progress = it
            }

            val menuPopupGFile =
                MyPopupMenuItem<ItemGDriveFile>(WeakReference(this@SettingsSincFragment), "GFilePopup").apply {
                    addButton(MenuPopupButton.DELETE) { itemGFile ->
                        viewLifecycleOwner.lifecycleScope.launch {
                            ktorGOA.deleteFile(itemGFile.id)
                            getGFileList()
                        }
                    }
                    addButton(MenuPopupButton.LOAD) { itemGFile ->
                        ktorGOA.downloadFile(itemGFile.id) { progress ->
                            binding.progLoad.progress = (progress * 100).toInt()
                        }
                    }
                    addButton(MenuPopupButton.OVERWRITE) { itemGFile ->
                        viewLifecycleOwner.lifecycleScope.launch {
                            ktorGOA.overwriteFile("databasefff.db", itemGFile.id) { progress ->
                                binding.progLoad.progress = (progress * 100).toInt()
                                if (progress == 1F) binding.progLoad.progress = 0
                            }
                        }
                    }
                }
            val vopName = OneVoprosStrDial(WeakReference(this@SettingsSincFragment), "voprosNameGnewFile") {
                viewLifecycleOwner.lifecycleScope.launch {
                    val intentUploadService = Intent(requireActivity().applicationContext, UploadFileBD::class.java)
                    intentUploadService.putExtra(UploadFileBD.nameExtra.nameBDonDevice.value, "databasefff.db")
                    intentUploadService.putExtra(UploadFileBD.nameExtra.nameNewBDonGoogle.value, it)
                    when {
                        ContextCompat.checkSelfPermission(
                            requireActivity(),
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                requireContext().applicationContext.startForegroundService(intentUploadService)
                            } else {
                                requireContext().applicationContext.startService(intentUploadService)
                            }
                            val intentBindService = Intent(requireContext(), UploadFileBD::class.java)
                            requireContext().bindService(intentBindService, connection, 0)

                        }

                        shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                            showInContextUI()
                        }

                        else -> {
                            requestPermissionLauncher.launch(
                                Manifest.permission.ACCESS_NOTIFICATION_POLICY
                            )
                        }
                    }
                }
            }
            val vopNameFolder =
                OneVoprosStrDial(WeakReference(this@SettingsSincFragment), "voprosNameGnewFolder") { nameFolder ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        ktorGOA.createFolder(nameFolder)
                        getGFileList()
                    }
                }

            ktorGOA.gFiles.observe(viewLifecycleOwner) { listGFile ->
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                    Log.d("MyTag", "############################    ktorGOA.gFiles.observe work")
                    rvmAdapter.updateData(formUniRVItemList(listGFile) { item ->
                        GDriveFileRVItem(item, longTapListener = { itemGFile ->
                            menuPopupGFile.showMenu(itemGFile, name = itemGFile.name)
                        })
                    })
                }
            }

            buttNewFolder.visibility = INVISIBLE
            buttOldMyBase.visibility = INVISIBLE
            checkVisibleOnlineButt(stateViewModel.authCode.value != "")
            stateViewModel.authCode.observe(viewLifecycleOwner) {

                checkVisibleOnlineButt(it != "")
                if (it != "") {
                    ktorGOA.getToken(it, stateViewModel.clientID, stateViewModel.clientSecret) { token ->
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                            tvAuthCode.text = token
                        }
                    }
                }
            }
            signInButton.setSize(SignInButton.SIZE_STANDARD)
            signInButton.setOnClickListener {
                val signInIntent = mGoogleSignInClient.signInIntent
                launcher.launch(signInIntent)
            }
            signInButton.visibility = VISIBLE
            buttExit.visibility = INVISIBLE
            buttExit.setOnClickListener {
                mGoogleSignInClient.revokeAccess()
                mGoogleSignInClient.signOut()
                    .addOnCompleteListener(requireActivity()) {
                        stateViewModel.authCode.value = ""
                    }
            }
            buttBdToDevice.setOnClickListener {
                copyBdToDevice()
            }
            buttGetFiles.setOnClickListener {
                getGFileList()
            }
            buttOldMyBase.setOnClickListener {
                loadBaseFileDB()
            }
            buttNetworkMyBase.setOnClickListener {
                loadBaseNetworkFileDB()
            }
            buttUploadMyBase.setOnClickListener {
                vopName.showVopros("Напишите имя нового файла в облаке.", "Имя:", "Загрузить", "MyMainDB_NB_")
            }
            buttNewFolder.setOnClickListener {
                vopNameFolder.showVopros(
                    "Напишите имя новой папки в облаке.",
                    "Имя:",
                    "Загрузить",
                    "Android_new_folder"
                )
            }
            buttNotifSett.setOnClickListener {
                val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, "ru.ragefalcon.tutatores")
                    putExtra(Settings.EXTRA_CHANNEL_ID, getString(R.string.channel_google_drive_id))
                }

                startActivity(intent)
            }
        }
    }

    private fun showInContextUI() {
        val intentShowReasons = Intent(requireContext(), ShowReasonsPermissions::class.java)
        intentShowReasons.putExtra(ShowReasonsPermissions.keyExtraPermissions, Manifest.permission.POST_NOTIFICATIONS)
        startActivity(intentShowReasons)
    }


    private fun copyBdToDevice() {
        if (SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    val uri = Uri.parse("package:" + APPLICATION_ID);
                    var intent = Intent(
                        Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                        uri
                    )
                    startActivity(intent);
                } catch (ex: Exception) {
                    val intent = Intent();
                    intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION;
                    startActivity(intent);
                }
                /**
                 * https://stackoverflow.com/questions/62782648/android-11-scoped-storage-permissions/67140033
                 * Т.к. я стартую активити без возвращения результата, то для копии БД кнопку будет нужно нажать еще раз.
                 * */

            } else {
                copyFileDBToDevice()
            }
        } else {

            copyFileDBToDevice()
        }
    }

    private fun getGFileList() {
        ktorGOA.GetAppFilesList {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                binding.tvAuthCode.text = it
                binding.tvAuthCode.requestLayout()
                binding.tvAuthCode.movementMethod = ScrollingMovementMethod()
            }
        }

    }

    private fun loadBaseFileDB() {
        val ASSETS_PATH = "databases"

        val DATABASE_NAME = "MyMainDB_2021_06_24"
        loadBaseFileDB(requireContext().assets.open("$ASSETS_PATH/$DATABASE_NAME.db"), "$ASSETS_PATH/$DATABASE_NAME.db")
    }

    private fun loadBaseNetworkFileDB() {
        loadBaseFileDB(
            FileInputStream(File(requireContext().getDatabasePath(CommonName.nameFromNetworkDBfile).path)),
            requireContext().getDatabasePath(CommonName.nameFromNetworkDBfile).path
        )
    }

    private fun loadBaseFileDB(inputStream: InputStream, pathFile: String = "") {
        try {
            val outputFile = File(requireContext().getDatabasePath("databasefff.db").path)
            val outputStream = FileOutputStream(outputFile)

            inputStream.copyTo(outputStream)
            inputStream.close()

            outputStream.flush()
            outputStream.close()
        } catch (exception: Throwable) {
            throw RuntimeException("The $pathFile database couldn't be installed.", exception)
        }
    }

    private fun copyFileDBToDevice() {

        try {
            val inputStream = FileInputStream(File(requireContext().getDatabasePath("databasefff.db").path))

            var dirFile = getExternalStorageDirectory()

            val fileTmp = File(File(dirFile.path, "Itreumme"), "backupDB")
            fileTmp.mkdirs()
            var mkdirr = fileTmp.exists()
            mkdirr = true
            val outputFile = if (mkdirr) File(
                fileTmp,
                "database_${Date().time}.db"
            ) else File(context?.getExternalFilesDir("")?.absolutePath ?: "", "database_${Date().time}.db")
            val outputStream = FileOutputStream(outputFile)

            val sizeCopy = inputStream.copyTo(outputStream)
            inputStream.close()

            outputStream.flush()
            outputStream.close()
            if (sizeCopy > 0) showMyMessage("Файл базы данных успешно скопирован. \n ${outputFile.path}")
        } catch (exception: Throwable) {
            throw RuntimeException("The pathFile database couldn't be installed.", exception)
        }
    }
}
