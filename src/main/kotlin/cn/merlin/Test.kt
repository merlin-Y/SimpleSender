package cn.merlin

import androidx.compose.animation.core.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PlanetOrbitAnimation(
    modifier: Modifier = Modifier,
    planetColor: Color = Color.Blue,
    ringColor: Color = Color.Gray,
    ringRadius: Float = 100f,
    orbitDuration: Int = 4000
) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(orbitDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size((ringRadius * 2).dp)
            .clip(MaterialTheme.shapes.medium)
            .background(Color.Transparent)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = ringColor,
                radius = ringRadius,
                style = Stroke(width = 2.dp.toPx())
            )
            val planetRadius = 10.dp.toPx()
            val radians = Math.toRadians(angle.toDouble())
            val x = (ringRadius * cos(radians)).toFloat() + size.width / 2
            val y = (ringRadius * sin(radians)).toFloat() + size.height / 2

            drawCircle(
                color = planetColor,
                radius = planetRadius,
                center = Offset(x, y)
            )
        }
    }
}

@Preview()
@Composable
fun PlanetOrbitAnimationPreview() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        PlanetOrbitAnimation(
            ringRadius = 100f,
            planetColor = Color.Blue,
            ringColor = Color.Gray,
            orbitDuration = 4000
        )
    }
}