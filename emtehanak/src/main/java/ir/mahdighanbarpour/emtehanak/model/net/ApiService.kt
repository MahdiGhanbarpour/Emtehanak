package ir.mahdighanbarpour.emtehanak.model.net

import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.emtehanak.model.data.AddExamMainResult
import ir.mahdighanbarpour.emtehanak.model.data.ExamsMainResult
import ir.mahdighanbarpour.emtehanak.model.data.FrequentlyQuestionsMainResult
import ir.mahdighanbarpour.emtehanak.model.data.LessonsMainResult
import ir.mahdighanbarpour.emtehanak.model.data.QuestionMainResult
import ir.mahdighanbarpour.emtehanak.model.data.ReportQuestionMainResult
import ir.mahdighanbarpour.emtehanak.model.data.SliderMainResult
import ir.mahdighanbarpour.emtehanak.model.data.StudentMainResult
import ir.mahdighanbarpour.emtehanak.model.data.TeacherMainResult
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

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
        @Query("lesson") lesson: String?,
        @Query("limit") limit: String
    ): Single<ExamsMainResult>

    @GET("exam/search-exams")
    fun searchExams(
        @Query("grade") grade: String,
        @Query("search") search: String,
        @Query("lesson") lesson: String?,
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

    @Multipart
    @POST("exam/add-exam")
    fun addExamWithQuestions(
        @Part parts: List<MultipartBody.Part>
    ): Single<AddExamMainResult>

    @GET("lesson/lessons")
    fun getLessons(
        @Query("grade") grade: String, @Query("limit") limit: String
    ): Single<LessonsMainResult>

    @GET("support/report-question")
    fun reportQuestion(
        @Query("questionId") questionId: Int, @Query("reporterPhoneNum") reporterPhoneNum: String
    ): Single<ReportQuestionMainResult>

    @GET("exam/teachers-exam")
    fun getTeachersExams(
        @Query("phone") phoneNumber: String,
        @Query("search") search: String,
    ): Single<ExamsMainResult>

    @GET("exam/delete-exam")
    fun deleteExam(
        @Query("id") id: Int,
    ): Single<ExamsMainResult>

    @GET("exam/change-visibility")
    fun changeVisibility(
        @Query("id") id: Int,
        @Query("visibility") visibility: String,
    ): Single<ExamsMainResult>

    @GET("slider/items")
    fun getSliderItems(
        @Query("role") role: String,
    ): Single<SliderMainResult>
}