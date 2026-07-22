package com.wiseravenstudios.arithmatic.data.repository

import com.wiseravenstudios.arithmatic.data.local.dao.CompletedRoundDao
import com.wiseravenstudios.arithmatic.data.local.mapper.CompletedRoundPersistenceMapper
import com.wiseravenstudios.arithmatic.data.local.relation.CompletedRoundWithAttempts
import com.wiseravenstudios.arithmatic.domain.results.CompletedGameRoundDto
import kotlinx.coroutines.flow.Flow

class CompletedRoundRepository(
    private val completedRoundDao: CompletedRoundDao
) {

    suspend fun saveCompletedRound(
        completedRound: CompletedGameRoundDto
    ): Long {
        val roundEntity =
            CompletedRoundPersistenceMapper.toRoundEntity(
                completedRound = completedRound
            )

        val attemptEntities =
            CompletedRoundPersistenceMapper.toAttemptEntities(
                completedRound = completedRound,
                roundId = 0L
            )

        return completedRoundDao.insertCompletedRound(
            round = roundEntity,
            attempts = attemptEntities
        )
    }

    fun observeAllCompletedRounds():
            Flow<List<CompletedRoundWithAttempts>> {
        return completedRoundDao.observeAllCompletedRounds()
    }

    fun observeCompletedRoundCount(): Flow<Int> {
        return completedRoundDao.observeCompletedRoundCount()
    }

    suspend fun getCompletedRoundById(
        roundId: Long
    ): CompletedRoundWithAttempts? {
        require(roundId > 0L) {
            "Round ID must be greater than zero."
        }

        return completedRoundDao.getCompletedRoundById(
            roundId = roundId
        )
    }

    suspend fun deleteCompletedRoundById(
        roundId: Long
    ): Boolean {
        require(roundId > 0L) {
            "Round ID must be greater than zero."
        }

        return completedRoundDao.deleteCompletedRoundById(
            roundId = roundId
        ) > 0
    }

    suspend fun deleteAllCompletedRounds(): Int {
        return completedRoundDao.deleteAllCompletedRounds()
    }
}