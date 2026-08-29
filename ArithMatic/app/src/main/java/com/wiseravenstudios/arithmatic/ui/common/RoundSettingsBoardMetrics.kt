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
                environment.heightScale,

            smallMinimum = 0f,
            smallMaximum = 2f,

            mediumMinimum = 1f,
            mediumMaximum = 4f,

            largeMinimum = 3f,
            largeMaximum = 6f
        )

    val smallSpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                environment.heightScale,

            smallMinimum = 1f,
            smallMaximum = 4f,

            mediumMinimum = 3f,
            mediumMaximum = 7f,

            largeMinimum = 6f,
            largeMaximum = 10f
        )

    val mediumSpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                environment.heightScale,

            smallMinimum = 2f,
            smallMaximum = 7f,

            mediumMinimum = 5f,
            mediumMaximum = 12f,

            largeMinimum = 10f,
            largeMaximum = 18f
        )

    val largeSpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                environment.heightScale,

            smallMinimum = 4f,
            smallMaximum = 12f,

            mediumMinimum = 10f,
            mediumMaximum = 20f,

            largeMinimum = 18f,
            largeMaximum = 30f
        )

    val extraLargeSpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                environment.heightScale,

            smallMinimum = 8f,
            smallMaximum = 20f,

            mediumMinimum = 18f,
            mediumMaximum = 34f,

            largeMinimum = 30f,
            largeMaximum = 48f
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

            smallMinimum = 1f,
            smallMaximum = 5f,

            mediumMinimum = 4f,
            mediumMaximum = 8f,

            largeMinimum = 7f,
            largeMaximum = 12f
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

    val minimumTouchTarget =
        when (environment.sizeBand) {
            BoardSizeBand.Small ->
                SETTINGS_SMALL_MINIMUM_TOUCH_TARGET_DP.dp

            BoardSizeBand.Medium ->
                SETTINGS_MEDIUM_MINIMUM_TOUCH_TARGET_DP.dp

            BoardSizeBand.Large ->
                SETTINGS_LARGE_MINIMUM_TOUCH_TARGET_DP.dp
        }

    val singleColumnBaseScale =
        calculateBasicSettingsBaseScale(
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
            mediumSpacing =
                mediumSpacing,
            largeSpacing =
                largeSpacing,
            actionHorizontalPadding =
                actionHorizontalPadding,
            minimumTouchTarget =
                minimumTouchTarget
        )

    val doubleColumnBaseScale =
        if (
            environment.shape ==
            BoardShape.NarrowTall
        ) {
            0f
        } else {
            calculateBasicSettingsBaseScale(
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
                mediumSpacing =
                    mediumSpacing,
                largeSpacing =
                    largeSpacing,
                actionHorizontalPadding =
                    actionHorizontalPadding,
                minimumTouchTarget =
                    minimumTouchTarget
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
            SETTINGS_DOUBLE_COLUMN_SELECTION_ADVANTAGE
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
            minimumTouchTarget,

        gameQuestionAreaHeight =
            minimumTouchTarget,
        gameAnswerButtonHeight =
            minimumTouchTarget,
        gameSectionSpacing =
            smallSpacing
    )
}

