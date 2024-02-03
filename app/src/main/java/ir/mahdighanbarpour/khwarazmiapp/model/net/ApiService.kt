package ir.mahdighanbarpour.khwarazmiapp.model.net

import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.khwarazmiapp.model.data.MainResult
import retrofit2.http.*

interface ApiService {

    @GET("student/login")
    fun loginStudent(@Query("phone") phoneNumber: String): Single<MainResult>

    @GET("student/register")
    fun registerStudent(
        @Query("name") name: String,
        @Query("phone") phoneNumber: String,
        @Query("birthday") birthday: String,
        @Query("grade") grade: String
    ): Single<MainResult>
}