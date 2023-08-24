package ru.ragefalcon.tutatores.ui.finance.deprecated


import androidx.fragment.app.Fragment

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
/*
class FinanceFragment() : BaseFragmentVM<FragmentFinanceBinding>(FragmentFinanceBinding::inflate) {

    private lateinit var rvmAdapter: RVMainAdapter<ItemCommonFinOper, ItemRasxodBinding, DoxodItemViewHolder>
    private val razdelName by lazy {
        FinanceType.fromString(
            arguments?.getString(RAZDEL_NAME) ?: FinanceType.values()[0].nameRazdel
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvmAdapter = RVMainAdapter(when (razdelName) {
            FinanceType.RASXOD -> R.layout.item_rasxod
            FinanceType.DOXOD -> R.layout.item_doxod
            FinanceType.SCHET -> R.layout.item_schet
        }, {  layoutInflater, viewGr, attach ->
            DoxodItemViewHolder(ItemRasxodBinding.inflate(layoutInflater, viewGr, attach), stateViewModel) }, {})
        with(binding) {
            tvFinPeriod.setTextColor(Color.WHITE)//resources.getColor(razdelName.colorText))
            with(rvFinanceList) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }
            Log.d("MyTag", "Старт фрагмента ${razdelName.name}")
            stateViewModel.visFilterFinPanel.observe(viewLifecycleOwner) {
                if (it && ((razdelName == FinanceType.RASXOD) || (razdelName == FinanceType.DOXOD))) {
                    srFilter.visibility = View.VISIBLE
                    srFilter.layoutParams.height = 40.pxF.toInt()
                    */
/** requestLayout() Нужно для обновления *//*

                    srFilter.requestLayout()
                } else {
                    srFilter.layoutParams.height = 0

                    */
/** requestLayout() Нужно для обновления *//*

                    srFilter.requestLayout()
                    srFilter.visibility = View.INVISIBLE
                }
            }

            when (razdelName) {
                FinanceType.RASXOD -> {
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
                            rvmAdapter.updateData(it.map {
                                ItemCommonFinOper(
                                    it.id,
                                    it.name,
                                    it.typeid,
                                    it.type,
                                    it.summa,
                                    it.data,
                                    it.schetid,
                                    it.schet
                                )
                            })
                        }
                        finSpis.rasxodSummaPeriod.observe(viewLifecycleOwner) {
                            setTextSum(it)
                        }

                        srFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                            }

                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                financeFun.setPosFilterRasx(srFilter.selectedItem as Pair<String, String>)
                            }

                        }
                    }
                }

                FinanceType.DOXOD -> {
                    srSchet.layoutParams.height = 0
                    with(viewmodel) {
                        finSpis.spisTypeDox.observe(viewLifecycleOwner) {
                            if (it.count() != 0) {
                                srFilter.adapter = TypeRasxodAdapter(context!!, it, R.layout.schet_item)
                                val k = (srFilter.adapter as TypeRasxodAdapter).getPosition(financeFun.getPosMainSchet())
                                srFilter.setSelection(k)
                                srFilter.refreshDrawableState()
                            }
                        }
                        finSpis.doxodSpisPeriod.observe(viewLifecycleOwner) {
                            rvmAdapter.updateData(it)
                        }
                        finSpis.doxodSummaPeriod.observe(viewLifecycleOwner) {
                            setTextSum(it)
                        }
                        srFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                            }

                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                financeFun.setPosFilterDox(srFilter.selectedItem as Pair<String, String>)
                            }

                        }
                    }
                }

                FinanceType.SCHET -> {
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
                        srSchet.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                            }

                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                finSpis.schetSpisPeriod.observe(viewLifecycleOwner) {
                                    rvmAdapter.updateData(it)
                                }
                                finSpis.schetSumma.observe(viewLifecycleOwner) {
                                    setTextSum(it)
                                }
                                financeFun.setPosMainSchet(srSchet.selectedItem as Pair<String, String>)
                                Log.d("VersVM", "FragSch-TimeStamp: ${viewmodel.tt}")
                            }

                        }
                    }
                }
            }

        }
    }

    private fun setTextSum(summa: String) {
        binding.tvFinPeriod.text = summa
    }

    companion object {
        private const val RAZDEL_NAME = "razdel_name"

        @JvmStatic
        fun newInstance(razdelName: String): FinanceFragment {
            return FinanceFragment().apply {
                arguments = bundleOf(RAZDEL_NAME to razdelName)
            }
        }
    }

}
*/
