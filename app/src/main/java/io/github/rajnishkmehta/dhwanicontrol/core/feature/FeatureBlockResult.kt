package io.github.rajnishkmehta.dhwanicontrol.core.feature

import android.content.Context
import androidx.annotation.StringRes

data class FeatureBlockResult(
    val isBlocked: Boolean,
    @StringRes val reasonRes: Int? = null
) {
    fun resolveReason(context: Context): String? {
        return reasonRes?.let(context::getString)
    }

    companion object {
        val NotBlocked = FeatureBlockResult(isBlocked = false)

        fun blocked(@StringRes reasonRes: Int): FeatureBlockResult {
            return FeatureBlockResult(
                isBlocked = true,
                reasonRes = reasonRes
            )
        }
    }
}
