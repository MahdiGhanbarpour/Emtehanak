package ir.mahdighanbarpour.emtehanak.features.homeStudentScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ir.mahdighanbarpour.emtehanak.model.data.ExamsMainResult
import ir.mahdighanbarpour.emtehanak.model.data.LessonsMainResult
import ir.mahdighanbarpour.emtehanak.model.data.SliderMainResult
import ir.mahdighanbarpour.emtehanak.model.repositories.ExamRepository
import ir.mahdighanbarpour.emtehanak.model.repositories.LessonRepository
import ir.mahdighanbarpour.emtehanak.model.repositories.StudentRepository

class StudentHomeViewModel(
    private val examRepository: ExamRepository,
    private val lessonRepository: LessonRepository,
    private val studentRepository: StudentRepository
) : ViewModel() {

    // Determines whether something is being received from the server or not
    val isPopularExamsDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()
    val isLessonsDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()
    val isSliderDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()

    // Getting the list of popular exams from the server based on the user's grade
    fun getPopularExams(grade: String, gradeList: String, limit: String): Single<ExamsMainResult> {
        // Determines whether something is being received from the server or not
        isPopularExamsDataLoading.onNext(true)

        // Getting the list of popular exams from the server
        return examRepository.getExams(grade, gradeList, null, limit).doFinally {
            isPopularExamsDataLoading.onNext(false)
        }
    }

    // Getting the list of lessons from the server based on the user's grade
    fun getLessons(grade: String, limit: String): Single<LessonsMainResult> {
        // Determines whether something is being received from the server or not
        isLessonsDataLoading.onNext(true)

        // Getting the list of lessons from the server
        return lessonRepository.getLessons(grade, limit).doFinally {
            isLessonsDataLoading.onNext(false)
        }
    }

    // Getting slider items
    fun getSliderItems(role: String): Single<SliderMainResult> {
        // Determines whether something is being received from the server or not
        isSliderDataLoading.onNext(true)

        return studentRepository.getSliderItems(role).doFinally {
            // Determines whether something is being received from the server or not
            isSliderDataLoading.onNext(false)
        }
    }
}