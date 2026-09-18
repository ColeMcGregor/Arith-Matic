package com.wiseravenstudios.arithmatic.ui.roundsettings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import com.wiseravenstudios.arithmatic.ui.common.BoardResponsiveMetrics
import kotlinx.coroutines.delay

internal data class RoundSettingsResponsiveSnapshot(
    val widthPx: Int,
    val heightPx: Int,
    val metrics: BoardResponsiveMetrics
)

@Composable
internal fun rememberRoundSettingsResponsiveSnapshot(
    widthPx: Int,
    heightPx: Int,
    metrics: BoardResponsiveMetrics
): RoundSettingsResponsiveSnapshot {

    val latestWidthPx by
    rememberUpdatedState(
        widthPx
    )

    val latestHeightPx by
    rememberUpdatedState(
        heightPx
    )

    val latestMetrics by
    rememberUpdatedState(
        metrics
    )

    var checkedSnapshot by
    remember {
        mutableStateOf(
            RoundSettingsResponsiveSnapshot(
                widthPx =
                    widthPx,
                heightPx =
                    heightPx,
                metrics =
                    metrics
            )
        )
    }

    LaunchedEffect(
        Unit
    ) {
        while (true) {
            delay(
                ROUND_SETTINGS_RESPONSIVE_CHECK_INTERVAL_MS
            )

            val currentWidth =
                latestWidthPx

            val currentHeight =
                latestHeightPx

            val currentMetrics =
                latestMetrics

            if (
                checkedSnapshot.widthPx !=
                currentWidth ||
                checkedSnapshot.heightPx !=
                currentHeight ||
                checkedSnapshot.metrics !=
                currentMetrics
            ) {
                checkedSnapshot =
                    RoundSettingsResponsiveSnapshot(
                        widthPx =
                            currentWidth,
                        heightPx =
                            currentHeight,
                        metrics =
                            currentMetrics
                    )
            }
        }
    }

    return checkedSnapshot
}

private const val ROUND_SETTINGS_RESPONSIVE_CHECK_INTERVAL_MS =
    1000L