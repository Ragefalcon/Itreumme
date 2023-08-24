package ru.ragefalcon.tutatores.adapter.unirvadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
//import kotlinx.android.extensions.LayoutContainer
import ru.ragefalcon.sharedcode.models.data.Id_class
import kotlin.reflect.KClass

fun <T : Id_class> getUniRVViewHolder(
    bindingClass: (LayoutInflater, ViewGroup, Boolean) -> ViewBinding,
    bindF: (UniRVMainAdapterViewHolder, T, UniRVComboSet) -> Unit
): UniRVViewHolder<T> {
    return object : UniRVViewHolder<T>(bindingClass) {
        override fun bind(
            vh: UniRVMainAdapterViewHolder,
            data: T,
            rvset: UniRVComboSet
        ) {
            bindF(vh, data, rvset)
        }
    }
}

abstract class UniRVViewHolder<T : Id_class>(
    private val bindingClass: (LayoutInflater, ViewGroup, Boolean) -> ViewBinding
) {
    abstract fun bind(
        vh: UniRVMainAdapterViewHolder,
        data: T,
        rvset: UniRVComboSet
    )

    fun getBinding() = bindingClass
}


fun <K : Id_class, T : BaseUniRVItem<K>> sravItemIdType(
    item: UniRVItem, id: String, type: KClass<T>
): Boolean {
    return (id == item.getData().id_main) && (item.getType() == getTypeUniRVItem(type))
}

fun <K : Id_class, T : BaseUniRVItem<K>> sravItemIdType(
    uniItem: UniRVItem,
    iditem: K,
    type: KClass<T>
): Boolean {
    return (iditem.id_main == uniItem.getData().id_main) && (uniItem.getType() == getTypeUniRVItem(type))
}

fun <T : BaseUniRVItem<*>> getTypeUniRVItem(type: KClass<T>): Int {
    return type.qualifiedName.hashCode()
}

open class BaseUniRVItem<T : Id_class>(
    private val data: T,
    private val holder: UniRVViewHolder<T>,
) {
    fun getData() = data

    fun getId() = data.id_main


    fun getBinding() = holder.getBinding()

    fun bind(
        vh: UniRVMainAdapterViewHolder,
        rvset: UniRVComboSet
    ) {
        holder.bind(vh, data, rvset)
    }

    fun getType(): Int {
        return this.javaClass.canonicalName?.hashCode() ?: -1
    }
}

class UniRVComboSet(
    val position: Int,
    val selFunc: ((UniRVItem) -> Unit)
)

class UniRVMainAdapterViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    var bindItem: UniRVItem? = null

    fun bind(
        item: UniRVItem,
        rvset: UniRVComboSet
    ) {
        bindItem = item
        item.bind(this, rvset)
    }

}

class UniRVItem(private val item: BaseUniRVItem<*>) {
    fun getData() = item.getData()

    fun <T : Id_class> getItem(t: KClass<T>): T? = if (t == getData()::class) getData() as T else null

    fun getId() = item.getId()

    fun getType() = item.getType()

    fun getBinding() = item.getBinding()

    fun bind(
        vh: UniRVMainAdapterViewHolder,
        rvset: UniRVComboSet
    ) {
        item.bind(vh, rvset)
    }
}

class UniRVItemList {
    private val items: MutableList<UniRVItem> = mutableListOf()

    fun getList() = items.toList()

    fun <T : Id_class, K : BaseUniRVItem<T>> setItems(collection: List<T>, transform: (T) -> K) {
        items.clear()
        items.addAll(collection.map {
            UniRVItem(transform(it))
        })
    }

}

fun <T : Id_class> formUniRVItemList(
    collection: List<T>,
    transform: (T) -> BaseUniRVItem<T>
): List<UniRVItem> =
    collection.map {
        UniRVItem(transform(it))
    }
