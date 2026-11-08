/*
 * Copyright 2026 Rajnish Kumar
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.rajnishkmehta.dhwanicontrol.home

data class FeatureCardUiModel(
    val featureId: String,
    val title: String,
    val description: String,
    val status: String,
    val statusIsWarning: Boolean,
    val showToggle: Boolean,
    val toggleEnabled: Boolean,
    val toggledOn: Boolean,
    val showConfig: Boolean,
    val configEnabled: Boolean,
    val configButtonText: String
)
