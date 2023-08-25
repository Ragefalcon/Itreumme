package ru.ragefalcon.tutatores.commonfragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import ru.ragefalcon.sharedcode.models.data.Id_class
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.databinding.FragmentAddChangeDialogBinding
import ru.ragefalcon.tutatores.extensions.getCFM
import ru.ragefalcon.tutatores.extensions.hideKeyboard
import java.util.*


class FragAddChangeDial<T : Id_class>(
    var fragHelper: FragAddChangeDialHelper<T, *>? = null,
    var tagg: String = "tegMyFragDial"
) : MyFragmentForDialog<FragmentAddChangeDialogBinding>(FragmentAddChangeDialogBinding::inflate) {

    init {
        tagg = "${tagg}_${Date().time}"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

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

            }
        } ?: run {
            savedInstanceState?.let {
                tagg = it.getString("tag") ?: ""
                val className = it.getString("className") ?: ""
                if (tagg != "" && className != "") {
                    getCFM().findFragmentByTag("${tagg}_fraghelp")?.let {

                        if (it::class.qualifiedName == className) fragHelper = it as FragAddChangeDialHelper<T, *>


                    }
                }

            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        with(binding) {
            buttCancelAcpanel.setOnClickListener {
                hideKeyboard()
                dismissDial()
            }
            fragHelper?.let { frHelp ->
                frHelp.changeObserve(viewLifecycleOwner) {
                    buttAddChangeAcpanel.text = if (it) "Изменить" else "Добавить"
                }
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