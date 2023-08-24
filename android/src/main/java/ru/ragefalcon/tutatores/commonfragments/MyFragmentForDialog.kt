package ru.ragefalcon.tutatores.commonfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.viewbinding.ViewBinding
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.tutatores.ui.viewmodels.AndroidFinanceViewModel
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel


open class  MyFragmentForDialogVM<T: ViewBinding>(inflateBF:(LayoutInflater,ViewGroup?,Boolean)->T):MyFragmentForDialog<T>(inflateBF) {
    val viewmodel: AndroidFinanceViewModel by activityViewModels()
    val stateViewModel: MyStateViewModel by activityViewModels {
        SavedStateViewModelFactory(requireActivity().application, this)
    }
}

open class MyFragmentForDialog<T: ViewBinding>(inflateBF:(LayoutInflater,ViewGroup?,Boolean)->T):BaseFragment<T>(inflateBF)  {
    var dismissDialValue: ((()->Unit)?)->Unit = {}
    fun dismissDial() { dismissDialValue(null) }
    fun dismissDial(endF: (()->Unit)) { dismissDialValue(endF) }

    var slideView: View? = null

    fun setDismiss(fd: ((()->Unit)?)->Unit){
        dismissDialValue = fd
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        slideView = view
    }

}
