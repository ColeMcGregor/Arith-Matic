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
import androidx.compose.ui.unit.dp
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
 * These values define the reference box used to establish the intended
 * relative size of both foreground furniture pieces.
 *
 * Each drawable keeps its own intrinsic aspect ratio inside this box.
 * Both pieces then use one shared scale factor.
 */
private const val FOREGROUND_REFERENCE_WIDTH_FRACTION = 0.33f
private const val FOREGROUND_REFERENCE_HEIGHT_FRACTION = 0.22f

/* Teacher desk */
private const val TEACHER_DESK_BOTTOM_INSET_FRACTION = 0.055f

/* Student desk and chair */
private const val STUDENT_AREA_BOTTOM_INSET_FRACTION = 0.055f

/*
 * Keeps foreground artwork clear of the outer board frame.
 */
private val FOREGROUND_BOARD_CLEARANCE =
    4.dp

/**
 * Padding encoded into a NinePatch drawable's content region.
 */
private data class NinePatchContentInsets(
    val left: Dp,
    val top: Dp,
    val right: Dp,
    val bottom: Dp
)

/**
 * Width and height of a foreground object.
 */
private data class ForegroundSize(
    val width: Dp,
    val height: Dp
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

    val teacherDeskAspectRatio =
        rememberDrawableAspectRatio(
            drawableRes =
                R.drawable.arithmatic_ui_teacher_desk
        )

    val studentAreaAspectRatio =
        rememberDrawableAspectRatio(
            drawableRes =
                R.drawable.arithmatic_ui_student_desk_and_chair
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

        val boardRight =
            boardLeft +
                    boardWidth

        val boardBottom =
            boardTop +
                    boardHeight

        /*
         * This is the original reference area that used 0.33 of the
         * scene width and 0.22 of the scene height.
         *
         * Each image is fitted into this box without changing its
         * intrinsic aspect ratio.
         */
        val foregroundReferenceWidth =
            maxWidth *
                    FOREGROUND_REFERENCE_WIDTH_FRACTION

        val foregroundReferenceHeight =
            maxHeight *
                    FOREGROUND_REFERENCE_HEIGHT_FRACTION

        val teacherDeskBaseSize =
            calculateAspectFitSize(
                maximumWidth =
                    foregroundReferenceWidth,
                maximumHeight =
                    foregroundReferenceHeight,
                aspectRatio =
                    teacherDeskAspectRatio
            )

        val studentAreaBaseSize =
            calculateAspectFitSize(
                maximumWidth =
                    foregroundReferenceWidth,
                maximumHeight =
                    foregroundReferenceHeight,
                aspectRatio =
                    studentAreaAspectRatio
            )

        val teacherDeskBottomInset =
            maxHeight *
                    TEACHER_DESK_BOTTOM_INSET_FRACTION

        val studentAreaBottomInset =
            maxHeight *
                    STUDENT_AREA_BOTTOM_INSET_FRACTION

        val teacherDeskBottom =
            maxHeight -
                    teacherDeskBottomInset

        val studentAreaBottom =
            maxHeight -
                    studentAreaBottomInset

        /*
         * The teacher desk is safe while either condition is true:
         *
         * - its right edge stays left of the board;
         * - its top edge stays below the board.
         *
         * It overlaps the board only after both limits are crossed.
         */
        val teacherDeskHorizontalGap =
            (
                    boardLeft -
                            FOREGROUND_BOARD_CLEARANCE
                    )
                .coerceAtLeast(
                    0.dp
                )

        val teacherDeskVerticalGap =
            (
                    teacherDeskBottom -
                            boardBottom -
                            FOREGROUND_BOARD_CLEARANCE
                    )
                .coerceAtLeast(
                    0.dp
                )

        val teacherDeskMaximumScale =
            calculateMaximumForegroundScale(
                sceneWidth =
                    maxWidth,
                objectBottom =
                    teacherDeskBottom,
                baseSize =
                    teacherDeskBaseSize,
                horizontalGap =
                    teacherDeskHorizontalGap,
                verticalGap =
                    teacherDeskVerticalGap
            )

        /*
         * The student desk and chair use the mirrored rule on the
         * right side of the board.
         */
        val studentAreaHorizontalGap =
            (
                    maxWidth -
                            boardRight -
                            FOREGROUND_BOARD_CLEARANCE
                    )
                .coerceAtLeast(
                    0.dp
                )

        val studentAreaVerticalGap =
            (
                    studentAreaBottom -
                            boardBottom -
                            FOREGROUND_BOARD_CLEARANCE
                    )
                .coerceAtLeast(
                    0.dp
                )

        val studentAreaMaximumScale =
            calculateMaximumForegroundScale(
                sceneWidth =
                    maxWidth,
                objectBottom =
                    studentAreaBottom,
                baseSize =
                    studentAreaBaseSize,
                horizontalGap =
                    studentAreaHorizontalGap,
                verticalGap =
                    studentAreaVerticalGap
            )

        /*
         * Both foreground pieces use the smaller maximum scale.
         *
         * If either piece reaches its board or scene limit first,
         * both pieces stop growing. This preserves their relative size.
         */
        val sharedForegroundScale =
            minOf(
                teacherDeskMaximumScale,
                studentAreaMaximumScale
            )
                .coerceAtLeast(
                    0f
                )

        val teacherDeskSize =
            ForegroundSize(
                width =
                    teacherDeskBaseSize.width *
                            sharedForegroundScale,
                height =
                    teacherDeskBaseSize.height *
                            sharedForegroundScale
            )

        val studentAreaSize =
            ForegroundSize(
                width =
                    studentAreaBaseSize.width *
                            sharedForegroundScale,
                height =
                    studentAreaBaseSize.height *
                            sharedForegroundScale
            )

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
                        -teacherDeskBottomInset
                )
                .size(
                    width =
                        teacherDeskSize.width,
                    height =
                        teacherDeskSize.height
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
                        -studentAreaBottomInset
                )
                .size(
                    width =
                        studentAreaSize.width,
                    height =
                        studentAreaSize.height
                ),
            contentScale =
                ContentScale.Fit,
            alignment =
                Alignment.BottomEnd
        )
    }
}

