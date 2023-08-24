package ru.ragefalcon.tutatores.ui.time

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ru.ragefalcon.sharedcode.models.data.ItemEffekt
import ru.ragefalcon.sharedcode.models.data.ItemPlan
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.commonfragments.MyFragmentForDialogVM
import ru.ragefalcon.tutatores.databinding.FragmentAddEffektBinding
import ru.ragefalcon.tutatores.extensions.getMyTransition
import ru.ragefalcon.tutatores.extensions.getSFM
import ru.ragefalcon.tutatores.extensions.setSFMResultListener

class TimeAddEffektDial(
    itemEff: ItemEffekt? = null,
    itemPlan: ItemPlan? = null,
    normValue: Double? = null,
    callbackKey: String? = null
) : MyFragmentForDialogVM<FragmentAddEffektBinding>(FragmentAddEffektBinding::inflate) {

    var callback_Key: String? by instanceState(callbackKey)
    var itemName: String? by instanceState(itemEff?.name ?: itemPlan?.name)
    var itemId: Long? by instanceState(itemEff?.idplan ?: (itemPlan?.id?.toLong()))
    var effId: Long? by instanceState(itemEff?.id?.toLong())
    var normValue: Double? by instanceState(itemEff?.norma ?: normValue)
    var change: Boolean by instanceStateDef(false,itemEff?.let { true } ?: false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = getMyTransition()
    }

    private fun updateVopros() {
        with(binding) {
            when (rgEffZlo.checkedRadioButtonId) {
                rbEff.id -> {
                    tvVopros.text =
                        "Добавить отслеживание эффективности по проекту \"${itemName}\" и установить норму:"
                }
                rbZlo.id -> {
                    tvVopros.text =
                        "Добавить отслеживание злоупотребления по проекту \"${itemName}\" и установить норму:"
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            updateVopros()
            rgEffZlo.setOnCheckedChangeListener { group, checkedId ->
                updateVopros()
            }
            normValue?.let {
                if (it<0){
                    normValue = -it
                    rbZlo.isChecked = true
                }

                edtvNormEff.setText(normValue.toString())
            }

            if (change) {
                buttAnswer.text = getString(R.string.name_butt_change)
            }   else    {
                buttAnswer.text = getString(R.string.name_butt_ustanovit)
            }

            buttCancelTpanel.setOnClickListener {
                callback_Key?.let { key ->
                    getSFM().setFragmentResult(
                        key,
                        bundleOf("answer" to false)
                    )
                }
                dismissDial()
            }
            buttAnswer.setOnClickListener {
                itemName?.let { name ->
                    itemId?.let { id ->

                        var norm: Double = 0.0
                        when (rgEffZlo.checkedRadioButtonId) {
                            rbEff.id -> {
                                norm = edtvNormEff.text.toString().toDouble()
                            }
                            rbZlo.id -> {
                                norm = -edtvNormEff.text.toString().toDouble()
                            }
                        }
                        if (change) {
                            effId?.let {
                                viewmodel.addTime.updEffekt(name, norm, it)
                            }
                        }   else    {
                            viewmodel.addTime.addEffekt(name, id, norm)
                        }
                        callback_Key?.let { key ->
                            getSFM().setFragmentResult(
                                key,
                                bundleOf("answer" to true)
                            )
                        }
                    }
                }
                dismissDial()
            }
        }
    }

    companion object {

        fun setRezListener(
            fragment: Fragment,
            requestKey: String,
            listener: ((answer: Boolean) -> Unit)? = null
        ) {
            fragment.setSFMResultListener(requestKey) { key, bundle ->
                val answer = bundle.getBoolean("answer")
                listener?.invoke(answer)
            }
        }
    }

}