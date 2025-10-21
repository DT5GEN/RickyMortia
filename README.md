# RickyMortia - Rick and Morty Characters App

A minimal Android application demonstrating integration with the public Rick and Morty API.  
The app displays a list of characters with pagination, search functionality, and detailed character information.

---

## 🚀 Features

- Fetches and displays characters from `https://rickandmortyapi.com/api/character`
- Paginated list of characters using **PagingSource**
- Search by name with instant update
- Character details screen with extended information
- Splash screen for Android 12+
- Basic loading and error states

---

## 🧰 Tech Stack

| Category | Technologies |
|-----------|--------------|
| **Language** | Kotlin 2.0.21 |
| **UI** | Jetpack Compose, Material 3 |
| **Navigation** | Navigation Compose |
| **Dependency Injection** | Hilt (via KSP) |
| **Networking** | Retrofit, Moshi, OkHttp (with logging) |
| **Database** | Room (via KSP) |
| **Image Loading** | Coil |
| **Pagination** | Paging 3 |
| **Minimum SDK** | 29 |
| **Compile SDK** | 36 |

---

## 🧩 Architecture

```
app/
 ├─ src/main/java/com/dt5gen/rickymortia/
 │   ├─ data/
 │   │   ├─ local/
 │   │   ├─ remote/
 │   │   └─ repositories/
 │   ├─ di/
 │   ├─ ui/
 │   │   ├─ screens/
 │   │   └─ theme/
 │   └─ viewmodel/
 └─ build.gradle.kts
```

---

## 🔧 Upcoming Improvements

- Replace **PagingSource** with **RemoteMediator** for full offline caching
- Implement pull-to-refresh with consistent state handling
- Add filtering options by status, species, and gender
- Add **DataStore** for persistent filters
- Improve UI animations, placeholders, and loading states

---

## 🎬 Demo

<p align="center">
  <img src="res/assets/demo.gif" alt="App demo" width="450" />
</p>

---

## 🏗️ How to Run

1. Open the project in **Android Studio Koala** or newer  
2. Ensure **JDK 17** is installed  
3. Sync Gradle and run the app  
4. Internet connection required for data fetching

---

© 2025 DT5GEN — RickyMortia Project
