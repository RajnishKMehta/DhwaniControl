/*
 * Copyright 2026 Rajnish Kumar
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.rajnishkmehta.dhwanicontrol.core.block

import android.content.Context

fun interface FeatureBlockCondition {
    fun evaluate(context: Context): FeatureBlockResult
}
