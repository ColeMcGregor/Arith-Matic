package com.wiseravenstudios.arithmatic.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseravenstudios.arithmatic.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val StudioAccent = Color(0xFFFFD66B)
private val StudioAccentSoft = Color(0xFFFFE59D)

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier
) {
    val logoAlpha = remember {
        Animatable(0f)
    }

    val logoScale = remember {
        Animatable(0.92f)
    }

    val titleAlpha = remember {
        Animatable(0f)
    }

    val glowStrength = remember {
        Animatable(0.15f)
    }

    LaunchedEffect(Unit) {
        launch {
            logoAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 650,
                    easing = FastOutSlowInEasing
                )
            )
        }

        launch {
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 750,
                    easing = FastOutSlowInEasing
                )
            )
        }

        delay(250L)

        launch {
            titleAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 600,
                    easing = FastOutSlowInEasing
                )
            )
        }

        delay(150L)

        glowStrength.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 850,
                easing = FastOutSlowInEasing
            )
        )

        glowStrength.animateTo(
            targetValue = 0.2f,
            animationSpec = tween(
                durationMillis = 900,
                easing = FastOutSlowInEasing
            )
        )
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.deep_blue_bg
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.studio_logo
                ),
                contentDescription = "Wise Raven Studios logo",
                modifier = Modifier
                    .fillMaxWidth(0.42f)
                    .widthIn(
                        min = 130.dp,
                        max = 260.dp
                    )
                    .sizeIn(
                        minWidth = 130.dp,
                        minHeight = 130.dp
                    )
                    .alpha(logoAlpha.value)
                    .scale(logoScale.value),
                contentScale = ContentScale.Fit
            )

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            Text(
                text = "WISE RAVEN STUDIOS",
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(titleAlpha.value),
                color = StudioAccentSoft,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 3.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                style = TextStyle(
                    shadow = Shadow(
                        color = StudioAccent.copy(
                            alpha = glowStrength.value
                        ),
                        offset = Offset.Zero,
                        blurRadius =
                            8f + (22f * glowStrength.value)
                    )
                )
            )
        }
    }
}