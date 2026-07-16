package com.wiseravenstudios.arithmatic.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wiseravenstudios.arithmatic.ui.common.ClassroomScene
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.game.GameBoard
import com.wiseravenstudios.arithmatic.ui.game.GameViewModel
import com.wiseravenstudios.arithmatic.ui.roundsettings.RoundSettingsBoard
import com.wiseravenstudios.arithmatic.ui.splash.SplashScreen
import com.wiseravenstudios.arithmatic.ui.start.StartBoard
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic
import kotlinx.coroutines.delay

@Composable
fun ArithMaticApp(
    gameViewModel: GameViewModel = viewModel()
) {
    var showSplash by rememberSaveable {
        mutableStateOf(true)
    }

    var currentDestination by rememberSaveable {
        mutableStateOf(AppDestination.Start)
    }

    val gameUiState by gameViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        delay(3_000L)
        showSplash = false
    }

    LaunchedEffect(gameUiState.isRoundCompleted) {
        if (gameUiState.isRoundCompleted) {
            currentDestination = AppDestination.Results
        }
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
                        currentDestination =
                            AppDestination.RoundSettings
                    },
                    onOpenSettings = {
                        currentDestination =
                            AppDestination.AppSettings
                    },
                    onOpenAbout = {
                        currentDestination =
                            AppDestination.About
                    },
                    onOpenParentArea = {
                        currentDestination =
                            AppDestination.ParentArea
                    }
                )
            }

            AppDestination.RoundSettings -> {
                RoundSettingsBoard(
                    onBack = {
                        currentDestination =
                            AppDestination.Start
                    },
                    onStartRound = { config ->
                        /*
                         * Clears any previous completed or abandoned round
                         * before creating the new one.
                         */
                        gameViewModel.clearRound()
                        gameViewModel.startRound(config)

                        currentDestination =
                            AppDestination.Practice
                    }
                )
            }

            AppDestination.Practice -> {
                GameBoard(
                    uiState = gameUiState,
                    onExit = {
                        gameViewModel.abandonRound()
                        gameViewModel.clearRound()

                        currentDestination =
                            AppDestination.RoundSettings
                    },
                    onAnswerSelected = { choiceIndex ->
                        gameViewModel.selectAnswer(choiceIndex)
                    },
                    onNext = {
                        gameViewModel.advance()
                    }
                )
            }

            AppDestination.Results -> {
                PlaceholderBoard(
                    title = "Results",
                    onBack = {
                        gameViewModel.clearRound()

                        currentDestination =
                            AppDestination.Start
                    },
                    onContinue = {
                        gameViewModel.clearRound()

                        currentDestination =
                            AppDestination.RoundSettings
                    },
                    continueText = "Practice Again"
                )
            }

            AppDestination.AppSettings -> {
                PlaceholderBoard(
                    title = "Settings",
                    onBack = {
                        currentDestination =
                            AppDestination.Start
                    }
                )
            }

            AppDestination.ParentArea -> {
                PlaceholderBoard(
                    title = "Parents / Guardians",
                    onBack = {
                        currentDestination =
                            AppDestination.Start
                    }
                )
            }

            AppDestination.About -> {
                PlaceholderBoard(
                    title = "About",
                    onBack = {
                        currentDestination =
                            AppDestination.Start
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

        if (onContinue != null) {
            ChalkTextAction(
                text = continueText,
                color = ChalkColors.PastelGreen,
                onClick = onContinue
            )
        }

        ChalkTextAction(
            text = "Back",
            color = ChalkColors.PastelYellow,
            onClick = onBack
        )
    }
}