package com.colemcg.arith_matic.generation

import com.colemcg.arith_matic.model.GameSettings
import com.colemcg.arith_matic.model.QuestionCard
import com.colemcg.arith_matic.model.QuestionType
import com.colemcg.arith_matic.utils.RNG
import kotlin.math.abs
import kotlin.math.max

/**
 * Division question generator honoring:
 *  - largestNumber: caps operand magnitude (|dividend| <= max, |divisor| <= max)
 *  - allowNegative: if false => strictly positive operands & results; if true => signed range
 *  - allowDecimals: when true, shows dividend as tenths and divisor as integer, quotient has one decimal place
 *
 * Strategy:
 *  - Integers: pick divisor (>=1), pick quotient, compute dividend = divisor * quotient → exact division.
 *  - Decimals: pick divisor (>=1 int), pick quotientT (tenths), dividendT = quotientT * divisor → exact 1-decimal result.
 */
class DivisionGenerator : QuestionGenerator {
    override val type: QuestionType = QuestionType.DIVISION

    override fun generate(settings: GameSettings): QuestionCard {
        return if (settings.allowDecimals) generateDecimal(settings) else generateInteger(settings)
    }

    // ------------------------
    // Integer path (exact integer quotient)
    // ------------------------
    private fun generateInteger(settings: GameSettings): QuestionCard {
        val maxOp = settings.largestNumber.coerceAtLeast(1)

        return if (settings.allowNegative) {
            // Choose magnitudes first to keep |dividend|, |divisor| <= maxOp
            val divMag = RNG.int(1, maxOp)                                    // |divisor|
            val maxQuoMag = max(1, maxOp / divMag)                            // ensure dividend within cap
            val quoMag = RNG.int(1, maxQuoMag)                                // |quotient|

            // Random signs
            val divisor = if (RNG.int(0, 1) == 0) divMag else -divMag
            val quotient = if (RNG.int(0, 1) == 0) quoMag else -quoMag
            val dividend = divisor * quotient                                  // within ±maxOp by construction

            makeIntCard(
                dividend = dividend,
                divisor = divisor,
                quotient = quotient,
                minRes = -maxOp,
                maxRes = maxOp,
                positiveOnly = false
            )
        } else {
            // Strictly positive operands and result
            val divisor = RNG.int(1, maxOp)
            val maxQuo = max(1, maxOp / divisor) // ensure dividend <= maxOp
            val quotient = RNG.int(1, maxQuo)
            val dividend = divisor * quotient

            makeIntCard(
                dividend = dividend,
                divisor = divisor,
                quotient = quotient,
                minRes = 1,
                maxRes = maxOp,
                positiveOnly = true
            )
        }
    }

    private fun makeIntCard(
        dividend: Int,
        divisor: Int,
        quotient: Int,
        minRes: Int,
        maxRes: Int,
        positiveOnly: Boolean
    ): QuestionCard {
        val questionText = "${fmtIntForQuestion(dividend)} ÷ ${fmtIntForQuestion(divisor)} = ?"
        val english = "What is $dividend divided by $divisor?"
        val options = buildIntOptions(quotient, minRes, maxRes, positiveOnly)
        val correct = quotient.toString()

        return QuestionCard.create(
            type = QuestionType.DIVISION,
            question = questionText,
            options = options,
            correctAnswer = correct,
            englishQuestion = english
        )
    }

    private fun buildIntOptions(
        correct: Int,
        minRes: Int,
        maxRes: Int,
        positiveOnly: Boolean
    ): List<String> {
        val choices = linkedSetOf<Int>()
        choices += correct

        // Close-by distractors around the quotient
        for (delta in listOf(-1, 1, -2, 2, -3, 3)) {
            val v = correct + delta
            if (v in minRes..maxRes && (!positiveOnly || v > 0)) choices += v
            if (choices.size >= 4) break
        }

        // Fill remaining with random plausible results
        var guard = 0
        while (choices.size < 4 && guard < 120) {
            guard++
            val v = RNG.int(minRes, maxRes)
            if (positiveOnly && v <= 0) continue
            if (v != correct) choices += v
        }

        // Widen if needed (very tight ranges)
        while (choices.size < 4) {
            val bump = max(5, abs(correct) + 2)
            val v = if (positiveOnly) RNG.int(1, max(maxRes, correct + bump))
            else RNG.int(minRes - bump, maxRes + bump)
            if (positiveOnly && v <= 0) continue
            if (v != correct) choices += v
        }

        return choices.map { it.toString() }.shuffled()
    }

