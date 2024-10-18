package ir.mahdighanbarpour.emtehanak

import android.app.Application
import ir.mahdighanbarpour.emtehanak.di.myModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

// Application class
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Koin
        val modules = myModules
        startKoin {
            androidContext(this@MyApp)
            modules(modules)
        }
    }
}