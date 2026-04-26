# DhwaniControl

Hello, I am Rajnish. I have developed this application since my volume buttons of the phone were not working, and I had to go into Settings each time I needed to adjust the volume.

With DhwaniControl, I can now **drag in from the side of the screen, regardless of what app I am using, to access the system volume control screen**. After that, all I need to do is drag the volume controller itself.

## What it does

- Sits in the background as a foreground service.
- Watches a thin invisible strip (around 5% of the screen width) on the edge you choose *left* or *right*.
- When you swipe inward from that edge, it opens Android's native volume panel using the same call the hardware buttons would use.
- Small vibration to confirm the gesture.
- Auto-starts after reboot if you had it on.
- Persistent low-priority notification with a **Stop** button (Android needs this for the service to keep running).

No custom volume UI, no extra sliders, nothing fancy. Just a shortcut to the panel that already exists on your phone.

## Install & setup

The simplest way is to grab the APK from the **Releases** page and install it on your phone. Step-by-step instructions are in [`docs/setup.md`](docs/setup.md).

## How to use it (once installed)

1. Launch the app.
2. Give permission to both: **Draw Over Other Apps** and **Show Notifications** (for Android 13 and above).
3. Choose which edge you want – just swipe inside from your desired edge 3 times.
4. That’s all! From now on, swipe inside from whichever edge and the volume popup appears.

This is all customizable by changing edges or stopping this service anytime via the home page of the app or notification.

## Requirements

- **Android *10*** (API 29) or newer.
- Permission to draw over other apps.
- Notification permission on Android 13+.

## Privacy

The app does not connect to the internet. No analytics, no tracking, nothing leaves your phone. Your edge choice is stored in local SharedPreferences and is excluded from cloud backup on purpose.

## Roadmap

The next big thing I want to add is a **Quick Settings Tile**. When that's done, the tile will become the **primary** way to use the app and the current edge-swipe overlay will continue as a **secondary** option for people who still prefer it. That's planned for a future update, it's not in this version.

A few smaller things I want to do later:
- Settings to adjust edge strip width and swipe sensitivity.
- Option to enable both edges at once.
- A short in-app tutorial on first launch.

## Tech

- Kotlin, single-module Android app.
- minSdk 29 (Android 10), targetSdk 36.
- Foreground service + system overlay window (`TYPE_APPLICATION_OVERLAY`).
- View Binding, Material Components.
- No third-party network libraries.

## License

Apache-2.0. See [LICENSE](LICENSE).

---

Made by **Rajnish Mehta** because my phone's volume button is broken. If it helps you too, that's a bonus.