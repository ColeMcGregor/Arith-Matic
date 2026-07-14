package com.wiseravenstudios.arithmatic.ui.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic

@Composable
fun ChalkTextAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = ChalkColors.ChalkWhite,
    fontSize: TextUnit = 34.sp,
    fontFamily: FontFamily = Chalktastic,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Center,
    paddingStart: Dp = 10.dp,
    paddingTop: Dp = 8.dp,
    paddingEnd: Dp = 10.dp,
    paddingBottom: Dp = 8.dp
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val displayedColor by animateColorAsState(
        targetValue = when {
            !enabled -> color.copy(alpha = 0.35f)
            isPressed -> color.copy(alpha = 0.72f)
            isHovered -> color.copy(alpha = 0.86f)
            else -> color
        },
        animationSpec = tween(durationMillis = 120),
        label = "chalkTextActionColor"
    )

    val displayedScale by animateFloatAsState(
        targetValue = when {
            !enabled -> 1f
            isPressed -> 0.96f
            isHovered -> 1.05f
            else -> 1f
        },
        animationSpec = tween(durationMillis = 120),
        label = "chalkTextActionScale"
    )

    Text(
        text = text,
        color = displayedColor,
        fontFamily = fontFamily,
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign,
        modifier = modifier
            .scale(displayedScale)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(
                start = paddingStart,
                top = paddingTop,
                end = paddingEnd,
                bottom = paddingBottom
            )
    )
}