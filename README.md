# Arith-Matic

**Arith-Matic** is a native Android application written in Kotlin that helps children build arithmetic fluency through an engaging, classroom-inspired experience. Rather than feeling like a traditional worksheet, Arith-Matic presents customizable practice sessions with a clean chalkboard aesthetic, immediate feedback, and a distraction-free interface designed specifically for young learners.

---

## Features

### Arithmetic Practice

- Addition
- Subtraction
- Multiplication
- Division
- Configurable operation selection

### Customizable Practice

- Select one or more operations
- Adjustable whole-number size
- Optional negative numbers
- Optional decimal numbers
- Configurable question count

### Child-Friendly Design

- Classroom/chalkboard themed interface
- Large touch targets
- Colored chalk UI elements
- Minimal distractions
- Gentle feedback options (planned)

### Progress & Results

- Round summaries
- Accuracy tracking
- Completion time
- Practice again workflow

### Offline

- Fully offline
- Local settings persistence using Android DataStore
- No account required
- No backend services

---

## Project Structure

```plaintext
app/
└── src/
    └── main/
        ├── java/com/wiseravenstudios/arithmatic/
        │   ├── domain/
        │   │   ├── config/
        │   │   ├── generator/
        │   │   ├── model/
        │   │   └── session/
        │   │
        │   ├── navigation/
        │   │
        │   ├── platform/
        │   │   ├── audio/
        │   │   ├── lifecycle/
        │   │   └── persistence/
        │   │
        │   ├── ui/
        │   │   ├── about/
        │   │   ├── appsettings/
        │   │   ├── common/
        │   │   ├── parent/
        │   │   ├── practice/
        │   │   ├── results/
        │   │   ├── roundsettings/
        │   │   ├── splash/
        │   │   ├── start/
        │   │   └── theme/
        │   │
        │   └── MainActivity.kt
        │
        ├── res/
        └── AndroidManifest.xml
```

---

## Current Status

Current implementation includes:

- Splash screen
- Classroom scene
- Start screen
- Navigation system
- Chalk-themed typography
- Reusable ChalkTextAction component
- Practice configuration model
- Practice configuration validation
- Round Settings screen (in progress)

---

## Planned Features

- Practice rounds
- Results screen
- App settings
- Parent area
- Audio feedback packs
- Chalk-style buttons
- Statistics and progress tracking
- Accessibility improvements
- Additional arithmetic modes
```
<img width="395" height="835" alt="{D8DFC6E7-29DD-48C3-AE8D-34467D0E0327}" src="https://github.com/user-attachments/assets/3468639d-12b1-4b37-97a3-410ff58cbea3" />

<img width="392" height="826" alt="{212DE687-5431-4846-9A5A-F2852C5A5CE4}" src="https://github.com/user-attachments/assets/05c215c3-8524-47cf-91af-29cefd80b525" />


