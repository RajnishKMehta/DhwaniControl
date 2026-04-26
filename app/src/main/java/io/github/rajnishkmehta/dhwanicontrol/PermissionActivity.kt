package io.github.rajnishkmehta.dhwanicontrol

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
import androidx.preference.PreferenceManager
import com.google.android.material.button.MaterialButton
import io.github.rajnishkmehta.dhwanicontrol.databinding.ActivityPermissionBinding

class PermissionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPermissionBinding
    private var hasRouted = false

    private val preferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        refreshPermissionUi()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.overlayGrantButton.setOnClickListener {
            openOverlayPermissionSettings()
        }

        binding.notificationGrantButton.setOnClickListener {
            requestNotificationPermission()
        }

        binding.continueButton.setOnClickListener {
            routeToNextScreen()
        }

        refreshPermissionUi()
    }

    override fun onResume() {
        super.onResume()
        val allPermissionsGranted = refreshPermissionUi()
        if (allPermissionsGranted) {
            routeToNextScreen()
        }
    }

    private fun openOverlayPermissionSettings() {
        val permissionIntent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.fromParts(Constants.PACKAGE_SCHEME, packageName, null)
        )
        startActivity(permissionIntent)
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
            refreshPermissionUi()
            return
        }

        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun refreshPermissionUi(): Boolean {
        val overlayGranted = Settings.canDrawOverlays(this)
        val notificationsRequired = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        val notificationsGranted = !notificationsRequired || ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        updatePermissionStatus(
            granted = overlayGranted,
            statusDot = binding.overlayStatusDot,
            statusText = binding.overlayStatusText,
            grantButton = binding.overlayGrantButton,
            required = true
        )

        updatePermissionStatus(
            granted = notificationsGranted,
            statusDot = binding.notificationStatusDot,
            statusText = binding.notificationStatusText,
            grantButton = binding.notificationGrantButton,
            required = notificationsRequired
        )

        val allPermissionsGranted = overlayGranted && notificationsGranted
        binding.continueButton.isEnabled = allPermissionsGranted

        return allPermissionsGranted
    }

    private fun updatePermissionStatus(
        granted: Boolean,
        statusDot: View,
        statusText: TextView,
        grantButton: MaterialButton,
        required: Boolean
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
        grantButton.alpha = if (grantButton.isEnabled) 1f else 0.65f
    }

    private fun routeToNextScreen() {
        if (hasRouted) {
            return
        }

        val setupComplete = preferences.getBoolean(Constants.PREF_SETUP_COMPLETE, false)
        val target = if (setupComplete) MainActivity::class.java else SetupActivity::class.java

        hasRouted = true
        startActivity(Intent(this, target))
        finish()
    }
}
