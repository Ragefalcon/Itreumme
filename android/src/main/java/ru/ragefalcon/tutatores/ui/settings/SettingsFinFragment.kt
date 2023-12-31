package ru.ragefalcon.tutatores.ui.settings

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.databinding.FragmentFinSettBinding
import ru.ragefalcon.tutatores.extensions.format
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel

class SettingsFinFragment  : BaseFragmentVM<FragmentFinSettBinding>(FragmentFinSettBinding::inflate) {


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("MyTag", "!!!!!________________________--------------SettingsFinFragment onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("MyTag", "!!!!!________________________--------------SettingsFinFragment onDetach")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            vpFinSett.adapter = FinSettPageAdapter(childFragmentManager,viewLifecycleOwner.lifecycle)
            tbScheta.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    vpFinSett.currentItem = FinSettTabType.SCHETA.ordinal
                }
            }
            tbTyperasxod.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    vpFinSett.currentItem = FinSettTabType.TYPERASXOD.ordinal
                }
            }
            tbTypedoxod.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    vpFinSett.currentItem = FinSettTabType.TYPEDOXOD.ordinal
                }
            }
            tbValut.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    vpFinSett.currentItem = FinSettTabType.VALUTA.ordinal
                }
            }
            tbScheta.isChecked = true

        }
    }

}
