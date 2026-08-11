package com.wiseravenstudios.arithmatic.domain.adults.statistics

import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.statistics.model.CompletedRoundHistory
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToLong

/**
 * Defines operand-size bands and calculates performance within those bands.
 *
 * Attempts are stratified separately for each arithmetic operation according
 * to the largest absolute operand in the question.
 */
object OperandStratification {

    /**
     * Generates non-overlapping operand bands through the supplied maximum.
     *
     * The first bands provide finer resolution for smaller arithmetic:
     *
     * 1-9
     * 10-20
     * 21-50
     * 51-100
     *
     * Larger bands repeat the same proportional pattern at each power of ten:
     *
     * 101-200
     * 201-400
     * 401-750
     * 751-1,000
     *
     * 1,001-2,000
     * 2,001-4,000
     * 4,001-7,500
     * 7,501-10,000
     */
    fun generateBands(
        maximumOperandInclusive: BigDecimal
    ): List<OperandBand> {
        require(
            maximumOperandInclusive >= BigDecimal.ONE
        ) {
            "Maximum operand must be at least one."
        }

        val bands =
            mutableListOf<OperandBand>()

        addBandThroughMaximum(
            bands = bands,
            minimum = BigDecimal.ONE,
            maximum = BigDecimal("9"),
            supportedMaximum =
                maximumOperandInclusive
        )

        addBandThroughMaximum(
            bands = bands,
            minimum = BigDecimal("10"),
            maximum = BigDecimal("20"),
            supportedMaximum =
                maximumOperandInclusive
        )

        addBandThroughMaximum(
            bands = bands,
            minimum = BigDecimal("21"),
            maximum = BigDecimal("50"),
            supportedMaximum =
                maximumOperandInclusive
        )

        addBandThroughMaximum(
            bands = bands,
            minimum = BigDecimal("51"),
            maximum = BigDecimal("100"),
            supportedMaximum =
                maximumOperandInclusive
        )

        var scale =
            BigDecimal.ONE

        while (
            BigDecimal("100")
                .multiply(scale) <
            maximumOperandInclusive
        ) {
            val hundred =
                BigDecimal("100")
                    .multiply(scale)

            val twoHundred =
                BigDecimal("200")
                    .multiply(scale)

            val fourHundred =
                BigDecimal("400")
                    .multiply(scale)

            val sevenHundredFifty =
                BigDecimal("750")
                    .multiply(scale)

            val oneThousand =
                BigDecimal("1000")
                    .multiply(scale)

            addBandThroughMaximum(
                bands = bands,
                minimum =
                    hundred.add(
                        BigDecimal.ONE
                    ),
                maximum =
                    twoHundred,
                supportedMaximum =
                    maximumOperandInclusive
            )

            addBandThroughMaximum(
                bands = bands,
                minimum =
                    twoHundred.add(
                        BigDecimal.ONE
                    ),
                maximum =
                    fourHundred,
                supportedMaximum =
                    maximumOperandInclusive
            )

            addBandThroughMaximum(
                bands = bands,
                minimum =
                    fourHundred.add(
                        BigDecimal.ONE
                    ),
                maximum =
                    sevenHundredFifty,
                supportedMaximum =
                    maximumOperandInclusive
            )

            addBandThroughMaximum(
                bands = bands,
                minimum =
                    sevenHundredFifty.add(
                        BigDecimal.ONE
                    ),
                maximum =
                    oneThousand,
                supportedMaximum =
                    maximumOperandInclusive
            )

            scale =
                scale.multiply(
                    BigDecimal.TEN
                )
        }

        return bands
    }

    /**
     * Calculates operand-size statistics separately for every operation
     * represented by the supplied attempts.
     *
     * Every generated band is retained even when it contains zero attempts so
     * reports can display the full supported difficulty range.
     */
    fun calculate(
        attempts: List<CompletedRoundHistory.Attempt>,
        maximumOperandInclusive: BigDecimal
    ): List<OperationOperandStratification> {
        val bands =
            generateBands(
                maximumOperandInclusive =
                    maximumOperandInclusive
            )

        val representedOperations =
            attempts
                .mapNotNull { attempt ->
                    attempt.operation
                }
                .distinct()
                .sortedBy { operation ->
                    operation.ordinal
                }

        return representedOperations.map { operation ->
            val operationAttempts =
                attempts.filter { attempt ->
                    attempt.operation ==
                            operation
                }

            OperationOperandStratification(
                operation = operation,
                strata =
                    bands.map { band ->
                        calculateStratum(
                            operation =
                                operation,
                            band =
                                band,
                            attempts =
                                operationAttempts
                        )
                    }
            )
        }
    }

