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

        bindStep(
            imageUrl = getString(R.string.quick_tile_guide_step_1_image_url),
            imageView = binding.step1Image,
            urlTextView = binding.step1ImageUrlText
        )
        bindStep(
            imageUrl = getString(R.string.quick_tile_guide_step_2_image_url),
            imageView = binding.step2Image,
            urlTextView = binding.step2ImageUrlText
        )
        bindStep(
            imageUrl = getString(R.string.quick_tile_guide_step_3_image_url),
            imageView = binding.step3Image,
            urlTextView = binding.step3ImageUrlText
        )
        bindStep(
            imageUrl = getString(R.string.quick_tile_guide_step_4_image_url),
            imageView = binding.step4Image,
            urlTextView = binding.step4ImageUrlText
        )

        binding.doneButton.setOnClickListener {
            finish()
        }
    }

    private fun bindStep(
        imageUrl: String,
        imageView: android.widget.ImageView,
        urlTextView: android.widget.TextView
    ) {
        imageView.load(imageUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_notification)
            error(R.drawable.ic_notification)
        }
        urlTextView.text = imageUrl
    }
}
