package com.colemcg.arith_matic.generation.generators

import com.colemcg.arith_matic.generation.SubtractionGenerator
import org.junit.Assert.*
import org.junit.Test
import org.junit.Before
import com.colemcg.arith_matic.model.QuestionCard
import com.colemcg.arith_matic.model.GameSettings
import com.colemcg.arith_matic.model.QuestionType


/**
 * Unit tests for the SubtactionGenerator class.
 * Tests both integer and decimal modes with positive-only settings.
 *
 */
class SubtractionGeneratorTest {
    private lateinit var generator: SubtractionGenerator

    @Before
    fun setUp() {
        generator = SubtractionGenerator()
    }

    /**
     * Integer-only, positive-only settings.
     */
    private fun integerSettings(): GameSettings{
        return GameSettings(
            largestNumber = 10,
            allowNegative = false,
            allowDecimals = false,
            selectedTypes = listOf(QuestionType.SUBTRACTION)
        )
    }
    /**
     * Decimal mode, positive-only settings.
     */
    private fun decimalSettings(): GameSettings {
        return GameSettings(
            largestNumber = 10,
            allowNegative = false,
            allowDecimals = true,
            selectedTypes = listOf(QuestionType.SUBTRACTION)
        )
    }

    // ---------- Integer mode tests ----------

    @Test
    fun `subtraction question should format properly with integer operands`() {
        repeat(50) {
            val card = generator.generate(integerSettings())

            assertTrue(
                "Question format should contain minus operator",
                card.question.contains("-")
            )

            // Question should end with "= ?"
            assertTrue(
                "Question should end with = ?",
                card.question.trim().endsWith("= ?")
            )
        }
    }

    @Test
    fun `correct answer should equal difference of operands in integer mode`() {
        repeat(50) {
            val card = generator.generate(integerSettings())

            val clean = card.question.replace(" ", "").replace("= ?", "")
            val parts = clean.split("-")

            assertEquals("Expected two operands", 2, parts.size)

            val a = parts[0].removeSurrounding("(", ")").toIntOrNull()
            val b = parts[1].removeSurrounding("(", ")").toIntOrNull()

            assertNotNull("Left operand not valid integer", a)
            assertNotNull("Right operand not valid integer", b)

            val expected = (a!! - b!!).toString()
            assertEquals("Correct answer mismatch", expected, card.correctAnswer)
        }
    }

    @Test
    fun `correct answer should be in options list`() {
        repeat(30) {
            val card = generator.generate(integerSettings())
            assertTrue(
                "Correct answer '${card.correctAnswer}' not in options: ${card.options}",
                card.options.contains(card.correctAnswer)
            )
        }
    }

    @Test
    fun `integer options should be unique and numeric`() {
        repeat(30) {
            val card = generator.generate(integerSettings())

            // Uniqueness
            val unique = card.options.toSet()
            assertEquals("Options contain duplicates", card.options.size, unique.size)

            // Numeric check
            card.options.forEach {
                assertTrue("Option '$it' is not numeric", it.toIntOrNull() != null)
            }
        }
    }

    @Test
    fun `integer answers should not be negative when allowNegative is false`() {
        repeat(30) {
            val card = generator.generate(integerSettings())
            val answer = card.correctAnswer.toIntOrNull()
            assertNotNull("Answer is not an integer", answer)
            assertTrue("Answer should be positive", answer!! > 0)
        }
    }

    // --- DECIMAL TESTS ---

    @Test
    fun `decimal questions should contain decimal points`() {
        repeat(30) {
            val card = generator.generate(decimalSettings())

            val regex = """-?\d+\.\d""".toRegex()
            assertTrue(
                "Question does not contain decimal operands: ${card.question}",
                regex.containsMatchIn(card.question)
            )
        }
    }

    @Test
    fun `decimal correct answer should match operand difference formatted to 1dp`() {
        repeat(30) {
            val card = generator.generate(decimalSettings())

            val clean = card.question.replace(" ", "").replace("= ?", "")
            val parts = clean.split("-")

            assertEquals("Expected two operands", 2, parts.size)

            val a = parts[0].removeSurrounding("(", ")").toDoubleOrNull()
            val b = parts[1].removeSurrounding("(", ")").toDoubleOrNull()

            assertNotNull("Left operand not valid decimal", a)
            assertNotNull("Right operand not valid decimal", b)

            val expected = String.format("%.1f", a!! - b!!)
            assertEquals("Decimal answer mismatch", expected, card.correctAnswer)
        }
    }

    @Test
    fun `decimal answers should be formatted to one decimal place`() {
        val pattern = """^-?\d+\.\d$""".toRegex()

        repeat(30) {
            val card = generator.generate(decimalSettings())
            assertTrue(
                "Decimal answer '${card.correctAnswer}' not formatted to one decimal place",
                pattern.matches(card.correctAnswer)
            )
        }
    }

    @Test
    fun `decimal options should be unique and valid decimals`() {
        repeat(30) {
            val card = generator.generate(decimalSettings())

            val unique = card.options.toSet()
            assertEquals("Options contain duplicates", card.options.size, unique.size)

            card.options.forEach {
                assertTrue("Option '$it' is not a decimal", it.toDoubleOrNull() != null)
            }
        }
    }
}