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
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.DenPlanRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.NapomRVItem
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentDenPlanBinding
import ru.ragefalcon.tutatores.extensions.*
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel
import java.lang.ref.WeakReference
import java.util.*


class DenPlanFragment : FragSaveInstanseDelegate() {


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
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private var _binding: FragmentDenPlanBinding? = null

    private val binding get() = _binding!!
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDenPlanBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvmAdapter = UniRVAdapter()
        val menuPopupDP = MyPopupMenuItem<ItemDenPlan>(WeakReference(this), "DenPlanDelChange").apply {
            addButton(MenuPopupButton.DELETE) {
                viewmodel.addTime.delDenPlan(it.id.toLong()){
                    /*
                    * TODO здесь необходимо реализовать функцию удаления изображений из памяти устройства
                    * пример в Desctop версии: MainDB.complexOpisSpis.spisComplexOpisForBloknot.delAllImageForItem(it)
                    * */
                }
            }
            addButton(MenuPopupButton.CHANGE) {
                showAddChangeFragDial(TimeAddDenPlanPanelFragment(it), getSFM())
            }
        }
        val menuPopupN = MyPopupMenuItem<ItemNapom>(WeakReference(this), "NapomDelChange").apply {
            addButton(MenuPopupButton.DELETE) {
                viewmodel.addTime.delNapom(it.id.toLong()){
                    /*
                    * TODO здесь необходимо реализовать функцию удаления изображений из памяти устройства
                    * пример в Desctop версии: MainDB.complexOpisSpis.spisComplexOpisForBloknot.delAllImageForItem(it)
                    * */
                }
            }
            addButton(MenuPopupButton.CHANGE) {
                showAddChangeFragDial(TimeAddNapomFragDialog(item = it), getSFM())
            }
        }

        with(binding) {
            with(rvDenplanList) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)

            }

            with(viewmodel) {

                val addBestDays = OneVoprosStrDial(WeakReference(this@DenPlanFragment), "voprosNameBestDay", listener = {
                    addAvatar.addBestDay(it, dateOporDp.value?.time ?: 0)
                }, listener_cancel = {
                    buttToggleBestday.isChecked = false
                })
                buttToggleBestday.setOnClickListener {
                    if (buttToggleBestday.isChecked) {
                        addBestDays.showVopros(
                            "Хотите добавить день ${etDateDp.text} как памятный?",
                            "Название памятного дня", "Да"
                        )
                    } else {
                        buttToggleBestday.isChecked = true
                    }

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
                            rvmAdapter.setSelectItem(it, NapomRVItem::class)
                        }
                    }
                }
                timeSpis.spisDenPlan.observe(viewLifecycleOwner) {

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
                        selItemDenPlan?.let {
                            rvmAdapter.setSelectItem(it, DenPlanRVItem::class)
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
                        val delta = if (time1 > time2) 24 * 60 * 60 * 1000 else 0
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
                        val delta = if (time1 > time2) 24 * 60 * 60 * 1000 else 0
                        val hourMls =
                            (time2 + delta - time1) * prog / 100
                        val hour = hourMls.toDouble() / 1000F / 60F / 60F
                        val exp: Double = when (it.vajn) {
                            0L -> hour * 1.25
                            1L -> hour
                            2L -> hour * 0.5
                            3L -> hour * 0.25
                            else -> 0.0
                        }
//                        it.gotov = prog.toDouble()
//                        it.sum_hour = hour
                        selTextView?.text = Date().fromHourFloat(it.sum_hour.toFloat())
                            .humanizeTime()

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
