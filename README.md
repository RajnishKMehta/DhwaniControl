# DhwaniControl

DhwaniControl helps you open Android's native volume panel without hardware volume buttons.

Version `0.3.0` focuses on launch stability fixes, workflow cleanup, and improved Quick Settings tile guidance.

## What it does in v0.3.0

- Uses a Home feature hub as the main entry.
- Provides two independent systems:
  - **Quick Settings Tile** (primary): add a tile and tap it to open Android volume controls.
  - **Edge Swipe Overlay** (secondary): swipe inward from your selected edge to open Android volume controls.
- Stabilizes startup path to reduce splash-time crashes.
- Keeps edge service startup fail-safe to avoid process-level crash on service errors.
- Shows a short in-app Quick Settings tutorial with per-step screenshot URLs.

## Install

[![Download DhwaniControl APK](https://img.shields.io/badge/APK_v0.3.0-Download-blue?logo=android&style=for-the-badge)](https://github.com/RajnishKMehta/DhwaniControl/releases/download/v0.3.0/DhwaniControl.apk)

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
- Internet access only for loading tutorial screenshot URLs from links you provide.

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

## License

Apache-2.0. See [LICENSE](LICENSE).
