<img width="600" height="316" alt="video-scoretask_resize" src="https://github.com/user-attachments/assets/5614c5e1-24dd-4d7c-8206-7136991d69e6" />
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
* **Solution:** Applied **State Hoisting** to separate UI from state management. Transformed Composables into stateless components that
 receive a single `UiState` object and emit user actions (Intents), making the UI fully testable and previewable.


## 🎬 App Flow Demo & Screenshots

<p align="center">
<img width="300"  alt="video-scoretask_resize" src="https://github.com/user-attachments/assets/730a22f2-b8b0-4e14-9fd2-b97a4f687718" />
</p>

|<img src="https://github.com/user-attachments/assets/0268158c-b956-4834-86c0-b10173018b95" width="230"/> | <img src="https://github.com/user-attachments/assets/3fec561e-af18-4b2d-840d-f10c8e844684" width="230"/> | <img src="https://github.com/user-attachments/assets/dfc533cf-477c-4b5d-9231-91c6acf4a5da" width="230"/> |<img width="230"  alt="Image" src="https://github.com/user-attachments/assets/92bcc467-c1e5-4045-9a09-821adb058aa8" /> | <img width="230" alt="Image" src="https://github.com/user-attachments/assets/25d0c5c9-51f4-4fd6-b799-a0e2a2b7c989" /> | <img width="230" alt="Image" src="https://github.com/user-attachments/assets/16fa7936-5f0a-4e77-a2c2-c9cee6deb010" /> | <img width="230" alt="Image" src="https://github.com/user-attachments/assets/208c7749-801b-4972-8270-347b091c1032" /> | <img width="230" alt="Image" src="https://github.com/user-attachments/assets/488d0c4c-8deb-47e0-8a9e-07efb27771a0" />  
















