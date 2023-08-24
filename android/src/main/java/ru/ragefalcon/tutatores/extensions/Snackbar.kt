package ru.ragefalcon.tutatores.extensions

import android.graphics.Color
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.Snackbar
import ru.ragefalcon.tutatores.R

fun Snackbar.config():Snackbar{
    val color = Color.parseColor("#FFFFFF")
//        if (ru.skillbranch.devintensive.repositories.PreferencesRepository.getAppTheme() == androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO) {
////        R.color.color_white
//        Color.parseColor("#FFFFFF")
//    } else {
//      //  R.color.color_BG_dark
//        Color.parseColor("#121212")
//    }
//    this.view.setBackgroundResource(R.drawable.bg_snackbar)
    val snackViewText = this.view.findViewById(R.id.snackbar_text) as TextView
    snackViewText.setTextColor(color)
//            val snackie =
//                Snackbar.make(rv_chat_list, "Вы точно хотите добавить ${it.title} в архив?", Snackbar.LENGTH_LONG)
//            val snackView = snackie.getView()
//            val snackViewText = snackView.findViewById(ru.skillbranch.devintensive.R.id.snackbar_text) as TextView
//            val snackViewButton = snackView.findViewById(android.support.design.R.id.snackbar_action) as Button
//            snackView.setBackgroundColor(BACKGROUND_COLOR.toInt())
//            snackViewText.setTextColor(TEXT_COLOR)
//            snackViewButton.setTextColor(BUTTON_COLOR)
//            val color = if (PreferencesRepository.getAppTheme() == AppCompatDelegate.MODE_NIGHT_NO) {
//                resources.getColor(R.color.color_white, theme)
//            } else {
//                resources.getColor(R.color.color_BG_dark, theme)
//            }
//    val sb = Snackbar.make(rv_chat_list, "Вы точно хотите добавить ${it.title} в архив?", Snackbar.LENGTH_LONG)
//            sb.view.setBackgroundResource(R.drawable.bg_snackbar)
//            val snackViewText = sb.view.findViewById(ru.skillbranch.devintensive.R.id.snackbar_text) as TextView
//            snackViewText.setTextColor(color)
//    sb.setAction("Отмена", MyUndoListener())
//        .config()
    //  .setActionTextColor(color)
//=======================
//    val params = this.view.layoutParams  as CoordinatorLayout.LayoutParams //.MarginLayoutParams
//    params.setMargins(120, 120, 120, 120)
//    params.gravity = Gravity.BOTTOM
////    params.anchorGravity = Gravity.BOTTOM
////   params.
//    this.view.layoutParams = params
////
    this.view.background = context.getDrawable(R.drawable.bg_snackbar)

    ViewCompat.setElevation(this.view, 60f)
    return this
}