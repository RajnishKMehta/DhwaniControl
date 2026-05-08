package io.github.rajnishkmehta.dhwanicontrol.features.quicktile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
            binding.stepTitleText.setText(step.titleRes)
            binding.stepBodyText.setText(step.bodyRes)
        }
    }
}