    /**
     * Finds the band containing the largest absolute operand of an attempt.
     */
    fun findBand(
        attempt: CompletedRoundHistory.Attempt,
        bands: List<OperandBand>
    ): OperandBand? {
        val largestOperand =
            attempt.largestAbsoluteOperand
                ?: return null

        return bands.firstOrNull { band ->
            band.contains(
                largestOperand
            )
        }
    }

    private fun calculateStratum(
        operation: ArithmeticOperation,
        band: OperandBand,
        attempts: List<CompletedRoundHistory.Attempt>
    ): OperandStratumSummary {
        val matchingAttempts =
            attempts.filter { attempt ->
                val largestOperand =
                    attempt.largestAbsoluteOperand

                largestOperand != null &&
                        band.contains(
                            largestOperand
                        )
            }

        val questionCount =
            matchingAttempts.size

        val correctCount =
            matchingAttempts.count { attempt ->
                attempt.isCorrect
            }

        val incorrectCount =
            questionCount -
                    correctCount

        val totalDurationMillis =
            matchingAttempts.sumOf { attempt ->
                attempt.activeDurationMillis
            }

        val accuracyPercent =
            if (questionCount == 0) {
                0.0
            } else {
                correctCount.toDouble() /
                        questionCount.toDouble() *
                        100.0
            }

        val averageDurationMillis =
            if (questionCount == 0) {
                0L
            } else {
                (
                        totalDurationMillis.toDouble() /
                                questionCount.toDouble()
                        ).roundToLong()
            }

        return OperandStratumSummary(
            operation =
                operation,
            band =
                band,
            questionCount =
                questionCount,
            correctCount =
                correctCount,
            incorrectCount =
                incorrectCount,
            accuracyPercent =
                accuracyPercent,
            totalDurationMillis =
                totalDurationMillis,
            averageDurationMillis =
                averageDurationMillis
        )
    }

    /**
     * Adds a band while truncating its upper boundary to the currently
     * supported operand maximum.
     */
    private fun addBandThroughMaximum(
        bands: MutableList<OperandBand>,
        minimum: BigDecimal,
        maximum: BigDecimal,
        supportedMaximum: BigDecimal
    ) {
        if (
            minimum >
            supportedMaximum
        ) {
            return
        }

        bands +=
            OperandBand(
                minimumInclusive =
                    minimum,
                maximumInclusive =
                    minOf(
                        maximum,
                        supportedMaximum
                    )
            )
    }
}

/**
 * One non-overlapping operand-size band.
 */
data class OperandBand(
    val minimumInclusive: BigDecimal,
    val maximumInclusive: BigDecimal
) {

    init {
        require(
            minimumInclusive >= BigDecimal.ZERO
        ) {
            "Operand band minimum cannot be negative."
        }

        require(
            minimumInclusive <=
                    maximumInclusive
        ) {
            "Operand band minimum cannot exceed its maximum."
        }
    }

    val displayName: String
        get() =
            "${minimumInclusive.toDisplayNumber()}-" +
                    maximumInclusive.toDisplayNumber()

    fun contains(
        absoluteOperand: BigDecimal
    ): Boolean {
        return absoluteOperand >=
                minimumInclusive &&
                absoluteOperand <=
                maximumInclusive
    }
}

/**
 * Complete operand-size breakdown for one arithmetic operation.
 */
data class OperationOperandStratification(
    val operation: ArithmeticOperation,
    val strata: List<OperandStratumSummary>
) {

    init {
        require(
            strata.all { stratum ->
                stratum.operation ==
                        operation
            }
        ) {
            "Every stratum must belong to the containing operation."
        }
    }
}

/**
 * Performance statistics for one operation inside one operand-size band.
 */
data class OperandStratumSummary(
    val operation: ArithmeticOperation,
    val band: OperandBand,
    val questionCount: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val accuracyPercent: Double,
    val totalDurationMillis: Long,
    val averageDurationMillis: Long
) {

    init {
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
            correctCount +
                    incorrectCount ==
                    questionCount
        ) {
            "Correct and incorrect counts must equal the question count."
        }

        require(
            accuracyPercent in
                    0.0..100.0
        ) {
            "Accuracy must be between zero and one hundred."
        }

        require(
            totalDurationMillis >= 0L
        ) {
            "Total duration cannot be negative."
        }

        require(
            averageDurationMillis >= 0L
        ) {
            "Average duration cannot be negative."
        }
    }

    val hasData: Boolean
        get() =
            questionCount > 0
}

private fun BigDecimal.toDisplayNumber():
        String {
    val normalized =
        stripTrailingZeros()

    return if (
        normalized.scale() <= 0
    ) {
        NumberFormat
            .getIntegerInstance(
                Locale.US
            )
            .format(
                normalized.toBigInteger()
            )
    } else {
        normalized.toPlainString()
    }
}