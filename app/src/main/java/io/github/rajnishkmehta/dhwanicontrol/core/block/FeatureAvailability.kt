package io.github.rajnishkmehta.dhwanicontrol.core.block

import io.github.rajnishkmehta.dhwanicontrol.core.feature.PermissionRequirement

data class FeatureAvailability(
    val blockResult: FeatureBlockResult,
    val missingPermissions: Set<PermissionRequirement>
) {
    val isAvailable: Boolean
        get() = blockResult is FeatureBlockResult.NotBlocked && missingPermissions.isEmpty()
}
