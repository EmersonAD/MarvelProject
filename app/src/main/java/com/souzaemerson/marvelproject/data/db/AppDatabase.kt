package com.souzaemerson.marvelproject.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.souzaemerson.marvelproject.data.model.Results

@Database(entities = [Results::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun characterDao(): CharacterDAO

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2: Migration = object: Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //include sql command to update database
            }
        }

            fun getDb(context: Context): AppDatabase{
                val tempInstance = INSTANCE
                if (tempInstance != null){
                    return tempInstance
                }

                synchronized(this){
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "appdatabase.db")
                        .addMigrations(MIGRATION_1_2)
                        .build()

                    INSTANCE = instance
                    return instance
                }
            }
        }
    }
