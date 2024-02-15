package ir.mahdighanbarpour.khwarazmiapp.features.otpLoginScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ir.mahdighanbarpour.khwarazmiapp.model.data.StudentMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.StudentRepository

class LoginOtpViewModel(private val studentRepository: StudentRepository) : ViewModel() {

    // Determines whether something is being received from the server or not
    val isDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()

    // receiving student information
    fun loginStudent(phoneNumber: String): Single<StudentMainResult> {
        isDataLoading.onNext(true)

        return studentRepository.loginStudent(phoneNumber).doFinally {
            isDataLoading.onNext(false)
        }
    }
}