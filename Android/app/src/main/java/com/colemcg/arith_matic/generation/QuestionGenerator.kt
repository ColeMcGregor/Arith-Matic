package com.colemcg.arith_matic.generation

import com.colemcg.arith_matic.model.GameSettings
import com.colemcg.arith_matic.model.QuestionCard
import com.colemcg.arith_matic.model.QuestionType

/**
 * Contract for all question generators (Addition, Subtraction, etc.).
 *
 * Implementations should be PURE (no I/O), and use the global RNG singleton
 * (com.colemcg.arith_matic.utils.Rng) internally when randomness is needed.
 *
 * Example inside an implementation:
 *   val a = Rng.int(1, 10)
 *   val option = Rng.choose(options)
 */
interface QuestionGenerator {

    /** The QuestionType this generator produces. */
    val type: QuestionType

    /**
     * Produce a single QuestionCard based on the provided GameSettings.
     * Use com.colemcg.arith_matic.utils.Rng for randomness.
     */
    fun generate(settings: GameSettings): QuestionCard

    /**
     * Return false if the generator cannot operate under these settings
     * (e.g., requires decimals but allowDecimals == false). Defaults to true.
     */
    fun supports(settings: GameSettings): Boolean = true
}
