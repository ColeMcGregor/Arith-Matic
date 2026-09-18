package com.wiseravenstudios.arithmatic.ui.roundsettings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import com.wiseravenstudios.arithmatic.ui.common.BoardResponsiveMetrics
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic
import kotlin.math.max
import kotlin.math.min

@Composable
fun AdvancedRoundSettingsContent(
    config: PracticeConfig,
    metrics: BoardResponsiveMetrics,
    onConfigChanged: (PracticeConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    val textMeasurer =
        rememberTextMeasurer()

    val density =
        LocalDensity.current

    BoxWithConstraints(
        modifier =
            modifier
    ) {
        val latestWidthPx =
            with(density) {
                maxWidth.roundToPx()
            }

        val latestHeightPx =
            with(density) {
                maxHeight.roundToPx()
            }

        val responsiveSnapshot =
            rememberRoundSettingsResponsiveSnapshot(
                widthPx =
                    latestWidthPx,
                heightPx =
                    latestHeightPx,
                metrics =
                    metrics
            )

        val responsiveMetrics =
            responsiveSnapshot.metrics

        val fittedScale =
            remember(
                responsiveSnapshot,
                config.allowNegatives,
                config.allowDecimals,
                config.focusNumber,
                density
            ) {
                calculateAdvancedTextScale(
                    config =
                        config,
                    metrics =
                        responsiveMetrics,
                    availableWidth =
                        responsiveSnapshot.widthPx,
                    availableHeight =
                        responsiveSnapshot.heightPx,
                    density =
                        density,
                    measureText = {
                            text,
                            fontSize ->

                        textMeasurer
                            .measure(
                                text =
                                    text,
                                style =
                                    TextStyle(
                                        fontFamily =
                                            Chalktastic,
                                        fontSize =
                                            fontSize
                                    ),
                                maxLines =
                                    1,
                                softWrap =
                                    false
                            )
                            .size
                    }
                )
            }

        val fittedMetrics =
            responsiveMetrics.withAdvancedTextScale(
                fittedScale
            )

        Column(
            modifier =
                Modifier.fillMaxSize(),
            verticalArrangement =
                Arrangement.spacedBy(
                    space =
                        fittedMetrics.smallSpacing,
                    alignment =
                        Alignment.CenterVertically
                ),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {
            BooleanSetting(
                label =
                    "Negatives?",
                enabled =
                    config.allowNegatives,
                metrics =
                    fittedMetrics,
                onToggle = {
                    onConfigChanged(
                        config.copy(
                            allowNegatives =
                                !config.allowNegatives
                        )
                    )
                }
            )

            BooleanSetting(
                label =
                    "Decimals?",
                enabled =
                    config.allowDecimals,
                metrics =
                    fittedMetrics,
                onToggle = {
                    onConfigChanged(
                        config.copy(
                            allowDecimals =
                                !config.allowDecimals
                        )
                    )
                }
            )

            FocusNumberSetting(
                focusNumber =
                    config.focusNumber,
                maximumOperand =
                    config.maximumOperand,
                metrics =
                    fittedMetrics,
                onFocusNumberChanged = {
                        focusNumber ->

                    onConfigChanged(
                        config.copy(
                            focusNumber =
                                focusNumber
                        )
                    )
                }
            )
        }
    }
}

private fun calculateAdvancedTextScale(
    config: PracticeConfig,
    metrics: BoardResponsiveMetrics,
    availableWidth: Int,
    availableHeight: Int,
    density: Density,
    measureText: (
        text: String,
        fontSize: TextUnit
    ) -> IntSize
): Float {
    if (
        availableWidth <= 0 ||
        availableHeight <= 0
    ) {
        return MINIMUM_ADVANCED_TEXT_SCALE
    }

    val smallSpacing =
        with(density) {
            metrics.smallSpacing.roundToPx()
        }

    val tinySpacing =
        with(density) {
            metrics.tinySpacing.roundToPx()
        }

    val actionHorizontalPadding =
        with(density) {
            metrics.actionHorizontalPadding.roundToPx()
        }

    val negativesLabel =
        measureText(
            "Negatives?",
            metrics.bodyTextSize
        )

    val negativesAction =
        measureText(
            if (
                config.allowNegatives
            ) {
                "On"
            } else {
                "Off"
            },
            metrics.bodyTextSize
        )

    val decimalsLabel =
        measureText(
            "Decimals?",
            metrics.bodyTextSize
        )

    val decimalsAction =
        measureText(
            if (
                config.allowDecimals
            ) {
                "On"
            } else {
                "Off"
            },
            metrics.bodyTextSize
        )

    val focusLabel =
        measureText(
            "Focus Number",
            metrics.bodyTextSize
        )

    val minus =
        measureText(
            "-",
            metrics.bodyTextSize
        )

    val focusValue =
        measureText(
            config.focusNumber
                ?.toString()
                ?: "Off",
            metrics.bodyTextSize
        )

    val plus =
        measureText(
            "+",
            metrics.bodyTextSize
        )

    val booleanFixedWidth =
        smallSpacing +
                actionHorizontalPadding * 2

    val booleanFixedHeight =
        tinySpacing * 2

    val negativesWidth =
        AdvancedScaleRequirement(
            scalable =
                negativesLabel.width +
                        negativesAction.width,
            fixed =
                booleanFixedWidth
        )

    val decimalsWidth =
        AdvancedScaleRequirement(
            scalable =
                decimalsLabel.width +
                        decimalsAction.width,
            fixed =
                booleanFixedWidth
        )

    val negativesHeight =
        AdvancedScaleRequirement(
            scalable =
                max(
                    negativesLabel.height,
                    negativesAction.height
                ),
            fixed =
                booleanFixedHeight
        )

    val decimalsHeight =
        AdvancedScaleRequirement(
            scalable =
                max(
                    decimalsLabel.height,
                    decimalsAction.height
                ),
            fixed =
                booleanFixedHeight
        )

    val focusRowWidth =
        AdvancedScaleRequirement(
            scalable =
                minus.width +
                        focusValue.width +
                        plus.width,
            fixed =
                actionHorizontalPadding * 6 +
                        smallSpacing * 2
        )

    val focusLabelWidth =
        AdvancedScaleRequirement(
            scalable =
                focusLabel.width,
            fixed =
                0
        )

    val focusRowHeight =
        maxOf(
            minus.height,
            focusValue.height,
            plus.height
        )

    val focusHeight =
        AdvancedScaleRequirement(
            scalable =
                focusLabel.height +
                        focusRowHeight,
            fixed =
                tinySpacing * 7
        )

    val widthScale =
        minOf(
            negativesWidth.scaleFor(
                availableWidth.toFloat()
            ),
            decimalsWidth.scaleFor(
                availableWidth.toFloat()
            ),
            focusLabelWidth.scaleFor(
                availableWidth.toFloat()
            ),
            focusRowWidth.scaleFor(
                availableWidth.toFloat()
            )
        )

    val outerVerticalSpacing =
        smallSpacing * 2

    val availableContentHeight =
        (
                availableHeight -
                        outerVerticalSpacing
                )
            .coerceAtLeast(
                0
            )
            .toFloat()

    val totalHeightRequirement =
        combineVerticalRequirements(
            negativesHeight,
            decimalsHeight,
            focusHeight
        )

    val heightScale =
        totalHeightRequirement.scaleFor(
            availableContentHeight
        )

    return min(
        widthScale,
        heightScale
    ).coerceIn(
        MINIMUM_ADVANCED_TEXT_SCALE,
        MAXIMUM_ADVANCED_TEXT_SCALE
    )
}

private fun combineVerticalRequirements(
    vararg requirements: AdvancedScaleRequirement
): AdvancedScaleRequirement {
    return AdvancedScaleRequirement(
        scalable =
            requirements.sumOf {
                it.scalable
            },
        fixed =
            requirements.sumOf {
                it.fixed
            }
    )
}

private fun AdvancedScaleRequirement.scaleFor(
    available: Float
): Float {
    if (scalable <= 0) {
        return if (
            fixed <= available
        ) {
            1f
        } else {
            MINIMUM_ADVANCED_TEXT_SCALE
        }
    }

    return (
            (
                    available -
                            fixed.toFloat()
                    ) /
                    scalable.toFloat()
            )
        .coerceAtMost(
            1f
        )
}

private fun BoardResponsiveMetrics.withAdvancedTextScale(
    scale: Float
): BoardResponsiveMetrics {
    return copy(
        problemTextSize =
            problemTextSize.scaledForAdvanced(
                scale
            ),
        displayTextSize =
            displayTextSize.scaledForAdvanced(
                scale
            ),
        primaryActionTextSize =
            primaryActionTextSize.scaledForAdvanced(
                scale
            ),
        widePrimaryActionTextSize =
            widePrimaryActionTextSize.scaledForAdvanced(
                scale
            ),
        headingTextSize =
            headingTextSize.scaledForAdvanced(
                scale
            ),
        bodyTextSize =
            bodyTextSize.scaledForAdvanced(
                scale
            ),
        compactTextSize =
            compactTextSize.scaledForAdvanced(
                scale
            ),
        microTextSize =
            microTextSize.scaledForAdvanced(
                scale
            )
    )
}

private fun TextUnit.scaledForAdvanced(
    scale: Float
): TextUnit {
    return (
            value *
                    scale
            ).sp
}

@Composable
private fun BooleanSetting(
    label: String,
    enabled: Boolean,
    metrics: BoardResponsiveMetrics,
    onToggle: () -> Unit
) {
    Row(
        horizontalArrangement =
            Arrangement.spacedBy(
                metrics.smallSpacing
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Text(
            text =
                label,
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.bodyTextSize,
            maxLines =
                1,
            softWrap =
                false
        )

        ChalkTextAction(
            text =
                if (enabled) {
                    "On"
                } else {
                    "Off"
                },
            color =
                if (enabled) {
                    ChalkColors.PastelGreen
                } else {
                    ChalkColors.PastelPink
                },
            metrics =
                metrics,
            fontSize =
                metrics.bodyTextSize,
            paddingTop =
                metrics.tinySpacing,
            paddingBottom =
                metrics.tinySpacing,
            onClick =
                onToggle
        )
    }
}

@Composable
private fun FocusNumberSetting(
    focusNumber: Int?,
    maximumOperand: Int,
    metrics: BoardResponsiveMetrics,
    onFocusNumberChanged: (Int?) -> Unit
) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(
                metrics.tinySpacing
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text =
                "Focus Number",
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.bodyTextSize,
            maxLines =
                1,
            softWrap =
                false
        )

        Row(
            horizontalArrangement =
                Arrangement.spacedBy(
                    metrics.smallSpacing
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            ChalkTextAction(
                text =
                    "-",
                color =
                    ChalkColors.PastelBlue,
                metrics =
                    metrics,
                fontSize =
                    metrics.bodyTextSize,
                paddingTop =
                    metrics.tinySpacing,
                paddingBottom =
                    metrics.tinySpacing,
                onClick = {
                    when {
                        focusNumber == null -> {
                            Unit
                        }

                        focusNumber >
                                PracticeConfig.MIN_FOCUS_NUMBER -> {
                            onFocusNumberChanged(
                                focusNumber - 1
                            )
                        }

                        else -> {
                            onFocusNumberChanged(
                                null
                            )
                        }
                    }
                }
            )

            ChalkTextAction(
                text =
                    focusNumber
                        ?.toString()
                        ?: "Off",
                color =
                    if (
                        focusNumber == null
                    ) {
                        ChalkColors.PastelPink
                    } else {
                        ChalkColors.PastelGreen
                    },
                metrics =
                    metrics,
                fontSize =
                    metrics.bodyTextSize,
                paddingTop =
                    metrics.tinySpacing,
                paddingBottom =
                    metrics.tinySpacing,
                onClick = {
                    if (
                        focusNumber == null
                    ) {
                        onFocusNumberChanged(
                            PracticeConfig.MIN_FOCUS_NUMBER
                        )
                    } else {
                        onFocusNumberChanged(
                            null
                        )
                    }
                }
            )

            ChalkTextAction(
                text =
                    "+",
                color =
                    ChalkColors.PastelBlue,
                metrics =
                    metrics,
                fontSize =
                    metrics.bodyTextSize,
                paddingTop =
                    metrics.tinySpacing,
                paddingBottom =
                    metrics.tinySpacing,
                onClick = {
                    when {
                        focusNumber == null -> {
                            onFocusNumberChanged(
                                PracticeConfig.MIN_FOCUS_NUMBER
                            )
                        }

                        focusNumber <
                                maximumOperand -> {
                            onFocusNumberChanged(
                                focusNumber + 1
                            )
                        }

                        else -> {
                            Unit
                        }
                    }
                }
            )
        }
    }
}

private data class AdvancedScaleRequirement(
    val scalable: Int,
    val fixed: Int
)

private const val MINIMUM_ADVANCED_TEXT_SCALE =
    0.10f

private const val MAXIMUM_ADVANCED_TEXT_SCALE =
    1.00f