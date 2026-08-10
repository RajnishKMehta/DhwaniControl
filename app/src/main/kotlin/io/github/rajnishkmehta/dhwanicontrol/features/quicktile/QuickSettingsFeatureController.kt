package io.github.rajnishkmehta.dhwanicontrol.features.quicktile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import io.github.rajnishkmehta.dhwanicontrol.Constants
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureController
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureSpec

object QuickSettingsFeatureController : FeatureController {

    override val spec = FeatureSpec(
        featureId = Constants.FEATURE_ID_QUICK_TILE,
        nameRes = R.string.feature_quick_tile_title,
        summaryRes = R.string.feature_quick_tile_description,
        supportsToggle = false,
        supportsConfig = true,
        configActionRes = R.string.home_need_help,
        requiredPermissions = emptySet(),
        displayOrder = 1
    )

    override fun isConfigured(context: Context): Boolean {
        return true
    }

    override fun isEnabled(context: Context): Boolean {
        return true
    }

    override fun setEnabled(context: Context, enabled: Boolean) {
        // This feature intentionally has no on/off state.
    }

    override fun openConfig(activity: Activity) {
        val url = activity.getString(R.string.quick_tile_help_url)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        runCatching {
            activity.startActivity(intent)
        }.onFailure {
            Toast.makeText(activity, R.string.app_info_open_link_failed, Toast.LENGTH_SHORT).show()
        }
    }

    override fun synchronize(context: Context) {
        // No background state to synchronize.
    }
}
