package com.wiseravenstudios.arithmatic.ui.statistics

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal enum class MyStatsLayoutMode {
    Vertical,
    Horizontal
}

internal data class MyStatsBoardMetrics(
    val layoutMode: MyStatsLayoutMode,

    val horizontalPadding: Dp,
    val verticalPadding: Dp,

    val titleSize: TextUnit,
    val titleSpacing: Dp,

    val headerSpacing: Dp,

    val tabSpacing: Dp,
    val tabFontSize: TextUnit,
    val tabHorizontalPadding: Dp,
    val selectedTabVerticalPadding: Dp,
    val tabVerticalPadding: Dp,
    val tabCornerRadius: Dp,

    val contentSpacing: Dp,

    val cardHorizontalPadding: Dp,
    val cardVerticalPadding: Dp,
    val cardCornerRadius: Dp,
    val cardTitleSize: TextUnit,
    val cardTitleSpacing: Dp,

    val statisticTextSize: TextUnit,
    val statisticRowVerticalPadding: Dp,

    val statusTextSize: TextUnit,
    val statusLineHeight: TextUnit,

    val emptyTitleSize: TextUnit,
    val emptyBodySize: TextUnit,
    val emptyBodyLineHeight: TextUnit,
    val emptySpacing: Dp,

    val backSize: TextUnit,
    val backTopPadding: Dp,
    val backBottomPadding: Dp,

    val contentBottomPadding: Dp
)

internal fun calculateMyStatsBoardMetrics(
    width: Dp,
    height: Dp
): MyStatsBoardMetrics {
    val widthValue =
        width.value
            .coerceAtLeast(1f)

    val heightValue =
        height.value
            .coerceAtLeast(1f)

    val aspectRatio =
        widthValue /
                heightValue

    val layoutMode =
        if (
            aspectRatio >=
            HORIZONTAL_LAYOUT_ASPECT_RATIO
        ) {
            MyStatsLayoutMode.Horizontal
        } else {
            MyStatsLayoutMode.Vertical
        }

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

    return MyStatsBoardMetrics(
        layoutMode =
            layoutMode,

        horizontalPadding =
            (10f * scale).dp,

        verticalPadding =
            (8f * scale).dp,

        titleSize =
            (31f * scale).sp,

        titleSpacing =
            (14f * scale).dp,

        headerSpacing =
            (10f * scale).dp,

        tabSpacing =
            (4f * scale).dp,

        tabFontSize =
            (10f * scale).sp,

        tabHorizontalPadding =
            (1f * scale).dp,

        selectedTabVerticalPadding =
            (6f * scale).dp,

        tabVerticalPadding =
            (4f * scale).dp,

        tabCornerRadius =
            (8f * scale).dp,

        contentSpacing =
            (12f * scale).dp,

        cardHorizontalPadding =
            (12f * scale).dp,

        cardVerticalPadding =
            (9f * scale).dp,

        cardCornerRadius =
            (9f * scale).dp,

        cardTitleSize =
            (22f * scale).sp,

        cardTitleSpacing =
            (6f * scale).dp,

        statisticTextSize =
            (17f * scale).sp,

        statisticRowVerticalPadding =
            (1f * scale).dp,

        statusTextSize =
            (21f * scale).sp,

        statusLineHeight =
            (27f * scale).sp,

        emptyTitleSize =
            (27f * scale).sp,

        emptyBodySize =
            (18f * scale).sp,

        emptyBodyLineHeight =
            (24f * scale).sp,

        emptySpacing =
            (12f * scale).dp,

        backSize =
            (24f * scale).sp,

        backTopPadding =
            (2f * scale).dp,

        backBottomPadding =
            (2f * scale).dp,

        contentBottomPadding =
            (8f * scale).dp
    )
}

private const val BASE_REFERENCE_DIMENSION =
    400f

private const val MINIMUM_SCALE =
    0.72f

private const val HORIZONTAL_LAYOUT_ASPECT_RATIO =
    1.45f