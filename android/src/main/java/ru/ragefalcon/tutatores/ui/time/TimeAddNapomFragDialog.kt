package ru.ragefalcon.tutatores.ui.time

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import ru.ragefalcon.sharedcode.models.data.ItemComplexOpisText
import ru.ragefalcon.sharedcode.models.data.ItemNapom
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TableNameForComplexOpis
import ru.ragefalcon.sharedcode.viewmodels.MainViewModels.EnumData.TypeOpisBlock
import ru.ragefalcon.tutatores.commonfragments.FragAddChangeDialHelper
import ru.ragefalcon.tutatores.databinding.FragmentTimeAddNapomBinding
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel

class TimeAddNapomFragDialog(item: ItemNapom? = null, val callback: ((ItemNapom?)->Unit)? = null) : FragAddChangeDialHelper<ItemNapom,FragmentTimeAddNapomBinding>(FragmentTimeAddNapomBinding::inflate,item) {

    override fun addNote() {
        with(binding) {
            viewmodel.addTime.addNapom(
                idplan = -1,
                idstap = -1,
                name = editNameNapomText.text.toString(),
                data = dateNapom.dateLong,
                opis = listOf(
                    ItemComplexOpisText(
                        -1L,
                        TableNameForComplexOpis.spisNapom.nameTable,
                        -1L,
                        TypeOpisBlock.simpleText,
                        1L,
                        text = editOpisNapomText.text.toString(),
                        color = 1,
                        fontSize = 3,
                        cursiv = false,
                        bold = 4
                    )
                ),
                time = timeNapom.timeStrHHmm,
                gotov = false
            )
            callback?.invoke(null)
        }
    }

    override fun changeNote() {
        with(binding) {
            item?.let {
                viewmodel.addTime.updNapom(
                    id = it.id.toLong(),
                    name = editNameNapomText.text.toString(),
                    data = dateNapom.dateLong,
                    opis = listOf(
                        ItemComplexOpisText(
                            -1L,
                            TableNameForComplexOpis.spisNapom.nameTable,
                            it.id.toLong(),
                            TypeOpisBlock.simpleText,
                            1L,
                            text = editOpisNapomText.text.toString(),
                            color = 1,
                            fontSize = 3,
                            cursiv = false,
                            bold = 4
                        )
                    ),
                    time = timeNapom.timeStrHHmm
                )
                callback?.invoke(item)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            dateNapom.setPattern("dd MMM yyyy (EEE)")
            dateNapom.setDate(viewmodel.dateOporDp.value!!)

            item?.let {
                editNameNapomText.setText(it.name)
                dateNapom.setDate(it.data)
                editOpisNapomText.setText(it.opis)
                timeNapom.setTimeHH_mm(it.time)
            }

            timeNapom.setPattern("Звонок: HH:mm")
        }
    }
}
