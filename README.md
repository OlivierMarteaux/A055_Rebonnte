# 📱 Rebonnte - a medicines supply chain management app

<div style="margin-left:0px; transform: scale(1.5); transform-origin: left;">
  <a href="https://sonarcloud.io/summary/new_code?id=OlivierMarteaux_A055_Rebonnte">
    <img
      src="https://sonarcloud.io/api/project_badges/measure?project=OlivierMarteaux_A055_Rebonnte&metric=coverage"
      alt="Quality Gate"
    />
  </a>
</div>

<br>

<div style="margin-left: 100px;">
  <img src="app/src/main/res/drawable/rebonnte_logo.png" width="240" alt="Rebonnte Logo"/>
</div>

## 🚀 About the Project

Rebonnte is an Android application dedicated to manage the supply chain for a medicines - pharmaceutics factory.  
The application is build following modern Android coding standard (Kotlin language, Jetpack Compose layout and MVVM architecture).
It uses Firebase as authentication Api, database storage and CD app distribution.

The project was developed as part of an educational and demonstration initiative showcasing how to structure Android apps with strong architectural boundaries.

## ✨ Features

👤 User Authentication – Login with Google or Email, registration, and password reset via Firebase Auth.

🏠 Aisles and Medicines browsers – Displays data dynamically paged and updated from Firestore.

➕ Add Aisles and Medicines – Users can create and publish new aisles and medicines with all details.

⚙️ Update and delete medicines - Users can modify medicine stock or delete medicine.

🔔 Historic change record - Each medicine modification performed by connected users are recorded and displayed on detail screen.

🧭 Navigation Graph – Declarative, type-safe navigation between screens.

🌙 Material 3 UI Design – Fully Compose-based adaptive theming with rounded shapes, elevation, and animations.

📊 Continuous Integration via GitHub Action + SonarCloud

🔥 Continuous Delivery via GitHub Action + Firebase App Distribution


## 🧰 Tech Stack

| Layer                | Technology                               |
|----------------------|------------------------------------------|
| Language             | Kotlin                                   |
| UI                   | Jetpack Compose, Material 3              |
| Architecture         | MVVM, ViewModel, State Management        |
| Navigation           | Jetpack Compose Navigation               |
| Authentication       | Firebase Auth, Google Credential         |
| Backend Storage      | Firebase FireStore                       |
| Crash Analyses       | Firebase CrashLytics                     |
| Background Work      | Kotlin Coroutines, Flows                 |
| Dependency Injection | Dagger/Hilt                              |
| Build                | Gradle (KTS)                             |
| Testing              | JUnit4, MockK, Espresso, Cucumber, Jacoco |
| CI                   | GitHub Action, SonarCloud                |
| CD                   | GitHub Action, Firebase App Distribution |


## 📂 Project Structure

```
A055_Rebonnte/
├── data/
│   ├── repository/           # Repositories wrapping data sources
│   └── service/              # Firebase API implementations (MedicineApi, AisleApi)
├── domain/
│   ├── model/                # Business entities (Aisle, Medicine, MEdicineChange)
│   └── mapper/               # DTO ↔ Domain model mappers
├── ui/
│   ├── screen/               # Compose screens (AisleList, MedicineList, etc)
│   ├── navigation/           # NavGraph and route definitions
│   └── components/           # Shared Compose UI components
├── di/                       # Hilt modules and providers
├── RebonnteApp.kt          # Top-level composable
├── RebonnteApplication.kt
└── MainActivity.kt
```

<!--
## 📲 Install

Coming soon.



To install the Rebonnte application on your physical Android device:

1. **Download the APK from your smartphone**
   - Go to the [Releases](https://github.com/OlivierMarteaux/A052_HexagonalGames/releases) section of this repository.
   - Download the latest ` HexagonalGames.apk ` file.

2. **Enable Unknown Sources**
   - On your device, go to `Settings` > `Security`.
   - Enable **Install from unknown sources** (you can disable it again after installation).

3. **Install the APK**
   - Use a file explorer app on your device to locate the APK file.
   - Tap on it and follow the prompts to install the app.

4. **Launch the App**
   - Once installed, open the app from your launcher and start using Hexagonal Games!

> ⚠️ Note: You may need to allow permissions during the first launch.

--> 


## ⚙️ Setup

1. Create a Firebase project and enable:

    - Authentication (Email/Password)

    - Firestore Database

    - Firebase App Distribution

2. Download google-services.json and place it in
   app/src/main/

3. (Optional) Set your own API keys in local.properties or a secure Gradle config – never commit secrets!

4. Build & Run 🚀


## 👨‍💼 Author

_Olivier Marteaux_  
https://oliviermarteaux.dev  
  
Former aerospace engineer turned Android developer.

Read more about my transition on [LinkedIn](https://linkedin.com/in/olivier-marteaux).  
Check out my journey and projects:
- 🔗 [Google Developer Profile](https://g.dev/OlivierMarteaux)
- 💻 [GitHub Projects](https://github.com/OlivierMarteaux)
- 📢 [LinkedIn Post – Career Change](https://www.linkedin.com/posts/olivier-marteaux_androidbasics-careerchange-androiddevelopment-activity-7351370158369628164-FmqZ?utm_source=share&utm_medium=member_desktop&rcm=ACoAACynrz8BkrhJFrStq3CEX6rQIEfnG7goFdg)


## 🤝 Acknowledgments

Special thanks to OpenClassrooms for providing the educational framework, and to the open-source community for libraries that make modern Android development elegant.

- [OpenClassrooms Android Pathway](https://openclassrooms.com/fr/paths/527/projects/1647/1900-mission---option-b,-scenario-fictif---creez-une-application-android-complexe)
- [Google Android Basics](https://developer.android.com/courses/android-basics-compose/course)
- JetBrains & Jetpack Compose Community


## 📄 License

This project is for educational and demonstration purposes. Not licensed for commercial use. For inquiries, please contact me.
