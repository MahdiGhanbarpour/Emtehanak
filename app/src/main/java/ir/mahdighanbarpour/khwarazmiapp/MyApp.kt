package ir.mahdighanbarpour.khwarazmiapp

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import ir.mahdighanbarpour.khwarazmiapp.di.myModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

// Application class
class MyApp : Application() {

    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate() {
        super.onCreate()

        analytics = Firebase.analytics

        // Koin
        val modules = myModules
        startKoin {
            androidContext(this@MyApp)
            modules(modules)
        }
    }
}