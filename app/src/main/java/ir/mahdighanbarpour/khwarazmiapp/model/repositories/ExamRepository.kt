package ir.mahdighanbarpour.khwarazmiapp.model.repositories

import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.khwarazmiapp.model.data.AddExamMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamsMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.QuestionMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.net.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ExamRepository(private val apiService: ApiService) {

    fun getExams(grade: String, gradeList: String, limit: String): Single<ExamsMainResult> {
        return apiService.getExams(grade, gradeList, limit)
    }

    // Getting the list of exam questions with the help of exam id
    fun getExamsQuestion(examId: Int): Single<QuestionMainResult> {
        return apiService.getExamsQuestion(examId)
    }

    fun searchExams(grade: String, search: String, gradeList: String): Single<ExamsMainResult> {
        return apiService.searchExams(grade, search, gradeList)
    }

    fun addExam(data: RequestBody, image: MultipartBody.Part): Single<AddExamMainResult> {
        return apiService.addExam(data, image)
    }
}