package io.github.rajnishkmehta.dhwanicontrol.home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.radiobutton.MaterialRadioButton
import io.github.rajnishkmehta.dhwanicontrol.Constants
import java.util.Locale
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.core.block.FeatureAvailabilityEvaluator
import io.github.rajnishkmehta.dhwanicontrol.core.block.FeatureBlockResult
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureController
import io.github.rajnishkmehta.dhwanicontrol.core.feature.FeatureRegistry
import io.github.rajnishkmehta.dhwanicontrol.core.locale.LocaleManager
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
        binding.homeMoreMenuButton.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menu.add(0, 1, 0, getString(R.string.menu_about))
            popup.menu.add(0, 2, 0, getString(R.string.menu_language))

            // Add external link icons to browser links
            val linkIcon = ContextCompat.getDrawable(this, R.drawable.ic_open_in_new)?.apply {
                setBounds(0, 0, 40, 40)
            }

            fun addIconToText(text: String, icon: Drawable?): CharSequence {
                if (icon == null) return text
                val spannable = SpannableString("$text  ")
                val imageSpan = ImageSpan(icon, ImageSpan.ALIGN_BASELINE)
                spannable.setSpan(imageSpan, text.length + 1, text.length + 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                return spannable
            }

            val reportBugItem = popup.menu.add(0, 3, 0, addIconToText(getString(R.string.menu_report_bug), linkIcon))
            val featureRequestItem = popup.menu.add(0, 4, 0, addIconToText(getString(R.string.menu_feature_request), linkIcon))


            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    1 -> {
                        startActivity(Intent(this, AppInfoActivity::class.java))
                        true
                    }
                    2 -> {
                        showLanguageDialog()
                        true
                    }
                    3, 4 -> {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_report_issue)))
                        try {
                            startActivity(intent)
                        } catch (_: ActivityNotFoundException) {
                            Toast.makeText(this@HomeActivity, R.string.app_info_open_link_failed, Toast.LENGTH_SHORT).show()
                        }
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
        binding.homeHelpButton.setOnClickListener {
            openHelpLink()
        }
    }

    private fun openHelpLink() {
        val url = getString(R.string.app_info_help_url)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        runCatching {
            startActivity(intent)
        }.onFailure {
            Toast.makeText(this, R.string.app_info_open_link_failed, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Displays a dialog for selecting the application language.
     * Allows the user to change the app's locale and updates it using [LocaleManager].
     * Also provides a link to contribute to translating the app.
     */
    private fun showLanguageDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_language_selection, null)
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.languageRadioGroup)
        val translateLink = dialogView.findViewById<TextView>(R.id.translateLinkText)

        val locales = LocaleManager.getSupportedLocales(this)
        val currentLocaleTag = LocaleManager.getCurrentLocale(this).toLanguageTag()

        // Add System Default Option
        val systemRadioButton = MaterialRadioButton(this).apply {
            text = getString(R.string.menu_system_default)
            tag = null
            id = View.generateViewId()
            setTextColor(ContextCompat.getColor(this@HomeActivity, R.color.colorTextPrimary))
            layoutParams = RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 8)
            }
        }
        radioGroup.addView(systemRadioButton)
        if (AppCompatDelegate.getApplicationLocales().isEmpty) {
            radioGroup.check(systemRadioButton.id)
        }

        locales.forEach { locale ->
            val code = locale.toLanguageTag()
            val name = LocaleManager.getDisplayName(locale)

            val radioButton = MaterialRadioButton(this).apply {
                text = name
                tag = locale
                id = View.generateViewId()
                setTextColor(ContextCompat.getColor(this@HomeActivity, R.color.colorTextPrimary))
                layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8, 0, 8)
                }
            }
            radioGroup.addView(radioButton)

            // Check if this is the current locale
            val isCurrent = code == currentLocaleTag ||
                            (code.substringBefore("-") == currentLocaleTag.substringBefore("-") &&
                             code.substringAfter("-", "") == currentLocaleTag.substringAfter("-", ""))

            if (isCurrent && !AppCompatDelegate.getApplicationLocales().isEmpty) {
                radioGroup.check(radioButton.id)
            }
        }


        val linkIcon = ContextCompat.getDrawable(this, R.drawable.ic_open_in_new)?.apply {
            setBounds(0, 0, 40, 40)
            setTint(ContextCompat.getColor(this@HomeActivity, R.color.colorAccent))
        }
        val spannableTranslate = SpannableString(getString(R.string.dialog_language_translate_link) + "  ")
        val translateImageSpan = ImageSpan(linkIcon!!, ImageSpan.ALIGN_BASELINE)
        spannableTranslate.setSpan(translateImageSpan, spannableTranslate.length - 1, spannableTranslate.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        translateLink.text = spannableTranslate

        translateLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_translate)))
            try {
                startActivity(intent)
            } catch (_: ActivityNotFoundException) {
                Toast.makeText(this, R.string.app_info_open_link_failed, Toast.LENGTH_SHORT).show()
            }
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_language_title)
            .setCancelable(false)
            .setView(dialogView)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                val checkedId = radioGroup.checkedRadioButtonId
                if (checkedId != -1) {
                    val selectedRadio = dialogView.findViewById<MaterialRadioButton>(checkedId)
                    val selectedLocale = selectedRadio?.tag as? Locale
                    if (selectedLocale != null) {
                        LocaleManager.setLocale(selectedLocale)
                    } else {
                        LocaleManager.resetLocale()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
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
                configEnabled = !isBlocked,
                configButtonText = getString(spec.configActionRes)
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
                configEnabled = false,
                configButtonText = getString(spec.configActionRes)
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

        revertToggle(featureId)
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
