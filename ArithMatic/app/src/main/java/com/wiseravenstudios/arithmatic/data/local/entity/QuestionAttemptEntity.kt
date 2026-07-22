package com.wiseravenstudios.arithmatic.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "question_attempts",
    foreignKeys = [
        ForeignKey(
            entity = CompletedRoundEntity::class,
            parentColumns = ["id"],
            childColumns = ["roundId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            value = ["roundId"]
        ),
        Index(
            value = ["roundId", "questionIndex"],
            unique = true
        )
    ]
)
data class QuestionAttemptEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val roundId: Long,

    val questionIndex: Int,

    val operation: String?,

    val questionText: String,

    val expectedAnswer: String,

    val selectedAnswer: String,

    val answerChoice0: String,

    val answerChoice1: String,

    val answerChoice2: String,

    val answerChoice3: String,

    val selectedChoiceIndex: Int,

    val correctChoiceIndex: Int,

    val isCorrect: Boolean,

    val activeDurationMillis: Long
)