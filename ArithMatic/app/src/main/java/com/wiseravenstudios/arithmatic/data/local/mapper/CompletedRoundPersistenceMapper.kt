package com.wiseravenstudios.arithmatic.data.local.mapper

import com.wiseravenstudios.arithmatic.data.local.entity.CompletedRoundEntity
import com.wiseravenstudios.arithmatic.data.local.entity.QuestionAttemptEntity
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticExpression
import com.wiseravenstudios.arithmatic.domain.results.CompletedGameRoundDto
import java.math.BigDecimal

object CompletedRoundPersistenceMapper {

    fun toRoundEntity(
        completedRound: CompletedGameRoundDto
    ): CompletedRoundEntity {
        return CompletedRoundEntity(
            completedAtEpochMillis =
                completedRound.completedAtEpochMillis,

            activeRoundDurationMillis =
                completedRound.activeRoundDurationMillis,

            enabledOperations =
                completedRound.config.enabledOperations
                    .joinToString(",") { operation ->
                        operation.name
                    },

            allowNegatives =
                completedRound.config.allowNegatives,

            allowDecimals =
                completedRound.config.allowDecimals,

            maximumOperand =
                completedRound.config.maximumOperand,

            focusNumber =
                completedRound.config.focusNumber,

            questionCount =
                completedRound.config.questionCount
        )
    }

    fun toAttemptEntities(
        completedRound: CompletedGameRoundDto,
        roundId: Long
    ): List<QuestionAttemptEntity> {
        return completedRound.questions.zip(
            completedRound.attempts
        ).map { (question, attempt) ->

            QuestionAttemptEntity(
                roundId =
                    roundId,

                questionIndex =
                    attempt.questionIndex,

                operation =
                    question.rootOperation?.name,

                operands =
                    extractOperands(
                        expression =
                            question.expression
                    )
                        .joinToString(
                            separator =
                                OPERAND_SEPARATOR
                        ) { operand ->
                            operand.toDatabaseString()
                        },

                questionText =
                    question.displayText,

                expectedAnswer =
                    question.expectedAnswer
                        .toDatabaseString(),

                selectedAnswer =
                    question.answerChoices[
                        attempt.selectedChoiceIndex
                    ].toDatabaseString(),

                answerChoice0 =
                    question.answerChoices[0]
                        .toDatabaseString(),

                answerChoice1 =
                    question.answerChoices[1]
                        .toDatabaseString(),

                answerChoice2 =
                    question.answerChoices[2]
                        .toDatabaseString(),

                answerChoice3 =
                    question.answerChoices[3]
                        .toDatabaseString(),

                selectedChoiceIndex =
                    attempt.selectedChoiceIndex,

                correctChoiceIndex =
                    question.correctChoiceIndex,

                isCorrect =
                    attempt.isCorrect,

                activeDurationMillis =
                    attempt.activeDurationMillis
            )
        }
    }

    private fun extractOperands(
        expression: ArithmeticExpression
    ): List<BigDecimal> {
        return when (
            expression
        ) {
            is ArithmeticExpression.Value -> {
                listOf(
                    expression.value
                )
            }

            is ArithmeticExpression.BinaryOperation -> {
                extractOperands(
                    expression =
                        expression.left
                ) +
                        extractOperands(
                            expression =
                                expression.right
                        )
            }
        }
    }

    private const val OPERAND_SEPARATOR =
        "|"
}

private fun BigDecimal.toDatabaseString(): String {
    return stripTrailingZeros()
        .toPlainString()
}