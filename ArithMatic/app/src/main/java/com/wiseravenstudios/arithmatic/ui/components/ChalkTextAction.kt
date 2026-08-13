package com.wiseravenstudios.arithmatic.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.defaultMinSize
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
import com.wiseravenstudios.arithmatic.ui.common.BoardResponsiveMetrics
import com.wiseravenstudios.arithmatic.ui.common.BoardTextRole
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic

/**
 * Clickable chalk-styled text action.
 *
 * When responsive metrics are supplied, text size, padding, and minimum
 * touch size are derived from the current writable board area.
 */
@Composable
fun ChalkTextAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = ChalkColors.ChalkWhite,

    metrics: BoardResponsiveMetrics? = null,
    textRole: BoardTextRole =
        BoardTextRole.PrimaryAction,

    /*
     * An explicit font size overrides the responsive role.
     *
     * This remains available during the responsive-layout migration and for
     * unusual controls that intentionally require a specific size.
     */
    fontSize: TextUnit? = null,

    fontFamily: FontFamily = Chalktastic,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Center,

    /*
     * Explicit padding values override responsive action padding.
     */
    paddingStart: Dp? = null,
    paddingTop: Dp? = null,
    paddingEnd: Dp? = null,
    paddingBottom: Dp? = null
) {
    val interactionSource =
        remember {
            MutableInteractionSource()
        }

    val isHovered by
    interactionSource
        .collectIsHoveredAsState()

    val isPressed by
    interactionSource
        .collectIsPressedAsState()

    val displayedColor by
    animateColorAsState(
        targetValue =
            when {
                !enabled ->
                    color.copy(
                        alpha = 0.35f
                    )

                isPressed ->
                    color.copy(
                        alpha = 0.72f
                    )

                isHovered ->
                    color.copy(
                        alpha = 0.86f
                    )

                else ->
                    color
            },
        animationSpec =
            tween(
                durationMillis = 120
            ),
        label =
            "chalkTextActionColor"
    )

    val displayedScale by
    animateFloatAsState(
        targetValue =
            when {
                !enabled ->
                    1f

                isPressed ->
                    0.96f

                isHovered ->
                    1.05f

                else ->
                    1f
            },
        animationSpec =
            tween(
                durationMillis = 120
            ),
        label =
            "chalkTextActionScale"
    )

    val resolvedFontSize =
        fontSize
            ?: metrics?.textSize(
                textRole
            )
            ?: DEFAULT_FONT_SIZE

    val resolvedHorizontalPadding =
        metrics
            ?.actionHorizontalPadding
            ?: DEFAULT_HORIZONTAL_PADDING

    val resolvedVerticalPadding =
        metrics
            ?.actionVerticalPadding
            ?: DEFAULT_VERTICAL_PADDING

    val resolvedPaddingStart =
        paddingStart
            ?: resolvedHorizontalPadding

    val resolvedPaddingTop =
        paddingTop
            ?: resolvedVerticalPadding

    val resolvedPaddingEnd =
        paddingEnd
            ?: resolvedHorizontalPadding

    val resolvedPaddingBottom =
        paddingBottom
            ?: resolvedVerticalPadding

    val minimumTouchTarget =
        metrics
            ?.minimumTouchTarget
            ?: DEFAULT_MINIMUM_TOUCH_TARGET

    Text(
        text = text,
        color =
            displayedColor,
        fontFamily =
            fontFamily,
        fontSize =
            resolvedFontSize,
        fontWeight =
            fontWeight,
        textAlign =
            textAlign,
        modifier = modifier
            .scale(
                displayedScale
            )
            .defaultMinSize(
                minWidth =
                    minimumTouchTarget,
                minHeight =
                    minimumTouchTarget
            )
            .clickable(
                enabled =
                    enabled,
                role =
                    Role.Button,
                interactionSource =
                    interactionSource,
                indication =
                    null,
                onClick =
                    onClick
            )
            .padding(
                start =
                    resolvedPaddingStart,
                top =
                    resolvedPaddingTop,
                end =
                    resolvedPaddingEnd,
                bottom =
                    resolvedPaddingBottom
            )
    )
}

private val DEFAULT_FONT_SIZE =
    34.sp

private val DEFAULT_HORIZONTAL_PADDING =
    10.dp

private val DEFAULT_VERTICAL_PADDING =
    8.dp

private val DEFAULT_MINIMUM_TOUCH_TARGET =
    48.dp