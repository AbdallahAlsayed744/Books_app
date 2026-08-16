package com.hyperdesign.books_app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class BooksApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BooksApplication)
//        workManagerFactory()
//        modules(allModules)
        }
    }


}