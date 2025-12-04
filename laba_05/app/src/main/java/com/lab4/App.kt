package com.lab4

import android.app.Application
import com.lab4.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

/**
 * App - class of your application (extends on Application())
 * - creates the MainActivity under the hood
 */
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        // startKoin {} - initialization of Koin in the app
        startKoin{
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }
}