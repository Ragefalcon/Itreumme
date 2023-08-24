package ru.ragefalcon.tutatores.extensions

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat

fun Activity.setWindowTransparency(
    listener: OnSystemBarsSizeChangedListener = { _, _ -> }
) {
    removeSystemInsets(window.decorView, listener)
    window.navigationBarColor = Color.TRANSPARENT
    window.statusBarColor = Color.TRANSPARENT

}
typealias OnSystemBarsSizeChangedListener =
            (statusBarSize: Int, navigationBarSize: Int) -> Unit

private fun View.isKeyboardAppeared(bottomInset: Int) =
    bottomInset / context.getResources().getDisplayMetrics().heightPixels.toDouble() > .25

fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int){
    if(view.layoutParams is ViewGroup.MarginLayoutParams){
        val screenDesity: Float = view.context.resources.displayMetrics.density
        val params: ViewGroup.MarginLayoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(left, top, right, bottom)
//        params.setMargins(left*screenDesity.toInt(), top*screenDesity.toInt(), right*screenDesity.toInt(), bottom*screenDesity.toInt())
        view.requestLayout()
    }
}
fun calculateDesiredBottomInset(
    view: View,
    topInset: Int,
    bottomInset: Int,
    listener: OnSystemBarsSizeChangedListener
): Int {
    val hasKeyboard = view.isKeyboardAppeared(bottomInset)
    val desiredBottomInset = if (hasKeyboard) bottomInset else 0
    listener(topInset, if (hasKeyboard) 0 else bottomInset)
    return desiredBottomInset
}

fun removeSystemInsets(view: View, listener: OnSystemBarsSizeChangedListener) {
    ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->

        val desiredBottomInset = calculateDesiredBottomInset(
            view,
            insets.systemWindowInsetTop,
            insets.systemWindowInsetBottom,
            listener
        )

        ViewCompat.onApplyWindowInsets(
            view,
            insets.replaceSystemWindowInsets(0, 0, 0, desiredBottomInset)
        )
    }

}