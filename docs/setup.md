# Setup Guide

Hello, I am Rajnish. This tutorial is meant for people who simply wish to **download and utilize** the DhwaniControl app on their mobile device. There is no requirement to have an Android Studio or anything related to programming here.

## Before you start

Make sure your phone is on **Android 10 or newer**. Most phones from 2019 onwards are fine. (Settings → About phone → Android version.)

That's the only requirement.

## Step 1 — Download the APK

[![Download DhwaniControl APK](https://img.shields.io/badge/APK_v0.1.0-Download-blue?logo=android&style=social)](https://github.com/RajnishKMehta/DhwaniControl/releases/download/v0.1.0/DhwaniControl.apk)

Your browser may show a warning that this kind of file can harm your device. That warning is normal for any APK that doesn't come from the Play Store. Tap **Download anyway** / **Keep**.

## Step 2 — Allow installs from your browser (one-time)

Android blocks APK installs from outside the Play Store by default. You only need to do this once.

1. Open the downloaded file (from your notification shade or the Files / Downloads app).
2. Android will show a popup saying installs from this source are not allowed. Tap **Settings**.
3. Toggle on **Allow from this source** for whichever app you used to open the file (usually Chrome, or your Files app).
4. Go back. The installer will continue.

The exact wording differs a bit between OEMs (Xiaomi, Samsung, Realme, etc.) but the flow is the same.

## Step 3 — Install

Tap **Install**. Once it finishes, tap **Open**. You'll land on the permission screen.

## Step 4 — Grant the permissions

The first screen lists two permissions. Both are needed for the app to work.

### a) Draw over other apps

This is required so the invisible edge strip can sit on top of whatever app you're using.

1. Tap **Grant** next to **Draw over other apps**.
2. Android will open a system settings page. Toggle **Allow display over other apps** on for **DhwaniControl**.
3. Press the back gesture / button to return to the app.

### b) Notifications (only asked on Android 13 and newer)

The app runs as a foreground service. Android requires a visible notification for that, otherwise the system kills the service after a few seconds.

1. Tap **Grant** next to **Notifications**.
2. Tap **Allow** in the popup.

On Android 12 and below this isn't asked you'll see "Not required on this Android version" and you can skip it.

When both show **Granted**, the **Continue** button at the bottom becomes active. Tap it.

## Step 5 — Pick your edge

You'll see a screen that says "Choose active edge" with a counter at the bottom.

1. Determine which side you prefer; the *left* or the *right*.
2. Sweep inwards from the edge of the side that you choose, starting from the exact edge and moving towards the center.
3. Do this three times on the same side. This tiny band will light up and count how many times it has sensed the action; showing 1/3, 2/3, and 3/3 successively.
4. When the count reaches three, your selection becomes permanent and you are brought to the main screen.

If a swipe doesn't register, check that you're actually starting at the very edge, and that the swipe is mostly horizontal and long enough (around 40dp / about 1cm or more).

## Step 6 — That's it, try it

You should now see the home screen with:

- A switch labelled **Service enabled** — this is on by default.
- A card showing your **Active edge** (Left or Right) with a **Change** button if you want to switch later.
- A hint line telling you what to do.

Open any application – WhatsApp, web browser, YouTube, whatever you want. Swipe from one side to another. The regular Android panel for controlling the volume will appear. Use the slider the same way as usual.

## Notification

You'll see a small persistent notification while the service is running:

> **DhwaniControl is active**
> Swipe inward from your selected edge to open volume controls.

This sits at the bottom of your notification shade (priority is set to MIN so it stays out of the way) and has a **Stop** button. Tap **Stop** to turn the service off without opening the app.

## Things that can go wrong

- **Nothing happens when I swipe.** Open the app; if you find a yellow warning asking to turn on overlay permission, then activate it using the permissions screen. This permission setting may be turned off automatically by some skins for Android phones (like Xiaomi, Realme, Vivo, Oppo).
- **Service stops by itself in the background.** On Xiaomi / Realme / Vivo / Oppo / OnePlus, also do this:
  - Lock the app in your recents screen (long-press the recents card → lock icon).
  - Turn off battery optimization for DhwaniControl: **Settings → Apps → DhwaniControl → Battery → Unrestricted / No restrictions**.
  - On Xiaomi / Redmi: **Settings → Apps → DhwaniControl → Other permissions → Display pop-up windows while running in the background → Allow**, and **Autostart → On**.
- **Swipe is hard to trigger.** Start the swipe right at the very edge of the screen. The active strip is only about 5% of screen width.
- **I picked the wrong edge.** Open the app → tap **Change** on the Active edge card → do the 3-swipe setup again from the other side.
- **I want to turn it off temporarily.** Use the **Stop** button on the notification, or open the app and turn off the **Service enabled** switch.
- **It doesn't survive a reboot on my phone.** Make sure autostart is allowed for the app on your OEM (see the Xiaomi note above). On stock Android it should restart automatically.

## Updating the app

When a new release is out on GitHub, just download the new APK and install it the same way. Your settings (active edge, service on/off) are kept.

> Note: settings are not included in Google's cloud backup or device-to-device transfer. If you switch to a new phone, you'll redo the short setup once.

## Uninstalling

Long-press the app icon → **Uninstall**. Or **Settings → Apps → DhwaniControl → Uninstall**. Standard Android stuff. Nothing is left behind on your device.

---

That's it! If there's any difference from what you observe on your device, just make sure to create an issue in the repository and let me know what went wrong: which phone, which Android version, and at what step. It'll be fixed.

— Rajnish
