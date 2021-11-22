package com.its.admob.lib

import android.os.Handler
import android.os.Looper


fun Any.runDelay(duration: Long = 300, runnable: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(runnable, duration)
}