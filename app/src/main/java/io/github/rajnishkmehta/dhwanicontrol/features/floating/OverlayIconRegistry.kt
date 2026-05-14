package io.github.rajnishkmehta.dhwanicontrol.features.floating

import android.content.Context
import io.github.rajnishkmehta.dhwanicontrol.R

object OverlayIconRegistry {

    /**
     * List of icon names available in res/drawable/overlay/
     * This list can be manually updated or ideally discovered.
     * Since dynamic discovery of resource names by pattern at runtime is 
     * resource-intensive, we use a controlled list.
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