private fun calculateBasicSettingsBaseScale(
    width: Dp,
    height: Dp,
    layoutMode: BoardLayoutMode,
    contentHorizontalPadding: Dp,
    contentVerticalPadding: Dp,
    tinySpacing: Dp,
    mediumSpacing: Dp,
    largeSpacing: Dp,
    actionHorizontalPadding: Dp,
    minimumTouchTarget: Dp
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

    val touchTarget =
        minimumTouchTarget.value

    return calculateLargestFittingBaseScale(
        maximumCandidate =
            maxOf(
                width.value,
                height.value
            )
    ) { baseScale ->

        val displayHeight =
            baseScale *
                    SETTINGS_DISPLAY_TEXT_RATIO *
                    SETTINGS_CHALKTASTIC_LINE_HEIGHT_FACTOR

        val primaryHeight =
            baseScale *
                    SETTINGS_PRIMARY_ACTION_TEXT_RATIO *
                    SETTINGS_CHALKTASTIC_LINE_HEIGHT_FACTOR

        val headingHeight =
            baseScale *
                    SETTINGS_HEADING_TEXT_RATIO *
                    SETTINGS_CHALKTASTIC_LINE_HEIGHT_FACTOR

        val bodyHeight =
            baseScale *
                    SETTINGS_BODY_TEXT_RATIO *
                    SETTINGS_CHALKTASTIC_LINE_HEIGHT_FACTOR

        val compactHeight =
            baseScale *
                    SETTINGS_COMPACT_TEXT_RATIO *
                    SETTINGS_CHALKTASTIC_LINE_HEIGHT_FACTOR

        val titleWidth =
            baseScale *
                    SETTINGS_DISPLAY_TEXT_RATIO *
                    SETTINGS_TITLE_WIDTH_EM

        val operationsLabelWidth =
            baseScale *
                    SETTINGS_HEADING_TEXT_RATIO *
                    SETTINGS_OPERATIONS_WIDTH_EM

        val questionsLabelWidth =
            baseScale *
                    SETTINGS_BODY_TEXT_RATIO *
                    SETTINGS_QUESTIONS_WIDTH_EM

        val biggestNumberLabelWidth =
            baseScale *
                    SETTINGS_BODY_TEXT_RATIO *
                    SETTINGS_BIGGEST_NUMBER_WIDTH_EM

        val maximumOperandWidth =
            baseScale *
                    SETTINGS_HEADING_TEXT_RATIO *
                    SETTINGS_MAXIMUM_OPERAND_WIDTH_EM

        val tabHeight =
            maxOf(
                touchTarget,
                bodyHeight +
                        tinySpacing.value * 2f
            )

        val headingActionHeight =
            maxOf(
                touchTarget,
                headingHeight +
                        tinySpacing.value * 2f
            )

        val primaryActionHeight =
            maxOf(
                touchTarget,
                primaryHeight +
                        tinySpacing.value * 2f
            )

        val bodyActionHeight =
            maxOf(
                touchTarget,
                bodyHeight +
                        tinySpacing.value * 2f
            )

        val compactActionHeight =
            maxOf(
                touchTarget,
                compactHeight
            )

        val headingActionWidth =
            maxOf(
                touchTarget,
                baseScale *
                        SETTINGS_HEADING_TEXT_RATIO *
                        SETTINGS_CONTROL_SYMBOL_WIDTH_EM +
                        actionHorizontalPadding.value * 2f
            )

        val primaryActionWidth =
            maxOf(
                touchTarget,
                baseScale *
                        SETTINGS_PRIMARY_ACTION_TEXT_RATIO *
                        SETTINGS_CONTROL_SYMBOL_WIDTH_EM +
                        actionHorizontalPadding.value * 2f
            )

        val operationRowWidth =
            primaryActionWidth *
                    SETTINGS_OPERATION_COUNT +
                    tinySpacing.value *
                    (
                            SETTINGS_OPERATION_COUNT -
                                    1
                            )

        val operationWidth =
            maxOf(
                operationsLabelWidth,
                operationRowWidth
            )

        val questionValueWidth =
            baseScale *
                    SETTINGS_HEADING_TEXT_RATIO *
                    SETTINGS_MAXIMUM_QUESTION_VALUE_WIDTH_EM

        val questionControlWidth =
            headingActionWidth * 2f +
                    questionValueWidth +
                    mediumSpacing.value * 2f

        val questionWidth =
            maxOf(
                questionsLabelWidth,
                questionControlWidth
            )

        val maximumControlWidth =
            headingActionWidth * 2f +
                    maximumOperandWidth +
                    mediumSpacing.value * 2f

        val quickStepTextWidth =
            baseScale *
                    SETTINGS_COMPACT_TEXT_RATIO *
                    SETTINGS_LARGEST_QUICK_STEP_WIDTH_EM

        val quickStepActionWidth =
            maxOf(
                touchTarget,
                quickStepTextWidth +
                        tinySpacing.value * 2f
            )

        val quickStepRowWidth =
            quickStepActionWidth *
                    SETTINGS_QUICK_STEP_COUNT +
                    tinySpacing.value *
                    (
                            SETTINGS_QUICK_STEP_COUNT -
                                    1
                            )

        val biggestNumberWidth =
            maxOf(
                biggestNumberLabelWidth,
                maximumControlWidth,
                quickStepRowWidth
            )

        val footerActionWidth =
            maxOf(
                touchTarget,
                baseScale *
                        SETTINGS_BODY_TEXT_RATIO *
                        SETTINGS_RESET_WIDTH_EM +
                        actionHorizontalPadding.value * 2f
            )

        val footerWidth =
            footerActionWidth *
                    SETTINGS_FOOTER_ACTION_COUNT

        val operationHeight =
            headingHeight +
                    primaryActionHeight

        val questionHeight =
            bodyHeight +
                    headingActionHeight

        val biggestNumberHeight =
            bodyHeight +
                    headingActionHeight +
                    compactActionHeight +
                    tinySpacing.value * 2f

        val footerHeight =
            bodyActionHeight

        val shellHeight =
            displayHeight +
                    tabHeight +
                    footerHeight

        val contentFits =
            when (layoutMode) {

                BoardLayoutMode.SingleColumn -> {

                    val requiredContentHeight =
                        operationHeight +
                                questionHeight +
                                biggestNumberHeight

                    operationWidth <=
                            contentWidth &&
                            questionWidth <=
                            contentWidth &&
                            biggestNumberWidth <=
                            contentWidth &&
                            shellHeight +
                            requiredContentHeight <=
                            contentHeight
                }

                BoardLayoutMode.DoubleColumn -> {

                    val requiredContentWidth =
                        operationWidth +
                                questionWidth +
                                biggestNumberWidth +
                                largeSpacing.value * 2f

                    val requiredContentHeight =
                        maxOf(
                            operationHeight,
                            questionHeight,
                            biggestNumberHeight
                        )

                    requiredContentWidth <=
                            contentWidth &&
                            shellHeight +
                            requiredContentHeight <=
                            contentHeight
                }
            }

        titleWidth <=
                contentWidth &&
                footerWidth <=
                contentWidth &&
                contentFits
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

private const val SETTINGS_MINIMUM_REFERENCE_WIDTH_DP =
    180f

private const val SETTINGS_MAXIMUM_REFERENCE_WIDTH_DP =
    1400f

private const val SETTINGS_MINIMUM_REFERENCE_HEIGHT_DP =
    140f

private const val SETTINGS_MAXIMUM_REFERENCE_HEIGHT_DP =
    1200f

private const val SETTINGS_DISPLAY_TEXT_RATIO =
    1.00f

private const val SETTINGS_PROBLEM_TEXT_RATIO =
    1.05f

private const val SETTINGS_PRIMARY_ACTION_TEXT_RATIO =
    0.84f

private const val SETTINGS_HEADING_TEXT_RATIO =
    0.78f

private const val SETTINGS_BODY_TEXT_RATIO =
    0.72f

private const val SETTINGS_COMPACT_TEXT_RATIO =
    0.43f

private const val SETTINGS_MICRO_TEXT_RATIO =
    0.32f

private const val SETTINGS_TITLE_WIDTH_EM =
    9.184f

private const val SETTINGS_OPERATIONS_WIDTH_EM =
    6.950f

private const val SETTINGS_QUESTIONS_WIDTH_EM =
    6.133f

/*
 * Conservative Chalktastic width estimates for the new controls.
 */
private const val SETTINGS_BIGGEST_NUMBER_WIDTH_EM =
    9.2f

private const val SETTINGS_MAXIMUM_OPERAND_WIDTH_EM =
    4.6f

private const val SETTINGS_LARGEST_QUICK_STEP_WIDTH_EM =
    5.6f

private const val SETTINGS_RESET_WIDTH_EM =
    3.458f

private const val SETTINGS_CONTROL_SYMBOL_WIDTH_EM =
    1.0f

private const val SETTINGS_MAXIMUM_QUESTION_VALUE_WIDTH_EM =
    1.731f

private const val SETTINGS_CHALKTASTIC_LINE_HEIGHT_FACTOR =
    1.29f

private const val SETTINGS_OPERATION_COUNT =
    4

private const val SETTINGS_QUICK_STEP_COUNT =
    4

private const val SETTINGS_FOOTER_ACTION_COUNT =
    3

private const val SETTINGS_DOUBLE_COLUMN_SELECTION_ADVANTAGE =
    1.06f

private const val SETTINGS_SMALL_MINIMUM_TOUCH_TARGET_DP =
    40f

private const val SETTINGS_MEDIUM_MINIMUM_TOUCH_TARGET_DP =
    44f

private const val SETTINGS_LARGE_MINIMUM_TOUCH_TARGET_DP =
    48f