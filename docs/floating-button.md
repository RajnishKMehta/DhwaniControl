# Floating Button Overlay

The Floating Button feature provides a constant, movable point of access to your device's volume controls.

## How it works

1.  **Overlay:** A small button floats above other applications.
2.  **Repositioning:** Long-press the button until you feel a haptic feedback, then drag it to any location on the screen.
3.  **Action:** A simple tap on the button triggers the native Android volume panel.
4.  **Persistence:** The position of the button is saved automatically. If you disable and re-enable the feature, or reboot your device, the button will reappear at the last saved location.

## Setup & Configuration

- **Enable/Disable:** Use the toggle on the home screen.
- **Permissions:** 
    - **Display over other apps:** Necessary to show the button on top of other apps.
    - **Notifications:** Required to keep the service running in the background reliably.

## Technical Details

- **Service:** `FloatingButtonService` handles the `WindowManager` logic and touch events.
- **Preferences:** Coordinates are stored in `SharedPreferences` as `feature.floating.x` and `feature.floating.y`.
- **Haptics:** Uses `VIRTUAL_KEY` feedback for taps and `LONG_PRESS` feedback for initiating drags.
