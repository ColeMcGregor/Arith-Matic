package com.wiseravenstudios.arithmatic.ui.common

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun calculateRoundSettingsBoardMetrics(
    width: Dp,
    height: Dp
): BoardResponsiveMetrics {

    val environment =
        createBoardEnvironment(
            width =
                width,
            height =
                height,
            minimumReferenceWidth =
                SETTINGS_MINIMUM_REFERENCE_WIDTH_DP,
            maximumReferenceWidth =
                SETTINGS_MAXIMUM_REFERENCE_WIDTH_DP,
            minimumReferenceHeight =
                SETTINGS_MINIMUM_REFERENCE_HEIGHT_DP,
            maximumReferenceHeight =
                SETTINGS_MAXIMUM_REFERENCE_HEIGHT_DP
        )

    val tinySpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                environment.widthScale,

            smallMinimum = 2f,
            smallMaximum = 5f,

            mediumMinimum = 4f,
            mediumMaximum = 8f,

            largeMinimum = 6f,
            largeMaximum = 10f
        )

    val smallSpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                environment.widthScale,

            smallMinimum = 3f,
            smallMaximum = 7f,

            mediumMinimum = 5f,
            mediumMaximum = 10f,

            largeMinimum = 8f,
            largeMaximum = 14f
        )

    val mediumSpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                environment.widthScale,

            smallMinimum = 5f,
            smallMaximum = 10f,

            mediumMinimum = 8f,
            mediumMaximum = 15f,

            largeMinimum = 12f,
            largeMaximum = 22f
        )

    val largeSpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                environment.widthScale,

            smallMinimum = 10f,
            smallMaximum = 20f,

            mediumMinimum = 16f,
            mediumMaximum = 30f,

            largeMinimum = 24f,
            largeMaximum = 42f
        )

    val extraLargeSpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                environment.widthScale,

            smallMinimum = 14f,
            smallMaximum = 26f,

            mediumMinimum = 24f,
            mediumMaximum = 40f,

            largeMinimum = 36f,
            largeMaximum = 56f
        )

    val actionHorizontalPadding =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                environment.widthScale,

            smallMinimum = 1f,
            smallMaximum = 5f,

            mediumMinimum = 4f,
            mediumMaximum = 9f,

            largeMinimum = 8f,
            largeMaximum = 14f
        )

    val actionVerticalPadding =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                environment.heightScale,

            smallMinimum = 0f,
            smallMaximum = 3f,

            mediumMinimum = 2f,
            mediumMaximum = 5f,

            largeMinimum = 4f,
            largeMaximum = 8f
        )

    val contentHorizontalPadding =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                environment.widthScale,

            smallMinimum = 2f,
            smallMaximum = 7f,

            mediumMinimum = 6f,
            mediumMaximum = 12f,

            largeMinimum = 10f,
            largeMaximum = 18f
        )

    val contentVerticalPadding =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                environment.heightScale,

            smallMinimum = 0f,
            smallMaximum = 3f,

            mediumMinimum = 2f,
            mediumMaximum = 6f,

            largeMinimum = 5f,
            largeMaximum = 10f
        )

    /*
     * Basic Round Settings chooses Tall, Middle, or Wide from the
     * actual Basic content area.
     *
     * The shared layout mode is therefore only the broad board-level
     * structural classification.
     */
    val layoutMode =
        when (environment.shape) {
            BoardShape.VerticalRectangle ->
                BoardLayoutMode.SingleColumn

            BoardShape.Square ->
                BoardLayoutMode.SingleColumn

            BoardShape.HorizontalRectangle ->
                BoardLayoutMode.DoubleColumn
        }

    val baseScale =
        calculateSettingsBaseScale(
            width =
                width,
            height =
                height,
            contentHorizontalPadding =
                contentHorizontalPadding,
            contentVerticalPadding =
                contentVerticalPadding
        )

    return BoardResponsiveMetrics(
        width =
            width,
        height =
            height,
        aspectRatio =
            environment.aspectRatio,

        sizeBand =
            environment.sizeBand,
        shape =
            environment.shape,
        layoutMode =
            layoutMode,

        problemTextSize =
            (
                    baseScale *
                            SETTINGS_PROBLEM_TEXT_RATIO
                    ).sp,

        displayTextSize =
            (
                    baseScale *
                            SETTINGS_DISPLAY_TEXT_RATIO
                    ).sp,

        primaryActionTextSize =
            (
                    baseScale *
                            SETTINGS_PRIMARY_ACTION_TEXT_RATIO
                    ).sp,

        widePrimaryActionTextSize =
            (
                    baseScale *
                            SETTINGS_PRIMARY_ACTION_TEXT_RATIO
                    ).sp,

        headingTextSize =
            (
                    baseScale *
                            SETTINGS_HEADING_TEXT_RATIO
                    ).sp,

        bodyTextSize =
            (
                    baseScale *
                            SETTINGS_BODY_TEXT_RATIO
                    ).sp,

        compactTextSize =
            (
                    baseScale *
                            SETTINGS_COMPACT_TEXT_RATIO
                    ).sp,

        microTextSize =
            (
                    baseScale *
                            SETTINGS_MICRO_TEXT_RATIO
                    ).sp,

        tinySpacing =
            tinySpacing,
        smallSpacing =
            smallSpacing,
        mediumSpacing =
            mediumSpacing,
        largeSpacing =
            largeSpacing,
        extraLargeSpacing =
            extraLargeSpacing,

        titleTopSpacing =
            tinySpacing,
        titleToActionsSpacing =
            smallSpacing,
        actionGroupSpacing =
            smallSpacing,
        tallActionTopSpacing =
            smallSpacing,

        actionHorizontalPadding =
            actionHorizontalPadding,
        actionVerticalPadding =
            actionVerticalPadding,

        contentHorizontalPadding =
            contentHorizontalPadding,
        contentVerticalPadding =
            contentVerticalPadding,

        minimumTouchTarget =
            SETTINGS_MINIMUM_TOUCH_TARGET_DP.dp,

        gameQuestionAreaHeight =
            SETTINGS_MINIMUM_TOUCH_TARGET_DP.dp,
        gameAnswerButtonHeight =
            SETTINGS_MINIMUM_TOUCH_TARGET_DP.dp,
        gameSectionSpacing =
            smallSpacing
    )
}

