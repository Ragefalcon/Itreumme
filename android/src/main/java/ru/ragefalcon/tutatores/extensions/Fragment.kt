package ru.ragefalcon.tutatores.extensions

import android.os.Bundle
import androidx.fragment.app.*
import ru.ragefalcon.sharedcode.models.data.Id_class
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.commonfragments.*
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel

fun Fragment.getSFM(): FragmentManager = requireActivity().supportFragmentManager

fun Fragment.getCFM(): FragmentManager = this.childFragmentManager

fun Fragment.getPFM(): FragmentManager = this.parentFragmentManager

fun <T : Id_class> Fragment.showAddChangeFragDial(
    fragHelper: FragAddChangeDialHelper<T,*>,
    manager: FragmentManager = getSFM(),
    tag: String = "tegMyFragDial",
    boundEnter: MyFragDial.BoundSlide = MyFragDial.BoundSlide.right
) {

    showMyFragDial(FragAddChangeDial(fragHelper, tag), manager, tag, boundEnter)
}

fun Fragment.showMyMessage(message: String, manager: FragmentManager = getSFM(), tag: String = "tegMyMessageFragDial"){
    showMyFragDial(FragMyMessage(message),manager, tag, MyFragDial.BoundSlide.alpha)
}
fun Fragment.getVM():AndroidFinanceViewModel {
    val vm: AndroidFinanceViewModel by activityViewModels()
    return vm
}


fun Fragment.showMyFragDial(
    fragDial: MyFragmentForDialog<*>,
    manager: FragmentManager = getSFM(),
    tag: String = "tegMyFragDial",
    bound: MyFragDial.BoundSlide = MyFragDial.BoundSlide.right,
    cancelable: Boolean = true
) {
    var aa = MyFragDial(fragDial, bound, tag,cancelable)
    aa.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme)
//            aa.dialog!!.getWindow()!!.setLayout(500, 500)
    aa.show(manager, tag + "_main")//+"_main"
}

fun Fragment.setSFMResultListener(
    requestKey: String,
    listener: ((requestKey: String, bundle: Bundle) -> Unit)
) {
    getSFM().setFragmentResultListener(requestKey, this, listener)
}