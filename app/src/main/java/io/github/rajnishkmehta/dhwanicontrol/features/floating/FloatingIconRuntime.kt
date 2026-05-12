package io.github.rajnishkmehta.dhwanicontrol.features.floating

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import io.github.rajnishkmehta.dhwanicontrol.core.block.FeatureBlockResult
import io.github.rajnishkmehta.dhwanicontrol.core.permission.PermissionPolicy
import io.github.rajnishkmehta.dhwanicontrol.core.preferences.AppPreferences

object FloatingIconRuntime {

    fun sync(context: Context) {
        if (!canRun(context)) {
            stop(context)
            return
        }

        start(context)
    }

    private fun canRun(context: Context): Boolean {
        val blockResult = FloatingIconFeatureController.blockCondition.evaluate(context)
        if (blockResult is FeatureBlockResult.Blocked) {
            return false
        }

        if (!AppPreferences.isFloatingEnabled(context)) {
            return false
        }

        val missingPermissions = PermissionPolicy.missingPermissions(
            context,
            FloatingIconFeatureController.spec.requiredPermissions
        )
        return missingPermissions.isEmpty()
    }

    private fun start(context: Context) {
        runCatching {
            ContextCompat.startForegroundService(
                context,
                Intent(context, FloatingIconService::class.java)
            )
        }
    }

    fun stop(context: Context) {
        runCatching {
            context.stopService(Intent(context, FloatingIconService::class.java))
        }
    }
}
