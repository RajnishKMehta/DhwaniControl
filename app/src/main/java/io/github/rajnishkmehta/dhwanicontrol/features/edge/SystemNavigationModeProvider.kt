package io.github.rajnishkmehta.dhwanicontrol.features.edge

import android.content.Context
import android.provider.Settings

object SystemNavigationModeProvider {

    fun getMode(context: Context): Int {
        return runCatching {
            Settings.Secure.getInt(
                context.contentResolver,
                "navigation_mode",
                0
            )
        }.getOrDefault(0)
    }
}
