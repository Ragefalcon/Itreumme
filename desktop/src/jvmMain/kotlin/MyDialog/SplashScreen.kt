package MyDialog

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen() {

    val keyFirst = remember { mutableStateOf(true) }
    val keyDial = remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    if (keyDial.value) {

        val keyVisSapere = remember { mutableStateOf(false) }
        val keyVisDial = remember { mutableStateOf(true) }
        val keyVisDial2 = remember { mutableStateOf(true) }

        val alpha: Float by animateFloatAsState(
            targetValue = if (keyVisDial.value) 1f else 0.0f,
            animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
        ) { if (!keyVisDial.value) keyDial.value = false }

        val alpha2: Float by animateFloatAsState(
            targetValue = if (keyVisDial2.value) 1f else 0.0f,
            animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
        ) { if (!keyVisDial2.value) keyDial.value = false }

        val alphaItreum: Float by animateFloatAsState(
            targetValue = if (keyVisSapere.value) 1f else 0.3f,
            animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
        )

        val alphaSapere: Float by animateFloatAsState(
            targetValue = if (keyVisSapere.value) 1f else 0.3f,
            animationSpec = infiniteRepeatable(
                tween(
                    durationMillis = 700,
                    easing = FastOutSlowInEasing
                ), repeatMode = RepeatMode.Reverse
            )
        ) {
        }

        Box(
            Modifier.fillMaxSize().alpha(alpha2).clickable {
                keyVisDial2.value = false
            },
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier.fillMaxSize().alpha(alpha),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource("background_01.jpg"),
                    contentDescription = "Sample",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource("itreumme.png"),
                        contentDescription = "Sample",
                        modifier = Modifier.width(500.dp).alpha(alphaItreum),
                    )
                    Spacer(Modifier.height(30.dp))
                    Image(
                        painter = painterResource("sapere_aude.png"),
                        contentDescription = "Sample",
                        modifier = Modifier.width(350.dp).alpha(alphaSapere),
                    )
                }
            }
        }
        if (keyFirst.value) {
            keyFirst.value = false
            coroutineScope.launch {
                keyVisSapere.value = true
                delay(5000)
                keyVisDial.value = false
            }
        }
    }
}
