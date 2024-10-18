package ir.mahdighanbarpour.emtehanak.model.repositories

import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.emtehanak.model.data.LessonsMainResult
import ir.mahdighanbarpour.emtehanak.model.net.ApiService

class LessonRepository(private val apiService: ApiService) {

    // Getting the list of lessons with the help of grade
    fun getLessons(grade: String, limit: String): Single<LessonsMainResult> {
        return apiService.getLessons(grade, limit)
    }
}