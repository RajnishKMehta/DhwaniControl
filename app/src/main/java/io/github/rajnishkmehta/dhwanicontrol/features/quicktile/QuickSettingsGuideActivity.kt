package io.github.rajnishkmehta.dhwanicontrol.features.quicktile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.rajnishkmehta.dhwanicontrol.databinding.ActivityQuickSettingsGuideBinding

class QuickSettingsGuideActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuickSettingsGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuickSettingsGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.doneButton.setOnClickListener {
            finish()
        }
    }
}
