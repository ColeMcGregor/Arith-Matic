package com.wiseravenstudios.arithmatic.ui.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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

@Composable
fun AboutBoard(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = 22.dp,
                vertical = 28.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "About\nArith-Matic",
            color = ChalkColors.PastelOrange,
            fontFamily = Chalktastic,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 28.sp
        )

        Spacer(
            modifier = Modifier.height(34.dp)
        )

        Text(
            text = "Practice arithmetic.\nBuild confidence.",
            color = ChalkColors.ChalkWhite,
            fontFamily = Chalktastic,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            lineHeight = 27.sp
        )

        Spacer(
            modifier = Modifier.height(34.dp)
        )

        Text(
            text = "Wise Raven Studios\n Arith-Matic Page ↗",
            modifier = Modifier.clickable {
                uriHandler.openUri(ARITH_MATIC_WEBSITE)
            },
            color = ChalkColors.PastelBlue,
            fontFamily = Chalktastic,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Text(
            text = "Version 1.0",
            color = ChalkColors.PastelPurple,
            fontFamily = Chalktastic,
            fontSize = 17.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        ChalkTextAction(
            text = "Back",
            color = ChalkColors.PastelYellow,
            onClick = onBack
        )
    }
}