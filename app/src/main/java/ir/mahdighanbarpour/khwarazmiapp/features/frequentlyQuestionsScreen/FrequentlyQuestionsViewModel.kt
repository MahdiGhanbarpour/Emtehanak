package ir.mahdighanbarpour.khwarazmiapp.features.frequentlyQuestionsScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.khwarazmiapp.model.data.FrequentlyQuestions
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.FrequentlyQuestionsRepository

class FrequentlyQuestionsViewModel(private val frequentlyQuestionsRepository: FrequentlyQuestionsRepository) :
    ViewModel() {

    fun getFrequentlyQuestions(page: String): Single<FrequentlyQuestions> {
        return frequentlyQuestionsRepository.getFrequentlyQuestions(page)
    }

    fun insertAllFrequentlyQuestions(frequentlyQuestions: FrequentlyQuestions) {
        frequentlyQuestionsRepository.insertAllFrequentlyQuestions(frequentlyQuestions)
    }
}