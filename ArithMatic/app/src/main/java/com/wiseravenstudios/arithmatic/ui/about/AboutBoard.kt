package com.wiseravenstudios.arithmatic.ui.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic

private const val ARITH_MATIC_WEBSITE =
    "https://colemcgregor.github.io/studio/projects/arithmatic.html"

private const val HORIZONTAL_LAYOUT_ASPECT_RATIO =
    1.45f

@Composable
fun AboutBoard(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uriHandler =
        LocalUriHandler.current

    BoxWithConstraints(
        modifier =
            modifier.fillMaxSize()
    ) {
        val width =
            maxWidth.value

        val height =
            maxHeight.value

        val aspectRatio =
            if (height > 0f) {
                width / height
            } else {
                1f
            }

        val limitingDimension =
            minOf(
                width,
                height
            )

        val horizontalPadding =
            (limitingDimension * 0.052f)
                .coerceIn(
                    12f,
                    28f
                )
                .dp

        val verticalPadding =
            (limitingDimension * 0.055f)
                .coerceIn(
                    12f,
                    28f
                )
                .dp

        val largeSpacing =
            (limitingDimension * 0.075f)
                .coerceIn(
                    16f,
                    34f
                )
                .dp

        val smallSpacing =
            (limitingDimension * 0.033f)
                .coerceIn(
                    8f,
                    16f
                )
                .dp

        val titleSize =
            (limitingDimension * 0.085f)
                .coerceAtLeast(20f)
                .sp

        val titleLineHeight =
            (limitingDimension * 0.090f)
                .coerceAtLeast(21f)
                .sp

        val taglineSize =
            (limitingDimension * 0.055f)
                .coerceAtLeast(14f)
                .sp

        val taglineLineHeight =
            (limitingDimension * 0.072f)
                .coerceAtLeast(20f)
                .sp

        val linkSize =
            (limitingDimension * 0.060f)
                .coerceAtLeast(15f)
                .sp

        val versionSize =
            (limitingDimension * 0.048f)
                .coerceAtLeast(13f)
                .sp

        if (
            aspectRatio >=
            HORIZONTAL_LAYOUT_ASPECT_RATIO
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal =
                                horizontalPadding,
                            vertical =
                                verticalPadding
                        ),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        largeSpacing
                    ),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    verticalArrangement =
                        Arrangement.Center,
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {
                    Text(
                        text =
                            "About\nArith-Matic",
                        color =
                            ChalkColors.PastelOrange,
                        fontFamily =
                            Chalktastic,
                        fontSize =
                            titleSize,
                        fontWeight =
                            FontWeight.Bold,
                        textAlign =
                            TextAlign.Center,
                        lineHeight =
                            titleLineHeight
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                largeSpacing
                            )
                    )

                    Text(
                        text =
                            "Practice arithmetic.\nBuild confidence.",
                        color =
                            ChalkColors.ChalkWhite,
                        fontFamily =
                            Chalktastic,
                        fontSize =
                            taglineSize,
                        textAlign =
                            TextAlign.Center,
                        lineHeight =
                            taglineLineHeight
                    )
                }

                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    verticalArrangement =
                        Arrangement.Center,
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {
                    Text(
                        text =
                            "Wise Raven Studios\nArith-Matic Page ↗",
                        modifier =
                            Modifier.clickable {
                                uriHandler.openUri(
                                    ARITH_MATIC_WEBSITE
                                )
                            },
                        color =
                            ChalkColors.PastelBlue,
                        fontFamily =
                            Chalktastic,
                        fontSize =
                            linkSize,
                        fontWeight =
                            FontWeight.Bold,
                        textAlign =
                            TextAlign.Center,
                        textDecoration =
                            TextDecoration.Underline
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                smallSpacing
                            )
                    )

                    Text(
                        text =
                            "Version 1.0",
                        color =
                            ChalkColors.PastelPurple,
                        fontFamily =
                            Chalktastic,
                        fontSize =
                            versionSize,
                        textAlign =
                            TextAlign.Center
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                smallSpacing
                            )
                    )

                    ChalkTextAction(
                        text =
                            "Back",
                        color =
                            ChalkColors.PastelYellow,
                        fontSize =
                            linkSize,
                        onClick =
                            onBack
                    )
                }
            }
        } else {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal =
                                horizontalPadding,
                            vertical =
                                verticalPadding
                        ),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {
                Text(
                    text =
                        "About\nArith-Matic",
                    color =
                        ChalkColors.PastelOrange,
                    fontFamily =
                        Chalktastic,
                    fontSize =
                        titleSize,
                    fontWeight =
                        FontWeight.Bold,
                    textAlign =
                        TextAlign.Center,
                    lineHeight =
                        titleLineHeight
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            largeSpacing
                        )
                )

                Text(
                    text =
                        "Practice arithmetic.\nBuild confidence.",
                    color =
                        ChalkColors.ChalkWhite,
                    fontFamily =
                        Chalktastic,
                    fontSize =
                        taglineSize,
                    textAlign =
                        TextAlign.Center,
                    lineHeight =
                        taglineLineHeight
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            largeSpacing
                        )
                )

                Text(
                    text =
                        "Wise Raven Studios\nArith-Matic Page ↗",
                    modifier =
                        Modifier.clickable {
                            uriHandler.openUri(
                                ARITH_MATIC_WEBSITE
                            )
                        },
                    color =
                        ChalkColors.PastelBlue,
                    fontFamily =
                        Chalktastic,
                    fontSize =
                        linkSize,
                    fontWeight =
                        FontWeight.Bold,
                    textAlign =
                        TextAlign.Center,
                    textDecoration =
                        TextDecoration.Underline
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            smallSpacing
                        )
                )

                Text(
                    text =
                        "Version 1.0",
                    color =
                        ChalkColors.PastelPurple,
                    fontFamily =
                        Chalktastic,
                    fontSize =
                        versionSize,
                    textAlign =
                        TextAlign.Center
                )

                Spacer(
                    modifier =
                        Modifier.weight(1f)
                )

                ChalkTextAction(
                    text =
                        "Back",
                    color =
                        ChalkColors.PastelYellow,
                    fontSize =
                        linkSize,
                    onClick =
                        onBack
                )
            }
        }
    }
}