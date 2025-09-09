package com.colemcg.arith_matic.vm

/**
 * This is the view model for the game
 * it is used as the temporary storage for the UI of the game, and is fed by the model
 * 
 * @author 
 */


import com.colemcg.arith_matic.model.GameSettings
import com.colemcg.arith_matic.model.QuestionCard
import com.colemcg.arith_matic.model.GameResults
import com.colemcg.arith_matic.generation.CardFactory
import com.colemcg.arith_matic.storage.SettingsDataStore

@Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
class GameViewModel(
    private val cardFactory: CardFactory,
    private val settingsDataStore: SettingsDataStore
) {
    // Fields from the reference diagram
    private var gameSettings: GameSettings? = null
    private val questionList: MutableList<QuestionCard> = mutableListOf()
    private var currentIndex: Int = 0
    private var score: Int = 0
    private var streak: Int = 0

    fun setSettings(settings: GameSettings) {
        // intentionally empty (stub)
    }

    fun startGame() {
        // intentionally empty (stub)
    }

    fun getNextQuestion(): QuestionCard {
        // intentionally empty (stub)
        throw NotImplementedError("stub")
    }

    fun recordAnswer(correct: Boolean, card: QuestionCard) {
        // intentionally empty (stub)
    }

    fun getResults(): GameResults {
        // intentionally empty (stub)
        throw NotImplementedError("stub")
    }
}
