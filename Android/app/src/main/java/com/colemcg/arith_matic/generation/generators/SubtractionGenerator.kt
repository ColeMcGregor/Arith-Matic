package com.colemcg.arith_matic.generation

import com.colemcg.arith_matic.model.GameSettings
import com.colemcg.arith_matic.model.QuestionCard
import com.colemcg.arith_matic.model.QuestionType
import com.colemcg.arith_matic.utils.RNG

/**
 * Subtraction question generator honoring:
 *  - largestNumber: caps operand magnitude
 *  - allowNegative: if false => strictly positive operands & results; if true => full signed range
 *  - allowDecimals: when true, uses one-decimal values (tenths)
 */
class SubtractionGenerator : QuestionGenerator {
    override val type: QuestionType = QuestionType.SUBTRACTION

    override fun generate(settings: GameSettings): QuestionCard {
        return if (settings.allowDecimals) generateDecimal(settings) else generateInteger(settings)
    }

    // ------------------------
    // Integer path
    // ------------------------
    private fun generateInteger(settings: GameSettings): QuestionCard {
        val baseMax = settings.largestNumber.coerceAtLeast(1)

        return if (settings.allowNegative) {
            // Signed operands in [-max, max]; result range is [-2*max, 2*max]
            val a = RNG.int(-baseMax, baseMax)
            val b = RNG.int(-baseMax, baseMax)
            val diff = a - b

            val minRes = -2 * baseMax
            val maxRes =  2 * baseMax

            makeIntCard(
                a = a, b = b, result = diff,
                minRes = minRes, maxRes = maxRes,
                positiveOnly = false
            )
        } else {
            // Strictly positive operands AND strictly positive result
            // Ensure a > b >= 1
            val max = baseMax.coerceAtLeast(2) // need room to keep a>b
            val a = RNG.int(2, max)
            val b = RNG.int(1, a - 1)
            val diff = a - b

            val minRes = 1
            val maxRes = max - 1

            makeIntCard(
                a = a, b = b, result = diff,
                minRes = minRes, maxRes = maxRes,
                positiveOnly = true
            )
        }
    }

    private fun makeIntCard(
        a: Int,
        b: Int,
        result: Int,
        minRes: Int,
        maxRes: Int,
        positiveOnly: Boolean
    ): QuestionCard {
        val questionText = "${fmtIntForQuestion(a)} - ${fmtIntForQuestion(b)} = ?"
        val english = "What is $a minus $b?"
        val options = buildIntOptions(result, minRes, maxRes, positiveOnly)
        val correct = result.toString()

        return QuestionCard.create(
            type = QuestionType.SUBTRACTION,
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

        // Prefer close distractors first
        for (delta in listOf(-1, 1, -2, 2, -3, 3)) {
            val v = correct + delta
            if (v in minRes..maxRes && (!positiveOnly || v > 0)) choices += v
            if (choices.size >= 4) break
        }

        // Fill remaining with random values in-range
        var guard = 0
        while (choices.size < 4 && guard < 100) {
            guard++
            val v = RNG.int(minRes, maxRes)
            if (positiveOnly && v <= 0) continue
            if (v != correct) choices += v
        }

        // Widen slightly if natural range is tiny
        while (choices.size < 4) {
            val v = if (positiveOnly) RNG.int(1, maxOf(maxRes, correct + 10))
            else RNG.int(minRes - 5, maxRes + 5)
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
        val baseMaxT = settings.largestNumber.coerceAtLeast(1) * 10

        return if (settings.allowNegative) {
            val aT = RNG.int(-baseMaxT, baseMaxT)
            val bT = RNG.int(-baseMaxT, baseMaxT)
            val diffT = aT - bT

            val minResT = -2 * baseMaxT
            val maxResT =  2 * baseMaxT

            makeDecimalCard(
                aT = aT, bT = bT, resultT = diffT,
                minResT = minResT, maxResT = maxResT,
                positiveOnly = false
            )
        } else {
            // Strictly positive tenths and positive result: ensure aT > bT >= 1 (0.1)
            val maxT = baseMaxT.coerceAtLeast(2)
            val aT = RNG.int(2, maxT)      // >= 0.2
            val bT = RNG.int(1, aT - 1)    // >= 0.1 and < aT
            val diffT = aT - bT

            val minResT = 1           // 0.1
            val maxResT = maxT - 1    // max - 0.1

            makeDecimalCard(
                aT = aT, bT = bT, resultT = diffT,
                minResT = minResT, maxResT = maxResT,
                positiveOnly = true
            )
        }
    }

    private fun makeDecimalCard(
        aT: Int,
        bT: Int,
        resultT: Int,
        minResT: Int,
        maxResT: Int,
        positiveOnly: Boolean
    ): QuestionCard {
        val aStr = fmtTenths(aT)
        val bStr = fmtTenths(bT)
        val questionText = "${fmtTenthsForQuestion(aT)} - ${fmtTenthsForQuestion(bT)} = ?"
        val english = "What is $aStr minus $bStr?"
        val options = buildDecimalOptions(resultT, minResT, maxResT, positiveOnly)
        val correct = fmtTenths(resultT)

        return QuestionCard.create(
            type = QuestionType.SUBTRACTION,
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

        // Close distractors (±0.1, ±0.2, ±0.3)
        for (delta in listOf(-1, 1, -2, 2, -3, 3)) {
            val v = correctT + delta
            if (v in minResT..maxResT && (!positiveOnly || v > 0)) choices += v
            if (choices.size >= 4) break
        }

        // Fill remaining in-range
        var guard = 0
        while (choices.size < 4 && guard < 100) {
            guard++
            val v = RNG.int(minResT, maxResT)
            if (positiveOnly && v <= 0) continue
            if (v != correctT) choices += v
        }

        // Widen if needed
        while (choices.size < 4) {
            val v = if (positiveOnly) RNG.int(1, maxOf(maxResT, correctT + 20))
            else RNG.int(minResT - 10, maxResT + 10)
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
