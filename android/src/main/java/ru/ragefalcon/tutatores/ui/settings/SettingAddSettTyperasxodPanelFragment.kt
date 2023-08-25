package ru.ragefalcon.tutatores.ui.settings;

import android.os.Bundle
import android.view.View
import ru.ragefalcon.sharedcode.models.data.ItemSettTyperasxod
import ru.ragefalcon.tutatores.commonfragments.FragAddChangeDialHelper
import ru.ragefalcon.tutatores.databinding.FragmentAddChangeSettTyperasxodBinding
import ru.ragefalcon.tutatores.extensions.showMyMessage

class SettingAddSettTyperasxodPanelFragment(item: ItemSettTyperasxod? = null) :
    FragAddChangeDialHelper<ItemSettTyperasxod, FragmentAddChangeSettTyperasxodBinding>(
        FragmentAddChangeSettTyperasxodBinding::inflate,
        item
    ) {

    override fun addNote() {
        with(binding) {
            viewmodel.finSpis.spisTyperasxodForSett.getLiveData().value
                ?.find { it.typer == editNameText.text.toString() }
                ?.let {
                    showMyMessage("Тип расхода с таким именем уже есть.")
                } ?: run {
                viewmodel.addFin.addTyperasxod(
                    name = editNameText.text.toString()
                )
            }
        }
    }

    override fun changeNote() {
        with(binding) {
            item?.let {
                viewmodel.addFin.updTyperasxodName(
                    id = it.id.toLong(),
                    name = editNameText.text.toString()
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            item?.let {
                editNameText.setText(it.typer)
            }
        }
    }

}