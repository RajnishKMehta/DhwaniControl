package io.github.rajnishkmehta.dhwanicontrol.permission

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.button.MaterialButton
import io.github.rajnishkmehta.dhwanicontrol.Constants
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureAvailabilityEvaluator
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureController
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureRegistry
import io.github.rajnishkmehta.dhwanicontrol.core.feature.PermissionRequirement
import io.github.rajnishkmehta.dhwanicontrol.core.permission.PermissionPolicy
import io.github.rajnishkmehta.dhwanicontrol.databinding.ActivityPermissionHubBinding

class PermissionHubActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPermissionHubBinding
    private var featureController: FeatureController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionHubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val featureId = intent.getStringExtra(Constants.EXTRA_FEATURE_ID)
        featureController = featureId?.let { FeatureRegistry.findById(it) }

        if (featureController == null) {
            finish()
            return
        }

        binding.accessibilityGrantButton.setOnClickListener {
            openAccessibilitySettings()
        }

        binding.continueButton.setOnClickListener {
            routeToFeatureConfigIfReady()
        }

        refreshPermissionUi()
    }

    override fun onResume() {
        super.onResume()
        refreshPermissionUi()
    }

    private fun routeToFeatureConfigIfReady() {
        val controller = featureController ?: return
        val availability = FeatureAvailabilityEvaluator.enforce(this, controller)
        if (availability.isBlocked || availability.missingPermissions.isNotEmpty()) {
            refreshPermissionUi()
            return
        }

        controller.openConfig(this)
        finish()
    }

    private fun openAccessibilitySettings() {
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }

    private fun refreshPermissionUi() {
        val controller = featureController ?: return
        val featureName = getString(controller.spec.titleRes)
        binding.permissionScreenTitle.text = getString(R.string.permission_screen_title_with_feature, featureName)
        val availability = FeatureAvailabilityEvaluator.enforce(this, controller)

        val requirements = controller.spec.requiredPermissions
        val accessibilityRequired = requirements.contains(PermissionRequirement.Accessibility)
        val accessibilityGranted = PermissionPolicy.isGranted(this, PermissionRequirement.Accessibility)

        binding.accessibilityPermissionCard.visibility = if (accessibilityRequired) View.VISIBLE else View.GONE
        val blockedReason = availability.blockResult.resolveReason(this)
        binding.blockedReasonText.isVisible = availability.isBlocked && !blockedReason.isNullOrBlank()
        if (binding.blockedReasonText.isVisible) {
            binding.blockedReasonText.text = blockedReason
        }

        updatePermissionStatus(
            granted = accessibilityGranted,
            statusDot = binding.accessibilityStatusDot,
            statusText = binding.accessibilityStatusText,
            grantButton = binding.accessibilityGrantButton,
            required = accessibilityRequired,
            blocked = availability.isBlocked
        )

        val allPermissionsGranted = availability.missingPermissions.isEmpty()
        binding.continueButton.isEnabled = allPermissionsGranted && !availability.isBlocked
    }

    private fun updatePermissionStatus(
        granted: Boolean,
        statusDot: View,
        statusText: TextView,
        grantButton: MaterialButton,
        required: Boolean,
        blocked: Boolean
    ) {
        val accent = ContextCompat.getColor(this, R.color.colorAccent)
        val pending = ContextCompat.getColor(this, R.color.colorTextSecondary)
        val statusColor = if (granted) accent else pending

        statusDot.backgroundTintList = android.content.res.ColorStateList.valueOf(statusColor)
        statusText.setText(
            when {
                !required -> R.string.permission_not_required
                granted -> R.string.permission_status_granted
                else -> R.string.permission_status_pending
            }
        )
        statusText.setTextColor(statusColor)

        grantButton.isEnabled = required && !granted && !blocked
        grantButton.alpha = if (grantButton.isEnabled) 1f else 0.65f
    }
}
