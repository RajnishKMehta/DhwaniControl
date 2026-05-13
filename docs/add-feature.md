# Add a New Feature

This document explains the minimum flow to add a new feature safely.

## Architecture Rules

- Every feature has one `FeatureController` implementation.
- Feature metadata lives in `FeatureSpec` inside that controller.
- `FeatureRegistry` is the only place that wires feature controllers into the app.
- Home and permission flows resolve everything from registry + metadata.

## Step-by-Step

1. Add a new feature ID constant in `Constants.kt`.
2. Create a new package under `features/` for the feature.
3. Implement a controller object that conforms to `FeatureController`.
4. Define `FeatureSpec` in that controller:
   - `featureId`
   - `nameRes`
   - `summaryRes`
   - `supportsToggle`
   - `supportsConfig` (set to `false` to hide the Config button)
   - `requiredPermissions`

   - `displayOrder`
5. Implement behavior methods:
   - `isConfigured`
   - `isEnabled`
   - `setEnabled`
   - `openConfig`
   - `synchronize`
6. Register the controller in `FeatureRegistry`.
7. Add strings for title and description.
8. Add any new permission or block condition logic only if truly required.
9. Add or update documentation for user-facing setup.

## Feature Design Checklist

- Keep feature state isolated to its own preference keys.
- Avoid cross-feature side effects.
- Keep blocking and permission logic explicit.
- Keep Home card description short and neutral.
- Assign a stable `displayOrder` value.

## Validation Checklist

- Feature card appears on Home with correct order.
- Config route opens expected screen.
- Toggle behavior is correct (if enabled for that feature).
- Blocked and permission states render correctly.
- Existing features continue to work unchanged.
