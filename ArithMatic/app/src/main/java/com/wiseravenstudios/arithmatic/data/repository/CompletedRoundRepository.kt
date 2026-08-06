
package com.wiseravenstudios.arithmatic.data.repository

import com.wiseravenstudios.arithmatic.data.local.dao.CompletedRoundDao
import com.wiseravenstudios.arithmatic.data.local.mapper.CompletedRoundHistoryMapper
import com.wiseravenstudios.arithmatic.data.local.mapper.CompletedRoundPersistenceMapper
import com.wiseravenstudios.arithmatic.domain.results.CompletedGameRoundDto
import com.wiseravenstudios.arithmatic.domain.statistics.model.CompletedRoundHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    /**
     * Observes all completed rounds as persistence-independent domain history.
     *
     * Room entities and relation models remain contained inside the data layer.
     */
    fun observeCompletedRoundHistory():
            Flow<List<CompletedRoundHistory>> {
        return completedRoundDao
            .observeAllCompletedRounds()
            .map(
                CompletedRoundHistoryMapper::toHistoryList
            )
    }

    fun observeCompletedRoundCount(): Flow<Int> {
        return completedRoundDao.observeCompletedRoundCount()
    }

    /**
     * Retrieves one completed round as a persistence-independent domain model.
     */
    suspend fun getCompletedRoundById(
        roundId: Long
    ): CompletedRoundHistory? {
        require(roundId > 0L) {
            "Round ID must be greater than zero."
        }

        return completedRoundDao
            .getCompletedRoundById(
                roundId = roundId
            )
            ?.let(
                CompletedRoundHistoryMapper::toHistory
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

