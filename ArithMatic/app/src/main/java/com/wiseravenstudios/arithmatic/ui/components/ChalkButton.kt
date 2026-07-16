package com.wiseravenstudios.arithmatic.ui.components

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.wiseravenstudios.arithmatic.R
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import kotlin.math.roundToInt

enum class ChalkButtonState {
    Normal,
    Selected,
    Correct,
    Incorrect,
    Disabled
}

@Composable
fun ChalkButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: ChalkButtonState = ChalkButtonState.Normal,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 24.dp,
        vertical = 12.dp
    ),
    @DrawableRes backgroundRes: Int = R.drawable.chalk_button,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val background: Drawable = remember(context, backgroundRes) {
        requireNotNull(
            ContextCompat.getDrawable(context, backgroundRes)
        ) {
            "Unable to load chalk button drawable: $backgroundRes"
        }.mutate()
    }

    val isInteractive =
        enabled &&
                state != ChalkButtonState.Correct &&
                state != ChalkButtonState.Incorrect &&
                state != ChalkButtonState.Disabled

    val stateColor = when (state) {
        ChalkButtonState.Normal -> Color.White
        ChalkButtonState.Selected -> ChalkColors.PastelBlue
        ChalkButtonState.Correct -> ChalkColors.PastelGreen
        ChalkButtonState.Incorrect -> ChalkColors.PastelPink
        ChalkButtonState.Disabled -> Color.White
    }

    val stateAlpha = when {
        state == ChalkButtonState.Disabled || !enabled -> 0.40f
        pressed -> 0.72f
        state == ChalkButtonState.Selected -> 0.90f
        else -> 1f
    }

    Box(
        modifier = modifier
            .defaultMinSize(
                minWidth = 120.dp,
                minHeight = 56.dp
            )
            .drawBehind {
                background.setTint(stateColor.toArgb())
                background.alpha = (stateAlpha * 255).roundToInt()

                background.setBounds(
                    0,
                    0,
                    size.width.roundToInt(),
                    size.height.roundToInt()
                )

                drawIntoCanvas { canvas ->
                    background.draw(canvas.nativeCanvas)
                }
            }
            .clickable(
                enabled = isInteractive,
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick
            )
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(
            LocalContentColor provides stateColor.copy(alpha = stateAlpha)
        ) {
            content()
        }
    }
}