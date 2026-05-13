package io.github.rajnishkmehta.dhwanicontrol.core.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import io.github.rajnishkmehta.dhwanicontrol.Constants

object AppPreferences {

    fun isEdgeConfigured(context: Context): Boolean {
        val preferences = preferences(context)
        migrateIfNeeded(preferences)
        return getBooleanSafe(preferences, Constants.PREF_EDGE_CONFIGURED, false)
    }

    fun setEdgeConfigured(context: Context, configured: Boolean) {
        val preferences = preferences(context)
        migrateIfNeeded(preferences)
        preferences.edit().putBoolean(Constants.PREF_EDGE_CONFIGURED, configured).apply()
    }

    fun getEdgeSelectedSide(context: Context): String {
        val preferences = preferences(context)
        migrateIfNeeded(preferences)
        return getStringSafe(preferences, Constants.PREF_EDGE_SELECTED_SIDE, Constants.SIDE_RIGHT)
    }

    fun setEdgeSelectedSide(context: Context, side: String) {
        val preferences = preferences(context)
        migrateIfNeeded(preferences)
        preferences.edit().putString(Constants.PREF_EDGE_SELECTED_SIDE, side).apply()
    }

    fun isEdgeEnabled(context: Context): Boolean {
        val preferences = preferences(context)
        migrateIfNeeded(preferences)
        return getBooleanSafe(preferences, Constants.PREF_EDGE_ENABLED, false)
    }

    fun setEdgeEnabled(context: Context, enabled: Boolean) {
        val preferences = preferences(context)
        migrateIfNeeded(preferences)
        preferences.edit().putBoolean(Constants.PREF_EDGE_ENABLED, enabled).apply()
    }

    fun isFloatingEnabled(context: Context): Boolean {
        val preferences = preferences(context)
        return getBooleanSafe(preferences, Constants.PREF_FLOATING_ENABLED, false)
    }

    fun setFloatingEnabled(context: Context, enabled: Boolean) {
        val preferences = preferences(context)
        preferences.edit().putBoolean(Constants.PREF_FLOATING_ENABLED, enabled).apply()
    }

    fun getFloatingPosition(context: Context): Pair<Int, Int>? {
        val preferences = preferences(context)
        if (!preferences.contains(Constants.PREF_FLOATING_X) || !preferences.contains(Constants.PREF_FLOATING_Y)) {
            return null
        }
        val x = getIntSafe(preferences, Constants.PREF_FLOATING_X, 0)
        val y = getIntSafe(preferences, Constants.PREF_FLOATING_Y, 0)
        return Pair(x, y)
    }

    fun setFloatingPosition(context: Context, x: Int, y: Int) {
        val preferences = preferences(context)
        preferences.edit()
            .putInt(Constants.PREF_FLOATING_X, x)
            .putInt(Constants.PREF_FLOATING_Y, y)
            .apply()
    }

    fun getFloatingIconName(context: Context): String {
        return getStringSafe(preferences(context), Constants.PREF_FLOATING_ICON_NAME, "ic_1_volume-up")
    }

    fun setFloatingIconName(context: Context, name: String) {
        preferences(context).edit().putString(Constants.PREF_FLOATING_ICON_NAME, name).apply()
    }

    fun getFloatingIconColor(context: Context): Int {
        return getIntSafe(preferences(context), Constants.PREF_FLOATING_ICON_COLOR, -1)
    }

    fun setFloatingIconColor(context: Context, color: Int) {
        preferences(context).edit().putInt(Constants.PREF_FLOATING_ICON_COLOR, color).apply()
    }

    fun getNotificationDenialCount(context: Context): Int {
        return getIntSafe(preferences(context), Constants.PREF_NOTIFICATION_DENIAL_COUNT, 0)
    }

    fun incrementNotificationDenialCount(context: Context) {
        val preferences = preferences(context)
        val newCount = getIntSafe(preferences, Constants.PREF_NOTIFICATION_DENIAL_COUNT, 0) + 1
        preferences.edit().putInt(Constants.PREF_NOTIFICATION_DENIAL_COUNT, newCount).apply()
    }

    fun resetNotificationDenialCount(context: Context) {
        preferences(context)
            .edit()
            .putInt(Constants.PREF_NOTIFICATION_DENIAL_COUNT, 0)
            .apply()
    }

    fun ensureMigration(context: Context) {
        migrateIfNeeded(preferences(context))
    }

    private fun preferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    }

    private fun migrateIfNeeded(preferences: SharedPreferences) {
        val alreadyMigrated = getBooleanSafe(preferences, Constants.PREF_MIGRATION_COMPLETE, false)
        if (alreadyMigrated) {
            return
        }

        val legacyConfigured = getBooleanSafe(preferences, Constants.LEGACY_PREF_SETUP_COMPLETE, false)
        val legacyEnabled = getBooleanSafe(preferences, Constants.LEGACY_PREF_SERVICE_ENABLED, false)
        val legacySide = getStringSafe(preferences, Constants.LEGACY_PREF_SELECTED_SIDE, Constants.SIDE_RIGHT)

        val editor = preferences.edit()
        if (!preferences.contains(Constants.PREF_EDGE_CONFIGURED)) {
            editor.putBoolean(Constants.PREF_EDGE_CONFIGURED, legacyConfigured)
        }
        if (!preferences.contains(Constants.PREF_EDGE_ENABLED)) {
            editor.putBoolean(Constants.PREF_EDGE_ENABLED, legacyEnabled)
        }
        if (!preferences.contains(Constants.PREF_EDGE_SELECTED_SIDE)) {
            editor.putString(Constants.PREF_EDGE_SELECTED_SIDE, legacySide)
        }

        editor.putBoolean(Constants.PREF_MIGRATION_COMPLETE, true)
        editor.apply()
    }

    private fun getBooleanSafe(preferences: SharedPreferences, key: String, defaultValue: Boolean): Boolean {
        return runCatching {
            preferences.getBoolean(key, defaultValue)
        }.getOrDefault(defaultValue)
    }

    private fun getIntSafe(preferences: SharedPreferences, key: String, defaultValue: Int): Int {
        return runCatching {
            preferences.getInt(key, defaultValue)
        }.getOrDefault(defaultValue)
    }

    private fun getStringSafe(preferences: SharedPreferences, key: String, defaultValue: String): String {
        return runCatching {
            preferences.getString(key, defaultValue)
        }.getOrNull() ?: defaultValue
    }
}
