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

    const val PREF_NOTIFICATION_DENIAL_COUNT = "permission.notifications.denial_count"

    const val SIDE_LEFT = "LEFT"
    const val SIDE_RIGHT = "RIGHT"

    const val EDGE_ZONE_PERCENT = 0.05f
    const val SETUP_SWIPES_NEEDED = 3

    const val ACTION_STOP_EDGE_SERVICE =
        "io.github.rajnishkmehta.dhwanicontrol.action.STOP_EDGE_SERVICE"
    const val QUICKBOOT_POWER_ON_ACTION = "android.intent.action.QUICKBOOT_POWERON"

    const val NOTIFICATION_CHANNEL_ID = "dhwanicontrol_edge_service_channel"
    const val NOTIFICATION_ID = 1201
    const val STOP_ACTION_REQUEST_CODE = 19

    const val SWIPE_MIN_DISTANCE_DP = 40f
    const val FLING_MIN_VELOCITY_PX = 700f
    const val SAFE_NAVIGATION_FALLBACK_DP = 24
    const val HAPTIC_FALLBACK_DURATION_MS = 30L
    const val SETUP_SUCCESS_DELAY_MS = 220L

    const val NOTIFICATION_DENIAL_REDIRECT_THRESHOLD = 2
}
