package ru.ragefalcon.tutatores.ui.finance

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemCommonFinOper
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.TypeRasxodAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.SchetRVItem
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.commonfragments.MenuPopupButton
import ru.ragefalcon.tutatores.commonfragments.MyPopupMenuItem
import ru.ragefalcon.tutatores.databinding.FragmentFinanceBinding
import ru.ragefalcon.tutatores.extensions.setOnItemSelectedListener
import ru.ragefalcon.tutatores.extensions.showMyMessage
import java.lang.ref.WeakReference

class SchetFragment() : BaseFragmentVM<FragmentFinanceBinding>(FragmentFinanceBinding::inflate) {

    private var rvmAdapter = UniRVAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val menuPopupSchet = MyPopupMenuItem<ItemCommonFinOper>(WeakReference(this@SchetFragment), "SchetDelChange").apply {
                addButton(MenuPopupButton.DELETE) {
                    viewmodel.addFin.delPerevod(it)
                }
            }
            tvFinPeriod.setTextColor(Color.WHITE)//resources.getColor(razdelName.colorText))
            with(rvFinanceList) {
                adapter = rvmAdapter
                layoutManager = LinearLayoutManager(context)
            }
            srSchet.visibility = View.VISIBLE
            with(viewmodel) {
                finSpis.spisSchet.observe(viewLifecycleOwner) {
                    if (it.count() != 0) {
                        srSchet.adapter = TypeRasxodAdapter(context!!, it.map { Pair(it.id,it.name) }, R.layout.schet_item)
                        val k = (srSchet.adapter as TypeRasxodAdapter).getPosition(financeFun.getPosMainSchet())
                        srSchet.setSelection(k)
                        srSchet.refreshDrawableState()
                    }
                }

                srSchet.setOnItemSelectedListener { adapterView, view, i, l ->
                    finSpis.schetSpisPeriod.observe(viewLifecycleOwner) {
                        rvmAdapter.updateData(formUniRVItemList(it) { item ->
                            SchetRVItem(item, longTapListener = { itemLT ->
                                if (itemLT.schet != "Перевод") {
                                    showMyMessage("Работайте с транзакцией в разделе ${itemLT.schet}")
                                } else {
                                    viewmodel.finSpis.spisSchet.getLiveData().value?.find { (it.id == itemLT.schetid) }?.let {
                                        menuPopupSchet.showMenu(itemLT, name = "${itemLT.name} (${itemLT.summa.roundToStringProb(1)})")
                                    } ?: run {
                                        showMyMessage("Втрой счет используемый в переводе уже закрыт, если хотите внести изменения вначале откройте его снова.")
                                    }
                                }
                            })
                        })
                    }
                    finSpis.schetSumma.observe(viewLifecycleOwner) {
                        tvFinPeriod.text = it
                    }
                    financeFun.setPosMainSchet(srSchet.selectedItem as Pair<String, String>)
                }
            }
        }
    }
}
