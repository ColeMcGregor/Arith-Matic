package com.wiseravenstudios.arithmatic.ui.common

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun calculateResultsBoardMetrics(
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
                RESULTS_MINIMUM_REFERENCE_WIDTH_DP,
            maximumReferenceWidth =
                RESULTS_MAXIMUM_REFERENCE_WIDTH_DP,
            minimumReferenceHeight =
                RESULTS_MINIMUM_REFERENCE_HEIGHT_DP,
            maximumReferenceHeight =
                RESULTS_MAXIMUM_REFERENCE_HEIGHT_DP
        )

    val tinySpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                environment.heightScale,

            smallMinimum = 1f,
            smallMaximum = 3f,

            mediumMinimum = 2f,
            mediumMaximum = 5f,

            largeMinimum = 3f,
            largeMaximum = 7f
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
                environment.heightScale,

            smallMinimum = 5f,
            smallMaximum = 10f,

            mediumMinimum = 8f,
            mediumMaximum = 15f,

            largeMinimum = 12f,
            largeMaximum = 20f
        )

    val largeSpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                environment.widthScale,

            smallMinimum = 8f,
            smallMaximum = 16f,

            mediumMinimum = 14f,
            mediumMaximum = 24f,

            largeMinimum = 20f,
            largeMaximum = 34f
        )

    val extraLargeSpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                environment.heightScale,

            smallMinimum = 12f,
            smallMaximum = 20f,

            mediumMinimum = 18f,
            mediumMaximum = 30f,

            largeMinimum = 26f,
            largeMaximum = 40f
        )

    val actionHorizontalPadding =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                environment.widthScale,

            smallMinimum = 2f,
            smallMaximum = 6f,

            mediumMinimum = 5f,
            mediumMaximum = 10f,

            largeMinimum = 8f,
            largeMaximum = 14f
        )

    val actionVerticalPadding =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                environment.heightScale,

            smallMinimum = 1f,
            smallMaximum = 3f,

            mediumMinimum = 2f,
            mediumMaximum = 5f,

            largeMinimum = 4f,
            largeMaximum = 7f
        )

    val contentHorizontalPadding =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                environment.widthScale,

            smallMinimum = 5f,
            smallMaximum = 10f,

            mediumMinimum = 8f,
            mediumMaximum = 16f,

            largeMinimum = 14f,
            largeMaximum = 22f
        )

    val contentVerticalPadding =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                environment.heightScale,

            smallMinimum = 3f,
            smallMaximum = 8f,

            mediumMinimum = 6f,
            mediumMaximum = 12f,

            largeMinimum = 10f,
            largeMaximum = 18f
        )

    val minimumTouchTarget =
        when (environment.sizeBand) {
            BoardSizeBand.Small ->
                RESULTS_SMALL_MINIMUM_TOUCH_TARGET_DP.dp

            BoardSizeBand.Medium ->
                RESULTS_MEDIUM_MINIMUM_TOUCH_TARGET_DP.dp

            BoardSizeBand.Large ->
                RESULTS_LARGE_MINIMUM_TOUCH_TARGET_DP.dp
        }

    val singleColumnBaseScale =
        calculateResultsBaseScale(
            width =
                width,
            height =
                height,
            layoutMode =
                BoardLayoutMode.SingleColumn,
            contentHorizontalPadding =
                contentHorizontalPadding,
            contentVerticalPadding =
                contentVerticalPadding,
            tinySpacing =
                tinySpacing,
            smallSpacing =
                smallSpacing,
            mediumSpacing =
                mediumSpacing,
            largeSpacing =
                largeSpacing,
            actionHorizontalPadding =
                actionHorizontalPadding,
            actionVerticalPadding =
                actionVerticalPadding
        )

    val doubleColumnBaseScale =
        if (
            environment.shape ==
            BoardShape.NarrowTall
        ) {
            0f
        } else {
            calculateResultsBaseScale(
                width =
                    width,
                height =
                    height,
                layoutMode =
                    BoardLayoutMode.DoubleColumn,
                contentHorizontalPadding =
                    contentHorizontalPadding,
                contentVerticalPadding =
                    contentVerticalPadding,
                tinySpacing =
                    tinySpacing,
                smallSpacing =
                    smallSpacing,
                mediumSpacing =
                    mediumSpacing,
                largeSpacing =
                    largeSpacing,
                actionHorizontalPadding =
                    actionHorizontalPadding,
                actionVerticalPadding =
                    actionVerticalPadding
            )
        }

    val layoutMode =
        if (
            environment.shape ==
            BoardShape.NarrowTall
        ) {
            BoardLayoutMode.SingleColumn
        } else if (
            doubleColumnBaseScale >
            singleColumnBaseScale *
            RESULTS_WIDE_LAYOUT_SELECTION_ADVANTAGE
        ) {
            BoardLayoutMode.DoubleColumn
        } else {
            BoardLayoutMode.SingleColumn
        }

    val baseScale =
        if (
            layoutMode ==
            BoardLayoutMode.DoubleColumn
        ) {
            doubleColumnBaseScale
        } else {
            singleColumnBaseScale
        }

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

        displayTextSize =
            (
                    baseScale *
                            RESULTS_DISPLAY_TEXT_RATIO
                    ).sp,

        problemTextSize =
            (
                    baseScale *
                            RESULTS_PROBLEM_TEXT_RATIO
                    ).sp,

        headingTextSize =
            (
                    baseScale *
                            RESULTS_HEADING_TEXT_RATIO
                    ).sp,

        bodyTextSize =
            (
                    baseScale *
                            RESULTS_BODY_TEXT_RATIO
                    ).sp,

        primaryActionTextSize =
            (
                    baseScale *
                            RESULTS_PRIMARY_ACTION_TEXT_RATIO
                    ).sp,

        widePrimaryActionTextSize =
            (
                    baseScale *
                            RESULTS_PRIMARY_ACTION_TEXT_RATIO
                    ).sp,

        compactTextSize =
            (
                    baseScale *
                            RESULTS_COMPACT_TEXT_RATIO
                    ).sp,

        microTextSize =
            (
                    baseScale *
                            RESULTS_MICRO_TEXT_RATIO
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
            mediumSpacing,

        actionGroupSpacing =
            smallSpacing,

        tallActionTopSpacing =
            mediumSpacing,

        actionHorizontalPadding =
            actionHorizontalPadding,

        actionVerticalPadding =
            actionVerticalPadding,

        contentHorizontalPadding =
            contentHorizontalPadding,

        contentVerticalPadding =
            contentVerticalPadding,

        minimumTouchTarget =
            minimumTouchTarget,

        gameQuestionAreaHeight =
            minimumTouchTarget,

        gameAnswerButtonHeight =
            minimumTouchTarget,

        gameSectionSpacing =
            smallSpacing
    )
}

