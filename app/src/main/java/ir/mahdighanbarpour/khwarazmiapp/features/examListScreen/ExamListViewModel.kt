package ir.mahdighanbarpour.khwarazmiapp.features.examListScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamsMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.ExamRepository

class ExamListViewModel(private val examRepository: ExamRepository) : ViewModel() {
    val isDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()

    fun getExamList(grade: String, gradeList: String, limit: String): Single<ExamsMainResult> {
        isDataLoading.onNext(true)

        return examRepository.getExams(grade, gradeList, limit).doFinally {
            isDataLoading.onNext(false)
        }
    }

    fun searchExams(grade: String, search: String): Single<ExamsMainResult> {
        isDataLoading.onNext(true)

        return examRepository.searchExams(grade, search).doFinally {
            isDataLoading.onNext(false)
        }
    }
}