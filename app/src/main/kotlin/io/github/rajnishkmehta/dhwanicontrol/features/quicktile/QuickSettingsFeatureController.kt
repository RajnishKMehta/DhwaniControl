package io.github.rajnishkmehta.dhwanicontrol.features.quicktile

import android.app.Activity
import android.content.Context
import android.content.Intent
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
        activity.startActivity(Intent(activity, QuickSettingsGuideActivity::class.java))
    }

    override fun synchronize(context: Context) {
        // No background state to synchronize.
    }
}
