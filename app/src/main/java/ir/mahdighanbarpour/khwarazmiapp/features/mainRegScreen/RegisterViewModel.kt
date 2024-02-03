package ir.mahdighanbarpour.khwarazmiapp.features.mainRegScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.khwarazmiapp.model.data.MainResult
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.StudentRepository

class RegisterViewModel(private val studentRepository: StudentRepository) : ViewModel() {

    fun registerStudent(
        name: String, phoneNumber: String, birthday: String, grade: String
    ): Single<MainResult> {
        return studentRepository.registerStudent(name, phoneNumber, birthday, grade)
    }
}