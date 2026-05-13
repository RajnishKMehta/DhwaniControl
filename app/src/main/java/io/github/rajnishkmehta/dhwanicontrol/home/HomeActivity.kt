package io.github.rajnishkmehta.dhwanicontrol.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.rajnishkmehta.dhwanicontrol.Constants
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.core.block.FeatureAvailabilityEvaluator
import io.github.rajnishkmehta.dhwanicontrol.core.block.FeatureBlockResult
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureController
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureRegistry
import io.github.rajnishkmehta.dhwanicontrol.core.permission.PermissionPolicy
import io.github.rajnishkmehta.dhwanicontrol.core.preferences.AppPreferences
import io.github.rajnishkmehta.dhwanicontrol.databinding.ActivityHomeBinding
import io.github.rajnishkmehta.dhwanicontrol.info.AppInfoActivity
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
        binding.homeInfoButton.setOnClickListener {
            startActivity(Intent(this, AppInfoActivity::class.java))
        }
    }

    private fun synchronizeFeatures() {
        FeatureRegistry.all().forEach { feature ->
            try {
                feature.synchronize(this)
            } catch (exception: Exception) {
                Log.e(
                    "HomeActivity",
                    "Failed to synchronize feature ${feature.spec.featureId}",
                    exception
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        synchronizeFeatures()
        refreshFeatureCards()
    }

    private fun refreshFeatureCards() {
        val controllers = FeatureRegistry.all()

        controllers.forEach { controller ->
            runCatching {
                FeatureAvailabilityEvaluator.enforce(this, controller)
            }.onFailure {
                Log.e(
                    "HomeActivity",
                    "Failed to enforce availability for ${controller.spec.featureId}",
                    it
                )
            }
        }

        val models = controllers.map { controller ->
            buildFeatureModel(controller)
        }

        adapter.submitList(models)
    }

    private fun buildFeatureModel(controller: FeatureController): FeatureCardUiModel {
        val spec = controller.spec

        return runCatching {
            val availability = FeatureAvailabilityEvaluator.evaluate(this, controller)
            val configured = controller.isConfigured(this)
            val enabled = controller.isEnabled(this)
            val blockResult = availability.blockResult
            val isBlocked = blockResult is FeatureBlockResult.Blocked
            val missingPermissions = PermissionPolicy.missingPermissions(this, spec.requiredPermissions)
            val hasPermissions = missingPermissions.isEmpty()

            val needsConfiguration = spec.supportsToggle && !configured
            val statusIsWarning = isBlocked || !hasPermissions || needsConfiguration

            val statusText = when {
                isBlocked -> getString((blockResult as FeatureBlockResult.Blocked).reasonRes)

                !hasPermissions -> getString(R.string.feature_status_permissions_required)

                needsConfiguration ->
                    getString(R.string.feature_status_needs_config)

                spec.supportsToggle && enabled ->
                    getString(R.string.feature_status_enabled)

                spec.supportsToggle ->
                    getString(R.string.feature_status_disabled)

                configured ->
                    getString(R.string.feature_status_ready)

                else ->
                    getString(R.string.feature_status_needs_config)
            }

            FeatureCardUiModel(
                featureId = spec.featureId,
                title = getString(spec.nameRes),
                description = getString(spec.summaryRes),
                status = statusText,
                statusIsWarning = statusIsWarning,
                showToggle = spec.supportsToggle,
                toggleEnabled = spec.supportsToggle && !isBlocked,
                toggledOn = spec.supportsToggle && enabled,
                showConfig = spec.supportsConfig,
                configEnabled = !isBlocked
            )
        }.getOrElse {
            FeatureCardUiModel(
                featureId = spec.featureId,
                title = getString(spec.nameRes),
                description = getString(spec.summaryRes),
                status = getString(R.string.feature_status_unavailable),
                statusIsWarning = true,
                showToggle = spec.supportsToggle,
                toggleEnabled = false,
                toggledOn = false,
                showConfig = spec.supportsConfig,
                configEnabled = false
            )
        }
    }

    private fun handleConfigClick(featureId: String) {
        val controller = FeatureRegistry.findById(featureId) ?: return

        val blockResult = controller.blockCondition.evaluate(this)
        if (blockResult is FeatureBlockResult.Blocked) {
            return
        }

        val missingPermissions = PermissionPolicy.missingPermissions(
            this,
            controller.spec.requiredPermissions
        )

        if (missingPermissions.isNotEmpty()) {
            val intent = Intent(this, PermissionHubActivity::class.java)
                .putExtra(Constants.EXTRA_FEATURE_ID, featureId)

            startActivity(intent)
            return
        }

        runCatching {
            controller.openConfig(this)
        }.onFailure {
            Toast.makeText(this, R.string.feature_action_failed, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun handleToggleChanged(featureId: String, isEnabled: Boolean) {
        val controller = FeatureRegistry.findById(featureId) ?: return

        if (!controller.spec.supportsToggle) {
            return
        }

        val blockResult = controller.blockCondition.evaluate(this)
        if (blockResult is FeatureBlockResult.Blocked) {
            refreshFeatureCards()
            return
        }

        if (isEnabled) {
            val missingPermissions = PermissionPolicy.missingPermissions(
                this,
                controller.spec.requiredPermissions
            )

            if (missingPermissions.isNotEmpty()) {
                handleConfigClick(featureId)
                revertToggle(featureId)
                return
            }

            if (controller.spec.supportsConfig && !controller.isConfigured(this)) {
                handleConfigClick(featureId)
                revertToggle(featureId)
                return
            }
        }

        runCatching {
            controller.setEnabled(this, isEnabled)
        }.onFailure {
            Toast.makeText(this, R.string.feature_action_failed, Toast.LENGTH_SHORT)
                .show()
        }

        refreshFeatureCards()
    }

    private fun revertToggle(featureId: String) {
        val index = adapter.currentList.indexOfFirst { it.featureId == featureId }
        if (index != -1) {
            binding.root.post {
                adapter.notifyItemChanged(index)
            }
        }
    }
}
