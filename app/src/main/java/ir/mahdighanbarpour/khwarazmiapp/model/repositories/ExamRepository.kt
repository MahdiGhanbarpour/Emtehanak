package ir.mahdighanbarpour.khwarazmiapp.model.repositories

import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamsMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.QuestionMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.net.ApiService

class ExamRepository(private val apiService: ApiService) {

    fun getExams(grade: String): Single<ExamsMainResult> {
        return apiService.getExams(grade)
    }

    // Getting the list of popular exams from the server based on the user's grade
    fun getPopularExams(grade: String): Single<ExamsMainResult> {
        return apiService.getPopularExams(grade)
    }

    // Getting the list of exam questions with the help of exam id
    fun getExamsQuestion(examId: Int): Single<QuestionMainResult> {
        return apiService.getExamsQuestion(examId)
    }

    fun searchExams(grade: String, search: String): Single<ExamsMainResult> {
        return apiService.searchExams(grade, search)
    }
}