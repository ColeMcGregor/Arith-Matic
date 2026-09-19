package com.wiseravenstudios.arithmatic.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wiseravenstudios.arithmatic.data.repository.CompletedRoundRepository

/**
 * Supplies GameViewModel with the completed-round repository owned by the
 * application composition root.
 */
class GameViewModelFactory(
    private val completedRoundRepository: CompletedRoundRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (
            modelClass.isAssignableFrom(
                GameViewModel::class.java
            )
        ) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(
                completedRoundRepository =
                    completedRoundRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}