package com.colemcg.arith_matic.generation

import com.colemcg.arith_matic.model.GameSettings
import com.colemcg.arith_matic.model.QuestionCard
import com.colemcg.arith_matic.model.QuestionType
import com.colemcg.arith_matic.utils.RNG
import kotlin.math.abs
import kotlin.math.max

/**
 * Multiplication question generator honoring:
 *  - largestNumber: caps operand magnitude
 *  - allowNegative: if false => strictly positive operands & products; if true => signed range
 *  - allowDecimals: when true, uses one-decimal values by multiplying a tenths value by an integer
 *                   so the answer still has one decimal place.
 */
class MultiplicationGenerator : QuestionGenerator {
    override val type: QuestionType = QuestionType.MULTIPLICATION

    override fun generate(settings: GameSettings): QuestionCard {
        return if (settings.allowDecimals) generateDecimal(settings) else generateInteger(settings)
    }

    // ------------------------
    // Integer path
    // ------------------------
    private fun generateInteger(settings: GameSettings): QuestionCard {
        val maxOp = settings.largestNumber.coerceAtLeast(1)

        return if (settings.allowNegative) {
            val a = RNG.int(-maxOp, maxOp)
            val b = RNG.int(-maxOp, maxOp)
            val product = a * b

            val maxRes = maxOp * maxOp
            val minRes = -maxRes

            makeIntCard(
                a = a, b = b, product = product,
                minRes = minRes, maxRes = maxRes,
                positiveOnly = false
            )
        } else {
            // strictly positive operands to keep product > 0
            val a = RNG.int(1, maxOp)
            val b = RNG.int(1, maxOp)
            val product = a * b

            val minRes = 1
            val maxRes = maxOp * maxOp

            makeIntCard(
                a = a, b = b, product = product,
                minRes = minRes, maxRes = maxRes,
                positiveOnly = true
            )
        }
    }

    private fun makeIntCard(
        a: Int,
        b: Int,
        product: Int,
        minRes: Int,
        maxRes: Int,
        positiveOnly: Boolean
    ): QuestionCard {
        val questionText = "${fmtIntForQuestion(a)} × ${fmtIntForQuestion(b)} = ?"
        val english = "What is $a times $b?"
        val options = buildIntOptions(product, a, b, minRes, maxRes, positiveOnly)
        val correct = product.toString()

        return QuestionCard.create(
            type = QuestionType.MULTIPLICATION,
            question = questionText,
            options = options,
            correctAnswer = correct,
            englishQuestion = english
        )
    }

    private fun buildIntOptions(
        correct: Int,
        a: Int,
        b: Int,
        minRes: Int,
        maxRes: Int,
        positiveOnly: Boolean
    ): List<String> {
        val choices = linkedSetOf<Int>()
        choices += correct

        // Plausible deltas for multiplication: ±a, ±b, ±(a+b)/2, ±1
        val approx = max(1, (abs(a) + abs(b)) / 2)
        val deltas = listOf(
            -abs(a), abs(a),
            -abs(b), abs(b),
            -approx, approx,
            -1, 1
        )

        for (d in deltas) {
            val v = correct + d
            if (v in minRes..maxRes && (!positiveOnly || v > 0)) choices += v
            if (choices.size >= 4) break
        }

        // Fill remaining with random values in-range
        var guard = 0
        while (choices.size < 4 && guard < 150) {
            guard++
            val v = RNG.int(minRes, maxRes)
            if (positiveOnly && v <= 0) continue
            if (v != correct) choices += v
        }

        // Widen if natural range is tiny
        while (choices.size < 4) {
            val v = if (positiveOnly) RNG.int(1, max(maxRes, correct + max(10, abs(a * b))))
            else RNG.int(minRes - 10, maxRes + 10)
            if (positiveOnly && v <= 0) continue
            if (v != correct) choices += v
        }

        return choices.map { it.toString() }.shuffled()
    }

    private fun fmtIntForQuestion(n: Int): String = if (n < 0) "($n)" else n.toString()

