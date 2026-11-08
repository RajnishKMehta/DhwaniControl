/*
 * Copyright 2026 Rajnish Kumar
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.rajnishkmehta.dhwanicontrol.features.assist

import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.rajnishkmehta.dhwanicontrol.R

class AssistEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.action != Intent.ACTION_ASSIST) {
            finish()
            return
        }

        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        runCatching {
            audioManager.adjustVolume(AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI)
        }.onFailure { e ->
            Log.e("AssistEntryActivity", "Failed to adjust volume", e)
            Toast.makeText(this, R.string.feature_action_failed, Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}
