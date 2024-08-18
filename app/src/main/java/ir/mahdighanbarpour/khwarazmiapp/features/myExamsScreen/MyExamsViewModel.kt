package ir.mahdighanbarpour.khwarazmiapp.features.myExamsScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamsMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.ExamRepository

class MyExamsViewModel(private val examRepository: ExamRepository) : ViewModel() {
    // Determines whether something is being received from the server or not
    val isDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()

    // Getting the list of teachers exams with the help of phone number
    fun getTeachersExams(phoneNumber: String, search: String): Single<ExamsMainResult> {
        // Determines whether something is being received from the server or not
        isDataLoading.onNext(true)

        // Getting the list of teachers exams with the help of phone number
        return examRepository.getTeachersExams(phoneNumber, search).doFinally {
            isDataLoading.onNext(false)
        }
    }

    // Deleting the exam with the help of id
    fun deleteExam(id: Int): Single<ExamsMainResult> {
        // Determines whether something is being received from the server or not
        isDataLoading.onNext(true)

        // Deleting the exam with the help of id
        return examRepository.deleteExam(id).doFinally {
            isDataLoading.onNext(false)
        }
    }

    // Changing the visibility of exam with the help of id
    fun changeVisibility(examId: Int, visibility: String): Single<ExamsMainResult> {
        // Determines whether something is being received from the server or not
        isDataLoading.onNext(true)

        // Changing the visibility of exam with the help of id
        return examRepository.changeVisibility(examId, visibility).doFinally {
            isDataLoading.onNext(false)
        }
    }
}