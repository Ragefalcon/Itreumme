package ru.ragefalcon.tutatores.commonfragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import ru.ragefalcon.sharedcode.models.data.ItemBodyDialog
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.bodydialogitem.BDButtonRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.bodydialogitem.BDDateRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.bodydialogitem.BDTextRVItem
import ru.ragefalcon.tutatores.databinding.FragmentTutDialogBinding
import java.lang.ref.WeakReference

class BodyTutDialog(val frag: WeakReference<MyFragmentForDialogVM<FragmentTutDialogBinding>>) {
    private val items: MutableLiveData<MutableList<UniRVItem>> = MutableLiveData(mutableListOf())
    fun getDialBody(): List<UniRVItem> = items.value!!
    var count = 0
        private set
    var countLD: MutableLiveData<Int> = MutableLiveData<Int>(0)

    var date: Long = 0

    fun observe(funobs: (List<UniRVItem>) -> Unit) {
        frag.get()?.viewLifecycleOwner?.let {
            countLD.observe(it) {
                funobs(items.value!!)
            }
        }
    }

    fun clear() {
        count = 0
        items.value?.clear()
        countLD.value = count
    }

    fun addText(text: String) {
        count++
        items.value?.add(UniRVItem(BDTextRVItem(ItemBodyDialog(count.toString(), text))))
        countLD.value = count
    }

    fun addButton(text: String, listener: () -> Unit) {
        count++
        items.value?.add(UniRVItem(BDButtonRVItem(ItemBodyDialog(count.toString(), text), { listener.invoke() })))
        countLD.value = count
    }

    fun addDateEdit(text: String, listener: (Long) -> Unit) {
//        count++
        frag.get()?.viewLifecycleOwner?.let { fr ->
            items.value?.add(
                UniRVItem(
                    BDDateRVItem(
                        ItemBodyDialog(count.toString(), text),
                        fr,
                        { listener.invoke(it) })
                )
            )
        }
        countLD.value = count
    }
}