package ru.ragefalcon.tutatores.adapter.unirvadapter.rvitems

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.SeekBar
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import ru.ragefalcon.tutatores.adapter.unirvadapter.BaseUniRVItem
import ru.ragefalcon.tutatores.adapter.unirvadapter.getUniRVViewHolder
import ru.ragefalcon.tutatores.commonfragments.MyPopupItem
import ru.ragefalcon.tutatores.databinding.ItemMyPopupBinding
import ru.ragefalcon.tutatores.databinding.ItemMyPopupDeleteBinding
import ru.ragefalcon.tutatores.extensions.collapse
import ru.ragefalcon.tutatores.extensions.dpToPx
import ru.ragefalcon.tutatores.extensions.getMyAnimListener
import ru.ragefalcon.tutatores.extensions.pxToDp

class MyPopupRVItem(
    data: MyPopupItem,
    listener: ((String) -> Unit)? = null
) : BaseUniRVItem<MyPopupItem>(
    data,
    getUniRVViewHolder(ItemMyPopupBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemMyPopupBinding) {
            with(vh.binding) {
                tvNamePopupItem.text = item.name
                ivMyPopupItem.setImageResource(item.resId)

                vh.itemView.setOnClickListener { // } .setOnClickListener {
                    vh.bindItem?.let { rvset.selFunc(it) }
                    listener?.invoke(item.podkey)
                }
//                vh.itemView.setOnLongClickListener {
//                    vh.bindItem?.let { rvset.selFunc(it) }
//                    longTapListener?.invoke(item)
//                    true
//                }
            }
        }
    }
)
class MyPopupDeleteRVItem(
    data: MyPopupItem,
    listener: ((String) -> Unit)? = null
) : BaseUniRVItem<MyPopupItem>(
    data,
    getUniRVViewHolder(ItemMyPopupDeleteBinding::inflate) { vh, item, rvset ->
        if (vh.binding is ItemMyPopupDeleteBinding) {
            with(vh.binding) {
                var startTrack = false
                var goodTouch = false
                sbDeleteSlider.setOnTouchListener { v, event ->
                    val thumb: Drawable = (v as SeekBar).thumb;

                    if (!startTrack) {
                        goodTouch = true
                        (event.x <= thumb.bounds.left - 20.dpToPx
                                || event.x >= thumb.bounds.right + 20.dpToPx
//                                || event.y <= thumb.bounds.top
//                                || event.y >= thumb.bounds.bottom
                                )
                    } else false

                }
                sbDeleteSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                        if (goodTouch){
                            startTrack = true
                        }
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        startTrack = false
                        goodTouch = false
                        seekBar?.let { sb ->
                            if (sb.max*0.9 < sb.progress ){
                                val prog = sb.progress
                                val animation: Animation = object : Animation() {
                                    protected override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                                        if (interpolatedTime == 1f) {
                                            sb.progress = sb.max
                                        } else {
                                            sb.progress = (prog + (sb.max - prog)*interpolatedTime).toInt()
                                        }
                                    }
                                    override fun willChangeBounds(): Boolean {
                                        return true
                                    }
                                }
                                animation.setAnimationListener(object : Animation.AnimationListener{
                                    override fun onAnimationStart(animation: Animation?) {
                                    }

                                    override fun onAnimationEnd(animation: Animation?) {
                                        listener?.invoke(item.podkey)
                                    }

                                    override fun onAnimationRepeat(animation: Animation?) {
                                    }

                                })
                                animation.duration = 100 // duration ?: (actualHeight / view.context.resources.displayMetrics.density * 1.3).toLong()
                                sb.startAnimation(animation)
                            }   else    {
                                val prog = sb.progress
                                val animation: Animation = object : Animation() {
                                    protected override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                                        if (interpolatedTime == 1f) {
                                            sb.progress = 0
                                        } else {
                                            sb.progress = (prog - prog*interpolatedTime).toInt()
                                        }
                                    }
                                    override fun willChangeBounds(): Boolean {
                                        return true
                                    }
                                }
                                animation.duration = 500 // duration ?: (actualHeight / view.context.resources.displayMetrics.density * 1.3).toLong()
                                sb.startAnimation(animation)
                            }
                        }
                    }

                })
            }
        }
    }
)