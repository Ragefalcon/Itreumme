package ru.ragefalcon.tutatores

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import leakcanary.LogcatSharkLog.Companion.install
import ru.ragefalcon.tutatores.di.AppComponent
import ru.ragefalcon.tutatores.di.DaggerAppComponent
import ru.ragefalcon.tutatores.di.KtorModule


class ItreummeApplication: Application() {

    companion object{
        lateinit var appComponent: AppComponent
    }
    override fun onCreate() {
        super.onCreate()
        initializeDagger()
        createNotificationChannel()
        install()
    }

    private fun initializeDagger() {
        appComponent = DaggerAppComponent.builder().ktorModule(KtorModule(this))
            .build()
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val CHANNEL_ID = getString(R.string.channel_google_drive_id)
            val name = getString(R.string.channel_google_drive_name)
            val description = getString(R.string.channel_google_drive_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}