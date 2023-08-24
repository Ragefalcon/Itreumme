package ru.ragefalcon.tutatores.ui.time

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.recyclerview.widget.SimpleItemAnimator
import ru.ragefalcon.sharedcode.models.data.ItemDenPlan
import ru.ragefalcon.sharedcode.models.data.ItemNapom
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.tutatores.adapter.unirvadapter.*
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.*
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentDenPlanBinding
import ru.ragefalcon.tutatores.extensions.*
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel
import java.util.*


class DenPlanFragment : FragSaveInstanseDelegate() {

    private var rvmAdapter = UniRVAdapter()
    //<ItemDenPlan, DenPlanItemViewHolder>

    var denPlans = UniRVItemList()
    var napoms = UniRVItemList()

    var kurok = KurokEndless()
    var kurokFresh = KurokOneShot()


    var selItemNapom: ItemNapom? by instanceState()
    var selItemDenPlan: ItemDenPlan? by instanceState()
    var selProgressBar: ProgressBar? = null
    var selTextView: TextView? = null

    val viewmodel: AndroidFinanceViewModel by activityViewModels {
        SavedStateViewModelFactory(requireActivity().application, this)
    }

    val stateViewModel: MyStateViewModel by activityViewModels {
        SavedStateViewModelFactory(requireActivity().application, this)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("MyTut", "DenPlan: onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        Log.d("MyTut", "DenPlan: onResume")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MyTut", "DenPlan: onCreate")
        super.onCreate(savedInstanceState)
    }

    private var _binding: FragmentDenPlanBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDenPlanBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.fragment_den_plan, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MyTut", "DenPlan: onViewCreated")
        val menuPopupDP = MyPopupMenuItem<ItemDenPlan>(this, "DenPlanDelChange").apply {
            addButton(MenuPopupButton.DELETE) {
                viewmodel.addTime.delDenPlan(it.id.toLong())
            }
            addButton(MenuPopupButton.CHANGE) {
                showAddChangeFragDial(TimeAddDenPlanPanelFragment(it), getSFM())
            }
        }
        val menuPopupN = MyPopupMenuItem<ItemNapom>(this, "NapomDelChange").apply {
            addButton(MenuPopupButton.DELETE) {
                viewmodel.addTime.delNapom(it.id.toLong())
            }
            addButton(MenuPopupButton.CHANGE) {
                showAddChangeFragDial(TimeAddNapomFragDialog(item = it), getSFM())
            }
        }

        with(binding) {
            with(rvDenplanList) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
//            addItemDecoration(divider)
            }

