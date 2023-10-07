package ru.ragefalcon.tutatores.ui.avatar;

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.SimpleItemAnimator
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.sharedcode.models.data.ItemPrivsGoal
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.PrivsGoalRVItem
import ru.ragefalcon.tutatores.commonfragments.MenuPopupButton
import ru.ragefalcon.tutatores.commonfragments.MyFragmentForDialogVM
import ru.ragefalcon.tutatores.commonfragments.MyPopupMenuItem
import ru.ragefalcon.tutatores.databinding.FragmentPrivPlanAndStapBinding
import ru.ragefalcon.tutatores.ui.time.SelectedPlanPanel
import ru.ragefalcon.tutatores.ui.time.SelectedPlanStapPanel
import java.lang.ref.WeakReference
import java.util.*

class PrivsGoalDial(idGoalDream: Long? = null) :
    MyFragmentForDialogVM<FragmentPrivPlanAndStapBinding>(FragmentPrivPlanAndStapBinding::inflate) {

    private var rvmAdapter = UniRVAdapter()

    var tmpPlanForPrivsStap: ItemPlan? by instanceState()
    var idGoalDream: Long? by instanceState(idGoalDream)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {


            buttBack.setOnClickListener {
                dismissDial()
            }
            with(rvPrivPlanAndStap) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }
            with(viewmodel) {
                avatarFun.setSelectedIdForPrivsGoal(idGoalDream ?: -1)
                val menuPopupPrivsGoal =
                    MyPopupMenuItem<ItemPrivsGoal>(WeakReference(this@PrivsGoalDial), "menuPopupPrivsGoal").apply {
                        addButton(MenuPopupButton.DELETE) {
                            addAvatar.delPrivsGoal(it.id.toLong())
                        }
                    }
                val dialAddPlan = SelectedPlanPanel(this@PrivsGoalDial, "addPrivsPlanForGoal") {
                    addAvatar.addPrivsGoal(
                        idGoalDream ?: -1,
                        it.name,
                        0,
                        it.id.toLong(),
                        it.vajn,
                        Date().time
                    )
                }
                val dialAddStap_2 = SelectedPlanStapPanel(this@PrivsGoalDial, "addPrivsStapForGoal_2") {
                    tmpPlanForPrivsStap?.let { plan ->
                        addAvatar.addPrivsGoal(
                            id_goal = idGoalDream ?: -1,
                            name = it.name,
                            stap = it.id.toLong(),
                            id_plan = plan.id.toLong(),
                            vajn = plan.vajn,
                            date = Date().time
                        )
                    }
                }
                val dialAddStap_1 = SelectedPlanPanel(this@PrivsGoalDial, "addPrivsStapForGoal_1") {
                    tmpPlanForPrivsStap = it
                    val aa = arrayListOf<Long>()
                    avatarSpis.spisPlanStapOfGoal.getLiveData().value?.map { it.stap.toLong() }?.toCollection(aa)
                    dialAddStap_2.showMenu(
                        parPlan = it,
                        arrayIskl = aa
                    )
                }
                buttAddPlan.setOnClickListener {
                    dialAddPlan.showMenu(arrayIskl = avatarSpis.spisPlanStapOfGoal.getLiveData().value?.filter { it.stap == "0" }
                        ?.map { it.id_plan.toLong() })
                }
                buttAddStap.setOnClickListener {
                    dialAddStap_1.showMenu(arrayIskl = avatarSpis.spisPlanStapOfGoal.getLiveData().value?.filter { it.stap == "0" }
                        ?.map { it.id_plan.toLong() })
                }
                avatarSpis.spisPlanStapOfGoal.observe(viewLifecycleOwner) {
                    rvmAdapter.updateData(formUniRVItemList(it) { item ->
                        PrivsGoalRVItem(item, rvPrivPlanAndStap, longTapListener = {
                            menuPopupPrivsGoal.showMenu(it, name = item.name)
                        })
                    })
                }
            }
            /**
             * строчка ниже нужна, чтобы не происходило мерцания при обновлении Item-а,
             * она отключает анимацию по умолчанию при вызове метода onBind (если я не путаю)
             * */
            (rvPrivPlanAndStap.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }
}