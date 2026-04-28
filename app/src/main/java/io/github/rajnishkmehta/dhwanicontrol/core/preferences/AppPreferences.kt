package io.github.rajnishkmehta.dhwanicontrol.core.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import io.github.rajnishkmehta.dhwanicontrol.Constants

object AppPreferences {

    fun isEdgeConfigured(context: Context): Boolean {
        val preferences = preferences(context)
        migrateIfNeeded(preferences)
        return getBooleanSafely(preferences, Constants.PREF_EDGE_CONFIGURED, false)
    }

    fun setEdgeConfigured(context: Context, configured: Boolean) {
        val preferences = preferences(context)
        migrateIfNeeded(preferences)
        preferences.edit().putBoolean(Constants.PREF_EDGE_CONFIGURED, configured).apply()
    }

    fun getEdgeSelectedSide(context: Context): String {
        val preferences = preferences(context)
        migrateIfNeeded(preferences)
        return getStringSafely(preferences, Constants.PREF_EDGE_SELECTED_SIDE, Constants.SIDE_RIGHT)
    }

    fun setEdgeSelectedSide(context: Context, side: String) {
        val preferences = preferences(context)
        migrateIfNeeded(preferences)
        preferences.edit().putString(Constants.PREF_EDGE_SELECTED_SIDE, side).apply()
    }

    fun isEdgeEnabled(context: Context): Boolean {
        val preferences = preferences(context)
        migrateIfNeeded(preferences)
        return getBooleanSafely(preferences, Constants.PREF_EDGE_ENABLED, true)
    }

    fun setEdgeEnabled(context: Context, enabled: Boolean) {
        val preferences = preferences(context)
        migrateIfNeeded(preferences)
        preferences.edit().putBoolean(Constants.PREF_EDGE_ENABLED, enabled).apply()
    }

    fun getNotificationDenialCount(context: Context): Int {
        return getIntSafely(preferences(context), Constants.PREF_NOTIFICATION_DENIAL_COUNT, 0)
    }

    fun incrementNotificationDenialCount(context: Context) {
        val preferences = preferences(context)
        val newCount = getIntSafely(preferences, Constants.PREF_NOTIFICATION_DENIAL_COUNT, 0) + 1
        preferences.edit().putInt(Constants.PREF_NOTIFICATION_DENIAL_COUNT, newCount).apply()
    }

    fun resetNotificationDenialCount(context: Context) {
        preferences(context)
            .edit()
            .putInt(Constants.PREF_NOTIFICATION_DENIAL_COUNT, 0)
            .apply()
    }

    fun ensureMigration(context: Context) {
        val prefs = preferences(context)
        runCatching {
            migrateIfNeeded(prefs)
        }.onFailure {
            prefs.edit().clear().apply()
            prefs.edit().putBoolean(Constants.PREF_MIGRATION_COMPLETE, true).apply()
        }
    }

    private fun preferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    }

    private fun migrateIfNeeded(preferences: SharedPreferences) {
        val alreadyMigrated = getBooleanSafely(preferences, Constants.PREF_MIGRATION_COMPLETE, false)
        if (alreadyMigrated) {
            return
        }

        val legacyConfigured = getBooleanSafely(preferences, Constants.LEGACY_PREF_SETUP_COMPLETE, false)
        val legacyEnabled = getBooleanSafely(preferences, Constants.LEGACY_PREF_SERVICE_ENABLED, true)
        val legacySide = getStringSafely(preferences, Constants.LEGACY_PREF_SELECTED_SIDE, Constants.SIDE_RIGHT)

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

    private fun getBooleanSafely(preferences: SharedPreferences, key: String, defaultValue: Boolean): Boolean {
        return runCatching {
            preferences.getBoolean(key, defaultValue)
        }.getOrElse {
            val fallback = preferences.all[key]
            when (fallback) {
                is Boolean -> fallback
                is Number -> fallback.toInt() != 0
                is String -> fallback.equals("true", ignoreCase = true) || fallback == "1"
                else -> defaultValue
            }
        }
    }

    private fun getIntSafely(preferences: SharedPreferences, key: String, defaultValue: Int): Int {
        return runCatching {
            preferences.getInt(key, defaultValue)
        }.getOrElse {
            val fallback = preferences.all[key]
            when (fallback) {
                is Int -> fallback
                is Long -> fallback.toInt()
                is Float -> fallback.toInt()
                is Boolean -> if (fallback) 1 else 0
                is String -> fallback.toIntOrNull() ?: defaultValue
                else -> defaultValue
            }
        }
    }

    private fun getStringSafely(preferences: SharedPreferences, key: String, defaultValue: String): String {
        return runCatching {
            preferences.getString(key, defaultValue)
        }.getOrElse {
            val fallback = preferences.all[key]
            when (fallback) {
                is String -> fallback
                is Number, is Boolean -> fallback.toString()
                else -> defaultValue
            }
        } ?: defaultValue
    }
}
