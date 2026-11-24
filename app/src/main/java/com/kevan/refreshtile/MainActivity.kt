package com.kevan.refreshtile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pad = (24 * resources.displayMetrics.density).toInt()
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(pad, pad, pad, pad)
        }
        status = TextView(this).apply { textSize = 18f; gravity = Gravity.CENTER }
        val grant = Button(this).apply {
            text = "Grant permission"
            setOnClickListener {
                startActivity(
                    Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:$packageName"))
                )
            }
        }
        val b60 = Button(this).apply { text = "Set 60 Hz"; setOnClickListener { apply(false) } }
        val b120 = Button(this).apply { text = "Set 120 Hz"; setOnClickListener { apply(true) } }
        val hint = TextView(this).apply {
            text = "Add the tile: swipe down twice, tap the pencil, drag \"Refresh rate\" into your tiles."
            gravity = Gravity.CENTER
        }
        listOf(status, grant, b60, b120, hint).forEach { root.addView(it) }
        setContentView(root)
    }

    private fun apply(high: Boolean) {
        if (RefreshUtil.canWrite(this)) RefreshUtil.set(this, high)
        refresh()
    }

    override fun onResume() { super.onResume(); refresh() }

    private fun refresh() {
        status.text = when {
            !RefreshUtil.canWrite(this) -> "Permission needed"
            RefreshUtil.isHigh(this) -> "Currently 120 Hz"
            else -> "Currently 60 Hz"
        }
    }
}
