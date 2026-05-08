package io.github.rajnishkmehta.dhwanicontrol.core.feature

import io.github.rajnishkmehta.dhwanicontrol.features.edge.EdgeSwipeFeatureController
import io.github.rajnishkmehta.dhwanicontrol.features.quicktile.QuickSettingsFeatureController

object FeatureRegistry {

    private val orderedControllers = listOf(
        QuickSettingsFeatureController,
        EdgeSwipeFeatureController
    ).sortedBy { it.spec.displayOrder }

    private val controllersById = orderedControllers.associateBy { it.spec.featureId }

    fun all(): List<FeatureController> {
        return orderedControllers
    }

    fun findById(featureId: String): FeatureController? {
        return controllersById[featureId]
    }
}
