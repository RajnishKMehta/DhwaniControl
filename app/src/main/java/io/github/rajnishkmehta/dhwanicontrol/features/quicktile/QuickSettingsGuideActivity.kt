package io.github.rajnishkmehta.dhwanicontrol.features.quicktile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.databinding.ActivityQuickSettingsGuideBinding

class QuickSettingsGuideActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuickSettingsGuideBinding
    private val steps = listOf(
        QuickSettingsGuideStep(
            titleRes = R.string.quick_tile_guide_step_1_title,
            bodyRes = R.string.quick_tile_guide_step_1_body,
            imageUrlRes = R.string.quick_tile_guide_step_1_image_url
        ),
        QuickSettingsGuideStep(
            titleRes = R.string.quick_tile_guide_step_2_title,
            bodyRes = R.string.quick_tile_guide_step_2_body,
            imageUrlRes = R.string.quick_tile_guide_step_2_image_url
        ),
        QuickSettingsGuideStep(
            titleRes = R.string.quick_tile_guide_step_3_title,
            bodyRes = R.string.quick_tile_guide_step_3_body,
            imageUrlRes = R.string.quick_tile_guide_step_3_image_url
        ),
        QuickSettingsGuideStep(
            titleRes = R.string.quick_tile_guide_step_4_title,
            bodyRes = R.string.quick_tile_guide_step_4_body,
            imageUrlRes = R.string.quick_tile_guide_step_4_image_url
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuickSettingsGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = QuickSettingsGuidePagerAdapter(steps)
        binding.guidePager.adapter = adapter
        binding.guidePager.offscreenPageLimit = 1
        binding.guidePager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.guidePager.setPageTransformer { page, position ->
            val scale = 0.94f + (1f - kotlin.math.abs(position)) * 0.06f
            page.scaleX = scale
            page.scaleY = scale
            page.alpha = 0.7f + (1f - kotlin.math.abs(position)) * 0.3f
        }

        updateControls(0)

        binding.guidePager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateControls(position)
            }
        })

        binding.previousButton.setOnClickListener {
            val current = binding.guidePager.currentItem
            if (current > 0) {
                binding.guidePager.currentItem = current - 1
            }
        }

        binding.nextButton.setOnClickListener {
            val current = binding.guidePager.currentItem
            if (current < steps.lastIndex) {
                binding.guidePager.currentItem = current + 1
            } else {
                finish()
            }
        }

        binding.doneButton.setOnClickListener {
            finish()
        }
    }

    private fun updateControls(position: Int) {
        val total = steps.size
        binding.stepCounterText.text = getString(R.string.quick_tile_guide_step_counter, position + 1, total)
        binding.previousButton.isEnabled = position > 0
        binding.nextButton.isEnabled = position < steps.lastIndex
        binding.nextButton.text = if (position == steps.lastIndex) {
            getString(R.string.quick_tile_guide_done_button)
        } else {
            getString(R.string.quick_tile_guide_next_button)
        }
    }
}
