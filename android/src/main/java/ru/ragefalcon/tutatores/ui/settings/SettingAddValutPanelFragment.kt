package ru.ragefalcon.tutatores.ui.settings

import android.os.Bundle
import android.view.View
import ru.ragefalcon.sharedcode.models.data.ItemValut
import ru.ragefalcon.tutatores.commonfragments.FragAddChangeDialHelper
import ru.ragefalcon.tutatores.databinding.FragmentAddChangeSettValutBinding
import ru.ragefalcon.tutatores.extensions.showMyMessage

class SettingAddValutPanelFragment(item: ItemValut? = null) :
    FragAddChangeDialHelper<ItemValut, FragmentAddChangeSettValutBinding>(
        FragmentAddChangeSettValutBinding::inflate,
        item
    ) {

    override fun addNote() {
        with(binding) {
            viewmodel.finSpis.spisValut.getLiveData().value?.find { it.name == editNameText.text.toString() }?.let {
                showMyMessage("Валюта с таким именем уже есть.")
            } ?: run {
                viewmodel.addFin.addValut(
                    name = editNameText.text.toString(),
                    cod = editCodText.text.toString(),
                    kurs = editKursText.text.toString().toDouble()
                )
            }
        }
    }

    override fun changeNote() {
        with(binding) {
            item?.let {
                viewmodel.addFin.updValut(
                    id = it.id.toLong(),
                    name = editNameText.text.toString(),
                    cod = editCodText.text.toString(),
                    kurs = editKursText.text.toString().toDouble()
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            item?.let {
                editNameText.setText(it.name)
                editCodText.setText(it.cod)
                editKursText.setText(it.kurs.toString())
            }
        }
    }

}