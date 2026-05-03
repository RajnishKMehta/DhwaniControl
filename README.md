# DhwaniControl

DhwaniControl helps you open Android's native volume panel without hardware volume buttons.

Version `0.4.0` migrates Edge Swipe to Accessibility mode and adds reusable feature blocking.

## What it does in v0.4.0

- Uses a Home feature hub as the main entry.
- Provides two independent systems:
  - **Quick Settings Tile** (primary): add a tile and tap it to open Android volume controls.
  - **Edge Swipe (Accessibility)** (secondary): use Accessibility-powered inward edge swipes to open Android volume controls.
- Adds a reusable per-feature block system with reason messaging and automatic forced-off behavior for blocked toggles.
- Blocks Edge Swipe when gesture navigation mode is active (`navigation_mode = 2`).
- Uses Android's Accessibility settings flow instead of overlay and notification permissions for Edge Swipe.
- Saves Quick Settings tutorial screenshots in app-private storage after first download.
- Hides screenshot URLs when the saved image is visible, and shows a small View Image fallback only when the image cannot be displayed.

## Install

[![Download DhwaniControl APK](https://img.shields.io/badge/APK_v0.4.0-Download-blue?logo=android&style=for-the-badge)](https://github.com/RajnishKMehta/DhwaniControl/releases/download/v0.4.0/DhwaniControl.apk)

## Documentation

- Setup and install: [`docs/setup.md`](docs/setup.md)
- Detailed tile tutorial: [`docs/quick-tile-guide.md`](docs/quick-tile-guide.md)

## Usage

1. Open the app.
2. Configure **Quick Settings Tile** from its `Config` button.
3. Add **Volume Panel** tile in Android Quick Settings.
4. Optionally configure **Edge Swipe (Accessibility)**.

## Requirements

- Android 10 (API 29) or newer.
- Accessibility permission for Edge Swipe.
- Edge Swipe is blocked on Android 10-11 and while gesture navigation mode is active.
- If navigation mode cannot be read on a device, DhwaniControl falls back to `0` and does not block Edge Swipe for that reason.
- Internet access only for downloading tutorial screenshots into app-private storage. No storage permission is required.

## Privacy

- No analytics.
- No tracking.
- No external data upload.
- Local preferences only.

## Tech

- Kotlin + XML + ViewBinding.
- Single Android module.
- Modular feature architecture (`FeatureSpec`, `FeatureController`, `FeatureRegistry`).
- Accessibility service for Edge Swipe detection.
- Android Quick Settings `TileService` support.
- App-private tutorial image storage under Android's internal files directory.

## License

Apache-2.0. See [LICENSE](LICENSE).