private fun calculateResultsBaseScale(
    width: Dp,
    height: Dp,
    layoutMode: BoardLayoutMode,
    contentHorizontalPadding: Dp,
    contentVerticalPadding: Dp,
    tinySpacing: Dp,
    smallSpacing: Dp,
    mediumSpacing: Dp,
    largeSpacing: Dp,
    actionHorizontalPadding: Dp,
    actionVerticalPadding: Dp
): Float {

    val contentWidth =
        (
                width.value -
                        contentHorizontalPadding.value * 2f
                )
            .coerceAtLeast(
                1f
            )

    val contentHeight =
        (
                height.value -
                        contentVerticalPadding.value * 2f
                )
            .coerceAtLeast(
                1f
            )

    return calculateLargestFittingBaseScale(
        maximumCandidate =
            maxOf(
                width.value,
                height.value
            )
    ) { baseScale ->

        val displayFontSize =
            baseScale *
                    RESULTS_DISPLAY_TEXT_RATIO

        val problemFontSize =
            baseScale *
                    RESULTS_PROBLEM_TEXT_RATIO

        val headingFontSize =
            baseScale *
                    RESULTS_HEADING_TEXT_RATIO

        val bodyFontSize =
            baseScale *
                    RESULTS_BODY_TEXT_RATIO

        val actionFontSize =
            baseScale *
                    RESULTS_PRIMARY_ACTION_TEXT_RATIO

        val displayHeight =
            displayFontSize *
                    RESULTS_CHALKTASTIC_LINE_HEIGHT_FACTOR

        val problemHeight =
            problemFontSize *
                    RESULTS_CHALKTASTIC_LINE_HEIGHT_FACTOR

        val headingHeight =
            headingFontSize *
                    RESULTS_CHALKTASTIC_LINE_HEIGHT_FACTOR

        val bodyHeight =
            bodyFontSize *
                    RESULTS_CHALKTASTIC_LINE_HEIGHT_FACTOR

        val actionHeight =
            actionFontSize *
                    RESULTS_CHALKTASTIC_LINE_HEIGHT_FACTOR +
                    actionVerticalPadding.value * 2f

        val titleFits =
            when (layoutMode) {
                BoardLayoutMode.SingleColumn ->
                    displayFontSize *
                            RESULTS_TALL_TITLE_WIDTH_EM <=
                            contentWidth

                BoardLayoutMode.DoubleColumn ->
                    displayFontSize *
                            RESULTS_WIDE_TITLE_WIDTH_EM <=
                            contentWidth
            }

        val singleColumnFits =
            if (
                layoutMode ==
                BoardLayoutMode.SingleColumn
            ) {
                val titleHeight =
                    displayHeight * 2f

                val scoreBlockHeight =
                    problemHeight +
                            tinySpacing.value +
                            bodyHeight

                val standardBlockHeight =
                    headingHeight +
                            tinySpacing.value +
                            bodyHeight

                val mainContentHeight =
                    titleHeight +
                            scoreBlockHeight +
                            standardBlockHeight * 3f +
                            mediumSpacing.value * 4f

                val footerHeight =
                    actionHeight * 2f

                val requiredHeight =
                    mainContentHeight +
                            largeSpacing.value +
                            footerHeight

                val scoreWidth =
                    problemFontSize *
                            RESULTS_SCORE_VALUE_WIDTH_EM

                val accuracyWidth =
                    headingFontSize *
                            RESULTS_ACCURACY_VALUE_WIDTH_EM

                val totalTimeWidth =
                    headingFontSize *
                            RESULTS_TOTAL_TIME_VALUE_WIDTH_EM

                val averageTimeWidth =
                    headingFontSize *
                            RESULTS_TALL_AVERAGE_TIME_VALUE_WIDTH_EM

                val practiceAgainWidth =
                    actionFontSize *
                            RESULTS_TALL_PRACTICE_WIDTH_EM +
                            actionHorizontalPadding.value * 2f

                val changeSettingsWidth =
                    actionFontSize *
                            RESULTS_TALL_CHANGE_WIDTH_EM +
                            actionHorizontalPadding.value * 2f

                val returnHomeWidth =
                    actionFontSize *
                            RESULTS_TALL_RETURN_WIDTH_EM +
                            actionHorizontalPadding.value * 2f

                val footerWidth =
                    practiceAgainWidth +
                            changeSettingsWidth +
                            returnHomeWidth

                scoreWidth <=
                        contentWidth &&
                        accuracyWidth <=
                        contentWidth &&
                        totalTimeWidth <=
                        contentWidth &&
                        averageTimeWidth <=
                        contentWidth &&
                        footerWidth <=
                        contentWidth &&
                        requiredHeight <=
                        contentHeight
            } else {
                true
            }

        val doubleColumnFits =
            if (
                layoutMode ==
                BoardLayoutMode.DoubleColumn
            ) {
                val summaryItemHeight =
                    maxOf(
                        problemHeight,
                        headingHeight
                    ) +
                            tinySpacing.value +
                            bodyHeight

                val footerHeight =
                    actionHeight

                val requiredHeight =
                    displayHeight +
                            largeSpacing.value +
                            summaryItemHeight +
                            largeSpacing.value +
                            footerHeight

                val scoreItemWidth =
                    maxOf(
                        problemFontSize *
                                RESULTS_SCORE_VALUE_WIDTH_EM,
                        bodyFontSize *
                                RESULTS_CORRECT_LABEL_WIDTH_EM
                    )

                val accuracyItemWidth =
                    maxOf(
                        headingFontSize *
                                RESULTS_ACCURACY_VALUE_WIDTH_EM,
                        bodyFontSize *
                                RESULTS_ACCURACY_LABEL_WIDTH_EM
                    )

                val totalTimeItemWidth =
                    maxOf(
                        headingFontSize *
                                RESULTS_TOTAL_TIME_VALUE_WIDTH_EM,
                        bodyFontSize *
                                RESULTS_TOTAL_TIME_LABEL_WIDTH_EM
                    )

                val averageItemWidth =
                    maxOf(
                        headingFontSize *
                                RESULTS_WIDE_AVERAGE_TIME_VALUE_WIDTH_EM,
                        bodyFontSize *
                                RESULTS_AVERAGE_LABEL_WIDTH_EM
                    )

                val summaryWidth =
                    scoreItemWidth +
                            accuracyItemWidth +
                            totalTimeItemWidth +
                            averageItemWidth +
                            largeSpacing.value * 3f

                val practiceAgainWidth =
                    actionFontSize *
                            RESULTS_WIDE_PRACTICE_AGAIN_WIDTH_EM +
                            actionHorizontalPadding.value * 2f

                val changeSettingsWidth =
                    actionFontSize *
                            RESULTS_WIDE_CHANGE_SETTINGS_WIDTH_EM +
                            actionHorizontalPadding.value * 2f

                val returnHomeWidth =
                    actionFontSize *
                            RESULTS_WIDE_RETURN_HOME_WIDTH_EM +
                            actionHorizontalPadding.value * 2f

                val footerWidth =
                    practiceAgainWidth +
                            changeSettingsWidth +
                            returnHomeWidth +
                            largeSpacing.value * 2f

                summaryWidth <=
                        contentWidth &&
                        footerWidth <=
                        contentWidth &&
                        requiredHeight <=
                        contentHeight
            } else {
                true
            }

        titleFits &&
                singleColumnFits &&
                doubleColumnFits
    }
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

private const val RESULTS_MINIMUM_REFERENCE_WIDTH_DP =
    180f

private const val RESULTS_MAXIMUM_REFERENCE_WIDTH_DP =
    1400f

private const val RESULTS_MINIMUM_REFERENCE_HEIGHT_DP =
    140f

private const val RESULTS_MAXIMUM_REFERENCE_HEIGHT_DP =
    1200f

private const val RESULTS_DISPLAY_TEXT_RATIO =
    1.00f

private const val RESULTS_PROBLEM_TEXT_RATIO =
    0.90f

private const val RESULTS_HEADING_TEXT_RATIO =
    0.76f

private const val RESULTS_BODY_TEXT_RATIO =
    0.63f

private const val RESULTS_PRIMARY_ACTION_TEXT_RATIO =
    0.42f

private const val RESULTS_COMPACT_TEXT_RATIO =
    0.45f

private const val RESULTS_MICRO_TEXT_RATIO =
    0.34f

private const val RESULTS_CHALKTASTIC_LINE_HEIGHT_FACTOR =
    1.31f

private const val RESULTS_TALL_TITLE_WIDTH_EM =
    5.9f

private const val RESULTS_WIDE_TITLE_WIDTH_EM =
    9.2f

private const val RESULTS_SCORE_VALUE_WIDTH_EM =
    5.8f

private const val RESULTS_ACCURACY_VALUE_WIDTH_EM =
    3.6f

private const val RESULTS_TOTAL_TIME_VALUE_WIDTH_EM =
    4.8f

private const val RESULTS_TALL_AVERAGE_TIME_VALUE_WIDTH_EM =
    8.8f

private const val RESULTS_WIDE_AVERAGE_TIME_VALUE_WIDTH_EM =
    6.4f

private const val RESULTS_CORRECT_LABEL_WIDTH_EM =
    4.5f

private const val RESULTS_ACCURACY_LABEL_WIDTH_EM =
    5.4f

private const val RESULTS_TOTAL_TIME_LABEL_WIDTH_EM =
    6.2f

private const val RESULTS_AVERAGE_LABEL_WIDTH_EM =
    4.8f

private const val RESULTS_TALL_PRACTICE_WIDTH_EM =
    4.8f

private const val RESULTS_TALL_CHANGE_WIDTH_EM =
    4.5f

private const val RESULTS_TALL_RETURN_WIDTH_EM =
    4.4f

private const val RESULTS_WIDE_PRACTICE_AGAIN_WIDTH_EM =
    8.2f

private const val RESULTS_WIDE_CHANGE_SETTINGS_WIDTH_EM =
    9.2f

private const val RESULTS_WIDE_RETURN_HOME_WIDTH_EM =
    7.5f

private const val RESULTS_WIDE_LAYOUT_SELECTION_ADVANTAGE =
    1.05f

private const val RESULTS_SMALL_MINIMUM_TOUCH_TARGET_DP =
    40f

private const val RESULTS_MEDIUM_MINIMUM_TOUCH_TARGET_DP =
    44f

private const val RESULTS_LARGE_MINIMUM_TOUCH_TARGET_DP =
    48f