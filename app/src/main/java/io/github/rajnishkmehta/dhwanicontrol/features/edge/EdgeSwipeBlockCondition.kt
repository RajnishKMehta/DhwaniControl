package io.github.rajnishkmehta.dhwanicontrol.features.edge

import android.content.Context
import android.os.Build
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureBlockCondition
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureBlockResult

object EdgeSwipeBlockCondition : FeatureBlockCondition {

    override fun evaluate(context: Context): FeatureBlockResult {
        if (Build.VERSION.SDK_INT in Build.VERSION_CODES.Q..Build.VERSION_CODES.R) {
            return FeatureBlockResult.blocked(R.string.feature_block_edge_android_10_11)
        }

        val navigationMode = SystemNavigationModeProvider.getMode(context)
        if (navigationMode == 2) {
            return FeatureBlockResult.blocked(R.string.feature_block_edge_gesture_navigation)
        }

        return FeatureBlockResult.NotBlocked
    }
}
