package com.wiseravenstudios.arithmatic.ui.common

import androidx.compose.runtime.staticCompositionLocalOf
import com.wiseravenstudios.arithmatic.platform.audio.SoundEffectPlayer

/**
 * Makes the app-scoped SoundEffectPlayer available to shared Compose controls
 * without passing the same dependency through every board and component.
 */
val LocalSoundEffectPlayer =
    staticCompositionLocalOf<SoundEffectPlayer> {
        error(
            "SoundEffectPlayer was not provided."
        )
    }