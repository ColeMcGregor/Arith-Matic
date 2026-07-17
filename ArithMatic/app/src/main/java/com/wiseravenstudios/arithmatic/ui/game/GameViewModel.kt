package com.wiseravenstudios.arithmatic.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiseravenstudios.arithmatic.data.time.AndroidAppClock
import com.wiseravenstudios.arithmatic.domain.game.GameRoundManager
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticQuestion
import com.wiseravenstudios.arithmatic.domain.model.GameRound
import com.wiseravenstudios.arithmatic.domain.model.GameRoundStatus
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import com.wiseravenstudios.arithmatic.domain.results.BasicResultsCalculator
import com.wiseravenstudios.arithmatic.domain.results.BasicRoundResults
import com.wiseravenstudios.arithmatic.domain.results.CompletedGameRoundDto
import com.wiseravenstudios.arithmatic.domain.results.CompletedGameRoundMapper
import com.wiseravenstudios.arithmatic.domain.time.ActiveTimer
import com.wiseravenstudios.arithmatic.domain.time.AppClock
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val FEEDBACK_DELAY_MILLIS = 1_750L

class GameViewModel(
    private val gameRoundManager: GameRoundManager = GameRoundManager(),
    clock: AppClock = AndroidAppClock()
) : ViewModel() {

    private val questionTimer = ActiveTimer(clock)
    private val roundTimer = ActiveTimer(clock)

    private var activeRound: GameRound? = null
    private var completedRound: CompletedGameRoundDto? = null
    private var completedResults: BasicRoundResults? = null
    private var feedbackJob: Job? = null

    /*
     * Tracks whether the application is currently paused or backgrounded.
     *
     * This prevents automatic advancement from starting the next question's
     * timers while the application is not active.
     */
    private var areTimersLifecyclePaused: Boolean = false

    private val _uiState = MutableStateFlow(GameUiState())

    val uiState: StateFlow<GameUiState> =
        _uiState.asStateFlow()

    fun startRound(
        config: PracticeConfig
    ) {
        check(activeRound == null) {
            "A round is already active."
        }

        feedbackJob?.cancel()
        feedbackJob = null

        completedRound = null
        completedResults = null

        val round = gameRoundManager.createAndStartRound(config)

        activeRound = round
        areTimersLifecyclePaused = false

        roundTimer.restart()
        questionTimer.restart()

        publishRoundState(round)
    }

    fun selectAnswer(
        choiceIndex: Int
    ) {
        val round = requireActiveRound()
        val currentState = _uiState.value

        check(round.status == GameRoundStatus.InProgress) {
            "Answers may only be selected during an active round."
        }

        check(!currentState.isAnswerLocked) {
            "An answer has already been selected for this question."
        }

        check(
            choiceIndex in round.currentQuestion.answerChoices.indices
        ) {
            "Selected choice index does not exist for the current question."
        }

        /*
         * Stop both timers before recording or displaying feedback.
         *
         * This ensures neither question time nor round time includes the
         * feedback delay.
         */
        val questionDurationMillis = questionTimer.stop()
        roundTimer.pause()

        val attempt = gameRoundManager.recordAttempt(
            round = round,
            selectedChoiceIndex = choiceIndex,
            activeDurationMillis = questionDurationMillis
        )

        /*
         * Lock the board and expose answer feedback immediately.
         */
        _uiState.update { state ->
            state.copy(
                selectedChoiceIndex = choiceIndex,
                selectedAnswerIsCorrect = attempt.isCorrect,
                correctChoiceIndex =
                    round.currentQuestion.correctChoiceIndex,
                isAnswerLocked = true
            )
        }

        feedbackJob?.cancel()

        feedbackJob = viewModelScope.launch {
            delay(FEEDBACK_DELAY_MILLIS)
            advanceAfterFeedback()
        }
    }

    private fun advanceAfterFeedback() {
        val round = activeRound ?: return

        /*
         * The round may have been abandoned or cleared during the feedback
         * delay.
         */
        if (round.status != GameRoundStatus.InProgress) {
            return
        }

        if (!_uiState.value.isAnswerLocked) {
            return
        }

        val hadMoreQuestions = round.hasMoreQuestions

        gameRoundManager.advanceOrComplete(round)

        if (hadMoreQuestions) {
            prepareNextQuestion(round)
        } else {
            completeRound(round)
        }

        feedbackJob = null
    }

    private fun prepareNextQuestion(
        round: GameRound
    ) {
        /*
         * The previous question timer contains its final recorded duration.
         * Reset it before preparing the next question.
         */
        questionTimer.reset()

        if (!areTimersLifecyclePaused) {
            questionTimer.start()
            roundTimer.resume()
        }

        /*
         * Replacing the state clears the old selected answer and feedback
         * state while publishing the newly advanced question.
         */
        publishRoundState(round)
    }

    private fun completeRound(
        round: GameRound
    ) {
        questionTimer.reset()

        /*
         * The round timer was already paused at answer selection, so its
         * elapsed value excludes the final feedback delay.
         */
        roundTimer.pause()

        val activeRoundDurationMillis =
            roundTimer.elapsedMillis

        val completedRoundSnapshot =
            CompletedGameRoundMapper.map(
                round = round,
                activeRoundDurationMillis =
                    activeRoundDurationMillis
            )

        completedRound = completedRoundSnapshot
        completedResults =
            BasicResultsCalculator.calculate(
                completedRoundSnapshot
            )

        _uiState.update { state ->
            state.copy(
                status = round.status,
                isRoundCompleted = true,
                isAnswerLocked = true,
                activeRoundDurationMillis =
                    activeRoundDurationMillis
            )
        }
    }

    fun abandonRound() {
        val round = activeRound ?: return

        feedbackJob?.cancel()
        feedbackJob = null

        if (!round.isFinished) {
            gameRoundManager.abandonRound(round)
        }

        questionTimer.reset()
        roundTimer.reset()

        completedRound = null
        completedResults = null

        _uiState.update { state ->
            state.copy(
                status = GameRoundStatus.Abandoned,
                isRoundAbandoned = true,
                isAnswerLocked = true
            )
        }
    }

    fun pauseTimers() {
        areTimersLifecyclePaused = true

        if (activeRound?.status != GameRoundStatus.InProgress) {
            return
        }

        questionTimer.pause()
        roundTimer.pause()
    }

    fun resumeTimers() {
        areTimersLifecyclePaused = false

        if (activeRound?.status != GameRoundStatus.InProgress) {
            return
        }

        /*
         * Both timers remain paused while answer feedback is displayed.
         * Automatic advancement will start them for the next question.
         */
        if (_uiState.value.isAnswerLocked) {
            return
        }

        questionTimer.resume()
        roundTimer.resume()
    }

    fun clearRound() {
        feedbackJob?.cancel()
        feedbackJob = null

        questionTimer.reset()
        roundTimer.reset()

        activeRound = null
        completedRound = null
        completedResults = null
        areTimersLifecyclePaused = false

        _uiState.value = GameUiState()
    }

    fun getCompletedRound(): CompletedGameRoundDto? {
        return completedRound
    }

    fun getCompletedResults(): BasicRoundResults? {
        return completedResults
    }

    private fun publishRoundState(
        round: GameRound
    ) {
        _uiState.value = GameUiState(
            status = round.status,
            currentQuestion = round.currentQuestion,
            currentQuestionIndex = round.currentQuestionIndex,
            currentQuestionNumber = round.currentQuestionNumber,
            totalQuestions = round.totalQuestions,
            selectedChoiceIndex = null,
            selectedAnswerIsCorrect = null,
            correctChoiceIndex =
                round.currentQuestion.correctChoiceIndex,
            isAnswerLocked = false,
            isRoundCompleted = false,
            isRoundAbandoned = false,
            activeRoundDurationMillis = roundTimer.elapsedMillis
        )
    }

    private fun requireActiveRound(): GameRound {
        return checkNotNull(activeRound) {
            "No active game round exists."
        }
    }
}

data class GameUiState(
    val status: GameRoundStatus = GameRoundStatus.NotStarted,
    val currentQuestion: ArithmeticQuestion? = null,
    val currentQuestionIndex: Int = 0,
    val currentQuestionNumber: Int = 0,
    val totalQuestions: Int = 0,
    val selectedChoiceIndex: Int? = null,
    val selectedAnswerIsCorrect: Boolean? = null,
    val correctChoiceIndex: Int? = null,
    val isAnswerLocked: Boolean = false,
    val isRoundCompleted: Boolean = false,
    val isRoundAbandoned: Boolean = false,
    val activeRoundDurationMillis: Long = 0L
) {
    val hasActiveQuestion: Boolean
        get() =
            currentQuestion != null &&
                    status == GameRoundStatus.InProgress

    val isShowingFeedback: Boolean
        get() = selectedChoiceIndex != null

    val isFinalQuestion: Boolean
        get() =
            totalQuestions > 0 &&
                    currentQuestionNumber == totalQuestions
}