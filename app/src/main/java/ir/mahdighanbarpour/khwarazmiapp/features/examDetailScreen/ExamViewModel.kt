package ir.mahdighanbarpour.khwarazmiapp.features.examDetailScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ir.mahdighanbarpour.khwarazmiapp.model.data.QuestionMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.ExamRepository

class ExamViewModel(private val examRepository: ExamRepository) : ViewModel() {

    // Determines whether something is being received from the server or not
    val isDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()

    // Getting the list of exam questions with the help of exam id
    fun getExamsQuestion(examId: Int): Single<QuestionMainResult> {
        isDataLoading.onNext(true)

        return examRepository.getExamsQuestion(examId).doFinally {
            isDataLoading.onNext(false)
        }
    }
}