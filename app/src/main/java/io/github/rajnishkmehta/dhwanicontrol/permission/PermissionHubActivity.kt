package io.github.rajnishkmehta.dhwanicontrol.permission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import io.github.rajnishkmehta.dhwanicontrol.Constants
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureController
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureRegistry
import io.github.rajnishkmehta.dhwanicontrol.core.feature.PermissionRequirement
import io.github.rajnishkmehta.dhwanicontrol.core.permission.PermissionPolicy
import io.github.rajnishkmehta.dhwanicontrol.core.preferences.AppPreferences
import io.github.rajnishkmehta.dhwanicontrol.databinding.ActivityPermissionHubBinding

class PermissionHubActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPermissionHubBinding
    private var featureController: FeatureController? = null

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            AppPreferences.resetNotificationDenialCount(this)
        } else {
            AppPreferences.incrementNotificationDenialCount(this)
        }
        refreshPermissionUi()
    }

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

        binding.notificationGrantButton.setOnClickListener {
            requestNotificationPermission()
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

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            refreshPermissionUi()
            return
        }

        val alreadyGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (alreadyGranted) {
            AppPreferences.resetNotificationDenialCount(this)
            refreshPermissionUi()
            return
        }

        val denialCount = AppPreferences.getNotificationDenialCount(this)
        val shouldRedirectToSettings = denialCount >= Constants.NOTIFICATION_DENIAL_REDIRECT_THRESHOLD ||
            (denialCount > 0 && !shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS))

        if (shouldRedirectToSettings) {
            openAppDetailsSettings()
            return
        }

        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun openAppDetailsSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        startActivity(intent)
    }

    private fun refreshPermissionUi() {
        val controller = featureController ?: return
        val featureName = getString(controller.spec.titleRes)
        binding.permissionScreenTitle.text = getString(R.string.permission_screen_title_with_feature, featureName)

        val requirements = controller.spec.requiredPermissions
        val overlayRequired = requirements.contains(PermissionRequirement.Overlay)
        val notificationsRequired = requirements.contains(PermissionRequirement.Notifications) &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

        binding.overlayPermissionCard.visibility = if (overlayRequired) View.VISIBLE else View.GONE
        binding.notificationPermissionCard.visibility = if (notificationsRequired) View.VISIBLE else View.GONE

        val overlayGranted = PermissionPolicy.isGranted(this, PermissionRequirement.Overlay)
        val notificationsGranted = PermissionPolicy.isGranted(this, PermissionRequirement.Notifications)

        updatePermissionStatus(
            granted = overlayGranted,
            statusDot = binding.overlayStatusDot,
            statusText = binding.overlayStatusText,
            grantButton = binding.overlayGrantButton,
            required = overlayRequired,
            settingsFallback = false
        )

        val denialCount = AppPreferences.getNotificationDenialCount(this)
        val shouldUseSettingsFallback = denialCount >= Constants.NOTIFICATION_DENIAL_REDIRECT_THRESHOLD ||
            (denialCount > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                !shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS))

        updatePermissionStatus(
            granted = notificationsGranted,
            statusDot = binding.notificationStatusDot,
            statusText = binding.notificationStatusText,
            grantButton = binding.notificationGrantButton,
            required = notificationsRequired,
            settingsFallback = shouldUseSettingsFallback
        )

        val allPermissionsGranted = PermissionPolicy.missingPermissions(this, requirements).isEmpty()
        binding.continueButton.isEnabled = allPermissionsGranted
        binding.settingsHintText.visibility =
            if (notificationsRequired && shouldUseSettingsFallback && !notificationsGranted) {
                View.VISIBLE
            } else {
                View.GONE
            }
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

        grantButton.isEnabled = required && !granted
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
