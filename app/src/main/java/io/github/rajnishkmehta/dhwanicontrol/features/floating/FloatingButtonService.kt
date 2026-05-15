package io.github.rajnishkmehta.dhwanicontrol.features.floating

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
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
import io.github.rajnishkmehta.dhwanicontrol.home.HomeActivity
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

    private var initialX: Int = 0
    private var initialY: Int = 0
    private var initialTouchX: Float = 0f
    private var initialTouchY: Float = 0f

    private var touchStartTime: Long = 0
    private val longPressTimeout = ViewConfiguration.getLongPressTimeout().toLong()
    private var isLongPressing = false

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

        startForeground(Constants.NOTIFICATION_ID_FLOATING, buildForegroundNotification())
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
        val iconResId = resources.getIdentifier(iconName, "drawable", packageName)
            .takeIf { it != 0 } ?: resources.getIdentifier("ic_0_default", "drawable", packageName)
val iconColor = AppPreferences.getFloatingIconColor(this)
val tintColor = if (iconColor == -1) {
    getColor(R.color.colorPrimary)
} else {
    iconColor
}

val opacity = AppPreferences.getFloatingOpacity(this)

if (floatingView != null) {
    (floatingView as? ImageView)?.let {
        it.setImageResource(iconResId)
        it.setColorFilter(tintColor)
        it.alpha = opacity
    }
    return
}

val iconSize = dpToPx(48)

        val position = AppPreferences.getFloatingPosition(this)

        val layoutParams = WindowManager.LayoutParams(
            iconSize,
            iconSize,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = position?.first ?: 100
            y = position?.second ?: 100
        }

        params = layoutParams

        val imageView = ImageView(this).apply {
            setImageResource(iconResId)
            setColorFilter(tintColor)
            alpha = opacity
            setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8))
            elevation = dpToPx(4).toFloat()
        }

        imageView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = layoutParams.x
                    initialY = layoutParams.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    touchStartTime = System.currentTimeMillis()
                    isLongPressing = false
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = event.rawX - initialTouchX
                    val deltaY = event.rawY - initialTouchY

                    if (!isLongPressing && (abs(deltaX) > 10 || abs(deltaY) > 10)) {
                        if (System.currentTimeMillis() - touchStartTime > longPressTimeout) {
                            isLongPressing = true
                            view.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
                        }
                    }

                    if (isLongPressing) {
                        val metrics = resources.displayMetrics
                        val screenWidth = metrics.widthPixels
                        val screenHeight = metrics.heightPixels

                        val newX = (initialX + deltaX.toInt()).coerceIn(0, screenWidth - view.width)
                        val newY = (initialY + deltaY.toInt()).coerceIn(0, screenHeight - view.height)

                        layoutParams.x = newX
                        layoutParams.y = newY
                        windowManager.updateViewLayout(view, layoutParams)
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    val deltaX = event.rawX - initialTouchX
                    val deltaY = event.rawY - initialTouchY
                    
                    if (!isLongPressing && abs(deltaX) < 10 && abs(deltaY) < 10) {
                        showNativeVolumePanel()
                        view.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
                    } else if (isLongPressing) {
                        val metrics = resources.displayMetrics
                        val screenWidth = metrics.widthPixels
                        val screenHeight = metrics.heightPixels
                        
                        val finalX = layoutParams.x.coerceIn(0, screenWidth - view.width)
                        val finalY = layoutParams.y.coerceIn(0, screenHeight - view.height)
                        
                        AppPreferences.setFloatingPosition(this, finalX, finalY)
                    }
                    isLongPressing = false
                    true
                }
                else -> false
            }
        }

        floatingView = imageView
        runCatching {
            windowManager.addView(imageView, layoutParams)
        }.onFailure {
            stopSelf()
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
            audioManager.adjustVolume(AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI)
        }
    }

    private fun buildForegroundNotification(): Notification {
        val stopServiceIntent = Intent(this, FloatingButtonService::class.java).apply {
            action = Constants.ACTION_STOP_FLOATING_SERVICE
        }

        val stopPendingIntent = PendingIntent.getService(
            this,
            Constants.STOP_ACTION_REQUEST_CODE + 1,
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

        return NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID_FLOATING)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getString(R.string.notification_title_floating))
            .setContentText(getString(R.string.notification_text_floating))
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
}
