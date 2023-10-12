package ru.ragefalcon.tutatores.services

import android.Manifest
import android.app.ForegroundServiceStartNotAllowedException
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.EXTRA_NOTIFICATION_ID
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import ru.ragefalcon.sharedcode.myGoogleLib.KtorGoogleOAuth
import ru.ragefalcon.tutatores.ItreummeApplication
import ru.ragefalcon.tutatores.R
import java.lang.ref.WeakReference
import javax.inject.Inject

class UploadFileBD : Service() {

    enum class nameExtra(val value: String) {
        cancelUpload("cancelUpload"),
        nameBDonDevice("nameBDonDevice"),
        nameNewBDonGoogle("nameNewBDonGoogle");
    }

    private val binder = LocalBinder(WeakReference(this@UploadFileBD))

    private val _progressValue = MutableLiveData<Int>(0)
    val progressValue: LiveData<Int> = _progressValue

    class LocalBinder(private val serviceRef: WeakReference<UploadFileBD>) : Binder() {
        fun getService() = serviceRef
    }


    @Inject
    lateinit var ktorGOA: KtorGoogleOAuth

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Default + serviceJob)

    override fun onCreate() {
        ItreummeApplication.appComponent.inject(this)
    }

    private fun startForeground(notification: Notification) {

        val internetPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED
        val readStoragePermission =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        if (internetPermission || readStoragePermission) {
            stopSelf()
            return
        }

        try {
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
            intent.extras?.getBoolean(nameExtra.cancelUpload.value)?.let { cancelUpload ->
                Log.d("MyTag", "Cancel upload from action button")
                if (cancelUpload) stopSelf()
            }
            intent.extras?.getString(nameExtra.nameBDonDevice.value)?.let { nameBDonDevice ->
                intent.extras?.getString(nameExtra.nameNewBDonGoogle.value)?.let { nameNewBDonGoogle ->
                    val PROGRESS_MAX = 100
                    var PROGRESS_CURRENT = 0
                    val ACTION_SNOOZE = "snooze"

                    val cancelIntent = Intent(this@UploadFileBD, UploadFileBD::class.java).apply {
                        action = ACTION_SNOOZE
                        putExtra(EXTRA_NOTIFICATION_ID, 100)
                        putExtra(nameExtra.cancelUpload.value, true)
                    }
                    val cancelPendingIntent: PendingIntent =
                        PendingIntent.getService(
                            this@UploadFileBD, 0, cancelIntent,
                            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
                        )

                    val notificationBuilder =
                        NotificationCompat.Builder(this@UploadFileBD, getString(R.string.channel_google_drive_id))
                            .setSmallIcon(R.drawable.ic_my_logo_500)
                            /**
                             * Похоже, что Notification От службы по умолчанию идет расширяемый или проблема в чем то еще,
                             * но если использовать просто setContentTitle и setContentText, то они не отображаются.
                             * */
                            .setStyle(
                                NotificationCompat.BigTextStyle()
                                    .setBigContentTitle("БД -> Google Drive")
                                    .bigText("Идет процесс загрузки")
                            )
                            .setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
                            .addAction(R.drawable.ic_back, "Отменить", cancelPendingIntent)
                            .setOnlyAlertOnce(true)

                    startForeground(notificationBuilder.build())
                    ktorGOA.uploadFileResumable(nameBDonDevice, nameNewBDonGoogle, {
                        Log.d("MyTag", "ktorGOA.uploadFileResumable Log = ${it}")
                    }) {
                        this.launch(Dispatchers.Main) {
                            _progressValue.value = (it * 100).toInt()
                            if ((it * 100).toInt() - PROGRESS_CURRENT >= 10 || (it * 100).toInt() == PROGRESS_MAX) {
                                PROGRESS_CURRENT = (it * 100).toInt()
                                with(NotificationManagerCompat.from(this@UploadFileBD)) {
                                    notificationBuilder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false).let {
                                        if (ActivityCompat.checkSelfPermission(
                                                this@UploadFileBD,
                                                Manifest.permission.POST_NOTIFICATIONS
                                            ) != PackageManager.PERMISSION_GRANTED
                                        ) {
                                            // TODO: Consider calling
                                            //    ActivityCompat#requestPermissions
                                            // here to request the missing permissions, and then overriding
                                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                            //                                          int[] grantResults)
                                            // to handle the case where the user grants the permission. See the documentation
                                            // for ActivityCompat#requestPermissions for more details.
                                        } else {
                                            notify(100, it.build())
                                        }
                                    }
                                }
                            }
                        }
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
        return binder
    }

    override fun onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
        serviceJob.cancel()
    }
}