package ru.ragefalcon.tutatores.ui.time

import android.os.Bundle
import android.view.View
import ru.ragefalcon.sharedcode.models.data.ItemComplexOpisText
import ru.ragefalcon.sharedcode.models.data.ItemVxod
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TableNameForComplexOpis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeOpisBlock
import ru.ragefalcon.tutatores.commonfragments.FragAddChangeDialHelper
import ru.ragefalcon.tutatores.databinding.FragmentTimeAddVxodPanelBinding
import ru.ragefalcon.tutatores.extensions.nowDateWithoutTimeLong

class TimeAddVxodPanelFragment(item: ItemVxod? = null) : FragAddChangeDialHelper<ItemVxod,FragmentTimeAddVxodPanelBinding>(FragmentTimeAddVxodPanelBinding::inflate,item) {

    override fun addNote() {
        viewmodel.addTime.addVxod(
            name = binding.editNameVxodText.text.toString(),
            opis = listOf(
                ItemComplexOpisText(
                    -1L,
                    TableNameForComplexOpis.spisVxod.nameTable,
                    -1L,
                    TypeOpisBlock.simpleText,
                    1L,
                    text = binding.editOpisVxodText.text.toString(),
                    color = 1,
                    fontSize = 3,
                    cursiv = false,
                    bold = 4
                )
            ),
            data = nowDateWithoutTimeLong(),
            stat = binding.vybStatVxod.selStat.toLong())
    }

    override fun changeNote() {
        item?.let {
            viewmodel.addTime.updVxod(
                id = it.id.toLong(),
                name = binding.editNameVxodText.text.toString(),
                opis = listOf(
                    ItemComplexOpisText(
                        -1L,
                        TableNameForComplexOpis.spisVxod.nameTable,
                        it.id.toLong(),
                        TypeOpisBlock.simpleText,
                        1L,
                        text = binding.editOpisVxodText.text.toString(),
                        color = 1,
                        fontSize = 3,
                        cursiv = false,
                        bold = 4
                    )
                ),
                data = nowDateWithoutTimeLong(),
                stat = binding.vybStatVxod.selStat.toLong()
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        item?.let {
            binding.editNameVxodText.setText(it.name)
            binding.editOpisVxodText.setText(it.opis)
            binding.vybStatVxod.selectStat(it.stat.toInt())
        }
    }
}