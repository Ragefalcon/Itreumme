package ru.ragefalcon.tutatores.extensions

import android.content.res.Resources

val Int.pxToDp: Int
    get () = (this / Resources.getSystem (). displayMetrics.density) .toInt ()
val Int.dpToPx: Int
    get () = (this * Resources.getSystem (). displayMetrics.density) .toInt ()
val Int.px: Int
    get () = (this * Resources.getSystem (). displayMetrics.density) .toInt ()
val Int.pxF: Float
    get () = (this * Resources.getSystem (). displayMetrics.density)