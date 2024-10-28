package com.volkov.vacancysearchapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(com.volkov.core.di.KoinModule)
            modules(com.volkov.listvacancy.di.listVacancyModule)
        }
    }
}