# Setup Guide (v0.2.0)

This guide is for installing and using DhwaniControl on a phone.

## Before you start

- Android 10 or newer.

## Step 1: Download APK

[![Download DhwaniControl APK](https://img.shields.io/badge/APK_v0.2.0-Download-blue?logo=android&style=social)](https://github.com/RajnishKMehta/DhwaniControl/releases/download/v0.2.0/DhwaniControl.apk)

If Android warns about APK downloads from browser, continue with **Download anyway**.

## Step 2: Allow install from source (one-time)

1. Open the downloaded APK.
2. If blocked, tap **Settings**.
3. Enable **Allow from this source** for your browser or file manager.
4. Return and continue install.

## Step 3: Open app

After install, open DhwaniControl.

You now land on the **Home feature hub**.

## Step 4: Configure Quick Settings Tile (Primary)

1. On Home, open **Quick Settings Tile** and tap **Config**.
2. Follow the in-app instructions.
3. Open Android Quick Settings, enter edit mode, and add the **Volume Panel** tile.
4. Tap the tile to open Android volume controls.

## Step 5: Configure Edge Swipe Overlay (Secondary, optional)

1. On Home, open **Edge Swipe Overlay** and tap **Config**.
2. Grant required permissions when prompted.
3. For first-time setup, swipe inward from your chosen edge 3 times.
4. After setup, the feature switch becomes available on Home.

Reconfiguration later uses a simple left/right selector screen.

## Permission behavior

- If required permissions are missing, DhwaniControl opens a dedicated permission flow for that feature.
- On Android 13+, if notification permission has been denied multiple times and Android no longer shows the prompt, DhwaniControl sends you to app settings.

## Troubleshooting

- **Edge swipe not working:**
  - Verify overlay permission is allowed.
  - Verify Edge Swipe switch is ON on Home.
- **Service stops on some OEM devices:**
  - Disable battery restrictions for DhwaniControl.
  - Enable autostart where available.
- **Tile not visible in edit list:**
  - Ensure app is installed and reopen Quick Settings edit mode.

## Uninstall

- Long-press app icon and uninstall.
- Or use Android Settings > Apps > DhwaniControl > Uninstall.
