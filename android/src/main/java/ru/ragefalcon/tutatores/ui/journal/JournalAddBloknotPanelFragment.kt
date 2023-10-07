package ru.ragefalcon.tutatores.ui.journal;

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import ru.ragefalcon.sharedcode.models.data.ItemBloknot
import ru.ragefalcon.sharedcode.models.data.ItemComplexOpisText
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.*
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
                opis = listOf(
                    ItemComplexOpisText(
                        -1L,
                        TableNameForComplexOpis.spisBloknot.nameTable,
                        -1L,
                        TypeOpisBlock.simpleText,
                        1L,
                        text = editOpisBloknotText.text.toString(),
                        color = 1,
                        fontSize = 3,
                        cursiv = false,
                        bold = 4
                    )
                )
            )
        }
    }

    override fun changeNote() {
        with(binding) {
            item?.let {
                viewmodel.addJournal.updBloknot(
                    id = it.id.toLong(),
                    name = editNameBloknotText.text.toString(),
                    opis = listOf(
                        ItemComplexOpisText(
                            -1L,
                            TableNameForComplexOpis.spisBloknot.nameTable,
                            it.id.toLong(),
                            TypeOpisBlock.simpleText,
                            1L,
                            text = editOpisBloknotText.text.toString(),
                            color = 1,
                            fontSize = 3,
                            cursiv = false,
                            bold = 4
                        )
                    )
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