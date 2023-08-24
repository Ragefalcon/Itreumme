package ru.ragefalcon.tutatores.ui.settings;

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.SimpleItemAnimator
import ru.ragefalcon.sharedcode.models.data.ItemSettSchet
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.SettSchetRVItem
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentTabFinSettBinding
import ru.ragefalcon.tutatores.extensions.showAddChangeFragDial

class FinSettSchetPage() : BaseFragmentVM<FragmentTabFinSettBinding>(FragmentTabFinSettBinding::inflate) {

    private var rvmAdapter = UniRVAdapter()
    private var selItem: ItemSettSchet? by instanceState()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            with(rvFinSettList) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }
            with(viewmodel) {
                val menuPopupSettSchet = MyPopupMenuItem<ItemSettSchet>(this@FinSettSchetPage, "SchetDelChange").apply {
                    addButton(MenuPopupButton.UNOPEN) {
                        addFin.updSchetOpen(id = it.id.toLong(), false)
                    }
                    addButton(MenuPopupButton.OPEN) {
                        addFin.updSchetOpen(id = it.id.toLong(), true)
                    }
                    addButton(MenuPopupButton.DELETE) {
                         addFin.delSchet(it.id.toLong())
                    }
                    addButton(MenuPopupButton.CHANGE) {
                        showAddChangeFragDial(SettingAddSettSchetPanelFragment(it))
                    }
                }
                finSpis.spisSchetForSett.observe(viewLifecycleOwner) { listItem ->
                    rvmAdapter.updateData(formUniRVItemList(listItem) { item ->
                        SettSchetRVItem(item, selectListener = {
                            selItem = it
                        },longTapListener = {
                            menuPopupSettSchet.showMenu(item,name = "${item.name}",{
                                if (item.countoper == 0L) (it == MenuPopupButton.DELETE)||(it == MenuPopupButton.CHANGE)
                                    else if (item.summa != 0.0) (it == MenuPopupButton.CHANGE)
                                            else if (item.open_) (it == MenuPopupButton.CHANGE)||(it == MenuPopupButton.UNOPEN)
                                                    else (it == MenuPopupButton.CHANGE)||(it == MenuPopupButton.OPEN) })
                        })
                    })
                    selItem?.let {
                        rvmAdapter.setSelectItem(it, SettSchetRVItem::class) //DenPlanViewHolder
                    }
                }
                buttToggleOpen.setOnCheckedChangeListener { buttonView, isChecked ->
                    financeFun.setVisibleOpenSettSchet(!isChecked)
                }
                buttAdd.setOnClickListener {
                    showAddChangeFragDial(SettingAddSettSchetPanelFragment())
                }
            }
            (rvFinSettList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }
}