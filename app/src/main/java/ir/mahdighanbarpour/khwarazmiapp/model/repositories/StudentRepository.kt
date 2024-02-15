package ir.mahdighanbarpour.khwarazmiapp.model.repositories

import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.khwarazmiapp.model.data.StudentMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.net.ApiService

class StudentRepository(private val apiService: ApiService) {

    // receiving student information
    fun loginStudent(phoneNumber: String): Single<StudentMainResult> {
        return apiService.loginStudent(phoneNumber)
    }

    // Student registration
    fun registerStudent(
        name: String, phoneNumber: String, birthday: String, grade: String
    ): Single<StudentMainResult> {
        return apiService.registerStudent(name, phoneNumber, birthday, grade)
    }
}