            with(viewmodel) {

                val addBestDays = OneVoprosStrDial(this@DenPlanFragment,"voprosNameBestDay",listener = {
                    addAvatar.addBestDay(it,dateOporDp.value?.time ?: 0)
                }, listener_cancel = {
                    buttToggleBestday.isChecked = false
                })
                buttToggleBestday.setOnClickListener {
                    if (buttToggleBestday.isChecked) {
                        addBestDays.showVopros("Хотите добавить день ${etDateDp.text} как памятный?", "Название памятного дня", "Да")
                    } else {
                        buttToggleBestday.isChecked = true
                    }
//                viewmodel.setOpenSpisPlan(buttToggleOpenPlan.isChecked)
                }
                timeSpis.spisNapom.observe(viewLifecycleOwner) {
                    napoms.setItems(it) { item ->
                        NapomRVItem(item,
                            recyclerView = rvDenplanList,
                            listener = {
                                selItemDenPlan = null
                                selItemNapom = it
                                sbGotovDenPlan.isVisible = false
                            },
                            longTapListener = {
                                menuPopupN.showMenu(it, name = it.name)
                            },
                            setVypNap = { vyp, id ->
                                kurok.skip()
                                addTime.setVypNapom(vyp, dateOporDp.value!!.time, id.toLong())
                            })
                    }

                    kurok.fire {
                        rvmAdapter.updateData(napoms.getList() + denPlans.getList())
                        selItemNapom?.let {
                            rvmAdapter.setSelectItem(it, NapomRVItem::class)//NapomViewHolder
                        }
//                    selItemDenPlan?.let {
//                        rvmAdapter.setSelectItem(it, DenPlanViewHolder::class)
//                    }

                    }
                }
                timeSpis.spisDenPlan.observe(viewLifecycleOwner) {
                    Log.d("DenPlan", "count: ${it.count()}")
//                Log.d("MyTut", "DenPlan: ${viewmodel.getDateTimeOpor().localUnix()}")

                    denPlans.setItems(it) { item ->
                        DenPlanRVItem(item,
                            recyclerView = rvDenplanList,
                            listener = {
                                menuPopupDP.showMenu(it, name = it.name)
                            },
                            getProgBar = { pb, item, tv, chahgeGot ->
                                selProgressBar = pb
                                selTextView = tv
                                selItemDenPlan = item
                                selItemNapom = null
                                binding.sbGotovDenPlan.isVisible = true
                                binding.sbGotovDenPlan.progress = pb.progress
                            })
                    }
                    kurok.fire {
                        rvmAdapter.updateData(napoms.getList() + denPlans.getList())
//                    selItemNapom?.let {
//                        rvmAdapter.setSelectItem(it, NapomViewHolder::class)
//                    }
                        selItemDenPlan?.let {
                            rvmAdapter.setSelectItem(it, DenPlanRVItem::class) //DenPlanViewHolder
                        }
                        kurokFresh.fire()
                    }
                }
                buttDateDpLeft.setOnClickListener {
                    timeFun.prevDay()
                }
                buttDateDpRight.setOnClickListener {
                    timeFun.nextDay()
                }
            }
            /**
             * строчка ниже нужна, чтобы не происходило мерцания при обновлении Item-а,
             * она отключает анимацию по умолчанию при вызове метода onBind (если я не путаю)
             * */
            (rvDenplanList?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            viewmodel.dateOporDp.observe(viewLifecycleOwner) { dateDp ->
                etDateDp.text = dateDp.format("dd MMM yyyy (EEE)")
                viewmodel.avatarSpis.spisBestDays.getLiveData().value?.filter {
                    it.data == dateDp.time
                }.let {
                    it?.let {
                        buttToggleBestday.isChecked = it.count() > 0
                    }
                }
            }
            etDateDp.setOnClickListener {
                var aa = Calendar.getInstance()
                aa.time = viewmodel.dateOporDp.value

                DatePickerDialog(
                    requireContext(),
                    { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                        var aa = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth, 0, 0, 0)
                            set(Calendar.MILLISECOND, 0)
                        }

                        viewmodel.dateOporDp.value = Date(aa.time.time)
                    },
                    aa.get(Calendar.YEAR),
                    aa.get(Calendar.MONTH),
                    aa.get(Calendar.DAY_OF_MONTH)
                )
                    .show()
            }

            buttAddDenPlan.setOnClickListener {
                stateViewModel.tmpTimeStampLong = Calendar.getInstance().time.time
                showAddChangeFragDial(TimeAddDenPlanPanelFragment(), requireActivity().supportFragmentManager)
            }
            buttAddNapom.setOnClickListener {
                showAddChangeFragDial(
                    TimeAddNapomFragDialog(),
                    requireActivity().supportFragmentManager,
                    boundEnter = MyFragDial.BoundSlide.left
                )
            }

            stateViewModel.gotovSelDenPlan.observe(viewLifecycleOwner) {
                sbGotovDenPlan.progress = it
            }
            sbGotovDenPlan.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    selProgressBar?.progress = progress
                    selItemDenPlan?.let {
                        val time1 = Date().timeFromHHmmss(it.time1).time
                        val time2 = Date().timeFromHHmmss(it.time2).time
                        val delta = if (time1>time2) 24*60*60*1000 else 0
                        val hourMls =
                            (time2 + delta - time1) * progress / 100
                        val hour = hourMls.toDouble() / 1000F / 60F / 60F
                        selTextView?.text = Date().fromHourFloat(hour.toFloat()).humanizeTime()
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    var prog = seekBar?.progress ?: 0
                    selProgressBar?.progress = prog
                    selItemDenPlan?.let {
                        val time1 = Date().timeFromHHmmss(it.time1).time
                        val time2 = Date().timeFromHHmmss(it.time2).time
                        val delta = if (time1>time2) 24*60*60*1000 else 0
                        val hourMls =
                            (time2 + delta - time1) * prog / 100
                        val hour = hourMls.toDouble() / 1000F / 60F / 60F
                        val exp: Double = when (it.vajn) {
                            0L -> hour * 1.25
                            1L -> hour //* 1.0
                            2L -> hour * 0.5
                            3L -> hour * 0.25
                            else -> 0.0
                        }
                        it.gotov = prog.toDouble()
                        it.sum_hour = hour
                        selTextView?.text = Date().fromHourFloat(it.sum_hour.toFloat())
                            .humanizeTime() // .toLong()).minusOffset().format("H ч. mm мин.") // item.sum_hour.roundToString(1)
//                    selTextView?.text = Date(hourMls).minusOffset().humanizeTime()
                        kurok.skip()
                        viewmodel.addTime.updGotovDenPlan(
                            it.id.toLong(),
                            prog.toDouble(),
                            hour,
                            exp
                        )
                    }
                }

            })
            Log.d("MyTut", "DenPlan: onViewCreated2")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DenPlanFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}