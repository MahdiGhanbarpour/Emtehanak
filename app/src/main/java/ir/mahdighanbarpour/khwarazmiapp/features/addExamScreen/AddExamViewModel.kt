package ir.mahdighanbarpour.khwarazmiapp.features.addExamScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ir.mahdighanbarpour.khwarazmiapp.model.data.AddExamMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.ExamRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddExamViewModel(private val examRepository: ExamRepository) : ViewModel() {
    val isDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()

    fun addExam(data: RequestBody, image: MultipartBody.Part): Single<AddExamMainResult> {
        isDataLoading.onNext(true)

        return examRepository.addExam(data, image).doFinally {
            isDataLoading.onNext(false)
        }
    }
}