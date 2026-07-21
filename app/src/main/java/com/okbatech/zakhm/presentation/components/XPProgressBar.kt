package com.okbatech.zakhm.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun XPProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 10.dp,
    trackColor: Color? = null,
    barColor: Brush? = null
) {
    val resolvedTrack = trackColor ?: MaterialTheme.colorScheme.surfaceContainerHigh
    val tertiary = MaterialTheme.colorScheme.tertiary
    val resolvedBar = barColor ?: Brush.horizontalGradient(
        listOf(tertiary.copy(alpha = 0.6f), tertiary)
    )

    var animTarget by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(progress) { animTarget = progress }
    val animated by animateFloatAsState(animTarget, tween(800), label = "xp")

    Box(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(resolvedTrack)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animated.coerceIn(0f, 1f))
                .clip(RoundedCornerShape(50))
                .background(resolvedBar)
        )
    }
}
