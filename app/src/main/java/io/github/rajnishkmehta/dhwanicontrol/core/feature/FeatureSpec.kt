package io.github.rajnishkmehta.dhwanicontrol.core.feature

import androidx.annotation.StringRes

data class FeatureSpec(
    val id: String,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    val supportsToggle: Boolean,
    val requiredPermissions: Set<PermissionRequirement>,
    val order: Int
)
