package com.souzaemerson.marvelproject.di.module

import android.content.Context
import androidx.room.Room
import com.souzaemerson.marvelproject.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        val tempInstance = AppDatabase.INSTANCE
        if (tempInstance != null) {
            return tempInstance
        }

        synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "appdatabase.db"
            )
                .addMigrations(AppDatabase.MIGRATION_1_2)
                .build()

            AppDatabase.INSTANCE = instance
            return instance
        }
    }

    @Singleton
    @Provides
    fun provideCharacterDao(database: AppDatabase) = database.characterDao()

    @Singleton
    @Provides
    fun provideUserDao(database: AppDatabase) = database.userDao()
}