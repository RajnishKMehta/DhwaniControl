package io.github.rajnishkmehta.dhwanicontrol.features.floating

object OverlayIconRegistry {

    /**
     * List of icon names available in res/overlay/drawable/
     */
    val allIcons = listOf(
        "ic_0_default",
        "ic_1_volume_up",
        "ic_2_volume_down",
        "ic_3_volume_medium",
        "ic_4_sound"
    )

    fun getDefaultIconName(): String = allIcons.first()

    fun getMoreIconName(): String = "ic_5_more"
}
