package com.wiseravenstudios.arithmatic.domain.generator

import com.wiseravenstudios.arithmatic.domain.model.ArithmeticQuestion
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig

fun interface OperationQuestionGenerator {
    fun generate(
        config: PracticeConfig
    ): ArithmeticQuestion
}