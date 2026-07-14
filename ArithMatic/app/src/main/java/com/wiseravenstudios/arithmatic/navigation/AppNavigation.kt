package com.wiseravenstudios.arithmatic.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseravenstudios.arithmatic.ui.common.ClassroomScene
import com.wiseravenstudios.arithmatic.ui.splash.SplashScreen
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic
import kotlinx.coroutines.delay

@Composable
fun ArithMaticApp() {
    var showSplash by rememberSaveable {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {
        delay(3_000L)
        showSplash = false
    }

    if (showSplash) {
        SplashScreen()
    } else {
        ClassroomScene {
            ChalkFontTestBoard()
        }
    }
}

@Composable
private fun ChalkFontTestBoard() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp
            ),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ChalkFontSample(
            fontFamily = Chalktastic
        )
    }
}

@Composable
private fun ChalkFontSample(
    fontFamily: FontFamily
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Arith-Matic",
            color = Color.White,
            fontFamily = fontFamily,
            fontSize = 37.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = """
                
                
                Start
                
                
                Settings
                
                
                About
                
                
                Parents/
                Guardians
            """.trimIndent(),
            color = Color.White,
            textAlign = TextAlign.Center,
            fontFamily = fontFamily,
            fontSize = 34.sp
        )
    }
}