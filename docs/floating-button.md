# $\textcolor{#6F42C1}{\textsf{Floating Button Overlay}}$

The **Floating Button** provides a persistent and movable on-screen trigger for volume controls.

---

## $\textcolor{#0078D4}{\textsf{How It Works}}$

1.  **Overlay:** A compact button floats above all applications.
2.  **Repositioning:** Long-press until haptic feedback occurs, then drag to any location.
3.  **Action:** A simple tap triggers the native Android volume panel.
4.  **Persistence:** The button's position is automatically saved and restored.

---

## $\textcolor{#28A745}{\textsf{Setup and Configuration}}$

- **Toggle:** Enable or disable the feature from the Home screen.
- **Customization:** Tap **Config** on the feature card to:
    - Select from multiple icon designs.
    - Adjust icon color and opacity levels.
    - **Fixed Position:** You can lock the button to prevent accidental movement. First position the floating button where you want it, then enable Fixed Position and save the configuration. To reposition a locked button, disable Fixed Position, move the button, and enable it again.
    - **Separate Portrait/Landscape Positions:** You can enable separate positions for portrait and landscape orientations (OFF by default). When enabled, the button remembers its location for each orientation separately. If landscape is not yet saved, it falls back to the portrait position.

---

## $\textcolor{#D9730D}{\textsf{Technical Details}}$

- **Service:** Powered by `FloatingButtonService`, running as a foreground service for stability.
- **Preferences:** Coordinates, icon selection, and opacity are stored locally in AppPreferences.
- **Haptics:** Uses standard system feedback for interactions.

---

## $\textcolor{#D00000}{\textsf{🔍 Troubleshooting}}$

- **Button missing:** Ensure the toggle is **ON** and **Overlay** permission is granted.
- **Cannot move:** Remember to **long-press** (~500ms) before dragging.
- **Low visibility:** Increase the **Opacity** slider in the Config menu.
