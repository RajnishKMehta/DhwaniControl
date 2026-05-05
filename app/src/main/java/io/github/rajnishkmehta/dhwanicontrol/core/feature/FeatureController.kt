package io.github.rajnishkmehta.dhwanicontrol.core.feature

import android.app.Activity
import android.content.Context

interface FeatureController {
    val spec: FeatureSpec
    val blockCondition: FeatureBlockCondition

    fun isConfigured(context: Context): Boolean

    fun isEnabled(context: Context): Boolean

    fun setEnabled(context: Context, enabled: Boolean)

    fun openConfig(activity: Activity)

    fun synchronize(context: Context)
}
