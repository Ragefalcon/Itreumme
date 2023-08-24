package ru.ragefalcon.tutatores.adapter.deprecated

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.ragefalcon.sharedcode.models.data.Id_class
import android.animation.ObjectAnimator

import android.animation.AnimatorSet
import android.content.Context
import android.widget.ProgressBar
import androidx.annotation.NonNull
import androidx.navigation.fragment.FragmentNavigator
import androidx.viewbinding.ViewBinding

class RVMainAdapter<T: Id_class,B: ViewBinding, K: RVMainAdapterViewHolder<T, B>>(val resource: Int, val vh: (LayoutInflater, ViewGroup, Boolean)->K, val listener: ((T) -> Unit)?, val getProgBar: ((ProgressBar)->Unit)? = null, val tranfun: ( (FragmentNavigator.Extras, View)-> Unit)? = null) ://val adaptViewHolder: RVMainAdapterViewHolder<T>
    RecyclerView.Adapter<K>() {

/**
 * Вроде как RecyclerView.Adapter можно прикрепить к нескольким RecyclerView, тогда эта
 * переменная будет перезаписываться при каждом прикреплении адаптера и в этом случае ее
 * не следует использовать вообще
 * */
    var mRecyclerView: RecyclerView? = null
    lateinit var context: Context
    var DURATION: Long = 500
    private var on_attach = true

    var selectedItem = 0
    private set
    private var selectedItemSver = false
    private var useSelSverPerem = false
    private var unselectedItemSver = false
    private var useUnselSverPerem = false

    var items: List<T> = listOf()
    fun getSelectedItem(): T?{
        return if (selectedItem < items.count()) items[selectedItem] else null
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
//                Log.d(TAG, "onScrollStateChanged: Called $newState")
                on_attach = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): K {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        return  vh(inflater,
            parent,
            false
        )//adaptViewHolder
    }

    override fun getItemCount(): Int = items.size


    fun setSelection(position: Int, svernut: Boolean){
        if (selectedItem != position) {  /**т.е. клик по не выделенному айтему*/
            useSelSverPerem = true
            useUnselSverPerem = true
            unselectedItemSver = selectedItemSver
            selectedItemSver = svernut
            notifyItemChanged(selectedItem)
            selectedItem = position
            notifyItemChanged(selectedItem)
        }   else    {
            selectedItemSver = svernut
        }
    }
    private fun setAnimation(itemView: View, i: Int) {
        var i = i
        if (!on_attach) {
            i = -1
        }
        val isNotFirstItem = i == -1
        i++
        itemView.alpha = 0f
        val animatorSet = AnimatorSet()
        val animator = ObjectAnimator.ofFloat(itemView, "alpha", 0f, 0.5f, 1.0f)
        ObjectAnimator.ofFloat(itemView, "alpha", 0f).start()
//        animator.setStartDelay(if (isNotFirstItem) (DURATION / 2).toLong() else (i * DURATION / 3).toLong())
        animator.duration = 500
        animatorSet.play(animator)
        animator.start()
    }
    private var lastPosition = -1
    override fun onBindViewHolder(holder: K, position: Int) {
        var sver = false
        if (selectedItem == position) {
            if (useSelSverPerem) {
                sver = selectedItemSver
                useSelSverPerem = false
            }
        }else   {
            if (useUnselSverPerem) {
                sver = unselectedItemSver
                useUnselSverPerem = false
            }
        }

        holder.itemView.setSelected(selectedItem == position)
        holder.bind(items[position], position,::setSelection, sver,mRecyclerView, listener,getProgBar,tranfun)
    }

    fun setSelection(item: T){//-},  predicate: (T)->Boolean){
        /**
         * кажется сейчас механика со сворачиванием выделением(когда только выделенный айтем развернут,
         * а при повторном клике он сворачивается) нигде не используетя, нужно подумать о том
         * чтобы или удалить ее совсем или продумать детали, потому что параметр svernut должен передаваться
         * из выделяемой вьюшки, но если это делается программно, то я пока не очень понимаю как достать этот
         * параметр
         * */
//        Log.d("MyTut", "itemSetSel: $item");
        items.indexOf(items.find{
            it.id_main == item.id_main
        }).let {//(predicate)
            Log.d("MyTut", "itemIndex: $it");
            if (it!=-1) setSelection(it,true)
        }

    }


    fun updateData(data: List<T>, ff: ()->Unit = {}) {
//        Log.d(
//            "M_rvmain", "update data adapter - new data ${data.size} hash : ${data.hashCode()}" +
//                    "old data ${items.size} hash : ${items.hashCode()}"
//        )

        val diffCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean = items[oldPos].id_main == data[newPos].id_main

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].hashCode() == data[newPos].hashCode()

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size


        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = data
        diffResult.dispatchUpdatesTo(this)
        ff()
    }

}

@Deprecated("Использовать RVItem и UniRVadapter")
abstract class RVMainAdapterViewHolder<T: Id_class,K: ViewBinding>(val binding: K) : RecyclerView.ViewHolder(binding.root) { //,ItemTouchViewHolder

    var mRecyclerView: RecyclerView? = null

    var svernut = false
    var pos: Int = 0
    var selFun: ((Int,Boolean) -> Unit) = { aa,bb ->

    }
    var funFromParentRV:  ((T) -> Unit)? = null
    var funGetProgBar: ((ProgressBar)->Unit)? = null
    var funForTransition: ((FragmentNavigator.Extras,View)-> Unit)? = null


    abstract fun onItemSelected()
//        {
//            itemView.setBackgroundColor(Color.LTGRAY)
//        }

    abstract fun onItemCleared()
//        {
//            itemView.setBackgroundColor(Color.WHITE)
//        }

    abstract fun adaptF(item: T)

    /* override*/

    //      abstract fun bind(item: T, listener: (T) -> Unit)
    fun bind(item: T, position: Int, selFunc: ((Int,Boolean) -> Unit), sver: Boolean = false, mRV: RecyclerView? = null, listener: ((T) -> Unit)?, getProgBar: ((ProgressBar)->Unit)? = null, tranfun: ((FragmentNavigator.Extras,View)-> Unit)? = null) {
        mRecyclerView = mRV
        svernut = sver
        pos = position
        selFun = selFunc


        funFromParentRV = listener
        funGetProgBar = getProgBar
        funForTransition = tranfun

        adaptF(item)
//        listener?.let {
//            itemView.setOnClickListener {
//                listener.invoke(item)
//            }
//
//        }
    }
}
