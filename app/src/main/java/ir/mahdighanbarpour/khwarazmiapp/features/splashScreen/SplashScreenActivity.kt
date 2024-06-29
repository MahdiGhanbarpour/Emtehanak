package ir.mahdighanbarpour.khwarazmiapp.features.splashScreen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivitySplashScreenBinding
import ir.mahdighanbarpour.khwarazmiapp.features.mainLoginScreen.LoginActivity
import ir.mahdighanbarpour.khwarazmiapp.features.mainStudentScreen.StudentMainActivity
import ir.mahdighanbarpour.khwarazmiapp.features.mainTeacherScreen.TeacherMainActivity
import ir.mahdighanbarpour.khwarazmiapp.model.data.StudentMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.TeacherMainResult
import ir.mahdighanbarpour.khwarazmiapp.utils.IS_USER_LOGGED_IN
import ir.mahdighanbarpour.khwarazmiapp.utils.STUDENT
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_FULL_NAME
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_PHONE_NUM
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_ROLE
import ir.mahdighanbarpour.khwarazmiapp.utils.asyncRequest
import ir.mahdighanbarpour.khwarazmiapp.utils.isInternetAvailable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Timer
import kotlin.concurrent.timerTask

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    private val splashScreenViewModel: SplashScreenViewModel by viewModel()
    private val sharedPreferences: SharedPreferences by inject()
    private val compositeDisposable = CompositeDisposable()

    private var phoneNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Checking whether the user has already logged in or not and determining the next page
        if (sharedPreferences.getBoolean(IS_USER_LOGGED_IN, false)) {
            userLoggedIn()
        } else {
            startNextActivity(LoginActivity())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun loginStudent() {
        splashScreenViewModel.loginStudent(phoneNumber).asyncRequest()
            .subscribe(object : SingleObserver<StudentMainResult> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    // Display the error
                    Snackbar.make(
                        binding.root, "خطا در دریافت اطلاعات", Snackbar.LENGTH_INDEFINITE
                    ).setAction("تلاش مجدد") { userLoggedIn() }.show()
                }

                override fun onSuccess(t: StudentMainResult) {
                    if (t.status != 404) {
                        if (t.result.student.fullName == sharedPreferences.getString(
                                USER_FULL_NAME, null
                            )
                        ) {
                            startNextActivity(StudentMainActivity())
                        } else {
                            startNextActivity(LoginActivity())
                        }
                    } else {
                        startNextActivity(LoginActivity())
                    }
                }
            })
    }

    private fun loginTeacher() {
        splashScreenViewModel.loginTeacher(phoneNumber).asyncRequest()
            .subscribe(object : SingleObserver<TeacherMainResult> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    // Display the error
                    Snackbar.make(
                        binding.root, "خطا در دریافت اطلاعات", Snackbar.LENGTH_INDEFINITE
                    ).setAction("تلاش مجدد") { userLoggedIn() }.show()
                    Log.v("testLog", e.message.toString())
                }

                override fun onSuccess(t: TeacherMainResult) {
                    if (t.status != 404) {
                        if (t.result.teacher.fullName == sharedPreferences.getString(
                                USER_FULL_NAME, null
                            )
                        ) {
                            startNextActivity(TeacherMainActivity())
                        } else {
                            startNextActivity(LoginActivity())
                        }
                    } else {
                        startNextActivity(LoginActivity())
                    }
                }
            })
    }

    private fun userLoggedIn() {
        if (isInternetAvailable(this)) {
            phoneNumber = sharedPreferences.getString(USER_PHONE_NUM, null)!!

            // Checking the role of the user and determining the next page based on it
            if (sharedPreferences.getString(USER_ROLE, null) == STUDENT) {
                loginStudent()
            } else {
                loginTeacher()
            }
        } else {
            // Display the error of not connecting to the Internet
            Snackbar.make(
                binding.root, "عدم دسترسی به اینترنت", Snackbar.LENGTH_INDEFINITE
            ).setAction("تلاش مجدد") { userLoggedIn() }.show()
        }

    }

    private fun startNextActivity(nextPage: Activity) {
        // Creating a 3-second delay and then opening the specified page
        Timer().schedule(timerTask {
            val intent = Intent(this@SplashScreenActivity, nextPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }, 3000)
    }
}