package io.github.rajnishkmehta.dhwanicontrol.features.floating

object OverlayIconRegistry {

    /**
     * List of icon names available in res/overlay/drawable/
     */
    val allIcons = listOf(
        "ic_0_default",
        "ic_1_tune",
        "ic_2_adjust",
        "ic_3_awareness_sound",
        "ic_4_mobile_speaker",
        "ic_5_headphones",
        "ic_6_volume_down",
        "ic_7_speaker_phone",
        "ic_8_doorbell_chime",
        "ic_9_mobile_sound",
        "ic_10_panel"
    )

    fun getDefaultIconName(): String = allIcons.first()

    fun getMoreIconName(): String = "ic_cards"
}
