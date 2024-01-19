package ir.mahdighanbarpour.khwarazmiapp.model.repositories

import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.khwarazmiapp.model.data.FrequentlyQuestions
import ir.mahdighanbarpour.khwarazmiapp.model.dataBase.FrequentlyQuestionsDao

class FrequentlyQuestionsRepository(private val frequentlyQuestionsDao: FrequentlyQuestionsDao) {

    // Getting the data of the frequently asked questions of the posted page from the local database
    fun getFrequentlyQuestions(page: String): Single<FrequentlyQuestions> {
        return frequentlyQuestionsDao.getFrequentlyQuestions(page)
    }

    // Inserting the data of frequently asked questions of the posted page in the local database
    fun insertAllFrequentlyQuestions(frequentlyQuestions: FrequentlyQuestions) {
        frequentlyQuestionsDao.insertAllFrequentlyQuestions(frequentlyQuestions)
    }
}