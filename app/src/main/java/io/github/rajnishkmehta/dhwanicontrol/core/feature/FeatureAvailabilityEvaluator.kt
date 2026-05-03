package io.github.rajnishkmehta.dhwanicontrol.core.feature

import android.content.Context
import io.github.rajnishkmehta.dhwanicontrol.core.permission.PermissionPolicy

object FeatureAvailabilityEvaluator {

    fun evaluate(context: Context, controller: FeatureController): FeatureAvailability {
        val blockResult = controller.blockCondition.evaluate(context)
        val missingPermissions = PermissionPolicy.missingPermissions(
            context,
            controller.spec.requiredPermissions
        )

        return FeatureAvailability(
            blockResult = blockResult,
            missingPermissions = missingPermissions
        )
    }

    fun enforce(context: Context, controller: FeatureController): FeatureAvailability {
        val initial = evaluate(context, controller)
        if (initial.isBlocked && controller.spec.supportsToggle && controller.isEnabled(context)) {
            runCatching {
                controller.setEnabled(context, false)
            }
        }

        return evaluate(context, controller)
    }
}
