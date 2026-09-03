# Time Estimation App

> **A productivity tool designed to train user time estimation accuracy through structured focus sessions and detailed time analytics.**

---

## 🎯 Key Features

* ⏱️ **Time Estimation Training:** Helps users build realistic time estimation habits for new and daily tasks.
* 📊 **Multi-Level Analytics:** Displays visual progress and accuracy statistics across daily, weekly, and monthly views.
* 🎯 **Partial Focus Tracking:** Accurately logs focused work time even if a session is ended before completion.
* ⏳ **One-Time Extension:** Allows a single session extension to accommodate unexpected task overruns gracefully.
* 📈 **Daily Performance Feedback:** Provides actionable user feedback and calculates daily task completion rates.

  ------
## 🛠️ Tech Stack & Architecture

* **Language:** Kotlin
* **UI Framework:** Jetpack Compose (Declarative UI)
* **Architecture:** MVI (Model-View-Intent) 
* **Navigation:** Navigation Compose (Single-Activity Architecture) 

-------


## 💡 Technical Decisions & Challenges 

* **Why MVI Architecture?**
  Selected because the app relies on a unified UI State object. MVI ensures that timer updates, session progress, and performance metrics update atomically in the UI without race conditions or conflicting view states.

* **Challenge:** High state coupling inside UI components made individual screens difficult to unit test and maintain.
* **Solution:** Applied **State Hoisting** to separate UI from state management. Transformed Composables into stateless components that receive a single `UiState` object and emit user actions (Intents), making the UI fully testable and previewable.
