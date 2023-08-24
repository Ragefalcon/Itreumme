package ru.ragefalcon.tutatores.ui.finance

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemDoxod
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.TypeRasxodAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.DoxodRVItem
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.commonfragments.MenuPopupButton
import ru.ragefalcon.tutatores.commonfragments.MyPopupMenuItem
import ru.ragefalcon.tutatores.databinding.FragmentFinanceBinding
import ru.ragefalcon.tutatores.extensions.pxF
import ru.ragefalcon.tutatores.extensions.setOnItemSelectedListener
import ru.ragefalcon.tutatores.extensions.showAddChangeFragDial
import ru.ragefalcon.tutatores.extensions.showMyMessage

class DoxodFragment() : BaseFragmentVM<FragmentFinanceBinding>(FragmentFinanceBinding::inflate) {

    private var rvmAdapter = UniRVAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val menuPopupDoxod = MyPopupMenuItem<ItemDoxod>(this@DoxodFragment, "DoxodDelChange").apply {
                addButton(MenuPopupButton.DELETE) {
                    viewmodel.addFin.delDoxod(it)
                }
                addButton(MenuPopupButton.CHANGE) { itemD ->
                    viewmodel.finSpis.spisTypeDox.getLiveData().value?.find { it.first == itemD.typeid }?.let {
                        showAddChangeFragDial(AddChangeDoxodPanFragment(item = itemD))
                    } ?: run {
                        showMyMessage("Используемый тип дохода уже закрыт, если хотите внести изменения вначале откройте его снова.")
                    }

                }
            }
            tvFinPeriod.setTextColor(Color.WHITE)//resources.getColor(razdelName.colorText))
            with(rvFinanceList) {
                adapter = rvmAdapter
                layoutManager = LinearLayoutManager(context)
            }
            stateViewModel.visFilterFinPanel.observe(viewLifecycleOwner) {
                if (it) {
                    srFilter.visibility = View.VISIBLE
                    srFilter.layoutParams.height = 40.pxF.toInt()
                    /** requestLayout() Нужно для обновления */
                    srFilter.requestLayout()
                } else {
                    srFilter.visibility = View.INVISIBLE
                    srFilter.layoutParams.height = 0
                    /** requestLayout() Нужно для обновления */
                    srFilter.requestLayout()
                }
            }
            srSchet.layoutParams.height = 0
            with(viewmodel) {
                finSpis.spisTypeDox.observe(viewLifecycleOwner) {
                    if (it.isNotEmpty()) {
                        srFilter.adapter = TypeRasxodAdapter(requireContext(), it, R.layout.schet_item)
                        val k = (srFilter.adapter as TypeRasxodAdapter).getPosition(financeFun.getPosMainSchet())
                        srFilter.setSelection(k)
                        srFilter.refreshDrawableState()
                    }
                }
                finSpis.doxodSpisPeriod.observe(viewLifecycleOwner) {
                    rvmAdapter.updateData(formUniRVItemList(it) { item ->
                        DoxodRVItem(item, longTapListener = { itemLT ->
                            finSpis.spisSchet.getLiveData().value?.find { it.id == itemLT.schetid }?.let {
                                menuPopupDoxod.showMenu(itemLT, name = "${itemLT.name} (${itemLT.summa.roundToStringProb(1)})")
                            } ?: run {
                                showMyMessage("Используемый счет уже закрыт, если хотите внести изменения вначале откройте его снова.")
                            }
                        })
                    })
                }
                finSpis.doxodSummaPeriod.observe(viewLifecycleOwner) {
                    tvFinPeriod.text = it
                }

                srFilter.setOnItemSelectedListener { adapterView, view, i, l ->
                    financeFun.setPosFilterDox(srFilter.selectedItem as Pair<String, String>)
                }
            }
        }
    }
}
