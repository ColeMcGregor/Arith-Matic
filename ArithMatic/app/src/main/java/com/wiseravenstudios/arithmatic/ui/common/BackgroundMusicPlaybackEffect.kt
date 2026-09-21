package com.wiseravenstudios.arithmatic.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.wiseravenstudios.arithmatic.platform.audio.BackgroundMusicPlayer

/**
 * Bridges app visibility and playback policy to the background music player so
 * individual boards do not need to manage music or Android lifecycle events.
 */
@Composable
fun BackgroundMusicPlaybackEffect(
    player: BackgroundMusicPlayer,
    enabled: Boolean,
    volume: Float,
    playbackAllowed: Boolean
) {
    val view =
        LocalView.current

    val context =
        LocalContext.current

    val lifecycleOwner =
        view.findViewTreeLifecycleOwner()
            ?: context as? LifecycleOwner

    var isAppForegrounded by
    remember(
        lifecycleOwner
    ) {
        mutableStateOf(
            lifecycleOwner
                ?.lifecycle
                ?.currentState
                ?.isAtLeast(
                    Lifecycle.State.STARTED
                )
                ?: false
        )
    }

    DisposableEffect(
        lifecycleOwner
    ) {
        if (lifecycleOwner == null) {
            onDispose {
            }
        } else {
            val lifecycleObserver =
                LifecycleEventObserver {
                        _,
                        event ->

                    when (event) {
                        Lifecycle.Event.ON_START -> {
                            isAppForegrounded =
                                true
                        }

                        Lifecycle.Event.ON_STOP -> {
                            isAppForegrounded =
                                false
                        }

                        else ->
                            Unit
                    }
                }

            lifecycleOwner.lifecycle
                .addObserver(
                    lifecycleObserver
                )

            onDispose {
                lifecycleOwner.lifecycle
                    .removeObserver(
                        lifecycleObserver
                    )
            }
        }
    }

    LaunchedEffect(
        player,
        enabled,
        volume,
        playbackAllowed,
        isAppForegrounded
    ) {
        player.setVolume(
            volume
        )

        val shouldPlay =
            enabled &&
                    volume > 0f &&
                    playbackAllowed &&
                    isAppForegrounded

        if (shouldPlay) {
            player.play()
        } else {
            player.pause()
        }
    }
}