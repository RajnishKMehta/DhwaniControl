# $\textcolor{#24292E}{\textsf{Adding a New Feature}}$

This document outlines the architectural requirements and steps for integrating a new feature into **DhwaniControl**.

---

## $\textcolor{#6F42C1}{\textsf{Architecture Rules}}$

- **Controller:** Every feature must implement a `FeatureController`.
- **Metadata:** Feature details must be defined within a `FeatureSpec`.
- **Registry:** All features must be registered in the `FeatureRegistry`.
- **Isolation:** Keep feature logic contained within its own package.

---

## $\textcolor{#28A745}{\textsf{Implementation Steps}}$

1.  **Define ID:** Add a unique feature ID constant in `Constants.kt`.
2.  **Package Creation:** Create a new sub-package under `features/`.
3.  **Controller Implementation:** Implement the `FeatureController` interface.
4.  **Spec Definition:** Configure the `FeatureSpec` with name, summary, and requirements.
5.  **Registration:** Add your controller to the `FeatureRegistry`.
6.  **UI Strings:** Add necessary string resources for the title and description.
7.  **Documentation:** Create or update a `.md` guide in the `docs/` folder.

---

## $\textcolor{#008080}{\textsf{Validation Checklist}}$

- [ ] Feature card appears correctly on the Home screen.
- [ ] Toggle and Config buttons function as expected.
- [ ] Permissions are handled gracefully.
- [ ] No regressions introduced in existing features.