    private fun fmtIntForQuestion(n: Int): String = if (n < 0) "($n)" else n.toString()

    // ------------------------
    // Decimal (tenths) path: dividend shown with one decimal; divisor integer; quotient one decimal
    // ------------------------
    private fun generateDecimal(settings: GameSettings): QuestionCard {
        val maxInt = settings.largestNumber.coerceAtLeast(1)
        val maxT = settings.largestNumber.coerceAtLeast(1) * 10 // tenths cap for dividend

        return if (settings.allowNegative) {
            val divMag = RNG.int(1, maxInt)                      // |divisor|
            val maxQuoTMag = max(1, maxT / divMag)               // |quotientT| so |dividendT| <= maxT
            val quoTMag = RNG.int(1, maxQuoTMag)                  // |quotient in tenths|

            val divisor = if (RNG.int(0, 1) == 0) divMag else -divMag
            val quotientT = if (RNG.int(0, 1) == 0) quoTMag else -quoTMag
            val dividendT = quotientT * divisor                   // still tenths

            makeDecimalCard(
                dividendT = dividendT,
                divisor = divisor,
                quotientT = quotientT,
                minResT = -maxT,
                maxResT = maxT,
                positiveOnly = false
            )
        } else {
            val divisor = RNG.int(1, maxInt)                      // >= 1
            val maxQuoT = max(1, maxT / divisor)                  // ensure dividendT <= maxT
            val quotientT = RNG.int(1, maxQuoT)                   // >= 0.1
            val dividendT = quotientT * divisor                   // tenths

            makeDecimalCard(
                dividendT = dividendT,
                divisor = divisor,
                quotientT = quotientT,
                minResT = 1,                                      // 0.1
                maxResT = maxT,                                   // up to largestNumber in tenths
                positiveOnly = true
            )
        }
    }

    private fun makeDecimalCard(
        dividendT: Int,
        divisor: Int,
        quotientT: Int,
        minResT: Int,
        maxResT: Int,
        positiveOnly: Boolean
    ): QuestionCard {
        val dividendStr = fmtTenths(dividendT)
        val questionText = "${fmtTenthsForQuestion(dividendT)} ÷ ${fmtIntForQuestion(divisor)} = ?"
        val english = "What is $dividendStr divided by ${fmtIntForQuestion(divisor)}?"
        val options = buildDecimalOptions(quotientT, minResT, maxResT, positiveOnly)
        val correct = fmtTenths(quotientT)

        return QuestionCard.create(
            type = QuestionType.DIVISION,
            question = questionText,
            options = options,
            correctAnswer = correct,
            englishQuestion = english
        )
    }

    private fun buildDecimalOptions(
        correctT: Int,
        minResT: Int,
        maxResT: Int,
        positiveOnly: Boolean
    ): List<String> {
        val choices = linkedSetOf<Int>()
        choices += correctT

        // Close tenths (±0.1, ±0.2, ±0.3)
        for (delta in listOf(-1, 1, -2, 2, -3, 3)) {
            val v = correctT + delta
            if (v in minResT..maxResT && (!positiveOnly || v > 0)) choices += v
            if (choices.size >= 4) break
        }

        // Fill remaining in-range (tenths)
        var guard = 0
        while (choices.size < 4 && guard < 150) {
            guard++
            val v = RNG.int(minResT, maxResT)
            if (positiveOnly && v <= 0) continue
            if (v != correctT) choices += v
        }

        // Widen if still short
        while (choices.size < 4) {
            val bump = max(10, abs(correctT) / 5)
            val v = if (positiveOnly) RNG.int(1, max(maxResT, correctT + bump))
            else RNG.int(minResT - bump, maxResT + bump)
            if (positiveOnly && v <= 0) continue
            if (v != correctT) choices += v
        }

        return choices.map { fmtTenths(it) }.shuffled()
    }

    /** One decimal place, e.g., 12.0, -3.4 */
    private fun fmtTenths(t: Int): String = String.format("%.1f", t / 10.0)

    /** For questions, wrap negatives in parentheses: (-3.4) */
    private fun fmtTenthsForQuestion(t: Int): String =
        if (t < 0) "(${fmtTenths(t)})" else fmtTenths(t)
}
