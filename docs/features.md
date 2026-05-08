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

## Feature Ordering

Feature order is controlled by metadata in each feature controller:

- `displayOrder = 1` -> Quick Settings Tile
- `displayOrder = 2` -> Edge Swipe Overlay

The Home screen and registry use this metadata to render a stable, predictable order.
