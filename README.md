# ComposeChatApp

A modern Android chat application built with **Kotlin**, **Jetpack Compose**, **Firebase Realtime Database**, and **Room**.  
The project demonstrates a clean MVVM architecture with offline-first support, dependency injection via **Hilt**, and reactive UI with **Coroutines + Flow**.

---

## 🏗 Architecture & Design Decisions

- **UI**: Jetpack Compose (Material 3) for a declarative, modern UI.  
- **Architecture**: MVVM + Clean layering (UI → ViewModel → Repository → Data).  
- **Dependency Injection**: Hilt for scoped injection of Firebase, Room, and repositories.  
- **Backend**: Firebase Realtime Database + Firebase Auth.  
- **Offline Support**: Room database caches both:
  - Messages per conversation.
  - User records (chat list).  
- **Coroutines/Flow**: Reactive streams for observing chats, messages, and auth state.  
- **Scroll Behavior**: Chat screen auto-scrolls to bottom with smooth animation.  

---

## ✅ Features

- **Authentication**  
  - Register & Login with Firebase Email/Password.  
- **Chats**  
  - View chat list with last message + timestamp.  
  - Open conversations and exchange messages in real time.  
- **Offline-first**  
  - Cached messages in Room (per conversation).  
  - Cached user records (chat list available offline).  
- **Extras implemented**  
  - In-memory cache utility for faster lookup.  
  - Reverse chat layout + auto-scroll to bottom.  
  - User presence handling stub (can be extended).  

---

## ⚠️ Known Issues / Future Improvements

- Push notifications (Firebase Cloud Messaging) not yet integrated.  
- No media (images, voice notes) support yet.  
- Presence indicator (online/offline) only stubbed, not fully implemented.  
- Error handling and retry strategies can be improved.  
- UI polish (animations, theming, dark mode) still minimal.
- Read receipt and unread message count not yet implement

---

## 📸 Screenshots

| Register Screen | Login Screen |
|-----------------|--------------|
| <img width="400" alt="registerscreen" src="https://github.com/user-attachments/assets/cc032933-97d0-4c21-a90a-1958758eaa25" /> | <img width="400" alt="loginscreen" src="https://github.com/user-attachments/assets/806f369d-6255-43e9-900f-a91c483e7e53" /> |

| Messages (Dark) | Messages (Light) | Chat List |
|-----------------|------------------|----------|
| <img width="400" alt="messageBoxDark" src="https://github.com/user-attachments/assets/0a48a3eb-e5f9-4958-84d9-433d3585312c" /> | <img width="400" alt="messageBoxLight" src="https://github.com/user-attachments/assets/bf8642e3-b7bc-46be-9bf4-98bf9232d0f5" /> | <img width="400" alt="Screenshot 2025-09-15 at 07 15 57" src="https://github.com/user-attachments/assets/58ddc0e5-d784-4144-9c96-7af2072a76bd" />
 |


---

## 🔑 Firebase Setup

> ⚠️ Do **not** commit your `google-services.json` file to the repo (it contains project secrets).  

1. Go to [Firebase Console](https://console.firebase.google.com/).  
2. Create a Firebase project.  
3. Add an **Android app** with your app’s package name.  
4. Download the `google-services.json` file and place it under:  
5. In Firebase console → **Authentication → Sign-in Method**, enable **Email/Password** login.  
6. In Firebase console → **Realtime Database**, create a database and set rules (start with test mode for dev).  

---

## ▶️ Run the App

1. Clone the repo.  
2. Add your `google-services.json` file under `/app`.  
3. Build & run with Android Studio (Arctic Fox or newer).  

---

## 📱 Tech Stack

- Kotlin, Jetpack Compose, Material 3  
- Hilt for Dependency Injection  
- Firebase Auth + Realtime Database  
- Room (for offline cache)  
- Coroutines + Flow  

---

## 📌 License

MIT License. Free to use and adapt.  

