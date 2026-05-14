package io.github.rajnishkmehta.dhwanicontrol.features.floating

object OverlayIconRegistry {

    /**
     * List of icon names available in res/overlay/drawable/
     */
    val allIcons = listOf(
        "sound_detection_loud_sound_24",
        "discover_tune_24",
        "adjust_24",
        "brand_awareness_24",
        "mobile_speaker_24",
        "headphones_24",
        "sliders_24",
        "volume_down_24",
        "speaker_phone_24",
        "doorbell_chime_24",
        "mobile_sound_24"
    )

    fun getDefaultIconName(): String = allIcons.first()

    fun getMoreIconName(): String = "cards_24"
}
