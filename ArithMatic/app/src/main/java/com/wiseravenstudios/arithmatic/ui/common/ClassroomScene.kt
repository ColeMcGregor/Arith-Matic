package com.wiseravenstudios.arithmatic.ui.common

import android.view.View
import androidx.annotation.DrawableRes
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.compose.ui.unit.dp
import com.wiseravenstudios.arithmatic.R
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.width

/*
 * All positions and sizes are expressed as fractions of the complete
 * ClassroomScene dimensions.
 */

/* Blackboard */
private const val BOARD_LEFT_FRACTION = 0.13f
private const val BOARD_TOP_FRACTION = 0.16f
private const val BOARD_WIDTH_FRACTION = 0.76f
private const val BOARD_HEIGHT_FRACTION = 0.64f

/*
 * Writable area within the board.
 *
 * These values are fractions of the board itself rather than fractions
 * of the full screen. They keep UI content away from the wooden frame
 * and chalk tray.
 */
private const val BOARD_CONTENT_LEFT_INSET = 0.06f
private const val BOARD_CONTENT_TOP_INSET = 0.05f
private const val BOARD_CONTENT_RIGHT_INSET = 0.105f
private const val BOARD_CONTENT_BOTTOM_INSET = 0.05f

/* Window */
private const val WINDOW_TOP_FRACTION = 0.18f
private const val WINDOW_WIDTH_FRACTION = 0.06f
private const val WINDOW_HEIGHT_FRACTION = 0.27f

/*
 * Foreground objects are anchored to the side walls but raised above
 * the bottom of the screen so they rest within the visible floor.
 */

/* Teacher desk */
private const val TEACHER_DESK_WIDTH_FRACTION = 0.33f
private const val TEACHER_DESK_HEIGHT_FRACTION = 0.22f
private const val TEACHER_DESK_BOTTOM_INSET_FRACTION = 0.055f

/* Student desk and chair */
private const val STUDENT_AREA_WIDTH_FRACTION = 0.33f
private const val STUDENT_AREA_HEIGHT_FRACTION = 0.22f
private const val STUDENT_AREA_BOTTOM_INSET_FRACTION = 0.055f

@Composable
fun ClassroomScene(
    modifier: Modifier = Modifier,
    blackboardContent: @Composable () -> Unit
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        /*
         * Layer 1: Stretchable classroom background.
         */
        NinePatchLayer(
            drawableRes = R.drawable.arithmatic_ui_bg,
            modifier = Modifier.fillMaxSize()
        )

        /*
         * Layer 2: Window.
         */
        Image(
            painter = painterResource(
                id = R.drawable.arithmatic_ui_window
            ),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(
                    y = maxHeight * WINDOW_TOP_FRACTION
                )
                .size(
                    width = maxWidth * WINDOW_WIDTH_FRACTION,
                    height = maxHeight * WINDOW_HEIGHT_FRACTION
                ),
            contentScale = ContentScale.Fit ,
            alignment = Alignment.TopStart

        )

        /*
         * Layer 3: Stretchable classroom board.
         */
        NinePatchLayer(
            drawableRes = R.drawable.arithmatic_ui_board,
            modifier = Modifier
                .offset(
                    x = maxWidth * BOARD_LEFT_FRACTION,
                    y = maxHeight * BOARD_TOP_FRACTION
                )
                .size(
                    width = maxWidth * BOARD_WIDTH_FRACTION,
                    height = maxHeight * BOARD_HEIGHT_FRACTION
                )
        )

        /*
         * Layer 4: Interactive blackboard content.
         *
         * The content area is inset from the board's outer bounds so
         * text and controls remain inside the green writing surface.
         */
        Box(
            modifier = Modifier
                .offset(
                    x = maxWidth * (
                            BOARD_LEFT_FRACTION +
                                    BOARD_WIDTH_FRACTION *
                                    BOARD_CONTENT_LEFT_INSET
                            ),
                    y = maxHeight * (
                            BOARD_TOP_FRACTION +
                                    BOARD_HEIGHT_FRACTION *
                                    BOARD_CONTENT_TOP_INSET
                            )
                )
                .size(
                    width = maxWidth *
                            BOARD_WIDTH_FRACTION *
                            (
                                    1f -
                                            BOARD_CONTENT_LEFT_INSET -
                                            BOARD_CONTENT_RIGHT_INSET
                                    ),
                    height = maxHeight *
                            BOARD_HEIGHT_FRACTION *
                            (
                                    1f -
                                            BOARD_CONTENT_TOP_INSET -
                                            BOARD_CONTENT_BOTTOM_INSET
                                    )
                )
                .clipToBounds()
        ) {
            blackboardContent()
        }

        /*
         * Layer 5: Teacher desk.
         */
        Image(
            painter = painterResource(
                id = R.drawable.arithmatic_ui_teacher_desk
            ),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(
                    y = -(maxHeight * TEACHER_DESK_BOTTOM_INSET_FRACTION)
                )
                .size(
                    width = maxWidth * TEACHER_DESK_WIDTH_FRACTION,
                    height = maxHeight * TEACHER_DESK_HEIGHT_FRACTION
                ),
            contentScale = ContentScale.Fit,
            alignment = Alignment.BottomStart
        )

        /*
         * Layer 6: Student desk and chair.
         */
        Image(
            painter = painterResource(
                id = R.drawable.arithmatic_ui_student_desk_and_chair
            ),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(
                    y = -(maxHeight * STUDENT_AREA_BOTTOM_INSET_FRACTION)
                )
                .size(
                    width = maxWidth * STUDENT_AREA_WIDTH_FRACTION,
                    height = maxHeight * STUDENT_AREA_HEIGHT_FRACTION
                ),
            contentScale = ContentScale.Fit,
            alignment = Alignment.BottomEnd
        )
    }
}

/**
 * Displays an Android NinePatch drawable as the background of a standard
 * Android View.
 */
@Composable
private fun NinePatchLayer(
    @DrawableRes drawableRes: Int,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            View(context).apply {
                background = ContextCompat.getDrawable(
                    context,
                    drawableRes
                )
            }
        },
        update = { view ->
            view.background = ContextCompat.getDrawable(
                view.context,
                drawableRes
            )
        }
    )
}
