package io.github.rajnishkmehta.dhwanicontrol

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureAvailabilityEvaluator
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
            FeatureAvailabilityEvaluator.enforce(context, controller)
            runCatching {
                controller.synchronize(context)
            }
        }
    }
}
