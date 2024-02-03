package ir.mahdighanbarpour.khwarazmiapp.features.otpLoginScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.khwarazmiapp.model.data.MainResult
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.StudentRepository

class LoginOtpViewModel(private val studentRepository: StudentRepository) :
    ViewModel() {

    fun loginStudent(phoneNumber: String): Single<MainResult> {
        return studentRepository.loginStudent(phoneNumber)
    }
}