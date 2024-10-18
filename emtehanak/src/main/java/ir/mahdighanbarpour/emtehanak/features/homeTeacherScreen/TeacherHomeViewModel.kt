package ir.mahdighanbarpour.emtehanak.features.homeTeacherScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ir.mahdighanbarpour.emtehanak.model.data.ExamsMainResult
import ir.mahdighanbarpour.emtehanak.model.data.SliderMainResult
import ir.mahdighanbarpour.emtehanak.model.repositories.ExamRepository
import ir.mahdighanbarpour.emtehanak.model.repositories.TeacherRepository

class TeacherHomeViewModel(
    private val examRepository: ExamRepository, private val teacherRepository: TeacherRepository
) : ViewModel() {
    // Determines whether something is being received from the server or not
    val isPopularExamsDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()
    val isSliderDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()

    // Getting the list of popular exams from the server based on the user's grade
    fun getPopularExams(grade: String, gradeList: String, limit: String): Single<ExamsMainResult> {
        // Determines whether something is being received from the server or not
        isPopularExamsDataLoading.onNext(true)

        return examRepository.getExams(grade, gradeList, null, limit).doFinally {
            // Determines whether something is being received from the server or not
            isPopularExamsDataLoading.onNext(false)
        }
    }

    // Getting slider items
    fun getSliderItems(role: String): Single<SliderMainResult> {
        // Determines whether something is being received from the server or not
        isSliderDataLoading.onNext(true)

        return teacherRepository.getSliderItems(role).doFinally {
            // Determines whether something is being received from the server or not
            isSliderDataLoading.onNext(false)
        }
    }
}