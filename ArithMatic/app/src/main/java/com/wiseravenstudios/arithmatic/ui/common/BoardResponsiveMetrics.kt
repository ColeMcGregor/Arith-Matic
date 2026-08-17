package com.wiseravenstudios.arithmatic.ui.common

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Semantic text roles shared by all blackboard UI.
 */
enum class BoardTextRole {
    Problem,
    Display,
    PrimaryAction,
    Heading,
    Body,
    Compact,
    Micro
}

/**
 * Describes the broad structural layout appropriate for the current
 * blackboard content area.
 */
enum class BoardLayoutMode {
    Tall,
    Wide
}

/**
 * Responsive sizing information calculated from the current writable
 * blackboard area.
 */
@Immutable
data class BoardResponsiveMetrics(
    val width: Dp,
    val height: Dp,
    val aspectRatio: Float,
    val layoutMode: BoardLayoutMode,

    val problemTextSize: TextUnit,
    val displayTextSize: TextUnit,
    val primaryActionTextSize: TextUnit,
    val widePrimaryActionTextSize: TextUnit,
    val headingTextSize: TextUnit,
    val bodyTextSize: TextUnit,
    val compactTextSize: TextUnit,
    val microTextSize: TextUnit,

    val tinySpacing: Dp,
    val smallSpacing: Dp,
    val mediumSpacing: Dp,
    val largeSpacing: Dp,
    val extraLargeSpacing: Dp,

    val titleTopSpacing: Dp,
    val titleToActionsSpacing: Dp,
    val actionGroupSpacing: Dp,
    val tallActionTopSpacing: Dp,

    val actionHorizontalPadding: Dp,
    val actionVerticalPadding: Dp,

    val contentHorizontalPadding: Dp,
    val contentVerticalPadding: Dp,

    val minimumTouchTarget: Dp
) {

    fun textSize(
        role: BoardTextRole
    ): TextUnit {
        return when (role) {
            BoardTextRole.Problem ->
                problemTextSize

            BoardTextRole.Display ->
                displayTextSize

            BoardTextRole.PrimaryAction ->
                primaryActionTextSize

            BoardTextRole.Heading ->
                headingTextSize

            BoardTextRole.Body ->
                bodyTextSize

            BoardTextRole.Compact ->
                compactTextSize

            BoardTextRole.Micro ->
                microTextSize
        }
    }

    val isWide: Boolean
        get() =
            layoutMode ==
                    BoardLayoutMode.Wide
}

/**
 * Calculates responsive UI metrics from the currently available
 * writable blackboard dimensions.
 *
 * Width and height are intentionally scaled independently:
 *
 * - Width governs horizontal spacing and horizontal fit.
 * - Height governs vertical spacing and vertical fit.
 * - Typography is constrained by whichever dimension has less room.
 * - Board shape determines the preferred structure.
 * - Near-square boards may switch to Wide early if Tall no longer fits.
 */
