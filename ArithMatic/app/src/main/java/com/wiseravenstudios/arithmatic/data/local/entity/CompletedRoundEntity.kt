package com.wiseravenstudios.arithmatic.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "completed_rounds"
)
data class CompletedRoundEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val completedAtEpochMillis: Long,

    val activeRoundDurationMillis: Long,

    val enabledOperations: String,

    val allowNegatives: Boolean,

    val allowDecimals: Boolean,

    val wholeNumberDigits: Int,

    val questionCount: Int
)