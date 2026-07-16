package com.wiseravenstudios.arithmatic.domain.model

class GameRound(
    val config: PracticeConfig,
    val questions: List<ArithmeticQuestion>
) {
    init {
        require(questions.isNotEmpty()) {
            "A game round must contain at least one question."
        }

        require(questions.size == config.questionCount) {
            "Question list size must match the configured question count."
        }
    }

    private val mutableAttempts = mutableListOf<QuestionAttempt>()

    val attempts: List<QuestionAttempt>
        get() = mutableAttempts.toList()

    var currentQuestionIndex: Int = 0
        private set

    var status: GameRoundStatus = GameRoundStatus.NotStarted
        private set

    val currentQuestion: ArithmeticQuestion
        get() {
            check(!isFinished) {
                "A completed or abandoned round does not have an active question."
            }

            return questions[currentQuestionIndex]
        }

    val currentQuestionNumber: Int
        get() = currentQuestionIndex + 1

    val totalQuestions: Int
        get() = questions.size

    val hasMoreQuestions: Boolean
        get() = currentQuestionIndex < questions.lastIndex

    val hasRecordedCurrentAttempt: Boolean
        get() = mutableAttempts.any {
            it.questionIndex == currentQuestionIndex
        }

    val isFinished: Boolean
        get() = status == GameRoundStatus.Completed ||
                status == GameRoundStatus.Abandoned

    fun start() {
        check(status == GameRoundStatus.NotStarted) {
            "Only a round that has not started may be started."
        }

        status = GameRoundStatus.InProgress
    }

    fun recordAttempt(attempt: QuestionAttempt) {
        check(status == GameRoundStatus.InProgress) {
            "Attempts may only be recorded during an active round."
        }

        check(attempt.questionIndex == currentQuestionIndex) {
            "Attempt question index does not match the current question."
        }

        check(!hasRecordedCurrentAttempt) {
            "The current question already has a recorded attempt."
        }

        mutableAttempts += attempt
    }

    fun advance() {
        check(status == GameRoundStatus.InProgress) {
            "Only an active round may advance."
        }

        check(hasRecordedCurrentAttempt) {
            "An attempt must be recorded before advancing."
        }

        check(hasMoreQuestions) {
            "There are no more questions to advance to."
        }

        currentQuestionIndex++
    }

    fun complete() {
        check(status == GameRoundStatus.InProgress) {
            "Only an active round may be completed."
        }

        check(!hasMoreQuestions) {
            "The round cannot be completed before reaching the final question."
        }

        check(hasRecordedCurrentAttempt) {
            "The final question must have a recorded attempt."
        }

        check(mutableAttempts.size == questions.size) {
            "Every question must have exactly one recorded attempt."
        }

        status = GameRoundStatus.Completed
    }

    fun abandon() {
        check(!isFinished) {
            "A completed or abandoned round cannot be abandoned again."
        }

        status = GameRoundStatus.Abandoned
    }
}

enum class GameRoundStatus {
    NotStarted,
    InProgress,
    Completed,
    Abandoned
}
