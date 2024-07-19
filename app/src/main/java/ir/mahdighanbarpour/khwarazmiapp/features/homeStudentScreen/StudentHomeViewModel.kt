package ir.mahdighanbarpour.khwarazmiapp.features.homeStudentScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamsMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.LessonsMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.ExamRepository
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.LessonRepository

class StudentHomeViewModel(
    private val examRepository: ExamRepository, private val lessonRepository: LessonRepository
) : ViewModel() {

    // Determines whether something is being received from the server or not
    val isPopularExamsDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()
    val isLessonsDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()

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

}