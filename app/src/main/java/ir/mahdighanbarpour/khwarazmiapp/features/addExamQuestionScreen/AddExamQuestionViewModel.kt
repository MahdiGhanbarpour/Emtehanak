package ir.mahdighanbarpour.khwarazmiapp.features.addExamQuestionScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ir.mahdighanbarpour.khwarazmiapp.model.data.AddExamMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.ExamRepository
import okhttp3.MultipartBody

class AddExamQuestionViewModel(private val examRepository: ExamRepository) : ViewModel() {
    val isDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()

    fun addExamWithQuestions(parts: List<MultipartBody.Part>): Single<AddExamMainResult> {
        isDataLoading.onNext(true)

        return examRepository.addExamWithQuestions(parts).doFinally {
            isDataLoading.onNext(false)
        }
    }
}