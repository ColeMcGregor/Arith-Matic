package com.wiseravenstudios.arithmatic.platform.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import com.wiseravenstudios.arithmatic.R

/**
 * Uses MediaPlayer for the packaged background track because the track is
 * long-form audio that must loop and preserve its position while paused.
 */
class AndroidBackgroundMusicPlayer(
    context: Context
) : BackgroundMusicPlayer {

    private val mediaPlayer: MediaPlayer =
        checkNotNull(
            MediaPlayer.create(
                context.applicationContext,
                R.raw.thinking_and_tinkering,
                AudioAttributes.Builder()
                    .setUsage(
                        AudioAttributes.USAGE_GAME
                    )
                    .setContentType(
                        AudioAttributes.CONTENT_TYPE_MUSIC
                    )
                    .build(),
                AudioManager.AUDIO_SESSION_ID_GENERATE
            )
        ) {
            "Unable to create the background music player."
        }.apply {
            isLooping = true
        }

    private var isClosed = false

    override fun play() {
        if (
            isClosed ||
            mediaPlayer.isPlaying
        ) {
            return
        }

        mediaPlayer.start()
    }

    override fun pause() {
        if (
            isClosed ||
            !mediaPlayer.isPlaying
        ) {
            return
        }

        mediaPlayer.pause()
    }

    override fun setVolume(
        volume: Float
    ) {
        if (isClosed) {
            return
        }

        val clampedVolume =
            volume.coerceIn(
                0f,
                1f
            )

        mediaPlayer.setVolume(
            clampedVolume,
            clampedVolume
        )
    }

    override fun close() {
        if (isClosed) {
            return
        }

        isClosed = true
        mediaPlayer.release()
    }
}