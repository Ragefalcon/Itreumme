package ru.ragefalcon.tutatores.extensions

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.TextView
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.recyclerview.widget.RecyclerView

fun getMyAnimListener(
    aEnd: (View?) -> Unit = {},
    aCancel: (View?) -> Unit = {},
    aStart: (View?) -> Unit = {}
): ViewPropertyAnimatorListener {

    return object : ViewPropertyAnimatorListener {
        override fun onAnimationEnd(view: View?) {
            aEnd(view)
        }

        override fun onAnimationCancel(view: View?) {
            aCancel(view)
        }

        override fun onAnimationStart(view: View?) {
            aStart(view)
        }

    }
}

fun expand(view: View, ff: () -> Unit = {}, height: Int? = null, duration: Long? = null) {
    val matchParentMeasureSpec: Int =
        View.MeasureSpec.makeMeasureSpec((view.getParent() as View).width, View.MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    view.measure(matchParentMeasureSpec, wrapContentMeasureSpec)

    val actualheight: Int = height ?: view.measuredHeight
    view.layoutParams.height = 1
    view.visibility = View.VISIBLE
    val animation: Animation = object : Animation() {
        protected override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            view.layoutParams.height =
                if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT
                else if ((actualheight * interpolatedTime).toInt() == 0) 1
                else (actualheight * interpolatedTime).toInt()
            view.requestLayout()
            if (interpolatedTime == 1f) {
                ff()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }
    animation.duration =
        duration ?: (actualheight / view.getContext().getResources().getDisplayMetrics().density * 1.3).toLong()
    view.startAnimation(animation)
}

private fun expandAction(view: View, ff: () -> Unit = {}): Animation {
    view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    val actualheight: Int = view.measuredHeight
    view.layoutParams.height = 0
    view.visibility = View.VISIBLE
    val animation: Animation = object : Animation() {
        protected override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            view.layoutParams.height =
                if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (actualheight * interpolatedTime).toInt()
            view.requestLayout()
            if (interpolatedTime == 1f) ff()
        }
    }
    animation.duration = (actualheight / view.getContext().getResources().getDisplayMetrics().density * 1.3).toLong()
    return animation
}

fun collapse(view: View, ff: () -> Unit = {}, duration: Long? = null) {
    val actualHeight: Int = view.measuredHeight
    val animation: Animation = object : Animation() {
        protected override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                view.layoutParams.height = 1
                view.requestLayout()
                ff()
            } else {
                view.layoutParams.height = actualHeight - ((actualHeight - 0) * interpolatedTime).toInt()
                view.requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
        }

        override fun onAnimationEnd(animation: Animation?) {
            view.layoutParams.height = 1
            view.requestLayout()
            view.parent.requestLayout()
        }

        override fun onAnimationRepeat(animation: Animation?) {
        }

    })
    animation.duration = duration ?: (actualHeight / view.context.resources.displayMetrics.density * 1.3).toLong()
    view.startAnimation(animation)
}


fun expandWidth(view: View, ff: () -> Unit = {}, width: Int = 500) {
    view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    val actWidth: Int = width
    view.layoutParams.width = 1
    view.visibility = View.VISIBLE
    val animation: Animation = object : Animation() {
        protected override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            view.layoutParams.width =
                if (interpolatedTime == 1f) width
                else if ((actWidth * interpolatedTime).toInt() == 0) 1
                else (actWidth * interpolatedTime).toInt()
            view.requestLayout()
            if (interpolatedTime == 1f) ff()
        }
    }
    animation.duration = (200)
    view.startAnimation(animation)
}

fun collapseWidth(view: View, ff: () -> Unit = {}, width: Int = 500) {
    val actWidth: Int = width
    val animation: Animation = object : Animation() {
        protected override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                view.layoutParams.width = 1
                view.requestLayout()
                ff()
                view.setVisibility(View.INVISIBLE)
            } else {
                view.layoutParams.width = actWidth - ((actWidth - 1) * interpolatedTime).toInt()
                view.requestLayout()
            }
        }
    }
    animation.duration = (200)
    view.startAnimation(animation)
}

fun sverOpis(mRecyclerView: RecyclerView, pos: Int, tview: TextView, sver: Boolean, anim: Boolean) {
    /**
     * Я не очень понял пока почему все именно так, но походу если не
     * перемежать сворачивание/разворачивание какой-нибудь анимацией, на вроде этой:
     * tview.animate().setDuration(0).rotation(0F)
     * на этом же или на другом объекте из Item-а RecyclerView, то
     * разворачивание текста происходит только один раз, до следующего
     * какого то действия которое сбросит что то из прорисовки...
     * Возможно она отслеживает, что такая анимация уже была выполнена
     * или еще что, может сам объект анимации каким то образом не обновляется.
     * У меня не получилось отследить проблему до ее корня...
     * */
    tview.animate().setDuration(0).rotation(0F)

    if (tview.text == "" || sver) {
        if (anim) {
            val originalPos = IntArray(2)
            tview.getLocationInWindow(originalPos)
            val originalPosRV = IntArray(2)
            mRecyclerView.getLocationInWindow(originalPosRV)
            if (originalPos[1] - originalPosRV[1] < 0) mRecyclerView.smoothScrollToPosition(pos)
            collapse(tview)
        } else {
            tview.layoutParams.height = 1
            tview.requestLayout()
        }
    } else {
        if (anim) {
            expand(tview)
        } else {
            tview.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            tview.visibility = View.VISIBLE
            tview.requestLayout()
        }
    }
}

fun rotateElemItem(rview: View, sver: Boolean, anim: Boolean, ugol: Float) {

    if (sver) {
        if (anim) {
            rview.animate().setDuration(200).rotation(0F)
        } else {
            rview.animate().setDuration(0).rotation(0F)
        }
    } else {
        if (anim) {
            rview.animate().setDuration(200).rotation(ugol)
        } else {
            rview.animate().setDuration(0).rotation(ugol)
        }
    }
}

fun sverWidthElemItem(wview: View, sver: Boolean, anim: Boolean, width: Int = 300) {

    if (sver) {
        if (anim) {
            collapseWidth(wview, width = width)
        } else {
            wview.layoutParams.width = 1
            wview.visibility = View.INVISIBLE
        }
    } else {
        if (anim) {
            expandWidth(wview, width = width)
        } else {
            wview.layoutParams.width = width
            wview.visibility = View.VISIBLE
        }
    }
}
