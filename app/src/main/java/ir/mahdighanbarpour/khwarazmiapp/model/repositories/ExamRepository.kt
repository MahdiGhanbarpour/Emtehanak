package ir.mahdighanbarpour.khwarazmiapp.model.repositories

import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.khwarazmiapp.model.data.AddExamMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamsMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.QuestionMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.net.ApiService
import okhttp3.MultipartBody

class ExamRepository(private val apiService: ApiService) {

    // Getting the list of exams with the help of grade and grade list
    fun getExams(grade: String, gradeList: String, limit: String): Single<ExamsMainResult> {
        return apiService.getExams(grade, gradeList, limit)
    }

    // Getting the list of exam questions with the help of exam id
    fun getExamsQuestion(examId: Int): Single<QuestionMainResult> {
        return apiService.getExamsQuestion(examId)
    }

    // Searching for exams with the help of grade, search and grade list
    fun searchExams(grade: String, search: String, gradeList: String): Single<ExamsMainResult> {
        return apiService.searchExams(grade, search, gradeList)
    }

    // Adding the exam with the help of parts
    fun addExamWithQuestions(parts: List<MultipartBody.Part>): Single<AddExamMainResult> {
        return apiService.addExamWithQuestions(parts)
    }
}