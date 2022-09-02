package com.souzaemerson.marvelproject.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.souzaemerson.marvelproject.data.db.converters.Converters
import com.souzaemerson.marvelproject.data.db.dao.CharacterDAO
import com.souzaemerson.marvelproject.data.db.dao.UserDAO
import com.souzaemerson.marvelproject.data.model.Favorites
import com.souzaemerson.marvelproject.data.model.User

@Database(entities = [Favorites::class, User::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        @Volatile
        var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("CREATE TABLE IF NOT EXISTS `user_table` (`email` TEXT NOT NULL, `name` TEXT NOT NULL, `password` TEXT NOT NULL, `photo` INTEGER, PRIMARY KEY(`email`))");
            }
        }
    }

    abstract fun characterDao(): CharacterDAO
    abstract fun userDao(): UserDAO
}
