package com.wiseravenstudios.arithmatic.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseravenstudios.arithmatic.ui.common.ClassroomScene
import com.wiseravenstudios.arithmatic.ui.theme.ChalkY
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic
import com.wiseravenstudios.arithmatic.ui.theme.NeatChalk

@Composable
fun ArithMaticApp() {
    ClassroomScene {
        ChalkFontTestBoard()
    }
}

@Composable
private fun ChalkFontTestBoard() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ChalkFontSample(
            name = "Chalktastic",
            fontFamily = Chalktastic
        )
    }
}

@Composable
private fun ChalkFontSample(
    name: String,
    fontFamily: FontFamily
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Arith-Matic\n",
            color = Color.White,
            fontFamily = fontFamily,
            fontSize = 37.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "\n\nStart\n\n\nSettings\n\n\nAbout\n\n\nParents/\nGuardians",
            color = Color.White,
            textAlign = TextAlign.Center,
            fontFamily = fontFamily,
            fontSize = 34.sp
        )

//        Text(
//            text = "\n\n12 + 8 = ?",
//            color = Color.White,
//            fontFamily = fontFamily,
//            fontSize = 34.sp
//        )


    }
}