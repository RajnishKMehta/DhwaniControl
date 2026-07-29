package io.github.rajnishkmehta.dhwanicontrol.features.floating

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import io.github.rajnishkmehta.dhwanicontrol.core.block.FeatureBlockResult
import io.github.rajnishkmehta.dhwanicontrol.core.permission.PermissionPolicy
import io.github.rajnishkmehta.dhwanicontrol.core.preferences.AppPreferences

object FloatingButtonRuntime {

    fun sync(context: Context) {
        val enabledByPref = AppPreferences.isFloatingEnabled(context)
        if (!enabledByPref) {
            stop(context)
            return
        }

        // Feature is intended to be ON. Check if it CAN run.
        val missingPermissions = PermissionPolicy.missingPermissions(
            context,
            FloatingButtonFeatureController.spec.requiredPermissions
        )

        if (missingPermissions.isNotEmpty()) {
            // Permissions revoked, turn off the feature preference
            AppPreferences.setFloatingEnabled(context, false)
            stop(context)
            return
        }

        val blockResult = FloatingButtonFeatureController.blockCondition.evaluate(context)
        if (blockResult is FeatureBlockResult.Blocked) {
            stop(context)
            return
        }

        start(context)
    }

    private fun start(context: Context) {
        runCatching {
            ContextCompat.startForegroundService(
                context,
                Intent(context, FloatingButtonService::class.java)
            )
        }
    }

    fun stop(context: Context) {
        runCatching {
            context.stopService(Intent(context, FloatingButtonService::class.java))
        }
    }
}
