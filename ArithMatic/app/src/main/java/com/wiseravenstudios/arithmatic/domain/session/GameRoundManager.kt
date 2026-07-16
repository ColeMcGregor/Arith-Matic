package com.wiseravenstudios.arithmatic.domain.session

import com.wiseravenstudios.arithmatic.domain.config.PracticeConfigValidationResult
import com.wiseravenstudios.arithmatic.domain.config.PracticeConfigValidator
import com.wiseravenstudios.arithmatic.domain.generator.QuestionSetGenerator
import com.wiseravenstudios.arithmatic.domain.model.GameRound
import com.wiseravenstudios.arithmatic.domain.model.GameRoundStatus
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import com.wiseravenstudios.arithmatic.domain.model.QuestionAttempt

class GameRoundManager(
    private val questionSetGenerator: QuestionSetGenerator =
        QuestionSetGenerator()
) {
    var currentRound: GameRound? = null
        private set

    val hasActiveRound: Boolean
        get() = currentRound?.status == GameRoundStatus.InProgress

    /**
     * Validates the configuration, generates the entire question set,
     * creates the round, and marks it as started.
     */
    fun createRound(
        config: PracticeConfig
    ): GameRoundCreationResult {
        val validationResult = PracticeConfigValidator.validate(config)

        if (validationResult is PracticeConfigValidationResult.Invalid) {
            return GameRoundCreationResult.InvalidConfig(
                errors = validationResult.errors
            )
        }

        return try {
            val questions = questionSetGenerator.generate(config)

            val round = GameRound(
                config = config,
                questions = questions
            ).also {
                it.start()
            }

            currentRound = round

            GameRoundCreationResult.Success(round)
        } catch (exception: IllegalArgumentException) {
            GameRoundCreationResult.GenerationFailure(
                message = exception.message
                    ?: "The round could not be generated."
            )
        } catch (exception: IllegalStateException) {
            GameRoundCreationResult.GenerationFailure(
                message = exception.message
                    ?: "The round could not be generated."
            )
        }
    }

    fun requireCurrentRound(): GameRound {
        return checkNotNull(currentRound) {
            "There is no current game round."
        }
    }

    fun recordAttempt(
        attempt: QuestionAttempt
    ) {
        requireCurrentRound().recordAttempt(attempt)
    }

    /**
     * Advances to the next question when one remains.
     *
     * When the current question is the final question, this completes
     * the round instead.
     *
     * Returns the resulting round state.
     */
    fun advanceOrComplete(): GameRoundProgress {
        val round = requireCurrentRound()

        check(round.status == GameRoundStatus.InProgress) {
            "Only an active game round may advance."
        }

        return if (round.hasMoreQuestions) {
            round.advance()

            GameRoundProgress.Advanced(
                currentQuestionIndex = round.currentQuestionIndex,
                currentQuestionNumber = round.currentQuestionNumber,
                totalQuestions = round.totalQuestions
            )
        } else {
            round.complete()

            GameRoundProgress.Completed(round)
        }
    }

    fun abandonRound() {
        val round = currentRound ?: return

        if (!round.isFinished) {
            round.abandon()
        }

        currentRound = null
    }

    fun clearCompletedRound() {
        val round = currentRound ?: return

        check(round.status == GameRoundStatus.Completed) {
            "Only a completed round may be cleared this way."
        }

        currentRound = null
    }
}

sealed interface GameRoundCreationResult {

    data class Success(
        val round: GameRound
    ) : GameRoundCreationResult

    data class InvalidConfig(
        val errors: List<String>
    ) : GameRoundCreationResult

    data class GenerationFailure(
        val message: String
    ) : GameRoundCreationResult
}

sealed interface GameRoundProgress {

    data class Advanced(
        val currentQuestionIndex: Int,
        val currentQuestionNumber: Int,
        val totalQuestions: Int
    ) : GameRoundProgress

    data class Completed(
        val round: GameRound
    ) : GameRoundProgress
}