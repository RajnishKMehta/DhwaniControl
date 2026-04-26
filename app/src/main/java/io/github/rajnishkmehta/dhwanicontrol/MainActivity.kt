package io.github.rajnishkmehta.dhwanicontrol

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import io.github.rajnishkmehta.dhwanicontrol.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val preferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    private var syncingSwitch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.serviceSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (syncingSwitch) {
                return@setOnCheckedChangeListener
            }

            preferences.edit()
                .putBoolean(Constants.PREF_SERVICE_ENABLED, isChecked)
                .apply()

            if (isChecked) {
                startOverlayServiceIfAllowed()
            } else {
                stopOverlayService()
            }
        }

        binding.changeEdgeButton.setOnClickListener {
            preferences.edit()
                .putBoolean(Constants.PREF_SETUP_COMPLETE, false)
                .apply()
            startActivity(Intent(this, SetupActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        syncUiFromPreferences()
        attemptServiceStartFromResume()
    }

    private fun syncUiFromPreferences() {
        val serviceEnabled = preferences.getBoolean(Constants.PREF_SERVICE_ENABLED, true)
        syncingSwitch = true
        binding.serviceSwitch.isChecked = serviceEnabled
        syncingSwitch = false

        val selectedSide = preferences.getString(
            Constants.PREF_SELECTED_SIDE,
            Constants.SIDE_RIGHT
        ) ?: Constants.SIDE_RIGHT

        val isLeftSide = selectedSide == Constants.SIDE_LEFT
        binding.activeEdgeText.setText(
            if (isLeftSide) R.string.main_edge_left else R.string.main_edge_right
        )
        binding.usageHintText.setText(
            if (isLeftSide) R.string.main_usage_hint_left else R.string.main_usage_hint_right
        )

        binding.overlayWarningText.isVisible = !Settings.canDrawOverlays(this)
    }

    private fun attemptServiceStartFromResume() {
        val serviceEnabled = preferences.getBoolean(Constants.PREF_SERVICE_ENABLED, true)
        if (!serviceEnabled) {
            stopOverlayService()
            return
        }

        startOverlayServiceIfAllowed()
    }

    private fun startOverlayServiceIfAllowed() {
        if (!Settings.canDrawOverlays(this)) {
            binding.overlayWarningText.isVisible = true
            return
        }

        if (!VolumeOverlayService.isRunning) {
            ContextCompat.startForegroundService(this, Intent(this, VolumeOverlayService::class.java))
        }
    }

    private fun stopOverlayService() {
        stopService(Intent(this, VolumeOverlayService::class.java))
    }
}
