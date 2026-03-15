package com.popop.lifesimulator.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToMainMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(1000)
        )
        delay(2000)
        onNavigateToMainMenu()
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .scale(scale.value)
                .alpha(alpha.value)
        ) {
            Text(
                text = "ULTIMATE",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
            Text(
                text = "LIFE SIMULATOR",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
            Text(
                text = "Royalty • Politics • Crime • Business • Career",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
