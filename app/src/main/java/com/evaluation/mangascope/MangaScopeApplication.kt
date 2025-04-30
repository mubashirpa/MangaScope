package com.evaluation.mangascope

import android.app.Application
import com.evaluation.mangascope.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MangaScopeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MangaScopeApplication)
            modules(appModule)
        }
    }
}