/**
 * Fits a rectangle inside the supplied bounds while preserving the
 * drawable's intrinsic aspect ratio.
 */
private fun calculateAspectFitSize(
    maximumWidth: Dp,
    maximumHeight: Dp,
    aspectRatio: Float
): ForegroundSize {
    if (
        maximumWidth <= 0.dp ||
        maximumHeight <= 0.dp ||
        aspectRatio <= 0f
    ) {
        return ForegroundSize(
            width =
                0.dp,
            height =
                0.dp
        )
    }

    val widthFromMaximumHeight =
        maximumHeight *
                aspectRatio

    return if (
        widthFromMaximumHeight <= maximumWidth
    ) {
        ForegroundSize(
            width =
                widthFromMaximumHeight,
            height =
                maximumHeight
        )
    } else {
        ForegroundSize(
            width =
                maximumWidth,
            height =
                maximumWidth /
                        aspectRatio
        )
    }
}

/**
 * Finds the largest safe scale for one foreground object.
 *
 * Board overlap happens only when the object crosses both the board's
 * bottom edge and the relevant vertical side edge.
 */
private fun calculateMaximumForegroundScale(
    sceneWidth: Dp,
    objectBottom: Dp,
    baseSize: ForegroundSize,
    horizontalGap: Dp,
    verticalGap: Dp
): Float {
    if (
        sceneWidth <= 0.dp ||
        objectBottom <= 0.dp ||
        baseSize.width <= 0.dp ||
        baseSize.height <= 0.dp
    ) {
        return 0f
    }

    /*
     * The object cannot grow past the horizontal scene bounds.
     */
    val sceneWidthScale =
        sceneWidth.value /
                baseSize.width.value

    /*
     * The object cannot grow above the top of the scene.
     */
    val sceneHeightScale =
        objectBottom.value /
                baseSize.height.value

    /*
     * At this scale, the object's inner side reaches the board side.
     */
    val sideScale =
        horizontalGap.value /
                baseSize.width.value

    /*
     * At this scale, the object's top reaches the board bottom.
     */
    val bottomScale =
        verticalGap.value /
                baseSize.height.value

    /*
     * Board overlap requires:
     *
     * crosses side
     * AND
     * crosses bottom
     *
     * Therefore the larger of these two thresholds is the last safe
     * board-related scale.
     */
    val boardCollisionScale =
        maxOf(
            sideScale,
            bottomScale
        )

    return minOf(
        sceneWidthScale,
        sceneHeightScale,
        boardCollisionScale
    )
        .coerceAtLeast(
            0f
        )
}

/**
 * Reads the drawable's intrinsic dimensions and returns its width-to-height
 * aspect ratio.
 */
@Composable
private fun rememberDrawableAspectRatio(
    @DrawableRes drawableRes: Int
): Float {
    val context =
        LocalContext.current

    return remember(
        context,
        drawableRes
    ) {
        val drawable =
            requireNotNull(
                ContextCompat.getDrawable(
                    context,
                    drawableRes
                )
            ) {
                "Unable to load drawable: $drawableRes"
            }

        val width =
            drawable.intrinsicWidth

        val height =
            drawable.intrinsicHeight

        require(
            width > 0 &&
                    height > 0
        ) {
            "Drawable must have valid intrinsic dimensions: $drawableRes"
        }

        width.toFloat() /
                height.toFloat()
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
 *
 * The drawable is assigned once when the View is created. The existing
 * NinePatch drawable automatically redraws itself when the View bounds
 * change, so it does not need to be recreated during Compose updates.
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
        }
    )
}