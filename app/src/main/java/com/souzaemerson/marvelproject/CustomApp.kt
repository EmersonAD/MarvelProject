package com.souzaemerson.marvelproject

import android.app.Application
import com.github.clans.fab.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CustomApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

            //TODO("FAZER O HAWK DEPOIS DO HILT E USECASE")
        }
    }
}