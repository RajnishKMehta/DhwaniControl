# DhwaniControl

DhwaniControl helps you open Android's native volume panel without using hardware volume buttons.

The app uses a feature-driven architecture where each feature is isolated behind its own controller and metadata.

## Features

- **Quick Settings Tile**: Add a tile and tap it to open Android volume controls.
- **Edge Swipe Overlay**: Swipe inward from your selected edge to open Android volume controls.

## Install

[![Download DhwaniControl APK](https://img.shields.io/badge/APK_v0.5.0-Download-blue?logo=android&style=for-the-badge)](https://github.com/RajnishKMehta/DhwaniControl/releases/download/v0.5.0/DhwaniControl.apk)

## Documentation

- User setup and daily usage: [`docs/user-guide.md`](docs/user-guide.md)
- Feature overview and requirements: [`docs/features.md`](docs/features.md)
- Quick Settings Tile walkthrough with screenshots: [`docs/quick-settings-tile.md`](docs/quick-settings-tile.md)
- Edge Swipe Overlay setup and behavior: [`docs/edge-swipe-overlay.md`](docs/edge-swipe-overlay.md)
- How to add a new feature in code: [`docs/add-feature.md`](docs/add-feature.md)
- Contributing guide: [`CONTRIBUTING.md`](CONTRIBUTING.md)

## Tech

- Kotlin + XML + ViewBinding
- Single Android module
- Feature contracts under `core/feature`
- Availability and blocking policy under `core/block`
- Permission checks under `core/permission`

## License

Apache-2.0. See [LICENSE](LICENSE).
