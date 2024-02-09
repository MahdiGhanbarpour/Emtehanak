package ir.mahdighanbarpour.khwarazmiapp.model.net

import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamsMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.FrequentlyQuestionsMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.QuestionMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.StudentMainResult
import retrofit2.http.*

interface ApiService {

    @GET("student/login")
    fun loginStudent(@Query("phone") phoneNumber: String): Single<StudentMainResult>

    @GET("student/register")
    fun registerStudent(
        @Query("name") name: String,
        @Query("phone") phoneNumber: String,
        @Query("birthday") birthday: String,
        @Query("grade") grade: String
    ): Single<StudentMainResult>

    @GET("exam/exams")
    fun getExams(
        @Query("grade") grade: String
    ): Single<ExamsMainResult>

    @GET("exam/popular-exams")
    fun getPopularExams(
        @Query("grade") grade: String
    ): Single<ExamsMainResult>

    @GET("exam/exam-questions")
    fun getExamsQuestion(
        @Query("exam") examId: Int
    ): Single<QuestionMainResult>

    @GET("frequently-question")
    fun getFrequentlyQuestions(
        @Query("page") page: String
    ): Single<FrequentlyQuestionsMainResult>
}