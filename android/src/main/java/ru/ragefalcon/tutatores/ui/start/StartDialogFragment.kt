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
            Log.d("MyTut", "startDial: testtt1");
            viewmodel.avatarSpis.spisMainParam.observe(viewLifecycleOwner) {
                Log.d("MyTut", "startDial: testtt2");
                if (!firstStart) {
                    Log.d("MyTut", "startDial: testtt3");
                    firstStart = true
                    if (it.find { it.name == "Birthday" } == null) {
                        Log.d("MyTut", "startDial: testtt4");
                        VoiceOver(this@StartDialogFragment).showDialog(VoiceOver.Companion.SpisVODialog.VO_FIRST)
                    } else {
                        Log.d("MyTut", "startDial: testtt44");
                        VoiceOver(this@StartDialogFragment).showDialog(VoiceOver.Companion.SpisVODialog.VO_SELECT_RAZDEL)
                    }
                }
            }
        }
        Log.d("MyTut", "startDial: testtt");
//        GlobalScope.launch(Dispatchers.IO) {
//            while (!stateViewModel.endFirstAnimation) {
//                sleep(100)
//            }
//        }
////            GlobalScope.launch(Dispatchers.IO) {
//                VoiceOver(this@StartDialogFragment).showDialog(VoiceOver.spisVODialog.VO_FIRST)
//            }
//        }
//        showMyFragDial(FragmentTutDialog(),cancelable = false)
    }
}