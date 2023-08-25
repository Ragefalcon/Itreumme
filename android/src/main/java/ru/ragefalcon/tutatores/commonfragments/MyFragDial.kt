package ru.ragefalcon.tutatores.commonfragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.databinding.FragmentMyDialogBinding
import ru.ragefalcon.tutatores.extensions.getCFM
import ru.ragefalcon.tutatores.extensions.getMyAnimListener
import ru.ragefalcon.tutatores.ui.viewmodels.MyStateViewModel
import java.util.*


class MyFragDial(
    var fragDial: MyFragmentForDialog<*>? = null,
    val bound: BoundSlide = BoundSlide.right,
    var tagg: String = "tegMyFragDial",
    cancelable: Boolean = true
) : DialogFragment() {

    init {
        isCancelable = cancelable
    }

    val timeAnimation = 350L

    val stateViewModel: MyStateViewModel by activityViewModels()

    private var funAfterSaveInst: ((MyFragmentForDialog<*>) -> Unit)? = null

    fun setFunSaveInst(ff: ((MyFragmentForDialog<*>) -> Unit)) {
        funAfterSaveInst = ff
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("tag", tagg)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragDial?.let {
            this.childFragmentManager.commit {
                replace(R.id.frag_mydial_container, it, tagg)
            }
        } ?: run {
            savedInstanceState?.let {
                tagg = it.getString("tag") ?: ""
                if (tagg != "") {
                    fragDial = getCFM().findFragmentByTag(tagg) as MyFragmentForDialog<*>
                    fragDial?.let {
                        funAfterSaveInst?.invoke(it)
                    }
                }

            }
        }
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    private var _binding: FragmentMyDialogBinding? = null
    private val binding get() = _binding!!
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            fragDial?.setDismiss { dissEndFun ->
                ViewCompat.animate(frontImgAddPlanTpanel)
                    .alpha(0f)
                    .setDuration(timeAnimation)
                    .setInterpolator(AccelerateDecelerateInterpolator())

                ViewCompat.animate(backImgAddPlanTpanel)
                    .alpha(0f)
                    .setDuration(timeAnimation)
                    .setInterpolator(AccelerateDecelerateInterpolator())

                fragDial?.slideView?.let {
                    when (bound) {
                        BoundSlide.left -> {
                            ViewCompat.animate(it)
                                .translationX(-backImgAddPlanTpanel.width.toFloat())
                                .setDuration(timeAnimation)
                                .setInterpolator(AccelerateDecelerateInterpolator())
                                .setListener(getMyAnimListener(aEnd = {
                                    dismiss()
                                    dissEndFun?.invoke()
                                }))
                        }

                        BoundSlide.right -> {
                            ViewCompat.animate(it)
                                .translationX(backImgAddPlanTpanel.width.toFloat())
                                .setDuration(timeAnimation)
                                .setInterpolator(AccelerateDecelerateInterpolator())
                                .setListener(getMyAnimListener(aEnd = {
                                    dismiss()
                                    dissEndFun?.invoke()
                                }))
                        }

                        BoundSlide.top -> {
                            ViewCompat.animate(it)
                                .translationY(-backImgAddPlanTpanel.height.toFloat())
                                .setDuration(timeAnimation)
                                .setInterpolator(AccelerateDecelerateInterpolator())
                                .setListener(getMyAnimListener(aEnd = {
                                    dismiss()
                                    dissEndFun?.invoke()
                                }))
                        }

                        BoundSlide.alpha -> {
                            ViewCompat.animate(it)
                                .alpha(0f)
                                .setDuration(timeAnimation)
                                .setInterpolator(AccelerateDecelerateInterpolator())
                                .setListener(getMyAnimListener(aEnd = {
                                    dismiss()
                                    dissEndFun?.invoke()
                                }))
                        }
                    }
                }
            }

            view.doOnPreDraw {
                frontImgAddPlanTpanel.alpha = 0F
                ViewCompat.animate(frontImgAddPlanTpanel)
                    .alpha(0.7f)
                    .setDuration(timeAnimation)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .setListener(getMyAnimListener(aEnd = {
                    }))
                backImgAddPlanTpanel.alpha = 0F
                ViewCompat.animate(backImgAddPlanTpanel)
                    .alpha(1f)
                    .setDuration(timeAnimation)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                fragDial?.slideView?.let {
                    when (bound) {
                        BoundSlide.left -> {
                            val aaa = it.translationX
                            it.translationX = -backImgAddPlanTpanel.width.toFloat()
                            ViewCompat.animate(it)
                                .translationX(aaa)
                                .setDuration(timeAnimation)
                                .setInterpolator(AccelerateDecelerateInterpolator())
                        }

                        BoundSlide.right -> {
                            val aaa = it.translationX
                            it.translationX = backImgAddPlanTpanel.width.toFloat()
                            ViewCompat.animate(it)
                                .translationX(aaa)
                                .setDuration(timeAnimation)
                                .setInterpolator(AccelerateDecelerateInterpolator())
                        }

                        BoundSlide.top -> {
                            val aaa = it.translationY
                            it.translationY = -backImgAddPlanTpanel.height.toFloat()
                            ViewCompat.animate(it)
                                .translationY(aaa)
                                .setDuration(timeAnimation)
                                .setInterpolator(AccelerateDecelerateInterpolator())
                        }

                        BoundSlide.alpha -> {
                            it.alpha = 0F
                            ViewCompat.animate(it)
                                .alpha(1f)
                                .setDuration(timeAnimation)
                                .setInterpolator(AccelerateDecelerateInterpolator())
                        }
                    }
                }
            }
        }
    }

    enum class BoundSlide { right, left, top, alpha }
    companion object {
        @JvmStatic
        fun newInstance() =
            MyFragDial().apply {
                arguments = Bundle().apply {
                }
            }
    }
}