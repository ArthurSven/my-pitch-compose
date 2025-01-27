package com.devapps.mypitch

import android.app.Application
import com.devapps.mypitch.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyPitchApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyPitchApplication)
            modules(appModule)
        }
    }
}