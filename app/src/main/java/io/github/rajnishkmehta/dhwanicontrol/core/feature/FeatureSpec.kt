package io.github.rajnishkmehta.dhwanicontrol.core.feature

import androidx.annotation.StringRes

data class FeatureSpec(
    val featureId: String,
    @StringRes val nameRes: Int,
    @StringRes val summaryRes: Int,
    val supportsToggle: Boolean,
    val requiredPermissions: Set<PermissionRequirement>,
    val displayOrder: Int
)