fun calculateBoardResponsiveMetrics(
    width: Dp,
    height: Dp
): BoardResponsiveMetrics {
    require(width > 0.dp) {
        "Board width must be greater than zero."
    }

    require(height > 0.dp) {
        "Board height must be greater than zero."
    }

    val aspectRatio =
        width.value /
                height.value

    val widthScale =
        normalize(
            value = width.value,
            minimum =
                MINIMUM_REFERENCE_WIDTH_DP,
            maximum =
                MAXIMUM_REFERENCE_WIDTH_DP
        )

    val heightScale =
        normalize(
            value = height.value,
            minimum =
                MINIMUM_REFERENCE_HEIGHT_DP,
            maximum =
                MAXIMUM_REFERENCE_HEIGHT_DP
        )

    val typographyScale =
        minOf(
            widthScale,
            heightScale
        )

    /*
     * Horizontal values respond primarily to width.
     */
    val actionHorizontalPadding =
        responsiveDp(
            scale = widthScale,
            minimum = 8f,
            maximum = 16f
        )

    val contentHorizontalPadding =
        responsiveDp(
            scale = widthScale,
            minimum = 4f,
            maximum = 14f
        )

    /*
     * General vertical spacing responds primarily to height.
     */
    val tinySpacing =
        responsiveDp(
            scale = heightScale,
            minimum = 2f,
            maximum = 6f
        )

    val smallSpacing =
        responsiveDp(
            scale = heightScale,
            minimum = 4f,
            maximum = 10f
        )

    val mediumSpacing =
        responsiveDp(
            scale = heightScale,
            minimum = 8f,
            maximum = 18f
        )

    val largeSpacing =
        responsiveDp(
            scale = heightScale,
            minimum = 14f,
            maximum = 32f
        )

    val extraLargeSpacing =
        responsiveDp(
            scale = heightScale,
            minimum = 28f,
            maximum = 56f
        )

    val actionVerticalPadding =
        responsiveDp(
            scale = heightScale,
            minimum = 5f,
            maximum = 10f
        )

    val contentVerticalPadding =
        responsiveDp(
            scale = heightScale,
            minimum = 4f,
            maximum = 14f
        )

    /*
     * Preferred Tall-layout spacing is calculated before selecting the
     * structural mode so we can determine whether Tall can still fit.
     */
    val preferredTallTitleTopSpacing =
        responsiveDp(
            scale = heightScale,
            minimum = 28f,
            maximum = 56f
        )

    val preferredTitleToActionsSpacing =
        responsiveDp(
            scale = heightScale,
            minimum = 16f,
            maximum = 60f
        )

    val preferredActionGroupSpacing =
        responsiveDp(
            scale = heightScale,
            minimum = 4f,
            maximum = 18f
        )

    /*
     * Natural Tall typography is used for the fit test.
     *
     * This test does not use the Wide typography reduction because the
     * question being answered is specifically whether Tall can still fit.
     */
    val naturalTallDisplayTextSize =
        responsiveSp(
            scale = typographyScale,
            minimum = 24f,
            maximum = 64f
        )

    val naturalTallPrimaryActionTextSize =
        responsiveSp(
            scale = typographyScale,
            minimum = 26f,
            maximum = 50f
        )

    val estimatedTallTitleHeight =
        naturalTallDisplayTextSize.value *
                TEXT_LINE_HEIGHT_FACTOR

    val estimatedTallActionHeight =
        maxOf(
            MINIMUM_TOUCH_TARGET_DP,
            naturalTallPrimaryActionTextSize.value *
                    TEXT_LINE_HEIGHT_FACTOR +
                    actionVerticalPadding.value * 2f
        )

    /*
     * Tall needs enough height for its title and four vertically stacked
     * actions.
     *
     * Only a small minimum title-to-actions gap is required for the fit
     * test. If more room exists, the final layout can use a larger gap.
     */
    val minimumTallHeight =
        contentVerticalPadding.value * 2f +
                preferredTallTitleTopSpacing.value +
                estimatedTallTitleHeight +
                MINIMUM_TALL_TITLE_TO_ACTION_SPACING_DP +
                estimatedTallActionHeight *
                START_ACTION_COUNT +
                preferredActionGroupSpacing.value *
                (START_ACTION_COUNT - 1)

    val tallLayoutFits =
        height.value >=
                minimumTallHeight

    /*
     * Structural selection:
     *
     * 1. Naturally wide boards always use Wide.
     *
     * 2. A square-ish board may switch to Wide early when four vertical
     *    actions no longer fit. This is especially important for resizable
     *    desktop windows.
     *
     * 3. Truly narrow / portrait boards remain Tall even when constrained.
     *    This prevents small portrait phones from becoming two-column menus.
     */
    val layoutMode =
        when {
            aspectRatio >=
                    WIDE_LAYOUT_ASPECT_RATIO ->
                BoardLayoutMode.Wide

            aspectRatio >=
                    MINIMUM_WIDE_FALLBACK_ASPECT_RATIO &&
                    !tallLayoutFits ->
                BoardLayoutMode.Wide

            else ->
                BoardLayoutMode.Tall
        }

    /*
     * Wide layouts reserve more vertical room by slightly reducing
     * typography.
     */
    val layoutTypographyMultiplier =
        if (
            layoutMode ==
            BoardLayoutMode.Wide
        ) {
            WIDE_TYPOGRAPHY_SCALE
        } else {
            1f
        }

    val problemTextSize =
        responsiveSp(
            scale = typographyScale,
            minimum = 32f,
            maximum = 60f,
            multiplier =
                layoutTypographyMultiplier
        )

    val displayTextSize =
        responsiveSp(
            scale = typographyScale,
            minimum = 24f,
            maximum = 64f,
            multiplier =
                layoutTypographyMultiplier
        )

    val primaryActionTextSize =
        responsiveSp(
            scale = typographyScale,
            minimum = 26f,
            maximum = 50f,
            multiplier =
                layoutTypographyMultiplier
        )

    val headingTextSize =
        responsiveSp(
            scale = typographyScale,
            minimum = 18f,
            maximum = 38f,
            multiplier =
                layoutTypographyMultiplier
        )

    val bodyTextSize =
        responsiveSp(
            scale = typographyScale,
            minimum = 16f,
            maximum = 30f,
            multiplier =
                layoutTypographyMultiplier
        )

    val compactTextSize =
        responsiveSp(
            scale = typographyScale,
            minimum = 14f,
            maximum = 34f,
            multiplier =
                layoutTypographyMultiplier
        )

    val microTextSize =
        responsiveSp(
            scale = typographyScale,
            minimum = 10f,
            maximum = 18f,
            multiplier =
                layoutTypographyMultiplier
        )

    /*
     * Title positioning has different priorities in Tall and Wide layouts.
     *
     * Tall boards can afford to place the title farther down.
     * Wide boards preserve more height for the two action rows.
     */
    val titleTopSpacing =
        if (
            layoutMode ==
            BoardLayoutMode.Wide
        ) {
            responsiveDp(
                scale = heightScale,
                minimum = 10f,
                maximum = 40f
            )
        } else {
            preferredTallTitleTopSpacing
        }

    /*
     * Wide layout:
     *
     * First determine the actual width available to each action column.
     * This caps the action typography on cramped wide boards.
     */
    val wideMenuHorizontalPadding =
        responsiveDp(
            scale = widthScale,
            minimum = 8f,
            maximum = 32f
        )

    val wideColumnGap =
        responsiveDp(
            scale = widthScale,
            minimum = 8f,
            maximum = 32f
        )

    val wideMenuContentWidth =
        (
                width.value -
                        contentHorizontalPadding.value * 2f -
                        wideMenuHorizontalPadding.value * 2f -
                        wideColumnGap.value
                )
            .coerceAtLeast(
                MINIMUM_WIDE_MENU_WIDTH_DP
            )

    val wideColumnWidth =
        wideMenuContentWidth /
                WIDE_COLUMN_COUNT

    val wideTextWidth =
        (
                wideColumnWidth -
                        actionHorizontalPadding.value * 2f
                )
            .coerceAtLeast(
                MINIMUM_WIDE_TEXT_WIDTH_DP
            )

    val widthLimitedActionSize =
        (
                wideTextWidth /
                        LONGEST_PRIMARY_ACTION_WIDTH_FACTOR
                )
            .coerceIn(
                MINIMUM_WIDE_PRIMARY_ACTION_SP,
                primaryActionTextSize.value
            )

    val widePrimaryActionTextSize =
        widthLimitedActionSize.sp

    /*
     * Estimated final sizes used for layout budgeting.
     */
    val estimatedTitleHeight =
        displayTextSize.value *
                TEXT_LINE_HEIGHT_FACTOR

    val estimatedWideActionHeight =
        maxOf(
            MINIMUM_TOUCH_TARGET_DP,
            widePrimaryActionTextSize.value *
                    TEXT_LINE_HEIGHT_FACTOR +
                    actionVerticalPadding.value * 2f
        )

    val estimatedFinalTallActionHeight =
        maxOf(
            MINIMUM_TOUCH_TARGET_DP,
            primaryActionTextSize.value *
                    TEXT_LINE_HEIGHT_FACTOR +
                    actionVerticalPadding.value * 2f
        )

    /*
     * Wide vertical budgeting.
     *
     * First reserve room for the title and both action rows. The remaining
     * height is divided between the larger title-to-actions gap and the
     * smaller action-to-action gap.
     */
    val wideRequiredHeightWithoutGaps =
        contentVerticalPadding.value * 2f +
                titleTopSpacing.value +
                estimatedTitleHeight +
                estimatedWideActionHeight *
                WIDE_ACTION_ROW_COUNT

    val wideAvailableGapSpace =
        (
                height.value -
                        wideRequiredHeightWithoutGaps
                )
            .coerceAtLeast(
                0f
            )

    val wideMinimumActionGap =
        minOf(
            MINIMUM_WIDE_ACTION_GROUP_SPACING_DP,
            wideAvailableGapSpace
        )

    val wideAvailableTitleGap =
        (
                wideAvailableGapSpace -
                        wideMinimumActionGap
                )
            .coerceAtLeast(
                0f
            )

    val wideTitleToActionsSpacing =
        minOf(
            preferredTitleToActionsSpacing.value,
            wideAvailableTitleGap
        )

    val wideRemainingGapSpace =
        (
                wideAvailableGapSpace -
                        wideTitleToActionsSpacing
                )
            .coerceAtLeast(
                0f
            )

    val wideActionGroupSpacing =
        minOf(
            preferredActionGroupSpacing.value,
            wideRemainingGapSpace
        )

    /*
     * Tall vertical budgeting.
     *
     * The title-to-actions gap contracts as vertical space becomes scarce.
     */
    val tallRequiredHeightWithoutTitleGap =
        contentVerticalPadding.value * 2f +
                titleTopSpacing.value +
                estimatedTitleHeight +
                estimatedFinalTallActionHeight *
                START_ACTION_COUNT +
                preferredActionGroupSpacing.value *
                (START_ACTION_COUNT - 1)

    val availableTallActionGap =
        (
                height.value -
                        tallRequiredHeightWithoutTitleGap
                )
            .coerceAtLeast(
                0f
            )

    val tallActionTopSpacing =
        minOf(
            preferredTitleToActionsSpacing.value,
            availableTallActionGap
        ).dp

    val titleToActionsSpacing =
        if (
            layoutMode ==
            BoardLayoutMode.Wide
        ) {
            wideTitleToActionsSpacing.dp
        } else {
            tallActionTopSpacing
        }

    val actionGroupSpacing =
        if (
            layoutMode ==
            BoardLayoutMode.Wide
        ) {
            wideActionGroupSpacing.dp
        } else {
            preferredActionGroupSpacing
        }

    return BoardResponsiveMetrics(
        width =
            width,

        height =
            height,

        aspectRatio =
            aspectRatio,

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
            tallActionTopSpacing,

        actionHorizontalPadding =
            actionHorizontalPadding,

        actionVerticalPadding =
            actionVerticalPadding,

        contentHorizontalPadding =
            contentHorizontalPadding,

        contentVerticalPadding =
            contentVerticalPadding,

        minimumTouchTarget =
            MINIMUM_TOUCH_TARGET_DP.dp
    )
}

