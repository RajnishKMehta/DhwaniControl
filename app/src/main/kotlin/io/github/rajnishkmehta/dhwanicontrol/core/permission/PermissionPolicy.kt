package io.github.rajnishkmehta.dhwanicontrol.core.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import io.github.rajnishkmehta.dhwanicontrol.core.feature.PermissionRequirement

object PermissionPolicy {

    fun isGranted(context: Context, requirement: PermissionRequirement): Boolean {
        return when (requirement) {
            PermissionRequirement.Overlay -> Settings.canDrawOverlays(context)
            PermissionRequirement.Notifications -> isNotificationGranted(context)
        }
    }

    fun isNotificationGranted(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true
        }

        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun missingPermissions(
        context: Context,
        requirements: Set<PermissionRequirement>
    ): Set<PermissionRequirement> {
        return requirements.filterNot { isGranted(context, it) }.toSet()
    }
}
