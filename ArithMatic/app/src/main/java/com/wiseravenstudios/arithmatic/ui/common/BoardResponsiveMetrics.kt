package com.wiseravenstudios.arithmatic.ui.common

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Semantic text roles shared by all blackboard UI.
 *
 * Each role resolves to a responsive text size according to the
 * currently available blackboard content area.
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
 *
 * Values scale continuously between minimum and maximum bounds as the
 * available board space changes.
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
    val headingTextSize: TextUnit,
    val bodyTextSize: TextUnit,
    val compactTextSize: TextUnit,
    val microTextSize: TextUnit,

    val tinySpacing: Dp,
    val smallSpacing: Dp,
    val mediumSpacing: Dp,
    val largeSpacing: Dp,
    val extraLargeSpacing: Dp,

    val actionHorizontalPadding: Dp,
    val actionVerticalPadding: Dp,

    val contentHorizontalPadding: Dp,
    val contentVerticalPadding: Dp,

    val minimumTouchTarget: Dp
) {

    /**
     * Returns the responsive size associated with one semantic text role.
     */
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
 * The shorter board dimension drives the main scale because it represents
 * the dimension most likely to constrain readable content.
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

    val layoutMode =
        if (
            aspectRatio >=
            WIDE_LAYOUT_ASPECT_RATIO
        ) {
            BoardLayoutMode.Wide
        } else {
            BoardLayoutMode.Tall
        }

    val shortSide =
        minOf(
            width.value,
            height.value
        )

    /*
     * Converts the current limiting board dimension into a value from
     * zero to one.
     *
     * Values below the lower reference use the minimum sizes.
     * Values above the upper reference use the maximum sizes.
     */
    val scale =
        normalize(
            value = shortSide,
            minimum =
                MINIMUM_REFERENCE_SHORT_SIDE_DP,
            maximum =
                MAXIMUM_REFERENCE_SHORT_SIDE_DP
        )

    /*
     * Wide boards have less vertical space relative to their width.
     *
     * A small typography reduction preserves vertical room while the
     * structural layout makes greater use of horizontal space.
     */
    val typographyScale =
        if (
            layoutMode ==
            BoardLayoutMode.Wide
        ) {
            WIDE_TYPOGRAPHY_SCALE
        } else {
            1f
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
            responsiveSp(
                scale = scale,
                minimum = 32f,
                maximum = 52f,
                multiplier =
                    typographyScale
            ),

        displayTextSize =
            responsiveSp(
                scale = scale,
                minimum = 24f,
                maximum = 44f,
                multiplier =
                    typographyScale
            ),

        primaryActionTextSize =
            responsiveSp(
                scale = scale,
                minimum = 22f,
                maximum = 38f,
                multiplier =
                    typographyScale
            ),

        headingTextSize =
            responsiveSp(
                scale = scale,
                minimum = 18f,
                maximum = 32f,
                multiplier =
                    typographyScale
            ),

        bodyTextSize =
            responsiveSp(
                scale = scale,
                minimum = 16f,
                maximum = 26f,
                multiplier =
                    typographyScale
            ),

        compactTextSize =
            responsiveSp(
                scale = scale,
                minimum = 12f,
                maximum = 20f,
                multiplier =
                    typographyScale
            ),

        microTextSize =
            responsiveSp(
                scale = scale,
                minimum = 10f,
                maximum = 16f,
                multiplier =
                    typographyScale
            ),

        tinySpacing =
            responsiveDp(
                scale = scale,
                minimum = 2f,
                maximum = 6f
            ),

        smallSpacing =
            responsiveDp(
                scale = scale,
                minimum = 4f,
                maximum = 10f
            ),

        mediumSpacing =
            responsiveDp(
                scale = scale,
                minimum = 8f,
                maximum = 18f
            ),

        largeSpacing =
            responsiveDp(
                scale = scale,
                minimum = 14f,
                maximum = 32f
            ),

        extraLargeSpacing =
            responsiveDp(
                scale = scale,
                minimum = 22f,
                maximum = 50f
            ),

        actionHorizontalPadding =
            responsiveDp(
                scale = scale,
                minimum = 8f,
                maximum = 16f
            ),

        actionVerticalPadding =
            responsiveDp(
                scale = scale,
                minimum = 5f,
                maximum = 10f
            ),

        contentHorizontalPadding =
            responsiveDp(
                scale = scale,
                minimum = 4f,
                maximum = 14f
            ),

        contentVerticalPadding =
            responsiveDp(
                scale = scale,
                minimum = 4f,
                maximum = 14f
            ),

        /*
         * Touch targets remain physically usable rather than shrinking
         * according to available visual space.
         */
        minimumTouchTarget =
            48.dp
    )
}

/**
 * Maps a value into the inclusive range 0 to 1.
 */
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

/**
 * Interpolates continuously between two values.
 */
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

    /*
     * Applying the multiplier after interpolation retains the role's
     * minimum readable size.
     */
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
 * A board becomes structurally wide when horizontal space substantially
 * exceeds vertical space.
 *
 * This is based on the current board shape rather than device orientation.
 */
private const val WIDE_LAYOUT_ASPECT_RATIO =
    1.35f

/*
 * Reference bounds used to map the limiting board dimension onto the
 * responsive scale.
 *
 * Boards outside this range simply remain at the appropriate minimum or
 * maximum values.
 */
private const val MINIMUM_REFERENCE_SHORT_SIDE_DP =
    180f

private const val MAXIMUM_REFERENCE_SHORT_SIDE_DP =
    420f

/*
 * Wide layouts reserve additional vertical room by slightly reducing
 * responsive typography.
 */
private const val WIDE_TYPOGRAPHY_SCALE =
    0.92f