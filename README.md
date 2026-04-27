# DhwaniControl

DhwaniControl helps you open Android's native volume panel without hardware volume buttons.

Version `0.2.0` introduces a modular feature hub with isolated systems so each feature can evolve independently.

## What it does in v0.2.0

- Uses a **Home feature hub** as the app entry.
- Provides two independent systems:
  - **Quick Settings Tile** (primary): add a tile and tap it to open Android volume controls.
  - **Edge Swipe Overlay** (secondary): swipe inward from a chosen edge to open Android volume controls.
- Keeps feature logic isolated, so a problem in one feature does not block the other.
- Uses shared permission orchestration before feature configuration.
- Redirects users to app settings when Android stops showing repeated notification permission prompts.
- Keeps edge-swipe service restart support after reboot (if configured and enabled).

## Install

[![Download DhwaniControl APK](https://img.shields.io/badge/APK_v0.2.0-Download-blue?logo=android&style=for-the-badge)](https://github.com/RajnishKMehta/DhwaniControl/releases/download/v0.2.0/DhwaniControl.apk)

Detailed install and setup steps are in [`docs/setup.md`](docs/setup.md).

## Usage

1. Open the app and land on the Home feature hub.
2. Configure **Quick Settings Tile** from its `Config` button and add the tile in Android Quick Settings.
3. Optionally configure **Edge Swipe Overlay** from its `Config` button.
4. Edge Swipe on/off switch becomes active only after first-time edge configuration.

## Requirements

- Android 10 (API 29) or newer.
- Overlay permission for Edge Swipe Overlay.
- Notification permission on Android 13+ for the edge foreground service.

## Privacy

- No analytics.
- No tracking.
- No external data upload by the app.
- Local preferences only.

## Tech

- Kotlin + XML + ViewBinding.
- Single Android module.
- Modular feature architecture (`FeatureSpec`, `FeatureController`, `FeatureRegistry`).
- Foreground service for Edge Swipe Overlay.
- Android Quick Settings TileService support.

## License

Apache-2.0. See [LICENSE](LICENSE).
