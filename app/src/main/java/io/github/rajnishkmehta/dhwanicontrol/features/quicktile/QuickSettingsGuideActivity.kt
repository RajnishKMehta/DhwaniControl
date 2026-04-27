package io.github.rajnishkmehta.dhwanicontrol.features.quicktile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.databinding.ActivityQuickSettingsGuideBinding

class QuickSettingsGuideActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuickSettingsGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuickSettingsGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tutorialImageUrl = getString(R.string.quick_tile_tutorial_image_url)
        binding.tutorialImage.load(tutorialImageUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_notification)
            error(R.drawable.ic_notification)
        }
        binding.imageUrlText.text = tutorialImageUrl

        binding.doneButton.setOnClickListener {
            finish()
        }
    }
}