private fun normalize(
    value: Float,
    minimum: Float,
    maximum: Float
): Float {
    require(maximum > minimum) {
        "Normalization maximum must exceed its minimum."
    }

    return (
            (value - minimum) /
                    (maximum - minimum)
            )
        .coerceIn(
            0f,
            1f
        )
}

private fun interpolate(
    scale: Float,
    minimum: Float,
    maximum: Float
): Float {
    return minimum +
            (
                    maximum -
                            minimum
                    ) * scale
}

private fun responsiveSp(
    scale: Float,
    minimum: Float,
    maximum: Float,
    multiplier: Float = 1f
): TextUnit {
    val interpolated =
        interpolate(
            scale = scale,
            minimum = minimum,
            maximum = maximum
        )

    val adjusted =
        (interpolated * multiplier)
            .coerceAtLeast(
                minimum
            )

    return adjusted.sp
}

private fun responsiveDp(
    scale: Float,
    minimum: Float,
    maximum: Float
): Dp {
    return interpolate(
        scale = scale,
        minimum = minimum,
        maximum = maximum
    ).dp
}

/*
 * A naturally wide board always uses the two-column structure.
 */
private const val WIDE_LAYOUT_ASPECT_RATIO =
    1.35f

/*
 * Below this ratio, Tall is retained even if its vertical fit estimate is
 * tight. This protects portrait phones from inappropriate two-column
 * layouts.
 */
