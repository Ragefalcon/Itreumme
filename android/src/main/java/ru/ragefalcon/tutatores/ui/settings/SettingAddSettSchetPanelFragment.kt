package ru.ragefalcon.tutatores.ui.settings;

import android.os.Bundle
import android.view.View
import ru.ragefalcon.sharedcode.models.data.ItemSettSchet
import ru.ragefalcon.tutatores.adapter.TypeRasxodAdapter
import ru.ragefalcon.tutatores.commonfragments.FragAddChangeDialHelper
import ru.ragefalcon.tutatores.databinding.FragmentAddChangeSettSchetBinding
import ru.ragefalcon.tutatores.extensions.showMyMessage

class SettingAddSettSchetPanelFragment(item: ItemSettSchet? = null) :
    FragAddChangeDialHelper<ItemSettSchet, FragmentAddChangeSettSchetBinding>(
        FragmentAddChangeSettSchetBinding::inflate,
        item
    ) {

    override fun addNote() {
        with(binding) {
            val valut = srValSchet.selectedItem as Pair<String, String>
            viewmodel.finSpis.spisValut.getLiveData().value?.find { it.id == valut.first }?.let { valItem ->
                viewmodel.finSpis.spisSchet.getLiveData().value?.find { it.name == "${editNameText.text.toString()}, ${valItem.cod}" }?.let {
                    showMyMessage("Счет с таким именем уже есть.")
                } ?: run {
                    viewmodel.addFin.addSchet(
                        name = editNameText.text.toString(),
                        val_id = valut.first.toLong()
                    )
                }
            }
        }
    }

    override fun changeNote() {
        with(binding) {
            item?.let {
                viewmodel.addFin.updSchetName(
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
                editNameText.setText(it.name)
                srValSchet.visibility = View.INVISIBLE
            }
            binding.srValSchet.adapter = TypeRasxodAdapter(requireContext(), listOf())
            viewmodel.finSpis.spisValut.observe(viewLifecycleOwner) {
                binding.srValSchet.adapter = TypeRasxodAdapter(requireContext(), it.map { Pair(it.id, it.name) })
            }
        }
    }

}