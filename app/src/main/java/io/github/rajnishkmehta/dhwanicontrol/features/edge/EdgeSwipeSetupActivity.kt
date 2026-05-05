package io.github.rajnishkmehta.dhwanicontrol.features.edge

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.TypedValue
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import io.github.rajnishkmehta.dhwanicontrol.Constants
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureAvailabilityEvaluator
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureBlockResult
import io.github.rajnishkmehta.dhwanicontrol.core.preferences.AppPreferences
import io.github.rajnishkmehta.dhwanicontrol.databinding.ActivityEdgeSwipeSetupBinding
import kotlin.math.abs

class EdgeSwipeSetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEdgeSwipeSetupBinding

    private var startX = 0f
    private var startY = 0f
    private var currentSide: String? = null
    private var swipeCount = 0
    private var setupCompleted = false

    private val edgeStripWidthPx by lazy {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            6f,
            resources.displayMetrics
        )
    }

    private val minSwipeDistancePx by lazy {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            Constants.SWIPE_MIN_DISTANCE_DP,
            resources.displayMetrics
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEdgeSwipeSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateSwipeCounter()

        binding.gestureSurface.setOnTouchListener { _, event ->
            handleGestureEvent(event)
            true
        }
    }

    override fun onResume() {
        super.onResume()
        val availability = FeatureAvailabilityEvaluator.enforce(this, EdgeSwipeFeatureController)
        if (availability.isBlocked) {
            val reason = when (val blockResult = availability.blockResult) {
                is FeatureBlockResult.Blocked -> blockResult.resolveReason(this)
                FeatureBlockResult.NotBlocked -> null
            }
            if (!reason.isNullOrBlank()) {
                Toast.makeText(this, reason, Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    private fun handleGestureEvent(event: MotionEvent) {
        if (setupCompleted) {
            return
        }

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }

            MotionEvent.ACTION_UP -> {
                processSwipe(endX = event.x, endY = event.y)
            }
        }
    }

    private fun processSwipe(endX: Float, endY: Float) {
        val deltaX = endX - startX
        val deltaY = endY - startY

        val isHorizontalSwipe = abs(deltaX) > abs(deltaY)
        val hasEnoughDistance = abs(deltaX) > minSwipeDistancePx
        if (!isHorizontalSwipe || !hasEnoughDistance) {
            return
        }

        val side = detectStartingSide(binding.gestureSurface.width.toFloat()) ?: return
        val isInwardSwipe = when (side) {
            Constants.SIDE_LEFT -> deltaX > 0
            Constants.SIDE_RIGHT -> deltaX < 0
            else -> false
        }

        if (!isInwardSwipe) {
            return
        }

        if (currentSide != side) {
            currentSide = side
            swipeCount = 1
        } else {
            swipeCount += 1
        }

        animateEdgeActivation(side)
        updateSwipeCounter()

        if (swipeCount >= Constants.SETUP_SWIPES_NEEDED) {
            completeSetup(side)
        }
    }

    private fun detectStartingSide(screenWidth: Float): String? {
        if (startX <= edgeStripWidthPx) {
            return Constants.SIDE_LEFT
        }

        if (startX >= screenWidth - edgeStripWidthPx) {
            return Constants.SIDE_RIGHT
        }

        return null
    }

    private fun animateEdgeActivation(side: String) {
        val targetView = if (side == Constants.SIDE_LEFT) {
            binding.leftEdgeStrip
        } else {
            binding.rightEdgeStrip
        }

        val inactiveColor = ContextCompat.getColor(this, R.color.edgeInactive)
        val activeColor = ContextCompat.getColor(this, R.color.edgeActive)

        ValueAnimator.ofObject(
            ArgbEvaluator(),
            inactiveColor,
            activeColor,
            inactiveColor
        ).apply {
            duration = 200L
            addUpdateListener { animator ->
                targetView.setBackgroundColor(animator.animatedValue as Int)
            }
            start()
        }
    }

    private fun updateSwipeCounter() {
        binding.swipeCounterText.text = getString(
            R.string.edge_setup_swipe_counter,
            swipeCount,
            Constants.SETUP_SWIPES_NEEDED
        )
    }

    private fun completeSetup(side: String) {
        setupCompleted = true

        AppPreferences.setEdgeSelectedSide(this, side)
        AppPreferences.setEdgeConfigured(this, true)

        binding.setupTitleText.setText(R.string.edge_setup_saved_message)
        binding.setupBodyText.setText(R.string.edge_setup_saved_detail)

        binding.gestureSurface.postDelayed({
            EdgeOverlayRuntime.sync(this)
            finish()
        }, Constants.SETUP_SUCCESS_DELAY_MS)
    }
}
