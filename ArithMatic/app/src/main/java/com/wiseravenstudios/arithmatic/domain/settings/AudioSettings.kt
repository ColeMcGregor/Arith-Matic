package com.wiseravenstudios.arithmatic.domain.settings

/**
 * Immutable audio preferences for Arith-Matic.
 *
 * Music and sound effects each have:
 *
 * - an enabled state used as a quick mute control;
 * - a retained level from 0 through 20.
 *
 * Disabling a channel does not erase its saved level. When the channel is
 * enabled again, playback resumes at the previously selected level.
 */
data class AudioSettings(
    val musicEnabled: Boolean = DEFAULT_MUSIC_ENABLED,
    val musicLevel: Int = DEFAULT_MUSIC_LEVEL,
    val soundEffectsEnabled: Boolean =
        DEFAULT_SOUND_EFFECTS_ENABLED,
    val soundEffectsLevel: Int =
        DEFAULT_SOUND_EFFECTS_LEVEL
) {

    init {
        require(musicLevel in AUDIO_LEVEL_RANGE) {
            "Music level must be between $MIN_LEVEL and $MAX_LEVEL."
        }

        require(soundEffectsLevel in AUDIO_LEVEL_RANGE) {
            "Sound effects level must be between $MIN_LEVEL and $MAX_LEVEL."
        }
    }

    /**
     * Music level represented as a whole percentage.
     *
     * Examples:
     *
     * 0  -> 0%
     * 10 -> 50%
     * 17 -> 85%
     * 20 -> 100%
     */
    val musicPercent: Int
        get() = levelToPercent(
            level = musicLevel
        )

    /**
     * Sound-effects level represented as a whole percentage.
     */
    val soundEffectsPercent: Int
        get() = levelToPercent(
            level = soundEffectsLevel
        )

    /**
     * Music volume represented in the normalized 0f through 1f range expected
     * by Android playback APIs.
     *
     * A disabled channel always produces zero effective volume without
     * modifying the saved level.
     */
    val effectiveMusicVolume: Float
        get() = effectiveVolume(
            enabled = musicEnabled,
            level = musicLevel
        )

    /**
     * Sound-effects volume represented in the normalized 0f through 1f range
     * expected by Android playback APIs.
     */
    val effectiveSoundEffectsVolume: Float
        get() = effectiveVolume(
            enabled = soundEffectsEnabled,
            level = soundEffectsLevel
        )

    /**
     * Returns a copy with a validated music level.
     *
     * Clamping here is useful at application boundaries where a value may
     * originate outside the trusted domain model.
     */
    fun withClampedMusicLevel(
        level: Int
    ): AudioSettings {
        return copy(
            musicLevel = clampLevel(level)
        )
    }

    /**
     * Returns a copy with a validated sound-effects level.
     */
    fun withClampedSoundEffectsLevel(
        level: Int
    ): AudioSettings {
        return copy(
            soundEffectsLevel = clampLevel(level)
        )
    }

    companion object {

        const val MIN_LEVEL = 0
        const val MAX_LEVEL = 20

        const val DEFAULT_MUSIC_ENABLED = true
        const val DEFAULT_MUSIC_LEVEL = 12

        const val DEFAULT_SOUND_EFFECTS_ENABLED = true
        const val DEFAULT_SOUND_EFFECTS_LEVEL = 16

        val AUDIO_LEVEL_RANGE: IntRange =
            MIN_LEVEL..MAX_LEVEL

        /**
         * Converts a level from 0 through 20 into a percentage.
         *
         * Each level is five percentage points.
         */
        fun levelToPercent(
            level: Int
        ): Int {
            require(level in AUDIO_LEVEL_RANGE) {
                "Audio level must be between $MIN_LEVEL and $MAX_LEVEL."
            }

            return level * 5
        }

        /**
         * Converts a valid level into normalized playback volume.
         */
        fun levelToVolumeFraction(
            level: Int
        ): Float {
            require(level in AUDIO_LEVEL_RANGE) {
                "Audio level must be between $MIN_LEVEL and $MAX_LEVEL."
            }

            return level.toFloat() /
                    MAX_LEVEL.toFloat()
        }

        /**
         * Restricts an arbitrary integer to the supported audio-level range.
         */
        fun clampLevel(
            level: Int
        ): Int {
            return level.coerceIn(
                minimumValue = MIN_LEVEL,
                maximumValue = MAX_LEVEL
            )
        }

        private fun effectiveVolume(
            enabled: Boolean,
            level: Int
        ): Float {
            return if (enabled) {
                levelToVolumeFraction(
                    level = level
                )
            } else {
                0f
            }
        }
    }
}