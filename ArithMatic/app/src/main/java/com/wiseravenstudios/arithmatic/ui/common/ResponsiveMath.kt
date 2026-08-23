package com.wiseravenstudios.arithmatic.ui.common

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sqrt

/**
 * Builds the shared description of the available board space.
 *
 * Board-specific metric calculators use this environment to decide
 * spacing, geometry, and structural layout.
 *
 * typographyScale remains available temporarily for boards that have
 * not yet migrated to constraint-driven typography.
 */
internal fun createBoardEnvironment(
    width: Dp,
    height: Dp,
    minimumReferenceWidth: Float,
    maximumReferenceWidth: Float,
    minimumReferenceHeight: Float,
    maximumReferenceHeight: Float
): BoardEnvironment {
    validateBoardSize(
        width = width,
        height = height
    )

    val widthValue =
        width.value

    val heightValue =
        height.value

    val aspectRatio =
        widthValue /
                heightValue

    /*
     * Geometric mean gives one useful size value from board area.
     *
     * Rotation does not change this value.
     */
    val effectiveSize =
        sqrt(
            widthValue *
                    heightValue
        )

    /*
     * The shortest side tracks the board's most constrained dimension.
     */
    val shortestSide =
        minOf(
            width,
            height
        )

    val sizeBand =
        calculateBoardSizeBand(
            effectiveSize =
                effectiveSize
        )

    val shape =
        calculateBoardShape(
            aspectRatio =
                aspectRatio
        )

    val widthScale =
        normalize(
            value = widthValue,
            minimum =
                minimumReferenceWidth,
            maximum =
                maximumReferenceWidth
        )

    val heightScale =
        normalize(
            value = heightValue,
            minimum =
                minimumReferenceHeight,
            maximum =
                maximumReferenceHeight
        )

    /*
     * Kept for boards that still use the previous typography system.
     *
     * Boards migrated to constraint-driven typography should not use
     * this value to determine their final font sizes.
     */
    val typographyScale =
        calculateTypographyScale(
            widthScale = widthScale,
            heightScale = heightScale,
            aspectRatio = aspectRatio
        )

    return BoardEnvironment(
        width = width,
        height = height,
        aspectRatio = aspectRatio,
        effectiveSize = effectiveSize,
        shortestSide = shortestSide,

        sizeBand = sizeBand,
        shape = shape,

        widthScale = widthScale,
        heightScale = heightScale,
        typographyScale = typographyScale
    )
}

/**
 * Verifies that the board has usable dimensions.
 */
internal fun validateBoardSize(
    width: Dp,
    height: Dp
) {
    require(width > 0.dp) {
        "Board width must be greater than zero."
    }

    require(height > 0.dp) {
        "Board height must be greater than zero."
    }
}

/**
 * Selects the broad board-size range.
 *
 * effectiveSize is sqrt(width * height).
 *
 * Size bands remain useful for spacing and other non-typographic
 * responsive decisions.
 */
internal fun calculateBoardSizeBand(
    effectiveSize: Float
): BoardSizeBand {
    require(effectiveSize > 0f) {
        "Effective board size must be greater than zero."
    }

    return when {
        effectiveSize <
                SMALL_TO_MEDIUM_EFFECTIVE_SIZE_DP -> {
            BoardSizeBand.Small
        }

        effectiveSize <
                MEDIUM_TO_LARGE_EFFECTIVE_SIZE_DP -> {
            BoardSizeBand.Medium
        }

        else -> {
            BoardSizeBand.Large
        }
    }
}

/**
 * Describes the physical shape of the available board space.
 *
 * Shape does not decide the board's column count.
 * The board-specific calculator makes that decision.
 */
internal fun calculateBoardShape(
    aspectRatio: Float
): BoardShape {
    require(aspectRatio > 0f) {
        "Aspect ratio must be greater than zero."
    }

    return when {
        aspectRatio <
                NARROW_TALL_MAXIMUM_ASPECT_RATIO -> {
            BoardShape.NarrowTall
        }

        aspectRatio <
                WIDE_MINIMUM_ASPECT_RATIO -> {
            BoardShape.Balanced
        }

        else -> {
            BoardShape.Wide
        }
    }
}

/**
 * Converts a value to a scale from 0 to 1.
 */
