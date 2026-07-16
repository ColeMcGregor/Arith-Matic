package com.wiseravenstudios.arithmatic.ui.game

import androidx.lifecycle.ViewModel
import com.wiseravenstudios.arithmatic.data.time.AndroidAppClock
import com.wiseravenstudios.arithmatic.domain.game.GameRoundManager
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticQuestion
import com.wiseravenstudios.arithmatic.domain.model.GameRound
import com.wiseravenstudios.arithmatic.domain.model.GameRoundStatus
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import com.wiseravenstudios.arithmatic.domain.time.ActiveTimer
import com.wiseravenstudios.arithmatic.domain.time.AppClock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel(
    private val gameRoundManager: GameRoundManager = GameRoundManager(),
    clock: AppClock = AndroidAppClock()
) : ViewModel() {

    private val questionTimer = ActiveTimer(clock)
    private val roundTimer = ActiveTimer(clock)

    private var activeRound: GameRound? = null

    private val _uiState = MutableStateFlow(GameUiState())

    val uiState: StateFlow<GameUiState> =
        _uiState.asStateFlow()

    fun startRound(
        config: PracticeConfig
    ) {
        check(activeRound == null) {
            "A round is already active."
        }

        val round = gameRoundManager.createAndStartRound(config)

        activeRound = round

        roundTimer.restart()
        questionTimer.restart()

        publishRoundState(round)
    }

    fun selectAnswer(
        choiceIndex: Int
    ) {
        val round = requireActiveRound()

        check(round.status == GameRoundStatus.InProgress) {
            "Answers may only be selected during an active round."
        }

        check(_uiState.value.selectedChoiceIndex == null) {
            "An answer has already been selected for this question."
        }

        check(choiceIndex in round.currentQuestion.answerChoices.indices) {
            "Selected choice index does not exist for the current question."
        }

        val durationMillis = questionTimer.stop()

        val attempt = gameRoundManager.recordAttempt(
            round = round,
            selectedChoiceIndex = choiceIndex,
            activeDurationMillis = durationMillis
        )

        _uiState.update { currentState ->
            currentState.copy(
                selectedChoiceIndex = choiceIndex,
                selectedAnswerIsCorrect = attempt.isCorrect,
                correctChoiceIndex = round.currentQuestion.correctChoiceIndex,
                isAnswerLocked = true
            )
        }
    }

    fun advance() {
        val round = requireActiveRound()

        check(round.status == GameRoundStatus.InProgress) {
            "Only an active round may advance."
        }

        check(_uiState.value.isAnswerLocked) {
            "An answer must be selected before advancing."
        }

        val hadMoreQuestions = round.hasMoreQuestions

        gameRoundManager.advanceOrComplete(round)

        if (hadMoreQuestions) {
            questionTimer.restart()
            publishRoundState(round)
        } else {
            roundTimer.stop()

            _uiState.update { currentState ->
                currentState.copy(
                    status = GameRoundStatus.Completed,
                    isRoundCompleted = true,
                    activeRoundDurationMillis = roundTimer.elapsedMillis
                )
            }
        }
    }

    fun abandonRound() {
        val round = activeRound ?: return

        if (!round.isFinished) {
            gameRoundManager.abandonRound(round)
        }

        questionTimer.reset()
        roundTimer.reset()

        _uiState.update { currentState ->
            currentState.copy(
                status = GameRoundStatus.Abandoned,
                isRoundAbandoned = true,
                isAnswerLocked = true
            )
        }
    }

    fun pauseTimers() {
        if (activeRound?.status != GameRoundStatus.InProgress) {
            return
        }

        questionTimer.pause()
        roundTimer.pause()
    }

    fun resumeTimers() {
        if (activeRound?.status != GameRoundStatus.InProgress) {
            return
        }

        /*
         * Do not resume the question timer while feedback is displayed.
         * Feedback time should not count toward the next response.
         */
        if (!_uiState.value.isAnswerLocked) {
            questionTimer.resume()
        }

        roundTimer.resume()
    }

    fun clearRound() {
        questionTimer.reset()
        roundTimer.reset()
        activeRound = null
        _uiState.value = GameUiState()
    }

    fun getCompletedRound(): GameRound? {
        return activeRound?.takeIf {
            it.status == GameRoundStatus.Completed
        }
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
            correctChoiceIndex = round.currentQuestion.correctChoiceIndex,
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
        get() = currentQuestion != null &&
                status == GameRoundStatus.InProgress

    val isShowingFeedback: Boolean
        get() = selectedChoiceIndex != null

    val isFinalQuestion: Boolean
        get() = totalQuestions > 0 &&
                currentQuestionNumber == totalQuestions
}