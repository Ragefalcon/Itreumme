package ru.ragefalcon.tutatores.commonfragments


import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.ragefalcon.tutatores.databinding.FragmentVoprosStringBinding
import ru.ragefalcon.tutatores.extensions.*

class OneVoprosStrDial(
    private val fragment: Fragment,
    val callbackKey: String,
    listener_cancel: (()->Unit)? = null,
    listener: ((answer: String) -> Unit)
) {

    init {
        FragmentOneVoprosStr.setRezListener(fragment, callbackKey,listener_cancel, listener)
    }

    fun showVopros(
        vopros: String? = null,
        namePole: String? = null,
        nameButt: String? = null,
        answerDefault: String? = null,
        manager: FragmentManager = fragment.getSFM(),
        tag: String = "tegOneVoprosStrDial",
        bound: MyFragDial.BoundSlide = MyFragDial.BoundSlide.top
    ) {
        fragment.showMyFragDial(FragmentOneVoprosStr( vopros,
                namePole,
                nameButt,
                answerDefault, callbackKey), manager, tag, bound)
    }

}
class FragmentOneVoprosStr(
    vopros: String? = null,
    namePole: String? = null,
    nameButt: String? = null,
    answerDefault: String? = null,
    callbackKey: String? = null
) : MyFragmentForDialog<FragmentVoprosStringBinding>(FragmentVoprosStringBinding::inflate) {

    var callback_Key: String? by instanceState(callbackKey)
    var vopros: String? by instanceState(vopros)
    var namePole: String? by instanceState(namePole)
    var nameButt: String? by instanceState(nameButt)
    var answerDefault: String? by instanceState(answerDefault)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = getMyTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            tvVopros.text = vopros
            editAnswer.hint = namePole
            buttAnswer.text = nameButt
            answerDefault?.let {
                editAnswerText.setText(it)
            }

            buttCancelTpanel.setOnClickListener {
                callback_Key?.let { key ->
                    getSFM().setFragmentResult(
                        "${key}_cancel",
                        bundleOf()
                    )
                }
                dismissDial()
            }
            buttAnswer.setOnClickListener {
                if (editAnswerText.text.toString()!="") {
                    callback_Key?.let { key ->
                        getSFM().setFragmentResult(
                            key,
                            bundleOf("answer" to editAnswerText.text.toString())
                        )
                    }
                    dismissDial()
                }   else    {
                    showMyMessage("Введите вначале значение $namePole")
                }
            }
        }
    }

    companion object {

        fun setRezListener(
            fragment: Fragment,
            requestKey: String,
            listener_cancel: (()->Unit)? = null,
            listener: ((answer: String) -> Unit)? = null
        ) {
            fragment.setSFMResultListener(requestKey) { _, bundle ->
                val answer = bundle.getString("answer") ?: ""
                listener?.invoke(answer)
            }
            fragment.setSFMResultListener("${requestKey}_cancel") { _, _ ->
                listener_cancel?.invoke()
            }
        }
    }

}