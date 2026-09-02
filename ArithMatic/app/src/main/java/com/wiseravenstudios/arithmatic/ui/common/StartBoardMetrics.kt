package com.wiseravenstudios.arithmatic.ui.common

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun calculateStartBoardMetrics(
    width: Dp,
    height: Dp
): BoardResponsiveMetrics {

    val environment =
        createBoardEnvironment(
            width = width,
            height = height,
            minimumReferenceWidth =
                START_MINIMUM_REFERENCE_WIDTH_DP,
            maximumReferenceWidth =
                START_MAXIMUM_REFERENCE_WIDTH_DP,
            minimumReferenceHeight =
                START_MINIMUM_REFERENCE_HEIGHT_DP,
            maximumReferenceHeight =
                START_MAXIMUM_REFERENCE_HEIGHT_DP
        )

    val widthScale =
        environment.widthScale

    val heightScale =
        environment.heightScale

    /*
     * ============================================================
     * CORE RESPONSIVE GEOMETRY
     * ============================================================
     */

    val actionHorizontalPadding =
        when (environment.sizeBand) {
            BoardSizeBand.Small ->
                responsiveDp(
                    scale = widthScale,
                    minimum = 6f,
                    maximum = 10f
                )

            BoardSizeBand.Medium ->
                responsiveDp(
                    scale = widthScale,
                    minimum = 9f,
                    maximum = 14f
                )

            BoardSizeBand.Large ->
                responsiveDp(
                    scale = widthScale,
                    minimum = 12f,
                    maximum = 18f
                )
        }

    val actionVerticalPadding =
        when (environment.sizeBand) {
            BoardSizeBand.Small ->
                responsiveDp(
                    scale = heightScale,
                    minimum = 3f,
                    maximum = 7f
                )

            BoardSizeBand.Medium ->
                responsiveDp(
                    scale = heightScale,
                    minimum = 5f,
                    maximum = 9f
                )

            BoardSizeBand.Large ->
                responsiveDp(
                    scale = heightScale,
                    minimum = 7f,
                    maximum = 12f
                )
        }

    val contentHorizontalPadding =
        when (environment.sizeBand) {
            BoardSizeBand.Small ->
                responsiveDp(
                    scale = widthScale,
                    minimum = 3f,
                    maximum = 8f
                )

            BoardSizeBand.Medium ->
                responsiveDp(
                    scale = widthScale,
                    minimum = 6f,
                    maximum = 12f
                )

            BoardSizeBand.Large ->
                responsiveDp(
                    scale = widthScale,
                    minimum = 10f,
                    maximum = 18f
                )
        }

    val contentVerticalPadding =
        when (environment.sizeBand) {
            BoardSizeBand.Small ->
                responsiveDp(
                    scale = heightScale,
                    minimum = 3f,
                    maximum = 8f
                )

            BoardSizeBand.Medium ->
                responsiveDp(
                    scale = heightScale,
                    minimum = 6f,
                    maximum = 12f
                )

            BoardSizeBand.Large ->
                responsiveDp(
                    scale = heightScale,
                    minimum = 10f,
                    maximum = 18f
                )
        }

    /*
     * ============================================================
     * GENERAL SPACING
     * ============================================================
     */

    val tinySpacing =
        bandResponsiveDp(
            sizeBand = environment.sizeBand,
            scale = heightScale,
            smallMinimum = 0f,
            smallMaximum = 3f,
            mediumMinimum = 2f,
            mediumMaximum = 5f,
            largeMinimum = 4f,
            largeMaximum = 8f
        )

    val smallSpacing =
        bandResponsiveDp(
            sizeBand = environment.sizeBand,
            scale = heightScale,
            smallMinimum = 2f,
            smallMaximum = 6f,
            mediumMinimum = 5f,
            mediumMaximum = 10f,
            largeMinimum = 8f,
            largeMaximum = 14f
        )

    val mediumSpacing =
        bandResponsiveDp(
            sizeBand = environment.sizeBand,
            scale = heightScale,
            smallMinimum = 5f,
            smallMaximum = 11f,
            mediumMinimum = 9f,
            mediumMaximum = 18f,
            largeMinimum = 14f,
            largeMaximum = 26f
        )

    val largeSpacing =
        bandResponsiveDp(
            sizeBand = environment.sizeBand,
            scale = heightScale,
            smallMinimum = 9f,
            smallMaximum = 18f,
            mediumMinimum = 16f,
            mediumMaximum = 30f,
            largeMinimum = 24f,
            largeMaximum = 42f
        )

    val extraLargeSpacing =
        bandResponsiveDp(
            sizeBand = environment.sizeBand,
            scale = heightScale,
            smallMinimum = 16f,
            smallMaximum = 30f,
            mediumMinimum = 28f,
            mediumMaximum = 48f,
            largeMinimum = 40f,
            largeMaximum = 64f
        )

    /*
     * ============================================================
     * START BOARD PREFERRED SPACING
     * ============================================================
     */

    val preferredSingleColumnTitleTopSpacing =
        bandResponsiveDp(
            sizeBand = environment.sizeBand,
            scale = heightScale,
            smallMinimum = 12f,
            smallMaximum = 30f,
            mediumMinimum = 24f,
            mediumMaximum = 44f,
            largeMinimum = 36f,
            largeMaximum = 60f
        )

    val verticalRectangleUtilityClearance =
        bandResponsiveDp(
            sizeBand = environment.sizeBand,
            scale = heightScale,
            smallMinimum = 16f,
            smallMaximum = 26f,
            mediumMinimum = 20f,
            mediumMaximum = 32f,
            largeMinimum = 24f,
            largeMaximum = 38f
        )

    val preferredTitleToActionsSpacing =
        bandResponsiveDp(
            sizeBand = environment.sizeBand,
            scale = heightScale,
            smallMinimum = 8f,
            smallMaximum = 24f,
            mediumMinimum = 16f,
            mediumMaximum = 40f,
            largeMinimum = 28f,
            largeMaximum = 60f
        )

    val preferredActionGroupSpacing =
        bandResponsiveDp(
            sizeBand = environment.sizeBand,
            scale = heightScale,
            smallMinimum = 2f,
            smallMaximum = 8f,
            mediumMinimum = 5f,
            mediumMaximum = 12f,
            largeMinimum = 8f,
            largeMaximum = 18f
        )

    val doubleColumnTitleTopSpacing =
        bandResponsiveDp(
            sizeBand = environment.sizeBand,
            scale = heightScale,
            smallMinimum = 4f,
            smallMaximum = 12f,
            mediumMinimum = 8f,
            mediumMaximum = 24f,
            largeMinimum = 14f,
            largeMaximum = 40f
        )

    val doubleColumnMenuHorizontalPadding =
        bandResponsiveDp(
            sizeBand = environment.sizeBand,
            scale = widthScale,
            smallMinimum = 4f,
            smallMaximum = 12f,
            mediumMinimum = 12f,
            mediumMaximum = 22f,
            largeMinimum = 14f,
            largeMaximum = 32f
        )

    val doubleColumnGap =
        bandResponsiveDp(
            sizeBand = environment.sizeBand,
            scale = widthScale,
            smallMinimum = 4f,
            smallMaximum = 10f,
            mediumMinimum = 16f,
            mediumMaximum = 20f,
            largeMinimum = 18f,
            largeMaximum = 32f
        )

    /*
     * ============================================================
     * TYPOGRAPHY CANDIDATES
     * ============================================================
     */

    val singleColumnTitleTopSpacing =
        preferredSingleColumnTitleTopSpacing +
                if (
                    environment.shape ==
                    BoardShape.VerticalRectangle
                ) {
                    verticalRectangleUtilityClearance
                } else {
                    0.dp
                }

    val singleColumnBaseScale =
        calculateStartLayoutBaseScale(
            width = width,
            height = height,
            shape = environment.shape,
            layoutMode =
                BoardLayoutMode.SingleColumn,
            contentHorizontalPadding =
                contentHorizontalPadding,
            contentVerticalPadding =
                contentVerticalPadding,
            titleTopSpacing =
                singleColumnTitleTopSpacing,
            actionHorizontalPadding =
                actionHorizontalPadding,
            actionVerticalPadding =
                actionVerticalPadding,
            tinySpacing =
                tinySpacing,
            doubleColumnMenuHorizontalPadding =
                doubleColumnMenuHorizontalPadding,
            doubleColumnGap =
                doubleColumnGap
        )

    val doubleColumnBaseScale =
        if (
            environment.shape ==
            BoardShape.VerticalRectangle
        ) {
            0f
        } else {
            calculateStartLayoutBaseScale(
                width = width,
                height = height,
                shape = environment.shape,
                layoutMode =
                    BoardLayoutMode.DoubleColumn,
                contentHorizontalPadding =
                    contentHorizontalPadding,
                contentVerticalPadding =
                    contentVerticalPadding,
                titleTopSpacing =
                    doubleColumnTitleTopSpacing,
                actionHorizontalPadding =
                    actionHorizontalPadding,
                actionVerticalPadding =
                    actionVerticalPadding,
                tinySpacing =
                    tinySpacing,
                doubleColumnMenuHorizontalPadding =
                    doubleColumnMenuHorizontalPadding,
                doubleColumnGap =
                    doubleColumnGap
            )
        }

    /*
     * ============================================================
     * STRUCTURAL LAYOUT SELECTION
     * ============================================================
     */

    val layoutMode =
        if (
            environment.shape ==
            BoardShape.VerticalRectangle
        ) {
            BoardLayoutMode.SingleColumn
        } else if (
            doubleColumnBaseScale >
            singleColumnBaseScale *
            START_DOUBLE_COLUMN_SELECTION_ADVANTAGE
        ) {
            BoardLayoutMode.DoubleColumn
        } else {
            BoardLayoutMode.SingleColumn
        }

    val isDoubleColumn =
        layoutMode ==
                BoardLayoutMode.DoubleColumn

    val fittedBaseScale =
        if (isDoubleColumn) {
            doubleColumnBaseScale
        } else {
            singleColumnBaseScale
        }

    /*
     * ============================================================
     * READABILITY BAND
     * ============================================================
     *
     * The geometric size band describes the amount of board area.
     *
     * The effective Start Board band can step downward when the
     * selected geometry cannot support readable typography for the
     * geometric band.
     *
     * We do not force text above the fitted scale. Fit remains the
     * hard constraint.
     */

    val effectiveSizeBand =
        calculateReadableStartSizeBand(
            geometricSizeBand =
                environment.sizeBand,
            fittedBaseScale =
                fittedBaseScale
        )

    /*
     * ============================================================
     * FINAL TYPOGRAPHY
     * ============================================================
     *
     * The fitted scale remains the hard maximum.
     *
     * A board that cannot support the readability threshold of its
     * geometric band is reported as the next smaller band instead
     * of pretending that tiny text is still Medium or Large.
     */

    val baseScale =
        fittedBaseScale

    val problemTextSize =
        (
                baseScale *
                        START_PROBLEM_TEXT_RATIO
                ).sp

    val displayTextSize =
        (
                baseScale *
                        START_DISPLAY_TEXT_RATIO
                ).sp

    val primaryActionTextSize =
        (
                baseScale *
                        START_PRIMARY_ACTION_TEXT_RATIO
                ).sp

    val headingTextSize =
        (
                baseScale *
                        START_HEADING_TEXT_RATIO
                ).sp

    val bodyTextSize =
        (
                baseScale *
                        START_BODY_TEXT_RATIO
                ).sp

    val compactTextSize =
        (
                baseScale *
                        START_COMPACT_TEXT_RATIO
                ).sp

    val microTextSize =
        (
                baseScale *
                        START_MICRO_TEXT_RATIO
                ).sp

    val widePrimaryActionTextSize =
        primaryActionTextSize

    /*
     * ============================================================
     * FINAL POSITIONING
     * ============================================================
     */

    val titleTopSpacing =
        if (isDoubleColumn) {
            doubleColumnTitleTopSpacing
        } else {
            singleColumnTitleTopSpacing
        }

    val actionRowCount =
        if (isDoubleColumn) {
            START_DOUBLE_COLUMN_ACTION_ROW_COUNT
        } else {
            START_ACTION_COUNT
        }

    val minimumActionGap =
        if (isDoubleColumn) {
            START_MINIMUM_DOUBLE_COLUMN_ACTION_GROUP_SPACING_DP
        } else {
            START_MINIMUM_SINGLE_COLUMN_ACTION_GROUP_SPACING_DP
        }

    val minimumTitleGap =
        if (isDoubleColumn) {
            START_MINIMUM_DOUBLE_COLUMN_TITLE_TO_ACTION_SPACING_DP
        } else {
            START_MINIMUM_SINGLE_COLUMN_TITLE_TO_ACTION_SPACING_DP
        }

    val titleHeight =
        baseScale *
                START_DISPLAY_TEXT_RATIO *
                START_CHALKTASTIC_LINE_HEIGHT_FACTOR

    val actionHeight =
        maxOf(
            START_MINIMUM_TOUCH_TARGET_DP,
            baseScale *
                    START_PRIMARY_ACTION_TEXT_RATIO *
                    START_CHALKTASTIC_LINE_HEIGHT_FACTOR +
                    actionVerticalPadding.value * 2f
        )

    val minimumRequiredHeight =
        contentVerticalPadding.value * 2f +
                titleTopSpacing.value +
                titleHeight +
                minimumTitleGap +
                actionHeight *
                actionRowCount +
                minimumActionGap *
                (actionRowCount - 1)

    val remainingVerticalSpace =
        (
                height.value -
                        minimumRequiredHeight
                )
            .coerceAtLeast(
                0f
            )

    val desiredAdditionalTitleGap =
        (
                preferredTitleToActionsSpacing.value -
                        minimumTitleGap
                )
            .coerceAtLeast(
                0f
            )

    val additionalTitleGap =
        minOf(
            desiredAdditionalTitleGap,
            remainingVerticalSpace
        )

    val spaceAfterTitleGap =
        (
                remainingVerticalSpace -
                        additionalTitleGap
                )
            .coerceAtLeast(
                0f
            )

    val desiredAdditionalActionGapPerGap =
        (
                preferredActionGroupSpacing.value -
                        minimumActionGap
                )
            .coerceAtLeast(
                0f
            )

    val actionGapCount =
        (
                actionRowCount -
                        1
                )
            .coerceAtLeast(
                1
            )

    val additionalActionGapPerGap =
        minOf(
            desiredAdditionalActionGapPerGap,
            spaceAfterTitleGap /
                    actionGapCount
        )

    val titleToActionsSpacing =
        (
                minimumTitleGap +
                        additionalTitleGap
                ).dp

    val actionGroupSpacing =
        (
                minimumActionGap +
                        additionalActionGapPerGap
                ).dp

    val minimumTouchTarget =
        START_MINIMUM_TOUCH_TARGET_DP.dp

    return BoardResponsiveMetrics(
        width = width,
        height = height,
        aspectRatio =
            environment.aspectRatio,

        sizeBand =
            effectiveSizeBand,
        shape =
            environment.shape,
        layoutMode =
            layoutMode,

        problemTextSize =
            problemTextSize,
        displayTextSize =
            displayTextSize,
        primaryActionTextSize =
            primaryActionTextSize,
        widePrimaryActionTextSize =
            widePrimaryActionTextSize,
        headingTextSize =
            headingTextSize,
        bodyTextSize =
            bodyTextSize,
        compactTextSize =
            compactTextSize,
        microTextSize =
            microTextSize,

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
            titleTopSpacing,
        titleToActionsSpacing =
            titleToActionsSpacing,
        actionGroupSpacing =
            actionGroupSpacing,
        tallActionTopSpacing =
            titleToActionsSpacing,

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

/**
 * Steps the Start Board down to a smaller size band when the
 * available geometry cannot support readable typography for its
 * geometric band.
 */
private fun calculateReadableStartSizeBand(
    geometricSizeBand: BoardSizeBand,
    fittedBaseScale: Float
): BoardSizeBand {
    return when (geometricSizeBand) {
        BoardSizeBand.Large -> {
            when {
                fittedBaseScale >=
                        START_LARGE_MINIMUM_BASE_SCALE ->
                    BoardSizeBand.Large

                fittedBaseScale >=
                        START_MEDIUM_MINIMUM_BASE_SCALE ->
                    BoardSizeBand.Medium

                else ->
                    BoardSizeBand.Small
            }
        }

        BoardSizeBand.Medium -> {
            if (
                fittedBaseScale >=
                START_MEDIUM_MINIMUM_BASE_SCALE
            ) {
                BoardSizeBand.Medium
            } else {
                BoardSizeBand.Small
            }
        }

        BoardSizeBand.Small ->
            BoardSizeBand.Small
    }
}

/**
 * Calculates the largest typography base scale that can fit one
 * candidate Start Board structure.
 */
private fun calculateStartLayoutBaseScale(
    width: Dp,
    height: Dp,
    shape: BoardShape,
    layoutMode: BoardLayoutMode,
    contentHorizontalPadding: Dp,
    contentVerticalPadding: Dp,
    titleTopSpacing: Dp,
    actionHorizontalPadding: Dp,
    actionVerticalPadding: Dp,
    tinySpacing: Dp,
    doubleColumnMenuHorizontalPadding: Dp,
    doubleColumnGap: Dp
): Float {

    val contentWidth =
        (
                width.value -
                        contentHorizontalPadding.value * 2f
                )
            .coerceAtLeast(
                1f
            )

    val maximumCandidate =
        maxOf(
            width.value,
            height.value
        )

    return calculateLargestFittingBaseScale(
        maximumCandidate =
            maximumCandidate
    ) { baseScale ->

        val titleTextWidth =
            baseScale *
                    START_DISPLAY_TEXT_RATIO *
                    START_TITLE_WIDTH_EM

        val primaryActionTextWidth =
            baseScale *
                    START_PRIMARY_ACTION_TEXT_RATIO *
                    START_LONGEST_PRIMARY_ACTION_WIDTH_EM

        val compactQuestionTextWidth =
            baseScale *
                    START_COMPACT_TEXT_RATIO *
                    START_QUESTION_WIDTH_EM

        val compactExitTextWidth =
            baseScale *
                    START_COMPACT_TEXT_RATIO *
                    START_EXIT_WIDTH_EM

        val questionActionWidth =
            maxOf(
                START_MINIMUM_TOUCH_TARGET_DP,
                compactQuestionTextWidth +
                        tinySpacing.value * 2f
            )

        val exitActionWidth =
            maxOf(
                START_MINIMUM_TOUCH_TARGET_DP,
                compactExitTextWidth +
                        tinySpacing.value * 2f
            )

        val titleFitsHorizontally =
            if (
                shape ==
                BoardShape.VerticalRectangle
            ) {
                titleTextWidth <=
                        contentWidth
            } else {
                questionActionWidth +
                        START_HEADER_HORIZONTAL_CLEARANCE_DP +
                        titleTextWidth +
                        START_HEADER_HORIZONTAL_CLEARANCE_DP +
                        exitActionWidth <=
                        width.value -
                        contentHorizontalPadding.value * 2f
            }

        val utilityRowFits =
            questionActionWidth +
                    exitActionWidth <=
                    contentWidth

        val primaryActionsFit =
            when (layoutMode) {
                BoardLayoutMode.SingleColumn -> {
                    val availableActionTextWidth =
                        (
                                contentWidth -
                                        actionHorizontalPadding.value * 2f
                                )
                            .coerceAtLeast(
                                0f
                            )

                    primaryActionTextWidth <=
                            availableActionTextWidth
                }

                BoardLayoutMode.DoubleColumn -> {
                    val doubleColumnContentWidth =
                        (
                                contentWidth -
                                        doubleColumnMenuHorizontalPadding.value * 2f -
                                        doubleColumnGap.value
                                )
                            .coerceAtLeast(
                                0f
                            )

                    val columnWidth =
                        doubleColumnContentWidth /
                                START_DOUBLE_COLUMN_COUNT

                    val availableActionTextWidth =
                        (
                                columnWidth -
                                        actionHorizontalPadding.value * 2f
                                )
                            .coerceAtLeast(
                                0f
                            )

                    primaryActionTextWidth <=
                            availableActionTextWidth
                }
            }

        val titleHeight =
            baseScale *
                    START_DISPLAY_TEXT_RATIO *
                    START_CHALKTASTIC_LINE_HEIGHT_FACTOR

        val actionHeight =
            maxOf(
                START_MINIMUM_TOUCH_TARGET_DP,
                baseScale *
                        START_PRIMARY_ACTION_TEXT_RATIO *
                        START_CHALKTASTIC_LINE_HEIGHT_FACTOR +
                        actionVerticalPadding.value * 2f
            )

        val actionRowCount =
            when (layoutMode) {
                BoardLayoutMode.SingleColumn ->
                    START_ACTION_COUNT

                BoardLayoutMode.DoubleColumn ->
                    START_DOUBLE_COLUMN_ACTION_ROW_COUNT
            }

        val minimumTitleGap =
            when (layoutMode) {
                BoardLayoutMode.SingleColumn ->
                    START_MINIMUM_SINGLE_COLUMN_TITLE_TO_ACTION_SPACING_DP

                BoardLayoutMode.DoubleColumn ->
                    START_MINIMUM_DOUBLE_COLUMN_TITLE_TO_ACTION_SPACING_DP
            }

        val minimumActionGap =
            when (layoutMode) {
                BoardLayoutMode.SingleColumn ->
                    START_MINIMUM_SINGLE_COLUMN_ACTION_GROUP_SPACING_DP

                BoardLayoutMode.DoubleColumn ->
                    START_MINIMUM_DOUBLE_COLUMN_ACTION_GROUP_SPACING_DP
            }

        val requiredHeight =
            contentVerticalPadding.value * 2f +
                    titleTopSpacing.value +
                    titleHeight +
                    minimumTitleGap +
                    actionHeight *
                    actionRowCount +
                    minimumActionGap *
                    (actionRowCount - 1)

        val fitsVertically =
            requiredHeight <=
                    height.value

        titleFitsHorizontally &&
                utilityRowFits &&
                primaryActionsFit &&
                fitsVertically
    }
}

/*
 * ============================================================
 * SHARED START BOARD BAND HELPERS
 * ============================================================
 */

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
                scale = scale,
                minimum = smallMinimum,
                maximum = smallMaximum
            )

        BoardSizeBand.Medium ->
            responsiveDp(
                scale = scale,
                minimum = mediumMinimum,
                maximum = mediumMaximum
            )

        BoardSizeBand.Large ->
            responsiveDp(
                scale = scale,
                minimum = largeMinimum,
                maximum = largeMaximum
            )
    }
}

