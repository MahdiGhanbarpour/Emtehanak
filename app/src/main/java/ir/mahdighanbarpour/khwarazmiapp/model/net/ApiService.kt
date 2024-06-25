package ir.mahdighanbarpour.khwarazmiapp.model.net

import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamsMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.FrequentlyQuestionsMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.QuestionMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.StudentMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.TeacherMainResult
import retrofit2.http.*

interface ApiService {

    @GET("user/login-student")
    fun loginStudent(@Query("phone") phoneNumber: String): Single<StudentMainResult>

    @GET("user/register-student")
    fun registerStudent(
        @Query("name") name: String,
        @Query("phone") phoneNumber: String,
        @Query("birthday") birthday: String,
        @Query("grade") grade: String
    ): Single<StudentMainResult>

    @GET("user/login-teacher")
    fun loginTeacher(@Query("phone") phoneNumber: String): Single<TeacherMainResult>

    @GET("user/register-teacher")
    fun registerTeacher(
        @Query("name") name: String,
        @Query("phone") phoneNumber: String,
        @Query("birthday") birthday: String,
        @Query("studyField") studyField: String,
        @Query("grades") grade: String,
        @Query("activityYear") activityYear: String
    ): Single<TeacherMainResult>

    @GET("exam/exams")
    fun getExams(
        @Query("grade") grade: String,
        @Query("gradeList") gradeList: String,
        @Query("limit") limit: String
    ): Single<ExamsMainResult>

    @GET("exam/search-exams")
    fun searchExams(
        @Query("grade") grade: String,
        @Query("search") search: String,
        @Query("gradeList") gradeList: String,
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