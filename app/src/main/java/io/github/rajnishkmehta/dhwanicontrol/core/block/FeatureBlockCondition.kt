package io.github.rajnishkmehta.dhwanicontrol.core.block

import android.content.Context

fun interface FeatureBlockCondition {
    fun evaluate(context: Context): FeatureBlockResult
}
