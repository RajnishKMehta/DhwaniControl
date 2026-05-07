package io.github.rajnishkmehta.dhwanicontrol.features.edge

import android.content.Context
import android.provider.Settings
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.core.block.FeatureBlockCondition
import io.github.rajnishkmehta.dhwanicontrol.core.block.FeatureBlockResult

object EdgeSwipeBlockCondition : FeatureBlockCondition {

    private const val NAVIGATION_MODE_KEY = "navigation_mode"
    private const val GESTURE_NAVIGATION_VALUE = 2

    override fun evaluate(context: Context): FeatureBlockResult {
        val navigationMode = Settings.Secure.getInt(
            context.contentResolver,
            NAVIGATION_MODE_KEY,
            -1
        )
        return if (navigationMode == GESTURE_NAVIGATION_VALUE) {
            FeatureBlockResult.Blocked(R.string.feature_blocked_gesture_nav)
        } else {
            FeatureBlockResult.NotBlocked
        }
    }
}
