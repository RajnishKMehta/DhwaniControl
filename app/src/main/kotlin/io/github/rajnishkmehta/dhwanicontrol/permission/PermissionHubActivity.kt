package io.github.rajnishkmehta.dhwanicontrol.permission

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import io.github.rajnishkmehta.dhwanicontrol.Constants
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.core.block.FeatureAvailabilityEvaluator
import io.github.rajnishkmehta.dhwanicontrol.core.block.FeatureBlockResult
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureController
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureRegistry
import io.github.rajnishkmehta.dhwanicontrol.core.feature.PermissionRequirement
import io.github.rajnishkmehta.dhwanicontrol.core.permission.PermissionPolicy
import io.github.rajnishkmehta.dhwanicontrol.core.preferences.AppPreferences
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

        binding.overlayGrantButton.setOnClickListener {
            openOverlayPermissionSettings()
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

        val blockResult = controller.blockCondition.evaluate(this)
        if (blockResult is FeatureBlockResult.Blocked) {
            refreshPermissionUi()
            return
        }

        val missingPermissions = PermissionPolicy.missingPermissions(
            this,
            controller.spec.requiredPermissions
        )

        if (missingPermissions.isNotEmpty()) {
            refreshPermissionUi()
            return
        }

        controller.openConfig(this)
        finish()
    }

    private fun openOverlayPermissionSettings() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.fromParts("package", packageName, null)
        )
        startActivity(intent)
    }

    private fun refreshPermissionUi() {
        val controller = featureController ?: return
        val featureName = getString(controller.spec.nameRes)
        binding.permissionScreenTitle.text = getString(R.string.permission_screen_title_with_feature, featureName)

        val availability = FeatureAvailabilityEvaluator.evaluate(this, controller)
        val blockResult = availability.blockResult
        val isBlocked = blockResult is FeatureBlockResult.Blocked

        if (blockResult is FeatureBlockResult.Blocked) {
            binding.blockedReasonCard.visibility = View.VISIBLE
            val warningSurface = ContextCompat.getColor(this, R.color.colorWarningSurface)
            val warningColor = ContextCompat.getColor(this, R.color.colorWarning)
            binding.blockedReasonCard.setCardBackgroundColor(warningSurface)
            binding.blockedReasonCard.strokeColor = warningColor
            binding.blockedReasonText.setText(blockResult.reasonRes)
            binding.blockedReasonText.setTextColor(warningColor)
        } else {
            binding.blockedReasonCard.visibility = View.GONE
        }

        val requirements = controller.spec.requiredPermissions
        val overlayRequired = requirements.contains(PermissionRequirement.Overlay)

        binding.overlayPermissionCard.visibility = if (overlayRequired) View.VISIBLE else View.GONE
        binding.notificationPermissionCard.visibility = View.GONE

        val overlayGranted = PermissionPolicy.isGranted(this, PermissionRequirement.Overlay)

        updatePermissionStatus(
            granted = overlayGranted,
            statusDot = binding.overlayStatusDot,
            statusText = binding.overlayStatusText,
            grantButton = binding.overlayGrantButton,
            required = overlayRequired,
            settingsFallback = false
        )

        val allPermissionsGranted = availability.missingPermissions.isEmpty()
        binding.continueButton.isEnabled = allPermissionsGranted && !isBlocked
        val continueBg = if (binding.continueButton.isEnabled) {
            ContextCompat.getColor(this, R.color.colorAccent)
        } else {
            ContextCompat.getColor(this, R.color.colorDisabledSurface)
        }
        val continueText = if (binding.continueButton.isEnabled) {
            ContextCompat.getColor(this, R.color.colorBackground)
        } else {
            ContextCompat.getColor(this, R.color.colorTextMuted)
        }
        binding.continueButton.backgroundTintList = ColorStateList.valueOf(continueBg)
        binding.continueButton.setTextColor(continueText)
        binding.continueButton.alpha = if (binding.continueButton.isEnabled) 1f else 0.7f
        binding.settingsHintText.visibility = View.GONE
    }

    private fun updatePermissionStatus(
        granted: Boolean,
        statusDot: View,
        statusText: TextView,
        grantButton: MaterialButton,
        required: Boolean,
        settingsFallback: Boolean
    ) {
        val accent = ContextCompat.getColor(this, R.color.colorAccent)
        val pending = ContextCompat.getColor(this, R.color.colorWarning)
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

        grantButton.isEnabled = required && !granted
        val buttonBg = if (grantButton.isEnabled) {
            ContextCompat.getColor(this, R.color.colorPrimary)
        } else {
            ContextCompat.getColor(this, R.color.colorDisabledSurface)
        }
        val buttonText = if (grantButton.isEnabled) {
            ContextCompat.getColor(this, R.color.colorTextPrimary)
        } else {
            ContextCompat.getColor(this, R.color.colorTextMuted)
        }
        grantButton.backgroundTintList = ColorStateList.valueOf(buttonBg)
        grantButton.setTextColor(buttonText)
        grantButton.setText(
            if (settingsFallback && grantButton.isEnabled) {
                R.string.permission_open_settings_button
            } else {
                R.string.permission_grant_button
            }
        )
        grantButton.alpha = if (grantButton.isEnabled) 1f else 0.65f
    }
}
