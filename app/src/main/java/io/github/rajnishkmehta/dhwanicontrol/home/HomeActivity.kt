package io.github.rajnishkmehta.dhwanicontrol.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.rajnishkmehta.dhwanicontrol.Constants
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureAvailabilityEvaluator
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureBlockResult
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureController
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureRegistry
import io.github.rajnishkmehta.dhwanicontrol.core.preferences.AppPreferences
import io.github.rajnishkmehta.dhwanicontrol.databinding.ActivityHomeBinding
import io.github.rajnishkmehta.dhwanicontrol.permission.PermissionHubActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val adapter by lazy {
        FeatureCardAdapter(
            onConfigClick = ::handleConfigClick,
            onToggleChanged = ::handleToggleChanged
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppPreferences.ensureMigration(this)

        binding.featureRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.featureRecyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        refreshFeatureCards()
    }

    private fun refreshFeatureCards() {
        val models = FeatureRegistry.all().map { controller ->
            buildFeatureModel(controller)
        }
        adapter.submitList(models)
    }

    private fun buildFeatureModel(controller: FeatureController): FeatureCardUiModel {
        val spec = controller.spec

        return runCatching {
            val availability = FeatureAvailabilityEvaluator.enforce(this, controller)
            val configured = controller.isConfigured(this)
            val enabled = controller.isEnabled(this)
            val blockedReason = when (val blockResult = availability.blockResult) {
                is FeatureBlockResult.Blocked -> blockResult.resolveReason(this)
                FeatureBlockResult.NotBlocked -> null
            }

            val statusText = when {
                availability.isBlocked -> blockedReason ?: getString(R.string.feature_status_blocked)
                spec.supportsToggle && !configured -> getString(R.string.feature_status_needs_config)
                spec.supportsToggle && enabled -> getString(R.string.feature_status_enabled)
                spec.supportsToggle -> getString(R.string.feature_status_disabled)
                configured -> getString(R.string.feature_status_ready)
                else -> getString(R.string.feature_status_needs_config)
            }

            FeatureCardUiModel(
                featureId = spec.id,
                title = getString(spec.titleRes),
                description = getString(spec.descriptionRes),
                status = statusText,
                isBlocked = availability.isBlocked,
                showToggle = spec.supportsToggle,
                toggleEnabled = spec.supportsToggle &&
                    configured &&
                    !availability.isBlocked &&
                    availability.missingPermissions.isEmpty(),
                toggledOn = spec.supportsToggle && enabled,
                configEnabled = !availability.isBlocked
            )
        }.getOrElse {
            FeatureCardUiModel(
                featureId = spec.id,
                title = getString(spec.titleRes),
                description = getString(spec.descriptionRes),
                status = getString(R.string.feature_status_unavailable),
                isBlocked = false,
                showToggle = spec.supportsToggle,
                toggleEnabled = false,
                toggledOn = false,
                configEnabled = false
            )
        }
    }

    private fun handleConfigClick(featureId: String) {
        val controller = FeatureRegistry.findById(featureId) ?: return
        val availability = FeatureAvailabilityEvaluator.enforce(this, controller)
        if (availability.isBlocked) {
            val blockReason = when (val blockResult = availability.blockResult) {
                is FeatureBlockResult.Blocked -> blockResult.resolveReason(this)
                FeatureBlockResult.NotBlocked -> null
            }
            if (!blockReason.isNullOrBlank()) {
                Toast.makeText(this, blockReason, Toast.LENGTH_SHORT).show()
            }
            refreshFeatureCards()
            return
        }

        if (availability.missingPermissions.isNotEmpty()) {
            val intent = Intent(this, PermissionHubActivity::class.java)
                .putExtra(Constants.EXTRA_FEATURE_ID, featureId)
            startActivity(intent)
            return
        }

        runCatching {
            controller.openConfig(this)
        }.onFailure {
            Toast.makeText(this, R.string.feature_action_failed, Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleToggleChanged(featureId: String, isEnabled: Boolean) {
        val controller = FeatureRegistry.findById(featureId) ?: return
        if (!controller.spec.supportsToggle) {
            return
        }

        val availability = FeatureAvailabilityEvaluator.enforce(this, controller)
        if (availability.isBlocked) {
            refreshFeatureCards()
            return
        }

        if (availability.missingPermissions.isNotEmpty()) {
            val intent = Intent(this, PermissionHubActivity::class.java)
                .putExtra(Constants.EXTRA_FEATURE_ID, featureId)
            startActivity(intent)
            refreshFeatureCards()
            return
        }

        runCatching {
            controller.setEnabled(this, isEnabled)
        }.onFailure {
            Toast.makeText(this, R.string.feature_action_failed, Toast.LENGTH_SHORT).show()
        }

        refreshFeatureCards()
    }
}
