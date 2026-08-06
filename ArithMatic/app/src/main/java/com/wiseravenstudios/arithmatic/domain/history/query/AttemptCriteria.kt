package com.wiseravenstudios.arithmatic.domain.history.query

import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation

/**
 * Filters individual attempts inside rounds that passed [RoundCriteria].
 *
 * An empty operation set means attempts of every operation are accepted.
 *
 * A populated operation set means an attempt is accepted when its operation
 * is one of the selected operations.
 */
data class AttemptCriteria(
    val operations: Set<ArithmeticOperation> = emptySet(),
    val correctness: CorrectnessFilter =
        CorrectnessFilter.All
)