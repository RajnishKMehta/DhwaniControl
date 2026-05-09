package io.github.rajnishkmehta.dhwanicontrol.home

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.rajnishkmehta.dhwanicontrol.R
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
            val context = binding.root.context
            val statusTextColor = if (item.statusIsWarning) {
                ContextCompat.getColor(context, R.color.colorWarning)
            } else {
                ContextCompat.getColor(context, R.color.colorSuccess)
            }
            val statusBgColor = if (item.statusIsWarning) {
                ContextCompat.getColor(context, R.color.colorWarningSurface)
            } else {
                ContextCompat.getColor(context, R.color.colorSurfaceVariant)
            }
            binding.featureStatusText.setTextColor(statusTextColor)
            binding.featureStatusText.backgroundTintList = ColorStateList.valueOf(statusBgColor)

            binding.featureToggleSwitch.setOnCheckedChangeListener(null)
            binding.featureToggleSwitch.isVisible = item.showToggle
            binding.featureToggleSwitch.isEnabled = item.toggleEnabled
            binding.featureToggleSwitch.isChecked = item.toggledOn
            binding.featureToggleSwitch.alpha = if (item.toggleEnabled) 1f else 0.5f

            binding.featureToggleSwitch.setOnCheckedChangeListener { _, isChecked ->
                onToggleChanged(item.featureId, isChecked)
            }

            binding.featureConfigButton.isEnabled = item.configEnabled
            binding.featureConfigButton.alpha = if (item.configEnabled) 1f else 0.6f
            val configBgColor = if (item.configEnabled) {
                ContextCompat.getColor(context, R.color.colorPrimary)
            } else {
                ContextCompat.getColor(context, R.color.colorDisabledSurface)
            }
            val configTextColor = if (item.configEnabled) {
                ContextCompat.getColor(context, R.color.colorTextPrimary)
            } else {
                ContextCompat.getColor(context, R.color.colorTextMuted)
            }
            binding.featureConfigButton.backgroundTintList = ColorStateList.valueOf(configBgColor)
            binding.featureConfigButton.setTextColor(configTextColor)
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
