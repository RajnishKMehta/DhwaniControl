package io.github.rajnishkmehta.dhwanicontrol.core.feature

import android.content.Context
import androidx.annotation.StringRes

sealed class FeatureBlockResult {
    object NotBlocked : FeatureBlockResult()

    data class Blocked(@StringRes val reasonRes: Int) : FeatureBlockResult() {
        fun resolveReason(context: Context): String {
            return context.getString(reasonRes)
        }
    }
}
