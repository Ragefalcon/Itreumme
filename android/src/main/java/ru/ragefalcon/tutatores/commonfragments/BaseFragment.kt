package ru.ragefalcon.tutatores.commonfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.viewbinding.ViewBinding
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel

open class BaseFragmentVM<T : ViewBinding>(
    inflateBF: (LayoutInflater, ViewGroup?, Boolean) -> T
) : BaseFragment<T>(inflateBF) {
    val viewmodel: AndroidFinanceViewModel by activityViewModels()
    val stateViewModel: MyStateViewModel by activityViewModels {
        SavedStateViewModelFactory(requireActivity().application, this)
    }
}

open class BaseFragment<T : ViewBinding>(
    private val inflateBF: (LayoutInflater, ViewGroup?, Boolean) -> T
) : FragSaveInstanseDelegate() {
    private var _binding: T? = null
    protected val binding get() = _binding ?: throw IllegalStateException("Binding is null")
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBF(inflater, container, false)
        return binding.root
    }
}