package ir.mahdighanbarpour.khwarazmiapp.features.frequentlyQuestionsScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.khwarazmiapp.model.data.FrequentlyQuestions
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.FrequentlyQuestionsRepository

class FrequentlyQuestionsViewModel(private val frequentlyQuestionsRepository: FrequentlyQuestionsRepository) :
    ViewModel() {

    // Getting the data of the frequently asked questions of the posted page from the local database
    fun getFrequentlyQuestions(page: String): Single<FrequentlyQuestions> {
        return frequentlyQuestionsRepository.getFrequentlyQuestions(page)
    }

    // Inserting the data of frequently asked questions of the posted page in the local database
    fun insertAllFrequentlyQuestions(frequentlyQuestions: FrequentlyQuestions) {
        frequentlyQuestionsRepository.insertAllFrequentlyQuestions(frequentlyQuestions)
    }
}