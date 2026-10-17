
package io.github.rajnishkmehta.dhwanicontrol.features.floating

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.res.Configuration
import android.graphics.PixelFormat
import android.media.AudioManager
import android.os.Build
import android.os.IBinder
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import io.github.rajnishkmehta.dhwanicontrol.Constants
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.core.feature.PermissionRequirement
import io.github.rajnishkmehta.dhwanicontrol.core.permission.PermissionPolicy
import io.github.rajnishkmehta.dhwanicontrol.core.preferences.AppPreferences
import kotlin.math.abs

class FloatingButtonService : Service() {

    private val windowManager by lazy {
        getSystemService(WINDOW_SERVICE) as WindowManager
    }

    private val audioManager by lazy {
        getSystemService(AUDIO_SERVICE) as AudioManager
    }

    private var floatingView: View? = null
    private var params: WindowManager.LayoutParams? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannelIfNeeded()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == Constants.ACTION_STOP_FLOATING_SERVICE) {
            AppPreferences.setFloatingEnabled(this, false)
            stopSelf()
            return START_NOT_STICKY
        }

        // Set to specialUse type to match VolumeOverlayService and since it's an overlay
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                Constants.NOTIFICATION_ID_FLOATING,
                buildForegroundNotification()
            )
        } else {
            startForeground(
                Constants.NOTIFICATION_ID_FLOATING,
                buildForegroundNotification()
            )
        }

        updateFloatingButton()
        return START_STICKY
    }

    override fun onDestroy() {
        detachFloatingButton()
        super.onDestroy()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun updateFloatingButton() {
        if (!PermissionPolicy.isGranted(this, PermissionRequirement.Overlay)) {
            stopSelf()
            return
        }

        val iconName = AppPreferences.getFloatingIconName(this)
        val icon = OverlayIconRegistry.getIconByName(iconName)
        val iconResId = icon.resId
        val iconColor = AppPreferences.getFloatingIconColor(this)

        val tintColor = if (iconColor == 0) {
            getColor(R.color.colorPrimary)
        } else {
            iconColor
        }

        val opacity = AppPreferences.getFloatingOpacity(this)
        val isFixedPosition = AppPreferences.isFloatingFixedPosition(this)

        if (floatingView != null) {
            (floatingView as? ImageView)?.let {
                it.setImageResource(iconResId)
                it.setColorFilter(tintColor)
                it.alpha = opacity
                it.setOnTouchListener(FloatingTouchListener(isFixedPosition))
            }
            return
        }

        val iconSize = dpToPx(48)

        val isLandscape =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val position = AppPreferences.getFloatingPosition(this, isLandscape)

        // Using FLAG_LAYOUT_NO_LIMITS to fix edge bounds clipping
        val layoutParams = WindowManager.LayoutParams(
            iconSize,
            iconSize,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START

            val bounds = getScreenBounds()
            val screenWidth = bounds.first
            val screenHeight = bounds.second

            x = if (position != null) {
                (position.first * (screenWidth - iconSize))
                    .toInt()
                    .coerceIn(0, (screenWidth - iconSize).coerceAtLeast(0))
            } else {
                100
            }

            y = if (position != null) {
                (position.second * (screenHeight - iconSize))
                    .toInt()
                    .coerceIn(0, (screenHeight - iconSize).coerceAtLeast(0))
            } else {
                100
            }
        }

        params = layoutParams

        val imageView = ImageView(this).apply {
            setImageResource(iconResId)
            setColorFilter(tintColor)
            alpha = opacity
            setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8))
            elevation = dpToPx(4).toFloat()
            setOnTouchListener(FloatingTouchListener(isFixedPosition))
        }

        floatingView = imageView

        runCatching {
            windowManager.addView(imageView, layoutParams)
        }.onFailure {
            stopSelf()
        }
    }

    private inner class FloatingTouchListener(
        private val isFixedPosition: Boolean
    ) : View.OnTouchListener {

        private var initialX: Int = 0
        private var initialY: Int = 0
        private var initialTouchX: Float = 0f
        private var initialTouchY: Float = 0f

        private var touchStartTime: Long = 0
        private val longPressTimeout = ViewConfiguration.getLongPressTimeout().toLong()
        private var isLongPressing = false
        private val touchSlop =
            ViewConfiguration.get(this@FloatingButtonService).scaledTouchSlop

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(view: View, event: MotionEvent): Boolean {
            val layoutParams = params ?: return false

            if (isFixedPosition) {
                if (event.action == MotionEvent.ACTION_UP) {
                    showNativeVolumePanel()
                    view.performHapticFeedback(
                        android.view.HapticFeedbackConstants.VIRTUAL_KEY
                    )
                    return true
                }
                return event.action == MotionEvent.ACTION_DOWN
            }

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = layoutParams.x
                    initialY = layoutParams.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    touchStartTime = System.currentTimeMillis()
                    isLongPressing = false
                    return true
                }

                MotionEvent.ACTION_MOVE -> {
                    val deltaX = event.rawX - initialTouchX
                    val deltaY = event.rawY - initialTouchY

                    if (
                        !isLongPressing &&
                        (abs(deltaX) > touchSlop || abs(deltaY) > touchSlop)
                    ) {
                        if (
                            System.currentTimeMillis() - touchStartTime >
                            longPressTimeout
                        ) {
                            isLongPressing = true
                            view.performHapticFeedback(
                                android.view.HapticFeedbackConstants.LONG_PRESS
                            )
                        }
                    }

                    if (isLongPressing) {
                        val bounds = getScreenBounds()
                        val screenWidth = bounds.first
                        val screenHeight = bounds.second

                        val maxX = (screenWidth - view.width).coerceAtLeast(0)
                        val maxY = (screenHeight - view.height).coerceAtLeast(0)

                        val newX = (initialX + deltaX.toInt()).coerceIn(0, maxX)
                        val newY = (initialY + deltaY.toInt()).coerceIn(0, maxY)

                        layoutParams.x = newX
                        layoutParams.y = newY

                        windowManager.updateViewLayout(view, layoutParams)
                    }

                    return true
                }

                MotionEvent.ACTION_UP -> {
                    val deltaX = event.rawX - initialTouchX
                    val deltaY = event.rawY - initialTouchY

                    if (
                        !isLongPressing &&
                        abs(deltaX) <= touchSlop &&
                        abs(deltaY) <= touchSlop
                    ) {
                        showNativeVolumePanel()
                        view.performHapticFeedback(
                            android.view.HapticFeedbackConstants.VIRTUAL_KEY
                        )
                    } else if (isLongPressing) {
                        val bounds = getScreenBounds()
                        val screenWidth = bounds.first
                        val screenHeight = bounds.second

                        val maxX = (screenWidth - view.width).coerceAtLeast(0)
                        val maxY = (screenHeight - view.height).coerceAtLeast(0)

                        val finalX = layoutParams.x.coerceIn(0, maxX)
                        val finalY = layoutParams.y.coerceIn(0, maxY)

                        val percentX =
                            finalX.toFloat() / maxX.coerceAtLeast(1)
                        val percentY =
                            finalY.toFloat() / maxY.coerceAtLeast(1)

                        val isLandscape =
                            resources.configuration.orientation ==
                                Configuration.ORIENTATION_LANDSCAPE

                        AppPreferences.setFloatingPosition(
                            this@FloatingButtonService,
                            percentX,
                            percentY,
                            isLandscape
                        )
                    }

                    isLongPressing = false
                    return true
                }

                else -> return false
            }
        }
    }

    private fun getScreenBounds(): Pair<Int, Int> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics = windowManager.currentWindowMetrics
            val bounds = metrics.bounds
            Pair(bounds.width(), bounds.height())
        } else {
            val metrics = resources.displayMetrics
            Pair(metrics.widthPixels, metrics.heightPixels)
        }
    }

    private fun detachFloatingButton() {
        floatingView?.let {
            runCatching {
                windowManager.removeView(it)
            }
        }

        floatingView = null
    }

    private fun showNativeVolumePanel() {
        runCatching {
            audioManager.adjustVolume(
                AudioManager.ADJUST_SAME,
                AudioManager.FLAG_SHOW_UI
            )
        }
    }

    private fun buildForegroundNotification(): Notification {
        return NotificationCompat.Builder(
            this,
            Constants.NOTIFICATION_CHANNEL_ID_FLOATING
        )
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .build()
    }

    private fun createNotificationChannelIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID_FLOATING,
            getString(R.string.notification_channel_name_floating),
            NotificationManager.IMPORTANCE_MIN
        ).apply {
            description = getString(R.string.notification_channel_description_floating)
            setShowBadge(false)
            lockscreenVisibility = Notification.VISIBILITY_SECRET
        }

        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)
    }

    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        applySavedPosition(
            newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
        )
    }

    private fun applySavedPosition(isLandscape: Boolean) {
        val view = floatingView ?: return
        val currentParams = params ?: return
        val position = AppPreferences.getFloatingPosition(this, isLandscape)

        val bounds = getScreenBounds()
        val screenWidth = bounds.first
        val screenHeight = bounds.second
        val iconSizePx = dpToPx(48)

        val maxX = (screenWidth - iconSizePx).coerceAtLeast(0)
        val maxY = (screenHeight - iconSizePx).coerceAtLeast(0)

        if (position != null) {
            currentParams.x =
                (position.first * maxX).toInt().coerceIn(0, maxX)
            currentParams.y =
                (position.second * maxY).toInt().coerceIn(0, maxY)
        } else {
            currentParams.x = currentParams.x.coerceIn(0, maxX)
            currentParams.y = currentParams.y.coerceIn(0, maxY)
        }

        windowManager.updateViewLayout(view, currentParams)
    }
}
