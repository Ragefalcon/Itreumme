package ru.ragefalcon.tutatores.di

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.ragefalcon.sharedcode.myGoogleLib.ItemKtorGoogleParams
import ru.ragefalcon.sharedcode.myGoogleLib.KtorGoogleOAuth
import ru.ragefalcon.sharedcode.source.disk.DbArgs
import ru.ragefalcon.tutatores.services.UploadFileBD
import ru.ragefalcon.tutatores.ui.finance.RasxodFragment
import ru.ragefalcon.tutatores.ui.settings.SettingsSincFragment
import javax.inject.Inject
import javax.inject.Singleton

@Module
class KtorModule(private val application: Application) {


    @get:Provides
    @Singleton
    val KtorGoogleParams:ItemKtorGoogleParams = ItemKtorGoogleParams()

    @Provides
    fun ktorGOA(params: ItemKtorGoogleParams):KtorGoogleOAuth = KtorGoogleOAuth(DbArgs(application), params) { } //params.value = it

}

@Component(modules = arrayOf(KtorModule::class))
@Singleton
interface AppComponent{
    fun inject(application: Application)
    fun inject(serviceUploadFileBD: UploadFileBD)
    fun inject(fragmentSettingsSincFragment: SettingsSincFragment)
    fun inject(fragmentRasxodFragment: RasxodFragment)
    fun inject(androidViewModel: AndroidViewModel)
}