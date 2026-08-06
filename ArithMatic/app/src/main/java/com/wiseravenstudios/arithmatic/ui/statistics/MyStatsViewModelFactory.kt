package com.wiseravenstudios.arithmatic.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wiseravenstudios.arithmatic.data.repository.CompletedRoundRepository

class MyStatsViewModelFactory(
    private val completedRoundRepository: CompletedRoundRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (
            modelClass.isAssignableFrom(
                MyStatsViewModel::class.java
            )
        ) {
            return MyStatsViewModel(
                completedRoundRepository =
                    completedRoundRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}