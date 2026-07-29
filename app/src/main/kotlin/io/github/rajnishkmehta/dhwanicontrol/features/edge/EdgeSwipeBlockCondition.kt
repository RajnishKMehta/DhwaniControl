package io.github.rajnishkmehta.dhwanicontrol.features.edge

import android.content.Context
import android.provider.Settings
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.core.block.FeatureBlockCondition
import io.github.rajnishkmehta.dhwanicontrol.core.block.FeatureBlockResult

object EdgeSwipeBlockCondition : FeatureBlockCondition {

    // NAVIGATION_MODE_KEY ("navigation_mode") is a non-public Settings.Secure
    // key commonly used on Android 10+ (API 29+) to detect gesture navigation.
    // Some OEMs/custom ROMs may omit or alter this value, so evaluate()
    // intentionally falls back to OEM-specific keys before treating -1 as unknown.
    private const val NAVIGATION_MODE_KEY = "navigation_mode"

    // Xiaomi / HyperOS fullscreen gesture flag.
    private const val FORCE_FSG_NAV_BAR_KEY = "force_fsg_nav_bar"

    // Older OnePlus gesture navigation flag.
    private const val OP_GESTURE_BUTTON_SIDE_ENABLED_KEY =
        "op_gesture_button_side_enabled"

    private const val GESTURE_NAVIGATION_VALUE = 2

    override fun evaluate(context: Context): FeatureBlockResult {
        val resolver = context.contentResolver

        var navigationMode = Settings.Secure.getInt(
            resolver,
            NAVIGATION_MODE_KEY,
            -1
        )

        // Fallback for Xiaomi / HyperOS ROMs.
        if (navigationMode == -1) {
            navigationMode = Settings.Global.getInt(
                resolver,
                FORCE_FSG_NAV_BAR_KEY,
                -1
            )
        }

        // Fallback for older OnePlus ROMs.
        if (navigationMode == -1) {
            navigationMode = Settings.Secure.getInt(
                resolver,
                OP_GESTURE_BUTTON_SIDE_ENABLED_KEY,
                -1
            )
        }

        return if (
            navigationMode == GESTURE_NAVIGATION_VALUE ||
            navigationMode == 1
        ) {
            FeatureBlockResult.Blocked(R.string.feature_blocked_gesture_nav)
        } else {
            FeatureBlockResult.NotBlocked
        }
    }
}
