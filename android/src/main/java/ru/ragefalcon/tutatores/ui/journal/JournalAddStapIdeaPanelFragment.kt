package ru.ragefalcon.tutatores.ui.journal;

import android.os.Bundle
import android.view.View
import ru.ragefalcon.sharedcode.models.data.ItemComplexOpisText
import ru.ragefalcon.sharedcode.models.data.ItemIdea
import ru.ragefalcon.sharedcode.models.data.ItemIdeaStap
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TableNameForComplexOpis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeOpisBlock
import ru.ragefalcon.tutatores.commonfragments.FragAddChangeDialHelper
import ru.ragefalcon.tutatores.databinding.FragmentAddIdeaStapPanelBinding
import java.util.*

class JournalAddStapIdeaPanelFragment(
    item: ItemIdeaStap? = null,
    parIdea: ItemIdea? = null,
    callbackKey: String? = null
) :
    FragAddChangeDialHelper<ItemIdeaStap, FragmentAddIdeaStapPanelBinding>(
        FragmentAddIdeaStapPanelBinding::inflate,
        item,
        callbackKey ?: "callMyFragDial"
    ) {

    private var parentIdea: ItemIdea? by instanceState(parIdea) { cache, value ->
        binding.nameParentIdea.text = value?.name
    }

    override fun addNote() {
        with(binding) {
            viewmodel.addJournal.addStapIdea(
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
                idea_id = parentIdea?.id?.toLong() ?: -1L
            )
        }
    }

    override fun changeNote() {
        with(binding) {
            item?.let {
                viewmodel.addJournal.updStapIdea(
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
                    idea_id = parentIdea?.id?.toLong() ?: -1L
                )
//                setRezChangeNote(it)
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
            nameParentIdea.text = parentIdea?.name
            val selIdeaPanel = SelectedIdeaPanel(this@JournalAddStapIdeaPanelFragment,"selIdeaForPodIdea"){
                parentIdea = it
            }
            clSelectedParentIdea.setOnClickListener{
                stateViewModel.selectItemBloknot.value?.let {
                    selIdeaPanel.showMenu(it,parentIdea)
                }
            }
        }
    }

}