package com.wiseravenstudios.arithmatic.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.wiseravenstudios.arithmatic.data.local.entity.CompletedRoundEntity
import com.wiseravenstudios.arithmatic.data.local.entity.QuestionAttemptEntity

data class CompletedRoundWithAttempts(

    @Embedded
    val round: CompletedRoundEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "roundId"
    )
    val attempts: List<QuestionAttemptEntity>
)