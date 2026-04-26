package io.github.rajnishkmehta.dhwanicontrol

object Constants {
    const val PREF_SETUP_COMPLETE = "setup_complete"
    const val PREF_SELECTED_SIDE = "selected_side"
    const val PREF_SERVICE_ENABLED = "service_enabled"

    const val SIDE_LEFT = "LEFT"
    const val SIDE_RIGHT = "RIGHT"

    const val EDGE_ZONE_PERCENT = 0.05f
    const val SETUP_SWIPES_NEEDED = 3

    const val ACTION_STOP_SERVICE = "io.github.rajnishkmehta.dhwanicontrol.action.STOP_SERVICE"
    const val QUICKBOOT_POWER_ON_ACTION = "android.intent.action.QUICKBOOT_POWERON"
    const val PACKAGE_SCHEME = "package"

    const val NOTIFICATION_CHANNEL_ID = "dhwanicontrol_service_channel"
    const val NOTIFICATION_ID = 1201
    const val STOP_ACTION_REQUEST_CODE = 19

    const val SWIPE_MIN_DISTANCE_DP = 40f
    const val FLING_MIN_VELOCITY_PX = 700f
    const val SAFE_NAVIGATION_FALLBACK_DP = 24
    const val HAPTIC_FALLBACK_DURATION_MS = 30L
    const val SETUP_SUCCESS_DELAY_MS = 220L
}
