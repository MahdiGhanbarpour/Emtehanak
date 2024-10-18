package ir.mahdighanbarpour.emtehanak.model.repositories

import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.emtehanak.model.data.SliderMainResult
import ir.mahdighanbarpour.emtehanak.model.data.TeacherMainResult
import ir.mahdighanbarpour.emtehanak.model.net.ApiService

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

    // Getting slider items
    fun getSliderItems(role: String): Single<SliderMainResult> {
        return apiService.getSliderItems(role)
    }
}