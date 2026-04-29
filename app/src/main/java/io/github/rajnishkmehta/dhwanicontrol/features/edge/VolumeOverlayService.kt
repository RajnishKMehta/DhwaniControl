package io.github.rajnishkmehta.dhwanicontrol.features.edge

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.res.Resources
import android.graphics.PixelFormat
import android.media.AudioManager
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.TypedValue
import android.view.GestureDetector
import android.view.Gravity
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.github.rajnishkmehta.dhwanicontrol.Constants
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.core.feature.PermissionRequirement
import io.github.rajnishkmehta.dhwanicontrol.core.permission.PermissionPolicy
import io.github.rajnishkmehta.dhwanicontrol.home.HomeActivity
import io.github.rajnishkmehta.dhwanicontrol.core.preferences.AppPreferences
import kotlin.math.abs

class VolumeOverlayService : Service() {

    companion object {
        @Volatile
        var isRunning: Boolean = false
            private set
    }

    private val windowManager by lazy {
        getSystemService(WINDOW_SERVICE) as WindowManager
    }

    private val audioManager by lazy {
        getSystemService(AUDIO_SERVICE) as AudioManager
    }

    private var overlayView: View? = null
    private var gestureDetector: GestureDetector? = null

    override fun onCreate() {
        super.onCreate()
        runCatching {
            createNotificationChannelIfNeeded()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == Constants.ACTION_STOP_EDGE_SERVICE) {
            AppPreferences.setEdgeEnabled(this, false)
            stopSelf()
            return START_NOT_STICKY
        }

        if (!isRunning) {
            val started = runCatching {
                startForeground(Constants.NOTIFICATION_ID, buildForegroundNotification())
            }.isSuccess
            if (!started) {
                stopSelf()
                return START_NOT_STICKY
            }
            isRunning = true
        }

        attachOverlay()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        detachOverlay()
        isRunning = false
        runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stopForeground(STOP_FOREGROUND_REMOVE)
            } else {
                @Suppress("DEPRECATION")
                stopForeground(true)
            }
        }
        super.onDestroy()
    }

    private fun attachOverlay() {
        if (!PermissionPolicy.isGranted(this, PermissionRequirement.Overlay)) {
            stopSelf()
            return
        }

        detachOverlay()

        val metrics = Resources.getSystem().displayMetrics
        val screenWidth = metrics.widthPixels
        val screenHeight = metrics.heightPixels
        val overlayWidth = (screenWidth * Constants.EDGE_ZONE_PERCENT).toInt().coerceAtLeast(1)

        val selectedSide = AppPreferences.getEdgeSelectedSide(this)
        val gravity = if (selectedSide == Constants.SIDE_LEFT) {
            Gravity.START or Gravity.TOP
        } else {
            Gravity.END or Gravity.TOP
        }

        val layoutParams = WindowManager.LayoutParams(
            overlayWidth,
            screenHeight,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            this.gravity = gravity
        }

        val touchView = View(this)
        val detector = GestureDetector(
            this,
            createGestureListener(side = selectedSide, touchTarget = touchView)
        )

        touchView.setOnTouchListener { _, motionEvent ->
            detector.onTouchEvent(motionEvent)
            false
        }

        gestureDetector = detector
        overlayView = touchView

        runCatching {
            windowManager.addView(touchView, layoutParams)
        }.onFailure {
            stopSelf()
            return
        }

        touchView.post {
            updateOverlayHeight(
                touchView = touchView,
                layoutParams = layoutParams,
                fallbackScreenHeight = screenHeight
            )
        }
    }

    private fun updateOverlayHeight(
        touchView: View,
        layoutParams: WindowManager.LayoutParams,
        fallbackScreenHeight: Int
    ) {
        val decorView = touchView.rootView
        val insets = ViewCompat.getRootWindowInsets(decorView)
        val navInsetBottom = insets
            ?.getInsets(WindowInsetsCompat.Type.navigationBars())
            ?.bottom

        val fallbackSafeMargin = dpToPx(Constants.SAFE_NAVIGATION_FALLBACK_DP)
        val targetHeight = if (navInsetBottom != null) {
            (fallbackScreenHeight - navInsetBottom).coerceAtLeast(1)
        } else {
            (fallbackScreenHeight - fallbackSafeMargin).coerceAtLeast(1)
        }

        if (layoutParams.height == targetHeight) {
            return
        }

        layoutParams.height = targetHeight
        runCatching {
            windowManager.updateViewLayout(touchView, layoutParams)
        }
    }

    private fun createGestureListener(side: String, touchTarget: View): GestureDetector.SimpleOnGestureListener {
        return object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 == null) {
                    return false
                }

                if (!isInwardSwipe(e1, e2, velocityX, velocityY, side)) {
                    return false
                }

                showNativeVolumePanel()
                performConfirmationHaptic(touchTarget)
                return false
            }
        }
    }

    private fun isInwardSwipe(
        startEvent: MotionEvent,
        endEvent: MotionEvent,
        velocityX: Float,
        velocityY: Float,
        side: String
    ): Boolean {
        val deltaX = endEvent.x - startEvent.x
        val deltaY = endEvent.y - startEvent.y

        val minDistancePx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            Constants.SWIPE_MIN_DISTANCE_DP,
            resources.displayMetrics
        )

        val isHorizontal = abs(deltaX) > abs(deltaY)
        val hasEnoughDistance = abs(deltaX) >= minDistancePx
        val hasEnoughVelocity = abs(velocityX) >= Constants.FLING_MIN_VELOCITY_PX

        val isInwardDirection = when (side) {
            Constants.SIDE_LEFT -> deltaX > 0f
            Constants.SIDE_RIGHT -> deltaX < 0f
            else -> false
        }

        return isHorizontal &&
            hasEnoughDistance &&
            hasEnoughVelocity &&
            isInwardDirection &&
            abs(velocityX) > abs(velocityY)
    }

    private fun showNativeVolumePanel() {
        runCatching {
            audioManager.adjustVolume(AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI)
        }
    }

    private fun performConfirmationHaptic(target: View) {
        val gestureEndAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
        val performed = if (gestureEndAvailable) {
            target.performHapticFeedback(HapticFeedbackConstants.GESTURE_END)
        } else {
            false
        }

        if (!performed) {
            vibrateFallback()
        }
    }

    private fun vibrateFallback() {
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

    private fun buildForegroundNotification(): Notification {
        val stopServiceIntent = Intent(this, VolumeOverlayService::class.java).apply {
            action = Constants.ACTION_STOP_EDGE_SERVICE
        }

        val stopPendingIntent = PendingIntent.getService(
            this,
            Constants.STOP_ACTION_REQUEST_CODE,
            stopServiceIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val openAppIntent = Intent(this, HomeActivity::class.java)
        val openAppPendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text))
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setOngoing(true)
            .setShowWhen(false)
            .setContentIntent(openAppPendingIntent)
            .addAction(
                R.drawable.ic_notification,
                getString(R.string.notification_stop_action),
                stopPendingIntent
            )
            .build()
    }

    private fun createNotificationChannelIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_MIN
        ).apply {
            description = getString(R.string.notification_channel_description)
            setShowBadge(false)
            lockscreenVisibility = Notification.VISIBILITY_SECRET
        }

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun detachOverlay() {
        val view = overlayView ?: return
        runCatching {
            windowManager.removeView(view)
        }
        overlayView = null
        gestureDetector = null
    }

    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }
}
