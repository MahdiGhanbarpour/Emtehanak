package ir.mahdighanbarpour.khwarazmiapp

import android.app.Application
import ir.mahdighanbarpour.khwarazmiapp.di.myModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val modules = myModules
        startKoin {
            androidContext(this@MyApp)
            modules(modules)
        }
    }
}