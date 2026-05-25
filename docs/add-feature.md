# $\textcolor{#D00000}{\textsf{Adding a New Feature}}$

This document outlines the architectural requirements and steps for integrating a new feature into **DhwaniControl**.

---

## $\textcolor{#D00000}{\textsf{Architecture Rules}}$

- **$\textcolor{#D00000}{\textsf{Controller:}}$** Every feature must implement a `FeatureController`.
- **$\textcolor{#D00000}{\textsf{Metadata:}}$** Feature details must be defined within a `FeatureSpec`.
- **$\textcolor{#D00000}{\textsf{Registry:}}$** All features must be registered in the `FeatureRegistry`.
- **$\textcolor{#D00000}{\textsf{Isolation:}}$** Keep feature logic contained within its own package.

---

## $\textcolor{#D00000}{\textsf{Implementation Steps}}$

1.  **$\textcolor{#D00000}{\textsf{Define ID:}}$** Add a unique feature ID constant in `Constants.kt`.
2.  **$\textcolor{#D00000}{\textsf{Package Creation:}}$** Create a new sub-package under `features/`.
3.  **$\textcolor{#D00000}{\textsf{Controller Implementation:}}$** Implement the `FeatureController` interface.
4.  **$\textcolor{#D00000}{\textsf{Spec Definition:}}$** Configure the `FeatureSpec` with name, summary, and requirements.
5.  **$\textcolor{#D00000}{\textsf{Registration:}}$** Add your controller to the `FeatureRegistry`.
6.  **$\textcolor{#D00000}{\textsf{UI Strings:}}$** Add necessary string resources for the title and description.
7.  **$\textcolor{#D00000}{\textsf{Documentation:}}$** Create or update a `.md` guide in the `docs/` folder.

---

## $\textcolor{#D00000}{\textsf{Validation Checklist}}$

- [ ] Feature card appears correctly on the Home screen.
- [ ] Toggle and Config buttons function as expected.
- [ ] Permissions are handled gracefully.
- [ ] No regressions introduced in existing features.
