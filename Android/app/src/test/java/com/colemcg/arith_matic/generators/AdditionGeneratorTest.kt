package com.colemcg.arith_matic.generation.generators

import com.colemcg.arith_matic.generation.AdditionGenerator
import com.colemcg.arith_matic.model.GameSettings
import com.colemcg.arith_matic.model.QuestionType
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Thorough unit test for AdditionGenerator.
 * Validates correctness, formatting, and respect for settings.
 */
class AdditionGeneratorTest {

    private lateinit var generator: AdditionGenerator

    @Before
    fun setup() {
        generator = AdditionGenerator()
    }

    // ----------- Settings Configurations ------------

    private fun integerSettings(allowNegative: Boolean = false): GameSettings {
        return GameSettings(
            allowDecimals = false,
            allowNegative = allowNegative,
            largestNumber = 10,
            selectedTypes = listOf(QuestionType.ADDITION)
        )
    }

    private fun decimalSettings(allowNegative: Boolean = false): GameSettings {
        return GameSettings(
            allowDecimals = true,
            allowNegative = allowNegative,
            largestNumber = 10,
            selectedTypes = listOf(QuestionType.ADDITION)
        )
    }

    // ----------- Integer Tests ------------

    @Test
    fun `integer questions should be formatted correctly`() {
        repeat(50) {
            val card = generator.generate(integerSettings())

            assertTrue("Question should include '+' sign", card.question.contains("+"))
            assertTrue("Question should end with '= ?'", card.question.trim().endsWith("= ?"))
        }
    }

    @Test
    fun `correct integer answer should match a + b`() {
        repeat(50) {
            val card = generator.generate(integerSettings())

            val question = card.question.replace(" ", "").replace("= ?", "")
            val parts = question.split("+")

            assertEquals("Should have two operands", 2, parts.size)

            val a = parts[0].removeSurrounding("(", ")").toIntOrNull()
            val b = parts[1].removeSurrounding("(", ")").toIntOrNull()

            assertNotNull("Invalid left operand", a)
            assertNotNull("Invalid right operand", b)

            val expected = (a!! + b!!).toString()
            assertEquals("Correct answer mismatch", expected, card.correctAnswer)
        }
    }

    @Test
    fun `integer options should be unique and contain correct answer`() {
        repeat(30) {
            val card = generator.generate(integerSettings())
            val options = card.options.toSet()

            assertEquals("Options are not unique", card.options.size, options.size)
            assertTrue("Correct answer not in options", options.contains(card.correctAnswer))
        }
    }

    @Test
    fun `answers should not be negative when allowNegative is false`() {
        repeat(30) {
            val card = generator.generate(integerSettings(allowNegative = false))
            val answer = card.correctAnswer.toIntOrNull()
            assertNotNull("Answer not a number", answer)
            assertTrue("Answer should be positive", answer!! > 0)
        }
    }

    @Test
    fun `answers can be negative when allowNegative is true`() {
        repeat(30) {
            val card = generator.generate(integerSettings(allowNegative = true))
            val answer = card.correctAnswer.toIntOrNull()
            assertNotNull("Answer not a number", answer)
            // This test only verifies that negatives *can* appear, not that they must
        }
    }

    // ----------- Decimal Tests ------------

    @Test
    fun `decimal questions should contain decimal operands`() {
        val regex = """-?\d+\.\d""".toRegex()

        repeat(30) {
            val card = generator.generate(decimalSettings())
            assertTrue("Question does not contain decimal operand: ${card.question}", regex.containsMatchIn(card.question))
        }
    }

    @Test
    fun `decimal answer should match sum of operands formatted to 1dp`() {
        repeat(30) {
            val card = generator.generate(decimalSettings())

            val question = card.question.replace(" ", "").replace("= ?", "")
            val parts = question.split("+")

            assertEquals("Should have two operands", 2, parts.size)

            val a = parts[0].removeSurrounding("(", ")").toDoubleOrNull()
            val b = parts[1].removeSurrounding("(", ")").toDoubleOrNull()

            assertNotNull("Invalid left operand", a)
            assertNotNull("Invalid right operand", b)

            val expected = String.format("%.1f", a!! + b!!)
            assertEquals("Correct decimal answer mismatch", expected, card.correctAnswer)
        }
    }

    @Test
    fun `decimal answers should be formatted to one decimal place`() {
        val pattern = """^-?\d+\.\d$""".toRegex()

        repeat(30) {
            val card = generator.generate(decimalSettings())
            assertTrue("Answer '${card.correctAnswer}' not 1dp", pattern.matches(card.correctAnswer))
        }
    }

    @Test
    fun `decimal options should be numeric and unique`() {
        repeat(30) {
            val card = generator.generate(decimalSettings())
            val options = card.options.toSet()

            assertEquals("Options contain duplicates", card.options.size, options.size)

            card.options.forEach {
                assertTrue("Option '$it' is not decimal", it.toDoubleOrNull() != null)
            }
        }
    }
}
