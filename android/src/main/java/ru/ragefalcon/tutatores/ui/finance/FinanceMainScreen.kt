package ru.ragefalcon.tutatores.ui.finance

//import android.R

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.DatePicker
import androidx.core.animation.doOnEnd
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.view.*
import androidx.transition.TransitionInflater
import com.google.android.material.tabs.TabLayout
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.adapter.FinanceType
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.databinding.FragmentMainFinscreenBinding
import ru.ragefalcon.tutatores.extensions.format
import ru.ragefalcon.tutatores.extensions.setMargins
import ru.ragefalcon.tutatores.extensions.showAddChangeFragDial
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.roundToInt


class FinanceMainScreen : BaseFragmentVM<FragmentMainFinscreenBinding>(FragmentMainFinscreenBinding::inflate), Postman {

    //    private lateinit var settingsPageAdapter: SettingsPageAdapter
    private lateinit var financePageAdapter: FinancePageAdapter
    private lateinit var financeAnalizPageAdapter: FinancePageAdapter
    private lateinit var colorMain: Array<Int>
    private var currentColorMain: Int = 0
    private var colorNumberShift = 0

    private lateinit var myActivity: Activity

    override fun setKeyUpVol(keyUV: () -> Unit) {
//        keyUpVol = keyUV
    }

    var monthBool = false

