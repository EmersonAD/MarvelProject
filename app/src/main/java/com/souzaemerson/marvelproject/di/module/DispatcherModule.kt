package com.souzaemerson.marvelproject.di.module

import com.souzaemerson.marvelproject.di.qualifier.Default
import com.souzaemerson.marvelproject.di.qualifier.IO
import com.souzaemerson.marvelproject.di.qualifier.Main
import com.souzaemerson.marvelproject.di.qualifier.Unconfined
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Singleton
    @Provides
    @IO
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    @Default
    fun defaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Singleton
    @Provides
    @Main
    fun mainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Singleton
    @Provides
    @Unconfined
    fun unconfinedDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined
}