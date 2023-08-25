package ru.ragefalcon.tutatores.ui.journal

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.transition.TransitionInflater
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.databinding.FragmentMainJournalScreenBinding
import ru.ragefalcon.tutatores.extensions.setMargins
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel

class JournalMainScreen : Fragment() {

    val viewmodel: AndroidFinanceViewModel by activityViewModels()
    val stateViewModel: MyStateViewModel by activityViewModels()

    private lateinit var myActivity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragmentManager: FragmentManager = requireActivity().getSupportFragmentManager()
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
        exitTransition = inflater.inflateTransition(R.transition.fade)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) myActivity = context //as Activity
    }

    override fun onResume() {
        super.onResume()
        setMargins(
            binding.fragmentJournalContainerLayout,
            0,
            stateViewModel.statusBarSize.value!!,
            0,
            stateViewModel.navigationBarSize.value!!
        )
    }

    private var _binding: FragmentMainJournalScreenBinding? = null
    private val binding get() = _binding!!
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainJournalScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stateViewModel.statusBarSize.observe(viewLifecycleOwner) {
            setMargins(
                binding.fragmentJournalContainerLayout,
                0,
                stateViewModel.statusBarSize.value!!,
                0,
                stateViewModel.navigationBarSize.value!!
            )
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            JournalMainScreen().apply {
                arguments = Bundle().apply {
                }
            }
    }
}