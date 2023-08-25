package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems;

import androidx.core.view.ViewCompat
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import ru.ragefalcon.sharedcode.extensions.roundToString
import ru.ragefalcon.sharedcode.models.data.ItemGoal
import ru.ragefalcon.tutatores.adapter.unirvadapter.BaseUniRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.getUniRVViewHolder
import ru.ragefalcon.tutatores.databinding.ItemGoalBinding

class GoalRVItem(
    data: ItemGoal,
    selectListener: ((ItemGoal) -> Unit)? = null,
    listener: ((ItemGoal) -> Unit)? = null,
    longTapListener: ((ItemGoal) -> Unit)? = null,
    funForTransition: ((FragmentNavigator.Extras) -> Unit)? = null
) : BaseUniRVItem<ItemGoal>(
    data,
    getUniRVViewHolder(ItemGoalBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemGoalBinding) {
            with(vh.binding) {

                ViewCompat.setTransitionName(tvNameGoal, "tvNameGoal${item.id}")
                ViewCompat.setTransitionName(tvGoalHour, "tvGoalHour${item.id}")
                ViewCompat.setTransitionName(clGoalItem, "clGoalItem${item.id}")
                tvNameGoal.text = item.name
                tvGoalHour.text = item.hour.roundToString(1)
                if (vh.itemView.isSelected) {
                    selectListener?.invoke(item)
                }

                vh.itemView.setOnClickListener {
                    vh.bindItem?.let { rvset.selFunc(it) }
                    listener?.invoke(item)
                    funForTransition?.invoke(
                        FragmentNavigatorExtras(
                            tvNameGoal to "tv_name_goal_frcl",
                            tvGoalHour to "tv_goal_hour_frcl",
                            clGoalItem to "cl_goal_detail_frcl"
                        )
                    )
                }
                vh.itemView.setOnLongClickListener {
                    vh.bindItem?.let { rvset.selFunc(it) }
                    longTapListener?.invoke(item)
                    true
                }
            }
        }
    }
)