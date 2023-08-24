package ru.ragefalcon.tutatores.ui.settings

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.SimpleItemAnimator
import ru.ragefalcon.sharedcode.models.data.ItemValut
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.SettValutRVItem
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentTabFinSettBinding
import ru.ragefalcon.tutatores.extensions.showAddChangeFragDial

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
                val menuPopupValut = MyPopupMenuItem<ItemValut>(this@FinSettValutPage, "ValutDelChange").apply {
//                    addButton(MenuPopupButton.UNOPEN) {
//                        addFin.updTyperasxodOpen(id = it.id.toLong(), false)
//                    }
//                    addButton(MenuPopupButton.OPEN) {
//                        addFin.updTyperasxodOpen(id = it.id.toLong(), true)
//                    }
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
//                                else if (item.open) (it == MenuPopupButton.CHANGE)||(it == MenuPopupButton.UNOPEN)
//                                else (it == MenuPopupButton.CHANGE)||(it == MenuPopupButton.OPEN) })
                                else (it == MenuPopupButton.CHANGE) })
                        })
                    })
                    selItem?.let {
                        rvmAdapter.setSelectItem(it, SettValutRVItem::class) //DenPlanViewHolder
                    }
                }
                tvCountPlanLabel.visibility = View.INVISIBLE
                buttToggleOpen.visibility = View.INVISIBLE
//                buttToggleOpen.setOnCheckedChangeListener { buttonView, isChecked ->
//                    financeFun.setVisibleOpenValut(!isChecked)
//                }
                buttAdd.setOnClickListener {
                    showAddChangeFragDial(SettingAddValutPanelFragment())
                }
            }
            (rvFinSettList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }
}