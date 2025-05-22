# Arith-Matic 🧠📱

**Arith-Matic** is a Kotlin-based Android game designed to help children practice arithmetic, logic, and pattern recognition through an engaging, interactive experience. Built with a modular architecture and scalable design patterns, it aims to combine educational value with fun and adaptive gameplay.

---

## 🚀 Features

- 🎮 **Arithmetic Practice**: Addition, Subtraction, Multiplication, Division
- 🧩 **Logic Challenges**: Includes propositional logic questions (AND, OR, NOT, IMPLIES)
- 🧠 **Adaptive Difficulty**: Tracks player performance and streaks
- 📊 **Stats Tracking**: High score, most recent score, accuracy by type
- ⏱️ **Custom Game Settings**:
  - Time per question
  - Total questions
  - Significant figures
  - Decimal inclusion
  - Use of parentheses
  - Selectable categories
- 🧸 **Child-Friendly Interface** with animated buttons and clean visuals
- 💾 **Offline Persistence** (No backend) using `SharedPreferences` or internal JSON

---

## 📁 Project Structure

```plaintext
app/
├── src/
│   ├── main/
│   │   ├── java/com/colemcg/arithmatic/
│   │   │   ├── ui/           # All Fragments (Start, Options, Game, Stats, Results, About)
│   │   │   ├── viewmodel/    # GameViewModel for shared game state
│   │   │   ├── model/        # Data classes: GameSettings, QuestionCard, GameStats, etc.
│   │   │   ├── generator/    # Strategy pattern for question generation
│   │   │   ├── storage/      # StatsManager and local data handling
│   │   │   └── util/         # UI animation utilities and helpers
│   │   ├── res/              # Layouts, animations, drawables, etc.
│   │   └── AndroidManifest.xml
