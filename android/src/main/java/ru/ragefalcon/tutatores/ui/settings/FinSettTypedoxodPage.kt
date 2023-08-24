package ru.ragefalcon.tutatores.ui.settings

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.SimpleItemAnimator
import ru.ragefalcon.sharedcode.models.data.ItemSettTypedoxod
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.SettSchetRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.SettTypedoxodRVItem
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentTabFinSettBinding
import ru.ragefalcon.tutatores.extensions.showAddChangeFragDial

class FinSettTypedoxodPage() : BaseFragmentVM<FragmentTabFinSettBinding>(FragmentTabFinSettBinding::inflate) {

    private var rvmAdapter = UniRVAdapter()
    private var selItem: ItemSettTypedoxod? by instanceState()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            with(rvFinSettList) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }
            with(viewmodel) {
                val menuPopupSettTypedoxod = MyPopupMenuItem<ItemSettTypedoxod>(this@FinSettTypedoxodPage, "TypedoxodDelChange").apply {
                    addButton(MenuPopupButton.UNOPEN) {
                        addFin.updTypedoxodOpen(id = it.id.toLong(), false)
                    }
                    addButton(MenuPopupButton.OPEN) {
                        addFin.updTypedoxodOpen(id = it.id.toLong(), true)
                    }
                    addButton(MenuPopupButton.DELETE) {
                        addFin.delTypedoxod(it.id.toLong())
                    }
                    addButton(MenuPopupButton.CHANGE) {
                        showAddChangeFragDial(SettingAddSettTypedoxodPanelFragment(it))
                    }
                }
                finSpis.spisTypedoxodForSett.observe(viewLifecycleOwner) { listItem ->
                    rvmAdapter.updateData(formUniRVItemList(listItem) { item ->
                        SettTypedoxodRVItem(item, selectListener = {
                            selItem = it
                        },longTapListener = {
                            menuPopupSettTypedoxod.showMenu(item,name = "${item.typed}",{
                                if (item.countoper == 0L) (it == MenuPopupButton.DELETE)||(it == MenuPopupButton.CHANGE)
                                else if (item.open) (it == MenuPopupButton.CHANGE)||(it == MenuPopupButton.UNOPEN)
                                else (it == MenuPopupButton.CHANGE)||(it == MenuPopupButton.OPEN) })
                        })
                    })
                    selItem?.let {
                        rvmAdapter.setSelectItem(it, SettTypedoxodRVItem::class) //DenPlanViewHolder
                    }
                }
                buttToggleOpen.setOnCheckedChangeListener { buttonView, isChecked ->
                    financeFun.setVisibleOpenSettTypedoxod(!isChecked)
                }
                buttAdd.setOnClickListener {
                    showAddChangeFragDial(SettingAddSettTypedoxodPanelFragment())
                }
            }
            (rvFinSettList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }
}