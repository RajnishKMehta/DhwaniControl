package io.github.rajnishkmehta.dhwanicontrol.features.quicktile

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.databinding.ItemQuickTileGuideStepBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QuickSettingsGuidePagerAdapter(
    private val steps: List<QuickSettingsGuideStep>,
    private val context: android.content.Context
) : RecyclerView.Adapter<QuickSettingsGuidePagerAdapter.StepViewHolder>() {

    private val imageExecutor: ExecutorService = Executors.newFixedThreadPool(2)
    private val mainHandler = Handler(Looper.getMainLooper())
    private val imageStore = QuickSettingsGuideImageStore(context.applicationContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val binding = ItemQuickTileGuideStepBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StepViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.bind(steps[position])
    }

    override fun getItemCount(): Int = steps.size

    override fun onViewRecycled(holder: StepViewHolder) {
        holder.clear()
        super.onViewRecycled(holder)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        imageExecutor.shutdownNow()
        super.onDetachedFromRecyclerView(recyclerView)
    }

    inner class StepViewHolder(
        private val binding: ItemQuickTileGuideStepBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var requestId = 0

        fun bind(step: QuickSettingsGuideStep) {
            val context = binding.root.context
            val imageUrl = context.getString(step.imageUrlRes)
            val currentRequestId = ++requestId

            binding.stepTitleText.setText(step.titleRes)
            binding.stepBodyText.setText(step.bodyRes)
            binding.stepImageUrlText.text = imageUrl
            binding.viewImageButton.setOnClickListener {
                openImageUrl(imageUrl)
            }

            showLoading(R.string.quick_tile_guide_image_loading)

            imageExecutor.execute {
                val result = imageStore.loadBitmap(imageUrl)
                mainHandler.post {
                    if (currentRequestId != requestId) return@post

                    result.fold(
                        onSuccess = { bitmap ->
                            binding.stepImage.setImageBitmap(bitmap)
                            binding.stepImage.isVisible = true
                            binding.stepFallbackText.isVisible = false
                            binding.stepImageUrlLabelText.isVisible = false
                            binding.stepImageUrlText.isVisible = false
                            binding.viewImageButton.isVisible = false
                        },
                        onFailure = {
                            showFallback(R.string.quick_tile_guide_image_fallback)
                        }
                    )
                }
            }
        }

        fun clear() {
            requestId++
            binding.stepImage.setImageDrawable(null)
            binding.stepImage.isVisible = false
        }

        private fun showLoading(messageRes: Int) {
            binding.stepImage.setImageDrawable(null)
            binding.stepImage.isVisible = false
            binding.stepFallbackText.setText(messageRes)
            binding.stepFallbackText.isVisible = true
            binding.stepImageUrlLabelText.isVisible = false
            binding.stepImageUrlText.isVisible = false
            binding.viewImageButton.isVisible = false
        }

        private fun showFallback(messageRes: Int) {
            binding.stepImage.setImageDrawable(null)
            binding.stepImage.isVisible = false
            binding.stepFallbackText.setText(messageRes)
            binding.stepFallbackText.isVisible = true
            binding.stepImageUrlLabelText.isVisible = true
            binding.stepImageUrlText.isVisible = true
            binding.viewImageButton.isVisible = true
        }

        private fun openImageUrl(imageUrl: String) {
            val context = binding.root.context
            val intent = Intent(Intent.ACTION_VIEW, imageUrl.toUri())
            try {
                context.startActivity(intent)
            } catch (_: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    R.string.quick_tile_guide_image_link_open_failed,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
