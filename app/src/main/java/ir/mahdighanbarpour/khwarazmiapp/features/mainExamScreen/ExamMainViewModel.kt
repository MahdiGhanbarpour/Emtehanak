package ir.mahdighanbarpour.khwarazmiapp.features.mainExamScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.khwarazmiapp.model.data.ReportQuestionMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.ExamRepository

class ExamMainViewModel(private val examRepository: ExamRepository) : ViewModel() {

    // Report question
    fun reportQuestion(
        questionId: Int, reporterPhoneNum: String
    ): Single<ReportQuestionMainResult> {
        return examRepository.reportQuestion(questionId, reporterPhoneNum)
    }
}