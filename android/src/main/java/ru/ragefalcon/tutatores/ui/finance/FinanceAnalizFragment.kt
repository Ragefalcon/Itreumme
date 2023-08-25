package ru.ragefalcon.tutatores.ui.finance

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.FinanceType
import ru.ragefalcon.tutatores.adapter.TypeRasxodAdapter
import ru.ragefalcon.tutatores.databinding.FragmentAnalizFinanceBinding
import ru.ragefalcon.tutatores.ui.drawclasses.AnalizRasxodDraw
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel

class FinanceAnalizFragment() : Fragment() {
    private val razdelName by lazy {
        FinanceType.fromString(
            arguments?.getString(FinanceAnalizFragment.RAZDEL_NAME) ?: FinanceType.values()[0].nameRazdel
        )
    }
    private var state_vid = 0
    internal var activity: Postman? = null

    val viewmodel: AndroidFinanceViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Postman) activity = context
    }

    override fun onStart() {
        super.onStart()
        binding.dvDiagram.onStart()
        binding.dvSpisok.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.dvDiagram.onStop()
        binding.dvSpisok.onStop()
    }

    private var _binding: FragmentAnalizFinanceBinding? = null
    private val binding get() = _binding!!
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAnalizFinanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            when (razdelName) {
                FinanceType.RASXOD -> {
                    var analizDraw = AnalizRasxodDraw()
                    with(viewmodel) {
                        finSpis.rasxodSummaPeriod.observe(viewLifecycleOwner) {
                            analizDraw.setSumItogo(it)
                        }
                        finSpis.spisRasxodByType.observe(viewLifecycleOwner) {
                            if (it.count() != 0) {
                                analizDraw.sectorDiag = it
                            } else {
                                analizDraw.sectorDiag = listOf()
                            }
                            analizDraw.setUpdate()
                            dvSpisok?.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                analizDraw.getHeightSpis()
                            )
                        }
                        finSpis.spisRasxodTypeByMonth.observe(viewLifecycleOwner) {
                            if (it.count() != 0) {
                                analizDraw.rectDiag = it
                            } else {
                                analizDraw.rectDiag = listOf()
                            }
                            analizDraw.setUpdateRect()

                            dvHorSpisok?.layoutParams = LinearLayout.LayoutParams(
                                analizDraw.getWidthRectDiag(),
                                LinearLayout.LayoutParams.MATCH_PARENT
                            )
                            dvHorSpisok.invalidate()
                            dvHorSpisok.requestLayout()
                        }
                        finSpis.spisTypeRasx.observe(viewLifecycleOwner) {
                            srTypeAnaliz?.adapter =
                                TypeRasxodAdapter(requireContext(), it, R.layout.simple_list_item_white)
                        }
                        srTypeAnaliz.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                            }
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                financeFun.selectRasxodTypeByMonth(srTypeAnaliz.selectedItem as Pair<String, String>)

                            }
                        }
                    }

                    fun prewVid() {
                        when (state_vid) {
                            0 -> {
                                hsvSpisok.visibility = View.INVISIBLE
                                srTypeAnaliz.visibility = View.INVISIBLE
                                dvDiagram.visibility = View.VISIBLE
                                vsvSpisok.visibility = View.VISIBLE

                                dvSpisok.setDrawFun(analizDraw::drawRasxodSpisByType)
                                dvSpisok.setCalcFun(analizDraw::calculateSpis)
                                dvDiagram.setDrawFun(analizDraw::drawRasxodByType)
                                dvDiagram.setCalcFun(analizDraw::calculate)
                                analizDraw.setUpdate()
                            }
                            1 -> {
                                hsvSpisok.visibility = View.VISIBLE
                                dvDiagram.visibility = View.INVISIBLE
                                vsvSpisok.visibility = View.INVISIBLE
                                srTypeAnaliz.visibility = View.VISIBLE

                                dvHorSpisok.setDrawFun(analizDraw::drawRectDiag)
                                dvHorSpisok.setCalcFun(analizDraw::calculateRect)
                                analizDraw.setUpdateRect()
                            }
                        }
                    }
                    buttChangeAnaliz.setOnClickListener {
                        state_vid++
                        if (state_vid > 1) state_vid = 0
                        prewVid()
                    }
                    prewVid()

                    hsvSpisok.postDelayed({
                        run() {
                            hsvSpisok?.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                        }
                    }, 1000L)
                    /** Без этого скролл не происходит, видимо потому что без задержки эта команда выполняется раньше чем скроллвью успевает увеличиться */
                }
                FinanceType.DOXOD -> {
                    val analizDraw = AnalizRasxodDraw()
                    with(viewmodel) {
                        finSpis.doxodSummaPeriod.observe(viewLifecycleOwner) {
                            analizDraw.setSumItogo(it)
                        }
                        finSpis.spisDoxodByType.observe(viewLifecycleOwner) {
                            if (it.count() != 0) {
                                analizDraw.sectorDiag = it
                            } else {
                                analizDraw.sectorDiag = listOf()
                            }
                            analizDraw.setUpdate()
                            dvSpisok?.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                analizDraw.getHeightSpis()
                            )
                        }
                        finSpis.spisDoxodRasxodByMonth.observe(viewLifecycleOwner) {
                            if (it.count() != 0) {
                                analizDraw.twoRectDiag = it
                            } else {
                                analizDraw.twoRectDiag = listOf()
                            }
                            analizDraw.setUpdateRect()

                            dvHorSpisok?.layoutParams = LinearLayout.LayoutParams(
                                analizDraw.getWidthTwoRectDiag(),
                                LinearLayout.LayoutParams.MATCH_PARENT
                            )
                        }
                        finSpis.spisSumOperWeek.observe(viewLifecycleOwner) {
                            if (it.count() != 0) {
                                analizDraw.sumOperWeek = it
                                analizDraw.minSumOperWeek = financeFun.getMinSumOperWeek().toFloat()
                                analizDraw.maxSumOperWeek = financeFun.getMaxSumOperWeek().toFloat()
                            } else {
                                analizDraw.sumOperWeek = listOf()
                            }
                            analizDraw.shirVisGraf = hsvSpisok.width
                            analizDraw.setUpdateRect()

                            dvHorSpisok?.layoutParams =
                                LinearLayout.LayoutParams(
                                    analizDraw.getWidthGraf(),
                                    LinearLayout.LayoutParams.MATCH_PARENT
                                )
                        }
                        sbCapital.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                analizDraw.cursor = progress.toFloat() / sbCapital.max.toFloat()
                                analizDraw.scrollValue = hsvSpisok.scrollX
                                analizDraw.shirVisGraf = hsvSpisok.width
                                analizDraw.setUpdateRect()
                                dvHorSpisok?.layoutParams = LinearLayout.LayoutParams(
                                    analizDraw.getWidthGraf(),
                                    LinearLayout.LayoutParams.MATCH_PARENT
                                )
                            }
                            override fun onStartTrackingTouch(seekBar: SeekBar?) {

                            }

                            override fun onStopTrackingTouch(seekBar: SeekBar?) {

                            }
                        })
                    }

                    fun prewVid() {
                        when (state_vid) {
                            0 -> {
                                hsvSpisok.visibility = View.INVISIBLE
                                srTypeAnaliz.visibility = View.INVISIBLE
                                dvDiagram.visibility = View.VISIBLE
                                vsvSpisok.visibility = View.VISIBLE
                                sbCapital.visibility = View.INVISIBLE

                                dvSpisok.setDrawFun(analizDraw::drawRasxodSpisByType)
                                dvSpisok.setCalcFun(analizDraw::calculateSpis)
                                dvDiagram.setDrawFun(analizDraw::drawRasxodByType)
                                dvDiagram.setCalcFun(analizDraw::calculate)
                                analizDraw.setUpdate()
                            }
                            1 -> {
                                hsvSpisok.visibility = View.VISIBLE
                                dvDiagram.visibility = View.INVISIBLE
                                vsvSpisok.visibility = View.INVISIBLE
                                srTypeAnaliz.visibility = View.INVISIBLE

                                dvHorSpisok.setDrawFun(analizDraw::drawTwoRectDiag)

                                dvHorSpisok.setCalcFun(analizDraw::calculateRect)
                                dvHorSpisok?.layoutParams = LinearLayout.LayoutParams(
                                    analizDraw.getWidthTwoRectDiag(),
                                    LinearLayout.LayoutParams.MATCH_PARENT
                                )
                                sbCapital.visibility = View.INVISIBLE

                                analizDraw.setUpdateRect()
                                hsvSpisok.postDelayed({
                                    run() {
                                        hsvSpisok?.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                                    }
                                }, 1000L)
                                /** Без этого скролл не происходит, видимо потому что без задержки эта команда выполняется раньше чем скроллвью успевает увеличиться */
                            }
                            2 -> {
                                hsvSpisok.visibility = View.VISIBLE
                                dvDiagram.visibility = View.INVISIBLE
                                vsvSpisok.visibility = View.INVISIBLE
                                srTypeAnaliz.visibility = View.INVISIBLE
                                sbCapital.visibility = View.VISIBLE
                                sbCapital.max = 200

                                dvHorSpisok.setDrawFun(analizDraw::drawGraf)
                                dvHorSpisok.setCalcFun(analizDraw::calculateRect)
                                dvHorSpisok?.layoutParams = LinearLayout.LayoutParams(
                                    analizDraw.getWidthGraf(),
                                    LinearLayout.LayoutParams.MATCH_PARENT
                                )

                                analizDraw.setUpdateRect()
                                hsvSpisok.postDelayed({
                                    run() {
                                        hsvSpisok?.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                                    }
                                }, 1000L)
                                /** Без этого скролл не происходит, видимо потому что без задержки эта команда выполняется раньше чем скроллвью успевает увеличиться */
                            }
                        }
                    }
                    buttChangeAnaliz.setOnClickListener {
                        state_vid++
                        if (state_vid > 2) state_vid = 0
                        prewVid()
                    }
                    prewVid()

                    hsvSpisok.postDelayed({
                        run() {
                            hsvSpisok?.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                        }
                    }, 1000L)
                    /** Без этого скролл не происходит, видимо потому что без задержки эта команда выполняется раньше чем скроллвью успевает увеличиться */
                }
                FinanceType.SCHET -> {
                    var analizDraw = AnalizRasxodDraw()

                    buttChangeAnaliz.visibility = View.INVISIBLE
                    viewmodel.finSpis.spisSchetWithSumm.getLiveData().observe(viewLifecycleOwner, Observer {
                        analizDraw.sumOnSchet = it
                        dvSpisok?.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            analizDraw.getHeightSumOnSchet()
                        )
                    })
                    hsvSpisok.visibility = View.INVISIBLE
                    srTypeAnaliz.visibility = View.INVISIBLE
                    dvDiagram.visibility = View.VISIBLE
                    vsvSpisok.visibility = View.VISIBLE
                    dvDiagram.layoutParams =
                        ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 1)
                    dvSpisok.setDrawFun(analizDraw::drawSumOnSchet)
                    dvSpisok.setCalcFun(analizDraw::calculateSumOnSchet)
                    analizDraw.setUpdateSumOnSchet()

                }
            }
        }
    }

    companion object {
        private const val RAZDEL_NAME = "razdel_name"

        @JvmStatic
        fun newInstance(razdelName: String): FinanceAnalizFragment {
            return FinanceAnalizFragment().apply {
                arguments = bundleOf(RAZDEL_NAME to razdelName)
            }
        }
    }

}