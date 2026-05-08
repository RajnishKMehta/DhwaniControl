package io.github.rajnishkmehta.dhwanicontrol.core.block

import androidx.annotation.StringRes

sealed class FeatureBlockResult {
    object NotBlocked : FeatureBlockResult()
    data class Blocked(@StringRes val reasonRes: Int) : FeatureBlockResult()
}
