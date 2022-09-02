package com.souzaemerson.marvelproject.di.module

import com.souzaemerson.marvelproject.data.db.repository.DatabaseRepository
import com.souzaemerson.marvelproject.data.db.repository.DatabaseRepositoryImpl
import com.souzaemerson.marvelproject.data.repository.category.CategoryRepository
import com.souzaemerson.marvelproject.data.repository.category.CategoryRepositoryImpl
import com.souzaemerson.marvelproject.data.repository.character.CharacterRepository
import com.souzaemerson.marvelproject.data.repository.character.CharactersRepositoryImpl
import com.souzaemerson.marvelproject.data.repository.login.LoginRepository
import com.souzaemerson.marvelproject.data.repository.login.LoginRepositoryImpl
import com.souzaemerson.marvelproject.data.repository.register.RegisterRepository
import com.souzaemerson.marvelproject.data.repository.register.RegisterRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppMudule {

    @Singleton
    @Binds
    abstract fun bindCategoryRepository(
        categoryRepository: CategoryRepositoryImpl
    ): CategoryRepository

    @Singleton
    @Binds
    abstract fun bindCharacterRepository(
        characterRepository: CharactersRepositoryImpl
    ): CharacterRepository

    @Singleton
    @Binds
    abstract fun bindDatabaseRepository(
        databaseRepository: DatabaseRepositoryImpl
    ): DatabaseRepository

    @Singleton
    @Binds
    abstract fun bindLoginRepository(
        loginRepository: LoginRepositoryImpl
    ): LoginRepository

    @Singleton
    @Binds
    abstract fun bindRegisterRepository(
        registerRepository: RegisterRepositoryImpl
    ): RegisterRepository
}