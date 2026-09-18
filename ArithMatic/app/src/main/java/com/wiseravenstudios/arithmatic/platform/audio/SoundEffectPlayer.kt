package com.wiseravenstudios.arithmatic.platform.audio

/**
 * Uses an enum so callers can request sounds by meaning instead of depending
 * on Android resource IDs or filenames.
 */
enum class SoundEffect {
    ButtonPress,
    CorrectAnswer,
    IncorrectAnswer,
    RoundComplete,
    Back
}

/**
 * Gives UI controls and gameplay events one shared way to request sound
 * effects without depending directly on Android audio APIs.
 */
interface SoundEffectPlayer {

    fun play(
        soundEffect: SoundEffect
    )

    fun setEnabled(
        enabled: Boolean
    )

    fun setVolume(
        volume: Float
    )

    fun close()
}