    // ------------------------
    // Decimal (tenths) path
    // ------------------------
    /**
     * Use a tenths operand (aT) and an integer operand (b) so that:
     *   answer(tenths) = aT * b   (still tenths; one decimal place when formatted)
     */
    private fun generateDecimal(settings: GameSettings): QuestionCard {
        val maxT = settings.largestNumber.coerceAtLeast(1) * 10
        val maxInt = settings.largestNumber.coerceAtLeast(1)

        return if (settings.allowNegative) {
            val aT = RNG.int(-maxT, maxT)     // tenths; may be negative/zero
            val b  = RNG.int(-maxInt, maxInt) // integer; may be negative/zero
            val prodT = aT * b                // still tenths

            val maxResT = maxT * maxInt
            val minResT = -maxResT

            makeDecimalCard(
                aT = aT, b = b, productT = prodT,
                minResT = minResT, maxResT = maxResT,
                positiveOnly = false
            )
        } else {
            // strictly positive tenths and integer to keep product > 0
            val aT = RNG.int(1, maxT)   // >= 0.1
            val b  = RNG.int(1, maxInt) // >= 1
            val prodT = aT * b

            val minResT = 1                 // 0.1
            val maxResT = maxT * maxInt

            makeDecimalCard(
                aT = aT, b = b, productT = prodT,
                minResT = minResT, maxResT = maxResT,
                positiveOnly = true
            )
        }
    }

    private fun makeDecimalCard(
        aT: Int,
        b: Int,
        productT: Int,
        minResT: Int,
        maxResT: Int,
        positiveOnly: Boolean
    ): QuestionCard {
        val aStr = fmtTenths(aT)
        val bStr = fmtIntForQuestion(b)
        val questionText = "${fmtTenthsForQuestion(aT)} × ${fmtIntForQuestion(b)} = ?"
        val english = "What is $aStr times $bStr?"
        val options = buildDecimalOptions(productT, aT, b, minResT, maxResT, positiveOnly)
        val correct = fmtTenths(productT)

        return QuestionCard.create(
            type = QuestionType.MULTIPLICATION,
            question = questionText,
            options = options,
            correctAnswer = correct,
            englishQuestion = english
        )
    }

    private fun buildDecimalOptions(
        correctT: Int,
        aT: Int,
        b: Int,
        minResT: Int,
        maxResT: Int,
        positiveOnly: Boolean
    ): List<String> {
        val choices = linkedSetOf<Int>()
        choices += correctT

        // Tenths deltas: ±aT (i.e., adding/subtracting roughly one "factor"),
        // plus some simple tenths steps (±1 = 0.1, ±2 = 0.2)
        val deltas = listOf(
            -abs(aT), abs(aT),
            -10, 10,
            -20, 20
        )

        for (d in deltas) {
            val v = correctT + d
            if (v in minResT..maxResT && (!positiveOnly || v > 0)) choices += v
            if (choices.size >= 4) break
        }

        // Fill remaining with random values in-range (tenths)
        var guard = 0
        while (choices.size < 4 && guard < 150) {
            guard++
            val v = RNG.int(minResT, maxResT)
            if (positiveOnly && v <= 0) continue
            if (v != correctT) choices += v
        }

        // Widen if still short
        while (choices.size < 4) {
            val bump = max(20, abs(correctT) / 10) // ~2.0 tenths or 10% of result
            val v = if (positiveOnly) RNG.int(1, max(maxResT, correctT + bump))
            else RNG.int(minResT - bump, maxResT + bump)
            if (positiveOnly && v <= 0) continue
            if (v != correctT) choices += v
        }

        return choices.map { fmtTenths(it) }.shuffled()
    }

    /** One decimal place, e.g., 12.0, -3.4 */
    private fun fmtTenths(t: Int): String = String.format("%.1f", t / 10.0)

    /** For questions, wrap negatives in parentheses for clarity: (-3.4) */
    private fun fmtTenthsForQuestion(t: Int): String =
        if (t < 0) "(${fmtTenths(t)})" else fmtTenths(t)
}
