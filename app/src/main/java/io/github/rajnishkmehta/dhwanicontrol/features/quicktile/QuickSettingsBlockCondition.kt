package io.github.rajnishkmehta.dhwanicontrol.features.quicktile

import android.content.Context
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureBlockCondition
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureBlockResult

object QuickSettingsBlockCondition : FeatureBlockCondition {
    override fun evaluate(context: Context): FeatureBlockResult {
        return FeatureBlockResult.NotBlocked
    }
}
