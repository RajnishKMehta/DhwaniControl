package io.github.rajnishkmehta.dhwanicontrol.core.permission

import android.content.Context
import android.provider.Settings
import io.github.rajnishkmehta.dhwanicontrol.core.feature.PermissionRequirement

object PermissionPolicy {

    fun isGranted(context: Context, requirement: PermissionRequirement): Boolean {
        return when (requirement) {
            PermissionRequirement.Overlay -> Settings.canDrawOverlays(context)
        }
    }

    fun missingPermissions(
        context: Context,
        requirements: Set<PermissionRequirement>
    ): Set<PermissionRequirement> {
        return requirements.filterNot { isGranted(context, it) }.toSet()
    }
}
