package ir.mahdighanbarpour.khwarazmiapp.model.repositories

import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.khwarazmiapp.model.data.FrequentlyQuestion
import ir.mahdighanbarpour.khwarazmiapp.model.data.FrequentlyQuestionsMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.dataBase.FrequentlyQuestionsDao
import ir.mahdighanbarpour.khwarazmiapp.model.net.ApiService

class FrequentlyQuestionsRepository(
    private val frequentlyQuestionsDao: FrequentlyQuestionsDao, private val apiService: ApiService
) {

    // Getting the data of the frequently asked questions of the posted page from the local database
    fun getFrequentlyQuestionsLocal(page: String): Single<List<FrequentlyQuestion>> {
        return frequentlyQuestionsDao.getFrequentlyQuestions(page)
    }

    // Getting the data of the frequently asked questions of the posted page from server
    fun getFrequentlyQuestionsOnline(page: String): Single<FrequentlyQuestionsMainResult> {
        return apiService.getFrequentlyQuestions(page)
    }

    // Inserting the data of frequently asked questions of the posted page in the local database
    fun insertAllFrequentlyQuestions(frequentlyQuestions: List<FrequentlyQuestion>) {
        frequentlyQuestionsDao.insertAllFrequentlyQuestions(frequentlyQuestions)
    }
}