package com.wiseravenstudios.arithmatic.domain.adults.statistics

import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation

/**
 * Complete statistical summary of the currently filtered Adult history.
 *
 * All values describe only the attempts selected by the active Adult history
 * filters.
 */
data class AdultStatsSummary(
    val roundCount: Int,
    val questionCount: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val accuracyPercent: Double,
    val totalQuestionDurationMillis: Long,
    val averageQuestionDurationMillis: Long,
    val averageRoundDurationMillis: Long,
    val operationSummaries: List<OperationStatsSummary>,
    val timePoints: List<StatsTimePoint>,
    val operandStratifications:
    List<OperationOperandStratification>
) {

    init {
        require(roundCount >= 0) {
            "Round count cannot be negative."
        }

        require(questionCount >= 0) {
            "Question count cannot be negative."
        }

        require(correctCount >= 0) {
            "Correct count cannot be negative."
        }

        require(incorrectCount >= 0) {
            "Incorrect count cannot be negative."
        }

        require(
            correctCount + incorrectCount ==
                    questionCount
        ) {
            "Correct and incorrect counts must equal the question count."
        }

        require(
            accuracyPercent in 0.0..100.0
        ) {
            "Accuracy percent must be between zero and one hundred."
        }

        require(
            totalQuestionDurationMillis >= 0L
        ) {
            "Total question duration cannot be negative."
        }

        require(
            averageQuestionDurationMillis >= 0L
        ) {
            "Average question duration cannot be negative."
        }

        require(
            averageRoundDurationMillis >= 0L
        ) {
            "Average round duration cannot be negative."
        }

        require(
            timePoints.zipWithNext().all {
                    (current, next) ->
                current.endEpochMillisExclusive <=
                        next.startEpochMillis
            }
        ) {
            "Statistics time points must be ordered and cannot overlap."
        }

        require(
            operandStratifications
                .map { stratification ->
                    stratification.operation
                }
                .distinct()
                .size ==
                    operandStratifications.size
        ) {
            "Each operation may have only one operand stratification."
        }
    }

    val hasData: Boolean
        get() = questionCount > 0

    companion object {

        val Empty =
            AdultStatsSummary(
                roundCount = 0,
                questionCount = 0,
                correctCount = 0,
                incorrectCount = 0,
                accuracyPercent = 0.0,
                totalQuestionDurationMillis = 0L,
                averageQuestionDurationMillis = 0L,
                averageRoundDurationMillis = 0L,
                operationSummaries =
                    emptyList(),
                timePoints =
                    emptyList(),
                operandStratifications =
                    emptyList()
            )
    }
}

/**
 * Statistical summary for one arithmetic operation.
 */
data class OperationStatsSummary(
    val operation: ArithmeticOperation,
    val questionCount: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val accuracyPercent: Double,
    val totalDurationMillis: Long,
    val averageDurationMillis: Long
) {

    init {
        require(questionCount > 0) {
            "An operation summary must contain at least one question."
        }

        require(correctCount >= 0) {
            "Correct count cannot be negative."
        }

        require(incorrectCount >= 0) {
            "Incorrect count cannot be negative."
        }

        require(
            correctCount + incorrectCount ==
                    questionCount
        ) {
            "Correct and incorrect counts must equal the question count."
        }

        require(
            accuracyPercent in 0.0..100.0
        ) {
            "Accuracy percent must be between zero and one hundred."
        }

        require(totalDurationMillis >= 0L) {
            "Total duration cannot be negative."
        }

        require(averageDurationMillis >= 0L) {
            "Average duration cannot be negative."
        }
    }
}

/**
 * One point in the Adult Statistics progress graph.
 *
 * A point may represent:
 *
 * - one completed round for a one-day view;
 * - one calendar day;
 * - three consecutive calendar days;
 * - seven consecutive calendar days.
 *
 * Empty time buckets are not represented. No practice is different from
 * zero-percent accuracy.
 */
data class StatsTimePoint(
    val startEpochMillis: Long,
    val endEpochMillisExclusive: Long,
    val questionCount: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val accuracyPercent: Double,
    val totalDurationMillis: Long,
    val averageDurationMillis: Long
) {

    init {
        require(startEpochMillis >= 0L) {
            "Time point start cannot be negative."
        }

        require(
            endEpochMillisExclusive >
                    startEpochMillis
        ) {
            "Time point end must be later than its start."
        }

        require(questionCount > 0) {
            "A time point must contain at least one question."
        }

        require(correctCount >= 0) {
            "Correct count cannot be negative."
        }

        require(incorrectCount >= 0) {
            "Incorrect count cannot be negative."
        }

        require(
            correctCount + incorrectCount ==
                    questionCount
        ) {
            "Correct and incorrect counts must equal the question count."
        }

        require(
            accuracyPercent in 0.0..100.0
        ) {
            "Accuracy percent must be between zero and one hundred."
        }

        require(totalDurationMillis >= 0L) {
            "Total duration cannot be negative."
        }

        require(averageDurationMillis >= 0L) {
            "Average duration cannot be negative."
        }
    }
}