private const val MINIMUM_WIDE_FALLBACK_ASPECT_RATIO =
    1.0f

private const val MINIMUM_REFERENCE_WIDTH_DP =
    180f

private const val MAXIMUM_REFERENCE_WIDTH_DP =
    700f

private const val MINIMUM_REFERENCE_HEIGHT_DP =
    220f

private const val MAXIMUM_REFERENCE_HEIGHT_DP =
    700f

private const val WIDE_TYPOGRAPHY_SCALE =
    0.92f

private const val TEXT_LINE_HEIGHT_FACTOR =
    1.15f

private const val START_ACTION_COUNT =
    4

private const val WIDE_ACTION_ROW_COUNT =
    2

private const val WIDE_COLUMN_COUNT =
    2f

private const val MINIMUM_TOUCH_TARGET_DP =
    48f

private const val MINIMUM_TALL_TITLE_TO_ACTION_SPACING_DP =
    8f

private const val MINIMUM_WIDE_ACTION_GROUP_SPACING_DP =
    4f

private const val MINIMUM_WIDE_MENU_WIDTH_DP =
    120f

private const val MINIMUM_WIDE_TEXT_WIDTH_DP =
    70f

private const val MINIMUM_WIDE_PRIMARY_ACTION_SP =
    14f

private const val LONGEST_PRIMARY_ACTION_WIDTH_FACTOR =
    7.0f