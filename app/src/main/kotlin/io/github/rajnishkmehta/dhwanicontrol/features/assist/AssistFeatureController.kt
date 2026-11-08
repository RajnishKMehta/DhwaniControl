/*
 * Copyright 2026 Rajnish Kumar
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.rajnishkmehta.dhwanicontrol.features.assist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import io.github.rajnishkmehta.dhwanicontrol.Constants
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureController
import android.app.role.RoleManager
import io.github.rajnishkmehta.dhwanicontrol.core.block.FeatureBlockCondition
import io.github.rajnishkmehta.dhwanicontrol.core.block.FeatureBlockResult
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureSpec

object AssistFeatureController : FeatureController {

    override val spec = FeatureSpec(
        featureId = Constants.FEATURE_ID_ASSIST,
        nameRes = R.string.feature_assist_title,
        summaryRes = R.string.feature_assist_description,
        supportsToggle = true,
        supportsConfig = true,
        configActionRes = R.string.home_need_help,
        requiredPermissions = emptySet(),
        displayOrder = 3
    )


    override val blockCondition = FeatureBlockCondition { context ->
        if (AssistantRoleChecker.isAvailable(context)) {
            FeatureBlockResult.NotBlocked
        } else {
            FeatureBlockResult.Blocked(R.string.assist_role_unavailable)
        }
    }

    override fun isConfigured(context: Context): Boolean {
        return true
    }

    override fun isEnabled(context: Context): Boolean {
        return AssistantRoleChecker.isDefaultAssistant(context)
    }

    override fun setEnabled(context: Context, enabled: Boolean) {
        val intent = if (enabled && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val roleManager = context.getSystemService(RoleManager::class.java)
            roleManager?.createRequestRoleIntent(RoleManager.ROLE_ASSISTANT)
                ?: Intent(Settings.ACTION_VOICE_INPUT_SETTINGS)
        } else {
            Intent(Settings.ACTION_VOICE_INPUT_SETTINGS)
        }

        if (intent.action == Settings.ACTION_VOICE_INPUT_SETTINGS) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        runCatching {
            if (context is Activity && intent.action != Settings.ACTION_VOICE_INPUT_SETTINGS) {
                context.startActivityForResult(intent, 0)
            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }.onFailure {
            Toast.makeText(context, R.string.feature_action_failed, Toast.LENGTH_SHORT).show()
        }
    }

    override fun openConfig(activity: Activity) {
        val url = activity.getString(R.string.assist_help_url)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        runCatching {
            activity.startActivity(intent)
        }.onFailure {
            Toast.makeText(activity, R.string.app_info_open_link_failed, Toast.LENGTH_SHORT).show()
        }
    }

    override fun synchronize(context: Context) {
        // Handled automatically via AssistantRoleChecker when queried
    }
}
