package com.colemcg.arith_matic.generation

import com.colemcg.arith_matic.model.GameSettings
import com.colemcg.arith_matic.model.QuestionCard
import com.colemcg.arith_matic.model.QuestionType
import com.colemcg.arith_matic.utils.RNG

/**
 * Factory that delegates question creation to the appropriate generator.
 * is used for setting up the game, by creating the question cards
 *
 * Usage:
 *   val factory = CardFactory.default()
 *   val card = factory.generateCard(QuestionType.ADDITION, settings)
 *   val random = factory.generateRandomCard(settings)
 *   val many = factory.generateBatch(settings, count = 10)
 */
class CardFactory(
    private val generators: Map<QuestionType, QuestionGenerator>
) {

    /** Ensure we have a generator for the requested type and that it supports the settings. */
    fun generateCard(type: QuestionType, settings: GameSettings): QuestionCard {
        val gen = generators[type]
            ?: error("No generator registered for type: $type")
        require(gen.supports(settings)) { "Generator $type does not support supplied settings." }
        return gen.generate(settings)
    }

    /** Types that are both selected in settings and supported by their generators. */
    fun supportedTypes(settings: GameSettings): List<QuestionType> {
        val selected = if (settings.selectedTypes.isEmpty())
            QuestionType.defaultSelected()
        else
            settings.selectedTypes

        return selected.filter { t -> generators[t]?.supports(settings) == true }
    }

    /** Pick a supported type at random (throws if none supported). */
    fun pickType(settings: GameSettings): QuestionType {
        val pool = supportedTypes(settings)
        require(pool.isNotEmpty()) { "No supported question types for current settings." }
        return RNG.choose(pool)
    }

    /** Generate a single random card from any supported type. */
    fun generateRandomCard(settings: GameSettings): QuestionCard =
        generateCard(pickType(settings), settings)

    /** Generate a list of cards (random types chosen per card). */
    fun generateBatch(settings: GameSettings, count: Int): List<QuestionCard> {
        require(count >= 0) { "count must be >= 0" }
        if (count == 0) return emptyList()
        val pool = supportedTypes(settings)
        require(pool.isNotEmpty()) { "No supported question types for current settings." }
        return List(count) { generateCard(RNG.choose(pool), settings) }
    }

    companion object {
        /** Default registry. Add your other generators here as you implement them. */
        fun default(): CardFactory = CardFactory(
            mapOf(
                QuestionType.ADDITION to AdditionGenerator()
                // QuestionType.SUBTRACTION to SubtractionGenerator(),
                // QuestionType.MULTIPLICATION to MultiplicationGenerator(),
                // QuestionType.DIVISION to DivisionGenerator(),
                // QuestionType.LOGIC to LogicGenerator(),
                // QuestionType.ADVANCED to AdvancedGenerator(),
            )
        )
    }
}
