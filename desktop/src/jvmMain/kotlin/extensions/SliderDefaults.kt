package extensions

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

@Composable
fun getMySliderColor(thumb: Color, inactive: Color): SliderColors = SliderDefaults.colors(
    thumbColor = thumb,
    disabledThumbColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
        .compositeOver(MaterialTheme.colors.surface),
    activeTrackColor = thumb,
    inactiveTrackColor = inactive,
    disabledActiveTrackColor = MaterialTheme.colors.onSurface.copy(alpha = SliderDefaults.DisabledActiveTrackAlpha),
    disabledInactiveTrackColor = MaterialTheme.colors.onSurface.copy(
        alpha = SliderDefaults.DisabledInactiveTrackAlpha
    ),
    activeTickColor = contentColorFor(Color.Blue).copy(alpha = SliderDefaults.TickAlpha),
    inactiveTickColor = Color.Magenta.copy(alpha = SliderDefaults.TickAlpha),
    disabledActiveTickColor = contentColorFor(thumb).copy(alpha = SliderDefaults.TickAlpha)
        .copy(alpha = SliderDefaults.DisabledTickAlpha),
    disabledInactiveTickColor = MaterialTheme.colors.onSurface.copy(
        alpha = SliderDefaults.DisabledInactiveTrackAlpha
    )
        .copy(alpha = SliderDefaults.DisabledTickAlpha)
)
