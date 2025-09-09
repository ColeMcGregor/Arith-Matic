package com.colemcg.arith_matic.generation

import com.colemcg.arith_matic.model.GameSettings
import com.colemcg.arith_matic.model.QuestionCard
import com.colemcg.arith_matic.model.QuestionType
import com.colemcg.arith_matic.utils.RNG

/**
 * Addition question generator honoring:
 *  - largestNumber: caps operand magnitude
 *  - allowNegative: if false => strictly positive operands & sums; if true => full signed range
 *  - allowDecimals: when true, uses one-decimal values (tenths)
 */
class AdditionGenerator : QuestionGenerator {
    //set the type of the generator
    override val type: QuestionType = QuestionType.ADDITION
    //override the generate method
    override fun generate(settings: GameSettings): QuestionCard {
        //generate the question card
        return if (settings.allowDecimals) generateDecimal(settings) else generateInteger(settings)
    }

    // ------------------------
    // Integer path
    // ------------------------
    private fun generateInteger(settings: GameSettings): QuestionCard {
        val max = settings.largestNumber.coerceAtLeast(1)

        val (minOp, maxOp) = if (settings.allowNegative) {
            -max to max
        } else {
            1 to max // strictly positive operands
        }

        val a = RNG.int(minOp, maxOp)
        val b = RNG.int(minOp, maxOp)
        val sum = a + b

        val (minSum, maxSum) = if (settings.allowNegative) {
            -2 * max to 2 * max
        } else {
            2 to 2 * max // strictly positive sums (since operands are >= 1)
        }

        val questionText = "${fmtIntForQuestion(a)} + ${fmtIntForQuestion(b)} = ?"
        val english = "What is ${a} plus ${b}?"

        val options = buildIntOptions(sum, minSum, maxSum, positiveOnly = !settings.allowNegative)
        val correct = sum.toString()

        return QuestionCard.create(
            type = QuestionType.ADDITION,
            question = questionText,
            options = options,
            correctAnswer = correct,
            englishQuestion = english
        )
    }

    private fun buildIntOptions(
        correct: Int,
        minSum: Int,
        maxSum: Int,
        positiveOnly: Boolean
    ): List<String> {
        val choices = linkedSetOf<Int>()
        choices += correct

        // Prefer close distractors first
        for (delta in listOf(-1, 1, -2, 2, -3, 3)) {
            val v = correct + delta
            if (v in minSum..maxSum && (!positiveOnly || v > 0)) choices += v
            if (choices.size >= 4) break
        }

        // Fill remaining with random values in-range
        var guard = 0
        while (choices.size < 4 && guard < 100) {
            guard++
            val v = RNG.int(minSum, maxSum)
            if (positiveOnly && v <= 0) continue
            if (v != correct) choices += v
        }

        // If the natural range is too narrow (e.g., max=1), widen slightly but keep positivity rule
        while (choices.size < 4) {
            val v = if (positiveOnly) RNG.int(1, (maxOf(maxSum, correct + 10)))
                    else RNG.int(minSum - 5, maxSum + 5)
            if (positiveOnly && v <= 0) continue
            if (v != correct) choices += v
        }

        return choices.map { it.toString() }.shuffled()
    }

    private fun fmtIntForQuestion(n: Int): String = if (n < 0) "($n)" else n.toString()

    // ------------------------
    // Decimal (tenths) path
    // ------------------------
    private fun generateDecimal(settings: GameSettings): QuestionCard {
        // Work in tenths to avoid FP rounding issues
        val maxT = settings.largestNumber.coerceAtLeast(1) * 10

        val (minOpT, maxOpT) = if (settings.allowNegative) {
            -maxT to maxT
        } else {
            1 to maxT // strictly positive tenths (>= 0.1)
        }

        val aT = RNG.int(minOpT, maxOpT)
        val bT = RNG.int(minOpT, maxOpT)
        val sumT = aT + bT

        val (minSumT, maxSumT) = if (settings.allowNegative) {
            -2 * maxT to 2 * maxT
        } else {
            2 to 2 * maxT // strictly positive sums in tenths (>= 0.2)
        }

        val aStr = fmtTenths(aT)
        val bStr = fmtTenths(bT)
        val sumStr = fmtTenths(sumT)

        val questionText = "${fmtTenthsForQuestion(aT)} + ${fmtTenthsForQuestion(bT)} = ?"
        val english = "What is $aStr plus $bStr?"

        val options = buildDecimalOptions(sumT, minSumT, maxSumT, positiveOnly = !settings.allowNegative)
        val correct = sumStr

        return QuestionCard.create(
            type = QuestionType.ADDITION,
            question = questionText,
            options = options,
            correctAnswer = correct,
            englishQuestion = english
        )
    }

    private fun buildDecimalOptions(
        correctT: Int,
        minSumT: Int,
        maxSumT: Int,
        positiveOnly: Boolean
    ): List<String> {
        val choices = linkedSetOf<Int>()
        choices += correctT

        // Close distractors (±0.1, ±0.2, ±0.3)
        for (delta in listOf(-1, 1, -2, 2, -3, 3)) {
            val v = correctT + delta
            if (v in minSumT..maxSumT && (!positiveOnly || v > 0)) choices += v
            if (choices.size >= 4) break
        }

        // Fill remaining in-range
        var guard = 0
        while (choices.size < 4 && guard < 100) {
            guard++
            val v = RNG.int(minSumT, maxSumT)
            if (positiveOnly && v <= 0) continue
            if (v != correctT) choices += v
        }

        // Widen if needed (tiny ranges)
        while (choices.size < 4) {
            val v = if (positiveOnly) RNG.int(1, maxOf(maxSumT, correctT + 20))
                    else RNG.int(minSumT - 10, maxSumT + 10)
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
