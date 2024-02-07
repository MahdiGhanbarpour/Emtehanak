package ir.mahdighanbarpour.khwarazmiapp.model.repositories

import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamsMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.net.ApiService

class ExamRepository(private val apiService: ApiService) {

    fun getExams(grade: String): Single<ExamsMainResult> {
        return apiService.getExams(grade)
    }

    fun getPopularExams(grade: String): Single<ExamsMainResult> {
        return apiService.getPopularExams(grade)
    }
}