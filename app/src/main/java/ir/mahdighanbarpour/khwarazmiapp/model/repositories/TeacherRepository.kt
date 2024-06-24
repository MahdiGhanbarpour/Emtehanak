package ir.mahdighanbarpour.khwarazmiapp.model.repositories

import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.khwarazmiapp.model.data.StudentMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.TeacherMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.net.ApiService

class TeacherRepository(private val apiService: ApiService) {

    // receiving Teacher information
    fun loginTeacher(phoneNumber: String): Single<TeacherMainResult> {
        return apiService.loginTeacher(phoneNumber)
    }

    // Teacher registration
    fun registerTeacher(
        name: String,
        phoneNumber: String,
        birthday: String,
        studyField: String,
        grade: String,
        activityYear: String
    ): Single<TeacherMainResult> {
        return apiService.registerTeacher(
            name, phoneNumber, birthday, studyField, grade, activityYear
        )
    }
}