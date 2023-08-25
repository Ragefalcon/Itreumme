package ru.ragefalcon.tutatores.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.provider.Settings
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ragefalcon.sharedcode.myGoogleLib.ItemGDriveFile
import ru.ragefalcon.tutatores.BuildConfig.APPLICATION_ID
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.GDriveFileRVItem
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.commonfragments.MenuPopupButton
import ru.ragefalcon.tutatores.commonfragments.MyPopupMenuItem
import ru.ragefalcon.tutatores.commonfragments.OneVoprosStrDial
import ru.ragefalcon.tutatores.databinding.FragmentSincSettBinding
import ru.ragefalcon.tutatores.extensions.showMyMessage
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*


class SettingsSincFragment() : BaseFragmentVM<FragmentSincSettBinding>(FragmentSincSettBinding::inflate) {


    private var rvmAdapter = UniRVAdapter()

    /**
     *
     *   Вот по этим сслыкам можно найти почти все необходимое по части авторизации в Google Api:
     *
     *   https://developers.google.com/identity/sign-in/android/sign-in
     *   https://developers.google.com/identity/sign-in/android/offline-access
     *
     * */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            with(rvDriveFileList) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
                (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            }
            val menuPopupGFile = MyPopupMenuItem<ItemGDriveFile>(this@SettingsSincFragment, "GFilePopup").apply {
                addButton(MenuPopupButton.DELETE) { itemGFile ->
                    lifecycleScope.launch {
                        stateViewModel.ktorGOA.deleteFile(itemGFile.id)
                        getGFileList()
                    }
                }
                addButton(MenuPopupButton.LOAD) { itemGFile ->
                    stateViewModel.ktorGOA.downloadFile(itemGFile.id) { progress ->
                        progLoad.progress = (progress * 100).toInt()
                    }
                }
                addButton(MenuPopupButton.OVERWRITE) { itemGFile ->
                    lifecycleScope.launch {
                        stateViewModel.ktorGOA.overwriteFile("databasefff.db", itemGFile.id) { progress ->
                            progLoad.progress = (progress * 100).toInt()
                            if (progress == 1F) progLoad.progress = 0
                        }
                    }
                }
            }
            stateViewModel.ktorGOA.pushListDriveFile = { listGFile ->
                rvmAdapter.updateData(formUniRVItemList(listGFile) { item ->
                    GDriveFileRVItem(item, longTapListener = { itemGFile ->
                        menuPopupGFile.showMenu(itemGFile, name = itemGFile.name)
                    })
                })
            }

            val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), stateViewModel.gso);
            fun checkVisibleOnlineButt(value: Boolean) {
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
            buttNewFolder.visibility = INVISIBLE
            buttOldMyBase.visibility = INVISIBLE
            checkVisibleOnlineButt(stateViewModel.authCode.value != "")
            stateViewModel.authCode.observe(viewLifecycleOwner) {

                checkVisibleOnlineButt(it != "")
                if (it != "") {
                    stateViewModel.ktorGOA.getToken(it, stateViewModel.clientID, stateViewModel.clientSecret) { token ->
                        lifecycleScope.launch(Dispatchers.Main) {
                            tvAuthCode.text = token
                        }
                    }
                }
            }
            signInButton.setSize(SignInButton.SIZE_STANDARD)
            signInButton.setOnClickListener {
                val signInIntent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, stateViewModel.RC_SIGN_IN)
            }
            signInButton.visibility = VISIBLE
            buttExit.visibility = INVISIBLE
            buttExit.setOnClickListener {
                mGoogleSignInClient.revokeAccess()
                mGoogleSignInClient.signOut()
                    .addOnCompleteListener(requireActivity(), OnCompleteListener<Void?> {
                        stateViewModel.authCode.value = ""

                    })

            }
            buttBdToDevice.setOnClickListener {
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
                }   else    {

                    copyFileDBToDevice()
                }
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
            val vopName = OneVoprosStrDial(this@SettingsSincFragment, "voprosNameGnewFile") {
                lifecycleScope.launch {
                    stateViewModel.ktorGOA.uploadFile("databasefff.db", it) { progress ->
                        progLoad.progress = (progress * 100).toInt()
                    }
                    getGFileList()
                }
            }
            buttUploadMyBase.setOnClickListener {
                vopName.showVopros("Напишите имя нового файла в облаке.", "Имя:", "Загрузить", "MyMainDB_NB_")
            }
            val vopNameFolder = OneVoprosStrDial(this@SettingsSincFragment, "voprosNameGnewFolder") { nameFolder ->
                lifecycleScope.launch {
                    stateViewModel.ktorGOA.createFolder(nameFolder)
                    getGFileList()
                }
            }
            buttNewFolder.setOnClickListener {
                vopNameFolder.showVopros(
                    "Напишите имя новой папки в облаке.",
                    "Имя:",
                    "Загрузить",
                    "Android_new_folder"
                )
            }
        }
    }

    private fun getGFileList() {
        stateViewModel.ktorGOA.GetAppFilesList {
            lifecycleScope.launch(Dispatchers.Main) {
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
            FileInputStream(File(requireContext().getDatabasePath("testNetwork.db").path)),
            requireContext().getDatabasePath("testNetwork.db").path
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

            val fileTmp = File(File(dirFile.path, "Itreumme"),"backupDB")
            fileTmp.mkdirs()
            var mkdirr = fileTmp.exists()
            mkdirr=true
            val outputFile = if (mkdirr) File(fileTmp,"database_${Date().time}.db") else File(context?.getExternalFilesDir("")?.absolutePath ?: "","database_${Date().time}.db")
            val outputStream = FileOutputStream(outputFile)

            val sizeCopy = inputStream.copyTo(outputStream)
            inputStream.close()

            outputStream.flush()
            outputStream.close()
            if (sizeCopy>0) showMyMessage("Файл базы данных успешно скопирован. \n ${outputFile.path}")
        } catch (exception: Throwable) {
            throw RuntimeException("The pathFile database couldn't be installed.", exception)
        }
    }
}
