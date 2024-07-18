package ir.mahdighanbarpour.khwarazmiapp.features.examListScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamsMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.ExamRepository

class ExamListViewModel(private val examRepository: ExamRepository) : ViewModel() {
    // Determines whether something is being received from the server or not
    val isDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()

    // Getting the list of exams with the help of grade and limit
    fun getExamList(grade: String, gradeList: String, limit: String): Single<ExamsMainResult> {
        // Determines whether something is being received from the server or not
        isDataLoading.onNext(true)

        // Getting the list of exams with the help of grade and limit
        return examRepository.getExams(grade, gradeList, limit).doFinally {
            isDataLoading.onNext(false)
        }
    }

    // Search for exams
    fun searchExams(grade: String, search: String, gradeList: String): Single<ExamsMainResult> {
        // Determines whether something is being received from the server or not
        isDataLoading.onNext(true)

        // Getting the list of exams with the help of grade and limit
        return examRepository.searchExams(grade, search, gradeList).doFinally {
            isDataLoading.onNext(false)
        }
    }
}