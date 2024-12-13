package com.example.androidhomework.app

import android.app.Application
import com.example.androidhomework.di.appModule
import org.koin.core.context.GlobalContext.startKoin
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }
}