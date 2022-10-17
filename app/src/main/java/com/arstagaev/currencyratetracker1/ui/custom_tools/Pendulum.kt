package com.arstagaev.currencyratetracker1.ui.custom_tools

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate

@Composable
fun Pendulum(
    modifier: Modifier = Modifier,
    swingDuration: Int = 150,
    startX: Float = .4f,
    endX: Float = .6f,
    topY: Float = .2f,
    bottomY: Float = .2f,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()

    BoxWithConstraints(
        modifier = modifier
    ) {

        val start = -1f
        val end   =  1f
        val top = maxHeight * topY
        val bottom = maxHeight * bottomY

        val x by infiniteTransition.animateFloat(
            initialValue = start,
            targetValue =    end,
            animationSpec = infiniteRepeatable(
                animation = tween(swingDuration, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        Box(
            modifier = Modifier.rotate(x)
        ) {
            content()
        }
    }
}