package ru.ragefalcon.tutatores.ui.start

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import ru.ragefalcon.tutatores.commonfragments.BaseFragmentVM
import ru.ragefalcon.tutatores.databinding.FragmentStartDialogBinding
import ru.ragefalcon.tutatores.extensions.setSFMResultListener
import ru.ragefalcon.tutatores.story.VoiceOver

class StartDialogFragment : BaseFragmentVM<FragmentStartDialogBinding>(FragmentStartDialogBinding::inflate) {

    var firstStart: Boolean by instanceStateDef(false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSFMResultListener("FinalStartDialog") { _, _ ->
            VoiceOver(this@StartDialogFragment).showDialog(VoiceOver.Companion.SpisVODialog.VO_SELECT_RAZDEL)
        }
        setSFMResultListener("actionStartToTime") { _, _ ->
            findNavController().navigate(
                StartDialogFragmentDirections.actionStartToTime()
            )
        }
        setSFMResultListener("actionStartToAvatar") { _, _ ->
            findNavController().navigate(
                StartDialogFragmentDirections.actionStartToAvatar()
            )
        }
        setSFMResultListener("actionStartToFinance") { _, _ ->
            findNavController().navigate(
                StartDialogFragmentDirections.actionStartToFinance()
            )
        }
        setSFMResultListener("actionStartToJournal") { _, _ ->
            findNavController().navigate(
                StartDialogFragmentDirections.actionStartToJournal()
            )
        }
        setSFMResultListener("StartFirstDialog") { _, _ ->
            viewmodel.avatarSpis.spisMainParam.observe(viewLifecycleOwner) {
                if (!firstStart) {
                    firstStart = true
                    if (it.find { it.name == "Birthday" } == null) {
                        VoiceOver(this@StartDialogFragment).showDialog(VoiceOver.Companion.SpisVODialog.VO_FIRST)
                    } else {
                        VoiceOver(this@StartDialogFragment).showDialog(VoiceOver.Companion.SpisVODialog.VO_SELECT_RAZDEL)
                    }
                }
            }
        }
    }
}