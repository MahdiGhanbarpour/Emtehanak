package ir.mahdighanbarpour.khwarazmiapp.model.repositories

import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.khwarazmiapp.model.data.MainResult
import ir.mahdighanbarpour.khwarazmiapp.model.net.ApiService

class StudentRepository(private val apiService: ApiService) {

    fun loginStudent(phoneNumber: String): Single<MainResult> {
        return apiService.loginStudent(phoneNumber)
    }

    fun registerStudent(
        name: String, phoneNumber: String, birthday: String, grade: String
    ): Single<MainResult> {
        return apiService.registerStudent(name, phoneNumber, birthday, grade)
    }
}