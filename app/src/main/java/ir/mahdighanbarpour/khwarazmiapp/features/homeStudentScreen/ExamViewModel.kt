package ir.mahdighanbarpour.khwarazmiapp.features.homeStudentScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamsMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.ExamRepository

class ExamViewModel(private val examRepository: ExamRepository) : ViewModel() {
    val isDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()

    fun getExams(grade: String): Single<ExamsMainResult> {
        return examRepository.getExams(grade)
    }

    fun getPopularExams(grade: String): Single<ExamsMainResult> {
        return examRepository.getPopularExams(grade)
    }
}