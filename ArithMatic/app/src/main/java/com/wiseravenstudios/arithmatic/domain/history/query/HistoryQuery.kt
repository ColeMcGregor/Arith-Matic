package com.wiseravenstudios.arithmatic.domain.history.query

/**
 * Describes which saved rounds and attempts a history consumer wants.
 *
 * This model is shared by:
 *
 * - My Stats
 * - Adult Stats
 * - history export
 *
 * It contains no Android, Compose, Room, or UI dependencies.
 */
data class HistoryQuery(
    val roundCriteria: RoundCriteria = RoundCriteria(),
    val attemptCriteria: AttemptCriteria = AttemptCriteria()
) {

    companion object {

        /**
         * Selects every stored completed round and every attempt.
         */
        val All = HistoryQuery()
    }
}