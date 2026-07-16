package com.wiseravenstudios.arithmatic.domain.generator

import com.wiseravenstudios.arithmatic.domain.model.ArithmeticExpression
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticQuestion
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import java.math.BigDecimal
import kotlin.random.Random

class QuestionSetGenerator(
    private val additionGenerator: OperationQuestionGenerator = AdditionGenerator(),
    private val subtractionGenerator: OperationQuestionGenerator = SubtractionGenerator(),
    private val multiplicationGenerator: OperationQuestionGenerator = MultiplicationGenerator(),
    private val divisionGenerator: OperationQuestionGenerator = DivisionGenerator(),
    private val random: Random = Random.Default
) {

    fun generate(
        config: PracticeConfig
    ): List<ArithmeticQuestion> {
        require(config.enabledOperations.isNotEmpty()) {
            "At least one arithmetic operation must be enabled."
        }

        require(config.questionCount > 0) {
            "Question count must be greater than zero."
        }

        val operationPlan = buildOperationPlan(config)
        val questions = mutableListOf<ArithmeticQuestion>()
        val seenQuestions = mutableSetOf<QuestionIdentity>()

        operationPlan.forEach { operation ->
            val generator = generatorFor(operation)

            questions += generateWithDuplicateProtection(
                config = config,
                generator = generator,
                seenQuestions = seenQuestions
            )
        }

        return questions.shuffled(random)
    }

    private fun buildOperationPlan(
        config: PracticeConfig
    ): List<ArithmeticOperation> {
        val operations = config.enabledOperations
            .toList()
            .shuffled(random)

        val baseCount = config.questionCount / operations.size
        val remainder = config.questionCount % operations.size

        return buildList {
            operations.forEachIndexed { index, operation ->
                val operationCount = baseCount +
                        if (index < remainder) 1 else 0

                repeat(operationCount) {
                    add(operation)
                }
            }
        }
    }

    private fun generateWithDuplicateProtection(
        config: PracticeConfig,
        generator: OperationQuestionGenerator,
        seenQuestions: MutableSet<QuestionIdentity>
    ): ArithmeticQuestion {
        repeat(MAX_DUPLICATE_RETRIES) {
            val candidate = generator.generate(config)
            val identity = QuestionIdentity.from(candidate)

            if (seenQuestions.add(identity)) {
                return candidate
            }
        }

        /*
         * Very small configurations may not contain enough possible unique
         * expressions to fill the entire round. Allow a duplicate after a
         * bounded number of retries instead of failing or looping forever.
         */
        return generator.generate(config)
    }

    private fun generatorFor(
        operation: ArithmeticOperation
    ): OperationQuestionGenerator {
        return when (operation) {
            ArithmeticOperation.Addition -> additionGenerator
            ArithmeticOperation.Subtraction -> subtractionGenerator
            ArithmeticOperation.Multiplication -> multiplicationGenerator
            ArithmeticOperation.Division -> divisionGenerator
        }
    }

    private data class QuestionIdentity(
        val expression: ExpressionIdentity
    ) {
        companion object {
            fun from(
                question: ArithmeticQuestion
            ): QuestionIdentity {
                return QuestionIdentity(
                    expression = ExpressionIdentity.from(
                        question.expression
                    )
                )
            }
        }
    }

    private sealed interface ExpressionIdentity {

        data class Value(
            val value: String
        ) : ExpressionIdentity

        data class BinaryOperation(
            val left: ExpressionIdentity,
            val operation: ArithmeticOperation,
            val right: ExpressionIdentity
        ) : ExpressionIdentity

        companion object {
            fun from(
                expression: ArithmeticExpression
            ): ExpressionIdentity {
                return when (expression) {
                    is ArithmeticExpression.Value -> {
                        Value(
                            value = expression.value.toIdentityString()
                        )
                    }

                    is ArithmeticExpression.BinaryOperation -> {
                        BinaryOperation(
                            left = from(expression.left),
                            operation = expression.operation,
                            right = from(expression.right)
                        )
                    }
                }
            }
        }
    }

    private companion object {
        const val MAX_DUPLICATE_RETRIES = 20
    }
}

private fun BigDecimal.toIdentityString(): String {
    return if (compareTo(BigDecimal.ZERO) == 0) {
        "0"
    } else {
        stripTrailingZeros().toPlainString()
    }
}