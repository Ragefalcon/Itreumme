package ru.ragefalcon.tutatores.ui.avatar.goal;

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import ru.ragefalcon.sharedcode.models.data.ItemGoal
import ru.ragefalcon.tutatores.commonfragments.FragAddChangeDialHelper
import ru.ragefalcon.tutatores.databinding.FragmentAvatarAddGoalPanelBinding
import ru.ragefalcon.tutatores.extensions.collapse
import ru.ragefalcon.tutatores.extensions.expand
import java.util.*

class AvatarAddGoalFragDial(item: ItemGoal? = null) :
    FragAddChangeDialHelper<ItemGoal, FragmentAvatarAddGoalPanelBinding>(FragmentAvatarAddGoalPanelBinding::inflate, item) {

    override fun addNote() {
        with(binding) {
            viewmodel.addAvatar.addGoal(
//                lvl = vybStatGoal.selStat.toLong(),
                name = editNameGoalText.text.toString(),
                data1 = Date().time,
                data2 = if (cbSrokGoal.isChecked) dateEndGoal.dateLong else 0,
                opis = editOpisGoalText.text.toString(),
                gotov = 0.0,
                foto = 0
            )
        }
    }

    override fun changeNote() {
        with(binding) {
            item?.let {
                viewmodel.addAvatar.updGoal(
                    id = it.id.toLong(),
//                    lvl = vybStatGoal.selStat.toLong(),
                    name = editNameGoalText.text.toString(),
                    data1 = it.data1,
                    data2 = if (cbSrokGoal.isChecked) dateEndGoal.dateLong else 0,
                    opis = editOpisGoalText.text.toString(),
                    foto = 0
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            item?.let {
//                vybStatGoal.selectStat(it.lvl.toInt())
                editNameGoalText.setText(it.name)
                editOpisGoalText.setText(it.opis)
                if (it.data2 != 0L) {
                    cbSrokGoal.isChecked = true
//                    dateStartGoal.setDate(it.data1)
                    dateEndGoal.setDate(it.data2)
                }            
            }
            
            vybStatGoal.setTimeSquare()
            var heightCl = 0
            clSrokGoal.doOnPreDraw {
                heightCl = clSrokGoal.height
                if (!cbSrokGoal.isChecked) collapse(clSrokGoal)
            }
//            dateStartGoal.setPattern("от dd MMM yyyy (EEE)")
            dateEndGoal.setPattern("до dd MMM yyyy (EEE)")
            cbSrokGoal.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
//                dateStartGoal.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//                expand(clSrokGoal,height = heightCl,duration = 300)
                    expand(clSrokGoal, duration = 300)
                } else {
                    collapse(clSrokGoal, duration = 300)
                }
            }

        }
        
    }

}