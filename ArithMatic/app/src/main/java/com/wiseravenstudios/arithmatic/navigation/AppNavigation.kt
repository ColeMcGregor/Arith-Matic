package com.wiseravenstudios.arithmatic.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import com.wiseravenstudios.arithmatic.domain.results.BasicRoundResults
import com.wiseravenstudios.arithmatic.ui.common.ClassroomScene
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.game.GameBoard
import com.wiseravenstudios.arithmatic.ui.game.GameViewModel
import com.wiseravenstudios.arithmatic.ui.results.ResultsBoard
import com.wiseravenstudios.arithmatic.ui.roundsettings.RoundSettingsBoard
import com.wiseravenstudios.arithmatic.ui.splash.SplashScreen
import com.wiseravenstudios.arithmatic.ui.start.StartBoard
import com.wiseravenstudios.arithmatic.ui.about.AboutBoard
import com.wiseravenstudios.arithmatic.ui.adults.AdultsBoard
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

    /*
     * These are retained outside the GameViewModel so the completed gameplay
     * state can be cleared without removing the data used by ResultsBoard.
     *
     * They are intentionally not rememberSaveable because the domain models
     * are not currently Parcelable or backed by custom Savers.
     */
    var completedResults by remember {
        mutableStateOf<BasicRoundResults?>(null)
    }

    var completedConfig by remember {
        mutableStateOf<PracticeConfig?>(null)
    }

    val gameUiState by gameViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        delay(3_000L)
        showSplash = false
    }

    /*
     * The ViewModel completes the round after the final feedback delay.
     *
     * Capture the immutable results and configuration before clearing the
     * gameplay state or navigating away from the practice board.
     */
    LaunchedEffect(
        gameUiState.isRoundCompleted,
        currentDestination
    ) {
        if (
            gameUiState.isRoundCompleted &&
            currentDestination == AppDestination.Practice
        ) {
            val roundResults =
                gameViewModel.getCompletedResults()

            val roundSnapshot =
                gameViewModel.getCompletedRound()

            if (
                roundResults != null &&
                roundSnapshot != null
            ) {
                completedResults = roundResults
                completedConfig = roundSnapshot.config

                currentDestination =
                    AppDestination.Results
            }
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
                    onOpenStats = {
                        currentDestination =
                            AppDestination.MyStats
                    },
                    onOpenAbout = {
                        currentDestination =
                            AppDestination.About
                    },
                    onOpenAdultArea = {
                        currentDestination =
                            AppDestination.AdultArea
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
                        gameViewModel.clearRound()

                        completedResults = null
                        completedConfig = null

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

                        completedResults = null
                        completedConfig = null

                        currentDestination =
                            AppDestination.RoundSettings
                    },
                    onAnswerSelected = { choiceIndex ->
                        gameViewModel.selectAnswer(choiceIndex)
                    }
                )
            }

            AppDestination.Results -> {
                val results = completedResults
                val config = completedConfig

                if (
                    results != null &&
                    config != null
                ) {
                    ResultsBoard(
                        results = results,
                        onPracticeAgain = {
                            /*
                             * Practice Again creates a fresh round using the
                             * same immutable configuration snapshot.
                             */
                            gameViewModel.clearRound()
                            gameViewModel.startRound(config)

                            completedResults = null

                            currentDestination =
                                AppDestination.Practice
                        },
                        onChangeSettings = {
                            gameViewModel.clearRound()

                            completedResults = null
                            completedConfig = null

                            currentDestination =
                                AppDestination.RoundSettings
                        },
                        onReturnHome = {
                            gameViewModel.clearRound()

                            completedResults = null
                            completedConfig = null

                            currentDestination =
                                AppDestination.Start
                        }
                    )
                } else {
                    MissingResultsBoard(
                        onReturnHome = {
                            gameViewModel.clearRound()

                            completedResults = null
                            completedConfig = null

                            currentDestination =
                                AppDestination.Start
                        }
                    )
                }
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

            AppDestination.MyStats -> {
                PlaceholderBoard(
                    title = "My Stats",
                    onBack = {
                        currentDestination =
                            AppDestination.Start
                    }
                )
            }

            AppDestination.AdultArea -> {
                AdultsBoard(
                    onBack = {
                        currentDestination =
                            AppDestination.Start
                    }
                )
            }

            AppDestination.About -> {
                AboutBoard(
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
    MyStats,
    AdultArea,
    About
}

@Composable
private fun MissingResultsBoard(
    onReturnHome: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Unable to load round results.",
            color = ChalkColors.PastelPink,
            fontFamily = Chalktastic,
            fontSize = 25.sp
        )

        ChalkTextAction(
            text = "Return Home",
            color = ChalkColors.PastelYellow,
            onClick = onReturnHome
        )
    }
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