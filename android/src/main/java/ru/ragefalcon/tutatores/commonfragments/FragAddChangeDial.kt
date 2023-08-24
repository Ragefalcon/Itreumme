package ru.ragefalcon.tutatores.commonfragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import ru.ragefalcon.sharedcode.models.data.Id_class
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.databinding.FragmentAddChangeDialogBinding
import ru.ragefalcon.tutatores.extensions.getCFM
import ru.ragefalcon.tutatores.extensions.getSFM
import ru.ragefalcon.tutatores.extensions.hideKeyboard
import java.util.*



class FragAddChangeDial<T : Id_class>(
    var fragHelper: FragAddChangeDialHelper<T,*>? = null,
    var tagg: String = "tegMyFragDial"
) : MyFragmentForDialog<FragmentAddChangeDialogBinding>(FragmentAddChangeDialogBinding::inflate) {

    init {
        tagg = "${tagg}_${Date().time}"
        Log.d("MyTut", "timeTagStamp: $tagg");
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        fragDial?.javaClass?.newInstance()
        outState.putString("tag", tagg)
        fragHelper?.let {
            outState.putString("className", it::class.qualifiedName)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragHelper?.let {
            this.childFragmentManager.commit {
                replace(R.id.frag_add_change_container, it, "${tagg}_fraghelp")
//            addToBackStack(null)
            }
        } ?: run {
            savedInstanceState?.let {
                tagg = it.getString("tag") ?: ""
                val className = it.getString("className") ?: ""
                if (tagg != "" && className != "") {
                    getCFM().findFragmentByTag("${tagg}_fraghelp")?.let {
//                        if (it::class.simpleName == className) Generic<FragAddChangeDialHelper<T>>.checkType(it)// fragHelper = it as FragAddChangeDialHelper<T>
                        if (it::class.qualifiedName == className) fragHelper = it as FragAddChangeDialHelper<T,*>
//                    fragHelper?.let {
//                        funAfterSaveInst?.invoke(it)
//                    }
                    }
                }

            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Log.d("MyTut", "FragList: AddChangeDialogFrag parentManager = $parentFragmentManager");
//        Log.d("MyTut", "FragList: AddChangeDialogFrag getCFM = ${getCFM()}");
//        Log.d("MyTut", "FragList: AddChangeDialogFrag getSFM = ${getSFM()}");
        with(binding) {
            buttCancelAcpanel.setOnClickListener {
                hideKeyboard()
                dismissDial()
            }
            fragHelper?.let { frHelp ->
                frHelp.changeObserve(viewLifecycleOwner){
                    buttAddChangeAcpanel.text = if (it) "Изменить" else "Добавить"
                }
                Log.d("MyTut", "orderMarker: 1 ${frHelp.change}");
                buttAddChangeAcpanel.text = if (frHelp.change) "Изменить" else "Добавить"
                buttAddChangeAcpanel.setOnClickListener {
                    hideKeyboard()
                    if (frHelp.change) frHelp.changeNote() else frHelp.addNote()
                    dismissDial()
                }
            } ?: run { buttAddChangeAcpanel.visibility = ViewGroup.INVISIBLE }
        }
    }

}