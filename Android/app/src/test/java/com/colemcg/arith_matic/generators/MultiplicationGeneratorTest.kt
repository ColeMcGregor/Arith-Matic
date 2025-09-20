package com.colemcg.arith_matic.generation.generators

import com.colemcg.arith_matic.generation.MultiplicationGenerator
import com.colemcg.arith_matic.model.GameSettings
import com.colemcg.arith_matic.model.QuestionType
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for MultiplicationGenerator.
 * Covers correctness, formatting, positivity rules, and decimal formatting.
 */
class MultiplicationGeneratorTest {

    private lateinit var generator: MultiplicationGenerator

    @Before
    fun setup() {
        generator = MultiplicationGenerator()
    }

    // ----------------- GameSettings Variants -----------------

    private fun integerSettings(allowNegative: Boolean = false): GameSettings {
        return GameSettings(
            allowDecimals = false,
            allowNegative = allowNegative,
            largestNumber = 10,
            selectedTypes = listOf(QuestionType.MULTIPLICATION)
        )
    }

    private fun decimalSettings(allowNegative: Boolean = false): GameSettings {
        return GameSettings(
            allowDecimals = true,
            allowNegative = allowNegative,
            largestNumber = 10,
            selectedTypes = listOf(QuestionType.MULTIPLICATION)
        )
    }

    // ----------------- Integer Mode Tests -----------------

    @Test
    fun `integer question should be formatted as a × b = ?`() {
        repeat(50) {
            val card = generator.generate(integerSettings())
            assertTrue("Question must contain '×'", card.question.contains("×"))
            assertTrue("Question must end with '= ?'", card.question.trim().endsWith("= ?"))
        }
    }

    @Test
    fun `correct integer answer should equal a * b`() {
        repeat(50) {
            val card = generator.generate(integerSettings())

            val question = card.question.replace(" ", "").replace("= ?", "")
            val parts = question.split("×")

            assertEquals("Expected 2 operands", 2, parts.size)

            val a = parts[0].removeSurrounding("(", ")").toIntOrNull()
            val b = parts[1].removeSurrounding("(", ")").toIntOrNull()

            assertNotNull("Invalid left operand", a)
            assertNotNull("Invalid right operand", b)

            val expected = (a!! * b!!).toString()
            assertEquals("Incorrect multiplication answer", expected, card.correctAnswer)
        }
    }

    @Test
    fun `integer answers should not be negative when allowNegative is false`() {
        repeat(30) {
            val card = generator.generate(integerSettings(allowNegative = false))
            val answer = card.correctAnswer.toIntOrNull()
            assertNotNull("Answer is not an integer", answer)
            assertTrue("Answer should be positive", answer!! > 0)
        }
    }

    @Test
    fun `integer answers may be negative when allowNegative is true`() {
        var sawNegative = false
        repeat(50) {
            val card = generator.generate(integerSettings(allowNegative = true))
            val answer = card.correctAnswer.toIntOrNull() ?: continue
            if (answer < 0) sawNegative = true
        }
        assertTrue("Expected at least one negative answer", sawNegative)
    }

    @Test
    fun `integer options should be unique and numeric`() {
        repeat(30) {
            val card = generator.generate(integerSettings())
            val unique = card.options.toSet()
            assertEquals("Options contain duplicates", card.options.size, unique.size)

            card.options.forEach {
                assertTrue("Option '$it' is not numeric", it.toIntOrNull() != null)
            }
        }
    }

    // ----------------- Decimal Mode Tests -----------------

    @Test
    fun `decimal question should contain a decimal operand`() {
        val regex = """-?\d+\.\d""".toRegex()

        repeat(30) {
            val card = generator.generate(decimalSettings())
            assertTrue(
                "Question should contain a decimal number: ${card.question}",
                regex.containsMatchIn(card.question)
            )
        }
    }

    @Test
    fun `correct decimal answer should equal a * b formatted to 1dp`() {
        repeat(30) {
            val card = generator.generate(decimalSettings())

            val question = card.question.replace(" ", "").replace("= ?", "")
            val parts = question.split("×")

            assertEquals("Expected 2 operands", 2, parts.size)

            val a = parts[0].removeSurrounding("(", ")").toDoubleOrNull()
            val b = parts[1].removeSurrounding("(", ")").toDoubleOrNull()

            assertNotNull("Invalid left operand", a)
            assertNotNull("Invalid right operand", b)

            val expected = String.format("%.1f", a!! * b!!)
            assertEquals("Incorrect decimal product", expected, card.correctAnswer)
        }
    }

    @Test
    fun `decimal answers should be formatted to one decimal place`() {
        val pattern = """^-?\d+\.\d$""".toRegex()

        repeat(30) {
            val card = generator.generate(decimalSettings())
            assertTrue(
                "Answer '${card.correctAnswer}' not formatted to 1 decimal place",
                pattern.matches(card.correctAnswer)
            )
        }
    }

    @Test
    fun `decimal options should be unique and numeric`() {
        repeat(30) {
            val card = generator.generate(decimalSettings())
            val options = card.options.toSet()

            assertEquals("Options are not unique", card.options.size, options.size)

            card.options.forEach {
                assertTrue("Option '$it' is not decimal", it.toDoubleOrNull() != null)
            }
        }
    }

    @Test
    fun `decimal answers may be negative when allowNegative is true`() {
        var sawNegative = false
        repeat(50) {
            val card = generator.generate(decimalSettings(allowNegative = true))
            val answer = card.correctAnswer.toDoubleOrNull() ?: continue
            if (answer < 0) sawNegative = true
        }
        assertTrue("Expected at least one negative decimal answer", sawNegative)
    }

    @Test
    fun `decimal answers should be positive when allowNegative is false`() {
        repeat(30) {
            val card = generator.generate(decimalSettings(allowNegative = false))
            val answer = card.correctAnswer.toDoubleOrNull()
            assertNotNull("Answer not numeric", answer)
            assertTrue("Answer should be positive", answer!! > 0)
        }
    }
}
