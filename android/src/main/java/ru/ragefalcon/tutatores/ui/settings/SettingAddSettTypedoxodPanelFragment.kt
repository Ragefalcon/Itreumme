package ru.ragefalcon.tutatores.ui.settings

import android.os.Bundle
import android.view.View
import ru.ragefalcon.sharedcode.models.data.ItemSettTypedoxod
import ru.ragefalcon.tutatores.commonfragments.FragAddChangeDialHelper
import ru.ragefalcon.tutatores.databinding.FragmentAddChangeSettTypedoxodBinding
import ru.ragefalcon.tutatores.extensions.showMyMessage

class SettingAddSettTypedoxodPanelFragment(item: ItemSettTypedoxod? = null) :
    FragAddChangeDialHelper<ItemSettTypedoxod, FragmentAddChangeSettTypedoxodBinding>(
        FragmentAddChangeSettTypedoxodBinding::inflate,
        item
    ) {

    override fun addNote() {
        with(binding) {
            viewmodel.finSpis.spisTypedoxodForSett.getLiveData().value?.find { it.typed == editNameText.text.toString() }?.let {
                showMyMessage("Тип дохода с таким именем уже есть.")
            } ?: run {
                viewmodel.addFin.addTypedoxod(
                    name = editNameText.text.toString()
                )
            }
        }
    }

    override fun changeNote() {
        with(binding) {
            item?.let {
                viewmodel.addFin.updTypedoxodName(
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
                editNameText.setText(it.typed)
            }
        }
    }

}