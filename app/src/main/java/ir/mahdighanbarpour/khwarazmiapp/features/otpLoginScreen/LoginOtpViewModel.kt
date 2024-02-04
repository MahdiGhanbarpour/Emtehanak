package ir.mahdighanbarpour.khwarazmiapp.features.otpLoginScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ir.mahdighanbarpour.khwarazmiapp.model.data.MainResult
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.StudentRepository

class LoginOtpViewModel(private val studentRepository: StudentRepository) : ViewModel() {
    val isDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()

    fun loginStudent(phoneNumber: String): Single<MainResult> {
        isDataLoading.onNext(true)

        return studentRepository.loginStudent(phoneNumber).doFinally {
            isDataLoading.onNext(false)
        }
    }
}