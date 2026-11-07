package io.github.rajnishkmehta.dhwanicontrol.features.assist

import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AssistEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent?.action != Intent.ACTION_ASSIST) {
            finish()
            return
        }

        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        runCatching {
            audioManager.adjustVolume(AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI)
        }

        finish()
    }
}
