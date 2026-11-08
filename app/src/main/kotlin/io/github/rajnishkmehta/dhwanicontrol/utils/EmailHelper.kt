/*
 * Copyright 2026 Rajnish Kumar
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.rajnishkmehta.dhwanicontrol.utils

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.content.pm.PackageInfoCompat
import io.github.rajnishkmehta.dhwanicontrol.R

object EmailHelper {

    private const val DEVELOPER_EMAIL =
        "RajnishKMehta+DhwaniControl@proton.me"

    fun contactDeveloper(context: Context) {
        val appName = context.getString(R.string.app_name)

        var versionName = "Unknown"
        var versionCode = 0L

        try {
            val packageInfo = context.packageManager
                .getPackageInfo(context.packageName, 0)

            versionName = packageInfo.versionName ?: "Unknown"
            versionCode = PackageInfoCompat.getLongVersionCode(packageInfo)
        } catch (_: Exception) {
            // Keep default values
        }

        val manufacturer = Build.MANUFACTURER
        val deviceModel = Build.MODEL
        val androidVersion = Build.VERSION.RELEASE
        val sdkLevel = Build.VERSION.SDK_INT

        val subject = "[DhwaniControl] "

        val body = """
            Hello Rajnish,

            [Write your message here in English, Hindi or Maithili]


            ------------------------------
            App Information:

            App: $appName
            App Version: $versionName ($versionCode)
            Device: $manufacturer ($deviceModel)
            Android Version: $androidVersion (SDK: $sdkLevel)
            ------------------------------

            Thank you.
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(DEVELOPER_EMAIL))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }

        try {
            context.startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            try {
                val clipboard = context.getSystemService(
                    Context.CLIPBOARD_SERVICE
                ) as ClipboardManager

                clipboard.setPrimaryClip(
                    ClipData.newPlainText(
                        context.getString(R.string.dc_dev_email),
                        DEVELOPER_EMAIL
                    )
                )

                Toast.makeText(
                    context,
                    context.getString(R.string.email_app_not_found),
                    Toast.LENGTH_LONG
                ).show()

            } catch (_: Exception) {
                Toast.makeText(
                    context,
                    context.getString(R.string.email_app_not_found_fallback),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
