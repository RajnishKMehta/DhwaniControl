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
