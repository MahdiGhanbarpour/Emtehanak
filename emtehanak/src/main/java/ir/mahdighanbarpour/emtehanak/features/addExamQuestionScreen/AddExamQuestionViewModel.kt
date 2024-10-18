package ir.mahdighanbarpour.emtehanak.features.addExamQuestionScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ir.mahdighanbarpour.emtehanak.model.data.AddExamMainResult
import ir.mahdighanbarpour.emtehanak.model.repositories.ExamRepository
import okhttp3.MultipartBody

class AddExamQuestionViewModel(private val examRepository: ExamRepository) : ViewModel() {
    // BehaviorSubject to track the loading state
    val isDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()

    // Adding the questions to the server
    fun addExamWithQuestions(parts: List<MultipartBody.Part>): Single<AddExamMainResult> {
        // Set the loading state to true
        isDataLoading.onNext(true)

        // Add the questions to the server
        return examRepository.addExamWithQuestions(parts).doFinally {
            isDataLoading.onNext(false)
        }
    }
}