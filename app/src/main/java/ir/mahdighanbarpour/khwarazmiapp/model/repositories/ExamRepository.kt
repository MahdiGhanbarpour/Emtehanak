package ir.mahdighanbarpour.khwarazmiapp.model.repositories

import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.khwarazmiapp.model.data.AddExamMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamsMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.QuestionMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.ReportQuestionMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.net.ApiService
import okhttp3.MultipartBody

class ExamRepository(private val apiService: ApiService) {

    // Getting the list of exams with the help of grade and grade list
    fun getExams(
        grade: String, gradeList: String, lesson: String?, limit: String
    ): Single<ExamsMainResult> {
        return apiService.getExams(grade, gradeList, lesson, limit)
    }

    // Getting the list of exam questions with the help of exam id
    fun getExamsQuestion(examId: Int): Single<QuestionMainResult> {
        return apiService.getExamsQuestion(examId)
    }

    // Searching for exams with the help of grade, search and grade list
    fun searchExams(
        grade: String, search: String, lesson: String?, gradeList: String
    ): Single<ExamsMainResult> {
        return apiService.searchExams(grade, search, lesson, gradeList)
    }

    // Adding the exam with the help of parts
    fun addExamWithQuestions(parts: List<MultipartBody.Part>): Single<AddExamMainResult> {
        return apiService.addExamWithQuestions(parts)
    }

    // Reporting the question with the help of question id and reporter phone number
    fun reportQuestion(
        questionId: Int, reporterPhoneNum: String
    ): Single<ReportQuestionMainResult> {
        return apiService.reportQuestion(questionId, reporterPhoneNum)
    }

    // Getting the list of teachers exams with the help of phone number
    fun getTeachersExams(phoneNumber: String, search: String): Single<ExamsMainResult> {
        return apiService.getTeachersExams(phoneNumber, search)
    }

    // Deleting the exam with the help of id
    fun deleteExam(id: Int): Single<ExamsMainResult> {
        return apiService.deleteExam(id)
    }

    // Changing the visibility of exam with the help of id
    fun changeVisibility(id: Int, visibility: String): Single<ExamsMainResult> {
        return apiService.changeVisibility(id, visibility)
    }
}