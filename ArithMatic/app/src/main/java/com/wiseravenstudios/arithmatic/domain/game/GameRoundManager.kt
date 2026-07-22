package com.wiseravenstudios.arithmatic.domain.game

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

    fun createAndStartRound(
        config: PracticeConfig
    ): GameRound {
        val validationResult =
            PracticeConfigValidator.validate(config)

        if (
            validationResult
                    is PracticeConfigValidationResult.Invalid
        ) {
            throw IllegalArgumentException(
                validationResult.errors.joinToString(
                    separator = " "
                )
            )
        }

        val questions =
            questionSetGenerator.generate(config)

        return GameRound(
            config = config,
            questions = questions
        ).also { round ->
            round.start()
        }
    }

    fun recordAttempt(
        round: GameRound,
        selectedChoiceIndex: Int,
        activeDurationMillis: Long
    ): QuestionAttempt {
        check(
            round.status ==
                    GameRoundStatus.InProgress
        ) {
            "Attempts may only be recorded during an active round."
        }

        check(!round.hasRecordedCurrentAttempt) {
            "The current question already has a recorded attempt."
        }

        check(
            selectedChoiceIndex in
                    round.currentQuestion.answerChoices.indices
        ) {
            "Selected choice index does not exist for the current question."
        }

        val attempt =
            QuestionAttempt.create(
                questionIndex =
                    round.currentQuestionIndex,
                question =
                    round.currentQuestion,
                selectedChoiceIndex =
                    selectedChoiceIndex,
                activeDurationMillis =
                    activeDurationMillis
            )

        round.recordAttempt(attempt)

        return attempt
    }

    fun advanceOrComplete(
        round: GameRound
    ) {
        check(
            round.status ==
                    GameRoundStatus.InProgress
        ) {
            "Only an active round may advance or complete."
        }

        check(round.hasRecordedCurrentAttempt) {
            "An attempt must be recorded before advancing or completing."
        }

        if (round.hasMoreQuestions) {
            round.advance()
        } else {
            round.complete()
        }
    }

    fun abandonRound(
        round: GameRound
    ) {
        if (!round.isFinished) {
            round.abandon()
        }
    }
}

