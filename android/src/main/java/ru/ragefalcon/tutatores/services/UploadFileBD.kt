package ru.ragefalcon.tutatores.services

import android.Manifest
import android.app.ForegroundServiceStartNotAllowedException
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.ragefalcon.sharedcode.myGoogleLib.KtorGoogleOAuth
import ru.ragefalcon.tutatores.ItreummeApplication
import javax.inject.Inject

class UploadFileBD : Service() {

    enum class nameExtra(val value: String){
        nameBDonDevice("nameBDonDevice"),
        nameNewBDonGoogle("nameNewBDonGoogle");
    }

    //    private lateinit var viewModel: MyStateViewModel
    @Inject
    lateinit var ktorGOA: KtorGoogleOAuth

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Default + serviceJob)

    override fun onCreate() {
        ItreummeApplication.appComponent.inject(this)
    }
    private fun startForeground() {
        // Before starting the service as foreground check that the app has the
        // appropriate runtime permissions. In this case, verify that the user has
        // granted the CAMERA permission.
        val internetPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED
        val readStoragePermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
        if (internetPermission || readStoragePermission) {
            // Without camera permissions the service cannot run in the foreground
            // Consider informing user or updating your app UI if visible.
            stopSelf()
            return
        }

        try {
            val notification = NotificationCompat.Builder(this, "CHANNEL_ID")
                // Create the notification to display while the service is running
                .build()
            ServiceCompat.startForeground(
                /* service = */ this,
                /* id = */ 100, // Cannot be 0
                /* notification = */ notification,
                /* foregroundServiceType = */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                } else {
                    0
                },
            )
        } catch (e: Exception) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && e is ForegroundServiceStartNotAllowedException
            ) {
                // App not in a valid state to start foreground service
                // (e.g. started from bg)
            }
            // ...
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()
        serviceScope.launch {
            intent.extras?.getString(nameExtra.nameBDonDevice.value)?.let { nameBDonDevice ->
                intent.extras?.getString(nameExtra.nameNewBDonGoogle.value)?.let { nameNewBDonGoogle ->
                    ktorGOA.uploadFileResumable(nameBDonDevice, nameNewBDonGoogle, {
                        Log.d("MyTag", "ktorGOA.uploadFileResumable Log = ${it}")
                    }){
                        Log.d("MyTag", "Progress = $it")
                    }
                }
            }
            stopSelf(startId)
        }

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
        serviceJob.cancel()
    }
}