/*
 * ============================================================
 * START BOARD REFERENCE RANGE
 * ============================================================
 */

private const val START_MINIMUM_REFERENCE_WIDTH_DP =
    180f

private const val START_MAXIMUM_REFERENCE_WIDTH_DP =
    1400f

private const val START_MINIMUM_REFERENCE_HEIGHT_DP =
    140f

private const val START_MAXIMUM_REFERENCE_HEIGHT_DP =
    1200f

/*
 * ============================================================
 * TYPOGRAPHY ROLE RATIOS
 * ============================================================
 */

private const val START_DISPLAY_TEXT_RATIO =
    1.00f

private const val START_PROBLEM_TEXT_RATIO =
    1.05f

private const val START_PRIMARY_ACTION_TEXT_RATIO =
    0.82f

private const val START_HEADING_TEXT_RATIO =
    0.58f

private const val START_BODY_TEXT_RATIO =
    0.46f

private const val START_COMPACT_TEXT_RATIO =
    0.45f

private const val START_MICRO_TEXT_RATIO =
    0.28f

/*
 * ============================================================
 * READABILITY THRESHOLDS
 * ============================================================
 *
 * These values define the lowest fitted base scale that still
 * qualifies as Medium or Large.
 *
 * They do not override the physical fit calculation.
 *
 * Small remains the final supported responsive band.
 */

