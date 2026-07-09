# Signal Lost

Android investigation puzzle built with Kotlin and Jetpack Compose.

## Current state

This is the first project scaffold:

- Android app module: `app`
- Kotlin + Jetpack Compose
- MVVM/Clean-friendly package direction
- Room and kotlinx.serialization dependencies prepared
- First domain models added
- First JSON case draft added in `app/src/main/assets/cases`
- Case Archive screen added
- First case is loaded from JSON assets through a ViewModel
- Investigation screen added with Evidence, Timeline, and Crew tabs

## How to open

Open this folder in Android Studio:

```text
/Users/dana/Documents/Codex/2026-07-09/android-kotlin-pet-project-signal-lost
```

Then let Android Studio sync the Gradle project.

## Next development step

The next small iteration should be:

1. Add the first Hypothesis screen.
2. Let the player choose victim, suspect, location, time range, cause, method, and evidence.
3. Add a simple solution checker.
