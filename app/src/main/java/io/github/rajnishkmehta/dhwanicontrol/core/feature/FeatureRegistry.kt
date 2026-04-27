package io.github.rajnishkmehta.dhwanicontrol.core.feature

import io.github.rajnishkmehta.dhwanicontrol.features.edge.EdgeSwipeFeatureController
import io.github.rajnishkmehta.dhwanicontrol.features.quicktile.QuickSettingsFeatureController

object FeatureRegistry {

    private val controllers = listOf(
        QuickSettingsFeatureController,
        EdgeSwipeFeatureController
    ).sortedBy { it.spec.order }

    fun all(): List<FeatureController> {
        return controllers
    }

    fun findById(featureId: String): FeatureController? {
        return controllers.firstOrNull { it.spec.id == featureId }
    }
}
