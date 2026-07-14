package com.wiseravenstudios.arithmatic.ui.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseravenstudios.arithmatic.ui.common.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic

@Composable
fun StartBoard(
    onStartPractice: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenAbout: () -> Unit,
    onOpenParentArea: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = 1.dp,
                vertical = 25.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Arith-Matic",
            color = ChalkColors.PastelOrange,
            fontFamily = Chalktastic,
            fontSize = 37.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 60.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ChalkTextAction(
                text = "Start",
                color = ChalkColors.PastelGreen,
                onClick = onStartPractice
            )

            ChalkTextAction(
                text = "Settings",
                color = ChalkColors.PastelPink,
                onClick = onOpenSettings
            )

            ChalkTextAction(
                text = "About",
                color = ChalkColors.PastelBlue,
                onClick = onOpenAbout
            )

            ChalkTextAction(
                text = "Parents / Guardians",
                color = ChalkColors.PastelPurple,
                onClick = onOpenParentArea
            )
        }
    }
}