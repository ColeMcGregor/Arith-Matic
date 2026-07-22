package com.wiseravenstudios.arithmatic.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wiseravenstudios.arithmatic.data.local.dao.CompletedRoundDao
import com.wiseravenstudios.arithmatic.data.local.entity.CompletedRoundEntity
import com.wiseravenstudios.arithmatic.data.local.entity.QuestionAttemptEntity

@Database(
    entities = [
        CompletedRoundEntity::class,
        QuestionAttemptEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ArithMaticDatabase : RoomDatabase() {

    abstract fun completedRoundDao(): CompletedRoundDao

    companion object {

        private const val DATABASE_NAME =
            "arithmatic_database"

        @Volatile
        private var instance: ArithMaticDatabase? = null

        fun getInstance(
            context: Context
        ): ArithMaticDatabase {
            return instance ?: synchronized(this) {

                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ArithMaticDatabase::class.java,
                    DATABASE_NAME
                ).build().also { database ->
                    instance = database
                }
            }
        }
    }
}