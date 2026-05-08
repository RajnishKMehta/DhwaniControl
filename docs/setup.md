# Setup Guide

This guide covers installation and first-time setup for DhwaniControl.

## Before You Start

- Android 10 (API 29) or newer.

## Step 1: Download APK

[![Download DhwaniControl APK](https://img.shields.io/badge/APK_v0.4.0-Download-blue?logo=android&style=social)](https://github.com/RajnishKMehta/DhwaniControl/releases/download/v0.4.0/DhwaniControl.apk)

## Step 2: Allow Install From Source (One-Time)

1. Open the downloaded APK.
2. If Android blocks install, tap **Settings**.
3. Enable **Allow from this source** for your browser or file manager.
4. Return and continue install.

## Step 3: Open the App

After install, open DhwaniControl.

You will land on the Home feature hub.

## Step 4: Configure Quick Settings Tile (Primary)

1. On Home, tap **Config** in **Quick Settings Tile**.
2. Follow the in-app short tutorial.
3. Add the **Volume Panel** tile in Android Quick Settings.
4. Tap the tile to open Android volume controls.

Detailed step-by-step tutorial with screenshots:
[`docs/quick-tile-guide.md`](quick-tile-guide.md)

The in-app tutorial downloads screenshots into app-private storage and does not need storage permission. Screenshot links and the View Image fallback are shown only if a guide image cannot be displayed.

## Step 5: Configure Edge Swipe Overlay (Optional)

> **Note:** Edge Swipe Overlay requires **button navigation** (3-button or 2-button). If your device is set to gesture navigation, the feature card will show a blocked reason and all actions will be disabled until you switch navigation mode in **Settings > Gestures > Navigation mode**.

1. On Home, tap **Config** in **Edge Swipe Overlay**.
2. Grant overlay and notification permissions when prompted.
3. For first-time setup, swipe inward from your chosen edge 3 times to confirm the side.
4. After setup completes, the feature turns on automatically and the overlay becomes active.

If you later want to switch the active edge, tap **Config** again — the change applies immediately without restarting anything.

## Permission Behavior

- If required permissions are missing, DhwaniControl opens the permission flow for that feature.
- On Android 13+, if notification permission has been denied repeatedly and Android stops showing prompts, DhwaniControl redirects to app settings.
- If a feature is blocked by a device condition (e.g., gesture navigation), the permission screen also shows the blocked reason and keeps the Continue button disabled.

## Troubleshooting

- App closes immediately on open:
  - Install the latest release (`v0.4.0` or newer).
  - Reinstall once if the install was upgraded from an older build.
- Edge swipe card is greyed out and shows a blocked message:
  - Your device is using gesture navigation. Go to **Settings > Gestures > Navigation mode** and switch to 3-button or 2-button navigation.
- Edge swipe not working after setup:
  - Verify overlay permission is granted.
  - Verify the Edge Swipe switch is ON on Home.
  - Swipe slowly and start from the very edge of the screen.
- Edge swipe side did not change after saving:
  - The change applies immediately. If the overlay still feels wrong, toggle the switch off and on once to force a full restart of the service.
- Tile not visible in edit list:
  - Confirm DhwaniControl is installed.
  - Close and reopen Quick Settings edit mode.
