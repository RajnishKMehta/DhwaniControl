# DhwaniControl

DhwaniControl helps you open Android's native volume panel without hardware volume buttons.

Version `0.4.0` introduces a feature blocking system, applies edge swipe side changes live, and sets edge swipe to off by default.

## What it does in v0.4.0

- Uses a Home feature hub as the main entry.
- Provides two independent systems:
  - **Quick Settings Tile** (primary): add a tile and tap it to open Android volume controls.
  - **Edge Swipe Overlay** (secondary): swipe inward from your selected edge to open Android volume controls.
- Ships a **feature blocking system** that detects device/state conditions and prevents incompatible features from running:
  - Edge Swipe Overlay is blocked when gesture navigation is active (navigation mode 2). The card shows the reason and disables config and toggle until the condition clears.
  - Blocked toggle features are automatically disabled at startup if they were previously left on.
- Applies edge swipe **side changes live** — switching left/right in Config now takes effect immediately without a service restart.
- Edge Swipe Overlay is **off by default** on fresh installs. After completing first-time setup, the feature turns on automatically.
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
4. Optionally configure **Edge Swipe Overlay** (requires button navigation, not gesture navigation).

## Requirements

- Android 10 (API 29) or newer.
- Overlay permission for Edge Swipe Overlay.
- Notification permission on Android 13+ for edge foreground service.
- Button navigation (3-button or 2-button) for Edge Swipe Overlay — gesture navigation blocks the feature.
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
- Feature blocking system (`FeatureBlockCondition`, `FeatureBlockResult`, `FeatureAvailabilityEvaluator`) — pure policy layer separate from UI and runtime.
- Foreground service for Edge Swipe Overlay.
- Android Quick Settings `TileService` support.
- App-private tutorial image storage under Android's internal files directory.

## License

Apache-2.0. See [LICENSE](LICENSE).
