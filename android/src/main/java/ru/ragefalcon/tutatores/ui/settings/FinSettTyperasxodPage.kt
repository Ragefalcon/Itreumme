package ru.ragefalcon.tutatores.ui.settings

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.SimpleItemAnimator
import ru.ragefalcon.sharedcode.models.data.ItemSettTyperasxod
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.SettTyperasxodRVItem
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentTabFinSettBinding
import ru.ragefalcon.tutatores.extensions.showAddChangeFragDial

class FinSettTyperasxodPage() : BaseFragmentVM<FragmentTabFinSettBinding>(FragmentTabFinSettBinding::inflate) {

    private var rvmAdapter = UniRVAdapter()
    private var selItem: ItemSettTyperasxod? by instanceState()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            with(rvFinSettList) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }
            with(viewmodel) {
                val menuPopupSettTyperasxod = MyPopupMenuItem<ItemSettTyperasxod>(this@FinSettTyperasxodPage, "TyperasxodDelChange").apply {
                    addButton(MenuPopupButton.UNOPEN) {
                        addFin.updTyperasxodOpen(id = it.id.toLong(), false)
                    }
                    addButton(MenuPopupButton.OPEN) {
                        addFin.updTyperasxodOpen(id = it.id.toLong(), true)
                    }
                    addButton(MenuPopupButton.DELETE) {
                        addFin.delTyperasxod(it.id.toLong())
                    }
                    addButton(MenuPopupButton.CHANGE) {
                        showAddChangeFragDial(SettingAddSettTyperasxodPanelFragment(it))
                    }
                }
                finSpis.spisTyperasxodForSett.observe(viewLifecycleOwner) { listItem ->
                    rvmAdapter.updateData(formUniRVItemList(listItem) { item ->
                        SettTyperasxodRVItem(item, selectListener = {
                            selItem = it
                        },longTapListener = {
                            menuPopupSettTyperasxod.showMenu(item,name = "${item.typer}",{
                                if (item.countoper == 0L) (it == MenuPopupButton.DELETE)||(it == MenuPopupButton.CHANGE)
                                else if (item.open) (it == MenuPopupButton.CHANGE)||(it == MenuPopupButton.UNOPEN)
                                else (it == MenuPopupButton.CHANGE)||(it == MenuPopupButton.OPEN) })
                        })
                    })
                    selItem?.let {
                        rvmAdapter.setSelectItem(it, SettTyperasxodRVItem::class) //DenPlanViewHolder
                    }
                }
                buttToggleOpen.setOnCheckedChangeListener { buttonView, isChecked ->
                    financeFun.setVisibleOpenSettTyperasxod(!isChecked)
                }
                buttAdd.setOnClickListener {
                    showAddChangeFragDial(SettingAddSettTyperasxodPanelFragment())
                }
            }
            (rvFinSettList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }
}