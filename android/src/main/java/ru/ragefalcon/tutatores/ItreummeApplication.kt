package ru.ragefalcon.tutatores

import android.app.Application
import dagger.internal.DaggerCollections
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
//        if (isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return
//        }
        initializeDagger()
        install()
        // Normal app init code...
//        val myComponent = DaggerKtor Collections KtorModule.builder()
//            .myModule(MyModule(application)) // Передайте объект Application в модуль
//            .build()
    }

    private fun initializeDagger() {
        appComponent = DaggerAppComponent.builder().ktorModule(KtorModule(this))
            .build()
    }

}