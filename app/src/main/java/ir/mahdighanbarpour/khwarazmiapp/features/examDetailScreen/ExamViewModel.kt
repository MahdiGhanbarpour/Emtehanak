package ir.mahdighanbarpour.khwarazmiapp.features.examDetailScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ir.mahdighanbarpour.khwarazmiapp.model.data.QuestionMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.ExamRepository

class ExamViewModel(private val examRepository: ExamRepository) : ViewModel() {
    val isDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()

    fun getExamsQuestion(examId: Int): Single<QuestionMainResult> {
        isDataLoading.onNext(true)

        return examRepository.getExamsQuestion(examId).doFinally {
            isDataLoading.onNext(false)
        }
    }
}