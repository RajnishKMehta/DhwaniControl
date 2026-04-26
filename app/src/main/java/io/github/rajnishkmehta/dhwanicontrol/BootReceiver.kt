package io.github.rajnishkmehta.dhwanicontrol

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action != Intent.ACTION_BOOT_COMPLETED && action != Constants.QUICKBOOT_POWER_ON_ACTION) {
            return
        }

        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val setupComplete = preferences.getBoolean(Constants.PREF_SETUP_COMPLETE, false)
        val serviceEnabled = preferences.getBoolean(Constants.PREF_SERVICE_ENABLED, true)
        val canDrawOverlays = Settings.canDrawOverlays(context)

        if (setupComplete && serviceEnabled && canDrawOverlays && !VolumeOverlayService.isRunning) {
            ContextCompat.startForegroundService(
                context,
                Intent(context, VolumeOverlayService::class.java)
            )
        }
    }
}
