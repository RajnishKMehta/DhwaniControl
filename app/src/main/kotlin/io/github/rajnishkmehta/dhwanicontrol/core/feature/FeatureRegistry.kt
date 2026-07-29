package io.github.rajnishkmehta.dhwanicontrol.core.feature

import io.github.rajnishkmehta.dhwanicontrol.features.edge.EdgeSwipeFeatureController
import io.github.rajnishkmehta.dhwanicontrol.features.floating.FloatingButtonFeatureController
import io.github.rajnishkmehta.dhwanicontrol.features.quicktile.QuickSettingsFeatureController

object FeatureRegistry {

    private val orderedControllers = listOf(
        QuickSettingsFeatureController,
        FloatingButtonFeatureController,
        EdgeSwipeFeatureController
    ).sortedBy { it.spec.displayOrder }

    init {
        validatePositiveDisplayOrders()
        validateUniqueFeatureIds()
        validateUniqueDisplayOrders()
    }

    private val controllersById = orderedControllers.associateBy { it.spec.featureId }

    fun all(): List<FeatureController> {
        return orderedControllers
    }

    fun findById(featureId: String): FeatureController? {
        return controllersById[featureId]
    }

    private fun validateUniqueFeatureIds() {
        val duplicates = orderedControllers
            .groupBy { it.spec.featureId }
            .filterValues { it.size > 1 }

        if (duplicates.isEmpty()) {
            return
        }

        val details = duplicates.entries.joinToString("; ") { (featureId, controllers) ->
            val names = controllers.joinToString(", ") { it::class.java.simpleName }
            "featureId=$featureId controllers=[$names]"
        }
        throw IllegalStateException("Duplicate featureId values in FeatureRegistry: $details")
    }

    private fun validatePositiveDisplayOrders() {
        val invalid = orderedControllers.filter { it.spec.displayOrder <= 0 }
        if (invalid.isEmpty()) {
            return
        }

        val details = invalid.joinToString(", ") {
            "${it::class.java.simpleName}(displayOrder=${it.spec.displayOrder})"
        }
        throw IllegalStateException("FeatureRegistry displayOrder must be positive. Invalid controllers: $details")
    }

    private fun validateUniqueDisplayOrders() {
        val duplicates = orderedControllers
            .groupBy { it.spec.displayOrder }
            .filterValues { it.size > 1 }

        if (duplicates.isEmpty()) {
            return
        }

        val details = duplicates.entries.joinToString("; ") { (displayOrder, controllers) ->
            val names = controllers.joinToString(", ") { it::class.java.simpleName }
            "displayOrder=$displayOrder controllers=[$names]"
        }
        throw IllegalStateException("Duplicate displayOrder values in FeatureRegistry: $details")
    }
}
