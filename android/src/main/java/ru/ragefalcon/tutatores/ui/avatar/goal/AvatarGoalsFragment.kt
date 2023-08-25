package ru.ragefalcon.tutatores.ui.avatar.goal;

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewGroupCompat
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.transition.MaterialElevationScale
import ru.ragefalcon.sharedcode.models.data.ItemGoal
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.GoalRVItem
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.commonfragments.MenuPopupButton
import ru.ragefalcon.tutatores.commonfragments.MyPopupMenuItem
import ru.ragefalcon.tutatores.databinding.FragmentGoalBinding
import ru.ragefalcon.tutatores.extensions.showAddChangeFragDial

class AvatarGoalsFragment() : BaseFragmentVM<FragmentGoalBinding>(FragmentGoalBinding::inflate) {

    private var rvmAdapter = UniRVAdapter()

    private var selItem: ItemGoal? by instanceState()

    fun toGoalDetail(extras: FragmentNavigator.Extras) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = 400
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 400
        }
        ViewGroupCompat.setTransitionGroup(binding.rvGoals, true)
        val directions = AvatarGoalsFragmentDirections.actionGoalToDetail()
        findNavController().navigate(directions, extras)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        /**без этого и  view.doOnPreDraw { startPostponedEnterTransition() } не будет срабатывать обратная анимация*/
        with(binding) {
            view.doOnPreDraw { startPostponedEnterTransition() }

            buttToggleOpenGoal.setOnClickListener {
                viewmodel.avatarFun.setOpenspisGoals(buttToggleOpenGoal.isChecked)
            }
            buttAddGoal.setOnClickListener {
                showAddChangeFragDial(AvatarAddGoalFragDial())
            }
            val menuPopupGoal = MyPopupMenuItem<ItemGoal>(this@AvatarGoalsFragment, "GoalDelChange").apply {
                addButton(MenuPopupButton.DELETE) {
                    viewmodel.addAvatar.delGoal(it.id.toLong())
                }
                addButton(MenuPopupButton.UNEXECUTE) {
                    viewmodel.addAvatar.setOpenGoal(id = it.id.toLong(), false)
                }
                addButton(MenuPopupButton.EXECUTE) {
                    viewmodel.addAvatar.setOpenGoal(id = it.id.toLong(), true)
                }
                addButton(MenuPopupButton.CHANGE) {
                    showAddChangeFragDial(AvatarAddGoalFragDial(it))
                }
            }
            with(rvGoals) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }
            with(viewmodel) {
                avatarSpis.spisGoals.observe(viewLifecycleOwner) {
                    tvNameSpisok.text = "Целей:  ${it.count()}"
                    rvmAdapter.updateData(formUniRVItemList(it) { item ->
                        GoalRVItem(item, selectListener = {
                            selItem = it
                            stateViewModel.selectItemGoal.value = item
                        }, funForTransition = ::toGoalDetail,
                            longTapListener = {
                                menuPopupGoal.showMenu(
                                    item,
                                    name = "${item.name}",
                                    { if (item.gotov == 100.0) it != MenuPopupButton.EXECUTE else it != MenuPopupButton.UNEXECUTE })
                            })
                    })
                    selItem?.let {
                        rvmAdapter.setSelectItem(it, GoalRVItem::class)
                    }
                }
            }
            (rvGoals.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }
}