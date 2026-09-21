package com.wiseravenstudios.arithmatic.platform.audio

/**
 * Keeps long-form background audio separate from SoundEffectPlayer because
 * music needs persistent pause and resume behavior rather than short pooled
 * playback.
 */
interface BackgroundMusicPlayer {
    fun play()
    fun pause()
    fun setVolume(volume: Float)
    fun close()
}