package ir.mahdighanbarpour.emtehanak.features.frequentlyQuestionsScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ir.mahdighanbarpour.emtehanak.model.data.FrequentlyQuestion
import ir.mahdighanbarpour.emtehanak.model.data.FrequentlyQuestionsMainResult
import ir.mahdighanbarpour.emtehanak.model.repositories.FrequentlyQuestionsRepository

class FrequentlyQuestionsViewModel(private val frequentlyQuestionsRepository: FrequentlyQuestionsRepository) :
    ViewModel() {

    // Determines whether something is being received from the server or not
    val isDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()

    // Getting the data of the frequently asked questions of the posted page from the local database
    fun getFrequentlyQuestionsLocal(page: String): Single<List<FrequentlyQuestion>> {
        return frequentlyQuestionsRepository.getFrequentlyQuestionsLocal(page)
    }

    // Getting the data of the frequently asked questions of the posted page from server
    fun getFrequentlyQuestionsOnline(page: String): Single<FrequentlyQuestionsMainResult> {
        // Determines whether something is being received from the server or not
        isDataLoading.onNext(true)

        // Getting the data of the frequently asked questions of the posted page from server
        return frequentlyQuestionsRepository.getFrequentlyQuestionsOnline(page).doFinally {
            isDataLoading.onNext(false)
        }
    }

    // Inserting the data of frequently asked questions of the posted page in the local database
    fun insertAllFrequentlyQuestions(frequentlyQuestions: List<FrequentlyQuestion>) {
        frequentlyQuestionsRepository.insertAllFrequentlyQuestions(frequentlyQuestions)
    }
}