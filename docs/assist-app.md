# $\textcolor{#E83E8C}{\textsf{Assist App Integration}}$

The **Assist App** feature allows DhwaniControl to be set as your device's default Digital Assistant, providing a seamless way to open the system volume panel using built-in assistant triggers.

---

## $\textcolor{#0078D4}{\textsf{How It Works}}$

1.  **Role Assignment:** DhwaniControl requests the `ROLE_ASSISTANT` from the Android system.
2.  **Invocation:** Triggering the assistant (e.g., via voice gesture, home button long-press, or power button, depending on your device settings) launches the app.
3.  **Action:** The system volume panel is displayed instantly without keeping any background service active.

---

## $\textcolor{#28A745}{\textsf{Setup and Configuration}}$

1.  Open the DhwaniControl app and go to the **Assist App** card on the Home screen.
2.  If it shows **Disabled**, tap the toggle button.
3.  This action will redirect you to the Android **Default Apps** settings.
4.  Select **DhwaniControl** as your default Digital Assistant app.
5.  Invoke your assistant as usual to confirm the volume panel opens.

---

## $\textcolor{#D9730D}{\textsf{Technical Details}}$

- **Controller:** Managed by `AssistFeatureController`.
- **Role Checker:** Uses `AssistantRoleChecker` to query the `RoleManager` (available from Android Q/API 29 onwards) to determine if DhwaniControl is the active default assistant.
- **Entry Point:** The feature relies on `AssistEntryActivity` to intercept the `Intent.ACTION_ASSIST` intent. It uses the system `AudioManager` with `AudioManager.FLAG_SHOW_UI` to display the volume UI and immediately finishes, ensuring minimal battery and memory impact.

---

## $\textcolor{#D00000}{\textsf{🔍 Troubleshooting}}$

- **Volume panel not appearing:** Ensure DhwaniControl is explicitly selected as your Digital Assistant in `Settings -> Apps -> Default Apps -> Digital Assistant`.
- **Settings screen does not open:** If the default apps setting screen cannot be launched automatically, navigate there manually via your device's system settings.
