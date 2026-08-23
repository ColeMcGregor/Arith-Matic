package com.wiseravenstudios.arithmatic.ui.common

import androidx.compose.ui.unit.Dp

@Deprecated(
    message =
        "Use the calculator for the specific board."
)
fun calculateBoardResponsiveMetrics(
    width: Dp,
    height: Dp
): BoardResponsiveMetrics {
    return calculateStartBoardMetrics(
        width = width,
        height = height
    )
}