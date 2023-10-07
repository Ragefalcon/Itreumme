package ru.ragefalcon.tutatores.ui.finance

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import ru.ragefalcon.sharedcode.extensions.roundToStringProb
import ru.ragefalcon.sharedcode.models.data.ItemRasxod
import ru.ragefalcon.sharedcode.myGoogleLib.KtorGoogleOAuth
import ru.ragefalcon.tutatores.ItreummeApplication
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.TypeRasxodAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.RasxodRVItem
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.commonfragments.MenuPopupButton
import ru.ragefalcon.tutatores.commonfragments.MyPopupMenuItem
import ru.ragefalcon.tutatores.databinding.FragmentFinanceBinding
import ru.ragefalcon.tutatores.extensions.pxF
import ru.ragefalcon.tutatores.extensions.setOnItemSelectedListener
import ru.ragefalcon.tutatores.extensions.showAddChangeFragDial
import ru.ragefalcon.tutatores.extensions.showMyMessage
import java.lang.ref.WeakReference
import javax.inject.Inject

class RasxodFragment() : BaseFragmentVM<FragmentFinanceBinding>(FragmentFinanceBinding::inflate) {

    private var rvmAdapter = UniRVAdapter()

    @Inject
    lateinit var ktorGOA: KtorGoogleOAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ItreummeApplication.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            ktorGOA.gFiles.observe(viewLifecycleOwner){
                Log.d("MyTag", "it = ${it}")
            }
            Log.d("MyTag", "ktorGOA = $ktorGOA")
            val menuPopupRasxod = MyPopupMenuItem<ItemRasxod>(WeakReference(this@RasxodFragment), "RasxodDelChange").apply {
                addButton(MenuPopupButton.DELETE) {
                    viewmodel.addFin.delRasxod(it)
                }
                addButton(MenuPopupButton.CHANGE) {itemR ->
                    viewmodel.finSpis.spisTypeRasx.getLiveData().value?.find { it.first == itemR.typeid }?.let {
                        showAddChangeFragDial(AddChangeRasxodPanFragment(item = itemR))
                    } ?: run {
                        showMyMessage("Используемый тип расхода уже закрыт, если хотите внести изменения вначале откройте его снова.")
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
                finSpis.spisTypeRasx.observe(viewLifecycleOwner) {
                    if (it.count() != 0) {
                        srFilter.adapter = TypeRasxodAdapter(context!!, it, R.layout.schet_item)
                        val k = (srFilter.adapter as TypeRasxodAdapter).getPosition(financeFun.getPosMainSchet())
                        srFilter.setSelection(k)
                        srFilter.refreshDrawableState()
                    }
                }
                finSpis.rasxodSpisPeriod.observe(viewLifecycleOwner) {
                    rvmAdapter.updateData(formUniRVItemList(it) { item ->
                        RasxodRVItem(item, longTapListener = { itemLT ->
                            viewmodel.finSpis.spisSchet.getLiveData().value?.find { it.id == itemLT.schetid }?.let {
                                menuPopupRasxod.showMenu(itemLT, name = "${itemLT.name} (${itemLT.summa.roundToStringProb(1)})")
                            } ?: run {
                                showMyMessage("Используемый счет уже закрыт, если хотите внести изменения вначале откройте его снова.")
                            }
                        })
                    })
                }

                finSpis.rasxodSummaPeriod.observe(viewLifecycleOwner) {
                    tvFinPeriod.text = it
                }

                srFilter.setOnItemSelectedListener { adapterView, view, i, l ->
                    financeFun.setPosFilterRasx(srFilter.selectedItem as Pair<String, String>)
                }
            }
        }
    }
}
