package ir.mahdighanbarpour.khwarazmiapp.features.splashScreen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivitySplashScreenBinding
import ir.mahdighanbarpour.khwarazmiapp.features.mainLoginScreen.LoginActivity
import ir.mahdighanbarpour.khwarazmiapp.features.mainStudentScreen.StudentMainActivity
import ir.mahdighanbarpour.khwarazmiapp.utils.IS_USER_LOGGED_IN
import ir.mahdighanbarpour.khwarazmiapp.utils.STUDENT
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_ROLE
import org.koin.android.ext.android.inject
import java.util.Timer
import kotlin.concurrent.timerTask

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var nextPage: Activity

    private val sharedPreferences: SharedPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Checking whether the user has already logged in or not and determining the next page
        if (sharedPreferences.getBoolean(IS_USER_LOGGED_IN, false)) {
            // Checking the role of the user and determining the next page based on it
            if (sharedPreferences.getString(USER_ROLE, null) == STUDENT) {
                nextPage = StudentMainActivity()
            } else {
                // TODO
            }
        } else {
            nextPage = LoginActivity()
        }

        // Creating a 3-second delay and then opening the specified page
        Timer().schedule(timerTask {
            val intent = Intent(this@SplashScreenActivity, nextPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }, 3000)
    }
}