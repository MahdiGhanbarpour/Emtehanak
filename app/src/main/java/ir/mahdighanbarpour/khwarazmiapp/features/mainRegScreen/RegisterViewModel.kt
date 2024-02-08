package ir.mahdighanbarpour.khwarazmiapp.features.mainRegScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ir.mahdighanbarpour.khwarazmiapp.model.data.StudentMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.StudentRepository

class RegisterViewModel(private val studentRepository: StudentRepository) : ViewModel() {
    val isDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()

    fun registerStudent(
        name: String, phoneNumber: String, birthday: String, grade: String
    ): Single<StudentMainResult> {
        isDataLoading.onNext(true)

        return studentRepository.registerStudent(name, phoneNumber, birthday, grade).doFinally {
            isDataLoading.onNext(false)
        }
    }
}