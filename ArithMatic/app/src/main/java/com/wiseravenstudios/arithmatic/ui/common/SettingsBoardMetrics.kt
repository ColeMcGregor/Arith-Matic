package com.wiseravenstudios.arithmatic.ui.settings

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SettingsBoardMetrics(
    val useDoubleColumn: Boolean,

    val horizontalPadding: Dp,
    val verticalPadding: Dp,

    val sectionSpacing: Dp,
    val controlSpacing: Dp,
    val contentColumnSpacing: Dp,

    val cardHorizontalPadding: Dp,
    val cardVerticalPadding: Dp,
    val cardCornerRadius: Dp,

    val controlHorizontalPadding: Dp,
    val controlVerticalPadding: Dp,

    val minimumTouchTarget: Dp,

    val titleFontSize: TextUnit,
    val tabFontSize: TextUnit,
    val sectionTitleFontSize: TextUnit,
    val valueFontSize: TextUnit,
    val toggleFontSize: TextUnit,
    val stepFontSize: TextUnit,
    val backFontSize: TextUnit,
    val statusFontSize: TextUnit,
    val statusLineHeight: TextUnit
)

fun createSettingsBoardMetrics(
    boardWidth: Dp,
    boardHeight: Dp
): SettingsBoardMetrics {
    val widthValue =
        boardWidth.value.coerceAtLeast(1f)

    val heightValue =
        boardHeight.value.coerceAtLeast(1f)

    val aspectRatio =
        widthValue / heightValue

    val limitingDimension =
        minOf(
            widthValue,
            heightValue
        )

    val scale =
        (
                limitingDimension /
                        BASE_REFERENCE_DIMENSION
                )
            .coerceAtLeast(
                MINIMUM_SCALE
            )

    return SettingsBoardMetrics(
        useDoubleColumn =
            aspectRatio >
                    DOUBLE_COLUMN_ASPECT_RATIO,

        horizontalPadding =
            scaledDp(
                base = 12f,
                scale = scale,
                minimum = 8f
            ),

        verticalPadding =
            scaledDp(
                base = 10f,
                scale = scale,
                minimum = 8f
            ),

        sectionSpacing =
            scaledDp(
                base = 18f,
                scale = scale,
                minimum = 12f
            ),

        controlSpacing =
            scaledDp(
                base = 12f,
                scale = scale,
                minimum = 8f
            ),

        contentColumnSpacing =
            scaledDp(
                base = 18f,
                scale = scale,
                minimum = 12f
            ),

        cardHorizontalPadding =
            scaledDp(
                base = 14f,
                scale = scale,
                minimum = 10f
            ),

        cardVerticalPadding =
            scaledDp(
                base = 12f,
                scale = scale,
                minimum = 10f
            ),

        cardCornerRadius =
            scaledDp(
                base = 10f,
                scale = scale,
                minimum = 8f
            ),

        controlHorizontalPadding =
            scaledDp(
                base = 14f,
                scale = scale,
                minimum = 10f
            ),

        controlVerticalPadding =
            scaledDp(
                base = 6f,
                scale = scale,
                minimum = 4f
            ),

        minimumTouchTarget = 48.dp,

        titleFontSize =
            scaledSp(
                base = 32f,
                scale = scale,
                minimum = 28f
            ),

        tabFontSize =
            scaledSp(
                base = 15f,
                scale = scale,
                minimum = 11f
            ),

        sectionTitleFontSize =
            scaledSp(
                base = 24f,
                scale = scale,
                minimum = 19f
            ),

        valueFontSize =
            scaledSp(
                base = 21f,
                scale = scale,
                minimum = 17f
            ),

        toggleFontSize =
            scaledSp(
                base = 15f,
                scale = scale,
                minimum = 12f
            ),

        stepFontSize =
            scaledSp(
                base = 30f,
                scale = scale,
                minimum = 24f
            ),

        backFontSize =
            scaledSp(
                base = 29f,
                scale = scale,
                minimum = 24f
            ),

        statusFontSize =
            scaledSp(
                base = 22f,
                scale = scale,
                minimum = 18f
            ),

        statusLineHeight =
            scaledSp(
                base = 28f,
                scale = scale,
                minimum = 23f
            )
    )
}

private fun scaledDp(
    base: Float,
    scale: Float,
    minimum: Float
): Dp {
    return (
            base * scale
            )
        .coerceAtLeast(
            minimum
        )
        .dp
}

private fun scaledSp(
    base: Float,
    scale: Float,
    minimum: Float
): TextUnit {
    return (
            base * scale
            )
        .coerceAtLeast(
            minimum
        )
        .sp
}

private const val BASE_REFERENCE_DIMENSION = 400f

private const val MINIMUM_SCALE = 0.75f

private const val DOUBLE_COLUMN_ASPECT_RATIO = 1.25f