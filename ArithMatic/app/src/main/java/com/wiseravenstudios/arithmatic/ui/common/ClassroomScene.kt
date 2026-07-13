package com.wiseravenstudios.arithmatic.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.wiseravenstudios.arithmatic.R

private const val BOARD_LEFT_FRACTION = 0.16f
private const val BOARD_TOP_FRACTION = 0.23f
private const val BOARD_WIDTH_FRACTION = 0.68f
private const val BOARD_HEIGHT_FRACTION = 0.52f

@Composable
fun ClassroomScene(
    modifier: Modifier = Modifier,
    blackboardContent: @Composable () -> Unit
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.classroom_scene),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Box(
            modifier = Modifier
                .offset(
                    x = maxWidth * BOARD_LEFT_FRACTION,
                    y = maxHeight * BOARD_TOP_FRACTION
                )
                .size(
                    width = maxWidth * BOARD_WIDTH_FRACTION,
                    height = maxHeight * BOARD_HEIGHT_FRACTION
                )
                .clipToBounds()
        ) {
            blackboardContent()
        }
    }
}