package io.github.rajnishkmehta.dhwanicontrol.core.feature

data class FeatureAvailability(
    val blockResult: FeatureBlockResult,
    val missingPermissions: Set<PermissionRequirement>
) {
    val isBlocked: Boolean
        get() = blockResult.isBlocked
}
