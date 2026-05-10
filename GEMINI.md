# DhwaniControl Project Instructions

## General Rules
- **Language:** All code, comments, and documentation must be in English.
- **Modern Standards:** Use future-proof and latest Android development practices (Kotlin, Jetpack, etc.).
- **Professionalism:** Code must be highly optimized, clean, and professional.
- **Git Safety:** Never use `git push` or `git commit` commands.

## App Overview
DhwaniControl was created to solve a common problem: physical volume buttons failing or becoming stuck. Instead of navigating deep into settings or dealing with a stuck button that resets volume to 0%, this app provides easy access to volume controls via Edge Swipe gestures and Quick Settings tiles.

## Project Structure
- `app/src/main/java/io/github/rajnishkmehta/dhwanicontrol/`: Main source code.
- `core/`: Core logic for features, permissions, and preferences.
- `features/`: Implementation of specific features (Edge Swipe, Quick Tiles).
- `home/`: Main UI logic for the feature dashboard.
- `info/`: App and developer information.

## Coding Conventions
- Prefer Kotlin coroutines for asynchronous tasks.
- Use Material Design 3 components and styling.
- Ensure all resources (strings, dimensions, colors) are externalized in `res/values`.
