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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.wiseravenstudios.arithmatic.R
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import kotlin.math.roundToInt

enum class ChalkButtonState {
    Normal,
    Selected,
    Correct,
    Incorrect,

    /**
     * Used for an unselected answer after the player has made a choice.
     *
     * The button remains visible but cannot be selected.
     */
    Locked,

    /**
     * Used when the control itself is unavailable.
     */
    Disabled
}

@Composable
fun ChalkButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: ChalkButtonState = ChalkButtonState.Normal,
    enabled: Boolean = true,
    borderColor: Color = ChalkColors.ChalkWhite,
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

    /*
     * Create an independent mutable copy of the Android drawable.
     *
     * Keeping this as a Drawable preserves NinePatch behavior. It is not
     * converted into an ImageBitmap or stretched as a plain bitmap.
     */
    val background: Drawable = remember(context, backgroundRes) {
        val original = requireNotNull(
            ContextCompat.getDrawable(context, backgroundRes)
        ) {
            "Unable to load chalk button drawable: $backgroundRes"
        }

        original.constantState
            ?.newDrawable(context.resources)
            ?.mutate()
            ?: original.mutate()
    }

    val isLocked =
        state == ChalkButtonState.Correct ||
                state == ChalkButtonState.Incorrect ||
                state == ChalkButtonState.Locked ||
                state == ChalkButtonState.Disabled

    val isInteractive = enabled && !isLocked

    /*
     * Tint determines the border's color.
     *
     * Alpha is calculated separately below so changing transparency does not
     * replace or recalculate the chosen tint.
     */
    val resolvedBorderColor = when (state) {
        ChalkButtonState.Normal -> borderColor
        ChalkButtonState.Selected -> ChalkColors.PastelBlue
        ChalkButtonState.Correct -> ChalkColors.PastelGreen
        ChalkButtonState.Incorrect -> ChalkColors.PastelPink
        ChalkButtonState.Locked -> borderColor
        ChalkButtonState.Disabled -> borderColor
    }

    val borderAlpha = when {
        !enabled -> 0.34f
        state == ChalkButtonState.Disabled -> 0.34f
        state == ChalkButtonState.Locked -> 0.28f
        pressed && isInteractive -> 0.72f
        state == ChalkButtonState.Selected -> 0.92f
        else -> 1f
    }

    /*
     * Content alpha is independent from the drawable alpha.
     *
     * A pressed button keeps its text readable while the frame dims and the
     * entire control contracts slightly.
     */
    val contentAlpha = when {
        !enabled -> 0.42f
        state == ChalkButtonState.Disabled -> 0.42f
        state == ChalkButtonState.Locked -> 0.42f
        else -> 1f
    }

    val contentColor = when (state) {
        ChalkButtonState.Normal -> borderColor
        ChalkButtonState.Selected -> ChalkColors.PastelBlue
        ChalkButtonState.Correct -> ChalkColors.PastelGreen
        ChalkButtonState.Incorrect -> ChalkColors.PastelPink
        ChalkButtonState.Locked -> borderColor
        ChalkButtonState.Disabled -> borderColor
    }

    val pressedScale =
        if (pressed && isInteractive) {
            0.975f
        } else {
            1f
        }

    Box(
        modifier = modifier
            .defaultMinSize(
                minWidth = 120.dp,
                minHeight = 56.dp
            )
            .graphicsLayer {
                scaleX = pressedScale
                scaleY = pressedScale
            }
            .drawBehind {
                DrawableCompat.setTint(
                    background,
                    resolvedBorderColor.toArgb()
                )

                background.alpha =
                    (borderAlpha * 255f)
                        .roundToInt()
                        .coerceIn(0, 255)

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
            LocalContentColor provides contentColor.copy(
                alpha = contentAlpha
            )
        ) {
            content()
        }
    }
}