package ru.ragefalcon.tutatores.ui.avatar.dream;

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewGroupCompat
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.transition.MaterialElevationScale
import ru.ragefalcon.sharedcode.models.data.ItemDream
import ru.ragefalcon.tutatores.adapter.unirvadapter.UniRVAdapter
import ru.ragefalcon.tutatores.adapter.unirvadapter.formUniRVItemList
import ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems.DreamRVItem
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.databinding.FragmentDreamBinding
import ru.ragefalcon.tutatores.extensions.showAddChangeFragDial

class AvatarDreamsFragment() : BaseFragmentVM<FragmentDreamBinding>(FragmentDreamBinding::inflate) {

    private var rvmAdapter = UniRVAdapter()

    private var selItem: ItemDream? by instanceState()

    fun toDreamDetail(extras: FragmentNavigator.Extras) { //view_name: View,view_container: View,
        exitTransition = MaterialElevationScale(false).apply {
            duration = 400
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 400
        }
        ViewGroupCompat.setTransitionGroup(binding.rvDreams, true)
        val directions = AvatarDreamsFragmentDirections.actionDreamToDetail()
        findNavController().navigate(directions, extras)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        /**без этого и  view.doOnPreDraw { startPostponedEnterTransition() } не будет срабатывать обратная анимация*/
        with(binding) {
            view.doOnPreDraw { startPostponedEnterTransition() }
            buttToggleOpenDream.setOnClickListener {
                viewmodel.avatarFun.setOpenspisDreams(buttToggleOpenDream.isChecked)
            }
            buttAddDream.setOnClickListener {
                showAddChangeFragDial(AvatarAddDreamFragDial())
            }
            val menuPopupDream = MyPopupMenuItem<ItemDream>(
                this@AvatarDreamsFragment,
                "DreamDelChange"
            ).apply {
                addButton(MenuPopupButton.DELETE) {
                    viewmodel.addAvatar.delDream(it.id.toLong())
                }
                addButton(MenuPopupButton.UNEXECUTE) {
                    viewmodel.addAvatar.setOpenDream(id = it.id.toLong(), false)
                }
                addButton(MenuPopupButton.EXECUTE) {
                    viewmodel.addAvatar.setOpenDream(id = it.id.toLong(), true)
                }
                addButton(MenuPopupButton.CHANGE) {
                    showAddChangeFragDial(AvatarAddDreamFragDial(it))
                }
            }
            with(rvDreams) {
                adapter = rvmAdapter
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            }
            with(viewmodel) {
                avatarSpis.spisDreams.observe(viewLifecycleOwner) {
                    tvNameSpisok.text = "Мечты:  ${it.count()}"
                    rvmAdapter.updateData(formUniRVItemList(it) { item ->
                        DreamRVItem(item, selectListener = {
                            selItem = it
                            stateViewModel.selectItemDream.value = item
                        }, funForTransition = ::toDreamDetail,
                            longTapListener = {
                                menuPopupDream.showMenu(
                                    item,
                                    name = "${item.name}",
                                    {
                                        if (item.stat == 10L) it != MenuPopupButton.EXECUTE
                                        else it != MenuPopupButton.UNEXECUTE
                                    })
                            })
                    })
                    selItem?.let {
                        rvmAdapter.setSelectItem(it, DreamRVItem::class) //DenPlanViewHolder
                    }
                }
            }
            (rvDreams.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }
}