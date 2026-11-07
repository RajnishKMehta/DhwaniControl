package io.github.rajnishkmehta.dhwanicontrol.features.assist

import android.app.role.RoleManager
import android.content.Context

object AssistantRoleChecker {

    fun isAvailable(context: Context): Boolean {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val roleManager = context.getSystemService(
                RoleManager::class.java
            )

            return roleManager?.isRoleAvailable(
                RoleManager.ROLE_ASSISTANT
            ) ?: false
        }
        return false
    }

    fun isDefaultAssistant(context: Context): Boolean {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val roleManager = context.getSystemService(
                RoleManager::class.java
            )

            return roleManager?.let {
                it.isRoleAvailable(RoleManager.ROLE_ASSISTANT) &&
                it.isRoleHeld(RoleManager.ROLE_ASSISTANT)
            } ?: false
        }
        return false
    }
}
