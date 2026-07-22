package com.wiseravenstudios.arithmatic.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.wiseravenstudios.arithmatic.data.local.entity.CompletedRoundEntity
import com.wiseravenstudios.arithmatic.data.local.entity.QuestionAttemptEntity
import com.wiseravenstudios.arithmatic.data.local.relation.CompletedRoundWithAttempts
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CompletedRoundDao {

    @Insert
    protected abstract suspend fun insertRound(
        round: CompletedRoundEntity
    ): Long

    @Insert
    protected abstract suspend fun insertAttempts(
        attempts: List<QuestionAttemptEntity>
    )

    @Transaction
    open suspend fun insertCompletedRound(
        round: CompletedRoundEntity,
        attempts: List<QuestionAttemptEntity>
    ): Long {
        require(round.id == 0L) {
            "A new completed round must not already have a database ID."
        }

        require(attempts.isNotEmpty()) {
            "A completed round must contain at least one question attempt."
        }

        val roundId = insertRound(round)

        val attemptsWithRoundId =
            attempts.map { attempt ->
                attempt.copy(
                    id = 0L,
                    roundId = roundId
                )
            }

        insertAttempts(attemptsWithRoundId)

        return roundId
    }

    @Transaction
    @Query(
        """
        SELECT *
        FROM completed_rounds
        ORDER BY completedAtEpochMillis DESC, id DESC
        """
    )
    abstract fun observeAllCompletedRounds():
            Flow<List<CompletedRoundWithAttempts>>

    @Transaction
    @Query(
        """
        SELECT *
        FROM completed_rounds
        WHERE id = :roundId
        LIMIT 1
        """
    )
    abstract suspend fun getCompletedRoundById(
        roundId: Long
    ): CompletedRoundWithAttempts?

    @Query(
        """
        SELECT COUNT(*)
        FROM completed_rounds
        """
    )
    abstract fun observeCompletedRoundCount(): Flow<Int>

    @Query(
        """
        DELETE FROM completed_rounds
        WHERE id = :roundId
        """
    )
    abstract suspend fun deleteCompletedRoundById(
        roundId: Long
    ): Int

    @Query(
        """
        DELETE FROM completed_rounds
        """
    )
    abstract suspend fun deleteAllCompletedRounds(): Int
}