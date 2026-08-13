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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wiseravenstudios.arithmatic.data.local.database.ArithMaticDatabase
import com.wiseravenstudios.arithmatic.data.preferences.getArithMaticDataStore
import com.wiseravenstudios.arithmatic.data.repository.CompletedRoundRepository
import com.wiseravenstudios.arithmatic.data.repository.SettingsRepository
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import com.wiseravenstudios.arithmatic.domain.results.BasicRoundResults
import com.wiseravenstudios.arithmatic.ui.about.AboutBoard
import com.wiseravenstudios.arithmatic.ui.adults.AdultAreaViewModel
import com.wiseravenstudios.arithmatic.ui.adults.AdultAreaViewModelFactory
import com.wiseravenstudios.arithmatic.ui.adults.AdultBoard
import com.wiseravenstudios.arithmatic.ui.adults.rememberAdultReportExporter
import com.wiseravenstudios.arithmatic.ui.common.ClassroomScene
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.game.GameBoard
import com.wiseravenstudios.arithmatic.ui.game.GameViewModel
import com.wiseravenstudios.arithmatic.ui.game.GameViewModelFactory
import com.wiseravenstudios.arithmatic.ui.results.ResultsBoard
import com.wiseravenstudios.arithmatic.ui.roundsettings.RoundSettingsBoard
import com.wiseravenstudios.arithmatic.ui.roundsettings.RoundSettingsUiState
import com.wiseravenstudios.arithmatic.ui.roundsettings.RoundSettingsViewModel
import com.wiseravenstudios.arithmatic.ui.roundsettings.RoundSettingsViewModelFactory
import com.wiseravenstudios.arithmatic.ui.settings.SettingsBoard
import com.wiseravenstudios.arithmatic.ui.settings.SettingsViewModel
import com.wiseravenstudios.arithmatic.ui.settings.SettingsViewModelFactory
import com.wiseravenstudios.arithmatic.ui.splash.SplashScreen
import com.wiseravenstudios.arithmatic.ui.start.StartBoard
import com.wiseravenstudios.arithmatic.ui.statistics.MyStatsBoard
import com.wiseravenstudios.arithmatic.ui.statistics.MyStatsViewModel
import com.wiseravenstudios.arithmatic.ui.statistics.MyStatsViewModelFactory
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic
import kotlinx.coroutines.delay

