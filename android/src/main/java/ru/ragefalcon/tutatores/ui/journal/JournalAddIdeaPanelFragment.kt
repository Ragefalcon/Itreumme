package ru.ragefalcon.tutatores.ui.journal;

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import ru.ragefalcon.sharedcode.models.data.Id_class
import ru.ragefalcon.sharedcode.models.data.ItemBloknot
import ru.ragefalcon.sharedcode.models.data.ItemComplexOpisText
import ru.ragefalcon.sharedcode.models.data.ItemIdea
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TableNameForComplexOpis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeOpisBlock
import ru.ragefalcon.tutatores.commonfragments.FragAddChangeDialHelper
import ru.ragefalcon.tutatores.commonfragments.MyFragDial
import ru.ragefalcon.tutatores.databinding.FragmentAddIdeaPanelBinding
import ru.ragefalcon.tutatores.extensions.getSFM
import ru.ragefalcon.tutatores.extensions.setSFMResultListener
import ru.ragefalcon.tutatores.extensions.showAddChangeFragDial
import ru.ragefalcon.tutatores.extensions.showMyFragDial
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel
import java.util.*


class JournalAddIdeaPanelFragment(
    item: ItemIdea? = null,
    parBloknot: ItemBloknot? = null,
    parIdea: ItemIdea? = null,
    callbackKey: String? = null
) :
    FragAddChangeDialHelper<ItemIdea, FragmentAddIdeaPanelBinding>(FragmentAddIdeaPanelBinding::inflate, item, callbackKey ?: "callMyFragDial") {

    private var parentBloknot: ItemBloknot? by instanceState<ItemBloknot?>(parBloknot) { cache, value ->
        if (cache != value) {
            parentIdea = null
        }
        binding.nameParentBloknot.text = value?.name
    }
    var parentIdea: ItemIdea? by instanceState(parIdea) { cache, value ->
        if (value != null) {
            binding.nameParentIdea.text = value.name
            binding.buttUnselIdea.isVisible = true
        } else {
            binding.nameParentIdea.text = "Сделать подразделом"
            binding.buttUnselIdea.isVisible = false
        }
    }
    
    override fun addNote() {
        with(binding) {
            viewmodel.addJournal.addIdea(
                name = editNameIdeaText.text.toString(),
                opis = listOf(
                    ItemComplexOpisText(
                        -1L,
                        TableNameForComplexOpis.spisIdea.nameTable,
                        -1L,
                        TypeOpisBlock.simpleText,
                        1L,
                        text = editOpisIdeaText.text.toString(),
                        color = 1,
                        fontSize = 3,
                        cursiv = false,
                        bold = 4
                    )
                ),
                data = Date().time,
                stat = vybStatIdea.selStat.toLong(),
                parent_id = parentIdea?.id?.toLong() ?: -1L ,
                bloknot = parentBloknot?.id?.toLong() ?: -1L
            )
        }
    }

    override fun changeNote() {
        with(binding) {
            item?.let {
                viewmodel.addJournal.updIdea(
                    id = it.id.toLong(),
                    name = editNameIdeaText.text.toString(),
                    opis = listOf(
                        ItemComplexOpisText(
                            -1L,
                            TableNameForComplexOpis.spisIdea.nameTable,
                            it.id.toLong(),
                            TypeOpisBlock.simpleText,
                            1L,
                            text = editOpisIdeaText.text.toString(),
                            color = 1,
                            fontSize = 3,
                            cursiv = false,
                            bold = 4
                        )
                    ),
                    data = Date().time,
                    stat = vybStatIdea.selStat.toLong(),
                    parent_id = parentIdea?.id?.toLong() ?: -1L ,
                    bloknot = parentBloknot?.id?.toLong() ?: -1L
                )
                setRezChangeNote(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            item?.let {
                editNameIdeaText.setText(it.name)
                editOpisIdeaText.setText(it.opis)
                vybStatIdea.selectStat(it.stat.toInt())
            }
            nameParentBloknot.text = parentBloknot?.name
            parentIdea = parentIdea
            val selBlokPanel = SelectedBloknotPanel(this@JournalAddIdeaPanelFragment,"selBloknotForIdea"){
               parentBloknot = it 
            }
            val selIdeaPanel = SelectedIdeaPanel(this@JournalAddIdeaPanelFragment,"selIdeaForPodIdea"){
                parentIdea = it
            }
            clSelectedParentBloknot.setOnClickListener{
                selBlokPanel.showMenu(parentBloknot)
            }
            clSelectedParentIdea.setOnClickListener{
                parentBloknot?.let {
                    selIdeaPanel.showMenu(it,parentIdea)
                }
            }
            buttUnselIdea.setOnClickListener {
                parentIdea = null
            }
        }
    }


}