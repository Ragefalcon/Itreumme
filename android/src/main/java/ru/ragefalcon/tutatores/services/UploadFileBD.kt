package ru.ragefalcon.tutatores.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
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