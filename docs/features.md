# Features

DhwaniControl currently ships with two features.

## 1) Quick Settings Tile

**What it does**
- Adds a tile labeled **Volume Panel** to Android Quick Settings.
- Tapping the tile opens Android's native volume controls.

**Requirements**
- No runtime permission is required for normal use.

**Configuration entry**
- Home -> Quick Settings Tile -> **Config**

## 2) Floating Button

**What it does**
- Displays a movable icon that stays on top of other apps.
- **Customizable:** You can choose the icon design and its color.
- **Movable:** Long-press and drag the icon to reposition it anywhere.
- Tapping the icon opens Android's native volume controls.

**Requirements**
- Draw over other apps permission
- Notification permission on Android 13+

**Configuration entry**
- Home -> Floating Button -> **Config**

## 3) Edge Swipe

**What it does**
- Detects inward swipes from the configured screen edge (Left or Right).
- Highly responsive and stays out of the way when not needed.
- Opens Android's native volume controls.

**Requirements**
- Draw over other apps permission
- Notification permission on Android 13+
- Button navigation mode (gesture navigation blocks this feature)

**Configuration entry**
- Home -> Edge Swipe -> **Config**

## Detailed Feature Guides

For in-depth setup and technical details of each feature, please refer to the following documents:

- [Quick Settings Tile Guide](quick-settings-tile.md)
- [Floating Button Guide](floating-button.md)
- [Edge Swipe Guide](edge-swipe.md)

## Feature Ordering

Feature order is controlled by metadata in each feature controller:

- `displayOrder = 1` -> Quick Settings Tile
- `displayOrder = 2` -> Floating Button
- `displayOrder = 3` -> Edge Swipe

The Home screen and registry use this metadata to render a stable, predictable order.

## Feature Management System

DhwaniControl uses a granular management system to ensure features only run when fully ready:

1. **Permissions First:** If a feature lacks required permissions, its status will show **Permission required**. Toggling it ON will prompt for permissions.
2. **Auto-Disabling:** If a granted permission is revoked later, the feature will automatically turn itself **OFF** to prevent crashes.
3. **Smart Configuration:** Features that need manual setup (like Edge Swipe) will show **Configuration required** until completed. Features without complex setup (like Floating Button) hide the **Config** button for a cleaner UI.
4. **Safety Blocks:** System-level settings (like Gesture Navigation) can temporarily block incompatible features to ensure a smooth user experience.