internal fun normalize(
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
 * Finds the largest common typography base scale for which the
 * supplied layout constraints still fit.
 *
 * The caller owns the geometry. This helper only searches for the
 * largest scale accepted by the fit predicate.
 *
 * This allows a board to preserve fixed ratios between Display,
 * PrimaryAction, Compact, and other text roles while letting the
 * entire typography system grow or shrink with the actual available
 * board space.
 */
internal fun calculateLargestFittingBaseScale(
    maximumCandidate: Float,
    fits: (Float) -> Boolean
): Float {
    require(maximumCandidate > 0f) {
        "Maximum typography candidate must be greater than zero."
    }

    var lowerBound =
        0f

    var upperBound =
        maximumCandidate

    repeat(
        FITTING_SCALE_SEARCH_ITERATIONS
    ) {
        val candidate =
            (
                    lowerBound +
                            upperBound
                    ) / 2f

        if (fits(candidate)) {
            lowerBound =
                candidate
        } else {
            upperBound =
                candidate
        }
    }

    return lowerBound
}

/**
 * Calculates typography scaling from both board dimensions.
 *
 * This is the previous responsive typography model.
 *
 * It remains available while other boards are migrated to the new
 * constraint-driven base-scale model.
 */
internal fun calculateTypographyScale(
    widthScale: Float,
    heightScale: Float,
    aspectRatio: Float
): Float {
    require(aspectRatio > 0f) {
        "Aspect ratio must be greater than zero."
    }

    val widthWeight =
        calculateTypographyWidthWeight(
            aspectRatio =
                aspectRatio
        )

    val heightWeight =
        1f -
                widthWeight

    val safeWidthScale =
        widthScale.coerceAtLeast(
            MINIMUM_SCALE_DIVISOR
        )

    val safeHeightScale =
        heightScale.coerceAtLeast(
            MINIMUM_SCALE_DIVISOR
        )

    return (
            1f /
                    (
                            widthWeight /
                                    safeWidthScale +
                                    heightWeight /
                                    safeHeightScale
                            )
            )
        .coerceIn(
            0f,
            1f
        )
}

/**
 * Calculates how strongly width affects the previous typography model.
 *
 * Narrow portrait:
 * width 70%, height 30%.
 *
 * Square:
 * width 50%, height 50%.
 *
 * Wide landscape:
 * width 40%, height 60%.
 */
private fun calculateTypographyWidthWeight(
    aspectRatio: Float
): Float {
    return if (aspectRatio <= 1f) {
        mapClamped(
            value = aspectRatio,
            inputMinimum =
                TYPOGRAPHY_PORTRAIT_LIMIT,
            inputMaximum = 1f,
            outputMinimum =
                TYPOGRAPHY_PORTRAIT_WIDTH_WEIGHT,
            outputMaximum =
                TYPOGRAPHY_SQUARE_WIDTH_WEIGHT
        )
    } else {
        mapClamped(
            value = aspectRatio,
            inputMinimum = 1f,
            inputMaximum =
                TYPOGRAPHY_LANDSCAPE_LIMIT,
            outputMinimum =
                TYPOGRAPHY_SQUARE_WIDTH_WEIGHT,
            outputMaximum =
                TYPOGRAPHY_LANDSCAPE_WIDTH_WEIGHT
        )
    }
}

/**
 * Produces a responsive font size inside a board-selected range.
 *
 * Retained for boards that have not yet migrated to
 * constraint-driven typography.
 */
internal fun responsiveSp(
    scale: Float,
    minimum: Float,
    maximum: Float,
    multiplier: Float = 1f
): TextUnit {
    require(maximum >= minimum) {
        "Responsive SP maximum must not be less than its minimum."
    }

    val interpolated =
        interpolate(
            scale =
                scale.coerceIn(
                    0f,
                    1f
                ),
            minimum = minimum,
            maximum = maximum
        )

    val adjusted =
        interpolated *
                multiplier

    return adjusted
        .coerceIn(
            minimum,
            maximum
        )
        .sp
}

/**
 * Produces responsive spacing or geometry.
 */
internal fun responsiveDp(
    scale: Float,
    minimum: Float,
    maximum: Float
): Dp {
    require(maximum >= minimum) {
        "Responsive DP maximum must not be less than its minimum."
    }

    return interpolate(
        scale =
            scale.coerceIn(
                0f,
                1f
            ),
        minimum = minimum,
        maximum = maximum
    ).dp
}

/**
 * Performs linear interpolation.
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

/**
 * Maps one bounded range to another bounded range.
 */
private fun mapClamped(
    value: Float,
    inputMinimum: Float,
    inputMaximum: Float,
    outputMinimum: Float,
    outputMaximum: Float
): Float {
    require(inputMaximum > inputMinimum) {
        "Input maximum must exceed input minimum."
    }

    val fraction =
        (
                (value - inputMinimum) /
                        (inputMaximum - inputMinimum)
                )
            .coerceIn(
                0f,
                1f
            )

    return outputMinimum +
            (
                    outputMaximum -
                            outputMinimum
                    ) * fraction
}

/*
 * ============================================================
 * SIZE BANDS
 * ============================================================
 */

private const val SMALL_TO_MEDIUM_EFFECTIVE_SIZE_DP =
    650f

private const val MEDIUM_TO_LARGE_EFFECTIVE_SIZE_DP =
    950f

/*
 * ============================================================
 * SHAPE CLASSIFICATION
 * ============================================================
 */

private const val NARROW_TALL_MAXIMUM_ASPECT_RATIO =
    0.80f

private const val WIDE_MINIMUM_ASPECT_RATIO =
    1.35f

/*
 * ============================================================
 * LEGACY TYPOGRAPHY SHAPE WEIGHTING
 * ============================================================
 *
 * Retained until all boards are migrated away from the previous
 * responsiveSp typography model.
 */

private const val TYPOGRAPHY_PORTRAIT_LIMIT =
    0.50f

private const val TYPOGRAPHY_LANDSCAPE_LIMIT =
    1.50f

private const val TYPOGRAPHY_PORTRAIT_WIDTH_WEIGHT =
    0.70f

private const val TYPOGRAPHY_SQUARE_WIDTH_WEIGHT =
    0.50f

private const val TYPOGRAPHY_LANDSCAPE_WIDTH_WEIGHT =
    0.40f

private const val MINIMUM_SCALE_DIVISOR =
    0.001f

/*
 * ============================================================
 * CONSTRAINT-DRIVEN TYPOGRAPHY
 * ============================================================
 */

private const val FITTING_SCALE_SEARCH_ITERATIONS =
    24