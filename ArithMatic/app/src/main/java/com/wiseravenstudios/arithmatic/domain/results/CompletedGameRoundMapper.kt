package com.wiseravenstudios.arithmatic.domain.results

import com.wiseravenstudios.arithmatic.domain.model.GameRound
import com.wiseravenstudios.arithmatic.domain.model.GameRoundStatus

object CompletedGameRoundMapper {

    fun map(
        round: GameRound,
        activeRoundDurationMillis: Long
    ): CompletedGameRoundDto {
        require(round.status == GameRoundStatus.Completed) {
            "Only a completed game round may be mapped to completed results."
        }

        require(activeRoundDurationMillis >= 0L) {
            "Active round duration cannot be negative."
        }

        return CompletedGameRoundDto(
            config = round.config,
            questions = round.questions.toList(),
            attempts = round.attempts.toList(),
            activeRoundDurationMillis = activeRoundDurationMillis
        )
    }
}