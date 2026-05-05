package io.github.rajnishkmehta.dhwanicontrol.core.feature

import android.content.Context

fun interface FeatureBlockCondition {
    fun evaluate(context: Context): FeatureBlockResult
}
