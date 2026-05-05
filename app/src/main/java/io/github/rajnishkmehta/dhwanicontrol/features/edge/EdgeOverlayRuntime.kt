package io.github.rajnishkmehta.dhwanicontrol.features.edge

import android.content.Context
import android.content.Intent
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureAvailabilityEvaluator
import io.github.rajnishkmehta.dhwanicontrol.core.permission.PermissionPolicy
import io.github.rajnishkmehta.dhwanicontrol.core.preferences.AppPreferences

object EdgeOverlayRuntime {
    private const val LEGACY_OVERLAY_SERVICE_CLASS_NAME =
        "io.github.rajnishkmehta.dhwanicontrol.features.edge.VolumeOverlayService"

    fun sync(context: Context) {
        stopLegacyOverlayService(context)

        if (!canRun(context)) {
            stopAccessibilityHandling()
            return
        }

        startAccessibilityHandling()
    }

    private fun canRun(context: Context): Boolean {
        if (!AppPreferences.isEdgeConfigured(context)) {
            return false
        }

        if (!AppPreferences.isEdgeEnabled(context)) {
            return false
        }

        val isBlocked = FeatureAvailabilityEvaluator
            .evaluate(context, EdgeSwipeFeatureController)
            .isBlocked
        if (isBlocked) {
            return false
        }

        return PermissionPolicy.missingPermissions(
            context,
            EdgeSwipeFeatureController.spec.requiredPermissions
        ).isEmpty()
    }

    private fun startAccessibilityHandling() {
        EdgeSwipeAccessibilityService.refreshConfiguration()
    }

    private fun stopAccessibilityHandling() {
        EdgeSwipeAccessibilityService.pauseEdgeDetection()
    }

    private fun stopLegacyOverlayService(context: Context) {
        runCatching {
            val intent = Intent().setClassName(context.packageName, LEGACY_OVERLAY_SERVICE_CLASS_NAME)
            context.stopService(intent)
        }
    }
}
