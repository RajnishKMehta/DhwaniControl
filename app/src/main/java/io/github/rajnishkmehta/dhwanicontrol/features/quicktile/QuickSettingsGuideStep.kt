package io.github.rajnishkmehta.dhwanicontrol.features.quicktile

import androidx.annotation.StringRes

data class QuickSettingsGuideStep(
    @StringRes val titleRes: Int,
    @StringRes val bodyRes: Int,
    @StringRes val imageUrlRes: Int
)
