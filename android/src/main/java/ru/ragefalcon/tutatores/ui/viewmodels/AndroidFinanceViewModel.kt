package ru.ragefalcon.tutatores.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import ru.ragefalcon.sharedcode.source.disk.DbArgs
import ru.ragefalcon.sharedcode.viewmodels.ObserveModels.ObserveBaseViewModel
import java.util.*

class AndroidFinanceViewModel(application: Application): AndroidViewModel(application) {
    private var ObserFM = ObserveBaseViewModel(DbArgs(getApplication(),"databasefff.db"))

    fun addDateOwner(owner: LifecycleOwner){

        dateOpor.observe(owner) {
            selPer.setDateOpor(it.time)
        }
        dateBegin.observe(owner) {
            selPer.SetPeriodDates(it.time,dateEnd.value!!.time)
        }
        dateEnd.observe(owner) {
            selPer.SetPeriodDates(dateBegin.value!!.time,it.time)
        }
        dateOporDp.observe(owner){
            if (keyDateChange){
                keyDateChange = false
            }   else {
                timeFun.setDay(it.time)
            }
        }
    }

    /**********************************************************/
    /**********************************************************/
    /****-------------------  Финансы  --------------------****/
    /**********************************************************/
    /**********************************************************/

    val tt = Calendar.getInstance().timeInMillis

    /** Объект отвечающий за добавление финансовых операций */
    val addFin = ObserFM.addFin
    val finSpis = ObserFM.financeSpis
    val financeFun = ObserFM.financeFun

    /** Объект отвечающий за работу с выбором временных периодов для отображения списков и графиков по финансам */
    val selPer = ObserFM.selPer

    val dateOpor = MutableLiveData<Date>().apply {
        this.value = Date(selPer.dateOporLong)

        selPer.setUpdDO { dd->
            this.value = Date(dd)
        }
    }

    val dateBegin = MutableLiveData<Date>()
    val dateEnd = MutableLiveData<Date>().apply {
        ObserFM.selPer.setUpdPer { aa,bb->
            if (dateBegin.value != Date(aa)) dateBegin.value = Date(aa)
            if (this.value != Date(bb)) this.value = Date(bb)
        }
    }

    /**********************************************************/
    /**********************************************************/
    /****-------------------  Журнал  ---------------------****/
    /**********************************************************/
    /**********************************************************/

    val journalSpis = ObserFM.journalSpis
    val addJournal = ObserFM.addJournal
    val journalFun = ObserFM.journalFun

    /**********************************************************/
    /**********************************************************/
    /****-------------------  Время  ----------------------****/
    /**********************************************************/
    /**********************************************************/

    /** Объект отвечающий за добавление операций ежедневника */
    val addTime = ObserFM.addTime
    val timeFun = ObserFM.timeFun
    val timeSpis = ObserFM.timeSpis


    var keyDateChange = false
    val dateOporDp = MutableLiveData<Date>().apply {
        this.value = Date(ObserFM.timeFun.getDay())

        ObserFM.timeFun.setFunDateOporTimeUpd{ dd->
            keyDateChange = true
            this.value = Date(dd)
        }
    }

    /**********************************************************/
    /**********************************************************/
    /****-------------------  Аватар  ---------------------****/
    /**********************************************************/
    /**********************************************************/

    /** Объект отвечающий за добавление операций аватара */
    val addAvatar = ObserFM.addAvatar

    val avatarSpis = ObserFM.avatarSpis
    val avatarFun = ObserFM.avatarFun
}