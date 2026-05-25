# $\textcolor{#D00000}{\textsf{Floating Button Overlay}}$

The **Floating Button** provides a persistent and movable on-screen trigger for volume controls.

---

## $\textcolor{#D00000}{\textsf{How It Works}}$

1.  **$\textcolor{#D00000}{\textsf{Overlay:}}$** A compact button floats above all applications.
2.  **$\textcolor{#D00000}{\textsf{Repositioning:}}$** Long-press until haptic feedback occurs, then drag to any location.
3.  **$\textcolor{#D00000}{\textsf{Action:}}$** A simple tap triggers the native Android volume panel.
4.  **$\textcolor{#D00000}{\textsf{Persistence:}}$** The button's position is automatically saved and restored.

---

## $\textcolor{#D00000}{\textsf{Setup & Configuration}}$

- **$\textcolor{#D00000}{\textsf{Toggle:}}$** Enable or disable the feature from the Home screen.
- **$\textcolor{#D00000}{\textsf{Customization:}}$** Tap **Config** on the feature card to:
    - Select from multiple icon designs.
    - Adjust icon color and opacity levels.

---

## $\textcolor{#D00000}{\textsf{Technical Details}}$

- **$\textcolor{#D00000}{\textsf{Service:}}$** Powered by `FloatingButtonService`, running as a foreground service for stability.
- **$\textcolor{#D00000}{\textsf{Preferences:}}$** Coordinates, icon selection, and opacity are stored locally in AppPreferences.
- **$\textcolor{#D00000}{\textsf{Haptics:}}$** Uses standard system feedback for interactions.

---

## $\textcolor{#D00000}{\textsf{🔍 Troubleshooting}}$

- **$\textcolor{#D00000}{\textsf{Button missing:}}$** Ensure the toggle is **ON** and **Overlay** permission is granted.
- **$\textcolor{#D00000}{\textsf{Cannot move:}}$** Remember to **long-press** (~500ms) before dragging.
- **$\textcolor{#D00000}{\textsf{Low visibility:}}$** Increase the **Opacity** slider in the Config menu.
