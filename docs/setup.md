# Setup Guide (v0.2.2)

This guide covers installation and first-time setup for DhwaniControl.

## Before You Start

- Android 10 (API 29) or newer.

## Step 1: Download APK

[![Download DhwaniControl APK](https://img.shields.io/badge/APK_v0.2.2-Download-blue?logo=android&style=social)](https://github.com/RajnishKMehta/DhwaniControl/releases/download/v0.2.2/DhwaniControl.apk)

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

Detailed step-by-step tutorial with screenshot URL placeholders:
[`docs/quick-tile-guide.md`](quick-tile-guide.md)

## Step 5: Configure Edge Swipe Overlay (Optional)

1. On Home, tap **Config** in **Edge Swipe Overlay**.
2. Grant required permissions.
3. For first-time setup, swipe inward from your chosen edge 3 times.
4. After setup, the feature switch becomes available on Home.

## Permission Behavior

- If required permissions are missing, DhwaniControl opens the permission flow for that feature.
- On Android 13+, if notification permission has been denied repeatedly and Android stops showing prompts, DhwaniControl redirects to app settings.

## Troubleshooting

- App closes immediately on open:
  - Install the latest release (`v0.2.2` or newer).
  - Reinstall once if the install was upgraded from an older build.
- Edge swipe not working:
  - Verify overlay permission is granted.
  - Verify Edge Swipe switch is ON on Home.
- Tile not visible in edit list:
  - Confirm DhwaniControl is installed.
  - Close and reopen Quick Settings edit mode.
