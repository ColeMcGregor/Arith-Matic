package com.wiseravenstudios.arithmatic.platform.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.wiseravenstudios.arithmatic.R
import java.util.concurrent.ConcurrentHashMap

/**
 * Uses SoundPool because Arith-Matic's sound effects are short, preloaded
 * sounds that need low-latency playback from UI controls and gameplay events.
 */
class AndroidSoundEffectPlayer(
    context: Context
) : SoundEffectPlayer {

    private val soundPool =
        SoundPool.Builder()
            .setMaxStreams(
                MAX_STREAMS
            )
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(
                        AudioAttributes.USAGE_GAME
                    )
                    .setContentType(
                        AudioAttributes.CONTENT_TYPE_SONIFICATION
                    )
                    .build()
            )
            .build()

    private val loadedSoundIds =
        ConcurrentHashMap
            .newKeySet<Int>()

    private val soundIds =
        mutableMapOf<SoundEffect, Int>()

    private val activeSingleInstanceStreams =
        mutableMapOf<SoundEffect, Int>()

    private var enabled =
        true

    private var volume =
        1f

    private var closed =
        false

    init {
        soundPool.setOnLoadCompleteListener {
                _,
                sampleId,
                status ->

            if (status == LOAD_SUCCESS) {
                loadedSoundIds.add(
                    sampleId
                )
            }
        }

        val applicationContext =
            context.applicationContext

        soundIds[SoundEffect.ButtonPress] =
            soundPool.load(
                applicationContext,
                R.raw.ui_button_press,
                LOAD_PRIORITY
            )

        soundIds[SoundEffect.CorrectAnswer] =
            soundPool.load(
                applicationContext,
                R.raw.answer_correct,
                LOAD_PRIORITY
            )

        soundIds[SoundEffect.IncorrectAnswer] =
            soundPool.load(
                applicationContext,
                R.raw.answer_incorrect,
                LOAD_PRIORITY
            )

        soundIds[SoundEffect.RoundComplete] =
            soundPool.load(
                applicationContext,
                R.raw.round_complete,
                LOAD_PRIORITY
            )

        soundIds[SoundEffect.Back] =
            soundPool.load(
                applicationContext,
                R.raw.navigation_back,
                LOAD_PRIORITY
            )
    }

    override fun play(
        soundEffect: SoundEffect
    ) {
        if (
            closed ||
            !enabled ||
            volume <= 0f
        ) {
            return
        }

        val soundId =
            soundIds[soundEffect]
                ?: return

        if (
            soundId !in loadedSoundIds
        ) {
            return
        }

        if (
            soundEffect in
            SINGLE_INSTANCE_EFFECTS
        ) {
            activeSingleInstanceStreams
                .remove(
                    soundEffect
                )
                ?.let { streamId ->
                    soundPool.stop(
                        streamId
                    )
                }
        }

        val streamId =
            soundPool.play(
                soundId,
                volume,
                volume,
                PLAYBACK_PRIORITY,
                NO_LOOP,
                NORMAL_PLAYBACK_RATE
            )

        if (
            streamId != PLAY_FAILED &&
            soundEffect in
            SINGLE_INSTANCE_EFFECTS
        ) {
            activeSingleInstanceStreams[
                soundEffect
            ] = streamId
        }
    }

    override fun setEnabled(
        enabled: Boolean
    ) {
        this.enabled =
            enabled

        if (!enabled) {
            stopSingleInstanceStreams()
        }
    }

    override fun setVolume(
        volume: Float
    ) {
        this.volume =
            volume.coerceIn(
                MINIMUM_VOLUME,
                MAXIMUM_VOLUME
            )
    }

    override fun close() {
        if (closed) {
            return
        }

        closed =
            true

        activeSingleInstanceStreams
            .clear()

        loadedSoundIds
            .clear()

        soundIds
            .clear()

        soundPool.release()
    }

    /**
     * Stops UI sounds that are intentionally limited to one active instance
     * when sound effects are disabled.
     */
    private fun stopSingleInstanceStreams() {
        activeSingleInstanceStreams
            .values
            .forEach { streamId ->
                soundPool.stop(
                    streamId
                )
            }

        activeSingleInstanceStreams
            .clear()
    }

    private companion object {

        const val MAX_STREAMS =
            4

        const val LOAD_PRIORITY =
            1

        const val LOAD_SUCCESS =
            0

        const val PLAYBACK_PRIORITY =
            1

        const val NO_LOOP =
            0

        const val NORMAL_PLAYBACK_RATE =
            1f

        const val PLAY_FAILED =
            0

        const val MINIMUM_VOLUME =
            0f

        const val MAXIMUM_VOLUME =
            1f

        /**
         * These UI effects restart instead of overlapping because they can be
         * triggered rapidly by repeated button presses or navigation actions.
         */
        val SINGLE_INSTANCE_EFFECTS =
            setOf(
                SoundEffect.ButtonPress,
                SoundEffect.Back
            )
    }
}