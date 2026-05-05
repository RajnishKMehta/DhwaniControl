package io.github.rajnishkmehta.dhwanicontrol.features.edge

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.Constants
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureAvailabilityEvaluator
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureBlockResult
import io.github.rajnishkmehta.dhwanicontrol.core.preferences.AppPreferences
import io.github.rajnishkmehta.dhwanicontrol.databinding.ActivityEdgeSwipeSideSelectorBinding
import kotlin.math.roundToInt

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

        val edgeWidthMin = (Constants.EDGE_ZONE_PERCENT_MIN * 100f).roundToInt()
        val edgeWidthMax = (Constants.EDGE_ZONE_PERCENT_MAX * 100f).roundToInt()
        val currentEdgeWidthPercent = (AppPreferences.getEdgeZonePercent(this) * 100f)
            .roundToInt()
            .coerceIn(edgeWidthMin, edgeWidthMax)
        binding.edgeWidthSlider.valueFrom = edgeWidthMin.toFloat()
        binding.edgeWidthSlider.valueTo = edgeWidthMax.toFloat()
        binding.edgeWidthSlider.stepSize = 1f
        binding.edgeWidthSlider.value = currentEdgeWidthPercent.toFloat()
        updateEdgeWidthLabel(currentEdgeWidthPercent)
        binding.edgeWidthSlider.addOnChangeListener { _, value, _ ->
            updateEdgeWidthLabel(value.roundToInt())
        }

        binding.saveEdgeButton.setOnClickListener {
            val side = if (binding.leftSideRadio.isChecked) {
                Constants.SIDE_LEFT
            } else {
                Constants.SIDE_RIGHT
            }
            val edgeWidthPercent = binding.edgeWidthSlider.value / 100f

            AppPreferences.setEdgeSelectedSide(this, side)
            AppPreferences.setEdgeZonePercent(this, edgeWidthPercent)
            AppPreferences.setEdgeConfigured(this, true)
            EdgeOverlayRuntime.sync(this)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        val availability = FeatureAvailabilityEvaluator.enforce(this, EdgeSwipeFeatureController)
        if (availability.isBlocked) {
            val reason = when (val blockResult = availability.blockResult) {
                is FeatureBlockResult.Blocked -> blockResult.resolveReason(this)
                FeatureBlockResult.NotBlocked -> null
            }
            if (!reason.isNullOrBlank()) {
                Toast.makeText(this, reason, Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    private fun updateEdgeWidthLabel(widthPercent: Int) {
        binding.edgeWidthValueText.text = getString(R.string.edge_selector_width_value, widthPercent)
    }
}
