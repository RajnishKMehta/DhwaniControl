package io.github.rajnishkmehta.dhwanicontrol.features.quicktile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.databinding.ItemQuickTileGuideStepBinding

class QuickSettingsGuidePagerAdapter(
    private val steps: List<QuickSettingsGuideStep>
) : RecyclerView.Adapter<QuickSettingsGuidePagerAdapter.StepViewHolder>() {

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

    inner class StepViewHolder(
        private val binding: ItemQuickTileGuideStepBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(step: QuickSettingsGuideStep) {
            val context = binding.root.context
            binding.stepTitleText.setText(step.titleRes)
            binding.stepBodyText.setText(step.bodyRes)

            binding.stepFallbackText.setText(R.string.quick_tile_guide_image_fallback)
            binding.stepImage.isVisible = true
            binding.stepFallbackText.isVisible = false

            val imageUrl = context.getString(step.imageUrlRes)
            binding.stepImage.load(imageUrl) {
                crossfade(true)
                diskCachePolicy(CachePolicy.ENABLED)
                memoryCachePolicy(CachePolicy.ENABLED)
                networkCachePolicy(CachePolicy.ENABLED)
                placeholder(R.drawable.ic_notification)
                error(R.drawable.ic_notification)
                listener(
                    onError = { _, _ ->
                        binding.stepImage.isVisible = false
                        binding.stepFallbackText.isVisible = true
                    }
                )
            }

            binding.stepImageUrlText.text = imageUrl
        }
    }
}
