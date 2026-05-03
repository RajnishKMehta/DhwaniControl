# DhwaniControl

DhwaniControl helps you open Android's native volume panel without hardware volume buttons.

Version `0.3.1` focuses on private guide image storage and cleaner Quick Settings tutorial fallbacks.

## What it does in v0.3.1

- Uses a Home feature hub as the main entry.
- Provides two independent systems:
  - **Quick Settings Tile** (primary): add a tile and tap it to open Android volume controls.
  - **Edge Swipe Overlay** (secondary): swipe inward from your selected edge to open Android volume controls.
- Stabilizes startup path to reduce splash-time crashes.
- Keeps edge service startup fail-safe to avoid process-level crash on service errors.
- Saves Quick Settings tutorial screenshots in app-private storage after first download.
- Hides screenshot URLs when the saved image is visible, and shows a small View Image fallback only when the image cannot be displayed.

## Install

[![Download DhwaniControl APK](https://img.shields.io/badge/APK_v0.3.1-Download-blue?logo=android&style=for-the-badge)](https://github.com/RajnishKMehta/DhwaniControl/releases/download/v0.3.1/DhwaniControl.apk)

## Documentation

- Setup and install: [`docs/setup.md`](docs/setup.md)
- Detailed tile tutorial: [`docs/quick-tile-guide.md`](docs/quick-tile-guide.md)

## Usage

1. Open the app.
2. Configure **Quick Settings Tile** from its `Config` button.
3. Add **Volume Panel** tile in Android Quick Settings.
4. Optionally configure **Edge Swipe Overlay**.

## Requirements

- Android 10 (API 29) or newer.
- Overlay permission for Edge Swipe Overlay.
- Notification permission on Android 13+ for edge foreground service.
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
- Foreground service for Edge Swipe Overlay.
- Android Quick Settings `TileService` support.
- App-private tutorial image storage under Android's internal files directory.

## License

Apache-2.0. See [LICENSE](LICENSE).
