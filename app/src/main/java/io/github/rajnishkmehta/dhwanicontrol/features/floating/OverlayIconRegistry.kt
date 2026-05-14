package io.github.rajnishkmehta.dhwanicontrol.features.floating

object OverlayIconRegistry {

    /**
     * List of icon names available in res/overlay/drawable/
     */
    val allIcons = listOf(
        "ic_0_default",
        "ic_1_volume-up",
        "ic_2_volume-down",
        "ic_3_volume-medium",
        "ic_4_sound"
    )

    fun getDefaultIconName(): String = allIcons.first()

    fun getMoreIconName(): String = "ic_5_more"
}
