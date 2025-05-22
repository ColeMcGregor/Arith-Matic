@startuml
' Skin customization for readability
skinparam classAttributeIconSize 0
skinparam linetype ortho

package "UI Layer" {
    class MainActivity {
        +onCreate()
    }

    class StartFragment {
        +onViewCreated()
    }

    class OptionsFragment {
        +onViewCreated()
        +onOptionsSelected()
    }

    class GameFragment {
        +onViewCreated()
        +renderQuestion()
        +onAnswerSelected()
    }

    class ResultsFragment {
        +onViewCreated()
        +displayResults()
    }

    class StatsFragment {
        +onViewCreated()
        +loadStats()
    }

    class AboutFragment {
        +onViewCreated()
    }

    MainActivity --> StartFragment : hosts >
    MainActivity --> OptionsFragment : used for settings 
    MainActivity --> GameFragment : hosts game play
    MainActivity --> ResultsFragment : gives results
    MainActivity --> StatsFragment : used for stats recall
    MainActivity --> AboutFragment : used for game information
}

package "ViewModel" {
    class GameViewModel {
        -gameSettings: GameSettings
        -questionList: List<QuestionCard>
        -currentIndex: Int
        -score: Int
        -streak: Int

        +setSettings(settings: GameSettings)
        +startGame()
        +getNextQuestion(): QuestionCard
        +recordAnswer(correct: Boolean, card: QuestionCard)
        +getResults(): GameResults
    }

    GameFragment --> GameViewModel : uses >
    OptionsFragment --> GameViewModel : configures >
    ResultsFragment --> GameViewModel : pulls summary >
}

package "Data Models" {
    class GameSettings {
        +timePerQuestion: Int
        +totalQuestions: Int
        +allowDecimals: Boolean
        +sigFigs: Int
        +useParentheses: Boolean
        +selectedTypes: List<QuestionType>
    }

    enum QuestionType {
        ADDITION
        SUBTRACTION
        MULTIPLICATION
        DIVISION
        LOGIC
        ADVANCED
    }

    class QuestionCard {
        +id: String
        +type: QuestionType
        +question: String
        +englishQuestion: String
        +options: List<String>
        +correctAnswer: String
        +timestamps: Pair<Long, Long>
        +wasCorrect: Boolean
    }
}

package "Question Generation" {
    interface QuestionGenerator {
        +generate(settings: GameSettings): QuestionCard
    }

    class AdditionGenerator implements QuestionGenerator
    class SubtractionGenerator implements QuestionGenerator
    class MultiplicationGenerator implements QuestionGenerator
    class DivisionGenerator implements QuestionGenerator
    class LogicGenerator implements QuestionGenerator
    class AdvancedGenerator implements QuestionGenerator

    class CardFactory {
        +generateCard(type: QuestionType, settings: GameSettings): QuestionCard
    }

    CardFactory --> QuestionGenerator : delegates >
    QuestionGenerator --> AdditionGenerator
    QuestionGenerator --> SubtractionGenerator
    QuestionGenerator --> MultiplicationGenerator
    QuestionGenerator --> DivisionGenerator
    QuestionGenerator --> LogicGenerator
    QuestionGenerator --> AdvancedGenerator
    GameViewModel --> CardFactory : uses to create cards >
}

package "Storage" {
    class StatsManager {
        -context: Context

        +saveResult(card: QuestionCard)
        +getStats(): GameStats
    }

    class GameStats {
        +correctCounts: Map<QuestionType, Int>
        +wrongCounts: Map<QuestionType, Int>
        +highestScore: Int
        +mostRecentScore: Int
    }

    ResultsFragment --> StatsManager : save results >
    StatsFragment --> StatsManager : fetch stats >
}
@enduml

