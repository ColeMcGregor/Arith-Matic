package com.wiseravenstudios.arithmatic.domain.generator

import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticQuestion
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import kotlin.random.Random

class MultiplicationGenerator(
    private val random: Random = Random.Default,
    private val answerChoiceGenerator: AnswerChoiceGenerator =
        AnswerChoiceGenerator(random)
) : OperationQuestionGenerator {

    override fun generate(
        config: PracticeConfig
    ): ArithmeticQuestion {
        val leftOperand = GeneratorSupport.randomOperand(
            config = config,
            random = random
        )

        val rightOperand = GeneratorSupport.randomOperand(
            config = config,
            random = random
        )

        val expression = GeneratorSupport.binaryExpression(
            leftOperand = leftOperand,
            operation = ArithmeticOperation.Multiplication,
            rightOperand = rightOperand
        )

        return answerChoiceGenerator.generateQuestion(
            expression = expression,
            config = config
        )
    }
}