package com.wiseravenstudios.arithmatic.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.wiseravenstudios.arithmatic.ui.common.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.common.ClassroomScene
import com.wiseravenstudios.arithmatic.ui.roundsettings.RoundSettingsBoard
import com.wiseravenstudios.arithmatic.ui.splash.SplashScreen
import com.wiseravenstudios.arithmatic.ui.start.StartBoard
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic
import kotlinx.coroutines.delay

@Composable
fun ArithMaticApp() {
    var showSplash by rememberSaveable {
        mutableStateOf(true)
    }

    var currentDestination by rememberSaveable {
        mutableStateOf(AppDestination.Start)
    }

    LaunchedEffect(Unit) {
        delay(3_000L)
        showSplash = false
    }

    if (showSplash) {
        SplashScreen()
        return
    }

    ClassroomScene {
        when (currentDestination) {
            AppDestination.Start -> {
                StartBoard(
                    onStartPractice = {
                        currentDestination = AppDestination.RoundSettings
                    },
                    onOpenSettings = {
                        currentDestination = AppDestination.AppSettings
                    },
                    onOpenAbout = {
                        currentDestination = AppDestination.About
                    },
                    onOpenParentArea = {
                        currentDestination = AppDestination.ParentArea
                    }
                )
            }

            AppDestination.RoundSettings -> {
                RoundSettingsBoard(
                    onBack = {
                        currentDestination = AppDestination.Start
                    },
                    onStartRound = { config ->
                        println("Starting with config: $config")
                        currentDestination = AppDestination.Practice
                    }
                )
            }

            AppDestination.Practice -> {
                PlaceholderBoard(
                    title = "Practice",
                    onBack = {
                        currentDestination = AppDestination.RoundSettings
                    },
                    onContinue = {
                        currentDestination = AppDestination.Results
                    },
                    continueText = "Finish Test Round"
                )
            }

            AppDestination.Results -> {
                PlaceholderBoard(
                    title = "Results",
                    onBack = {
                        currentDestination = AppDestination.Start
                    },
                    onContinue = {
                        currentDestination = AppDestination.RoundSettings
                    },
                    continueText = "Practice Again"
                )
            }

            AppDestination.AppSettings -> {
                PlaceholderBoard(
                    title = "Settings",
                    onBack = {
                        currentDestination = AppDestination.Start
                    }
                )
            }

            AppDestination.ParentArea -> {
                PlaceholderBoard(
                    title = "Parents / Guardians",
                    onBack = {
                        currentDestination = AppDestination.Start
                    }
                )
            }

            AppDestination.About -> {
                PlaceholderBoard(
                    title = "About",
                    onBack = {
                        currentDestination = AppDestination.Start
                    }
                )
            }
        }
    }
}

enum class AppDestination {
    Start,
    RoundSettings,
    Practice,
    Results,
    AppSettings,
    ParentArea,
    About
}

@Composable
private fun PlaceholderBoard(
    title: String,
    onBack: () -> Unit,
    onContinue: (() -> Unit)? = null,
    continueText: String = "Continue"
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = ChalkColors.ChalkWhite,
            fontFamily = Chalktastic,
            fontSize = 34.sp
        )

        onContinue?.let {
            ChalkTextAction(
                text = continueText,
                color = ChalkColors.PastelGreen,
                onClick = it
            )
        }

        ChalkTextAction(
            text = "Back",
            color = ChalkColors.PastelYellow,
            onClick = onBack
        )
    }
}