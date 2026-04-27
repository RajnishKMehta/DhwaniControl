package io.github.rajnishkmehta.dhwanicontrol

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureRegistry
import io.github.rajnishkmehta.dhwanicontrol.core.preferences.AppPreferences

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action != Intent.ACTION_BOOT_COMPLETED && action != Constants.QUICKBOOT_POWER_ON_ACTION) {
            return
        }

        AppPreferences.ensureMigration(context)

        FeatureRegistry.all().forEach { controller ->
            runCatching {
                controller.synchronize(context)
            }.onFailure { throwable ->
                Log.e(TAG, "Failed to sync feature ${controller.spec.id} after boot", throwable)
            }
        }
    }

    private companion object {
        const val TAG = "BootReceiver"
    }
}
