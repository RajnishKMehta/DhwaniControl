package io.github.rajnishkmehta.dhwanicontrol.features.edge

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.rajnishkmehta.dhwanicontrol.Constants
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureAvailabilityEvaluator
import io.github.rajnishkmehta.dhwanicontrol.core.preferences.AppPreferences
import io.github.rajnishkmehta.dhwanicontrol.databinding.ActivityEdgeSwipeSideSelectorBinding

class EdgeSwipeSideSelectorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEdgeSwipeSideSelectorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEdgeSwipeSideSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedSide = AppPreferences.getEdgeSelectedSide(this)
        if (selectedSide == Constants.SIDE_LEFT) {
            binding.leftSideRadio.isChecked = true
        } else {
            binding.rightSideRadio.isChecked = true
        }

        binding.saveEdgeButton.setOnClickListener {
            val side = if (binding.leftSideRadio.isChecked) {
                Constants.SIDE_LEFT
            } else {
                Constants.SIDE_RIGHT
            }

            AppPreferences.setEdgeSelectedSide(this, side)
            AppPreferences.setEdgeConfigured(this, true)
            EdgeOverlayRuntime.sync(this)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        val availability = FeatureAvailabilityEvaluator.enforce(this, EdgeSwipeFeatureController)
        if (availability.isBlocked) {
            val reason = availability.blockResult.resolveReason(this)
            if (!reason.isNullOrBlank()) {
                Toast.makeText(this, reason, Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}
