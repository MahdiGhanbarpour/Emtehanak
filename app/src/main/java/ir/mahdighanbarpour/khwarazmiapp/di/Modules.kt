package ir.mahdighanbarpour.khwarazmiapp.di

import ir.mahdighanbarpour.khwarazmiapp.features.frequentlyQuestionsScreen.FrequentlyQuestionsViewModel
import ir.mahdighanbarpour.khwarazmiapp.model.dataBase.MainDataBase
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.FrequentlyQuestionsRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// Adding repetitive classes in the app to dependency injection
val myModules = module {
    single { MainDataBase.getDataBase(get()) }
    single { get<MainDataBase>().mainDao }

    single { FrequentlyQuestionsRepository(get()) }

    viewModel { FrequentlyQuestionsViewModel(get()) }
}