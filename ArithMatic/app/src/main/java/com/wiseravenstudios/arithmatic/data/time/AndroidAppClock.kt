package com.wiseravenstudios.arithmatic.data.time

import android.os.SystemClock
import com.wiseravenstudios.arithmatic.domain.time.AppClock

class AndroidAppClock : AppClock {

    override fun elapsedRealtimeMillis(): Long {
        return SystemClock.elapsedRealtime()
    }
}