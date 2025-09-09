@startuml
' Skin customization for readability
skinparam classAttributeIconSize 0
skinparam linetype polyline

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
    
    class GameStats {
        +correctCounts: Map<QuestionType, Int>
        +wrongCounts: Map<QuestionType, Int>
        +highestScore: Int
        +mostRecentScore: Int
    }
    
    class GameResults {
        +correctCounts: Map<QuestionType, Int>
        +wrongCounts: Map<QuestionType, Int>  
        +timeTaken: Pair<Long, Long>
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
}

package "Storage" {
    class StatsManager {
        -context: Context
        +saveResult(card: QuestionCard)
        +getStats(): GameStats
    }
    
    class SettingsDataStore {
        -context: Context
        +saveSettings(settings: GameSettings)
        +loadSettings(): GameSettings
        +clearSettings()
    }
}

' ------------------------
' RELATIONSHIPS BLOBBED BELOW
' ------------------------

MainActivity --> StartFragment : hosts start screen 
MainActivity --> OptionsFragment : navigates to options 
MainActivity --> GameFragment : launches gameplay screen 
MainActivity --> ResultsFragment : navigates to results 
MainActivity --> StatsFragment : opens stats view 
MainActivity --> AboutFragment : shows about page 


GameFragment --> GameViewModel : retrieves questions 
OptionsFragment --> GameViewModel : updates settings
ResultsFragment --> GameViewModel : summarizes results 
StatsFragment --> GameViewModel : access session data 

GameViewModel --> GameSettings : applies user preferences 
GameViewModel --> QuestionCard : manages current question 
GameViewModel --> GameResults : generates end results 
StatsManager --> GameStats : compiles long-term stats 

CardFactory --> QuestionGenerator : delegates generation 
QuestionGenerator --> AdditionGenerator : addition logic 
QuestionGenerator --> SubtractionGenerator : subtraction logic 
QuestionGenerator --> MultiplicationGenerator : multiplication logic 
QuestionGenerator --> DivisionGenerator : division logic 
QuestionGenerator --> LogicGenerator : logic problem logic 
QuestionGenerator --> AdvancedGenerator : advanced rules 
GameViewModel --> CardFactory : requests question cards 

ResultsFragment --> StatsManager : save game session 
StatsFragment --> StatsManager : fetch overall stats 
OptionsFragment --> SettingsDataStore : save/load settings 
GameViewModel --> SettingsDataStore : load startup settings 
StatsManager --> QuestionCard : logs individual answers 

@enduml
