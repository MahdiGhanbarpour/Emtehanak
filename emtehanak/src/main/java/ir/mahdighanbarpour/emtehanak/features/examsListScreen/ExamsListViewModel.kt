package ir.mahdighanbarpour.emtehanak.features.examsListScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ir.mahdighanbarpour.emtehanak.model.data.ExamsMainResult
import ir.mahdighanbarpour.emtehanak.model.repositories.ExamRepository

class ExamsListViewModel(private val examRepository: ExamRepository) : ViewModel() {
    // Determines whether something is being received from the server or not
    val isDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()

    // Getting the list of exams with the help of grade and limit
    fun getExamList(
        grade: String, gradeList: String, lesson: String?, limit: String
    ): Single<ExamsMainResult> {
        // Determines whether something is being received from the server or not
        isDataLoading.onNext(true)

        // Getting the list of exams with the help of grade and limit
        return examRepository.getExams(grade, gradeList, lesson, limit).doFinally {
            isDataLoading.onNext(false)
        }
    }

    // Search for exams
    fun searchExams(
        grade: String, search: String, lesson: String?, gradeList: String
    ): Single<ExamsMainResult> {
        // Determines whether something is being received from the server or not
        isDataLoading.onNext(true)

        // Getting the list of exams with the help of grade and limit
        return examRepository.searchExams(grade, search, lesson, gradeList).doFinally {
            isDataLoading.onNext(false)
        }
    }
}