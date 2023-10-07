package ru.ragefalcon.tutatores.ui.settings

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.SimpleItemAnimator
import ru.ragefalcon.sharedcode.models.data.ItemValut
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.SettValutRVItem
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.commonfragments.MenuPopupButton
import ru.ragefalcon.tutatores.commonfragments.MyPopupMenuItem
import ru.ragefalcon.tutatores.databinding.FragmentTabFinSettBinding
import ru.ragefalcon.tutatores.extensions.showAddChangeFragDial
import java.lang.ref.WeakReference

class FinSettValutPage() : BaseFragmentVM<FragmentTabFinSettBinding>(FragmentTabFinSettBinding::inflate) {

    private var rvmAdapter = UniRVAdapter()
    private var selItem: ItemValut? by instanceState()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            with(rvFinSettList) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }
            with(viewmodel) {
                val menuPopupValut = MyPopupMenuItem<ItemValut>(WeakReference(this@FinSettValutPage), "ValutDelChange").apply {
                    addButton(MenuPopupButton.DELETE) {
                        addFin.delValut(it.id.toLong())
                    }
                    addButton(MenuPopupButton.CHANGE) {
                        showAddChangeFragDial(SettingAddValutPanelFragment(it))
                    }
                }
                finSpis.spisValut.observe(viewLifecycleOwner) { listItem ->
                    rvmAdapter.updateData(formUniRVItemList(listItem) { item ->
                        SettValutRVItem(item, selectListener = {
                            selItem = it
                        },longTapListener = {
                            menuPopupValut.showMenu(item,name = "${item.name}",{
                                if (item.countschet == 0L) (it == MenuPopupButton.DELETE)||(it == MenuPopupButton.CHANGE)
                                else (it == MenuPopupButton.CHANGE) })
                        })
                    })
                    selItem?.let {
                        rvmAdapter.setSelectItem(it, SettValutRVItem::class) //DenPlanViewHolder
                    }
                }
                tvCountPlanLabel.visibility = View.INVISIBLE
                buttToggleOpen.visibility = View.INVISIBLE
                buttAdd.setOnClickListener {
                    showAddChangeFragDial(SettingAddValutPanelFragment())
                }
            }
            (rvFinSettList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }
}