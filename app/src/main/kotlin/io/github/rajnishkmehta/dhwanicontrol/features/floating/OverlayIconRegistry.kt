package io.github.rajnishkmehta.dhwanicontrol.features.floating

import io.github.rajnishkmehta.dhwanicontrol.R

data class OverlayIcon(
    val name: String,
    val resId: Int
)

object OverlayIconRegistry {

    /**
     * List of icons available in res/overlay/drawable/
     */
    val allIcons = listOf(
        OverlayIcon("ic_0_default", R.drawable.ic_0_default),
        OverlayIcon("ic_1_tune", R.drawable.ic_1_tune),
        OverlayIcon("ic_2_adjust", R.drawable.ic_2_adjust),
        OverlayIcon("ic_3_awareness_sound", R.drawable.ic_3_awareness_sound),
        OverlayIcon("ic_4_mobile_speaker", R.drawable.ic_4_mobile_speaker),
        OverlayIcon("ic_5_headphones", R.drawable.ic_5_headphones),
        OverlayIcon("ic_6_volume_down", R.drawable.ic_6_volume_down),
        OverlayIcon("ic_7_speaker_phone", R.drawable.ic_7_speaker_phone),
        OverlayIcon("ic_8_doorbell_chime", R.drawable.ic_8_doorbell_chime),
        OverlayIcon("ic_9_mobile_sound", R.drawable.ic_9_mobile_sound),
        OverlayIcon("ic_10_panel", R.drawable.ic_10_panel)
    )

    fun getDefaultIcon(): OverlayIcon = allIcons.first()

    fun getMoreIcon(): OverlayIcon = OverlayIcon("ic_cards", R.drawable.ic_cards)
    
    fun getIconByName(name: String): OverlayIcon {
        return allIcons.find { it.name == name } ?: getDefaultIcon()
    }
}
