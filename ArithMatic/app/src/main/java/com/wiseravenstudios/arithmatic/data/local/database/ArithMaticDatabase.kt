package com.wiseravenstudios.arithmatic.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wiseravenstudios.arithmatic.data.local.dao.CompletedRoundDao
import com.wiseravenstudios.arithmatic.data.local.entity.CompletedRoundEntity
import com.wiseravenstudios.arithmatic.data.local.entity.QuestionAttemptEntity

@Database(
    entities = [
        CompletedRoundEntity::class,
        QuestionAttemptEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class ArithMaticDatabase : RoomDatabase() {

    abstract fun completedRoundDao(): CompletedRoundDao

    companion object {

        private const val DATABASE_NAME =
            "arithmatic_database"

        private val MIGRATION_1_2 =
            object : Migration(
                startVersion = 1,
                endVersion = 2
            ) {
                override fun migrate(
                    db: SupportSQLiteDatabase
                ) {
                    db.execSQL(
                        """
                        ALTER TABLE question_attempts
                        ADD COLUMN operands TEXT NOT NULL DEFAULT ''
                        """.trimIndent()
                    )
                }
            }

        private val MIGRATION_2_3 =
            object : Migration(
                startVersion = 2,
                endVersion = 3
            ) {
                override fun migrate(
                    db: SupportSQLiteDatabase
                ) {
                    db.execSQL(
                        """
                        CREATE TABLE completed_rounds_new (
                            id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                            completedAtEpochMillis INTEGER NOT NULL,
                            activeRoundDurationMillis INTEGER NOT NULL,
                            enabledOperations TEXT NOT NULL,
                            allowNegatives INTEGER NOT NULL,
                            allowDecimals INTEGER NOT NULL,
                            maximumOperand INTEGER NOT NULL,
                            questionCount INTEGER NOT NULL
                        )
                        """.trimIndent()
                    )

                    db.execSQL(
                        """
                        INSERT INTO completed_rounds_new (
                            id,
                            completedAtEpochMillis,
                            activeRoundDurationMillis,
                            enabledOperations,
                            allowNegatives,
                            allowDecimals,
                            maximumOperand,
                            questionCount
                        )
                        SELECT
                            id,
                            completedAtEpochMillis,
                            activeRoundDurationMillis,
                            enabledOperations,
                            allowNegatives,
                            allowDecimals,
                            CASE wholeNumberDigits
                                WHEN 1 THEN 9
                                WHEN 2 THEN 99
                                WHEN 3 THEN 999
                                WHEN 4 THEN 9999
                                WHEN 5 THEN 99999
                                WHEN 6 THEN 999999
                                ELSE 9
                            END,
                            questionCount
                        FROM completed_rounds
                        """.trimIndent()
                    )

                    db.execSQL(
                        """
                        DROP TABLE completed_rounds
                        """.trimIndent()
                    )

                    db.execSQL(
                        """
                        ALTER TABLE completed_rounds_new
                        RENAME TO completed_rounds
                        """.trimIndent()
                    )
                }
            }

        private val MIGRATION_3_4 =
            object : Migration(
                startVersion = 3,
                endVersion = 4
            ) {
                override fun migrate(
                    db: SupportSQLiteDatabase
                ) {
                    db.execSQL(
                        """
                        ALTER TABLE completed_rounds
                        ADD COLUMN focusNumber INTEGER
                        """.trimIndent()
                    )
                }
            }

        @Volatile
        private var instance: ArithMaticDatabase? =
            null

        fun getInstance(
            context: Context
        ): ArithMaticDatabase {
            return instance
                ?: synchronized(this) {
                    instance
                        ?: Room.databaseBuilder(
                            context.applicationContext,
                            ArithMaticDatabase::class.java,
                            DATABASE_NAME
                        )
                            .addMigrations(
                                MIGRATION_1_2,
                                MIGRATION_2_3,
                                MIGRATION_3_4
                            )
                            .build()
                            .also { database ->
                                instance =
                                    database
                            }
                }
        }
    }
}