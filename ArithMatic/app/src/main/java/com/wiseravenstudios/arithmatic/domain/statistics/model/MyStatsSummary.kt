
package com.wiseravenstudios.arithmatic.domain.statistics.model

import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation

/**
 * Complete statistics displayed for one selected My Stats period.
 *
 * @property period the calendar period represented by this summary
 * @property overall performance across every matching attempt
 * @property byOperation performance grouped by arithmetic operation
 */
data class MyStatsSummary(
    val period: StatsPeriod,
    val overall: PerformanceSummary,
    val byOperation: List<OperationPerformanceSummary>
) {

    /**
     * True when no attempts were completed during the selected period.
     */
    val isEmpty: Boolean
        get() = overall.totalCount == 0

    init {
        require(
            byOperation
                .map { it.operation }
                .distinct()
                .size == byOperation.size
        ) {
            "Each arithmetic operation may appear only once."
        }

        require(
            byOperation.sumOf {
                it.performance.totalCount
            } <= overall.totalCount
        ) {
            "Per-operation attempt totals cannot exceed the overall total."
        }
    }

    companion object {

        /**
         * Creates an empty summary for a selected period.
         */
        fun empty(
            period: StatsPeriod
        ): MyStatsSummary {
            return MyStatsSummary(
                period = period,
                overall = PerformanceSummary.empty(),
                byOperation = emptyList()
            )
        }
    }
}

/**
 * Performance statistics calculated from a collection of question attempts.
 *
 * @property correctCount number of correctly answered questions
 * @property totalCount total number of answered questions
 * @property averageDurationMillis average active answer duration, or null when
 * there were no attempts
 */
data class PerformanceSummary(
    val correctCount: Int,
    val totalCount: Int,
    val averageDurationMillis: Long?
) {

    /**
     * Correct-answer percentage from 0.0 through 100.0.
     *
     * A summary with no attempts returns 0.0 rather than producing an
     * undefined value.
     */
    val percentCorrect: Double
        get() {
            if (totalCount == 0) {
                return 0.0
            }

            return correctCount
                .toDouble()
                .div(totalCount)
                .times(100.0)
        }

    init {
        require(correctCount >= 0) {
            "Correct count cannot be negative."
        }

        require(totalCount >= 0) {
            "Total count cannot be negative."
        }

        require(correctCount <= totalCount) {
            "Correct count cannot exceed total count."
        }

        require(
            averageDurationMillis == null ||
                    averageDurationMillis >= 0L
        ) {
            "Average duration cannot be negative."
        }

        require(
            totalCount > 0 ||
                    averageDurationMillis == null
        ) {
            "A summary with no attempts cannot have an average duration."
        }
    }

    companion object {

        /**
         * Creates a performance summary containing no attempts.
         */
        fun empty(): PerformanceSummary {
            return PerformanceSummary(
                correctCount = 0,
                totalCount = 0,
                averageDurationMillis = null
            )
        }
    }
}

/**
 * Performance statistics for one arithmetic operation.
 */
data class OperationPerformanceSummary(
    val operation: ArithmeticOperation,
    val performance: PerformanceSummary
)

