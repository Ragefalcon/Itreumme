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
import androidx.fragment.app.*
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.databinding.FragmentMyDialogBinding
import ru.ragefalcon.tutatores.databinding.FragmentSelectPlanPanelBinding
import ru.ragefalcon.tutatores.extensions.getCFM
import ru.ragefalcon.tutatores.extensions.getMyAnimListener
import ru.ragefalcon.tutatores.extensions.getSFM
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
//        fragDial?.javaClass?.newInstance()
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
//        dialog?.window?.attributes?.windowAnimations = R.style.dialog_animation
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    //    override fun onResume() {
//        super.onResume()
//        setStyle(STYLE_NO_FRAME,0)
////        dialog?.window?.setLayout(500, 500)
//        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
//        params.width = ViewGroup.LayoutParams.MATCH_PARENT
//        params.height = ViewGroup.LayoutParams.MATCH_PARENT
//        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
//    }

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
//        Log.d("MyTut", "FragList: MyFragDialog parentManager = $parentFragmentManager");
//        Log.d("MyTut", "FragList: MyFragDialog getCFM = ${getCFM()}");
//        Log.d("MyTut", "FragList: MyFragDialog getSFM = ${getSFM()}");
        with(binding) {
            Log.d("MyTut", "fragDial: ${fragDial}");
//        val supportFragmentManager = Fragment.getChildFragmentManager() //requireActivity().getSupportFragmentManager()
//        val supportFragmentManager =  requireActivity().getChildFragmentManager()
            fragDial?.setDismiss { dissEndFun ->
                ViewCompat.animate(frontImgAddPlanTpanel)
                    .alpha(0f)
                    .setDuration(timeAnimation)
                    .setInterpolator(AccelerateDecelerateInterpolator())
//                .setStartDelay(10)
                ViewCompat.animate(backImgAddPlanTpanel)
                    .alpha(0f)
                    .setDuration(timeAnimation)
                    .setInterpolator(AccelerateDecelerateInterpolator())
//                .setStartDelay(10)
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
//        var fragment = MyTest1Fragment()
//        requireActivity().supportFragmentManager.commit {
//            setReorderingAllowed(false)
//            replace(R.id.fr_vyb_stat_vxod_testt, fragment)
//            addToBackStack(null)
//        }
//        vyb_stat_plan.observe(viewLifecycleOwner){
//            butt_add_vxod_record_tpanel.text = vyb_stat_vxod.selStat.toString()//(requireActivity().supportFragmentManager
//        }
            view.doOnPreDraw {
                frontImgAddPlanTpanel.alpha = 0F
                Log.d("MyTut", "timeStamp: ${stateViewModel.tmpTimeStampLong - Calendar.getInstance().time.time}");
                ViewCompat.animate(frontImgAddPlanTpanel)
                    .alpha(0.7f)
                    .setDuration(timeAnimation)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .setListener(getMyAnimListener(aEnd = {
                        Log.d(
                            "MyTut",
                            "timeStamp: ${stateViewModel.tmpTimeStampLong - Calendar.getInstance().time.time}"
                        );
                    }))
//                .setStartDelay(10)
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