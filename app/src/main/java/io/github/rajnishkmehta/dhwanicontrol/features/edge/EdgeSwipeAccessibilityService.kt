package io.github.rajnishkmehta.dhwanicontrol.features.edge

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.accessibilityservice.AccessibilityGestureEvent
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.TypedValue
import android.view.InputDevice
import android.view.MotionEvent
import android.view.accessibility.AccessibilityEvent
import io.github.rajnishkmehta.dhwanicontrol.Constants
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureBlockResult
import io.github.rajnishkmehta.dhwanicontrol.core.preferences.AppPreferences
import kotlin.math.abs

class EdgeSwipeAccessibilityService : AccessibilityService() {

    companion object {
        @Volatile
        private var instance: EdgeSwipeAccessibilityService? = null

        fun refreshConfiguration() {
            instance?.apply {
                runCatching { updateDynamicServiceInfo() }
            }
        }

        fun pauseEdgeDetection() {
            instance?.apply {
                runCatching { updateDynamicServiceInfo() }
            }
        }
    }

    private val audioManager by lazy {
        getSystemService(AUDIO_SERVICE) as AudioManager
    }

    private var startX = 0f
    private var startY = 0f
    private var lastX = 0f
    private var lastY = 0f
    private var hasMove = false

    private val minSwipeDistancePx by lazy {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            Constants.SWIPE_MIN_DISTANCE_DP,
            resources.displayMetrics
        )
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        updateDynamicServiceInfo()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // This service only observes gestures/motion for edge detection.
    }

    override fun onInterrupt() {
        // No spoken/haptic feedback stream to interrupt.
    }

    override fun onUnbind(intent: android.content.Intent?): Boolean {
        instance = null
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        instance = null
        super.onDestroy()
    }

    override fun onGesture(gestureEvent: AccessibilityGestureEvent): Boolean {
        if (!shouldDetectSwipe()) {
            return false
        }

        if (Build.VERSION.SDK_INT !in Build.VERSION_CODES.S..Build.VERSION_CODES.TIRAMISU) {
            return false
        }

        val fromMotion = isInwardSwipeFromMotionEvents(gestureEvent.motionEvents)
        val fromGestureId = isInwardSwipeFromGestureId(gestureEvent.gestureId)

        if (!fromMotion && !fromGestureId) {
            return false
        }

        triggerVolumePanel()
        return true
    }

    override fun onMotionEvent(event: MotionEvent) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            return
        }

        if (!shouldDetectSwipe()) {
            return
        }

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                lastX = event.x
                lastY = event.y
                hasMove = false
            }

            MotionEvent.ACTION_MOVE -> {
                lastX = event.x
                lastY = event.y
                hasMove = true
            }

            MotionEvent.ACTION_UP -> {
                lastX = event.x
                lastY = event.y
                if (isValidInwardSwipe(startX, startY, lastX, lastY)) {
                    triggerVolumePanel()
                }
                hasMove = false
            }

            MotionEvent.ACTION_CANCEL -> {
                hasMove = false
            }
        }
    }

    private fun updateDynamicServiceInfo() {
        val info = serviceInfo ?: AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPE_WINDOWS_CHANGED
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        info.notificationTimeout = 0

        val shouldDetect = shouldDetectSwipe()
        var flags = info.flags and AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE.inv()
        flags = flags and AccessibilityServiceInfo.FLAG_SEND_MOTION_EVENTS.inv()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && shouldDetect) {
            flags = flags or AccessibilityServiceInfo.FLAG_SEND_MOTION_EVENTS
        }
        info.flags = flags

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            if (shouldDetect) {
                info.setMotionEventSources(InputDevice.SOURCE_TOUCHSCREEN)
            } else {
                info.setMotionEventSources(0)
            }
        }

        serviceInfo = info
    }

    private fun shouldDetectSwipe(): Boolean {
        if (!AppPreferences.isEdgeConfigured(this) || !AppPreferences.isEdgeEnabled(this)) {
            return false
        }

        return EdgeSwipeBlockCondition.evaluate(this) !is FeatureBlockResult.Blocked
    }

    private fun isInwardSwipeFromMotionEvents(motionEvents: List<MotionEvent>): Boolean {
        if (motionEvents.size < 2) {
            return false
        }

        val downEvent = motionEvents.firstOrNull { it.actionMasked == MotionEvent.ACTION_DOWN }
            ?: motionEvents.first()
        val moveOrUpEvent = motionEvents.lastOrNull {
            it.actionMasked == MotionEvent.ACTION_MOVE || it.actionMasked == MotionEvent.ACTION_UP
        } ?: motionEvents.last()

        return isValidInwardSwipe(
            startX = downEvent.x,
            startY = downEvent.y,
            endX = moveOrUpEvent.x,
            endY = moveOrUpEvent.y
        )
    }

    private fun isInwardSwipeFromGestureId(gestureId: Int): Boolean {
        val side = AppPreferences.getEdgeSelectedSide(this)
        return when (side) {
            Constants.SIDE_LEFT -> gestureId == AccessibilityService.GESTURE_SWIPE_RIGHT
            Constants.SIDE_RIGHT -> gestureId == AccessibilityService.GESTURE_SWIPE_LEFT
            else -> false
        }
    }

    private fun isValidInwardSwipe(startX: Float, startY: Float, endX: Float, endY: Float): Boolean {
        val screenWidth = resources.displayMetrics.widthPixels.toFloat()
        val edgeZonePercent = AppPreferences.getEdgeZonePercent(this)
        val edgeThreshold = (screenWidth * edgeZonePercent).coerceAtLeast(1f)

        val side = when {
            startX <= edgeThreshold -> Constants.SIDE_LEFT
            startX >= screenWidth - edgeThreshold -> Constants.SIDE_RIGHT
            else -> return false
        }

        val selectedSide = AppPreferences.getEdgeSelectedSide(this)
        if (side != selectedSide) {
            return false
        }

        val deltaX = endX - startX
        val deltaY = endY - startY
        val horizontalDistance = abs(deltaX)
        val verticalDistance = abs(deltaY)
        val isHorizontal = horizontalDistance >= verticalDistance * Constants.SWIPE_HORIZONTAL_BIAS
        val hasMinDistance = horizontalDistance >= minSwipeDistancePx
        val isInward = when (side) {
            Constants.SIDE_LEFT -> deltaX > 0f
            Constants.SIDE_RIGHT -> deltaX < 0f
            else -> false
        }

        return isHorizontal && hasMinDistance && isInward
    }

    private fun triggerVolumePanel() {
        runCatching {
            audioManager.adjustVolume(AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI)
            performHapticFeedback()
        }
    }

    private fun performHapticFeedback() {
        val effect = VibrationEffect.createOneShot(
            Constants.HAPTIC_FALLBACK_DURATION_MS,
            VibrationEffect.DEFAULT_AMPLITUDE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator?.vibrate(effect)
            return
        }

        val vibrator = getSystemService(VIBRATOR_SERVICE) as? Vibrator ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(effect)
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(Constants.HAPTIC_FALLBACK_DURATION_MS)
        }
    }
}
