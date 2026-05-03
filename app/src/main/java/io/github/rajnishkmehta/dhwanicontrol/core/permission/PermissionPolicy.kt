package io.github.rajnishkmehta.dhwanicontrol.core.permission

import android.content.ComponentName
import android.content.Context
import android.provider.Settings
import android.text.TextUtils
import io.github.rajnishkmehta.dhwanicontrol.core.feature.PermissionRequirement
import io.github.rajnishkmehta.dhwanicontrol.features.edge.EdgeSwipeAccessibilityService

object PermissionPolicy {

    fun isGranted(context: Context, requirement: PermissionRequirement): Boolean {
        return when (requirement) {
            PermissionRequirement.Accessibility -> isAccessibilityGranted(context)
        }
    }

    fun isAccessibilityGranted(context: Context): Boolean {
        val accessibilityEnabled = runCatching {
            Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED,
                0
            )
        }.getOrDefault(0) == 1
        if (!accessibilityEnabled) {
            return false
        }

        val expectedComponent = ComponentName(
            context,
            EdgeSwipeAccessibilityService::class.java
        ).flattenToString()

        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false

        val splitter = TextUtils.SimpleStringSplitter(':').apply {
            setString(enabledServices)
        }
        while (splitter.hasNext()) {
            if (splitter.next().equals(expectedComponent, ignoreCase = true)) {
                return true
            }
        }

        return false
    }

    fun missingPermissions(
        context: Context,
        requirements: Set<PermissionRequirement>
    ): Set<PermissionRequirement> {
        return requirements.filterNot { isGranted(context, it) }.toSet()
    }
}
