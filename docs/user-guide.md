# User Guide

![Version](https://img.shields.io/github/v/release/RajnishKMehta/DhwaniControl?include_prereleases&style=flat-square)

This guide covers the initial setup and daily usage of DhwaniControl.

---

## 🛠 Prerequisites

- **Android Version**: Android 10 (API 29) or newer.
- **System Permissions**: 
    - `Overlay Permission` (required for Edge Swipe and Floating Button).
    - `Notification Permission` (required for Android 13+ background services).

---

## 🔋 Battery Optimization

To ensure that DhwaniControl services (Floating Button and Edge Swipe) remain active and responsive, it is highly recommended to exclude the app from system battery restrictions.

1. Go to **Settings** > **Apps** > **DhwaniControl**.
2. Tap on **Battery** or **Battery usage**.
3. Select **Unrestricted**.

This prevents the Android system from killing the background services that power the on-screen triggers.

---

## 🏁 Getting Started

1. **Install**: Sideload the APK or install via F-Droid.
2. **Launch**: Open **DhwaniControl** from your app drawer.
3. **Configure Quick Settings**:
    - Go to `Home -> Quick Settings Tile -> Config`.
    - Follow the step-by-step visual guide to add the tile to your notification panel.
4. **Enable Triggers**:
    - Toggle on **Floating Button** or **Edge Swipe** based on your preference.
    - Grant any requested permissions to activate the feature.

---

## 💡 Daily Usage

- **Quick Access**: Tap the **Volume Panel** tile in your Quick Settings at any time.
- **Gestures**: If Edge Swipe is active, swipe inward from your chosen screen edge.
- **Overlay**: Tap the floating button icon to trigger the volume panel.
- **Repositioning**: Long-press and drag the Floating Button to any screen location.
- **Customization**: Use the **Config** button on any feature card to adjust styles and behavior.

---

## 🔍 Troubleshooting

- **Feature says "Configuration required"**: Some features need a one-time setup (like selecting an edge for Edge Swipe). Open the **Config** menu for that feature and follow the prompts.
- **Edge Swipe is hidden or unresponsive**:
    - Ensure you are using **3-Button Navigation** mode. Gesture navigation uses the same screen edges and will block DhwaniControl.
    - Check if the **Notification permission** is granted.
- **Floating Button disappears or doesn't start**:
    - The system may have revoked the **Display over other apps** (Overlay) permission.
    - Ensure the app is set to **Unrestricted** in battery settings.
- **Quick Settings Tile missing**: You must manually add the tile to your active area. See the [Quick Settings Guide](quick-settings-tile.md) for visual steps.
- **Permissions not sticking**: Some aggressive battery savers or "security" apps may reset permissions. Ensure DhwaniControl is whitelisted.

---

## 📚 Related Documentation

- [Features Overview](features.md)
- [Floating Button Customization](floating-button.md)
- [Edge Swipe Configuration](edge-swipe.md)
- [Quick Settings Setup](quick-settings-tile.md)
