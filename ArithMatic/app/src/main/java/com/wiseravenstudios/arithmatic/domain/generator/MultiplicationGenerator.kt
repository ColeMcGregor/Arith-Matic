package com.wiseravenstudios.arithmatic.domain.generator

import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticQuestion
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import kotlin.random.Random

class MultiplicationGenerator(
    private val random: Random =
        Random.Default,
    private val answerChoiceGenerator:
    AnswerChoiceGenerator =
        AnswerChoiceGenerator(random)
) : OperationQuestionGenerator {

    override fun generate(
        config: PracticeConfig
    ): ArithmeticQuestion {
        val focusOperand =
            GeneratorSupport.focusOperand(
                config = config
            )

        val operands =
            if (
                focusOperand == null
            ) {
                GeneratorSupport.randomOperand(
                    config = config,
                    random = random
                ) to
                        GeneratorSupport.randomOperand(
                            config = config,
                            random = random
                        )
            } else {
                val otherOperand =
                    GeneratorSupport.randomOperand(
                        config = config,
                        random = random
                    )

                if (
                    random.nextBoolean()
                ) {
                    focusOperand to
                            otherOperand
                } else {
                    otherOperand to
                            focusOperand
                }
            }

        val expression =
            GeneratorSupport.binaryExpression(
                leftOperand =
                    operands.first,
                operation =
                    ArithmeticOperation.Multiplication,
                rightOperand =
                    operands.second
            )

        return answerChoiceGenerator
            .generateQuestion(
                expression = expression,
                config = config
            )
    }
}