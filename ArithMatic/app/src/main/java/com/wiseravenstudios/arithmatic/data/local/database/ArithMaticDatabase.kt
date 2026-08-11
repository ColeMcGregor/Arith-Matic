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
    version = 2,
    exportSchema = false
)
abstract class ArithMaticDatabase : RoomDatabase() {

    abstract fun completedRoundDao(): CompletedRoundDao

    companion object {

        private const val DATABASE_NAME =
            "arithmatic_database"

        /**
         * Adds structured operand storage to saved question attempts.
         *
         * Existing attempts receive an empty operand value because operands
         * cannot be reconstructed reliably from previously stored history.
         */
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
                                MIGRATION_1_2
                            )
                            .build()
                            .also { database ->
                                instance = database
                            }
                }
        }
    }
}