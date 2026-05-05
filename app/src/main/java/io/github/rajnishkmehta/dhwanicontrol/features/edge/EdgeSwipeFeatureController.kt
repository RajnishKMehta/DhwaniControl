package io.github.rajnishkmehta.dhwanicontrol.features.edge

import android.app.Activity
import android.content.Context
import android.content.Intent
import io.github.rajnishkmehta.dhwanicontrol.Constants
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureController
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureSpec
import io.github.rajnishkmehta.dhwanicontrol.core.feature.PermissionRequirement
import io.github.rajnishkmehta.dhwanicontrol.core.preferences.AppPreferences

object EdgeSwipeFeatureController : FeatureController {

    override val spec = FeatureSpec(
        id = Constants.FEATURE_ID_EDGE_SWIPE,
        titleRes = R.string.feature_edge_title,
        descriptionRes = R.string.feature_edge_description,
        supportsToggle = true,
        requiredPermissions = setOf(PermissionRequirement.Accessibility),
        order = 2
    )
    override val blockCondition = EdgeSwipeBlockCondition

    override fun isConfigured(context: Context): Boolean {
        return AppPreferences.isEdgeConfigured(context)
    }

    override fun isEnabled(context: Context): Boolean {
        return AppPreferences.isEdgeEnabled(context)
    }

    override fun setEnabled(context: Context, enabled: Boolean) {
        AppPreferences.setEdgeEnabled(context, enabled)
        EdgeOverlayRuntime.sync(context)
    }

    override fun openConfig(activity: Activity) {
        val target = if (AppPreferences.isEdgeConfigured(activity)) {
            EdgeSwipeSideSelectorActivity::class.java
        } else {
            EdgeSwipeSetupActivity::class.java
        }
        activity.startActivity(Intent(activity, target))
    }

    override fun synchronize(context: Context) {
        EdgeOverlayRuntime.sync(context)
    }
}
