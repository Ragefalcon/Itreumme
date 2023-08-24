package ru.ragefalcon.tutatores.commonfragments

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.SimpleItemAnimator
import kotlinx.android.parcel.Parcelize
import ru.ragefalcon.sharedcode.models.data.Id_class
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.MyPopupDeleteRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.MyPopupRVItem
import ru.ragefalcon.tutatores.databinding.FragmentSpisEffektBinding
import ru.ragefalcon.tutatores.extensions.getSFM
import ru.ragefalcon.tutatores.extensions.setSFMResultListener
import ru.ragefalcon.tutatores.extensions.showMyFragDial


@Parcelize
class MyPopupItem(
    val resId: Int,
    val name: String,
    val podkey: String
) : Id_class(resId.toString()), Parcelable

@Parcelize
class MyPopupNabor(
    val list: List<MyPopupItem>
) : Parcelable


/**
 * Класс MyPopupMenuItem должен быть создан в корне onViewCreated
 * или в любом месте где логично задавать setFragmentResultListener,
 * а не по клику, например, на item-е списка, т.к. в этом случае
 * при повороте экрана или другом пересоздании экрана не пересоздадутся
 * слушатели для этого меню.
 * */
class MyPopupMenuItem<T:Id_class>(private val fragment: Fragment, val callbackKey: String){
    private val menuPopupPlan = mutableListOf<MenuPopupButton>()
    fun getListMenu() = menuPopupPlan.toList()
    fun addButton(butt: MenuPopupButton,listener: ((item: T) -> Unit)) {
        FragMyPopupMenuDial.addRezListener<T>(menuPopupPlan, butt, fragment, callbackKey) {
            listener(it)
        }
    }
    fun showMenu(
                 item:T,
                 name: String? = null,
                 predicate: (MenuPopupButton)->Boolean = {true},
                 manager: FragmentManager = fragment.getSFM(),
                 tag: String = "tegMyFragDial",
                 bound: MyFragDial.BoundSlide = MyFragDial.BoundSlide.right
    ){
        fragment.showMyFragDial(FragMyPopupMenuDial( item, name = name, this.getListMenu().filter { predicate(it) },callbackKey),manager,tag,bound)
    }
}

enum class MenuPopupButton(val butt: MyPopupItem) {
    DELETE(
        MyPopupItem(
            R.drawable.ic_delete,
            "Удалить",
            "delete"
        )
    ),
    CHANGE(
        MyPopupItem(
            R.drawable.ic_change,
            "Изменить",
            "change"
        )
    ),
    UNOPEN(
        MyPopupItem(
            R.drawable.ic_baseline_archive_24,
            "Закрыть",
            "unopen"
        )
    ),
    OPEN(
        MyPopupItem(
            R.drawable.ic_baseline_unarchive_24,
            "Открыть",
            "open"
        )
    ),
    LOAD(
        MyPopupItem(
            R.drawable.ic_baseline_cloud_download_24,
            "Загрузить",
            "load"
        )
    ),
    OVERWRITE(
        MyPopupItem(
            R.drawable.ic_baseline_published_with_changes_24,
            "Перезаписать",
            "overwrite"
        )
    ),
    UNEXECUTE(
        MyPopupItem(
            R.drawable.ic_round_check_circle_outline_24,
            "Развыполнить",
            "unexecute"
        )
    ),
    EXECUTE(
        MyPopupItem(
            R.drawable.ic_round_check_circle_24,
            "Выполнить",
            "execute"
        )
    );
}

class FragMyPopupMenuDial<T : Id_class>(
    itemPop: T? = null,
    name: String? = null,
    nabor: List<MenuPopupButton>? = null,
    callbackKey: String? = null
) :
    MyFragmentForDialogVM<FragmentSpisEffektBinding>(FragmentSpisEffektBinding::inflate) {

    var name: String by instanceStateDef("",name)
    var itemPop: T? by instanceState(itemPop)
    var nabor: MyPopupNabor? by instanceState(nabor?.let { MyPopupNabor(it.map { it.butt }) })
    var callbackKey: String? by instanceState(callbackKey)

    private var rvmAdapter = UniRVAdapter()


    private fun setRezList(type: String) {
        itemPop?.let { item ->
            callbackKey?.let { key ->
                getSFM().setFragmentResult(
                    "${key}_${type}",
                    bundleOf(
                        "item" to item
                    )
                )
                dismissDial()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            with(rvEffektList) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }
            textName.text = name

            val aa = rvEffektList.layoutParams as ViewGroup.MarginLayoutParams //as ConstraintLayout.LayoutParams
            aa.width = ViewGroup.MarginLayoutParams.WRAP_CONTENT
            aa.height = ViewGroup.MarginLayoutParams.WRAP_CONTENT
            rvEffektList.requestLayout()
            val bb = clAddTpanel.layoutParams as ViewGroup.MarginLayoutParams //as ConstraintLayout.LayoutParams
            bb.width = ViewGroup.MarginLayoutParams.WRAP_CONTENT
            bb.height = ViewGroup.MarginLayoutParams.WRAP_CONTENT
            rvEffektList.requestLayout()

            with(viewmodel) {
                nabor?.list?.let { items ->
                    rvmAdapter.updateData(formUniRVItemList(items) {
                        if (it.podkey == "delete")
                            MyPopupDeleteRVItem(it) { podkey ->
                                setRezList(podkey)
                            }
                            else
                        MyPopupRVItem(it) { podkey ->
                            setRezList(podkey)
                        }
                    })
                }
            }
            /**
             * строчка ниже нужна, чтобы не происходило мерцания при обновлении Item-а,
             * она отключает анимацию по умолчанию при вызове метода onBind (если я не путаю)
             * */
            (rvEffektList?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            buttCancelTpanel.setOnClickListener {
                dismissDial()
            }
        }
    }

    companion object {
        fun <T : Id_class> addRezListener(
            list: MutableList<MenuPopupButton>,
            itemMenu: MenuPopupButton,
            fragment: Fragment,
            requestKey: String,
            listener: ((item: T) -> Unit)? = null
        ) {
            list.add(itemMenu)
            fragment.setSFMResultListener("${requestKey}_${itemMenu.butt.podkey}") { key, bundle ->
                val itemRez = bundle.getParcelable<T>("item")
                itemRez?.let {
                    listener?.invoke(it)
                }
            }
        }
    }

}