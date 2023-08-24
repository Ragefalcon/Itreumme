package ru.ragefalcon.tutatores.adapter.unirvadapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.ragefalcon.sharedcode.models.data.Id_class
import kotlin.reflect.KClass

/**
 * Вдохновлено вот этим:
 * https://habr.com/ru/company/google/blog/345172/
 * с исходниками здесь:
 * https://github.com/MaksTuev/EasyAdapter
 * **/
class UniRVAdapter : RecyclerView.Adapter<UniRVMainAdapterViewHolder>() {

    private val items: MutableList<UniRVItem> = mutableListOf()
    private val supportedItemTypes: SparseArray<(LayoutInflater, ViewGroup, Boolean) -> ViewBinding> =
        SparseArray<(LayoutInflater, ViewGroup, Boolean) -> ViewBinding>()

    fun getItems() = items.toList()

    private var on_attach = true

    var selectedItem: MutableLiveData<UniRVItem?> = MutableLiveData<UniRVItem?>().apply { this.value = null }
        private set


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                on_attach = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniRVMainAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UniRVMainAdapterViewHolder(supportedItemTypes.get(viewType).invoke(inflater, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].getType()
    }

    override fun getItemCount(): Int = items.size

    fun getItemPosition(item: UniRVItem) = items.indexOfFirst {
        ((it.getId() == item.getId()) && (it.getType() == item.getType()))
    }

    fun <K : Id_class, T : BaseUniRVItem<K>> getItem(date: K, type: KClass<T>): UniRVItem? {
        return items.find { findItem ->
            sravItemIdType(findItem, date.id_main, type)
        }
    }

    fun <K : Id_class, T : BaseUniRVItem<K>> setSelectItem(date: K, type: KClass<T>) {
        val item = items.find { findItem ->
            sravItemIdType(findItem, date.id_main, type)
        }
        setSelect(item)
    }

    fun <K : Id_class, T : BaseUniRVItem<K>> removeInsertItem(date: K, type: KClass<T>) {
        val pos = items.indexOfFirst { findItem ->
            sravItemIdType(findItem, date.id_main, type)
        }
        notifyItemRemoved(pos)
        notifyItemInserted(pos)
    }


    fun areItemsTheSame(first: UniRVItem?, second: UniRVItem): Boolean =
        first?.let { ((it.getId() == second.getId()) && (it.getType() == second.getType())) } ?: false

    fun setSelect(item: UniRVItem?) {
        item?.let {
            if (!areItemsTheSame(selectedItem.value, item)) {
                selectedItem.value?.let { notifyItemChanged(getItemPosition(it)) }
                selectedItem.value = item
                selectedItem.value?.let { notifyItemChanged(getItemPosition(it)) }
            }
        }
    }

    override fun onBindViewHolder(holder: UniRVMainAdapterViewHolder, position: Int) {
        holder.itemView.isSelected = areItemsTheSame(selectedItem.value, items[position])
        holder.bind(items[position], UniRVComboSet(position, ::setSelect))
    }


    private fun updateSupportedItemControllers(items: List<UniRVItem>) {
        supportedItemTypes.clear()
        for (item in items) {
            supportedItemTypes.put(item.getType(), item.getBinding())
        }
    }

    fun updateData(data: List<UniRVItem>) {
        updateSupportedItemControllers(data)

        val diffCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
                ((items[oldPos].getId() == data[newPos].getId()) && (items[oldPos].getType() == data[newPos].getType()))

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].getData().hashCode() == data[newPos].getData().hashCode()

            override fun getOldListSize() = items.size

            override fun getNewListSize() = data.size


        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items.clear()
        items.addAll(data)
        diffResult.dispatchUpdatesTo(this)

        if (selectedItem.value == null) {
            if (itemCount > 0) selectedItem.value = items[0]
        } else {
            selectedItem.value?.let {
                if (getItemPosition(it) == -1) {
                    if (itemCount > 0) selectedItem.value = items[0] else selectedItem.value = null
                }
            }
        }
    }

}