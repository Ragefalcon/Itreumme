package ru.ragefalcon.tutatores.ui.journal;

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import ru.ragefalcon.sharedcode.models.data.ItemBloknot
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.tutatores.commonfragments.FragAddChangeDialHelper
import ru.ragefalcon.tutatores.databinding.FragmentAddBloknotPanelBinding
import ru.ragefalcon.tutatores.databinding.FragmentTimeAddPlanPanelBinding
import ru.ragefalcon.tutatores.extensions.collapse
import ru.ragefalcon.tutatores.extensions.expand

class JournalAddBloknotPanelFragment(item: ItemBloknot? = null) :
    FragAddChangeDialHelper<ItemBloknot, FragmentAddBloknotPanelBinding>(
        FragmentAddBloknotPanelBinding::inflate,
        item
    ) {

    override fun addNote() {
        with(binding) {
            viewmodel.addJournal.addBloknot(
                name = editNameBloknotText.text.toString(),
                opis = editOpisBloknotText.text.toString()
            )
        }
    }

    override fun changeNote() {
        with(binding) {
            item?.let {
                viewmodel.addJournal.updBloknot(
                    id = it.id.toLong(),
                    name = editNameBloknotText.text.toString(),
                    opis = editOpisBloknotText.text.toString()
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            item?.let {
                editNameBloknotText.setText(it.name)
                editOpisBloknotText.setText(it.opis)
            }
        }
    }

}