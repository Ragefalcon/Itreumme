package extensions

import kotlin.math.atan2

fun getAngle(x: Float, y: Float): Double {
    return 0.5 * Math.PI - atan2( //
        x.toDouble(),
        y.toDouble()
    ) //note the atan2 call, the order of paramers is y then x
}
