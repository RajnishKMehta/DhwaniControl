package io.github.rajnishkmehta.dhwanicontrol

object Constants {
    const val FEATURE_ID_QUICK_TILE = "quick_tile"
    const val FEATURE_ID_EDGE_SWIPE = "edge_swipe"

    const val EXTRA_FEATURE_ID = "extra_feature_id"

    const val LEGACY_PREF_SETUP_COMPLETE = "setup_complete"
    const val LEGACY_PREF_SELECTED_SIDE = "selected_side"
    const val LEGACY_PREF_SERVICE_ENABLED = "service_enabled"

    const val PREF_MIGRATION_COMPLETE = "migration.v0_2.complete"

    const val PREF_EDGE_CONFIGURED = "feature.edge.configured"
    const val PREF_EDGE_SELECTED_SIDE = "feature.edge.selected_side"
    const val PREF_EDGE_ENABLED = "feature.edge.enabled"
    const val PREF_EDGE_ZONE_PERCENT = "feature.edge.zone.percent"

    const val SIDE_LEFT = "LEFT"
    const val SIDE_RIGHT = "RIGHT"

    const val EDGE_ZONE_PERCENT_MIN = 0.02f
    const val EDGE_ZONE_PERCENT_DEFAULT = 0.05f
    const val EDGE_ZONE_PERCENT_MAX = 0.08f
    const val SETUP_SWIPES_NEEDED = 3

    const val QUICKBOOT_POWER_ON_ACTION = "android.intent.action.QUICKBOOT_POWERON"

    const val SWIPE_MIN_DISTANCE_DP = 40f
    /**
     * Empirically tuned swipe bias used by edge detection in
     * `EdgeSwipeAccessibilityService`: a swipe is treated as horizontal when
     * `horizontalDistance >= verticalDistance * SWIPE_HORIZONTAL_BIAS`.
     * The 1.15 factor reduces false positives from diagonal swipes.
     */
    const val SWIPE_HORIZONTAL_BIAS = 1.15f
    const val HAPTIC_FALLBACK_DURATION_MS = 30L
    const val SETUP_SUCCESS_DELAY_MS = 220L
}
