package com.kevan.refreshtile

import android.content.Context
import android.provider.Settings

object RefreshUtil {
    private const val PEAK = "peak_refresh_rate"
    private const val MIN = "min_refresh_rate"

    fun canWrite(c: Context) = Settings.System.canWrite(c)

    fun isHigh(c: Context): Boolean {
        val peak = Settings.System.getFloat(c.contentResolver, PEAK, 0f)
        return peak == 0f || peak > 60f
    }

    fun set(c: Context, high: Boolean) {
        val cr = c.contentResolver
        Settings.System.putFloat(cr, PEAK, if (high) 120f else 60f)
        Settings.System.putFloat(cr, MIN, 0f)
    }
}
