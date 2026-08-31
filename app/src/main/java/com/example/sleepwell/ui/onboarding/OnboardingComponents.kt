package com.example.sleepwell.ui.onboarding

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleepwell.ui.theme.TextWhite
import kotlin.random.Random

@Composable
fun StarBackground(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "stars")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    val stars = remember {
        List(50) {
            Offset(Random.nextFloat(), Random.nextFloat())
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        stars.forEach { star ->
            drawCircle(
                color = Color.White.copy(alpha = alpha * Random.nextFloat()),
                radius = 2.dp.toPx(),
                center = Offset(star.x * size.width, star.y * size.height)
            )
        }
    }
}

@Composable
fun FloatingIllustration(content: @Composable () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "float")
    val translateY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "translateY"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .graphicsLayer(translationY = translateY),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun MoonIllustration() {
    Text(text = "🌙", fontSize = 100.sp)
}

@Composable
fun RobotIllustration() {
    Text(text = "🤖", fontSize = 100.sp)
}

@Composable
fun SleepingIllustration() {
    Text(text = "😴", fontSize = 100.sp)
}