private fun calculateSettingsBaseScale(
    width: Dp,
    height: Dp,
    contentHorizontalPadding: Dp,
    contentVerticalPadding: Dp
): Float {

    val usableWidth =
        (
                width.value -
                        contentHorizontalPadding.value * 2f
                )
            .coerceAtLeast(
                1f
            )

    val usableHeight =
        (
                height.value -
                        contentVerticalPadding.value * 2f
                )
            .coerceAtLeast(
                1f
            )

    val limitingDimension =
        minOf(
            usableWidth,
            usableHeight
        )

    return limitingDimension *
            SETTINGS_BASE_SCALE_RATIO
}

private fun bandResponsiveDp(
    sizeBand: BoardSizeBand,
    scale: Float,

    smallMinimum: Float,
    smallMaximum: Float,

    mediumMinimum: Float,
    mediumMaximum: Float,

    largeMinimum: Float,
    largeMaximum: Float
): Dp {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveDp(
                scale =
                    scale,
                minimum =
                    smallMinimum,
                maximum =
                    smallMaximum
            )

        BoardSizeBand.Medium ->
            responsiveDp(
                scale =
                    scale,
                minimum =
                    mediumMinimum,
                maximum =
                    mediumMaximum
            )

        BoardSizeBand.Large ->
            responsiveDp(
                scale =
                    scale,
                minimum =
                    largeMinimum,
                maximum =
                    largeMaximum
            )
    }
}

private const val SETTINGS_MINIMUM_REFERENCE_WIDTH_DP =
    180f

private const val SETTINGS_MAXIMUM_REFERENCE_WIDTH_DP =
    1400f

private const val SETTINGS_MINIMUM_REFERENCE_HEIGHT_DP =
    140f

private const val SETTINGS_MAXIMUM_REFERENCE_HEIGHT_DP =
    1200f

private const val SETTINGS_BASE_SCALE_RATIO =
    0.14f

private const val SETTINGS_DISPLAY_TEXT_RATIO =
    1.00f

private const val SETTINGS_PROBLEM_TEXT_RATIO =
    1.05f

private const val SETTINGS_PRIMARY_ACTION_TEXT_RATIO =
    0.82f

private const val SETTINGS_HEADING_TEXT_RATIO =
    0.66f

private const val SETTINGS_BODY_TEXT_RATIO =
    0.52f

private const val SETTINGS_COMPACT_TEXT_RATIO =
    0.40f

private const val SETTINGS_MICRO_TEXT_RATIO =
    0.32f

private const val SETTINGS_MINIMUM_TOUCH_TARGET_DP =
    48f