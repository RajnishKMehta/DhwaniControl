# User Guide

This guide covers first-time setup and normal usage.

## Requirements

- Android 10 (API 29) or newer
- Overlay permission for Edge Swipe Overlay
- Notification permission on Android 13+ for the edge background service

## First-Time Setup

1. Install the APK.
2. Open **DhwaniControl**.
3. On Home, open **Quick Settings Tile** and follow the in-app text guide.
4. Add the **Volume Panel** tile in Android Quick Settings.
5. Optionally set up **Edge Swipe Overlay** from its Config button.

Detailed tile steps with screenshots:
[`docs/quick-settings-tile.md`](quick-settings-tile.md)

## Daily Usage

- Tap the **Volume Panel** tile from Quick Settings to open volume controls.
- If Edge Swipe Overlay is enabled, swipe inward from the configured edge.
- Use each feature card's **Config** button for feature-specific setup.

## Troubleshooting

- **Edge Swipe card is unavailable**
  - Your device is likely using gesture navigation.
  - Switch to button navigation in Android Settings, then reopen the app.
- **Edge Swipe does not trigger**
  - Confirm overlay permission is granted.
  - Confirm the Edge Swipe toggle is ON.
- **Quick Settings tile is missing**
  - Reopen Quick Settings edit mode and check available tiles again.