@Composable
fun ArithMaticApp(
    onExitApp: () -> Unit
) {
    val context =
        LocalContext.current

    val applicationContext =
        context.applicationContext

    val database =
        remember(applicationContext) {
            ArithMaticDatabase.getInstance(
                applicationContext
            )
        }

    val completedRoundRepository =
        remember(database) {
            CompletedRoundRepository(
                completedRoundDao =
                    database.completedRoundDao()
            )
        }

    val preferencesDataStore =
        remember(applicationContext) {
            applicationContext
                .getArithMaticDataStore()
        }

    val settingsRepository =
        remember(preferencesDataStore) {
            SettingsRepository(
                dataStore =
                    preferencesDataStore
            )
        }

    val gameViewModelFactory =
        remember(completedRoundRepository) {
            GameViewModelFactory(
                completedRoundRepository =
                    completedRoundRepository
            )
        }

    val gameViewModel: GameViewModel =
        viewModel(
            factory =
                gameViewModelFactory
        )

    val myStatsViewModelFactory =
        remember(completedRoundRepository) {
            MyStatsViewModelFactory(
                completedRoundRepository =
                    completedRoundRepository
            )
        }

    val myStatsViewModel: MyStatsViewModel =
        viewModel(
            factory =
                myStatsViewModelFactory
        )

    val adultAreaViewModelFactory =
        remember(completedRoundRepository) {
            AdultAreaViewModelFactory(
                completedRoundRepository =
                    completedRoundRepository
            )
        }

    val adultAreaViewModel:
            AdultAreaViewModel =
        viewModel(
            factory =
                adultAreaViewModelFactory
        )

    val settingsViewModelFactory =
        remember(settingsRepository) {
            SettingsViewModelFactory(
                settingsRepository =
                    settingsRepository
            )
        }

    val settingsViewModel:
            SettingsViewModel =
        viewModel(
            factory =
                settingsViewModelFactory
        )

    val roundSettingsViewModelFactory =
        remember(settingsRepository) {
            RoundSettingsViewModelFactory(
                settingsRepository =
                    settingsRepository
            )
        }

    val roundSettingsViewModel:
            RoundSettingsViewModel =
        viewModel(
            factory =
                roundSettingsViewModelFactory
        )

    val exportAdultReport =
        rememberAdultReportExporter()

    var showSplash by rememberSaveable {
        mutableStateOf(true)
    }

    var currentDestination by rememberSaveable {
        mutableStateOf(
            AppDestination.Start
        )
    }

    var completedResults by remember {
        mutableStateOf<BasicRoundResults?>(
            null
        )
    }

    var completedConfig by remember {
        mutableStateOf<PracticeConfig?>(
            null
        )
    }

    val gameUiState by
    gameViewModel
        .uiState
        .collectAsState()

    val myStatsUiState by
    myStatsViewModel
        .uiState
        .collectAsState()

    val settingsUiState by
    settingsViewModel
        .uiState
        .collectAsState()

    val roundSettingsUiState by
    roundSettingsViewModel
        .uiState
        .collectAsState()

    LaunchedEffect(Unit) {
        delay(
            3_000L
        )

        showSplash =
            false
    }

    LaunchedEffect(
        gameUiState.isRoundCompleted,
        currentDestination
    ) {
        if (
            gameUiState.isRoundCompleted &&
            currentDestination ==
            AppDestination.Practice
        ) {
            val roundResults =
                gameViewModel
                    .getCompletedResults()

            val roundSnapshot =
                gameViewModel
                    .getCompletedRound()

            if (
                roundResults != null &&
                roundSnapshot != null
            ) {
                completedResults =
                    roundResults

                completedConfig =
                    roundSnapshot.config

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
                            AppDestination
                                .RoundSettings
                    },
                    onOpenSettings = {
                        currentDestination =
                            AppDestination
                                .AppSettings
                    },
                    onOpenStats = {
                        currentDestination =
                            AppDestination
                                .MyStats
                    },
                    onOpenAbout = {
                        currentDestination =
                            AppDestination
                                .About
                    },
                    onOpenAdultArea = {
                        currentDestination =
                            AppDestination
                                .AdultArea
                    },
                    onExit =
                        onExitApp
                )
            }

            AppDestination.RoundSettings -> {
                when (
                    val state =
                        roundSettingsUiState
                ) {
                    RoundSettingsUiState.Loading -> {
                        RoundSettingsStatusBoard(
                            message =
                                "Loading round settings...",
                            messageColor =
                                ChalkColors
                                    .ChalkWhite,
                            onBack = {
                                currentDestination =
                                    AppDestination
                                        .Start
                            }
                        )
                    }

                    is RoundSettingsUiState.Error -> {
                        RoundSettingsStatusBoard(
                            message =
                                state.message,
                            messageColor =
                                ChalkColors
                                    .PastelPink,
                            onBack = {
                                currentDestination =
                                    AppDestination
                                        .Start
                            }
                        )
                    }

                    is RoundSettingsUiState.Ready -> {
                        RoundSettingsBoard(
                            initialConfig =
                                state.initialConfig,
                            onBack = {
                                currentDestination =
                                    AppDestination
                                        .Start
                            },
                            onStartRound = {
                                    config ->

                                roundSettingsViewModel
                                    .saveConfig(
                                        config =
                                            config,
                                        onSaved = {
                                            gameViewModel
                                                .clearRound()

                                            completedResults =
                                                null

                                            completedConfig =
                                                null

                                            gameViewModel
                                                .startRound(
                                                    config
                                                )

                                            currentDestination =
                                                AppDestination
                                                    .Practice
                                        }
                                    )
                            }
                        )
                    }
                }
            }

            AppDestination.Practice -> {
                GameBoard(
                    uiState =
                        gameUiState,
                    onExit = {
                        gameViewModel
                            .abandonRound()

                        gameViewModel
                            .clearRound()

                        completedResults =
                            null

                        completedConfig =
                            null

                        currentDestination =
                            AppDestination
                                .RoundSettings
                    },
                    onAnswerSelected = {
                            choiceIndex ->

                        gameViewModel
                            .selectAnswer(
                                choiceIndex
                            )
                    }
                )
            }

            AppDestination.Results -> {
                val results =
                    completedResults

                val config =
                    completedConfig

                if (
                    results != null &&
                    config != null
                ) {
                    ResultsBoard(
                        results =
                            results,
                        onPracticeAgain = {
                            gameViewModel
                                .clearRound()

                            gameViewModel
                                .startRound(
                                    config
                                )

                            completedResults =
                                null

                            currentDestination =
                                AppDestination
                                    .Practice
                        },
                        onChangeSettings = {
                            gameViewModel
                                .clearRound()

                            completedResults =
                                null

                            completedConfig =
                                null

                            currentDestination =
                                AppDestination
                                    .RoundSettings
                        },
                        onReturnHome = {
                            gameViewModel
                                .clearRound()

                            completedResults =
                                null

                            completedConfig =
                                null

                            currentDestination =
                                AppDestination
                                    .Start
                        }
                    )
                } else {
                    MissingResultsBoard(
                        onReturnHome = {
                            gameViewModel
                                .clearRound()

                            completedResults =
                                null

                            completedConfig =
                                null

                            currentDestination =
                                AppDestination
                                    .Start
                        }
                    )
                }
            }

            AppDestination.AppSettings -> {
                SettingsBoard(
                    uiState =
                        settingsUiState,
                    onToggleMusic = {
                        settingsViewModel
                            .toggleMusic()
                    },
                    onIncreaseMusic = {
                        settingsViewModel
                            .increaseMusicLevel()
                    },
                    onDecreaseMusic = {
                        settingsViewModel
                            .decreaseMusicLevel()
                    },
                    onToggleSoundEffects = {
                        settingsViewModel
                            .toggleSoundEffects()
                    },
                    onIncreaseSoundEffects = {
                        settingsViewModel
                            .increaseSoundEffectsLevel()
                    },
                    onDecreaseSoundEffects = {
                        settingsViewModel
                            .decreaseSoundEffectsLevel()
                    },
                    onBack = {
                        currentDestination =
                            AppDestination
                                .Start
                    }
                )
            }

            AppDestination.MyStats -> {
                MyStatsBoard(
                    uiState =
                        myStatsUiState,
                    onPeriodSelected = {
                            period ->

                        myStatsViewModel
                            .selectPeriod(
                                period
                            )
                    },
                    onBack = {
                        currentDestination =
                            AppDestination
                                .Start
                    }
                )
            }

            AppDestination.AdultArea -> {
                AdultBoard(
                    viewModel =
                        adultAreaViewModel,
                    onExportReport =
                        exportAdultReport,
                    onBack = {
                        currentDestination =
                            AppDestination
                                .Start
                    }
                )
            }

            AppDestination.About -> {
                AboutBoard(
                    onBack = {
                        currentDestination =
                            AppDestination
                                .Start
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
private fun RoundSettingsStatusBoard(
    message: String,
    messageColor:
    androidx.compose.ui.graphics.Color,
    onBack: () -> Unit
) {
    Column(
        modifier =
            Modifier.fillMaxSize(),
        verticalArrangement =
            Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text =
                message,
            color =
                messageColor,
            fontFamily =
                Chalktastic,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            textAlign =
                TextAlign.Center
        )

        ChalkTextAction(
            text = "Back",
            color =
                ChalkColors.PastelYellow,
            onClick =
                onBack
        )
    }
}

@Composable
private fun MissingResultsBoard(
    onReturnHome: () -> Unit
) {
    Column(
        modifier =
            Modifier.fillMaxSize(),
        verticalArrangement =
            Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text =
                "Unable to load round results.",
            color =
                ChalkColors.PastelPink,
            fontFamily =
                Chalktastic,
            fontSize = 25.sp
        )

        ChalkTextAction(
            text =
                "Return Home",
            color =
                ChalkColors.PastelYellow,
            onClick =
                onReturnHome
        )
    }
}