    /** во фрагментах контекст лучше всего ловить здесь в onAttach иначе может быть ошибка
     * Fragment not attached to a context
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) myActivity = context //as Activity
        financeAnalizPageAdapter = FinancePageAdapter(true, childFragmentManager) // supportFragmentManager
        financePageAdapter = FinancePageAdapter(false, childFragmentManager) // childFragmentManager
        colorMain = requireContext().run {
            arrayOf(
                getColor(R.color.colorRasxodTheme),//.toMyColorARGB().plusWhite().plusWhite().toIntColor(),
                getColor(R.color.colorDoxodTheme), //.toMyColorARGB().plusWhite().plusWhite().toIntColor(),
                getColor(R.color.colorSchetTheme) //.toMyColorARGB().plusWhite().plusWhite().toIntColor()
            )
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
        exitTransition = inflater.inflateTransition(R.transition.fade)
//        setContentView(R.layout.fragment_main_finscreen)
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            stateViewModel.statusBarSize.observe(viewLifecycleOwner) {
                setMargins(tvAllCapital, 0, stateViewModel.statusBarSize.value!!, 0, 0)
                setMargins(buttFilter, buttFilter.marginStart, buttFilter.marginTop, buttFilter.marginEnd, stateViewModel.navigationBarSize.value!!)
            }
            vpFinance.adapter = financePageAdapter
            buttAnaliz.setOnCheckedChangeListener { buttonView, isChecked -> // .setOnClickListener {
                colorNumberShift = tabLay.selectedTabPosition
                financePageAdapter.analiz = isChecked // toggleAnaliz()
                financePageAdapter.notifyDataSetChanged()
                if (isChecked) {
                    buttFilter.isChecked = false
                    buttFilter.isEnabled = false
                } else {
                    buttFilter.isEnabled = true
                }
            }
            buttFilter.setOnCheckedChangeListener { buttonView, isChecked -> //setOnClickListener{
                stateViewModel.visFilterFinPanel.value = isChecked //stateViewModel.toggleFiler()
                viewmodel.financeFun.setEnableFilter(isChecked) //stateViewModel.visFilterFinPanel.value ?: false)
            }
            viewmodel.dateOpor.observe(viewLifecycleOwner) {
                etMaindate.text = it.format("dd MMM yyyy (EEE)")
            }
//        viewmodel.addDateOwner(viewLifecycleOwner)
            etMaindate.setOnClickListener {
                val aa = Calendar.getInstance()
                aa.time = viewmodel.dateOpor.value

                DatePickerDialog(
                    requireContext(),
                    { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                        val aa = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth, 0, 0, 0)
                        }

                        viewmodel.dateOpor.value = Date(aa.time.time)
                    },
                    aa.get(Calendar.YEAR),
                    aa.get(Calendar.MONTH),
                    aa.get(Calendar.DAY_OF_MONTH)
                )
                    .show()
            }
            buttMaindateRight.setOnClickListener {
                viewmodel.selPer.nextDate()
            }
            buttMaindateLeft.setOnClickListener {
                viewmodel.selPer.prevDate()
            }

            vpFinance.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                v?.let {
                    var koef = (colorNumberShift * v.width + scrollX.toDouble()) / v.width
                    //                    var koef = (scrollX.toDouble()) / v.width
                    var indCol1 = (koef - (koef % 1)).roundToInt()
                    if (indCol1 < 0) indCol1 = 0
                    val indCol2 = if (indCol1 == tabLay.tabCount - 1) indCol1 else indCol1 + 1
                    koef %= 1
                    /**             buttAdd.text = "${tabLay.selectedTabPosition} - $scrollX - $indCol1 - $indCol2 - ${koef.roundToString(4)} "        */
                    /**             buttAdd.text = "${tabLay.selectedTabPosition} - $scrollX - $indCol1 - $indCol2 - ${koef.roundToString(4)} "        */
                    //                    var indCol1=tabLay.selectedTabPosition
                    //                    var indCol2=if (koef>0) { if (indCol1==tabLay.tabCount-1) indCol1 else indCol1+1} else {if (indCol1==0) indCol1 else indCol1-1}
                    if (koef < 0) koef *= (-1)
                    val testR = colorMain[indCol1].red
                    val testG = colorMain[indCol1].green
                    val testB = colorMain[indCol1].blue
                    val testR2 = colorMain[indCol2].red
                    val testG2 = colorMain[indCol2].green
                    val testB2 = colorMain[indCol2].blue
                    clBackgr.setBackgroundColor(
                        Color.argb(
                            255,
                            (testR + (testR2 - testR) * koef).toInt(),
                            (testG + (testG2 - testG) * koef).toInt(),
                            (testB + (testB2 - testB) * koef).toInt()
                        )
                    )
                }
            }
            with(tabLay) {
                setupWithViewPager(vpFinance)
                addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                    }

                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        tab?.let { it_tab ->
                            val position = it_tab.position ?: 0
                            stateViewModel.currentFinType = FinanceType.values()[position]
                            if ((clBackgr.background as ColorDrawable).color != colorMain[position]) {
                                val rect = Rect()
                                it_tab.let {
                                    val tabView = it.view as View
                                    tabView.getGlobalVisibleRect(rect)
                                    animateBackgrReval(position, rect.centerX(), rect.centerY())
                                }
                            }
                        }
                    }

                })
            }




            buttAdd.setOnClickListener {
                when(stateViewModel.currentFinType){
                    FinanceType.RASXOD -> showAddChangeFragDial(AddChangeRasxodPanFragment())
                    FinanceType.DOXOD -> showAddChangeFragDial(AddChangeDoxodPanFragment())
                    FinanceType.SCHET -> showAddChangeFragDial(AddChangeSchetPanFragment())
                }
            }

            buttMonth.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    viewmodel.dateOpor.observe(viewLifecycleOwner) {
                        etMaindate.text = it.format("MMM yyyy")
                    }
                    viewmodel.selPer.SetPeriodMonth()
                }
            }
            buttWeek.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    viewmodel.dateOpor.observe(viewLifecycleOwner) {
                        etMaindate.text = it.format("dd MMM yyyy")
                    }
                    viewmodel.selPer.SetPeriodWeek()
                }
            }
            buttYear.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    viewmodel.dateOpor.observe(viewLifecycleOwner) {
                        etMaindate.text = it.format("yyyy")
                    }
                    viewmodel.selPer.SetPeriodYear()
                }
            }
            tbPeriod.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    viewmodel.selPer.SetPeriodSelectDates()
                    laySelperiod.visibility = View.VISIBLE
                    laySeldate.visibility = View.INVISIBLE
                } else {
                    laySeldate.visibility = View.VISIBLE
                    laySelperiod.visibility = View.INVISIBLE
                }
            }
            buttMonth.isChecked = true
            viewmodel.dateBegin.observe(viewLifecycleOwner) {
                etBegindate.text = it.format("dd.MM.yyyy")
            }
            etBegindate.setOnClickListener {
                var aa = Calendar.getInstance()
                aa.time = viewmodel.dateBegin.value

                DatePickerDialog(
                    requireContext(),
                    { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                        var aa = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth, 0, 0, 0)
                        }

                        viewmodel.dateBegin.value = Date(aa.time.time)
                    },
                    aa.get(Calendar.YEAR),
                    aa.get(Calendar.MONTH),
                    aa.get(Calendar.DAY_OF_MONTH)
                )
                    .show()
            }
            viewmodel.dateEnd.observe(viewLifecycleOwner) {
                etEnddate.text = it.format("dd.MM.yyyy")
            }
            etEnddate.setOnClickListener {
                var aa = Calendar.getInstance()
                aa.time = viewmodel.dateEnd.value

                DatePickerDialog(
                    requireContext(),
                    { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                        var aa = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth, 0, 0, 0)
                        }

                        viewmodel.dateEnd.value = Date(aa.time.time)
                    },
                    aa.get(Calendar.YEAR),
                    aa.get(Calendar.MONTH),
                    aa.get(Calendar.DAY_OF_MONTH)
                )
                    .show()
            }
            initViewModel()
        }
    }

    override fun onResume() {
        super.onResume()
        with(binding) {
            setMargins(tvAllCapital, 0, stateViewModel.statusBarSize.value!!, 0, 0)
            setMargins(
                buttFilter,
                buttFilter.marginStart,
                buttFilter.marginTop,
                buttFilter.marginEnd,
                stateViewModel.navigationBarSize.value!!
            )
        }
    }



    private fun initViewModel() {
        with(viewmodel) {
            finSpis.sumAllCap.observe(viewLifecycleOwner, {
                binding.tvAllCapital.text = it
            })
        }
        Log.d("VersVM", "MainA-TimeStamp: ${viewmodel.tt}")
    }

    class MyVPAL() : ViewPropertyAnimatorListener {
        override fun onAnimationEnd(view: View?) {
            if (view?.alpha == 0f) {
                view.visibility = android.view.View.INVISIBLE

            }
//            Snackbar.make(view!!, "Анимация закончилась", Snackbar.LENGTH_LONG)
        }

        override fun onAnimationCancel(view: View?) {
        }

        override fun onAnimationStart(view: View?) {
        }

    }

    private fun animateBackgrReval(position: Int, centerX: Int, centerY: Int) {
        val endRadius = max(
            hypot(centerX.toDouble(), centerY.toDouble()),
            hypot(binding.vBackgr.width.toDouble() - centerX.toDouble(), centerY.toDouble())
        )

        with(binding.vBackgr) {
            visibility = View.VISIBLE
            setBackgroundColor((colorMain[position]))
        }

        /**
         *  https://stackoverflow.com/questions/26819429/cannot-start-this-animator-on-a-detached-view-reveal-effect
         * */
        view!!.post {
            //create your anim here
            ViewAnimationUtils.createCircularReveal(
                binding.vBackgr,
                centerX,
                centerY,
                0f,
                endRadius.toFloat()
            ).apply {
                duration = 1000
                doOnEnd {
                    binding.clBackgr.setBackgroundColor(colorMain[position])
                    binding.vBackgr.visibility = View.INVISIBLE
                }
                start()
            }
            currentColorMain = colorMain[position]
        }
    }
}
