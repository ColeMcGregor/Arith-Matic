package com.wiseravenstudios.arithmatic.domain.model

data class QuestionAttempt(
    val questionIndex: Int,
    val selectedChoiceIndex: Int,
    val isCorrect: Boolean,
    val activeDurationMillis: Long
) {
    init {
        require(questionIndex >= 0) {
            "Question index cannot be negative."
        }

        require(
            selectedChoiceIndex in 0 until ArithmeticQuestion.ANSWER_CHOICE_COUNT
        ) {
            "Selected choice index must point to a valid answer choice."
        }

        require(activeDurationMillis >= 0L) {
            "Active question duration cannot be negative."
        }
    }

    companion object {
        fun create(
            questionIndex: Int,
            question: ArithmeticQuestion,
            selectedChoiceIndex: Int,
            activeDurationMillis: Long
        ): QuestionAttempt {
            require(selectedChoiceIndex in question.answerChoices.indices) {
                "Selected choice index must point to a valid answer choice."
            }

            return QuestionAttempt(
                questionIndex = questionIndex,
                selectedChoiceIndex = selectedChoiceIndex,
                isCorrect = question.isCorrectChoice(selectedChoiceIndex),
                activeDurationMillis = activeDurationMillis
            )
        }
    }
}