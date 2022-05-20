package com.example.vrg_soft_test_task.app

import android.app.Application
import com.example.vrg_soft_test_task.di.dataModule
import com.example.vrg_soft_test_task.di.domainModule
import com.example.vrg_soft_test_task.di.networkModule
import com.example.vrg_soft_test_task.di.viewModelModule
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)
            modules(
                listOf(
                    networkModule,
                    viewModelModule,
                    dataModule,
                    domainModule,
                )
            )
        }
    }
}