private const val START_MEDIUM_MINIMUM_BASE_SCALE =
    34f

private const val START_LARGE_MINIMUM_BASE_SCALE =
    46f

/*
 * ============================================================
 * CHALKTASTIC FONT METRICS
 * ============================================================
 */

private const val START_TITLE_WIDTH_EM =
    7.078f

private const val START_LONGEST_PRIMARY_ACTION_WIDTH_EM =
    5.558f

private const val START_EXIT_WIDTH_EM =
    2.388f

private const val START_QUESTION_WIDTH_EM =
    0.722f

private const val START_CHALKTASTIC_LINE_HEIGHT_FACTOR =
    1.29f

/*
 * ============================================================
 * START BOARD LAYOUT
 * ============================================================
 */

private const val START_ACTION_COUNT =
    4

private const val START_DOUBLE_COLUMN_ACTION_ROW_COUNT =
    2

private const val START_DOUBLE_COLUMN_COUNT =
    2f

private const val START_DOUBLE_COLUMN_SELECTION_ADVANTAGE =
    1.04f

private const val START_HEADER_HORIZONTAL_CLEARANCE_DP =
    8f

/*
 * ============================================================
 * START BOARD MINIMUM GEOMETRY
 * ============================================================
 */

private const val START_MINIMUM_TOUCH_TARGET_DP =
    48f

private const val START_MINIMUM_SINGLE_COLUMN_TITLE_TO_ACTION_SPACING_DP =
    8f

private const val START_MINIMUM_DOUBLE_COLUMN_TITLE_TO_ACTION_SPACING_DP =
    4f

private const val START_MINIMUM_SINGLE_COLUMN_ACTION_GROUP_SPACING_DP =
    2f

private const val START_MINIMUM_DOUBLE_COLUMN_ACTION_GROUP_SPACING_DP =
    4f