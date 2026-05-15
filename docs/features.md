# Features Overview

![Version](https://img.shields.io/github/v/release/RajnishKMehta/DhwaniControl?include_prereleases&style=flat-square)

DhwaniControl provides multiple ways to access your device's volume controls. Each feature is designed to be lightweight, efficient, and non-intrusive.

---

## 1) Quick Settings Tile 🚀

**What it does**
- Adds a **Volume Panel** tile to your system's Quick Settings.
- Tap the tile from any screen to instantly open native volume controls.

**Key Highlights**
- Zero background battery drain.
- No special permissions required for basic operation.

**Configuration**
- Accessible via: `Home -> Quick Settings Tile -> Config`

---

## 2) Floating Button 🔘

**What it does**
- A persistent, movable overlay button that stays on top of all applications.
- **Customizable**: Choose from a variety of icons and set custom colors/opacity.
- **Draggable**: Long-press and move it to any position on your screen.

**Requirements**
- `Display over other apps` permission.
- `Notification permission` (Android 13+).

**Configuration**
- Accessible via: `Home -> Floating Button -> Config`

---

## 3) Edge Swipe ↔️

**What it does**
- Triggers volume controls by swiping inward from the screen edge.
- Discreet and highly responsive gesture-based trigger.

**Requirements**
- `Display over other apps` permission.
- `Notification permission` (Android 13+).
- **Note**: Requires button-based navigation (gesture navigation may interfere).

**Configuration**
- Accessible via: `Home -> Edge Swipe -> Config`

---

## 🛠 Feature Management System

DhwaniControl uses an intelligent management system to ensure stability:

1. **Permission Guard**: Features automatically prompt for missing permissions when toggled.
2. **Auto-Shutdown**: If a required permission is revoked, the feature gracefully turns itself OFF.
3. **Hardware Compatibility**: Some features (like Edge Swipe) intelligently detect if your system settings (like navigation mode) are compatible.
4. **Persistent State**: Your preferences and toggle states are saved and restored automatically on device reboot.

---

## 📖 Detailed Guides

- [Quick Settings Tile Guide](quick-settings-tile.md)
- [Floating Button Guide](floating-button.md)
- [Edge Swipe Guide](edge-swipe.md)
