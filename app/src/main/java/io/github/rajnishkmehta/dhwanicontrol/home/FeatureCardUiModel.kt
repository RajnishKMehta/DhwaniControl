package io.github.rajnishkmehta.dhwanicontrol.home

data class FeatureCardUiModel(
    val featureId: String,
    val title: String,
    val description: String,
    val status: String,
    val isBlocked: Boolean,
    val showToggle: Boolean,
    val toggleEnabled: Boolean,
    val toggledOn: Boolean,
    val configEnabled: Boolean
)
