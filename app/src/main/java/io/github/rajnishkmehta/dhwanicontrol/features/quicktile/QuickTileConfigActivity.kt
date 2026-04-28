package io.github.rajnishkmehta.dhwanicontrol.features.quicktile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.rajnishkmehta.dhwanicontrol.databinding.ActivityQuickTileConfigBinding

class QuickTileConfigActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuickTileConfigBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuickTileConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.openTutorialButton.setOnClickListener {
            startActivity(Intent(this, QuickSettingsGuideActivity::class.java))
        }

        binding.doneButton.setOnClickListener {
            finish()
        }
    }
}
