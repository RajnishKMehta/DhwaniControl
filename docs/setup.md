# Setup Guide

This guide covers installation and first-time setup for DhwaniControl.

## Before You Start

- Android 10 (API 29) or newer.
- Edge Swipe runs in Accessibility mode and is blocked on Android 10-11 and during gesture navigation mode.
- If navigation mode cannot be detected on a device, DhwaniControl defaults it to `0` and does not block Edge Swipe for that reason.

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

Detailed step-by-step tutorial with screenshot URL placeholders:
[`docs/quick-tile-guide.md`](quick-tile-guide.md)

The in-app tutorial downloads screenshots into app-private storage and does not need storage permission. Screenshot links and the View Image fallback are shown only if a guide image cannot be displayed.

## Step 5: Configure Edge Swipe (Accessibility) (Optional)

1. On Home, tap **Config** in **Edge Swipe (Accessibility)**.
2. Open Accessibility settings and enable the DhwaniControl accessibility service.
3. For first-time setup, swipe inward from your chosen edge 3 times.
4. After setup, the feature switch becomes available on Home.

## Permission Behavior

- If required permissions are missing, DhwaniControl opens the permission flow for that feature.
- If Edge Swipe is blocked, the card shows a reason and disables config/toggle until the block condition is gone.

## Troubleshooting

- App closes immediately on open:
  - Install the latest release (`v0.4.0` or newer).
  - Reinstall once if the install was upgraded from an older build.
- Edge swipe not working:
  - Verify Accessibility service is enabled for DhwaniControl.
  - Verify Edge Swipe switch is ON on Home.
- Tile not visible in edit list:
  - Confirm DhwaniControl is installed.
  - Close and reopen Quick Settings edit mode.
