package io.github.rajnishkmehta.dhwanicontrol.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.rajnishkmehta.dhwanicontrol.databinding.ItemFeatureCardBinding

class FeatureCardAdapter(
    private val onConfigClick: (featureId: String) -> Unit,
    private val onToggleChanged: (featureId: String, isEnabled: Boolean) -> Unit
) : ListAdapter<FeatureCardUiModel, FeatureCardAdapter.FeatureViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        val binding = ItemFeatureCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FeatureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FeatureViewHolder(
        private val binding: ItemFeatureCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FeatureCardUiModel) {
            binding.featureTitleText.text = item.title
            binding.featureDescriptionText.text = item.description
            binding.featureStatusText.text = item.status

            binding.featureToggleSwitch.setOnCheckedChangeListener(null)
            binding.featureToggleSwitch.isVisible = item.showToggle
            binding.featureToggleSwitch.isEnabled = item.toggleEnabled
            binding.featureToggleSwitch.isChecked = item.toggledOn

            binding.featureToggleSwitch.setOnCheckedChangeListener { _, isChecked ->
                onToggleChanged(item.featureId, isChecked)
            }

            binding.featureConfigButton.isEnabled = item.configEnabled
            binding.featureConfigButton.setOnClickListener {
                onConfigClick(item.featureId)
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<FeatureCardUiModel>() {
        override fun areItemsTheSame(oldItem: FeatureCardUiModel, newItem: FeatureCardUiModel): Boolean {
            return oldItem.featureId == newItem.featureId
        }

        override fun areContentsTheSame(oldItem: FeatureCardUiModel, newItem: FeatureCardUiModel): Boolean {
            return oldItem == newItem
        }
    }
}
