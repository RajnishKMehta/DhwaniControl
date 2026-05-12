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

## 2) Edge Swipe Overlay

**What it does**
- Detects inward swipes from the configured screen edge.
- Opens Android's native volume controls.

**Requirements**
- Draw over other apps permission
- Notification permission on Android 13+
- Button navigation mode (gesture navigation blocks this feature)

**Configuration entry**
- Home -> Edge Swipe Overlay -> **Config**

## 3) Floating Button

**What it does**
- Displays a movable button that stays on top of other apps.
- Tapping the button opens Android's native volume controls.
- Long-pressing and dragging the button allows you to reposition it anywhere on the screen.
- Saves the last position across app restarts.

**Requirements**
- Draw over other apps permission
- Notification permission on Android 13+

**Configuration entry**
- Home -> Floating Button (Toggle only)

## Feature Ordering

Feature order is controlled by metadata in each feature controller:

- `displayOrder = 1` -> Quick Settings Tile
- `displayOrder = 2` -> Floating Button
- `displayOrder = 3` -> Edge Swipe Overlay

The Home screen and registry use this metadata to render a stable, predictable order.
