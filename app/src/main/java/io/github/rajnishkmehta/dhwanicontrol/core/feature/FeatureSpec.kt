package io.github.rajnishkmehta.dhwanicontrol.core.feature

import androidx.annotation.IntRange
import androidx.annotation.StringRes

/**
 * Static metadata used by [FeatureRegistry] to render and resolve a feature.
 *
 * [displayOrder] must be a positive number (1..n). Each registered feature
 * must also use a unique displayOrder so Home ordering remains deterministic.
 */
data class FeatureSpec(
    val featureId: String,
    @StringRes val nameRes: Int,
    @StringRes val summaryRes: Int,
    val supportsToggle: Boolean,
    val supportsConfig: Boolean = true,
    val requiredPermissions: Set<PermissionRequirement>,
    @IntRange(from = 1)
    val displayOrder: Int
)
