package ru.ragefalcon.tutatores.ui.time

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import ru.ragefalcon.sharedcode.models.data.ItemVxod
import ru.ragefalcon.tutatores.commonfragments.FragAddChangeDialHelper
import ru.ragefalcon.tutatores.databinding.FragmentTimeAddVxodPanelBinding
import ru.ragefalcon.tutatores.extensions.*
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel

class TimeAddVxodPanelFragment(item: ItemVxod? = null) : FragAddChangeDialHelper<ItemVxod,FragmentTimeAddVxodPanelBinding>(FragmentTimeAddVxodPanelBinding::inflate,item) {

    override fun addNote() {
        viewmodel.addTime.addVxod(
            name = binding.editNameVxodText.text.toString(),
            opis = binding.editOpisVxodText.text.toString(),
            data = nowDateWithoutTimeLong(),
            stat = binding.vybStatVxod.selStat.toLong())
    }

    override fun changeNote() {
        item?.let {
            viewmodel.addTime.updVxod(
                id = it.id.toLong(),
                name = binding.editNameVxodText.text.toString(),
                opis = binding.editOpisVxodText.text.toString(),
                data = nowDateWithoutTimeLong(),
                stat = binding.vybStatVxod.selStat.toLong()
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        view.doOnPreDraw {
//            Log.d("MyTut", "timeStamp: ${stateViewModel.tmpTimeStampLong - Calendar.getInstance().time.time}");
//        }
        item?.let {
            binding.editNameVxodText.setText(it.name)
            binding.editOpisVxodText.setText(it.opis)
            binding.vybStatVxod.selectStat(it.stat.toInt())
        }
    }
}