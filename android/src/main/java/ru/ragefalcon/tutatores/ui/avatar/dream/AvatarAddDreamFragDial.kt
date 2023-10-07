package ru.ragefalcon.tutatores.ui.avatar.dream

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import ru.ragefalcon.sharedcode.models.data.ItemDream
import ru.ragefalcon.tutatores.commonfragments.FragAddChangeDialHelper
import ru.ragefalcon.tutatores.databinding.FragmentAvatarAddDreamPanelBinding
import ru.ragefalcon.tutatores.extensions.collapse
import ru.ragefalcon.tutatores.extensions.expand
import java.util.*

class AvatarAddDreamFragDial(item: ItemDream? = null) :
    FragAddChangeDialHelper<ItemDream, FragmentAvatarAddDreamPanelBinding>(FragmentAvatarAddDreamPanelBinding::inflate, item) {

    override fun addNote() {
        with(binding) {
            viewmodel.addAvatar.addDream(
                name = editNameDreamText.text.toString(),
                data1 = if (cbSrokDream.isChecked) dateEndDream.dateLong else 1,
                opis = editOpisDreamText.text.toString(),
                stat = 0,
                foto = 0
            )
        }
    }

    override fun changeNote() {
        with(binding) {
            item?.let {
                viewmodel.addAvatar.updDream(
                    id = it.id.toLong(),
                    name = editNameDreamText.text.toString(),
                    data1 = if (cbSrokDream.isChecked) dateEndDream.dateLong else 0L,
                    opis = editOpisDreamText.text.toString(),
                    foto = 0
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            item?.let {
                vybStatDream.selectStat(it.lvl.toInt())
                editNameDreamText.setText(it.name)
                editOpisDreamText.setText(it.opis)
                if (it.data1 != 0L) {
                    cbSrokDream.isChecked = true
                    dateEndDream.setDate(it.data1)
                }
            }

            vybStatDream.setTimeSquare()
            clSrokDream.doOnPreDraw {
                if (!cbSrokDream.isChecked) collapse(clSrokDream)
            }
            dateEndDream.setPattern("до dd MMM yyyy (EEE)")
            cbSrokDream.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    expand(clSrokDream, duration = 300)
                } else {
                    collapse(clSrokDream, duration = 300)
                }
            }
        }
    }
}