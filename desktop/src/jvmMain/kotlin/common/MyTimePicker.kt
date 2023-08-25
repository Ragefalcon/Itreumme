package common

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import extensions.BoxWithVScrollBarLazyList
import extensions.format
import kotlinx.coroutines.launch
import java.util.*


class MyTimePicker(val datePick: MutableState<Date>, val expandedOut: MutableState<Boolean>? = null) {

    val expanded: MutableState<Boolean> = mutableStateOf(false)
        get() = expandedOut ?: field

    var keyFirstStart = true
    var keyFirstStart2 = true
    val calendar = Calendar.getInstance().apply {
        time = datePick.value
    }


    @Composable
    fun show() {

        val heightScroll: Dp by animateDpAsState(
            targetValue = if (expanded.value) 120.dp else 0.dp,
            animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
        ) {

        }
        val listState = rememberLazyListState()
        val listState2 = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            MyTextButtStyle1(datePick.value.format("HH:mm")) {
                expanded.value = expanded.value.not()
            }

            Row {
                BoxWithVScrollBarLazyList(
                    modifier = Modifier.height(heightScroll).width(38.dp),
                    listState
                ) { scrollState ->
                    LazyColumn(state = scrollState) {
                        for (i in 0..23) {
                            item {
                                Surface(
                                    modifier = Modifier.padding(2.dp),
                                    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                    color = if (i == calendar.get(Calendar.HOUR_OF_DAY)) Color(0xFF2B2B2B) else Color.Transparent
                                ) {
                                    Row(
                                        Modifier
                                            .clickable(remember(::MutableInteractionSource), indication = null) {
                                                calendar.set(Calendar.HOUR_OF_DAY, i)
                                                datePick.value = calendar.time
                                            }.border(
                                                width = 1.dp,
                                                brush = Brush.horizontalGradient(
                                                    listOf(
                                                        Color(0xFF888888),
                                                        Color(0xFF888888)
                                                    )
                                                ),
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .padding(4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Text(
                                            text = "${if (i < 10) "0" else ""}$i",
                                            color = if (i == calendar.get(Calendar.HOUR_OF_DAY)) Color(0xFFFFF7D9) else Color(
                                                0xAFFFF7D9
                                            ),
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(1.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }.apply {
                        if (keyFirstStart) {
                            coroutineScope.launch {
                                val xT = calendar.get(Calendar.HOUR_OF_DAY)
                                if ((xT > 2) && (xT < 25)) listState.scrollToItem(xT)
                            }
                            keyFirstStart = false
                        }
                    }
                }
                Spacer(Modifier.width(5.dp))
                BoxWithVScrollBarLazyList(
                    modifier = Modifier.height(heightScroll).width(38.dp),
                    listState2
                ) { scrollState ->
                    LazyColumn(state = scrollState) {
                        for (i in 0..59) {
                            item {
                                Surface(
                                    modifier = Modifier.padding(2.dp),
                                    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                                    color = if (i == calendar.get(Calendar.MINUTE)) Color(0xFF2B2B2B) else Color.Transparent
                                ) {
                                    Row(
                                        Modifier
                                            .clickable(remember(::MutableInteractionSource), indication = null) {
                                                calendar.set(Calendar.MINUTE, i)
                                                datePick.value = calendar.time
                                            }.border(
                                                width = 1.dp,
                                                brush = Brush.horizontalGradient(
                                                    listOf(
                                                        Color(0xFF888888),
                                                        Color(0xFF888888)
                                                    )
                                                ),
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .padding(4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Text(
                                            text = "${if (i < 10) "0" else ""}$i",
                                            color = if (i == calendar.get(Calendar.MINUTE)) Color(0xFFFFF7D9) else Color(
                                                0xAFFFF7D9
                                            ),
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(1.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }.apply {
                        if (keyFirstStart2) {
                            coroutineScope.launch {
                                val xT = calendar.get(Calendar.MINUTE)
                                if ((xT > 2) && (xT < 25)) listState2.scrollToItem(xT)
                            }
                            keyFirstStart2 = false
                        }
                    }
                }
            }
        }
    }
}