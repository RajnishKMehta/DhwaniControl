package io.github.rajnishkmehta.dhwanicontrol.features.edge

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import io.github.rajnishkmehta.dhwanicontrol.core.permission.PermissionPolicy
import io.github.rajnishkmehta.dhwanicontrol.core.preferences.AppPreferences

object EdgeOverlayRuntime {

    fun sync(context: Context) {
        if (!canRun(context)) {
            stop(context)
            return
        }

        start(context)
    }

    private fun canRun(context: Context): Boolean {
        if (!AppPreferences.isEdgeConfigured(context)) {
            return false
        }

        if (!AppPreferences.isEdgeEnabled(context)) {
            return false
        }

        val missingPermissions = PermissionPolicy.missingPermissions(
            context,
            EdgeSwipeFeatureController.spec.requiredPermissions
        )
        return missingPermissions.isEmpty()
    }

    private fun start(context: Context) {
        if (VolumeOverlayService.isRunning) {
            return
        }

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
