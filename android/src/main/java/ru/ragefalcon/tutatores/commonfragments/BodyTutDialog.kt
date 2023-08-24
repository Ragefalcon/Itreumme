package ru.ragefalcon.tutatores.commonfragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.ragefalcon.sharedcode.models.data.ItemBodyDialog
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.bodydialogitem.BDButtonRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.bodydialogitem.BDDateRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.bodydialogitem.BDTextRVItem
import ru.ragefalcon.tutatores.databinding.FragmentTutDialogBinding

class BodyTutDialog(val frag: MyFragmentForDialogVM<FragmentTutDialogBinding>) {
    private val items: MutableLiveData<MutableList<UniRVItem>> = MutableLiveData(mutableListOf())
    fun getDialBody():List<UniRVItem> = items.value!!
    var count = 0
        private set
    var countLD: MutableLiveData<Int> = MutableLiveData<Int>(0)

    var date: Long = 0

    fun observe(funobs: (List<UniRVItem>)->Unit){
        Log.d("MyTut", "BodyTutDialog: observe start");
        countLD.observe(frag.viewLifecycleOwner){
            Log.d("MyTut", "BodyTutDialog: observe in work");
            funobs(items.value!!)
        }
    }

    fun clear(){
        count = 0
        items.value?.clear()
        countLD.value = count
    }

    fun addText(text: String){
        count++
        items.value?.add(UniRVItem(BDTextRVItem(ItemBodyDialog(count.toString(),text))))
        countLD.value = count
    }
    fun addButton(text: String, listener: ()->Unit){
        count++
        items.value?.add(UniRVItem(BDButtonRVItem(ItemBodyDialog(count.toString(),text),{listener.invoke()})))
        countLD.value = count
    }
    fun addDateEdit(text: String, listener: (Long)->Unit){
//        count++
        items.value?.add(UniRVItem(BDDateRVItem(ItemBodyDialog(count.toString(),text), frag.viewLifecycleOwner, {listener.invoke(it)})))
        countLD.value = count
    }
}