# Floating Button Overlay

The Floating Button feature provides a constant, movable point of access to your device's volume controls.

## How it works

1.  **Overlay:** A small button floats above other applications.
2.  **Repositioning:** Long-press the button until you feel a haptic feedback, then drag it to any location on the screen.
3.  **Action:** A simple tap on the button triggers the native Android volume panel.
4.  **Persistence:** The position of the button is saved automatically. If you disable and re-enable the feature, or reboot your device, the button will reappear at the last saved location.

## Setup & Configuration

- **Enable/Disable:** Use the toggle on the home screen.
- **Customization:** Tap **Config** on the floating button card to:
    - Choose from multiple icon designs (Volume Up, Volume Down, Sound, etc.).
    - Change the icon color to match your preference or theme.
- **Permissions:** 
    - **Display over other apps:** Necessary to show the button on top of other apps.
    - **Notifications:** Required to keep the service running in the background reliably.

## Technical Details

- **Service:** `FloatingButtonService` handles the `WindowManager` logic and touch events. It runs as a foreground service to ensure it is not killed by the system.
- **Preferences:** 
    - Coordinates: `feature.floating.x` and `feature.floating.y` (stored as percentage of screen width/height for cross-rotation consistency).
    - Icon Name: `feature.floating.icon_name` (references vector drawable resources).
    - Icon Color: `feature.floating.icon_color` (stored as Hex ARGB).
    - Opacity: `feature.floating.opacity` (ranges from 0.1 to 1.0).
- **Haptics:** Uses `VIRTUAL_KEY` feedback for taps and `LONG_PRESS` feedback for initiating drags.

## 🔍 Troubleshooting

- **Button is missing:**
    - Ensure the feature is toggled **ON** in the Home screen.
    - Check if **Display over other apps** permission is still granted.
    - If it disappears during heavy use, check [Battery Optimization](user-guide.md#battery-optimization).
- **Cannot move the button:**
    - You must **Long-press** (hold for ~500ms) until you feel a vibration before dragging.
- **Button is too transparent:**
    - Go to **Config** and increase the **Opacity** slider.
