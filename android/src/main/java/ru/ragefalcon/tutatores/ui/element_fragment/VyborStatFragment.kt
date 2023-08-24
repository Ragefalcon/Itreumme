package ru.ragefalcon.tutatores.ui.element_fragment

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import ru.ragefalcon.tutatores.R
import ru.ragefalcon.tutatores.extensions.collapse
import ru.ragefalcon.tutatores.extensions.expand
import android.os.Parcelable
import ru.ragefalcon.tutatores.databinding.FragmentVyborStatBinding


class VyborStatFragment @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    enum class typeVybStat {fivecolor,timesquare}
    private val mutableSelectedStat = MutableLiveData<Int>().apply { value = 0 }
    var selStat: Int = 0
        get() = mutableSelectedStat.value ?: 0

    private var selType = typeVybStat.fivecolor

    fun observe(owner: LifecycleOwner, observer:(Int)->Unit){
        mutableSelectedStat.observe(owner,observer)
    }

    
    fun setTimeSquare(){
        with(binding) {
            selType = typeVybStat.timesquare
            buttStat5.visibility = GONE
            buttStat1.buttonTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorStatTimeSquareTint_00))
            buttStat2.buttonTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorStatTimeSquareTint_01))
            buttStat3.buttonTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorStatTimeSquareTint_02))
            buttStat4.buttonTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorStatTimeSquareTint_03))
            /**
             * https://stackoverflow.com/questions/29801031/how-to-add-button-tint-programmatically
             * здесь отмечено кажется решение немного лучше,
             * с поддержкой состояний, но вроде для данного случая мне должно
             * хватать и этого
             * */
            selectStat(selStat)
        }
    }
    fun setFiveColor(){
        with(binding) {
            selType = typeVybStat.fivecolor
            buttStat5.visibility = VISIBLE
            buttStat1.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorStatTint_01))
            buttStat2.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorStatTint_02))
            buttStat3.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorStatTint_03))
            buttStat4.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorStatTint_04))
            buttStat5.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorStatTint_05))
            selectStat(selStat)
        }
    }
    
    private var _binding: FragmentVyborStatBinding? = null
    private val binding get() = _binding!!


    init {
        _binding = FragmentVyborStatBinding.inflate(LayoutInflater.from(context), this, true)
        with(binding) {
            collapse(rgStatVxodAddpt, duration = 0)
            when (selStat) {
                0 -> ivStatVxod.setColorFilter(
                    ContextCompat.getColor(context, R.color.colorStatTint_01),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
                1 -> ivStatVxod.setColorFilter(
                    ContextCompat.getColor(context, R.color.colorStatTint_02),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
                2 -> ivStatVxod.setColorFilter(
                    ContextCompat.getColor(context, R.color.colorStatTint_03),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
                3 -> ivStatVxod.setColorFilter(
                    ContextCompat.getColor(context, R.color.colorStatTint_04),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
                4 -> ivStatVxod.setColorFilter(
                    ContextCompat.getColor(context, R.color.colorStatTint_05),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
            }
            ivStatVxod.requestLayout()

            ivStatVxod.setOnClickListener {
                expand(rgStatVxodAddpt, duration = 200)
            }
            buttStat1.setOnClickListener {
                selectStat(0)
            }
            buttStat2.setOnClickListener {
                selectStat(1)
            }
            buttStat3.setOnClickListener {
                selectStat(2)
            }
            buttStat4.setOnClickListener {
                selectStat(3)
            }
            buttStat5.setOnClickListener {
                selectStat(4)
            }
        }
    }


    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putInt("selStat", selStat) // ... save stuff
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var state = state
        if (state is Bundle) // implicit null check
        {
            val bundle = state
            selectStat(bundle.getInt("selStat")) // ... load stuff
            state = bundle.getParcelable("superState")
        }
        super.onRestoreInstanceState(state)
    }

    fun selectStat(stat: Int){
        with(binding) {
            mutableSelectedStat.value = stat
            when (selType) {
                typeVybStat.fivecolor -> when (selStat) {
                    0 -> ivStatVxod.setColorFilter(
                        ContextCompat.getColor(context, R.color.colorStatTint_01),
                        android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    1 -> ivStatVxod.setColorFilter(
                        ContextCompat.getColor(context, R.color.colorStatTint_02),
                        android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    2 -> ivStatVxod.setColorFilter(
                        ContextCompat.getColor(context, R.color.colorStatTint_03),
                        android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    3 -> ivStatVxod.setColorFilter(
                        ContextCompat.getColor(context, R.color.colorStatTint_04),
                        android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    4 -> ivStatVxod.setColorFilter(
                        ContextCompat.getColor(context, R.color.colorStatTint_05),
                        android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                }
                typeVybStat.timesquare -> when (selStat) {
                    0 -> ivStatVxod.setColorFilter(
                        ContextCompat.getColor(context, R.color.colorStatTimeSquareTint_00),
                        android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    1 -> ivStatVxod.setColorFilter(
                        ContextCompat.getColor(context, R.color.colorStatTimeSquareTint_01),
                        android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    2 -> ivStatVxod.setColorFilter(
                        ContextCompat.getColor(context, R.color.colorStatTimeSquareTint_02),
                        android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    3 -> ivStatVxod.setColorFilter(
                        ContextCompat.getColor(context, R.color.colorStatTimeSquareTint_03),
                        android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                }
            }

            ivStatVxod.requestLayout()
            collapse(rgStatVxodAddpt, duration = 200)
        }
    }

}
