package com.wiseravenstudios.arithmatic.ui.common

import android.graphics.Rect
import android.view.View
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.wiseravenstudios.arithmatic.R

/*
 * All classroom object positions and sizes are expressed as fractions of
 * the complete ClassroomScene dimensions.
 */

/* Blackboard */
private const val BOARD_LEFT_FRACTION = 0.13f
private const val BOARD_TOP_FRACTION = 0.16f
private const val BOARD_WIDTH_FRACTION = 0.76f
private const val BOARD_HEIGHT_FRACTION = 0.64f

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

/**
 * Padding encoded into a NinePatch drawable's content region.
 */
private data class NinePatchContentInsets(
    val left: Dp,
    val top: Dp,
    val right: Dp,
    val bottom: Dp
)

@Composable
fun ClassroomScene(
    modifier: Modifier = Modifier,
    blackboardContent: @Composable () -> Unit
) {
    val boardContentInsets =
        rememberNinePatchContentInsets(
            drawableRes =
                R.drawable.arithmatic_ui_board
        )

    BoxWithConstraints(
        modifier =
            modifier.fillMaxSize()
    ) {
        val boardWidth =
            maxWidth *
                    BOARD_WIDTH_FRACTION

        val boardHeight =
            maxHeight *
                    BOARD_HEIGHT_FRACTION

        val boardLeft =
            maxWidth *
                    BOARD_LEFT_FRACTION

        val boardTop =
            maxHeight *
                    BOARD_TOP_FRACTION

        /*
         * Layer 1: Stretchable classroom background.
         */
        NinePatchLayer(
            drawableRes =
                R.drawable.arithmatic_ui_bg,
            modifier =
                Modifier.fillMaxSize()
        )

        /*
         * Layer 2: Window.
         */
        Image(
            painter =
                painterResource(
                    id =
                        R.drawable.arithmatic_ui_window
                ),
            contentDescription =
                null,
            modifier = Modifier
                .align(
                    Alignment.TopStart
                )
                .offset(
                    y =
                        maxHeight *
                                WINDOW_TOP_FRACTION
                )
                .size(
                    width =
                        maxWidth *
                                WINDOW_WIDTH_FRACTION,
                    height =
                        maxHeight *
                                WINDOW_HEIGHT_FRACTION
                ),
            contentScale =
                ContentScale.Fit,
            alignment =
                Alignment.TopStart
        )

        /*
         * Layer 3: Stretchable classroom board.
         */
        NinePatchLayer(
            drawableRes =
                R.drawable.arithmatic_ui_board,
            modifier = Modifier
                .offset(
                    x =
                        boardLeft,
                    y =
                        boardTop
                )
                .size(
                    width =
                        boardWidth,
                    height =
                        boardHeight
                )
        )

        /*
         * Layer 4: Interactive blackboard content.
         *
         * The writable bounds come directly from the content-padding
         * region encoded into the board NinePatch.
         */
        Box(
            modifier = Modifier
                .offset(
                    x =
                        boardLeft +
                                boardContentInsets.left,
                    y =
                        boardTop +
                                boardContentInsets.top
                )
                .size(
                    width =
                        (
                                boardWidth -
                                        boardContentInsets.left -
                                        boardContentInsets.right
                                )
                            .coerceAtLeast(
                                Dp.Hairline
                            ),
                    height =
                        (
                                boardHeight -
                                        boardContentInsets.top -
                                        boardContentInsets.bottom
                                )
                            .coerceAtLeast(
                                Dp.Hairline
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
            painter =
                painterResource(
                    id =
                        R.drawable.arithmatic_ui_teacher_desk
                ),
            contentDescription =
                null,
            modifier = Modifier
                .align(
                    Alignment.BottomStart
                )
                .offset(
                    y =
                        -(
                                maxHeight *
                                        TEACHER_DESK_BOTTOM_INSET_FRACTION
                                )
                )
                .size(
                    width =
                        maxWidth *
                                TEACHER_DESK_WIDTH_FRACTION,
                    height =
                        maxHeight *
                                TEACHER_DESK_HEIGHT_FRACTION
                ),
            contentScale =
                ContentScale.Fit,
            alignment =
                Alignment.BottomStart
        )

        /*
         * Layer 6: Student desk and chair.
         */
        Image(
            painter =
                painterResource(
                    id =
                        R.drawable.arithmatic_ui_student_desk_and_chair
                ),
            contentDescription =
                null,
            modifier = Modifier
                .align(
                    Alignment.BottomEnd
                )
                .offset(
                    y =
                        -(
                                maxHeight *
                                        STUDENT_AREA_BOTTOM_INSET_FRACTION
                                )
                )
                .size(
                    width =
                        maxWidth *
                                STUDENT_AREA_WIDTH_FRACTION,
                    height =
                        maxHeight *
                                STUDENT_AREA_HEIGHT_FRACTION
                ),
            contentScale =
                ContentScale.Fit,
            alignment =
                Alignment.BottomEnd
        )
    }
}

/**
 * Reads the content-padding region encoded into a NinePatch drawable.
 *
 * The padding returned by the Android drawable is expressed in physical
 * pixels, so it is converted into Compose Dp before being used to position
 * blackboard content.
 */
@Composable
private fun rememberNinePatchContentInsets(
    @DrawableRes drawableRes: Int
): NinePatchContentInsets {
    val context =
        LocalContext.current

    val density =
        LocalDensity.current

    return remember(
        context,
        density,
        drawableRes
    ) {
        val drawable =
            requireNotNull(
                ContextCompat.getDrawable(
                    context,
                    drawableRes
                )
            ) {
                "Unable to load NinePatch drawable: $drawableRes"
            }

        val padding =
            Rect()

        drawable.getPadding(
            padding
        )

        with(density) {
            NinePatchContentInsets(
                left =
                    padding.left
                        .toDp(),
                top =
                    padding.top
                        .toDp(),
                right =
                    padding.right
                        .toDp(),
                bottom =
                    padding.bottom
                        .toDp()
            )
        }
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
        modifier =
            modifier,
        factory = { context ->
            View(context).apply {
                background =
                    ContextCompat.getDrawable(
                        context,
                        drawableRes
                    )
            }
        },
        update = { view ->
            view.background =
                ContextCompat.getDrawable(
                    view.context,
                    drawableRes
                )
        }
    )
}