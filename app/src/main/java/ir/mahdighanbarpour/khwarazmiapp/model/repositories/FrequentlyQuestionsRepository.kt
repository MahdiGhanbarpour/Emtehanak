package ir.mahdighanbarpour.khwarazmiapp.model.repositories

import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.khwarazmiapp.model.data.FrequentlyQuestions
import ir.mahdighanbarpour.khwarazmiapp.model.dataBase.FrequentlyQuestionsDao

class FrequentlyQuestionsRepository(private val frequentlyQuestionsDao: FrequentlyQuestionsDao) {

    fun getFrequentlyQuestions(page: String): Single<FrequentlyQuestions> {
        return frequentlyQuestionsDao.getFrequentlyQuestions(page)
    }

    fun insertAllFrequentlyQuestions(frequentlyQuestions: FrequentlyQuestions) {
        frequentlyQuestionsDao.insertAllFrequentlyQuestions(frequentlyQuestions)
    }
}