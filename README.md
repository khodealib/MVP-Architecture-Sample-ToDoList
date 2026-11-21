# ToDoList (Kotlin + MVVM + Compose + Hilt)

Modern Android sample for a simple to-do list.

## Stack
- Kotlin (AGP 9 built-in Kotlin)
- Jetpack Compose (Material 3)
- MVVM + Coroutines / Flow
- Room + KSP
- Hilt
- Navigation Compose
- Gradle 9.9+ / AGP 9.0.1

## Features
- Create, edit, and delete tasks
- Search tasks by title
- Toggle completion
- Importance levels (High / Normal / Low)
- Delete all tasks

## Android Studio setup
Use **Gradle JDK = jbr-25** (Embedded JDK from Android Studio), not an older JDK 11.

Settings → Build, Execution, Deployment → Build Tools → Gradle → Gradle JDK → `jbr-25`
