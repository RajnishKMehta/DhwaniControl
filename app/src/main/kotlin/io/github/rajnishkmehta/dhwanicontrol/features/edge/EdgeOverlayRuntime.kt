package io.github.rajnishkmehta.dhwanicontrol.features.edge

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import io.github.rajnishkmehta.dhwanicontrol.core.block.FeatureBlockResult
import io.github.rajnishkmehta.dhwanicontrol.core.permission.PermissionPolicy
import io.github.rajnishkmehta.dhwanicontrol.core.preferences.AppPreferences

object EdgeOverlayRuntime {

    fun sync(context: Context) {
        val enabledByPref = AppPreferences.isEdgeEnabled(context)
        if (!enabledByPref) {
            stop(context)
            return
        }

        // Feature is intended to be ON. Check if it CAN run.
        val missingPermissions = PermissionPolicy.missingPermissions(
            context,
            EdgeSwipeFeatureController.spec.requiredPermissions
        )

        if (missingPermissions.isNotEmpty()) {
            // Permissions revoked, turn off the feature preference
            AppPreferences.setEdgeEnabled(context, false)
            stop(context)
            return
        }

        if (!AppPreferences.isEdgeConfigured(context)) {
            AppPreferences.setEdgeEnabled(context, false)
            stop(context)
            return
        }

        val blockResult = EdgeSwipeFeatureController.blockCondition.evaluate(context)
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
                Intent(context, VolumeOverlayService::class.java)
            )
        }
    }

    fun stop(context: Context) {
        runCatching {
            context.stopService(Intent(context, VolumeOverlayService::class.java))
        }
    }
}
