package com.wiseravenstudios.arithmatic.platform.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class AppLifecycleObserver(
    private val onEnterBackground: () -> Unit,
    private val onEnterForeground: () -> Unit
) : DefaultLifecycleObserver {

    override fun onStop(owner: LifecycleOwner) {
        onEnterBackground()
    }

    override fun onStart(owner: LifecycleOwner) {
        onEnterForeground()
    }
}