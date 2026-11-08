/*
 * Copyright 2026 Rajnish Kumar
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.rajnishkmehta.dhwanicontrol.core.block

import androidx.annotation.StringRes

sealed class FeatureBlockResult {
    object NotBlocked : FeatureBlockResult()
    data class Blocked(@StringRes val reasonRes: Int) : FeatureBlockResult()
}
